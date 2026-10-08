package com.guoche.teyvat_artifacts;

import net.minecraft.network.chat.Component;

public enum ArtifactSet {
    INITIATE("initiate", "tooltip.teyvat_artifacts.set.initiate", "tooltip.teyvat_artifacts.set.initiate.2", "", 1, 1),
    LUCKY("lucky", "tooltip.teyvat_artifacts.set.lucky", "tooltip.teyvat_artifacts.set.lucky.2", "tooltip.teyvat_artifacts.set.lucky.4", 1, 3),
    ADVENTURER("adventurer", "tooltip.teyvat_artifacts.set.adventurer", "tooltip.teyvat_artifacts.set.adventurer.2", "tooltip.teyvat_artifacts.set.adventurer.4", 1, 3),
    TRAVELING_DOCTOR("traveling_doctor", "tooltip.teyvat_artifacts.set.traveling_doctor", "tooltip.teyvat_artifacts.set.traveling_doctor.2", "tooltip.teyvat_artifacts.set.traveling_doctor.4", 1, 3),
    RESOLUTION_OF_SOJOURNER("resolution_of_sojourner", "tooltip.teyvat_artifacts.set.resolution_of_sojourner", "tooltip.teyvat_artifacts.set.resolution_of_sojourner.2", "tooltip.teyvat_artifacts.set.resolution_of_sojourner.4", 3, 4),
    TINY_MIRACLE("tiny_miracle", "tooltip.teyvat_artifacts.set.tiny_miracle", "tooltip.teyvat_artifacts.set.tiny_miracle.2", "tooltip.teyvat_artifacts.set.tiny_miracle.4", 3, 4),
    BERSERKER("berserker", "tooltip.teyvat_artifacts.set.berserker", "tooltip.teyvat_artifacts.set.berserker.2", "tooltip.teyvat_artifacts.set.berserker.4", 3, 4),
    INSTRUCTOR("instructor", "tooltip.teyvat_artifacts.set.instructor", "tooltip.teyvat_artifacts.set.instructor.2", "tooltip.teyvat_artifacts.set.instructor.4", 3, 4),
    THE_EXILE("the_exile", "tooltip.teyvat_artifacts.set.the_exile", "tooltip.teyvat_artifacts.set.the_exile.2", "tooltip.teyvat_artifacts.set.the_exile.4", 3, 4),
    DEFENDERS_WILL("defenders_will", "tooltip.teyvat_artifacts.set.defenders_will", "tooltip.teyvat_artifacts.set.defenders_will.2", "tooltip.teyvat_artifacts.set.defenders_will.4", 3, 4),
    BRAVE_HEART("brave_heart", "tooltip.teyvat_artifacts.set.brave_heart", "tooltip.teyvat_artifacts.set.brave_heart.2", "tooltip.teyvat_artifacts.set.brave_heart.4", 3, 4),
    MARTIAL_ARTIST("martial_artist", "tooltip.teyvat_artifacts.set.martial_artist", "tooltip.teyvat_artifacts.set.martial_artist.2", "tooltip.teyvat_artifacts.set.martial_artist.4", 3, 4),
    GAMBLER("gambler", "tooltip.teyvat_artifacts.set.gambler", "tooltip.teyvat_artifacts.set.gambler.2", "tooltip.teyvat_artifacts.set.gambler.4", 3, 4),
    SCHOLAR("scholar", "tooltip.teyvat_artifacts.set.scholar", "tooltip.teyvat_artifacts.set.scholar.2", "tooltip.teyvat_artifacts.set.scholar.4", 3, 4),
    GLADIATORS_FINALE("gladiators_finale", "tooltip.teyvat_artifacts.set.gladiators_finale", "tooltip.teyvat_artifacts.set.gladiators_finale.2", "tooltip.teyvat_artifacts.set.gladiators_finale.4", 4, 5),
    WANDERERS_TROUPE("wanderers_troupe", "tooltip.teyvat_artifacts.set.wanderers_troupe", "tooltip.teyvat_artifacts.set.wanderers_troupe.2", "tooltip.teyvat_artifacts.set.wanderers_troupe.4", 4, 5),
    NOBLESSE_OBLIGE("noblesse_oblige", "tooltip.teyvat_artifacts.set.noblesse_oblige", "tooltip.teyvat_artifacts.set.noblesse_oblige.2", "tooltip.teyvat_artifacts.set.noblesse_oblige.4", 4, 5),
    BLOODSTAINED_CHIVALRY("bloodstained_chivalry", "tooltip.teyvat_artifacts.set.bloodstained_chivalry", "tooltip.teyvat_artifacts.set.bloodstained_chivalry.2", "tooltip.teyvat_artifacts.set.bloodstained_chivalry.4", 4, 5),
    MAIDEN_BELOVED("maiden_beloved", "tooltip.teyvat_artifacts.set.maiden_beloved", "tooltip.teyvat_artifacts.set.maiden_beloved.2", "tooltip.teyvat_artifacts.set.maiden_beloved.4", 4, 5),
    VIRIDESCENT_VENERER("viridescent_venerer", "tooltip.teyvat_artifacts.set.viridescent_venerer", "tooltip.teyvat_artifacts.set.viridescent_venerer.2", "tooltip.teyvat_artifacts.set.viridescent_venerer.4", 4, 5),
    ARCHAIC_PETRA("archaic_petra", "tooltip.teyvat_artifacts.set.archaic_petra", "tooltip.teyvat_artifacts.set.archaic_petra.2", "tooltip.teyvat_artifacts.set.archaic_petra.4", 4, 5),
    RETRACING_BOLIDE("retracing_bolide", "tooltip.teyvat_artifacts.set.retracing_bolide", "tooltip.teyvat_artifacts.set.retracing_bolide.2", "tooltip.teyvat_artifacts.set.retracing_bolide.4", 4, 5),
    TENACITY_OF_THE_MILLELITH("tenacity_of_the_millelith", "tooltip.teyvat_artifacts.set.tenacity_of_the_millelith", "tooltip.teyvat_artifacts.set.tenacity_of_the_millelith.2", "tooltip.teyvat_artifacts.set.tenacity_of_the_millelith.4", 4, 5),
    PALE_FLAME("pale_flame", "tooltip.teyvat_artifacts.set.pale_flame", "tooltip.teyvat_artifacts.set.pale_flame.2", "tooltip.teyvat_artifacts.set.pale_flame.4", 4, 5),
    SHIMENAWAS_REMINISCENCE("shimenawas_reminiscence", "tooltip.teyvat_artifacts.set.shimenawas_reminiscence", "tooltip.teyvat_artifacts.set.shimenawas_reminiscence.2", "tooltip.teyvat_artifacts.set.shimenawas_reminiscence.4", 4, 5),
    EMBLEM_OF_SEVERED_FATE("emblem_of_severed_fate", "tooltip.teyvat_artifacts.set.emblem_of_severed_fate", "tooltip.teyvat_artifacts.set.emblem_of_severed_fate.2", "tooltip.teyvat_artifacts.set.emblem_of_severed_fate.4", 4, 5),
    HUSK_OF_OPULENT_DREAMS("husk_of_opulent_dreams", "tooltip.teyvat_artifacts.set.husk_of_opulent_dreams", "tooltip.teyvat_artifacts.set.husk_of_opulent_dreams.2", "tooltip.teyvat_artifacts.set.husk_of_opulent_dreams.4", 4, 5),
    OCEAN_HUED_CLAM("ocean_hued_clam", "tooltip.teyvat_artifacts.set.ocean_hued_clam", "tooltip.teyvat_artifacts.set.ocean_hued_clam.2", "tooltip.teyvat_artifacts.set.ocean_hued_clam.4", 4, 5),
    VERMILLION_HEREAFTER("vermillion_hereafter", "tooltip.teyvat_artifacts.set.vermillion_hereafter", "tooltip.teyvat_artifacts.set.vermillion_hereafter.2", "tooltip.teyvat_artifacts.set.vermillion_hereafter.4", 4, 5),
    ECHOES_OF_AN_OFFERING("echoes_of_an_offering", "tooltip.teyvat_artifacts.set.echoes_of_an_offering", "tooltip.teyvat_artifacts.set.echoes_of_an_offering.2", "tooltip.teyvat_artifacts.set.echoes_of_an_offering.4", 4, 5),
    DEEPWOOD_MEMORIES("deepwood_memories", "tooltip.teyvat_artifacts.set.deepwood_memories", "tooltip.teyvat_artifacts.set.deepwood_memories.2", "tooltip.teyvat_artifacts.set.deepwood_memories.4", 4, 5),
    GILDED_DREAMS("gilded_dreams", "tooltip.teyvat_artifacts.set.gilded_dreams", "tooltip.teyvat_artifacts.set.gilded_dreams.2", "tooltip.teyvat_artifacts.set.gilded_dreams.4", 4, 5),
    DESERT_PAVILION_CHRONICLE("desert_pavilion_chronicle", "tooltip.teyvat_artifacts.set.desert_pavilion_chronicle", "tooltip.teyvat_artifacts.set.desert_pavilion_chronicle.2", "tooltip.teyvat_artifacts.set.desert_pavilion_chronicle.4", 4, 5),
    FLOWER_OF_PARADISE_LOST("flower_of_paradise_lost", "tooltip.teyvat_artifacts.set.flower_of_paradise_lost", "tooltip.teyvat_artifacts.set.flower_of_paradise_lost.2", "tooltip.teyvat_artifacts.set.flower_of_paradise_lost.4", 4, 5),
    NYMPHS_DREAM("nymphs_dream", "tooltip.teyvat_artifacts.set.nymphs_dream", "tooltip.teyvat_artifacts.set.nymphs_dream.2", "tooltip.teyvat_artifacts.set.nymphs_dream.4", 4, 5),
    VOURUKASHAS_GLOW("vourukashas_glow", "tooltip.teyvat_artifacts.set.vourukashas_glow", "tooltip.teyvat_artifacts.set.vourukashas_glow.2", "tooltip.teyvat_artifacts.set.vourukashas_glow.4", 4, 5),
    MARECHAUSSEE_HUNTER("marechaussee_hunter", "tooltip.teyvat_artifacts.set.marechaussee_hunter", "tooltip.teyvat_artifacts.set.marechaussee_hunter.2", "tooltip.teyvat_artifacts.set.marechaussee_hunter.4", 4, 5),
    GOLDEN_TROUPE("golden_troupe", "tooltip.teyvat_artifacts.set.golden_troupe", "tooltip.teyvat_artifacts.set.golden_troupe.2", "tooltip.teyvat_artifacts.set.golden_troupe.4", 4, 5),
    SONG_OF_DAYS_PAST("song_of_days_past", "tooltip.teyvat_artifacts.set.song_of_days_past", "tooltip.teyvat_artifacts.set.song_of_days_past.2", "tooltip.teyvat_artifacts.set.song_of_days_past.4", 4, 5),
    NIGHTTIME_WHISPERS("nighttime_whispers", "tooltip.teyvat_artifacts.set.nighttime_whispers", "tooltip.teyvat_artifacts.set.nighttime_whispers.2", "tooltip.teyvat_artifacts.set.nighttime_whispers.4", 4, 5),
    FRAGMENT_OF_HARMONIC_WHIMSY("fragment_of_harmonic_whimsy", "tooltip.teyvat_artifacts.set.fragment_of_harmonic_whimsy", "tooltip.teyvat_artifacts.set.fragment_of_harmonic_whimsy.2", "tooltip.teyvat_artifacts.set.fragment_of_harmonic_whimsy.4", 4, 5),
    UNFINISHED_REVERIE("unfinished_reverie", "tooltip.teyvat_artifacts.set.unfinished_reverie", "tooltip.teyvat_artifacts.set.unfinished_reverie.2", "tooltip.teyvat_artifacts.set.unfinished_reverie.4", 4, 5),
    SCROLL_OF_THE_HERO_OF_CINDER_CITY("scroll_of_the_hero_of_cinder_city", "tooltip.teyvat_artifacts.set.scroll_of_the_hero_of_cinder_city", "tooltip.teyvat_artifacts.set.scroll_of_the_hero_of_cinder_city.2", "tooltip.teyvat_artifacts.set.scroll_of_the_hero_of_cinder_city.4", 4, 5),
    OBSIDIAN_CODEX("obsidian_codex", "tooltip.teyvat_artifacts.set.obsidian_codex", "tooltip.teyvat_artifacts.set.obsidian_codex.2", "tooltip.teyvat_artifacts.set.obsidian_codex.4", 4, 5),
    LONG_NIGHTS_OATH("long_nights_oath", "tooltip.teyvat_artifacts.set.long_nights_oath", "tooltip.teyvat_artifacts.set.long_nights_oath.2", "tooltip.teyvat_artifacts.set.long_nights_oath.4", 4, 5),
    FINALE_OF_THE_DEEP_GALLERIES("finale_of_the_deep_galleries", "tooltip.teyvat_artifacts.set.finale_of_the_deep_galleries", "tooltip.teyvat_artifacts.set.finale_of_the_deep_galleries.2", "tooltip.teyvat_artifacts.set.finale_of_the_deep_galleries.4", 4, 5),
    NIGHT_OF_THE_SKYS_UNVEILING("night_of_the_skys_unveiling", "tooltip.teyvat_artifacts.set.night_of_the_skys_unveiling", "tooltip.teyvat_artifacts.set.night_of_the_skys_unveiling.2", "tooltip.teyvat_artifacts.set.night_of_the_skys_unveiling.4", 4, 5),
    SILKEN_MOONS_SERENADE("silken_moons_serenade", "tooltip.teyvat_artifacts.set.silken_moons_serenade", "tooltip.teyvat_artifacts.set.silken_moons_serenade.2", "tooltip.teyvat_artifacts.set.silken_moons_serenade.4", 4, 5),
    AUBADE_OF_MORNINGSTAR_AND_MOON("aubade_of_morningstar_and_moon", "tooltip.teyvat_artifacts.set.aubade_of_morningstar_and_moon", "tooltip.teyvat_artifacts.set.aubade_of_morningstar_and_moon.2", "tooltip.teyvat_artifacts.set.aubade_of_morningstar_and_moon.4", 4, 5),
    A_DAY_CARVED_FROM_RISING_WINDS("a_day_carved_from_rising_winds", "tooltip.teyvat_artifacts.set.a_day_carved_from_rising_winds", "tooltip.teyvat_artifacts.set.a_day_carved_from_rising_winds.2", "tooltip.teyvat_artifacts.set.a_day_carved_from_rising_winds.4", 4, 5),
    CELESTIAL_GIFT("celestial_gift", "tooltip.teyvat_artifacts.set.celestial_gift", "tooltip.teyvat_artifacts.set.celestial_gift.2", "tooltip.teyvat_artifacts.set.celestial_gift.4", 4, 5),
    DISENCHANTMENT_IN_DEEP_SHADOW("disenchantment_in_deep_shadow", "tooltip.teyvat_artifacts.set.disenchantment_in_deep_shadow", "tooltip.teyvat_artifacts.set.disenchantment_in_deep_shadow.2", "tooltip.teyvat_artifacts.set.disenchantment_in_deep_shadow.4", 4, 5),
    SCARLET_PROOF("scarlet_proof", "tooltip.teyvat_artifacts.set.scarlet_proof", "tooltip.teyvat_artifacts.set.scarlet_proof.2", "tooltip.teyvat_artifacts.set.scarlet_proof.4", 4, 5),
    HEART_OF_THE_FURNACE("heart_of_the_furnace", "tooltip.teyvat_artifacts.set.heart_of_the_furnace", "tooltip.teyvat_artifacts.set.heart_of_the_furnace.2", "tooltip.teyvat_artifacts.set.heart_of_the_furnace.4", 4, 5),
    THUNDERSOOTHER("thundersoother", "tooltip.teyvat_artifacts.set.thundersoother", "tooltip.teyvat_artifacts.set.thundersoother.2", "tooltip.teyvat_artifacts.set.thundersoother.4", 4, 5),
    THUNDERING_FURY("thundering_fury", "tooltip.teyvat_artifacts.set.thundering_fury", "tooltip.teyvat_artifacts.set.thundering_fury.2", "tooltip.teyvat_artifacts.set.thundering_fury.4", 4, 5),
    LAVAWALKER("lavawalker", "tooltip.teyvat_artifacts.set.lavawalker", "tooltip.teyvat_artifacts.set.lavawalker.2", "tooltip.teyvat_artifacts.set.lavawalker.4", 4, 5),
    CRIMSON_WITCH_OF_FLAMES("crimson_witch_of_flames", "tooltip.teyvat_artifacts.set.crimson_witch_of_flames", "tooltip.teyvat_artifacts.set.crimson_witch_of_flames.2", "tooltip.teyvat_artifacts.set.crimson_witch_of_flames.4", 4, 5),
    BLIZZARD_STRAYER("blizzard_strayer", "tooltip.teyvat_artifacts.set.blizzard_strayer", "tooltip.teyvat_artifacts.set.blizzard_strayer.2", "tooltip.teyvat_artifacts.set.blizzard_strayer.4", 4, 5),
    HEART_OF_DEPTH("heart_of_depth", "tooltip.teyvat_artifacts.set.heart_of_depth", "tooltip.teyvat_artifacts.set.heart_of_depth.2", "tooltip.teyvat_artifacts.set.heart_of_depth.4", 4, 5),
    PRAYERS_FOR_THUNDER("prayers_for_thunder", "tooltip.teyvat_artifacts.set.prayers_for_thunder", "tooltip.teyvat_artifacts.set.prayers_for_thunder.1", "", "", 3, 4),
    PRAYERS_FOR_DESTINY("prayers_for_destiny", "tooltip.teyvat_artifacts.set.prayers_for_destiny", "tooltip.teyvat_artifacts.set.prayers_for_destiny.1", "", "", 3, 4),
    PRAYERS_FOR_ILLUMINATION("prayers_for_illumination", "tooltip.teyvat_artifacts.set.prayers_for_illumination", "tooltip.teyvat_artifacts.set.prayers_for_illumination.1", "", "", 3, 4),
    PRAYERS_TO_SPRINGTIME("prayers_to_springtime", "tooltip.teyvat_artifacts.set.prayers_to_springtime", "tooltip.teyvat_artifacts.set.prayers_to_springtime.1", "", "", 3, 4);

    private final String id;
    private final String nameKey;
    private final String onePieceKey;
    private final String twoPieceKey;
    private final String fourPieceKey;
    private final int minStars;
    private final int maxStars;

    ArtifactSet(String id, String nameKey, String twoPieceKey, String fourPieceKey, int minStars, int maxStars) {
        this(id, nameKey, "", twoPieceKey, fourPieceKey, minStars, maxStars);
    }

    ArtifactSet(String id, String nameKey, String onePieceKey, String twoPieceKey, String fourPieceKey, int minStars, int maxStars) {
        this.id = id;
        this.nameKey = nameKey;
        this.onePieceKey = onePieceKey;
        this.twoPieceKey = twoPieceKey;
        this.fourPieceKey = fourPieceKey;
        this.minStars = ArtifactItemData.clampStars(minStars);
        this.maxStars = Math.max(this.minStars, ArtifactItemData.clampStars(maxStars));
    }

    public String id() {
        return id;
    }

    public String nameKey() {
        return nameKey;
    }

    public String onePieceKey() {
        return onePieceKey;
    }

    public String twoPieceKey() {
        return twoPieceKey;
    }

    public String fourPieceKey() {
        return fourPieceKey;
    }

    public boolean hasTwoPiece() {
        return !twoPieceKey.isEmpty();
    }

    public boolean hasOnePiece() {
        return !onePieceKey.isEmpty();
    }

    public boolean hasFourPiece() {
        return !fourPieceKey.isEmpty();
    }

    public int minStars() {
        return minStars;
    }

    public int maxStars() {
        return maxStars;
    }

    public Component displayName() {
        return Component.translatable(nameKey);
    }
}
