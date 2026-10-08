package com.guoche.teyvat_artifacts;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ArtifactChestLootModifier extends LootModifier {
    public static final MapCodec<ArtifactChestLootModifier> CODEC =
            RecordCodecBuilder.mapCodec(instance -> codecStart(instance).apply(instance, ArtifactChestLootModifier::new));

    private static final List<ArtifactSlot> FULL_SET_SLOTS = List.of(
            ArtifactSlot.FLOWER,
            ArtifactSlot.FEATHER,
            ArtifactSlot.SANDS,
            ArtifactSlot.GOBLET,
            ArtifactSlot.CIRCLET
    );
    private static final List<ArtifactSlot> CIRCLET_ONLY = List.of(ArtifactSlot.CIRCLET);
    private static final List<ArtifactSet> COMMON_SETS = List.of(
            ArtifactSet.LUCKY,
            ArtifactSet.ADVENTURER,
            ArtifactSet.TRAVELING_DOCTOR,
            ArtifactSet.RESOLUTION_OF_SOJOURNER,
            ArtifactSet.TINY_MIRACLE,
            ArtifactSet.BERSERKER,
            ArtifactSet.INSTRUCTOR,
            ArtifactSet.THE_EXILE,
            ArtifactSet.DEFENDERS_WILL,
            ArtifactSet.BRAVE_HEART,
            ArtifactSet.MARTIAL_ARTIST,
            ArtifactSet.GAMBLER,
            ArtifactSet.SCHOLAR
    );
    private static final List<ArtifactSet> PRAYER_SETS = List.of(
            ArtifactSet.PRAYERS_FOR_ILLUMINATION,
            ArtifactSet.PRAYERS_FOR_THUNDER,
            ArtifactSet.PRAYERS_FOR_DESTINY,
            ArtifactSet.PRAYERS_TO_SPRINGTIME
    );
    private static final List<ArtifactSet> RARE_SETS = List.of(
            ArtifactSet.GLADIATORS_FINALE,
            ArtifactSet.WANDERERS_TROUPE
    );

    public static boolean isChestLootSet(ArtifactSet set) {
        return COMMON_SETS.contains(set) || PRAYER_SETS.contains(set) || RARE_SETS.contains(set);
    }
    private static final Map<ResourceLocation, ChestTier> WORLD_PRIMOGEM_CHEST_TIERS = Map.ofEntries(
            chestTier("abandoned_mineshaft", ChestTier.LOW),
            chestTier("ancient_city", ChestTier.HIGH),
            chestTier("ancient_city_ice_box", ChestTier.HIGH),
            chestTier("bastion_bridge", ChestTier.HIGH),
            chestTier("bastion_hoglin_stable", ChestTier.HIGH),
            chestTier("bastion_other", ChestTier.HIGH),
            chestTier("bastion_treasure", ChestTier.HIGH),
            chestTier("buried_treasure", ChestTier.HIGH),
            chestTier("desert_pyramid", ChestTier.MID),
            chestTier("end_city_treasure", ChestTier.HIGH),
            chestTier("igloo_chest", ChestTier.LOW),
            chestTier("jungle_temple", ChestTier.MID),
            chestTier("nether_bridge", ChestTier.MID),
            chestTier("pillager_outpost", ChestTier.MID),
            chestTier("ruined_portal", ChestTier.LOW),
            chestTier("shipwreck_map", ChestTier.LOW),
            chestTier("shipwreck_supply", ChestTier.LOW),
            chestTier("shipwreck_treasure", ChestTier.MID),
            chestTier("simple_dungeon", ChestTier.LOW),
            chestTier("spawn_bonus_chest", ChestTier.LOW),
            chestTier("stronghold_corridor", ChestTier.MID),
            chestTier("stronghold_crossing", ChestTier.MID),
            chestTier("stronghold_library", ChestTier.MID),
            chestTier("trial_chambers/corridor", ChestTier.MID),
            chestTier("trial_chambers/entrance", ChestTier.MID),
            chestTier("trial_chambers/intersection", ChestTier.MID),
            chestTier("trial_chambers/intersection_barrel", ChestTier.MID),
            chestTier("trial_chambers/reward", ChestTier.HIGH),
            chestTier("trial_chambers/reward_common", ChestTier.MID),
            chestTier("trial_chambers/reward_ominous", ChestTier.HIGH),
            chestTier("trial_chambers/reward_ominous_common", ChestTier.HIGH),
            chestTier("trial_chambers/reward_ominous_rare", ChestTier.HIGH),
            chestTier("trial_chambers/reward_ominous_unique", ChestTier.HIGH),
            chestTier("trial_chambers/reward_rare", ChestTier.HIGH),
            chestTier("trial_chambers/reward_unique", ChestTier.HIGH),
            chestTier("trial_chambers/supply", ChestTier.MID),
            chestTier("underwater_ruin_big", ChestTier.MID),
            chestTier("underwater_ruin_small", ChestTier.MID),
            chestTier("village/village_armorer", ChestTier.LOW),
            chestTier("village/village_butcher", ChestTier.LOW),
            chestTier("village/village_cartographer", ChestTier.LOW),
            chestTier("village/village_desert_house", ChestTier.LOW),
            chestTier("village/village_fisher", ChestTier.LOW),
            chestTier("village/village_fletcher", ChestTier.LOW),
            chestTier("village/village_mason", ChestTier.LOW),
            chestTier("village/village_plains_house", ChestTier.LOW),
            chestTier("village/village_savanna_house", ChestTier.LOW),
            chestTier("village/village_shepherd", ChestTier.LOW),
            chestTier("village/village_snowy_house", ChestTier.LOW),
            chestTier("village/village_taiga_house", ChestTier.LOW),
            chestTier("village/village_tannery", ChestTier.LOW),
            chestTier("village/village_temple", ChestTier.LOW),
            chestTier("village/village_toolsmith", ChestTier.LOW),
            chestTier("village/village_weaponsmith", ChestTier.MID),
            chestTier("woodland_mansion", ChestTier.MID)
    );
    private static final Map<ResourceLocation, ChestTier> PRIMOGEM_CHEST_TIERS = chestTiersWithExtras(
            WORLD_PRIMOGEM_CHEST_TIERS,
            modChestTier("leyline_ruined_portal", ChestTier.MID)
    );
    private static final Map<ResourceLocation, ChestTier> CHEST_TIERS = Map.copyOf(PRIMOGEM_CHEST_TIERS);

    protected ArtifactChestLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        WitchGiftProgress.tryAddEndChestDictionaryLoot(generatedLoot, context);

        ChestTier tier = CHEST_TIERS.get(context.getQueriedLootTableId());
        if (tier == null) {
            return generatedLoot;
        }

        RandomSource random = context.getRandom();
        rollPool(generatedLoot, COMMON_SETS, FULL_SET_SLOTS, tier.commonChance, random);
        rollPool(generatedLoot, PRAYER_SETS, CIRCLET_ONLY, tier.prayerChance, random);
        ChestTier resinTier = PRIMOGEM_CHEST_TIERS.get(context.getQueriedLootTableId());
        if (resinTier != null) {
            rollCondensedResin(generatedLoot, resinTier.rareChance, random);
        }
        ChestTier primogemTier = PRIMOGEM_CHEST_TIERS.get(context.getQueriedLootTableId());
        if (primogemTier != null) {
            rollPool(generatedLoot, RARE_SETS, FULL_SET_SLOTS, primogemTier.rareChance, random);
        }
        return generatedLoot;
    }

    static ItemStack createRolledStack(ArtifactSet set, ArtifactSlot slot, RandomSource random, int forcedStars) {
        Item item = BuiltInRegistries.ITEM.get(artifactItemId(set, slot));
        if (!(item instanceof ArtifactItem artifact)) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = new ItemStack(item);
        if (forcedStars > 0) {
            ArtifactItemData.setStars(stack, forcedStars);
        }
        ArtifactItemData.ensureStars(stack, artifact, random);
        return stack;
    }

    private static void rollPool(ObjectArrayList<ItemStack> generatedLoot, List<ArtifactSet> sets, List<ArtifactSlot> slots, double chance, RandomSource random) {
        if (sets.isEmpty() || slots.isEmpty() || random.nextDouble() >= chance) {
            return;
        }

        ArtifactSet set = sets.get(random.nextInt(sets.size()));
        ArtifactSlot slot = slots.get(random.nextInt(slots.size()));
        ItemStack stack = createRolledStack(set, slot, random, 0);
        if (!stack.isEmpty()) {
            generatedLoot.add(stack);
        }
    }

    private static void rollCondensedResin(ObjectArrayList<ItemStack> generatedLoot, double chance, RandomSource random) {
        if (random.nextDouble() < chance) {
            generatedLoot.add(new ItemStack(TeyvatArtifacts.CONDENSED_RESIN.get()));
        }
    }

    private static ResourceLocation artifactItemId(ArtifactSet set, ArtifactSlot slot) {
        return ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, set.id() + "_" + slot.id());
    }

    private static ResourceLocation chest(String path) {
        return ResourceLocation.fromNamespaceAndPath("minecraft", "chests/" + path);
    }

    private static Map.Entry<ResourceLocation, ChestTier> chestTier(String path, ChestTier tier) {
        return Map.entry(chest(path), tier);
    }

    private static Map.Entry<ResourceLocation, ChestTier> modChestTier(String path, ChestTier tier) {
        return Map.entry(ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "chests/" + path), tier);
    }

    @SafeVarargs
    private static Map<ResourceLocation, ChestTier> chestTiersWithExtras(Map<ResourceLocation, ChestTier> base, Map.Entry<ResourceLocation, ChestTier>... extras) {
        Map<ResourceLocation, ChestTier> tiers = new LinkedHashMap<>(base);
        for (Map.Entry<ResourceLocation, ChestTier> extra : extras) {
            tiers.put(extra.getKey(), extra.getValue());
        }
        return Map.copyOf(tiers);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return TeyvatArtifacts.ARTIFACT_CHEST_LOOT_MODIFIER.get();
    }

    private enum ChestTier {
        LOW(0.75D, 0.375D, 0.10D),
        MID(0.60D, 0.30D, 0.30D),
        HIGH(0.10D, 0.05D, 0.50D);

        private final double commonChance;
        private final double prayerChance;
        private final double rareChance;

        ChestTier(double commonChance, double prayerChance, double rareChance) {
            this.commonChance = commonChance;
            this.prayerChance = prayerChance;
            this.rareChance = rareChance;
        }
    }
}
