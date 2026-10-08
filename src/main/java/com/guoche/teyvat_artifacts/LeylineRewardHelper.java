package com.guoche.teyvat_artifacts;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class LeylineRewardHelper {
    private static final int MIN_REWARD_COUNT = 2;
    private static final int MAX_REWARD_COUNT = 3;
    private static final int TEN_LEVEL_XP_COST = experiencePointsForLevels(10);
    private static final float TEYVAT_DELIGHT_PRIMOGEM_CHANCE = 0.05F;
    private static final ResourceLocation TEYVAT_DELIGHT_MORA_ID = ResourceLocation.fromNamespaceAndPath("teyvatdelight", "mora");
    private static final ResourceLocation TEYVAT_DELIGHT_PRIMOGEM_ID = ResourceLocation.fromNamespaceAndPath("teyvatdelight", "primogem");
    private static final TagKey<Item> REWARD_DOUBLER_TAG = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "leyline_reward_doublers")
    );
    private static final List<Supplier<ArtifactItem>> MONDSTADT_REWARD_POOL = List.of(
            TeyvatArtifacts.THUNDERSOOTHER_FLOWER::get,
            TeyvatArtifacts.THUNDERSOOTHER_FEATHER::get,
            TeyvatArtifacts.THUNDERSOOTHER_SANDS::get,
            TeyvatArtifacts.THUNDERSOOTHER_GOBLET::get,
            TeyvatArtifacts.THUNDERSOOTHER_CIRCLET::get,
            TeyvatArtifacts.THUNDERING_FURY_FLOWER::get,
            TeyvatArtifacts.THUNDERING_FURY_FEATHER::get,
            TeyvatArtifacts.THUNDERING_FURY_SANDS::get,
            TeyvatArtifacts.THUNDERING_FURY_GOBLET::get,
            TeyvatArtifacts.THUNDERING_FURY_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> LIYUE_CLEAR_POOL_REWARD_POOL = List.of(
            TeyvatArtifacts.BLOODSTAINED_CHIVALRY_FLOWER::get,
            TeyvatArtifacts.BLOODSTAINED_CHIVALRY_FEATHER::get,
            TeyvatArtifacts.BLOODSTAINED_CHIVALRY_SANDS::get,
            TeyvatArtifacts.BLOODSTAINED_CHIVALRY_GOBLET::get,
            TeyvatArtifacts.BLOODSTAINED_CHIVALRY_CIRCLET::get,
            TeyvatArtifacts.NOBLESSE_OBLIGE_FLOWER::get,
            TeyvatArtifacts.NOBLESSE_OBLIGE_FEATHER::get,
            TeyvatArtifacts.NOBLESSE_OBLIGE_SANDS::get,
            TeyvatArtifacts.NOBLESSE_OBLIGE_GOBLET::get,
            TeyvatArtifacts.NOBLESSE_OBLIGE_CIRCLET::get
    );

    private static final List<Supplier<ArtifactItem>> MONDSTADT_RIDGE_WATCH_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.PALE_FLAME_FLOWER::get,
            TeyvatArtifacts.PALE_FLAME_FEATHER::get,
            TeyvatArtifacts.PALE_FLAME_SANDS::get,
            TeyvatArtifacts.PALE_FLAME_GOBLET::get,
            TeyvatArtifacts.PALE_FLAME_CIRCLET::get,
            TeyvatArtifacts.TENACITY_OF_THE_MILLELITH_FLOWER::get,
            TeyvatArtifacts.TENACITY_OF_THE_MILLELITH_FEATHER::get,
            TeyvatArtifacts.TENACITY_OF_THE_MILLELITH_SANDS::get,
            TeyvatArtifacts.TENACITY_OF_THE_MILLELITH_GOBLET::get,
            TeyvatArtifacts.TENACITY_OF_THE_MILLELITH_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> MONDSTADT_THORNY_CROWN_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.CELESTIAL_GIFT_FLOWER::get,
            TeyvatArtifacts.CELESTIAL_GIFT_FEATHER::get,
            TeyvatArtifacts.CELESTIAL_GIFT_SANDS::get,
            TeyvatArtifacts.CELESTIAL_GIFT_GOBLET::get,
            TeyvatArtifacts.CELESTIAL_GIFT_CIRCLET::get,
            TeyvatArtifacts.DISENCHANTMENT_IN_DEEP_SHADOW_FLOWER::get,
            TeyvatArtifacts.DISENCHANTMENT_IN_DEEP_SHADOW_FEATHER::get,
            TeyvatArtifacts.DISENCHANTMENT_IN_DEEP_SHADOW_SANDS::get,
            TeyvatArtifacts.DISENCHANTMENT_IN_DEEP_SHADOW_GOBLET::get,
            TeyvatArtifacts.DISENCHANTMENT_IN_DEEP_SHADOW_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> MONDSTADT_PEAK_VINDAGNYR_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.BLIZZARD_STRAYER_FLOWER::get,
            TeyvatArtifacts.BLIZZARD_STRAYER_FEATHER::get,
            TeyvatArtifacts.BLIZZARD_STRAYER_SANDS::get,
            TeyvatArtifacts.BLIZZARD_STRAYER_GOBLET::get,
            TeyvatArtifacts.BLIZZARD_STRAYER_CIRCLET::get,
            TeyvatArtifacts.HEART_OF_DEPTH_FLOWER::get,
            TeyvatArtifacts.HEART_OF_DEPTH_FEATHER::get,
            TeyvatArtifacts.HEART_OF_DEPTH_SANDS::get,
            TeyvatArtifacts.HEART_OF_DEPTH_GOBLET::get,
            TeyvatArtifacts.HEART_OF_DEPTH_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> MONDSTADT_VALLEY_REMEMBRANCE_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.MAIDEN_BELOVED_FLOWER::get,
            TeyvatArtifacts.MAIDEN_BELOVED_FEATHER::get,
            TeyvatArtifacts.MAIDEN_BELOVED_SANDS::get,
            TeyvatArtifacts.MAIDEN_BELOVED_GOBLET::get,
            TeyvatArtifacts.MAIDEN_BELOVED_CIRCLET::get,
            TeyvatArtifacts.VIRIDESCENT_VENERER_FLOWER::get,
            TeyvatArtifacts.VIRIDESCENT_VENERER_FEATHER::get,
            TeyvatArtifacts.VIRIDESCENT_VENERER_SANDS::get,
            TeyvatArtifacts.VIRIDESCENT_VENERER_GOBLET::get,
            TeyvatArtifacts.VIRIDESCENT_VENERER_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> LIYUE_DOMAIN_GUYUN_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.ARCHAIC_PETRA_FLOWER::get,
            TeyvatArtifacts.ARCHAIC_PETRA_FEATHER::get,
            TeyvatArtifacts.ARCHAIC_PETRA_SANDS::get,
            TeyvatArtifacts.ARCHAIC_PETRA_GOBLET::get,
            TeyvatArtifacts.ARCHAIC_PETRA_CIRCLET::get,
            TeyvatArtifacts.RETRACING_BOLIDE_FLOWER::get,
            TeyvatArtifacts.RETRACING_BOLIDE_FEATHER::get,
            TeyvatArtifacts.RETRACING_BOLIDE_SANDS::get,
            TeyvatArtifacts.RETRACING_BOLIDE_GOBLET::get,
            TeyvatArtifacts.RETRACING_BOLIDE_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> LIYUE_LOST_VALLEY_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.ECHOES_OF_AN_OFFERING_FLOWER::get,
            TeyvatArtifacts.ECHOES_OF_AN_OFFERING_FEATHER::get,
            TeyvatArtifacts.ECHOES_OF_AN_OFFERING_SANDS::get,
            TeyvatArtifacts.ECHOES_OF_AN_OFFERING_GOBLET::get,
            TeyvatArtifacts.ECHOES_OF_AN_OFFERING_CIRCLET::get,
            TeyvatArtifacts.VERMILLION_HEREAFTER_FLOWER::get,
            TeyvatArtifacts.VERMILLION_HEREAFTER_FEATHER::get,
            TeyvatArtifacts.VERMILLION_HEREAFTER_SANDS::get,
            TeyvatArtifacts.VERMILLION_HEREAFTER_GOBLET::get,
            TeyvatArtifacts.VERMILLION_HEREAFTER_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> LIYUE_ZHOU_FORMULA_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.CRIMSON_WITCH_OF_FLAMES_FLOWER::get,
            TeyvatArtifacts.CRIMSON_WITCH_OF_FLAMES_FEATHER::get,
            TeyvatArtifacts.CRIMSON_WITCH_OF_FLAMES_SANDS::get,
            TeyvatArtifacts.CRIMSON_WITCH_OF_FLAMES_GOBLET::get,
            TeyvatArtifacts.CRIMSON_WITCH_OF_FLAMES_CIRCLET::get,
            TeyvatArtifacts.LAVAWALKER_FLOWER::get,
            TeyvatArtifacts.LAVAWALKER_FEATHER::get,
            TeyvatArtifacts.LAVAWALKER_SANDS::get,
            TeyvatArtifacts.LAVAWALKER_GOBLET::get,
            TeyvatArtifacts.LAVAWALKER_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> INAZUMA_MOMIJI_DYED_COURT_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.EMBLEM_OF_SEVERED_FATE_FLOWER::get,
            TeyvatArtifacts.EMBLEM_OF_SEVERED_FATE_FEATHER::get,
            TeyvatArtifacts.EMBLEM_OF_SEVERED_FATE_SANDS::get,
            TeyvatArtifacts.EMBLEM_OF_SEVERED_FATE_GOBLET::get,
            TeyvatArtifacts.EMBLEM_OF_SEVERED_FATE_CIRCLET::get,
            TeyvatArtifacts.SHIMENAWAS_REMINISCENCE_FLOWER::get,
            TeyvatArtifacts.SHIMENAWAS_REMINISCENCE_FEATHER::get,
            TeyvatArtifacts.SHIMENAWAS_REMINISCENCE_SANDS::get,
            TeyvatArtifacts.SHIMENAWAS_REMINISCENCE_GOBLET::get,
            TeyvatArtifacts.SHIMENAWAS_REMINISCENCE_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> INAZUMA_SLUMBERING_COURT_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.HUSK_OF_OPULENT_DREAMS_FLOWER::get,
            TeyvatArtifacts.HUSK_OF_OPULENT_DREAMS_FEATHER::get,
            TeyvatArtifacts.HUSK_OF_OPULENT_DREAMS_SANDS::get,
            TeyvatArtifacts.HUSK_OF_OPULENT_DREAMS_GOBLET::get,
            TeyvatArtifacts.HUSK_OF_OPULENT_DREAMS_CIRCLET::get,
            TeyvatArtifacts.OCEAN_HUED_CLAM_FLOWER::get,
            TeyvatArtifacts.OCEAN_HUED_CLAM_FEATHER::get,
            TeyvatArtifacts.OCEAN_HUED_CLAM_SANDS::get,
            TeyvatArtifacts.OCEAN_HUED_CLAM_GOBLET::get,
            TeyvatArtifacts.OCEAN_HUED_CLAM_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> SUMERU_MOLTEN_IRON_FORTRESS_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.NYMPHS_DREAM_FLOWER::get,
            TeyvatArtifacts.NYMPHS_DREAM_FEATHER::get,
            TeyvatArtifacts.NYMPHS_DREAM_SANDS::get,
            TeyvatArtifacts.NYMPHS_DREAM_GOBLET::get,
            TeyvatArtifacts.NYMPHS_DREAM_CIRCLET::get,
            TeyvatArtifacts.VOURUKASHAS_GLOW_FLOWER::get,
            TeyvatArtifacts.VOURUKASHAS_GLOW_FEATHER::get,
            TeyvatArtifacts.VOURUKASHAS_GLOW_SANDS::get,
            TeyvatArtifacts.VOURUKASHAS_GLOW_GOBLET::get,
            TeyvatArtifacts.VOURUKASHAS_GLOW_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> SUMERU_SOLITARY_ENLIGHTENMENT_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.DEEPWOOD_MEMORIES_FLOWER::get,
            TeyvatArtifacts.DEEPWOOD_MEMORIES_FEATHER::get,
            TeyvatArtifacts.DEEPWOOD_MEMORIES_SANDS::get,
            TeyvatArtifacts.DEEPWOOD_MEMORIES_GOBLET::get,
            TeyvatArtifacts.DEEPWOOD_MEMORIES_CIRCLET::get,
            TeyvatArtifacts.GILDED_DREAMS_FLOWER::get,
            TeyvatArtifacts.GILDED_DREAMS_FEATHER::get,
            TeyvatArtifacts.GILDED_DREAMS_SANDS::get,
            TeyvatArtifacts.GILDED_DREAMS_GOBLET::get,
            TeyvatArtifacts.GILDED_DREAMS_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> SUMERU_CITY_GOLD_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.DESERT_PAVILION_CHRONICLE_FLOWER::get,
            TeyvatArtifacts.DESERT_PAVILION_CHRONICLE_FEATHER::get,
            TeyvatArtifacts.DESERT_PAVILION_CHRONICLE_SANDS::get,
            TeyvatArtifacts.DESERT_PAVILION_CHRONICLE_GOBLET::get,
            TeyvatArtifacts.DESERT_PAVILION_CHRONICLE_CIRCLET::get,
            TeyvatArtifacts.FLOWER_OF_PARADISE_LOST_FLOWER::get,
            TeyvatArtifacts.FLOWER_OF_PARADISE_LOST_FEATHER::get,
            TeyvatArtifacts.FLOWER_OF_PARADISE_LOST_SANDS::get,
            TeyvatArtifacts.FLOWER_OF_PARADISE_LOST_GOBLET::get,
            TeyvatArtifacts.FLOWER_OF_PARADISE_LOST_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> FONTAINE_WATERFALL_WEN_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.NIGHTTIME_WHISPERS_FLOWER::get,
            TeyvatArtifacts.NIGHTTIME_WHISPERS_FEATHER::get,
            TeyvatArtifacts.NIGHTTIME_WHISPERS_SANDS::get,
            TeyvatArtifacts.NIGHTTIME_WHISPERS_GOBLET::get,
            TeyvatArtifacts.NIGHTTIME_WHISPERS_CIRCLET::get,
            TeyvatArtifacts.SONG_OF_DAYS_PAST_FLOWER::get,
            TeyvatArtifacts.SONG_OF_DAYS_PAST_FEATHER::get,
            TeyvatArtifacts.SONG_OF_DAYS_PAST_SANDS::get,
            TeyvatArtifacts.SONG_OF_DAYS_PAST_GOBLET::get,
            TeyvatArtifacts.SONG_OF_DAYS_PAST_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> FONTAINE_DENOUEMENT_SIN_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.GOLDEN_TROUPE_FLOWER::get,
            TeyvatArtifacts.GOLDEN_TROUPE_FEATHER::get,
            TeyvatArtifacts.GOLDEN_TROUPE_SANDS::get,
            TeyvatArtifacts.GOLDEN_TROUPE_GOBLET::get,
            TeyvatArtifacts.GOLDEN_TROUPE_CIRCLET::get,
            TeyvatArtifacts.MARECHAUSSEE_HUNTER_FLOWER::get,
            TeyvatArtifacts.MARECHAUSSEE_HUNTER_FEATHER::get,
            TeyvatArtifacts.MARECHAUSSEE_HUNTER_SANDS::get,
            TeyvatArtifacts.MARECHAUSSEE_HUNTER_GOBLET::get,
            TeyvatArtifacts.MARECHAUSSEE_HUNTER_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> FONTAINE_FADED_THEATER_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.FRAGMENT_OF_HARMONIC_WHIMSY_FLOWER::get,
            TeyvatArtifacts.FRAGMENT_OF_HARMONIC_WHIMSY_FEATHER::get,
            TeyvatArtifacts.FRAGMENT_OF_HARMONIC_WHIMSY_SANDS::get,
            TeyvatArtifacts.FRAGMENT_OF_HARMONIC_WHIMSY_GOBLET::get,
            TeyvatArtifacts.FRAGMENT_OF_HARMONIC_WHIMSY_CIRCLET::get,
            TeyvatArtifacts.UNFINISHED_REVERIE_FLOWER::get,
            TeyvatArtifacts.UNFINISHED_REVERIE_FEATHER::get,
            TeyvatArtifacts.UNFINISHED_REVERIE_SANDS::get,
            TeyvatArtifacts.UNFINISHED_REVERIE_GOBLET::get,
            TeyvatArtifacts.UNFINISHED_REVERIE_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> NATLAN_DERELICT_MASONRY_DOCK_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.FINALE_OF_THE_DEEP_GALLERIES_FLOWER::get,
            TeyvatArtifacts.FINALE_OF_THE_DEEP_GALLERIES_FEATHER::get,
            TeyvatArtifacts.FINALE_OF_THE_DEEP_GALLERIES_SANDS::get,
            TeyvatArtifacts.FINALE_OF_THE_DEEP_GALLERIES_GOBLET::get,
            TeyvatArtifacts.FINALE_OF_THE_DEEP_GALLERIES_CIRCLET::get,
            TeyvatArtifacts.LONG_NIGHTS_OATH_FLOWER::get,
            TeyvatArtifacts.LONG_NIGHTS_OATH_FEATHER::get,
            TeyvatArtifacts.LONG_NIGHTS_OATH_SANDS::get,
            TeyvatArtifacts.LONG_NIGHTS_OATH_GOBLET::get,
            TeyvatArtifacts.LONG_NIGHTS_OATH_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> NATLAN_RAINBOW_SANCTUM_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.OBSIDIAN_CODEX_FLOWER::get,
            TeyvatArtifacts.OBSIDIAN_CODEX_FEATHER::get,
            TeyvatArtifacts.OBSIDIAN_CODEX_SANDS::get,
            TeyvatArtifacts.OBSIDIAN_CODEX_GOBLET::get,
            TeyvatArtifacts.OBSIDIAN_CODEX_CIRCLET::get,
            TeyvatArtifacts.SCROLL_OF_THE_HERO_OF_CINDER_CITY_FLOWER::get,
            TeyvatArtifacts.SCROLL_OF_THE_HERO_OF_CINDER_CITY_FEATHER::get,
            TeyvatArtifacts.SCROLL_OF_THE_HERO_OF_CINDER_CITY_SANDS::get,
            TeyvatArtifacts.SCROLL_OF_THE_HERO_OF_CINDER_CITY_GOBLET::get,
            TeyvatArtifacts.SCROLL_OF_THE_HERO_OF_CINDER_CITY_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> NOD_KRAI_MOONCHILDS_TREASURES_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.A_DAY_CARVED_FROM_RISING_WINDS_FLOWER::get,
            TeyvatArtifacts.A_DAY_CARVED_FROM_RISING_WINDS_FEATHER::get,
            TeyvatArtifacts.A_DAY_CARVED_FROM_RISING_WINDS_SANDS::get,
            TeyvatArtifacts.A_DAY_CARVED_FROM_RISING_WINDS_GOBLET::get,
            TeyvatArtifacts.A_DAY_CARVED_FROM_RISING_WINDS_CIRCLET::get,
            TeyvatArtifacts.AUBADE_OF_MORNINGSTAR_AND_MOON_FLOWER::get,
            TeyvatArtifacts.AUBADE_OF_MORNINGSTAR_AND_MOON_FEATHER::get,
            TeyvatArtifacts.AUBADE_OF_MORNINGSTAR_AND_MOON_SANDS::get,
            TeyvatArtifacts.AUBADE_OF_MORNINGSTAR_AND_MOON_GOBLET::get,
            TeyvatArtifacts.AUBADE_OF_MORNINGSTAR_AND_MOON_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> NOD_KRAI_FROSTLADEN_MACHINERY_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.NIGHT_OF_THE_SKYS_UNVEILING_FLOWER::get,
            TeyvatArtifacts.NIGHT_OF_THE_SKYS_UNVEILING_FEATHER::get,
            TeyvatArtifacts.NIGHT_OF_THE_SKYS_UNVEILING_SANDS::get,
            TeyvatArtifacts.NIGHT_OF_THE_SKYS_UNVEILING_GOBLET::get,
            TeyvatArtifacts.NIGHT_OF_THE_SKYS_UNVEILING_CIRCLET::get,
            TeyvatArtifacts.SILKEN_MOONS_SERENADE_FLOWER::get,
            TeyvatArtifacts.SILKEN_MOONS_SERENADE_FEATHER::get,
            TeyvatArtifacts.SILKEN_MOONS_SERENADE_SANDS::get,
            TeyvatArtifacts.SILKEN_MOONS_SERENADE_GOBLET::get,
            TeyvatArtifacts.SILKEN_MOONS_SERENADE_CIRCLET::get
    );
    private static final List<Supplier<ArtifactItem>> SNEZHNAYA_INVERTED_GLACIER_LEYLINE_SPAWNER_REWARD_POOL = List.of(
            TeyvatArtifacts.HEART_OF_THE_FURNACE_FLOWER::get,
            TeyvatArtifacts.HEART_OF_THE_FURNACE_FEATHER::get,
            TeyvatArtifacts.HEART_OF_THE_FURNACE_SANDS::get,
            TeyvatArtifacts.HEART_OF_THE_FURNACE_GOBLET::get,
            TeyvatArtifacts.HEART_OF_THE_FURNACE_CIRCLET::get,
            TeyvatArtifacts.SCARLET_PROOF_FLOWER::get,
            TeyvatArtifacts.SCARLET_PROOF_FEATHER::get,
            TeyvatArtifacts.SCARLET_PROOF_SANDS::get,
            TeyvatArtifacts.SCARLET_PROOF_GOBLET::get,
            TeyvatArtifacts.SCARLET_PROOF_CIRCLET::get
    );

    private LeylineRewardHelper() {
    }

    public static List<ArtifactSet> getRewardSets(String spawnerId) {
        List<Supplier<ArtifactItem>> pool = switch (spawnerId) {
            case "mondstadt_leyline_spawner" -> MONDSTADT_REWARD_POOL;
            case "liyue_clear_pool_leyline_spawner" -> LIYUE_CLEAR_POOL_REWARD_POOL;
            case "mondstadt_ridge_watch_leyline_spawner" -> MONDSTADT_RIDGE_WATCH_LEYLINE_SPAWNER_REWARD_POOL;
            case "mondstadt_thorny_crown_leyline_spawner" -> MONDSTADT_THORNY_CROWN_LEYLINE_SPAWNER_REWARD_POOL;
            case "mondstadt_peak_vindagnyr_leyline_spawner" -> MONDSTADT_PEAK_VINDAGNYR_LEYLINE_SPAWNER_REWARD_POOL;
            case "mondstadt_valley_remembrance_leyline_spawner" -> MONDSTADT_VALLEY_REMEMBRANCE_LEYLINE_SPAWNER_REWARD_POOL;
            case "liyue_domain_guyun_leyline_spawner" -> LIYUE_DOMAIN_GUYUN_LEYLINE_SPAWNER_REWARD_POOL;
            case "liyue_lost_valley_leyline_spawner" -> LIYUE_LOST_VALLEY_LEYLINE_SPAWNER_REWARD_POOL;
            case "liyue_zhou_formula_leyline_spawner" -> LIYUE_ZHOU_FORMULA_LEYLINE_SPAWNER_REWARD_POOL;
            case "inazuma_momiji_dyed_court_leyline_spawner" -> INAZUMA_MOMIJI_DYED_COURT_LEYLINE_SPAWNER_REWARD_POOL;
            case "inazuma_slumbering_court_leyline_spawner" -> INAZUMA_SLUMBERING_COURT_LEYLINE_SPAWNER_REWARD_POOL;
            case "sumeru_molten_iron_fortress_leyline_spawner" -> SUMERU_MOLTEN_IRON_FORTRESS_LEYLINE_SPAWNER_REWARD_POOL;
            case "sumeru_solitary_enlightenment_leyline_spawner" -> SUMERU_SOLITARY_ENLIGHTENMENT_LEYLINE_SPAWNER_REWARD_POOL;
            case "sumeru_city_gold_leyline_spawner" -> SUMERU_CITY_GOLD_LEYLINE_SPAWNER_REWARD_POOL;
            case "fontaine_waterfall_wen_leyline_spawner" -> FONTAINE_WATERFALL_WEN_LEYLINE_SPAWNER_REWARD_POOL;
            case "fontaine_denouement_sin_leyline_spawner" -> FONTAINE_DENOUEMENT_SIN_LEYLINE_SPAWNER_REWARD_POOL;
            case "fontaine_faded_theater_leyline_spawner" -> FONTAINE_FADED_THEATER_LEYLINE_SPAWNER_REWARD_POOL;
            case "natlan_derelict_masonry_dock_leyline_spawner" -> NATLAN_DERELICT_MASONRY_DOCK_LEYLINE_SPAWNER_REWARD_POOL;
            case "natlan_rainbow_sanctum_leyline_spawner" -> NATLAN_RAINBOW_SANCTUM_LEYLINE_SPAWNER_REWARD_POOL;
            case "nod_krai_moonchilds_treasures_leyline_spawner" -> NOD_KRAI_MOONCHILDS_TREASURES_LEYLINE_SPAWNER_REWARD_POOL;
            case "nod_krai_frostladen_machinery_leyline_spawner" -> NOD_KRAI_FROSTLADEN_MACHINERY_LEYLINE_SPAWNER_REWARD_POOL;
            case "snezhnaya_inverted_glacier_leyline_spawner" -> SNEZHNAYA_INVERTED_GLACIER_LEYLINE_SPAWNER_REWARD_POOL;
            default -> List.of();
        };
        return pool.stream().map(item -> item.get().getSet()).distinct().toList();
    }

    public static int getTenLevelXpCost() {
        return TEN_LEVEL_XP_COST;
    }

    public static int getRewardXpCost() {
        return TeyvatArtifactsConfig.LEYLINE_REWARD_XP_COST.get();
    }

    public static int getCrystalCoreXpCost() {
        return getRewardXpCost() * 2;
    }

    public static int getExperiencePointsForLevels(int levels) {
        return experiencePointsForLevels(levels);
    }

    public static int consumePayment(Player player, ItemStack heldStack) {
        if (isRewardDoubler(heldStack)) {
            heldStack.shrink(1);
            return 2;
        }

        int xpCost = getRewardXpCost();
        if (player.totalExperience < xpCost) {
            return 0;
        }

        player.giveExperiencePoints(-xpCost);
        return 1;
    }

    public static boolean isRewardDoubler(ItemStack stack) {
        return !stack.isEmpty() && stack.is(REWARD_DOUBLER_TAG);
    }

    public static List<ItemStack> createMondstadtRewards(RandomSource random, int multiplier) {
        return createRewards(random, multiplier, MONDSTADT_REWARD_POOL);
    }

    public static int getMondstadtDisplayPoolSize() {
        return MONDSTADT_REWARD_POOL.size();
    }

    public static ItemStack createMondstadtDisplayStack(int index) {
        if (MONDSTADT_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = MONDSTADT_REWARD_POOL.get(Math.floorMod(index, MONDSTADT_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createClearPoolRewards(RandomSource random, int multiplier) {
        return createRewards(random, multiplier, LIYUE_CLEAR_POOL_REWARD_POOL);
    }

    private static List<ItemStack> createRewards(RandomSource random, int multiplier, List<Supplier<ArtifactItem>> rewardPool) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(rewardPool);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount + 2);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        appendTeyvatDelightBonusRewards(rewards, random);
        return rewards;
    }

    private static void appendTeyvatDelightBonusRewards(List<ItemStack> rewards, RandomSource random) {
        BuiltInRegistries.ITEM.getOptional(TEYVAT_DELIGHT_MORA_ID).ifPresent(mora -> {
            rewards.add(new ItemStack(mora, 3));
            if (random.nextFloat() < TEYVAT_DELIGHT_PRIMOGEM_CHANCE) {
                BuiltInRegistries.ITEM.getOptional(TEYVAT_DELIGHT_PRIMOGEM_ID)
                        .ifPresent(primogem -> rewards.add(new ItemStack(primogem)));
            }
        });
    }

    public static int getClearPoolDisplayPoolSize() {
        return LIYUE_CLEAR_POOL_REWARD_POOL.size();
    }

    public static ItemStack createClearPoolDisplayStack(int index) {
        if (LIYUE_CLEAR_POOL_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = LIYUE_CLEAR_POOL_REWARD_POOL.get(Math.floorMod(index, LIYUE_CLEAR_POOL_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createRidgeWatchRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(MONDSTADT_RIDGE_WATCH_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getRidgeWatchDisplayPoolSize() {
        return MONDSTADT_RIDGE_WATCH_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createRidgeWatchDisplayStack(int index) {
        if (MONDSTADT_RIDGE_WATCH_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = MONDSTADT_RIDGE_WATCH_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, MONDSTADT_RIDGE_WATCH_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createThornyCrownRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(MONDSTADT_THORNY_CROWN_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getThornyCrownDisplayPoolSize() {
        return MONDSTADT_THORNY_CROWN_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createThornyCrownDisplayStack(int index) {
        if (MONDSTADT_THORNY_CROWN_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = MONDSTADT_THORNY_CROWN_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, MONDSTADT_THORNY_CROWN_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createPeakVindagnyrRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(MONDSTADT_PEAK_VINDAGNYR_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getPeakVindagnyrDisplayPoolSize() {
        return MONDSTADT_PEAK_VINDAGNYR_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createPeakVindagnyrDisplayStack(int index) {
        if (MONDSTADT_PEAK_VINDAGNYR_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = MONDSTADT_PEAK_VINDAGNYR_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, MONDSTADT_PEAK_VINDAGNYR_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createValleyRemembranceRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(MONDSTADT_VALLEY_REMEMBRANCE_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getValleyRemembranceDisplayPoolSize() {
        return MONDSTADT_VALLEY_REMEMBRANCE_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createValleyRemembranceDisplayStack(int index) {
        if (MONDSTADT_VALLEY_REMEMBRANCE_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = MONDSTADT_VALLEY_REMEMBRANCE_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, MONDSTADT_VALLEY_REMEMBRANCE_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createDomainGuyunRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(LIYUE_DOMAIN_GUYUN_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getDomainGuyunDisplayPoolSize() {
        return LIYUE_DOMAIN_GUYUN_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createDomainGuyunDisplayStack(int index) {
        if (LIYUE_DOMAIN_GUYUN_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = LIYUE_DOMAIN_GUYUN_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, LIYUE_DOMAIN_GUYUN_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createLostValleyRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(LIYUE_LOST_VALLEY_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getLostValleyDisplayPoolSize() {
        return LIYUE_LOST_VALLEY_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createLostValleyDisplayStack(int index) {
        if (LIYUE_LOST_VALLEY_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = LIYUE_LOST_VALLEY_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, LIYUE_LOST_VALLEY_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createZhouFormulaRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(LIYUE_ZHOU_FORMULA_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getZhouFormulaDisplayPoolSize() {
        return LIYUE_ZHOU_FORMULA_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createZhouFormulaDisplayStack(int index) {
        if (LIYUE_ZHOU_FORMULA_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = LIYUE_ZHOU_FORMULA_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, LIYUE_ZHOU_FORMULA_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createMomijiDyedCourtRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(INAZUMA_MOMIJI_DYED_COURT_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getMomijiDyedCourtDisplayPoolSize() {
        return INAZUMA_MOMIJI_DYED_COURT_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createMomijiDyedCourtDisplayStack(int index) {
        if (INAZUMA_MOMIJI_DYED_COURT_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = INAZUMA_MOMIJI_DYED_COURT_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, INAZUMA_MOMIJI_DYED_COURT_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createSlumberingCourtRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(INAZUMA_SLUMBERING_COURT_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getSlumberingCourtDisplayPoolSize() {
        return INAZUMA_SLUMBERING_COURT_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createSlumberingCourtDisplayStack(int index) {
        if (INAZUMA_SLUMBERING_COURT_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = INAZUMA_SLUMBERING_COURT_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, INAZUMA_SLUMBERING_COURT_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createMoltenIronFortressRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(SUMERU_MOLTEN_IRON_FORTRESS_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getMoltenIronFortressDisplayPoolSize() {
        return SUMERU_MOLTEN_IRON_FORTRESS_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createMoltenIronFortressDisplayStack(int index) {
        if (SUMERU_MOLTEN_IRON_FORTRESS_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = SUMERU_MOLTEN_IRON_FORTRESS_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, SUMERU_MOLTEN_IRON_FORTRESS_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createSolitaryEnlightenmentRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(SUMERU_SOLITARY_ENLIGHTENMENT_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getSolitaryEnlightenmentDisplayPoolSize() {
        return SUMERU_SOLITARY_ENLIGHTENMENT_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createSolitaryEnlightenmentDisplayStack(int index) {
        if (SUMERU_SOLITARY_ENLIGHTENMENT_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = SUMERU_SOLITARY_ENLIGHTENMENT_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, SUMERU_SOLITARY_ENLIGHTENMENT_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createCityGoldRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(SUMERU_CITY_GOLD_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getCityGoldDisplayPoolSize() {
        return SUMERU_CITY_GOLD_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createCityGoldDisplayStack(int index) {
        if (SUMERU_CITY_GOLD_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = SUMERU_CITY_GOLD_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, SUMERU_CITY_GOLD_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createWaterfallWenRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(FONTAINE_WATERFALL_WEN_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getWaterfallWenDisplayPoolSize() {
        return FONTAINE_WATERFALL_WEN_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createWaterfallWenDisplayStack(int index) {
        if (FONTAINE_WATERFALL_WEN_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = FONTAINE_WATERFALL_WEN_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, FONTAINE_WATERFALL_WEN_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createDenouementSinRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(FONTAINE_DENOUEMENT_SIN_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getDenouementSinDisplayPoolSize() {
        return FONTAINE_DENOUEMENT_SIN_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createDenouementSinDisplayStack(int index) {
        if (FONTAINE_DENOUEMENT_SIN_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = FONTAINE_DENOUEMENT_SIN_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, FONTAINE_DENOUEMENT_SIN_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createFadedTheaterRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(FONTAINE_FADED_THEATER_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getFadedTheaterDisplayPoolSize() {
        return FONTAINE_FADED_THEATER_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createFadedTheaterDisplayStack(int index) {
        if (FONTAINE_FADED_THEATER_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = FONTAINE_FADED_THEATER_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, FONTAINE_FADED_THEATER_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createDerelictMasonryDockRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(NATLAN_DERELICT_MASONRY_DOCK_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getDerelictMasonryDockDisplayPoolSize() {
        return NATLAN_DERELICT_MASONRY_DOCK_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createDerelictMasonryDockDisplayStack(int index) {
        if (NATLAN_DERELICT_MASONRY_DOCK_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = NATLAN_DERELICT_MASONRY_DOCK_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, NATLAN_DERELICT_MASONRY_DOCK_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createRainbowSanctumRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(NATLAN_RAINBOW_SANCTUM_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getRainbowSanctumDisplayPoolSize() {
        return NATLAN_RAINBOW_SANCTUM_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createRainbowSanctumDisplayStack(int index) {
        if (NATLAN_RAINBOW_SANCTUM_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = NATLAN_RAINBOW_SANCTUM_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, NATLAN_RAINBOW_SANCTUM_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createMoonchildsTreasuresRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(NOD_KRAI_MOONCHILDS_TREASURES_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getMoonchildsTreasuresDisplayPoolSize() {
        return NOD_KRAI_MOONCHILDS_TREASURES_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createMoonchildsTreasuresDisplayStack(int index) {
        if (NOD_KRAI_MOONCHILDS_TREASURES_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = NOD_KRAI_MOONCHILDS_TREASURES_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, NOD_KRAI_MOONCHILDS_TREASURES_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createFrostladenMachineryRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(NOD_KRAI_FROSTLADEN_MACHINERY_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getFrostladenMachineryDisplayPoolSize() {
        return NOD_KRAI_FROSTLADEN_MACHINERY_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createFrostladenMachineryDisplayStack(int index) {
        if (NOD_KRAI_FROSTLADEN_MACHINERY_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = NOD_KRAI_FROSTLADEN_MACHINERY_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, NOD_KRAI_FROSTLADEN_MACHINERY_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    public static List<ItemStack> createInvertedGlacierRewards(RandomSource random, int multiplier) {
        int rewardCount = (MIN_REWARD_COUNT + random.nextInt(MAX_REWARD_COUNT - MIN_REWARD_COUNT + 1)) * Math.max(1, multiplier);
        ArrayList<Supplier<ArtifactItem>> pool = new ArrayList<>(SNEZHNAYA_INVERTED_GLACIER_LEYLINE_SPAWNER_REWARD_POOL);
        ArrayList<ItemStack> rewards = new ArrayList<>(rewardCount);

        while (!pool.isEmpty() && rewards.size() < rewardCount) {
            Supplier<ArtifactItem> supplier = pool.remove(random.nextInt(pool.size()));
            ArtifactItem item = supplier.get();
            ItemStack stack = new ItemStack(item);
            initializeReward(stack, item, random);
            rewards.add(stack);
        }

        return rewards;
    }

    public static int getInvertedGlacierDisplayPoolSize() {
        return SNEZHNAYA_INVERTED_GLACIER_LEYLINE_SPAWNER_REWARD_POOL.size();
    }

    public static ItemStack createInvertedGlacierDisplayStack(int index) {
        if (SNEZHNAYA_INVERTED_GLACIER_LEYLINE_SPAWNER_REWARD_POOL.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ArtifactItem item = SNEZHNAYA_INVERTED_GLACIER_LEYLINE_SPAWNER_REWARD_POOL.get(Math.floorMod(index, SNEZHNAYA_INVERTED_GLACIER_LEYLINE_SPAWNER_REWARD_POOL.size())).get();
        return new ItemStack(item);
    }

    private static void initializeReward(ItemStack stack, ArtifactItem item, RandomSource random) {
        if (ArtifactItemData.getStars(stack) == 0) {
            ArtifactItemData.setStars(stack, ArtifactItemData.rollLeylineStars(item.getSet(), random));
        }
        ArtifactItemData.ensureStars(stack, item, random);
    }

    private static int experiencePointsForLevels(int levels) {
        int total = 0;
        for (int level = 0; level < levels; level++) {
            total += experiencePointsToReachNextLevel(level);
        }
        return total;
    }

    private static int experiencePointsToReachNextLevel(int level) {
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        }

        if (level >= 15) {
            return 37 + (level - 15) * 5;
        }

        return 7 + level * 2;
    }
}
