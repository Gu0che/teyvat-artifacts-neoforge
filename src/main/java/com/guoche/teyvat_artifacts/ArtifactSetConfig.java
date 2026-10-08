package com.guoche.teyvat_artifacts;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class ArtifactSetConfig {
    public static final ModConfigSpec SPEC;

    public enum Value {
        LUCKY_2_DAMAGE_REDUCTION(ArtifactSet.LUCKY, 2, "damageReductionPercent", Type.PERCENT, 15),
        LUCKY_4_HEAL_PER_ORB(ArtifactSet.LUCKY, 4, "healPerExperienceOrb", Type.NUMBER, 2),

        ADVENTURER_2_MAX_HEALTH(ArtifactSet.ADVENTURER, 2, "maxHealth", Type.NUMBER, 5),
        ADVENTURER_4_INSTANT_HEAL_LEVEL(ArtifactSet.ADVENTURER, 4, "instantHealLevel", Type.LEVEL, 1),

        TRAVELING_DOCTOR_2_HEALING_LEVEL_BONUS(ArtifactSet.TRAVELING_DOCTOR, 2, "healingEffectLevelBonus", Type.INTEGER, 1),
        TRAVELING_DOCTOR_4_REGENERATION_LEVEL(ArtifactSet.TRAVELING_DOCTOR, 4, "regenerationLevel", Type.LEVEL, 2),
        TRAVELING_DOCTOR_4_DURATION(ArtifactSet.TRAVELING_DOCTOR, 4, "durationSeconds", Type.SECONDS, 10),

        RESOLUTION_2_DAMAGE(ArtifactSet.RESOLUTION_OF_SOJOURNER, 2, "damageBonusPercent", Type.PERCENT, 15),
        RESOLUTION_4_CRIT_RATE(ArtifactSet.RESOLUTION_OF_SOJOURNER, 4, "critRateBonusPercent", Type.PERCENT, 15),

        TINY_MIRACLE_2_REDUCTION(ArtifactSet.TINY_MIRACLE, 2, "damageReductionPercent", Type.PERCENT, 20),
        TINY_MIRACLE_4_REDUCTION(ArtifactSet.TINY_MIRACLE, 4, "boostedDamageReductionPercent", Type.PERCENT, 80),
        TINY_MIRACLE_4_DURATION(ArtifactSet.TINY_MIRACLE, 4, "durationSeconds", Type.SECONDS, 10),
        TINY_MIRACLE_4_COOLDOWN(ArtifactSet.TINY_MIRACLE, 4, "cooldownSeconds", Type.SECONDS, 10),

        BERSERKER_2_CRIT_RATE(ArtifactSet.BERSERKER, 2, "critRateBonusPercent", Type.PERCENT, 12),
        BERSERKER_4_HEALTH_THRESHOLD(ArtifactSet.BERSERKER, 4, "healthThresholdPercent", Type.PERCENT, 70),
        BERSERKER_4_CRIT_RATE(ArtifactSet.BERSERKER, 4, "critRateBonusPercent", Type.PERCENT, 24),

        INSTRUCTOR_2_ARMOR_IGNORE(ArtifactSet.INSTRUCTOR, 2, "armorIgnorePercent", Type.PERCENT, 15),
        INSTRUCTOR_4_ARMOR_IGNORE(ArtifactSet.INSTRUCTOR, 4, "armorIgnorePercent", Type.PERCENT, 20),
        INSTRUCTOR_4_DURATION(ArtifactSet.INSTRUCTOR, 4, "durationSeconds", Type.SECONDS, 8),

        EXILE_2_ATTACK_SPEED(ArtifactSet.THE_EXILE, 2, "attackSpeedBonusPercent", Type.PERCENT, 20),
        EXILE_4_ATTACK_SPEED(ArtifactSet.THE_EXILE, 4, "attackSpeedBonusPercent", Type.PERCENT, 20),
        EXILE_4_DURATION(ArtifactSet.THE_EXILE, 4, "durationSeconds", Type.SECONDS, 8),

        DEFENDERS_WILL_2_REDUCTION(ArtifactSet.DEFENDERS_WILL, 2, "damageReductionPercent", Type.PERCENT, 20),
        DEFENDERS_WILL_4_REDUCTION(ArtifactSet.DEFENDERS_WILL, 4, "additionalDamageReductionPercent", Type.PERCENT, 20),
        DEFENDERS_WILL_4_KNOCKBACK_RESISTANCE(ArtifactSet.DEFENDERS_WILL, 4, "knockbackResistancePercent", Type.PERCENT, 20),

        BRAVE_HEART_2_DAMAGE(ArtifactSet.BRAVE_HEART, 2, "damageBonusPercent", Type.PERCENT, 15),
        BRAVE_HEART_4_TARGET_HEALTH_THRESHOLD(ArtifactSet.BRAVE_HEART, 4, "targetHealthThresholdPercent", Type.PERCENT, 50),
        BRAVE_HEART_4_DAMAGE(ArtifactSet.BRAVE_HEART, 4, "damageBonusPercent", Type.PERCENT, 30),

        MARTIAL_ARTIST_2_DAMAGE(ArtifactSet.MARTIAL_ARTIST, 2, "damageBonusPercent", Type.PERCENT, 15),
        MARTIAL_ARTIST_4_DAMAGE(ArtifactSet.MARTIAL_ARTIST, 4, "damageBonusPercent", Type.PERCENT, 20),
        MARTIAL_ARTIST_4_DURATION(ArtifactSet.MARTIAL_ARTIST, 4, "durationSeconds", Type.SECONDS, 8),

        GAMBLER_2_PROJECTILE_DAMAGE(ArtifactSet.GAMBLER, 2, "projectileDamageBonusPercent", Type.PERCENT, 25),
        GAMBLER_4_PROJECTILE_DAMAGE(ArtifactSet.GAMBLER, 4, "nextProjectileDamageBonusPercent", Type.PERCENT, 500),
        GAMBLER_4_COOLDOWN(ArtifactSet.GAMBLER, 4, "cooldownSeconds", Type.SECONDS, 15),

        SCHOLAR_2_COOLDOWN_REDUCTION(ArtifactSet.SCHOLAR, 2, "spellCooldownReductionPercent", Type.PERCENT, 20),
        SCHOLAR_2_CAST_TIME_REDUCTION(ArtifactSet.SCHOLAR, 2, "spellCastTimeReductionPercent", Type.PERCENT, 20),
        SCHOLAR_4_MAX_MANA(ArtifactSet.SCHOLAR, 4, "maxMana", Type.NUMBER, 200),
        SCHOLAR_4_SPELL_DAMAGE(ArtifactSet.SCHOLAR, 4, "spellDamageBonusPercent", Type.PERCENT, 20),

        GLADIATOR_2_DAMAGE(ArtifactSet.GLADIATORS_FINALE, 2, "damageBonusPercent", Type.PERCENT, 20),
        GLADIATOR_4_MELEE_DAMAGE(ArtifactSet.GLADIATORS_FINALE, 4, "meleeDamageBonusPercent", Type.PERCENT, 35),
        WANDERER_2_DAMAGE(ArtifactSet.WANDERERS_TROUPE, 2, "damageBonusPercent", Type.PERCENT, 20),
        WANDERER_4_PROJECTILE_SPELL_DAMAGE(ArtifactSet.WANDERERS_TROUPE, 4, "projectileAndSpellDamageBonusPercent", Type.PERCENT, 35),

        THUNDERSOOTHER_2_SPELL_REDUCTION(ArtifactSet.THUNDERSOOTHER, 2, "lightningSpellDamageReductionPercent", Type.PERCENT, 40),
        THUNDERSOOTHER_4_DAMAGE_PER_STACK(ArtifactSet.THUNDERSOOTHER, 4, "damageBonusPerStackPercent", Type.PERCENT, 10),
        THUNDERSOOTHER_4_MAX_STACKS(ArtifactSet.THUNDERSOOTHER, 4, "maxStacks", Type.POSITIVE_INTEGER, 8),
        THUNDERSOOTHER_4_DURATION(ArtifactSet.THUNDERSOOTHER, 4, "durationSeconds", Type.SECONDS, 10),

        THUNDERING_FURY_2_SPELL_DAMAGE(ArtifactSet.THUNDERING_FURY, 2, "spellDamageBonusPercent", Type.PERCENT, 35),
        THUNDERING_FURY_2_GLOWING_DURATION(ArtifactSet.THUNDERING_FURY, 2, "glowingDurationSeconds", Type.SECONDS, 20),
        THUNDERING_FURY_4_COOLDOWN_REDUCTION(ArtifactSet.THUNDERING_FURY, 4, "spellCooldownReductionPercent", Type.PERCENT, 25),
        THUNDERING_FURY_4_DEBUFF_DURATION(ArtifactSet.THUNDERING_FURY, 4, "debuffDurationSeconds", Type.SECONDS, 20),
        THUNDERING_FURY_4_WEAKNESS_LEVEL(ArtifactSet.THUNDERING_FURY, 4, "weaknessLevel", Type.LEVEL, 1),
        THUNDERING_FURY_4_SLOWNESS_LEVEL(ArtifactSet.THUNDERING_FURY, 4, "slownessLevel", Type.LEVEL, 1),
        THUNDERING_FURY_4_GLOWING_DAMAGE(ArtifactSet.THUNDERING_FURY, 4, "glowingTargetSpellDamageBonusPercent", Type.PERCENT, 60),

        LAVAWALKER_2_SPELL_REDUCTION(ArtifactSet.LAVAWALKER, 2, "fireSpellDamageReductionPercent", Type.PERCENT, 40),
        LAVAWALKER_4_DAMAGE_PER_STACK(ArtifactSet.LAVAWALKER, 4, "damageBonusPerStackPercent", Type.PERCENT, 10),
        LAVAWALKER_4_MAX_STACKS(ArtifactSet.LAVAWALKER, 4, "maxStacks", Type.POSITIVE_INTEGER, 8),
        LAVAWALKER_4_DURATION(ArtifactSet.LAVAWALKER, 4, "durationSeconds", Type.SECONDS, 10),

        CRIMSON_WITCH_2_SPELL_DAMAGE(ArtifactSet.CRIMSON_WITCH_OF_FLAMES, 2, "spellDamageBonusPercent", Type.PERCENT, 35),
        CRIMSON_WITCH_2_GLOWING_DURATION(ArtifactSet.CRIMSON_WITCH_OF_FLAMES, 2, "glowingDurationSeconds", Type.SECONDS, 20),
        CRIMSON_WITCH_4_COOLDOWN_REDUCTION(ArtifactSet.CRIMSON_WITCH_OF_FLAMES, 4, "spellCooldownReductionPercent", Type.PERCENT, 25),
        CRIMSON_WITCH_4_IGNITE_DURATION(ArtifactSet.CRIMSON_WITCH_OF_FLAMES, 4, "soulFireDurationSeconds", Type.SECONDS, 20),
        CRIMSON_WITCH_4_DEBUFF_DURATION(ArtifactSet.CRIMSON_WITCH_OF_FLAMES, 4, "debuffDurationSeconds", Type.SECONDS, 20),
        CRIMSON_WITCH_4_WEAKNESS_LEVEL(ArtifactSet.CRIMSON_WITCH_OF_FLAMES, 4, "weaknessLevel", Type.LEVEL, 1),
        CRIMSON_WITCH_4_SLOWNESS_LEVEL(ArtifactSet.CRIMSON_WITCH_OF_FLAMES, 4, "slownessLevel", Type.LEVEL, 1),
        CRIMSON_WITCH_4_GLOWING_DAMAGE(ArtifactSet.CRIMSON_WITCH_OF_FLAMES, 4, "glowingTargetSpellDamageBonusPercent", Type.PERCENT, 60),

        BLIZZARD_STRAYER_2_SPELL_DAMAGE(ArtifactSet.BLIZZARD_STRAYER, 2, "spellDamageBonusPercent", Type.PERCENT, 35),
        BLIZZARD_STRAYER_2_GLOWING_DURATION(ArtifactSet.BLIZZARD_STRAYER, 2, "glowingDurationSeconds", Type.SECONDS, 20),
        BLIZZARD_STRAYER_4_COOLDOWN_REDUCTION(ArtifactSet.BLIZZARD_STRAYER, 4, "spellCooldownReductionPercent", Type.PERCENT, 25),
        BLIZZARD_STRAYER_4_FREEZE_DURATION(ArtifactSet.BLIZZARD_STRAYER, 4, "freezeDurationSeconds", Type.SECONDS, 20),
        BLIZZARD_STRAYER_4_DEBUFF_DURATION(ArtifactSet.BLIZZARD_STRAYER, 4, "debuffDurationSeconds", Type.SECONDS, 20),
        BLIZZARD_STRAYER_4_WEAKNESS_LEVEL(ArtifactSet.BLIZZARD_STRAYER, 4, "weaknessLevel", Type.LEVEL, 1),
        BLIZZARD_STRAYER_4_SLOWNESS_LEVEL(ArtifactSet.BLIZZARD_STRAYER, 4, "slownessLevel", Type.LEVEL, 1),
        BLIZZARD_STRAYER_4_GLOWING_DAMAGE(ArtifactSet.BLIZZARD_STRAYER, 4, "glowingTargetSpellDamageBonusPercent", Type.PERCENT, 60),

        HEART_OF_DEPTH_4_CRIT_DURATION(ArtifactSet.HEART_OF_DEPTH, 4, "criticalBuffDurationSeconds", Type.SECONDS, 15),
        HEART_OF_DEPTH_4_MELEE_DAMAGE(ArtifactSet.HEART_OF_DEPTH, 4, "meleeDamageBonusPercent", Type.PERCENT, 30),
        HEART_OF_DEPTH_4_CRIT_DAMAGE(ArtifactSet.HEART_OF_DEPTH, 4, "critDamageBonusPercent", Type.PERCENT, 30),
        HEART_OF_DEPTH_4_WATER_DAMAGE(ArtifactSet.HEART_OF_DEPTH, 4, "waterOrRainDamageBonusPercent", Type.PERCENT, 30),
        HEART_OF_DEPTH_4_WATER_REDUCTION(ArtifactSet.HEART_OF_DEPTH, 4, "waterOrRainDamageReductionPercent", Type.PERCENT, 30),

        NOBLESSE_2_CRIT_DAMAGE(ArtifactSet.NOBLESSE_OBLIGE, 2, "critDamageBonusPercent", Type.PERCENT, 30),
        NOBLESSE_4_DAMAGE(ArtifactSet.NOBLESSE_OBLIGE, 4, "damageBonusPercent", Type.PERCENT, 30),
        NOBLESSE_4_DURATION(ArtifactSet.NOBLESSE_OBLIGE, 4, "durationSeconds", Type.SECONDS, 12),

        BLOODSTAINED_2_NON_SPELL_DAMAGE(ArtifactSet.BLOODSTAINED_CHIVALRY, 2, "nonSpellDamageBonusPercent", Type.PERCENT, 25),
        BLOODSTAINED_4_DURATION(ArtifactSet.BLOODSTAINED_CHIVALRY, 4, "durationSeconds", Type.SECONDS, 10),
        BLOODSTAINED_4_REGENERATION_LEVEL(ArtifactSet.BLOODSTAINED_CHIVALRY, 4, "regenerationLevel", Type.LEVEL, 2),
        BLOODSTAINED_4_DAMAGE(ArtifactSet.BLOODSTAINED_CHIVALRY, 4, "damageBonusPercent", Type.PERCENT, 30),

        MAIDEN_2_HEALING_LEVEL_BONUS(ArtifactSet.MAIDEN_BELOVED, 2, "healingEffectLevelBonus", Type.INTEGER, 2),
        MAIDEN_4_EXTRA_HEAL(ArtifactSet.MAIDEN_BELOVED, 4, "extraHealing", Type.NUMBER, 2),
        MAIDEN_4_LOW_HEALTH_THRESHOLD(ArtifactSet.MAIDEN_BELOVED, 4, "lowHealthThresholdPercent", Type.PERCENT, 50),
        MAIDEN_4_LOW_HEALTH_MULTIPLIER(ArtifactSet.MAIDEN_BELOVED, 4, "lowHealthHealingMultiplier", Type.NUMBER, 2),
        MAIDEN_4_CRIT_DURATION(ArtifactSet.MAIDEN_BELOVED, 4, "criticalBuffDurationSeconds", Type.SECONDS, 10),
        MAIDEN_4_CRIT_MULTIPLIER(ArtifactSet.MAIDEN_BELOVED, 4, "criticalHealingMultiplier", Type.NUMBER, 2),

        VIRIDESCENT_2_MOVEMENT_SPEED(ArtifactSet.VIRIDESCENT_VENERER, 2, "movementSpeedBonusPercent", Type.PERCENT, 15),
        VIRIDESCENT_2_FALL_REDUCTION(ArtifactSet.VIRIDESCENT_VENERER, 2, "fallDamageReductionPercent", Type.PERCENT, 90),
        VIRIDESCENT_4_SWEEP_DAMAGE(ArtifactSet.VIRIDESCENT_VENERER, 4, "sweepDamagePercent", Type.PERCENT, 100),
        VIRIDESCENT_4_ARMOR_IGNORE(ArtifactSet.VIRIDESCENT_VENERER, 4, "armorIgnorePercent", Type.PERCENT, 25),
        VIRIDESCENT_4_ATTACK_SPEED(ArtifactSet.VIRIDESCENT_VENERER, 4, "attackSpeedBonusPercent", Type.PERCENT, 40),
        VIRIDESCENT_4_DURATION(ArtifactSet.VIRIDESCENT_VENERER, 4, "durationSeconds", Type.SECONDS, 10),

        ARCHAIC_PETRA_2_RESISTANCE_LEVEL(ArtifactSet.ARCHAIC_PETRA, 2, "resistanceLevel", Type.LEVEL, 1),
        ARCHAIC_PETRA_4_DAMAGE(ArtifactSet.ARCHAIC_PETRA, 4, "damageBonusPercent", Type.PERCENT, 35),
        ARCHAIC_PETRA_4_CRIT_RATE(ArtifactSet.ARCHAIC_PETRA, 4, "critRateBonusPercent", Type.PERCENT, 15),
        ARCHAIC_PETRA_4_CRIT_DAMAGE(ArtifactSet.ARCHAIC_PETRA, 4, "critDamageBonusPercent", Type.PERCENT, 20),
        ARCHAIC_PETRA_4_DURATION(ArtifactSet.ARCHAIC_PETRA, 4, "durationSeconds", Type.SECONDS, 10),

        RETRACING_BOLIDE_2_ARMOR(ArtifactSet.RETRACING_BOLIDE, 2, "armorBonusPercent", Type.PERCENT, 40),
        RETRACING_BOLIDE_2_RESISTANCE_LEVEL_BONUS(ArtifactSet.RETRACING_BOLIDE, 2, "resistanceLevelBonus", Type.INTEGER, 1),
        RETRACING_BOLIDE_4_DAMAGE_PER_ARMOR(ArtifactSet.RETRACING_BOLIDE, 4, "damageBonusPerArmorPointPercent", Type.PERCENT, 2),

        TENACITY_2_MAX_HEALTH(ArtifactSet.TENACITY_OF_THE_MILLELITH, 2, "maxHealth", Type.NUMBER, 20),
        TENACITY_4_DAMAGE_PER_PLAYER(ArtifactSet.TENACITY_OF_THE_MILLELITH, 4, "damageBonusPerOnlinePlayerPercent", Type.PERCENT, 5),
        TENACITY_4_HEALTH_PER_PLAYER(ArtifactSet.TENACITY_OF_THE_MILLELITH, 4, "maxHealthPerOnlinePlayer", Type.NUMBER, 2),

        PALE_FLAME_2_MELEE_DAMAGE(ArtifactSet.PALE_FLAME, 2, "meleeDamageBonusPercent", Type.PERCENT, 25),
        PALE_FLAME_4_DAMAGE_PER_STACK(ArtifactSet.PALE_FLAME, 4, "damageBonusPerStackPercent", Type.PERCENT, 10),
        PALE_FLAME_4_DURATION(ArtifactSet.PALE_FLAME, 4, "stackDurationSeconds", Type.SECONDS, 7),
        PALE_FLAME_4_MAX_STACKS(ArtifactSet.PALE_FLAME, 4, "maxStacks", Type.POSITIVE_INTEGER, 2),
        PALE_FLAME_4_TWO_PIECE_INCREASE(ArtifactSet.PALE_FLAME, 4, "twoPieceEffectIncreasePercent", Type.PERCENT, 100),

        SHIMENAWA_2_DAMAGE(ArtifactSet.SHIMENAWAS_REMINISCENCE, 2, "damageBonusPercent", Type.PERCENT, 20),
        SHIMENAWA_4_FOOD_THRESHOLD(ArtifactSet.SHIMENAWAS_REMINISCENCE, 4, "minimumFoodLevel", Type.INTEGER, 10),
        SHIMENAWA_4_DURATION(ArtifactSet.SHIMENAWAS_REMINISCENCE, 4, "durationSeconds", Type.SECONDS, 10),
        SHIMENAWA_4_FOOD_DRAIN(ArtifactSet.SHIMENAWAS_REMINISCENCE, 4, "foodDrainPerSecond", Type.INTEGER, 1),
        SHIMENAWA_4_DAMAGE(ArtifactSet.SHIMENAWAS_REMINISCENCE, 4, "damageBonusPercent", Type.PERCENT, 60),

        EMBLEM_2_ATTACK_SPEED(ArtifactSet.EMBLEM_OF_SEVERED_FATE, 2, "attackSpeedBonusPercent", Type.PERCENT, 20),
        EMBLEM_4_CRIT_DAMAGE_RATIO(ArtifactSet.EMBLEM_OF_SEVERED_FATE, 4, "critDamagePerAttackSpeedPercent", Type.NUMBER, 2),

        HUSK_2_ARMOR(ArtifactSet.HUSK_OF_OPULENT_DREAMS, 2, "armorBonusPercent", Type.PERCENT, 30),
        HUSK_2_MAGIC_REDUCTION(ArtifactSet.HUSK_OF_OPULENT_DREAMS, 2, "magicDamageReductionPercent", Type.PERCENT, 30),
        HUSK_4_TRIGGER_INTERVAL(ArtifactSet.HUSK_OF_OPULENT_DREAMS, 4, "stackTriggerIntervalSeconds", Type.SECONDS, 0.3),
        HUSK_4_MAX_STACKS(ArtifactSet.HUSK_OF_OPULENT_DREAMS, 4, "maxStacks", Type.POSITIVE_INTEGER, 5),
        HUSK_4_ARMOR_PER_STACK(ArtifactSet.HUSK_OF_OPULENT_DREAMS, 4, "armorBonusPerStackPercent", Type.PERCENT, 6),
        HUSK_4_DAMAGE_PER_STACK(ArtifactSet.HUSK_OF_OPULENT_DREAMS, 4, "damageBonusPerStackPercent", Type.PERCENT, 6),
        HUSK_4_DECAY_INTERVAL(ArtifactSet.HUSK_OF_OPULENT_DREAMS, 4, "stackDecayIntervalSeconds", Type.SECONDS, 6),

        OCEAN_CLAM_2_HEALING_LEVEL_BONUS(ArtifactSet.OCEAN_HUED_CLAM, 2, "healingEffectLevelBonus", Type.INTEGER, 2),
        OCEAN_CLAM_4_REGENERATION_LEVEL(ArtifactSet.OCEAN_HUED_CLAM, 4, "regenerationLevel", Type.LEVEL, 1),
        OCEAN_CLAM_4_RECORD_DURATION(ArtifactSet.OCEAN_HUED_CLAM, 4, "healingRecordDurationSeconds", Type.SECONDS, 10),
        OCEAN_CLAM_4_DAMAGE_PER_HEAL(ArtifactSet.OCEAN_HUED_CLAM, 4, "damageBonusPerHealthPointPercent", Type.PERCENT, 5),
        OCEAN_CLAM_4_DAMAGE_DURATION(ArtifactSet.OCEAN_HUED_CLAM, 4, "damageBonusDurationSeconds", Type.SECONDS, 10),

        VERMILLION_2_DAMAGE(ArtifactSet.VERMILLION_HEREAFTER, 2, "damageBonusPercent", Type.PERCENT, 20),
        VERMILLION_4_CRIT_DAMAGE(ArtifactSet.VERMILLION_HEREAFTER, 4, "criticalTriggerDamageBonusPercent", Type.PERCENT, 10),
        VERMILLION_4_HEALTH_LOSS_DAMAGE(ArtifactSet.VERMILLION_HEREAFTER, 4, "healthLossDamageBonusPerStackPercent", Type.PERCENT, 10),
        VERMILLION_4_MAX_STACKS(ArtifactSet.VERMILLION_HEREAFTER, 4, "maxStacks", Type.POSITIVE_INTEGER, 4),
        VERMILLION_4_DURATION(ArtifactSet.VERMILLION_HEREAFTER, 4, "durationSeconds", Type.SECONDS, 8),

        ECHOES_2_DAMAGE(ArtifactSet.ECHOES_OF_AN_OFFERING, 2, "damageBonusPercent", Type.PERCENT, 20),
        ECHOES_4_BASE_CHANCE(ArtifactSet.ECHOES_OF_AN_OFFERING, 4, "baseTriggerChancePercent", Type.PERCENT, 40),
        ECHOES_4_DAMAGE(ArtifactSet.ECHOES_OF_AN_OFFERING, 4, "triggeredDamageBonusPercent", Type.PERCENT, 70),
        ECHOES_4_FAILURE_CHANCE(ArtifactSet.ECHOES_OF_AN_OFFERING, 4, "failureChanceIncreasePercent", Type.PERCENT, 20),

        DEEPWOOD_2_SPELL_DAMAGE(ArtifactSet.DEEPWOOD_MEMORIES, 2, "spellDamageBonusPercent", Type.PERCENT, 35),
        DEEPWOOD_2_GLOWING_DURATION(ArtifactSet.DEEPWOOD_MEMORIES, 2, "glowingDurationSeconds", Type.SECONDS, 20),
        DEEPWOOD_4_COOLDOWN_REDUCTION(ArtifactSet.DEEPWOOD_MEMORIES, 4, "spellCooldownReductionPercent", Type.PERCENT, 25),
        DEEPWOOD_4_POISON_DURATION(ArtifactSet.DEEPWOOD_MEMORIES, 4, "poisonDurationSeconds", Type.SECONDS, 20),
        DEEPWOOD_4_POISON_LEVEL(ArtifactSet.DEEPWOOD_MEMORIES, 4, "poisonLevel", Type.LEVEL, 1),
        DEEPWOOD_4_DEBUFF_DURATION(ArtifactSet.DEEPWOOD_MEMORIES, 4, "debuffDurationSeconds", Type.SECONDS, 20),
        DEEPWOOD_4_WEAKNESS_LEVEL(ArtifactSet.DEEPWOOD_MEMORIES, 4, "weaknessLevel", Type.LEVEL, 1),
        DEEPWOOD_4_SLOWNESS_LEVEL(ArtifactSet.DEEPWOOD_MEMORIES, 4, "slownessLevel", Type.LEVEL, 1),
        DEEPWOOD_4_GLOWING_DAMAGE(ArtifactSet.DEEPWOOD_MEMORIES, 4, "glowingTargetSpellDamageBonusPercent", Type.PERCENT, 60),

        GILDED_2_ARMOR_IGNORE(ArtifactSet.GILDED_DREAMS, 2, "armorIgnorePercent", Type.PERCENT, 75),
        GILDED_4_DAMAGE_PER_ARMOR(ArtifactSet.GILDED_DREAMS, 4, "damageBonusPerTargetArmorPointPercent", Type.PERCENT, 1),

        DESERT_2_DAMAGE(ArtifactSet.DESERT_PAVILION_CHRONICLE, 2, "damageBonusPercent", Type.PERCENT, 20),
        DESERT_4_ATTACK_SPEED(ArtifactSet.DESERT_PAVILION_CHRONICLE, 4, "meleeAttackSpeedBonusPercent", Type.PERCENT, 10),
        DESERT_4_DAMAGE(ArtifactSet.DESERT_PAVILION_CHRONICLE, 4, "damageBonusPercent", Type.PERCENT, 40),
        DESERT_4_DURATION(ArtifactSet.DESERT_PAVILION_CHRONICLE, 4, "durationSeconds", Type.SECONDS, 15),

        PARADISE_LOST_2_SPELL_DAMAGE(ArtifactSet.FLOWER_OF_PARADISE_LOST, 2, "spellDamageBonusPercent", Type.PERCENT, 30),
        PARADISE_LOST_2_GLOWING_DURATION(ArtifactSet.FLOWER_OF_PARADISE_LOST, 2, "glowingDurationSeconds", Type.SECONDS, 20),
        PARADISE_LOST_4_COOLDOWN_REDUCTION(ArtifactSet.FLOWER_OF_PARADISE_LOST, 4, "spellCooldownReductionPercent", Type.PERCENT, 25),
        PARADISE_LOST_4_DEBUFF_DURATION(ArtifactSet.FLOWER_OF_PARADISE_LOST, 4, "debuffDurationSeconds", Type.SECONDS, 20),
        PARADISE_LOST_4_WEAKNESS_LEVEL(ArtifactSet.FLOWER_OF_PARADISE_LOST, 4, "weaknessLevel", Type.LEVEL, 1),
        PARADISE_LOST_4_SLOWNESS_LEVEL(ArtifactSet.FLOWER_OF_PARADISE_LOST, 4, "slownessLevel", Type.LEVEL, 1),
        PARADISE_LOST_4_GLOWING_DAMAGE(ArtifactSet.FLOWER_OF_PARADISE_LOST, 4, "glowingTargetSpellDamageBonusPercent", Type.PERCENT, 60),

        NYMPH_2_DAMAGE(ArtifactSet.NYMPHS_DREAM, 2, "damageBonusPercent", Type.PERCENT, 20),
        NYMPH_4_DURATION(ArtifactSet.NYMPHS_DREAM, 4, "stackDurationSeconds", Type.SECONDS, 8),
        NYMPH_4_ONE_STACK_DAMAGE(ArtifactSet.NYMPHS_DREAM, 4, "oneStackDamageBonusPercent", Type.PERCENT, 5),
        NYMPH_4_TWO_STACK_DAMAGE(ArtifactSet.NYMPHS_DREAM, 4, "twoStackDamageBonusPercent", Type.PERCENT, 10),
        NYMPH_4_THREE_STACK_DAMAGE(ArtifactSet.NYMPHS_DREAM, 4, "threeOrMoreStackDamageBonusPercent", Type.PERCENT, 25),

        VOURUKASHA_2_MAX_HEALTH(ArtifactSet.VOURUKASHAS_GLOW, 2, "maxHealth", Type.NUMBER, 20),
        VOURUKASHA_4_CRIT_DAMAGE(ArtifactSet.VOURUKASHAS_GLOW, 4, "critDamageBonusPercent", Type.PERCENT, 10),
        VOURUKASHA_4_STACK_INCREASE(ArtifactSet.VOURUKASHAS_GLOW, 4, "critDamageIncreasePerStackPercent", Type.PERCENT, 80),
        VOURUKASHA_4_DURATION(ArtifactSet.VOURUKASHAS_GLOW, 4, "stackDurationSeconds", Type.SECONDS, 5),
        VOURUKASHA_4_MAX_STACKS(ArtifactSet.VOURUKASHAS_GLOW, 4, "maxStacks", Type.POSITIVE_INTEGER, 5),

        MARECHAUSSEE_2_MELEE_DAMAGE(ArtifactSet.MARECHAUSSEE_HUNTER, 2, "meleeDamageBonusPercent", Type.PERCENT, 25),
        MARECHAUSSEE_4_CRIT_RATE_PER_STACK(ArtifactSet.MARECHAUSSEE_HUNTER, 4, "critRateBonusPerStackPercent", Type.PERCENT, 12),
        MARECHAUSSEE_4_DURATION(ArtifactSet.MARECHAUSSEE_HUNTER, 4, "stackDurationSeconds", Type.SECONDS, 5),
        MARECHAUSSEE_4_MAX_STACKS(ArtifactSet.MARECHAUSSEE_HUNTER, 4, "maxStacks", Type.POSITIVE_INTEGER, 3),

        GOLDEN_TROUPE_2_SPELL_DAMAGE(ArtifactSet.GOLDEN_TROUPE, 2, "spellDamageBonusPercent", Type.PERCENT, 35),
        GOLDEN_TROUPE_2_GLOWING_DURATION(ArtifactSet.GOLDEN_TROUPE, 2, "glowingDurationSeconds", Type.SECONDS, 20),
        GOLDEN_TROUPE_4_COOLDOWN_REDUCTION(ArtifactSet.GOLDEN_TROUPE, 4, "spellCooldownReductionPercent", Type.PERCENT, 25),
        GOLDEN_TROUPE_4_DEBUFF_DURATION(ArtifactSet.GOLDEN_TROUPE, 4, "debuffDurationSeconds", Type.SECONDS, 20),
        GOLDEN_TROUPE_4_WEAKNESS_LEVEL(ArtifactSet.GOLDEN_TROUPE, 4, "weaknessLevel", Type.LEVEL, 1),
        GOLDEN_TROUPE_4_SLOWNESS_LEVEL(ArtifactSet.GOLDEN_TROUPE, 4, "slownessLevel", Type.LEVEL, 1),
        GOLDEN_TROUPE_4_GLOWING_DAMAGE(ArtifactSet.GOLDEN_TROUPE, 4, "glowingTargetSpellDamageBonusPercent", Type.PERCENT, 60),

        SONG_2_HEALING_LEVEL_BONUS(ArtifactSet.SONG_OF_DAYS_PAST, 2, "healingEffectLevelBonus", Type.INTEGER, 2),
        SONG_4_DAMAGE_PER_STACK(ArtifactSet.SONG_OF_DAYS_PAST, 4, "damageBonusPerStackPercent", Type.PERCENT, 5),
        SONG_4_MAX_STACKS(ArtifactSet.SONG_OF_DAYS_PAST, 4, "maxStacks", Type.POSITIVE_INTEGER, 10),
        SONG_4_DURATION(ArtifactSet.SONG_OF_DAYS_PAST, 4, "durationSeconds", Type.SECONDS, 6),

        NIGHTTIME_WHISPERS_2_DAMAGE(ArtifactSet.NIGHTTIME_WHISPERS, 2, "damageBonusPercent", Type.PERCENT, 20),
        NIGHTTIME_WHISPERS_4_DURATION(ArtifactSet.NIGHTTIME_WHISPERS, 4, "durationSeconds", Type.SECONDS, 10),
        NIGHTTIME_WHISPERS_4_DAMAGE(ArtifactSet.NIGHTTIME_WHISPERS, 4, "damageBonusPercent", Type.PERCENT, 20),
        NIGHTTIME_WHISPERS_4_ARMORED_DAMAGE(ArtifactSet.NIGHTTIME_WHISPERS, 4, "armoredOrShieldDamageBonusPercent", Type.PERCENT, 40),

        HARMONIC_WHIMSY_2_SPELL_DAMAGE(ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, 2, "spellDamageBonusPercent", Type.PERCENT, 35),
        HARMONIC_WHIMSY_2_GLOWING_DURATION(ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, 2, "glowingDurationSeconds", Type.SECONDS, 20),
        HARMONIC_WHIMSY_4_COOLDOWN_REDUCTION(ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, 4, "spellCooldownReductionPercent", Type.PERCENT, 25),
        HARMONIC_WHIMSY_4_WITHER_DURATION(ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, 4, "witherDurationSeconds", Type.SECONDS, 20),
        HARMONIC_WHIMSY_4_WITHER_LEVEL(ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, 4, "witherLevel", Type.LEVEL, 1),
        HARMONIC_WHIMSY_4_DEBUFF_DURATION(ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, 4, "debuffDurationSeconds", Type.SECONDS, 20),
        HARMONIC_WHIMSY_4_WEAKNESS_LEVEL(ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, 4, "weaknessLevel", Type.LEVEL, 1),
        HARMONIC_WHIMSY_4_SLOWNESS_LEVEL(ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, 4, "slownessLevel", Type.LEVEL, 1),
        HARMONIC_WHIMSY_4_GLOWING_DAMAGE(ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, 4, "glowingTargetSpellDamageBonusPercent", Type.PERCENT, 60),

        UNFINISHED_REVERIE_2_DAMAGE(ArtifactSet.UNFINISHED_REVERIE, 2, "damageBonusPercent", Type.PERCENT, 20),
        UNFINISHED_REVERIE_4_DAMAGE(ArtifactSet.UNFINISHED_REVERIE, 4, "damageBonusPercent", Type.PERCENT, 80),
        UNFINISHED_REVERIE_4_DISABLED_DURATION(ArtifactSet.UNFINISHED_REVERIE, 4, "disabledDurationSeconds", Type.SECONDS, 3),

        CINDER_CITY_2_MAX_HEALTH_REDUCTION(ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, 2, "maxHealthReductionPercent", Type.PERCENT, 50),
        CINDER_CITY_2_COOLDOWN_BASE(ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, 2, "totemCooldownBaseSeconds", Type.SECONDS, 60),
        CINDER_CITY_2_COOLDOWN_HEALTH_OFFSET(ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, 2, "totemCooldownHealthOffset", Type.NUMBER, 10),
        CINDER_CITY_2_COOLDOWN_PER_HEALTH(ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, 2, "totemCooldownSecondsPerHealthPoint", Type.NUMBER, 8),
        CINDER_CITY_4_COOLDOWN_REDUCTION(ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, 4, "cooldownReductionPerDamageSeconds", Type.SECONDS, 5),
        CINDER_CITY_4_PARTY_DAMAGE(ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, 4, "partyDamageBonusPercent", Type.PERCENT, 80),
        CINDER_CITY_4_DURATION(ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, 4, "partyBuffDurationSeconds", Type.SECONDS, 20),

        OBSIDIAN_CODEX_2_DAMAGE(ArtifactSet.OBSIDIAN_CODEX, 2, "damageBonusPercent", Type.PERCENT, 30),
        OBSIDIAN_CODEX_4_CRIT_RATE(ArtifactSet.OBSIDIAN_CODEX, 4, "critRateBonusPercent", Type.PERCENT, 40),
        OBSIDIAN_CODEX_4_CRIT_DAMAGE(ArtifactSet.OBSIDIAN_CODEX, 4, "critDamageBonusPercent", Type.PERCENT, 50),

        LONG_NIGHT_2_UNDEAD_DAMAGE(ArtifactSet.LONG_NIGHTS_OATH, 2, "undeadDamageBonusPercent", Type.PERCENT, 80),
        LONG_NIGHT_4_HEALTH_THRESHOLD(ArtifactSet.LONG_NIGHTS_OATH, 4, "targetHealthThresholdPercent", Type.PERCENT, 50),

        DEEP_GALLERIES_2_DAMAGE(ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, 2, "damageBonusPercent", Type.PERCENT, 20),
        DEEP_GALLERIES_4_MELEE_DAMAGE(ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, 4, "meleeDamageBonusPercent", Type.PERCENT, 50),
        DEEP_GALLERIES_4_CRIT_DAMAGE(ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, 4, "critDamageBonusPercent", Type.PERCENT, 80),
        DEEP_GALLERIES_4_HITS_REQUIRED(ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, 4, "hitsRequiredForForcedCritical", Type.POSITIVE_INTEGER, 10),

        SKYS_UNVEILING_2_SPELL_DAMAGE(ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING, 2, "spellDamageBonusPercent", Type.PERCENT, 35),
        SKYS_UNVEILING_2_GLOWING_DURATION(ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING, 2, "glowingDurationSeconds", Type.SECONDS, 20),
        SKYS_UNVEILING_4_COOLDOWN_REDUCTION(ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING, 4, "spellCooldownReductionPercent", Type.PERCENT, 25),
        SKYS_UNVEILING_4_DEBUFF_DURATION(ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING, 4, "debuffDurationSeconds", Type.SECONDS, 20),
        SKYS_UNVEILING_4_WEAKNESS_LEVEL(ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING, 4, "weaknessLevel", Type.LEVEL, 1),
        SKYS_UNVEILING_4_SLOWNESS_LEVEL(ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING, 4, "slownessLevel", Type.LEVEL, 1),
        SKYS_UNVEILING_4_GLOWING_DAMAGE(ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING, 4, "glowingTargetSpellDamageBonusPercent", Type.PERCENT, 60),

        SILKEN_MOON_2_ATTACK_SPEED(ArtifactSet.SILKEN_MOONS_SERENADE, 2, "attackSpeedBonusPercent", Type.PERCENT, 25),
        SILKEN_MOON_4_DAMAGE_PER_STACK(ArtifactSet.SILKEN_MOONS_SERENADE, 4, "damageBonusPerStackPercent", Type.PERCENT, 5),
        SILKEN_MOON_4_ARMOR_IGNORE_PER_STACK(ArtifactSet.SILKEN_MOONS_SERENADE, 4, "armorIgnorePerStackPercent", Type.PERCENT, 10),
        SILKEN_MOON_4_MAX_STACKS(ArtifactSet.SILKEN_MOONS_SERENADE, 4, "maxStacks", Type.POSITIVE_INTEGER, 10),
        SILKEN_MOON_4_DURATION(ArtifactSet.SILKEN_MOONS_SERENADE, 4, "durationSeconds", Type.SECONDS, 8),

        AUBADE_2_ARMOR_IGNORE(ArtifactSet.AUBADE_OF_MORNINGSTAR_AND_MOON, 2, "armorIgnorePercent", Type.PERCENT, 75),
        AUBADE_4_DAMAGE(ArtifactSet.AUBADE_OF_MORNINGSTAR_AND_MOON, 4, "damageBonusPercent", Type.PERCENT, 20),
        AUBADE_4_NIGHT_DAMAGE(ArtifactSet.AUBADE_OF_MORNINGSTAR_AND_MOON, 4, "nightOrNonOverworldDamageBonusPercent", Type.PERCENT, 40),

        RISING_WINDS_2_DAMAGE(ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, 2, "damageBonusPercent", Type.PERCENT, 20),
        RISING_WINDS_4_DAMAGE(ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, 4, "damageBonusPercent", Type.PERCENT, 25),
        RISING_WINDS_4_DURATION(ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, 4, "durationSeconds", Type.SECONDS, 6),
        RISING_WINDS_4_DICTIONARY_DAMAGE(ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, 4, "dictionaryDamageBonusPercent", Type.PERCENT, 10),
        RISING_WINDS_4_DICTIONARY_CRIT_RATE(ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, 4, "dictionaryCritRateBonusPercent", Type.PERCENT, 20),
        RISING_WINDS_4_DICTIONARY_ATTACK_SPEED(ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, 4, "dictionaryAttackSpeedBonusPercent", Type.PERCENT, 20),

        CELESTIAL_GIFT_2_SPELL_DAMAGE(ArtifactSet.CELESTIAL_GIFT, 2, "spellDamageBonusPercent", Type.PERCENT, 35),
        CELESTIAL_GIFT_2_GLOWING_DURATION(ArtifactSet.CELESTIAL_GIFT, 2, "glowingDurationSeconds", Type.SECONDS, 20),
        CELESTIAL_GIFT_4_COOLDOWN_REDUCTION(ArtifactSet.CELESTIAL_GIFT, 4, "spellCooldownReductionPercent", Type.PERCENT, 25),
        CELESTIAL_GIFT_4_WITHER_DURATION(ArtifactSet.CELESTIAL_GIFT, 4, "witherDurationSeconds", Type.SECONDS, 20),
        CELESTIAL_GIFT_4_WITHER_LEVEL(ArtifactSet.CELESTIAL_GIFT, 4, "witherLevel", Type.LEVEL, 1),
        CELESTIAL_GIFT_4_DEBUFF_DURATION(ArtifactSet.CELESTIAL_GIFT, 4, "debuffDurationSeconds", Type.SECONDS, 20),
        CELESTIAL_GIFT_4_WEAKNESS_LEVEL(ArtifactSet.CELESTIAL_GIFT, 4, "weaknessLevel", Type.LEVEL, 1),
        CELESTIAL_GIFT_4_SLOWNESS_LEVEL(ArtifactSet.CELESTIAL_GIFT, 4, "slownessLevel", Type.LEVEL, 1),
        CELESTIAL_GIFT_4_GLOWING_DAMAGE(ArtifactSet.CELESTIAL_GIFT, 4, "glowingTargetSpellDamageBonusPercent", Type.PERCENT, 60),

        DISENCHANTMENT_2_DAMAGE(ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, 2, "damageBonusPercent", Type.PERCENT, 20),
        DISENCHANTMENT_4_ARMORED_DAMAGE(ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, 4, "armoredTargetDamageBonusPercent", Type.PERCENT, 20),
        DISENCHANTMENT_4_CRIT_RATE(ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, 4, "armoredTargetCritRateBonusPercent", Type.PERCENT, 20),
        DISENCHANTMENT_4_DAMAGE_PER_STACK(ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, 4, "nonCriticalDamageBonusPerStackPercent", Type.PERCENT, 5),
        DISENCHANTMENT_4_MAX_DAMAGE(ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, 4, "maximumNonCriticalDamageBonusPercent", Type.PERCENT, 40),
        DISENCHANTMENT_4_DURATION(ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, 4, "durationSeconds", Type.SECONDS, 10),

        SCARLET_PROOF_2_SPELL_DAMAGE(ArtifactSet.SCARLET_PROOF, 2, "spellDamageBonusPercent", Type.PERCENT, 35),
        SCARLET_PROOF_2_GLOWING_DURATION(ArtifactSet.SCARLET_PROOF, 2, "glowingDurationSeconds", Type.SECONDS, 20),
        SCARLET_PROOF_4_COOLDOWN_REDUCTION(ArtifactSet.SCARLET_PROOF, 4, "spellCooldownReductionPercent", Type.PERCENT, 25),
        SCARLET_PROOF_4_NO_HEAL_DURATION(ArtifactSet.SCARLET_PROOF, 4, "healingBlockDurationSeconds", Type.SECONDS, 20),
        SCARLET_PROOF_4_DEBUFF_DURATION(ArtifactSet.SCARLET_PROOF, 4, "debuffDurationSeconds", Type.SECONDS, 20),
        SCARLET_PROOF_4_WEAKNESS_LEVEL(ArtifactSet.SCARLET_PROOF, 4, "weaknessLevel", Type.LEVEL, 1),
        SCARLET_PROOF_4_SLOWNESS_LEVEL(ArtifactSet.SCARLET_PROOF, 4, "slownessLevel", Type.LEVEL, 1),
        SCARLET_PROOF_4_GLOWING_DAMAGE(ArtifactSet.SCARLET_PROOF, 4, "glowingTargetSpellDamageBonusPercent", Type.PERCENT, 60),

        FURNACE_HEART_2_DAMAGE(ArtifactSet.HEART_OF_THE_FURNACE, 2, "damageBonusPercent", Type.PERCENT, 20),
        FURNACE_HEART_4_DAMAGE_PER_COAL_STACK(ArtifactSet.HEART_OF_THE_FURNACE, 4, "damageBonusPerCoalStackPercent", Type.PERCENT, 2.5);

        private final ArtifactSet set;
        private final int pieces;
        private final String key;
        private final Type type;
        private final double defaultValue;
        private ModConfigSpec.ConfigValue<? extends Number> configValue;

        Value(ArtifactSet set, int pieces, String key, Type type, double defaultValue) {
            this.set = set;
            this.pieces = pieces;
            this.key = key;
            this.type = type;
            this.defaultValue = defaultValue;
        }

        private void define(ModConfigSpec.Builder builder) {
            builder.comment(
                    "Unit: " + type.englishUnit + ". Default: " + formatNumber(defaultValue) + ".",
                    pieces + "件套：" + chineseDescription(key) + "。",
                    "单位：" + type.chineseUnit + "；默认值：" + formatNumber(defaultValue) + "。"
            );
            if (type.integer) {
                configValue = builder.defineInRange(key, (int) Math.round(defaultValue), (int) type.minimum, (int) type.maximum);
            } else {
                configValue = builder.defineInRange(key, defaultValue, type.minimum, type.maximum);
            }
        }

        public ArtifactSet set() {
            return set;
        }

        public int pieces() {
            return pieces;
        }

        private double value() {
            return configValue == null ? defaultValue : configValue.get().doubleValue();
        }
    }

    private enum Type {
        PERCENT("percent", "百分比（填写 20 代表 20%）", false, 0, 10000),
        SECONDS("seconds", "秒", false, 0, 86400),
        NUMBER("points or multiplier", "数值或倍率", false, 0, 1000000),
        INTEGER("count", "整数", true, 0, 100000),
        POSITIVE_INTEGER("count", "正整数", true, 1, 100000),
        LEVEL("displayed effect level", "显示的效果等级（填写 1 代表 I 级）", true, 1, 255);

        private final String englishUnit;
        private final String chineseUnit;
        private final boolean integer;
        private final double minimum;
        private final double maximum;

        Type(String englishUnit, String chineseUnit, boolean integer, double minimum, double maximum) {
            this.englishUnit = englishUnit;
            this.chineseUnit = chineseUnit;
            this.integer = integer;
            this.minimum = minimum;
            this.maximum = maximum;
        }
    }

    private static String chineseDescription(String key) {
        return switch (key) {
            case "additionalDamageReductionPercent" -> "额外伤害减免";
            case "armorBonusPercent" -> "护甲加成";
            case "armorBonusPerStackPercent" -> "每层护甲加成";
            case "armoredOrShieldDamageBonusPercent" -> "拥有护甲值或副手持盾时的伤害加成";
            case "armoredTargetCritRateBonusPercent" -> "对有护甲目标的暴击率加成";
            case "armoredTargetDamageBonusPercent" -> "对有护甲目标的伤害加成";
            case "armorIgnorePercent" -> "无视目标护甲";
            case "armorIgnorePerStackPercent" -> "每层无视目标护甲";
            case "attackSpeedBonusPercent" -> "攻击速度加成";
            case "baseTriggerChancePercent" -> "基础触发概率";
            case "boostedDamageReductionPercent" -> "强化后的伤害减免";
            case "cooldownReductionPerDamageSeconds" -> "每次造成伤害减少的冷却时间";
            case "cooldownSeconds" -> "冷却时间";
            case "critDamageBonusPercent" -> "暴击伤害加成";
            case "critDamageIncreasePerStackPercent" -> "每层暴击伤害提升";
            case "critDamagePerAttackSpeedPercent" -> "每 1% 攻击速度转化的暴击伤害";
            case "criticalBuffDurationSeconds" -> "暴击触发增益持续时间";
            case "criticalHealingMultiplier" -> "暴击期间的治疗倍率";
            case "criticalTriggerDamageBonusPercent" -> "暴击触发后的伤害加成";
            case "critRateBonusPercent" -> "暴击率加成";
            case "critRateBonusPerStackPercent" -> "每层暴击率加成";
            case "damageBonusDurationSeconds" -> "伤害加成持续时间";
            case "damageBonusPerArmorPointPercent" -> "每点自身护甲提供的伤害加成";
            case "damageBonusPercent" -> "伤害加成";
            case "damageBonusPerCoalStackPercent" -> "物品栏或背包中每组煤炭提供的伤害加成";
            case "damageBonusPerHealthPointPercent" -> "每点记录的治疗量提供的伤害加成";
            case "damageBonusPerOnlinePlayerPercent" -> "每名在线玩家提供的伤害加成";
            case "damageBonusPerStackPercent" -> "每层伤害加成";
            case "damageBonusPerTargetArmorPointPercent" -> "目标每点护甲提供的伤害加成";
            case "damageReductionPercent" -> "伤害减免";
            case "debuffDurationSeconds" -> "负面效果持续时间";
            case "dictionaryAttackSpeedBonusPercent" -> "装备小魔女辞典时的额外攻击速度";
            case "dictionaryCritRateBonusPercent" -> "装备小魔女辞典时的额外暴击率";
            case "dictionaryDamageBonusPercent" -> "装备小魔女辞典时的额外伤害";
            case "disabledDurationSeconds" -> "效果失效持续时间";
            case "durationSeconds" -> "效果持续时间";
            case "extraHealing" -> "额外治疗量";
            case "failureChanceIncreasePercent" -> "未触发时下次触发概率增加";
            case "fallDamageReductionPercent" -> "摔落伤害减免";
            case "fireSpellDamageReductionPercent" -> "火焰法术伤害减免";
            case "foodDrainPerSecond" -> "每秒消耗的饱食度";
            case "freezeDurationSeconds" -> "冻结持续时间";
            case "glowingDurationSeconds" -> "发光持续时间";
            case "glowingTargetSpellDamageBonusPercent" -> "对发光目标的额外法术伤害加成";
            case "healingBlockDurationSeconds" -> "禁止回复生命持续时间";
            case "healingEffectLevelBonus" -> "治疗类效果等级加成";
            case "healingRecordDurationSeconds" -> "治疗量记录时间";
            case "healPerExperienceOrb" -> "每个经验球恢复的生命值";
            case "healthLossDamageBonusPerStackPercent" -> "每层因生命值减少获得的伤害加成";
            case "healthThresholdPercent" -> "生命值触发阈值";
            case "hitsRequiredForForcedCritical" -> "必定暴击所需累计伤害次数";
            case "instantHealLevel" -> "瞬间治疗效果等级";
            case "knockbackResistancePercent" -> "击退抗性";
            case "lightningSpellDamageReductionPercent" -> "雷电法术伤害减免";
            case "lowHealthHealingMultiplier" -> "低生命值时的治疗倍率";
            case "lowHealthThresholdPercent" -> "低生命值判定阈值";
            case "magicDamageReductionPercent" -> "魔法伤害减免";
            case "maxHealth" -> "最大生命值加成";
            case "maxHealthPerOnlinePlayer" -> "每名在线玩家提供的最大生命值";
            case "maxHealthReductionPercent" -> "最大生命值降低";
            case "maximumNonCriticalDamageBonusPercent" -> "未暴击累积伤害加成上限";
            case "maxMana" -> "最大法力值加成";
            case "maxStacks" -> "最大叠加层数";
            case "meleeAttackSpeedBonusPercent" -> "近战攻击速度加成";
            case "meleeDamageBonusPercent" -> "近战伤害加成";
            case "minimumFoodLevel" -> "触发所需最低饱食度";
            case "movementSpeedBonusPercent" -> "移动速度加成";
            case "nextProjectileDamageBonusPercent" -> "下一次弹射物伤害加成";
            case "nightOrNonOverworldDamageBonusPercent" -> "夜晚或非主世界维度时的额外伤害加成";
            case "nonCriticalDamageBonusPerStackPercent" -> "每次未暴击获得的伤害加成";
            case "nonSpellDamageBonusPercent" -> "非法术伤害加成";
            case "oneStackDamageBonusPercent" -> "一层时的伤害加成";
            case "partyBuffDurationSeconds" -> "自身与附近玩家的增益持续时间";
            case "partyDamageBonusPercent" -> "自身与附近玩家的伤害加成";
            case "poisonDurationSeconds" -> "中毒持续时间";
            case "poisonLevel" -> "中毒效果等级";
            case "projectileAndSpellDamageBonusPercent" -> "弹射物与法术伤害加成";
            case "projectileDamageBonusPercent" -> "弹射物伤害加成";
            case "regenerationLevel" -> "生命恢复效果等级";
            case "resistanceLevel" -> "抗性提升效果等级";
            case "resistanceLevelBonus" -> "抗性提升效果等级加成";
            case "slownessLevel" -> "缓慢效果等级";
            case "soulFireDurationSeconds" -> "灵魂火焰燃烧持续时间";
            case "spellCastTimeReductionPercent" -> "法术吟唱时间缩减";
            case "spellCooldownReductionPercent" -> "法术冷却缩减";
            case "spellDamageBonusPercent" -> "法术伤害加成";
            case "stackDecayIntervalSeconds" -> "未触发时每层衰减间隔";
            case "stackDurationSeconds" -> "每层效果持续时间";
            case "stackTriggerIntervalSeconds" -> "叠层触发最短间隔";
            case "sweepDamagePercent" -> "横扫伤害比例";
            case "targetHealthThresholdPercent" -> "目标生命值判定阈值";
            case "threeOrMoreStackDamageBonusPercent" -> "三层及以上时的伤害加成";
            case "totemCooldownBaseSeconds" -> "不死图腾效果基础冷却时间";
            case "totemCooldownHealthOffset" -> "冷却公式中的最大生命值偏移量";
            case "totemCooldownSecondsPerHealthPoint" -> "冷却公式中每点生命值对应的秒数";
            case "triggeredDamageBonusPercent" -> "触发后的伤害加成";
            case "twoPieceEffectIncreasePercent" -> "二件套效果提升比例";
            case "twoStackDamageBonusPercent" -> "两层时的伤害加成";
            case "undeadDamageBonusPercent" -> "对亡灵生物的伤害加成";
            case "waterOrRainDamageBonusPercent" -> "水中或雨天时的伤害加成";
            case "waterOrRainDamageReductionPercent" -> "水中或雨天时的伤害减免";
            case "weaknessLevel" -> "虚弱效果等级";
            case "witherDurationSeconds" -> "凋零持续时间";
            case "witherLevel" -> "凋零效果等级";
            default -> throw new IllegalArgumentException("缺少配置项的中文说明：" + key);
        };
    }

    private static String chineseSetName(ArtifactSet set) {
        return switch (set) {
            case INITIATE -> "初学者";
            case LUCKY -> "幸运儿";
            case ADVENTURER -> "冒险家";
            case TRAVELING_DOCTOR -> "游医";
            case RESOLUTION_OF_SOJOURNER -> "行者之心";
            case TINY_MIRACLE -> "奇迹";
            case BERSERKER -> "战狂";
            case INSTRUCTOR -> "教官";
            case THE_EXILE -> "流放者";
            case DEFENDERS_WILL -> "守护之心";
            case BRAVE_HEART -> "勇士之心";
            case MARTIAL_ARTIST -> "武人";
            case GAMBLER -> "赌徒";
            case SCHOLAR -> "学士";
            case GLADIATORS_FINALE -> "角斗士的终幕礼";
            case WANDERERS_TROUPE -> "流浪大地的乐团";
            case NOBLESSE_OBLIGE -> "昔日宗室之仪";
            case BLOODSTAINED_CHIVALRY -> "染血的骑士道";
            case MAIDEN_BELOVED -> "被怜爱的少女";
            case VIRIDESCENT_VENERER -> "翠绿之影";
            case ARCHAIC_PETRA -> "悠古的磐岩";
            case RETRACING_BOLIDE -> "逆飞的流星";
            case TENACITY_OF_THE_MILLELITH -> "千岩牢固";
            case PALE_FLAME -> "苍白之火";
            case SHIMENAWAS_REMINISCENCE -> "追忆之注连";
            case EMBLEM_OF_SEVERED_FATE -> "绝缘之旗印";
            case HUSK_OF_OPULENT_DREAMS -> "华馆梦醒形骸记";
            case OCEAN_HUED_CLAM -> "海染砗磲";
            case VERMILLION_HEREAFTER -> "辰砂往生录";
            case ECHOES_OF_AN_OFFERING -> "来歆余响";
            case DEEPWOOD_MEMORIES -> "深林的记忆";
            case GILDED_DREAMS -> "饰金之梦";
            case DESERT_PAVILION_CHRONICLE -> "沙上楼阁史话";
            case FLOWER_OF_PARADISE_LOST -> "乐园遗落之花";
            case NYMPHS_DREAM -> "水仙之梦";
            case VOURUKASHAS_GLOW -> "花海甘露之光";
            case MARECHAUSSEE_HUNTER -> "逐影猎人";
            case GOLDEN_TROUPE -> "黄金剧团";
            case SONG_OF_DAYS_PAST -> "昔时之歌";
            case NIGHTTIME_WHISPERS -> "回声之林夜话";
            case FRAGMENT_OF_HARMONIC_WHIMSY -> "谐律异想断章";
            case UNFINISHED_REVERIE -> "未竟的遐思";
            case SCROLL_OF_THE_HERO_OF_CINDER_CITY -> "烬城勇者绘卷";
            case OBSIDIAN_CODEX -> "黑曜秘典";
            case LONG_NIGHTS_OATH -> "长夜之誓";
            case FINALE_OF_THE_DEEP_GALLERIES -> "深廊终曲";
            case NIGHT_OF_THE_SKYS_UNVEILING -> "穹境示现之夜";
            case SILKEN_MOONS_SERENADE -> "纺月的夜歌";
            case AUBADE_OF_MORNINGSTAR_AND_MOON -> "晨星与月的晓歌";
            case A_DAY_CARVED_FROM_RISING_WINDS -> "风起之日";
            case CELESTIAL_GIFT -> "天之美赐";
            case DISENCHANTMENT_IN_DEEP_SHADOW -> "影中沉凝的幻灭";
            case SCARLET_PROOF -> "血红之证";
            case HEART_OF_THE_FURNACE -> "炉火融炼之心";
            case THUNDERSOOTHER -> "平息鸣雷的尊者";
            case THUNDERING_FURY -> "如雷的盛怒";
            case LAVAWALKER -> "渡过烈火的贤人";
            case CRIMSON_WITCH_OF_FLAMES -> "炽烈的炎之魔女";
            case BLIZZARD_STRAYER -> "冰风迷途的勇士";
            case HEART_OF_DEPTH -> "沉沦之心";
            case PRAYERS_FOR_THUNDER -> "祭雷之人";
            case PRAYERS_FOR_DESTINY -> "祭水之人";
            case PRAYERS_FOR_ILLUMINATION -> "祭火之人";
            case PRAYERS_TO_SPRINGTIME -> "祭冰之人";
        };
    }

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment(
                "Artifact set bonus values.",
                "Percent values use 20 for 20%, and durations use seconds.",
                "This global common config is stored in the config directory.",
                "It is not synchronized over the network; multiplayer clients and servers should use the same file.",
                "圣遗物二件套与四件套效果数值配置。",
                "百分比填写 20 代表 20%，持续时间统一使用秒。",
                "为兼容已有配置，分组名与配置键保持英文；每个配置项上方均有中文说明。",
                "这是保存在游戏实例 config 目录中的全局 COMMON 配置。",
                "本配置不会通过网络同步；多人游戏的客户端与服务端应使用相同文件。"
        );
        List<ArtifactSet> configuredSets = new ArrayList<>();
        for (Value value : Value.values()) {
            if (!configuredSets.contains(value.set)) {
                configuredSets.add(value.set);
            }
        }
        builder.push("artifactSets");
        for (ArtifactSet set : configuredSets) {
            builder.comment(
                    "Artifact set ID: " + set.id() + ".",
                    "套装中文名：" + chineseSetName(set) + "。"
            ).push(set.id());
            for (int pieces : List.of(2, 4)) {
                boolean hasValues = false;
                for (Value value : Value.values()) {
                    if (value.set == set && value.pieces == pieces) {
                        hasValues = true;
                        break;
                    }
                }
                if (!hasValues) {
                    continue;
                }
                builder.push(pieces == 2 ? "twoPiece" : "fourPiece");
                for (Value value : Value.values()) {
                    if (value.set == set && value.pieces == pieces) {
                        value.define(builder);
                    }
                }
                builder.pop();
            }
            builder.pop();
        }
        builder.pop();
        SPEC = builder.build();
    }

    private ArtifactSetConfig() {
    }

    public static double number(Value value) {
        return value.value();
    }

    public static int integer(Value value) {
        return Math.max(0, (int) Math.round(value.value()));
    }

    public static int effectAmplifier(Value value) {
        return Math.max(0, integer(value) - 1);
    }

    public static float percent(Value value) {
        return (float) (value.value() / 100.0D);
    }

    public static float multiplier(Value value) {
        return 1.0F + percent(value);
    }

    public static float remaining(Value value) {
        return Math.max(0.0F, 1.0F - percent(value));
    }

    public static int ticks(Value value) {
        return Math.max(0, (int) Math.round(value.value() * 20.0D));
    }

    public static Object[] tooltipArguments(ArtifactSet set, int pieces) {
        List<Object> arguments = new ArrayList<>();
        for (Value value : Value.values()) {
            if (value.set == set && value.pieces == pieces) {
                arguments.add(value.type == Type.LEVEL ? formatLevel(integer(value)) : formatNumber(value.value()));
            }
        }
        return arguments.toArray();
    }

    private static String formatLevel(int level) {
        if (level <= 0) {
            return "0";
        }
        if (level <= 10) {
            String[] roman = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
            return roman[level];
        }
        return Integer.toString(level);
    }

    private static String formatNumber(double value) {
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }
}
