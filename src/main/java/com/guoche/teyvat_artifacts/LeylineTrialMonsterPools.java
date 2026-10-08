package com.guoche.teyvat_artifacts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

final class LeylineTrialMonsterPools {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String BUNDLED_DIRECTORY = "/defaultconfigs/teyvat_artifacts/leyline_monster_pools/";
    private static final Path CONFIG_DIRECTORY = FMLPaths.CONFIGDIR.get()
            .resolve(TeyvatArtifacts.MODID)
            .resolve("leyline_monster_pools");
    private static final int MAX_DRAWS_PER_WAVE = 64;
    private static final int MAX_ENTRY_COUNT = 32;
    private static final int MAX_ENTITIES_PER_WAVE = 128;
    private static final int MAX_PASSENGER_DEPTH = 8;
    private static final String LEGACY_DEFAULT_HASH = "41c03af87885cf1b598249d2e5ecbbdb5a672384f0c6817097e41f6ad9af74a0";
    private static final Set<String> WARNED_MISSING_POOLS = new HashSet<>();
    private static final Set<String> WARNED_MISSING_ENTITIES = new HashSet<>();
    private static volatile Map<String, MonsterPool> loadedPools;

    private LeylineTrialMonsterPools() {
    }

    static void initialize() {
        ensureConfigFiles();
        pools();
    }

    static void ensureConfigFiles() {
        try {
            Files.createDirectories(CONFIG_DIRECTORY);
            copyBundledFileIfMissing("default.json");
            copyBundledFileIfMissing("example.json");
            upgradeUnmodifiedDefault();
        } catch (IOException exception) {
            LOGGER.error("Could not create Leyline Spawner monster pool config files", exception);
        }
    }

    static SpawnResult spawnWave(ServerLevel level, BlockPos origin, BlockState state, int spawnRadius, int maxSpawnAttempts) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        String spawnerId = blockId == null ? "" : blockId.getPath();
        String configuredPool = TeyvatArtifactsConfig.leylineMonsterPoolForSpawner(spawnerId);
        MonsterPool pool = resolvePool(configuredPool);
        SelectedWave selectedWave = selectWave(pool, level.random);
        if (selectedWave == null) {
            selectedWave = selectWave(bundledDefault(), level.random);
        }
        if (selectedWave == null) {
            return SpawnResult.EMPTY;
        }

        WaveDefinition wave = selectedWave.wave();
        List<MonsterEntry> entries = selectedWave.entries();
        int draws = clamp(wave.draws == null ? 1 : wave.draws, 1, MAX_DRAWS_PER_WAVE);
        List<MonsterEntry> chosen = new ArrayList<>();
        for (int draw = 0; draw < draws && chosen.size() < MAX_ENTITIES_PER_WAVE; draw++) {
            MonsterEntry entry = Boolean.TRUE.equals(wave.fixed) ? entries.get(draw % entries.size()) : pickWeighted(entries, level.random);
            if (entry == null) {
                continue;
            }
            int count = clamp(entry.count == null ? 1 : entry.count, 1, MAX_ENTRY_COUNT);
            for (int copy = 0; copy < count && chosen.size() < MAX_ENTITIES_PER_WAVE; copy++) {
                chosen.add(entry);
            }
        }
        return spawnGroup(level, origin, chosen, spawnRadius, maxSpawnAttempts);
    }

    private static SpawnResult spawnGroup(ServerLevel level, BlockPos origin, List<MonsterEntry> entries, int spawnRadius, int maxSpawnAttempts) {
        if (entries.isEmpty()) {
            return SpawnResult.EMPTY;
        }
        RandomSource random = level.random;
        for (int attempt = 0; attempt < Math.max(1, maxSpawnAttempts); attempt++) {
            SpawnSpot spot = findSpawnSpot(level, origin, spawnRadius, random);
            if (spot == null) {
                continue;
            }
            List<List<Mob>> groups = new ArrayList<>();
            int[] remainingEntities = {MAX_ENTITIES_PER_WAVE};
            boolean valid = true;
            for (MonsterEntry entry : entries) {
                MobNode root = createMobTree(level, entry, spot.x(), spot.y(), spot.z(), random.nextFloat() * 360.0F, 0, remainingEntities);
                if (root == null) {
                    valid = false;
                    break;
                }
                List<Mob> graph = new ArrayList<>();
                flatten(root, graph);
                for (Mob mob : graph) {
                    if (!mob.checkSpawnObstruction(level)
                            || !level.noCollision(mob, mob.getBoundingBox().inflate(0.5D, 0.0D, 0.5D))) {
                        valid = false;
                        break;
                    }
                }
                if (!valid) {
                    break;
                }
                groups.add(graph);
            }
            if (!valid) {
                continue;
            }
            List<UUID> added = new ArrayList<>();
            for (List<Mob> graph : groups) {
                for (Mob mob : graph) {
                    mob.setPersistenceRequired();
                    LeylineTrialDrops.mark(mob);
                }
                List<UUID> groupIds = addGraph(level, graph);
                if (groupIds.isEmpty()) {
                    valid = false;
                    break;
                }
                added.addAll(groupIds);
                level.levelEvent(3011, origin, 0);
                level.levelEvent(3012, spot.pos(), 0);
                level.gameEvent(graph.getFirst(), GameEvent.ENTITY_PLACE, spot.pos());
            }
            if (valid) {
                return new SpawnResult(true, List.copyOf(added));
            }
            for (UUID id : added) {
                Entity mob = level.getEntity(id);
                if (mob != null) {
                    mob.discard();
                }
            }
        }
        return SpawnResult.EMPTY;
    }

    private static SpawnSpot findSpawnSpot(ServerLevel level, BlockPos origin, int spawnRadius, RandomSource random) {
        double angle = random.nextDouble() * Math.PI * 2.0D;
        double distance = 3.0D + random.nextDouble() * Math.max(1, spawnRadius - 3);
        double x = origin.getX() + 0.5D + Math.cos(angle) * distance;
        double z = origin.getZ() + 0.5D + Math.sin(angle) * distance;
        BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlockPos.containing(x, 0.0D, z));
        BlockPos ground = pos.below();
        if (Math.abs(pos.getY() - origin.getY()) > 5
                || !level.getBlockState(ground).isFaceSturdy(level, ground, Direction.UP)
                || !level.getFluidState(pos).isEmpty()
                || !level.getFluidState(ground).isEmpty()) {
            return null;
        }
        return new SpawnSpot(x, pos.getY(), z, pos);
    }

    static void recoverTrackedMobs(ServerLevel level, BlockPos origin, List<UUID> mobIds, int spawnRadius) {
        for (UUID id : mobIds) {
            Entity entity = level.getEntity(id);
            if (!(entity instanceof Mob mob) || mob.getVehicle() != null) {
                continue;
            }
            boolean stranded = mob.distanceToSqr(origin.getX() + 0.5D, origin.getY() + 0.5D, origin.getZ() + 0.5D) > 144.0D
                    || mob.getY() < origin.getY() - 5 || mob.isInWall() || mob.isInLava();
            CompoundTag data = mob.getPersistentData();
            int ticks = stranded ? data.getInt("teyvat_artifacts.leyline_stranded_ticks") + 1 : 0;
            data.putInt("teyvat_artifacts.leyline_stranded_ticks", ticks);
            int recoveryDelay = mob.getY() < origin.getY() - 10 ? 1 : mob.isInWall() ? 10 : 60;
            if (ticks < recoveryDelay) {
                continue;
            }
            for (int attempt = 0; attempt < 20; attempt++) {
                SpawnSpot spot = findSpawnSpot(level, origin, spawnRadius, level.random);
                if (spot == null) {
                    continue;
                }
                if (level.noCollision(mob, mob.getBoundingBox().move(
                        spot.x() - mob.getX(), spot.y() - mob.getY(), spot.z() - mob.getZ()))) {
                    mob.teleportTo(spot.x(), spot.y(), spot.z());
                    mob.setDeltaMovement(0.0D, 0.0D, 0.0D);
                    mob.fallDistance = 0.0F;
                    data.putInt("teyvat_artifacts.leyline_stranded_ticks", 0);
                    break;
                }
            }
        }
    }

    private static MobNode createMobTree(
            ServerLevel level,
            MonsterEntry entry,
            double x,
            double y,
            double z,
            float yaw,
            int depth,
            int[] remainingEntities
    ) {
        if (entry == null || entry.entity == null || entry.entity.isBlank()
                || depth > MAX_PASSENGER_DEPTH || remainingEntities[0] <= 0) {
            return null;
        }

        ResourceLocation entityId = ResourceLocation.tryParse(entry.entity);
        EntityType<?> entityType = entityId == null ? null : BuiltInRegistries.ENTITY_TYPE.getOptional(entityId).orElse(null);
        Entity created = entityType == null ? null : entityType.create(level);
        if (!(created instanceof Mob mob)) {
            if (WARNED_MISSING_ENTITIES.add(entry.entity)) {
                LOGGER.warn("Skipping invalid Leyline trial mob '{}': the entity is missing or is not a Mob", entry.entity);
            }
            return null;
        }
        remainingEntities[0]--;

        mob.moveTo(x, y, z, yaw, 0.0F);
        if (entry.finalize_spawn == null || entry.finalize_spawn) {
            mob.finalizeSpawn(level, level.getCurrentDifficultyAt(BlockPos.containing(x, y, z)), MobSpawnType.TRIAL_SPAWNER, null);
        }
        applyExtraNbt(mob, entry.nbt);
        mob.moveTo(x, y, z, yaw, 0.0F);
        applyEquipment(mob, entry.equipment);
        applyEnchantments(level, mob, entry.enchantments);
        applyAttributes(mob, entry);
        if (entry.custom_name != null && !entry.custom_name.isBlank()) {
            mob.setCustomName(Component.literal(entry.custom_name));
        }

        MobNode node = new MobNode(mob);
        if (entry.passengers != null) {
            for (MonsterEntry passengerEntry : entry.passengers) {
                MobNode passenger = createMobTree(level, passengerEntry, x, y, z, yaw, depth + 1, remainingEntities);
                if (passenger != null && passenger.mob.startRiding(mob, true)) {
                    node.passengers.add(passenger);
                }
            }
        }
        return node;
    }

    private static void applyExtraNbt(Mob mob, String snbt) {
        if (snbt == null || snbt.isBlank()) {
            return;
        }
        try {
            CompoundTag tag = TagParser.parseTag(snbt);
            tag.remove("UUID");
            tag.remove("UUIDMost");
            tag.remove("UUIDLeast");
            tag.remove("Pos");
            tag.remove("Motion");
            tag.remove("Rotation");
            tag.remove("Passengers");
            mob.load(tag);
        } catch (CommandSyntaxException | RuntimeException exception) {
            LOGGER.warn("Ignoring invalid Leyline trial mob SNBT: {}", snbt, exception);
        }
    }

    private static void applyEquipment(Mob mob, Map<String, String> equipment) {
        if (equipment == null || equipment.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : equipment.entrySet()) {
            EquipmentSlot slot = equipmentSlot(entry.getKey());
            ResourceLocation itemId = ResourceLocation.tryParse(entry.getValue());
            Item item = itemId == null ? null : BuiltInRegistries.ITEM.getOptional(itemId).orElse(null);
            if (slot != null && item != null) {
                mob.setItemSlot(slot, new ItemStack(item));
            }
        }
    }

    private static void applyEnchantments(ServerLevel level, Mob mob, Map<String, Map<String, Integer>> enchantments) {
        if (enchantments == null) {
            return;
        }
        for (Map.Entry<String, Map<String, Integer>> slotEntry : enchantments.entrySet()) {
            EquipmentSlot slot = equipmentSlot(slotEntry.getKey());
            if (slot == null || slotEntry.getValue() == null) {
                continue;
            }
            ItemStack stack = mob.getItemBySlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            for (Map.Entry<String, Integer> enchantment : slotEntry.getValue().entrySet()) {
                ResourceLocation id = ResourceLocation.tryParse(enchantment.getKey());
                if (id != null && enchantment.getValue() != null && enchantment.getValue() > 0) {
                    level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                            .get(ResourceKey.create(Registries.ENCHANTMENT, id))
                            .ifPresent(holder -> stack.enchant(holder, enchantment.getValue()));
                }
            }
        }
    }

    private static void applyAttributes(Mob mob, MonsterEntry entry) {
        setBase(mob.getAttribute(Attributes.MAX_HEALTH), entry.max_health);
        setBase(mob.getAttribute(Attributes.ATTACK_DAMAGE), entry.attack_damage);
        if (entry.total_armor != null && Double.isFinite(entry.total_armor) && entry.total_armor >= 0.0D) {
            AttributeInstance armor = mob.getAttribute(Attributes.ARMOR);
            if (armor != null) {
                armor.setBaseValue(armor.getBaseValue() + entry.total_armor - mob.getArmorValue());
            }
        }
        if (entry.speed_multiplier != null && Double.isFinite(entry.speed_multiplier) && entry.speed_multiplier > 0.0D) {
            AttributeInstance speed = mob.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speed != null) {
                speed.setBaseValue(speed.getBaseValue() * entry.speed_multiplier);
            }
        }
        if (entry.max_health != null) {
            mob.setHealth(mob.getMaxHealth());
        }
    }

    private static void setBase(AttributeInstance attribute, Double value) {
        if (attribute != null && value != null && Double.isFinite(value) && value > 0.0D) {
            attribute.setBaseValue(value);
        }
    }

    private static EquipmentSlot equipmentSlot(String name) {
        if (name == null) {
            return null;
        }
        return switch (name.toLowerCase(Locale.ROOT)) {
            case "mainhand", "main_hand" -> EquipmentSlot.MAINHAND;
            case "offhand", "off_hand" -> EquipmentSlot.OFFHAND;
            case "feet", "boots" -> EquipmentSlot.FEET;
            case "legs", "leggings" -> EquipmentSlot.LEGS;
            case "chest", "chestplate" -> EquipmentSlot.CHEST;
            case "head", "helmet" -> EquipmentSlot.HEAD;
            default -> null;
        };
    }

    private static List<UUID> addGraph(ServerLevel level, List<Mob> graph) {
        if (graph.isEmpty() || !level.tryAddFreshEntityWithPassengers(graph.getFirst())) {
            return List.of();
        }
        List<UUID> added = new ArrayList<>();
        for (Mob mob : graph) {
            added.add(mob.getUUID());
        }
        return added;
    }

    private static void flatten(MobNode node, List<Mob> graph) {
        graph.add(node.mob);
        for (MobNode passenger : node.passengers) {
            flatten(passenger, graph);
        }
    }

    private static MonsterEntry pickWeighted(List<MonsterEntry> entries, RandomSource random) {
        int totalWeight = 0;
        for (MonsterEntry entry : entries) {
            totalWeight += clamp(entry.weight == null ? 1 : entry.weight, 1, 1_000_000);
        }
        if (totalWeight <= 0) {
            return null;
        }
        int roll = random.nextInt(totalWeight);
        for (MonsterEntry entry : entries) {
            roll -= clamp(entry.weight == null ? 1 : entry.weight, 1, 1_000_000);
            if (roll < 0) {
                return entry;
            }
        }
        return entries.getLast();
    }

    private static SelectedWave selectWave(MonsterPool pool, RandomSource random) {
        List<MonsterEntry> normalEntries = registeredEntries(pool.normal);
        List<MonsterEntry> eliteEntries = registeredEntries(pool.elite);
        int normalWeight = normalEntries.isEmpty()
                ? 0
                : clamp(pool.normal_wave_weight == null ? 1 : pool.normal_wave_weight, 0, 1_000_000);
        int eliteWeight = eliteEntries.isEmpty()
                ? 0
                : clamp(pool.elite_wave_weight == null ? 1 : pool.elite_wave_weight, 0, 1_000_000);
        int totalWeight = normalWeight + eliteWeight;
        if (totalWeight <= 0) {
            return null;
        }
        return random.nextInt(totalWeight) < normalWeight
                ? new SelectedWave(pool.normal, normalEntries)
                : new SelectedWave(pool.elite, eliteEntries);
    }

    private static List<MonsterEntry> registeredEntries(WaveDefinition wave) {
        if (wave == null) {
            return List.of();
        }
        List<MonsterEntry> registered = new ArrayList<>();
        for (MonsterEntry entry : wave.validEntries()) {
            ResourceLocation id = ResourceLocation.tryParse(entry.entity);
            if (id != null && BuiltInRegistries.ENTITY_TYPE.getOptional(id).isPresent()) {
                registered.add(entry);
            } else if (WARNED_MISSING_ENTITIES.add(entry.entity)) {
                LOGGER.warn("Skipping missing Leyline trial entity '{}'", entry.entity);
            }
        }
        return registered;
    }

    private static MonsterPool resolvePool(String requestedName) {
        Map<String, MonsterPool> pools = pools();
        MonsterPool selected = pools.get(normalizePoolName(requestedName));
        if (selected != null) {
            return selected;
        }
        if (WARNED_MISSING_POOLS.add(requestedName)) {
            LOGGER.warn("Leyline trial monster pool '{}' does not exist; using default", requestedName);
        }
        MonsterPool fallback = pools.get("default");
        return fallback != null ? fallback : bundledDefault();
    }

    private static Map<String, MonsterPool> pools() {
        Map<String, MonsterPool> current = loadedPools;
        if (current != null) {
            return current;
        }
        synchronized (LeylineTrialMonsterPools.class) {
            if (loadedPools == null) {
                loadedPools = loadPools();
            }
            return loadedPools;
        }
    }

    private static Map<String, MonsterPool> loadPools() {
        ensureConfigFiles();
        Map<String, MonsterPool> pools = new LinkedHashMap<>();
        try (Stream<Path> files = Files.list(CONFIG_DIRECTORY)) {
            List<Path> jsonFiles = files
                    .filter(path -> path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".json"))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .toList();
            for (Path path : jsonFiles) {
                try (Reader reader = Files.newBufferedReader(path)) {
                    MonsterPool pool = GSON.fromJson(reader, MonsterPool.class);
                    if (pool != null && pool.isUsable()) {
                        String fileName = path.getFileName().toString();
                        pools.put(normalizePoolName(fileName.substring(0, fileName.length() - 5)), pool);
                    } else {
                        LOGGER.warn("Ignoring unusable Leyline trial monster pool file {}", path);
                    }
                } catch (IOException | RuntimeException exception) {
                    LOGGER.error("Could not read Leyline trial monster pool file {}", path, exception);
                }
            }
        } catch (IOException exception) {
            LOGGER.error("Could not list Leyline trial monster pool directory {}", CONFIG_DIRECTORY, exception);
        }

        pools.putIfAbsent("default", bundledDefault());
        LOGGER.info("Loaded {} Leyline trial monster pools", pools.size());
        return Map.copyOf(pools);
    }

    private static MonsterPool bundledDefault() {
        try (InputStream input = LeylineTrialMonsterPools.class.getResourceAsStream(BUNDLED_DIRECTORY + "default.json")) {
            if (input != null) {
                try (Reader reader = new java.io.InputStreamReader(input)) {
                    MonsterPool pool = GSON.fromJson(reader, MonsterPool.class);
                    if (pool != null && pool.isUsable()) {
                        return pool;
                    }
                }
            }
        } catch (IOException | RuntimeException exception) {
            LOGGER.error("Could not read bundled default Leyline trial monster pool", exception);
        }
        return MonsterPool.emergencyDefault();
    }

    private static void upgradeUnmodifiedDefault() throws IOException {
        Path target = CONFIG_DIRECTORY.resolve("default.json");
        if (!Files.exists(target)) {
            return;
        }
        try {
            String hash = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(target)));
            if (!LEGACY_DEFAULT_HASH.equals(hash)) {
                return;
            }
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
        try (InputStream input = LeylineTrialMonsterPools.class.getResourceAsStream(BUNDLED_DIRECTORY + "default.json")) {
            if (input != null) {
                Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
                LOGGER.info("Upgraded the unmodified Leyline trial default monster pool");
            }
        }
    }

    private static void copyBundledFileIfMissing(String fileName) throws IOException {
        Path target = CONFIG_DIRECTORY.resolve(fileName);
        if (Files.exists(target)) {
            return;
        }
        try (InputStream input = LeylineTrialMonsterPools.class.getResourceAsStream(BUNDLED_DIRECTORY + fileName)) {
            if (input == null) {
                throw new IOException("Missing bundled config template " + fileName);
            }
            Files.copy(input, target);
        }
    }

    private static String normalizePoolName(String name) {
        return name == null ? "default" : name.trim().toLowerCase(Locale.ROOT);
    }

    private static int clamp(int value, int minimum, int maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }

    record SpawnResult(boolean spawned, List<UUID> entityIds) {
        private static final SpawnResult EMPTY = new SpawnResult(false, List.of());
    }

    private record SelectedWave(WaveDefinition wave, List<MonsterEntry> entries) {
    }

    private static final class MobNode {
        private final Mob mob;
        private final List<MobNode> passengers = new ArrayList<>();

        private MobNode(Mob mob) {
            this.mob = mob;
        }
    }

    private static final class MonsterPool {
        private Integer normal_wave_weight;
        private Integer elite_wave_weight;
        private WaveDefinition normal;
        private WaveDefinition elite;

        private WaveDefinition chooseWave(RandomSource random) {
            int normalWeight = normal != null && !normal.validEntries().isEmpty()
                    ? Math.max(0, normal_wave_weight == null ? 1 : normal_wave_weight)
                    : 0;
            int eliteWeight = elite != null && !elite.validEntries().isEmpty()
                    ? Math.max(0, elite_wave_weight == null ? 1 : elite_wave_weight)
                    : 0;
            int total = normalWeight + eliteWeight;
            if (total <= 0) {
                return null;
            }
            return random.nextInt(total) < normalWeight ? normal : elite;
        }

        private boolean isUsable() {
            return (normal != null && !normal.validEntries().isEmpty())
                    || (elite != null && !elite.validEntries().isEmpty());
        }

        private static MonsterPool emergencyDefault() {
            MonsterEntry zombie = new MonsterEntry();
            zombie.entity = "minecraft:zombie";
            WaveDefinition wave = new WaveDefinition();
            wave.draws = 5;
            wave.entries = List.of(zombie);
            MonsterPool pool = new MonsterPool();
            pool.normal_wave_weight = 1;
            pool.elite_wave_weight = 0;
            pool.normal = wave;
            return pool;
        }
    }

    private static final class WaveDefinition {
        private Integer draws;
        private Boolean fixed;
        private List<MonsterEntry> entries;

        private List<MonsterEntry> validEntries() {
            if (entries == null || entries.isEmpty()) {
                return List.of();
            }
            return entries.stream()
                    .filter(entry -> entry != null && entry.entity != null && !entry.entity.isBlank())
                    .filter(entry -> entry.weight == null || entry.weight > 0)
                    .toList();
        }
    }

    private static final class MonsterEntry {
        private String entity;
        private Integer weight;
        private Integer count;
        private String custom_name;
        private Boolean finalize_spawn;
        private String nbt;
        private Map<String, String> equipment;
        private Map<String, Map<String, Integer>> enchantments;
        private Double max_health;
        private Double attack_damage;
        private Double total_armor;
        private Double speed_multiplier;
        private List<MonsterEntry> passengers;
    }

    private record SpawnSpot(double x, double y, double z, BlockPos pos) {
    }
}
