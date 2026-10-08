package com.guoche.teyvat_artifacts;

import com.mojang.logging.LogUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.fml.event.config.ModConfigEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public record ArtifactStat(String id, ResourceLocation attributeId, int operation,
                           List<Double> initialValues, double enhancement) {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<String> DEFAULT_DEFINITIONS = List.of(
            "flower_health,minecraft:generic.max_health,1,0.5,1,1.5,2,2.5,0.5",
            "feather_damage,teyvat_artifacts:damage_bonus,2,0.01,0.02,0.03,0.04,0.05,0.01",
            "damage_bonus,teyvat_artifacts:damage_bonus,2,0.01,0.02,0.03,0.04,0.07,0.01",
            "damage_reduction,teyvat_artifacts:damage_reduction,2,0.02,0.03,0.06,0.08,0.15,0.02",
            "max_health,minecraft:generic.max_health,1,1,2,3,4,7,1",
            "armor_ignore,teyvat_artifacts:armor_ignore,2,0.07,0.09,0.11,0.13,0.25,0.03",
            "attack_speed,minecraft:generic.attack_speed,2,0.02,0.04,0.06,0.08,0.15,0.02",
            "attack_range,minecraft:player.entity_interaction_range,1,0.25,0.5,0.75,1,1.75,0.25",
            "movement_speed,minecraft:generic.movement_speed,2,0.02,0.04,0.06,0.08,0.15,0.02",
            "crit_rate,apothic_attributes:crit_chance,1,0.02,0.04,0.06,0.08,0.15,0.03",
            "crit_damage,apothic_attributes:crit_damage,1,0.04,0.08,0.12,0.16,0.30,0.06",
            "luck,minecraft:generic.luck,1,1,2,3,4,7,1"
    );
    private static volatile Catalog cachedCatalog;

    public ArtifactStat {
        initialValues = List.copyOf(initialValues);
    }

    public static List<String> defaultDefinitions() {
        return DEFAULT_DEFINITIONS;
    }

    public static List<String> defaultPoolForSlot(ArtifactSlot slot) {
        return switch (slot) {
            case FLOWER -> List.of("flower_health");
            case FEATHER -> List.of("feather_damage");
            case SANDS -> List.of("damage_bonus", "damage_reduction", "max_health", "armor_ignore", "attack_speed");
            case GOBLET -> List.of("damage_bonus", "damage_reduction", "max_health", "armor_ignore", "attack_range", "movement_speed");
            case CIRCLET -> List.of("damage_bonus", "damage_reduction", "max_health", "armor_ignore", "crit_rate", "crit_damage", "luck");
        };
    }

    public double amountForStars(int stars, int enhancements) {
        int rank = ArtifactItemData.clampStars(stars);
        int used = Math.min(rank, Math.max(0, enhancements));
        return initialValues.get(rank - 1) + enhancement * used;
    }

    public Component displayName() {
        Holder<Attribute> attribute = ArtifactSubstat.resolve(attributeId);
        return attribute == null ? Component.literal(attributeId.toString())
                : Component.translatable(attribute.value().getDescriptionId());
    }

    public MutableComponent modifierComponent(double amount) {
        Holder<Attribute> attribute = ArtifactSubstat.resolve(attributeId);
        if (attribute == null) return Component.literal(attributeId + ": " + amount);
        return attribute.value().toComponent(new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "main_stat_message"),
                amount, modifierOperation()), TooltipFlag.NORMAL);
    }

    public AttributeModifier.Operation modifierOperation() {
        return switch (operation) {
            case 2 -> AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
            case 3 -> AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
            default -> AttributeModifier.Operation.ADD_VALUE;
        };
    }

    public static boolean isValidId(String id) {
        return id != null && id.matches("[a-z0-9_.-]+");
    }

    public static boolean isValidDefinition(String raw) {
        return parse(raw) != null;
    }

    static ArtifactStat parse(String raw) {
        if (raw == null) return null;
        String[] parts = raw.split(",", -1);
        if (parts.length != 9 || !isValidId(parts[0].trim())) return null;
        ResourceLocation attribute = ResourceLocation.tryParse(parts[1].trim());
        if (attribute == null || attribute.toString().equals("teyvat_artifacts:display_damage_bonus")) return null;
        try {
            int operation = Integer.parseInt(parts[2].trim());
            if (operation < 1 || operation > 3) return null;
            List<Double> initial = new ArrayList<>(5);
            double step = Double.parseDouble(parts[8].trim());
            if (!Double.isFinite(step)) return null;
            for (int i = 3; i < 8; i++) {
                double value = Double.parseDouble(parts[i].trim());
                if (!Double.isFinite(value) || !Double.isFinite(value + (i - 2) * step)) return null;
                initial.add(value);
            }
            return new ArtifactStat(parts[0].trim(), attribute, operation, initial, step);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static ArtifactStat byId(String id) {
        return id == null || id.isEmpty() ? null : catalog().definitions().get(id);
    }

    public static List<ArtifactStat> poolForSlot(ArtifactSlot slot) {
        return catalog().pools().getOrDefault(slot, List.of());
    }

    public static ArtifactStat rollForSlot(ArtifactSlot slot, RandomSource random) {
        List<ArtifactStat> pool = poolForSlot(slot);
        return pool.isEmpty() ? null : pool.get(random.nextInt(pool.size()));
    }

    public static boolean canRollOnSlot(ArtifactSlot slot, ArtifactStat stat) {
        return stat != null && poolForSlot(slot).stream().anyMatch(candidate -> candidate.id().equals(stat.id()));
    }

    public static void invalidateCache() {
        synchronized (ArtifactStat.class) {
            cachedCatalog = null;
        }
    }

    public static void onConfigLoading(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == TeyvatArtifactsConfig.SPEC) invalidateCache();
    }

    public static void onConfigReloading(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == TeyvatArtifactsConfig.SPEC) invalidateCache();
    }

    private static Catalog catalog() {
        Catalog result = cachedCatalog;
        if (result != null) return result;
        synchronized (ArtifactStat.class) {
            if (cachedCatalog != null) return cachedCatalog;
            List<? extends String> definitions;
            Map<ArtifactSlot, List<? extends String>> pools = new EnumMap<>(ArtifactSlot.class);
            try {
                definitions = TeyvatArtifactsConfig.MAIN_STAT_DEFINITIONS.get();
                for (ArtifactSlot slot : ArtifactSlot.values()) {
                    pools.put(slot, TeyvatArtifactsConfig.MAIN_STAT_POOLS.get(slot).get());
                }
            } catch (IllegalStateException configNotLoaded) {
                for (ArtifactSlot slot : ArtifactSlot.values()) pools.put(slot, defaultPoolForSlot(slot));
                return buildCatalog(DEFAULT_DEFINITIONS, pools, id -> ArtifactSubstat.resolve(id) != null);
            }
            result = buildCatalog(definitions, pools, id -> ArtifactSubstat.resolve(id) != null);
            cachedCatalog = result;
            return result;
        }
    }

    static record Catalog(Map<String, ArtifactStat> definitions, Map<ArtifactSlot, List<ArtifactStat>> pools) {
    }

    static Catalog buildCatalog(List<? extends String> rawDefinitions,
                                Map<ArtifactSlot, ? extends List<? extends String>> rawPools,
                                Predicate<ResourceLocation> available) {
        Map<String, ArtifactStat> definitions = new LinkedHashMap<>();
        for (String raw : rawDefinitions) {
            ArtifactStat stat = parse(raw);
            if (stat == null) {
                LOGGER.warn("Ignoring invalid artifact main stat definition: {}", raw);
            } else if (definitions.putIfAbsent(stat.id(), stat) != null) {
                LOGGER.warn("Ignoring duplicate artifact main stat ID: {}", stat.id());
            }
        }
        Map<ArtifactSlot, List<ArtifactStat>> pools = new EnumMap<>(ArtifactSlot.class);
        for (ArtifactSlot slot : ArtifactSlot.values()) {
            List<ArtifactStat> pool = new ArrayList<>();
            List<? extends String> ids = rawPools.get(slot);
            if (ids != null) {
                for (String id : ids) {
                    ArtifactStat stat = definitions.get(id);
                    if (stat == null || !available.test(stat.attributeId())) {
                        LOGGER.warn("Skipping unavailable artifact main stat {} in {} pool", id, slot.id());
                    } else {
                        pool.add(stat);
                    }
                }
            }
            pools.put(slot, List.copyOf(pool));
        }
        return new Catalog(Map.copyOf(definitions), Map.copyOf(pools));
    }
}
