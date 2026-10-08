package com.guoche.teyvat_artifacts;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.EnumMap;
import java.util.Map;

public final class TeyvatArtifactsConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue GIVE_INITIATE_ARTIFACTS_ON_FIRST_SPAWN;
    public static final ModConfigSpec.BooleanValue ENABLE_ALL_LEYLINE_SPAWNER_GENERATION;
    public static final ModConfigSpec.ConfigValue<String> DEFAULT_LEYLINE_MONSTER_POOL;
    public static final ModConfigSpec.BooleanValue GLOW_LEYLINE_TRIAL_MOBS;
    public static final ModConfigSpec.ConfigValue<List<? extends Number>> SUBSTAT_COUNTS;
    public static final ModConfigSpec.ConfigValue<List<? extends Number>> SUBSTAT_COUNT_BONUSES;
    public static final ModConfigSpec.ConfigValue<List<? extends Number>> SUBSTAT_STAR_MULTIPLIERS;
    public static final ModConfigSpec.ConfigValue<List<? extends Number>> SUBSTAT_STAR_VARIANCES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> SUBSTAT_POOL;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> MAIN_STAT_DEFINITIONS;
    public static final Map<ArtifactSlot, ModConfigSpec.ConfigValue<List<? extends String>>> MAIN_STAT_POOLS = new EnumMap<>(ArtifactSlot.class);
    public static final ModConfigSpec.BooleanValue REQUIRE_LEYLINE_ADVANCEMENTS;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LEYLINE_REQUIRED_ADVANCEMENTS;
    public static final ModConfigSpec.BooleanValue REQUIRE_LEYLINE_ACTIVATION_ITEM;
    public static final ModConfigSpec.ConfigValue<String> LEYLINE_ACTIVATION_ITEM;
    public static final ModConfigSpec.BooleanValue CONSUME_LEYLINE_ACTIVATION_ITEM;
    public static final ModConfigSpec.IntValue LEYLINE_REWARD_XP_COST;
    public static final ModConfigSpec.DoubleValue LEYLINE_FIVE_STAR_CHANCE_PERCENT;
    public static final ModConfigSpec.IntValue ALCHEMY_AUTOMATIC_INTERVAL_TICKS;

    public enum LeylineSpawner {
        MONDSTADT_SUMMER_COURTYARD(
                "mondstadt_leyline_spawner",
                "Mondstadt Leyline Spawner: Summer Courtyard",
                "蒙德地脉刷怪笼：仲夏庭园",
                List.of("minecraft:meadow")
        ),
        MONDSTADT_RIDGE_WATCH(
                "mondstadt_ridge_watch_leyline_spawner",
                "Mondstadt Leyline Spawner: Ridge Watch",
                "蒙德地脉刷怪笼：山脊守望",
                List.of("minecraft:windswept_hills")
        ),
        MONDSTADT_THORNY_CROWN(
                "mondstadt_thorny_crown_leyline_spawner",
                "Mondstadt Leyline Spawner: Thorny Crown of the Mountain Wind",
                "蒙德地脉刷怪笼：山风的荆冕",
                List.of("minecraft:jagged_peaks")
        ),
        MONDSTADT_PEAK_OF_VINDAGNYR(
                "mondstadt_peak_vindagnyr_leyline_spawner",
                "Mondstadt Leyline Spawner: Peak of Vindagnyr",
                "蒙德地脉刷怪笼：芬德尼尔之顶",
                List.of("minecraft:frozen_peaks")
        ),
        MONDSTADT_VALLEY_OF_REMEMBRANCE(
                "mondstadt_valley_remembrance_leyline_spawner",
                "Mondstadt Leyline Spawner: Valley of Remembrance",
                "蒙德地脉刷怪笼：铭记之谷",
                List.of("minecraft:windswept_forest")
        ),
        LIYUE_CLEAR_POOL(
                "liyue_clear_pool_leyline_spawner",
                "Liyue Leyline Spawner: Clear Pool and Mountain Cavern",
                "璃月地脉刷怪笼：华池岩岫",
                List.of("minecraft:savanna_plateau")
        ),
        LIYUE_DOMAIN_OF_GUYUN(
                "liyue_domain_guyun_leyline_spawner",
                "Liyue Leyline Spawner: Domain of Guyun",
                "璃月地脉刷怪笼：孤云凌霄之处",
                List.of("minecraft:stony_peaks")
        ),
        LIYUE_LOST_VALLEY(
                "liyue_lost_valley_leyline_spawner",
                "Liyue Leyline Spawner: The Lost Valley",
                "璃月地脉刷怪笼：岩中幽谷",
                List.of("minecraft:windswept_gravelly_hills")
        ),
        LIYUE_ZHOU_FORMULA(
                "liyue_zhou_formula_leyline_spawner",
                "Liyue Leyline Spawner: Hidden Palace of Zhou Formula",
                "璃月地脉刷怪笼：无妄引咎密宫",
                List.of("minecraft:nether_wastes")
        ),
        INAZUMA_MOMIJI_DYED_COURT(
                "inazuma_momiji_dyed_court_leyline_spawner",
                "Inazuma Leyline Spawner: Momiji-Dyed Court",
                "稻妻地脉刷怪笼：椛染之庭",
                List.of("minecraft:cherry_grove")
        ),
        INAZUMA_SLUMBERING_COURT(
                "inazuma_slumbering_court_leyline_spawner",
                "Inazuma Leyline Spawner: Slumbering Court",
                "稻妻地脉刷怪笼：沉眠之庭",
                List.of("minecraft:warm_ocean")
        ),
        SUMERU_MOLTEN_IRON_FORTRESS(
                "sumeru_molten_iron_fortress_leyline_spawner",
                "Sumeru Leyline Spawner: Molten Iron Fortress",
                "须弥地脉刷怪笼：熔铁的孤塞",
                List.of("minecraft:wooded_badlands")
        ),
        SUMERU_SOLITARY_ENLIGHTENMENT(
                "sumeru_solitary_enlightenment_leyline_spawner",
                "Sumeru Leyline Spawner: Spire of Solitary Enlightenment",
                "须弥地脉刷怪笼：缘觉塔",
                List.of("minecraft:jungle")
        ),
        SUMERU_CITY_OF_GOLD(
                "sumeru_city_gold_leyline_spawner",
                "Sumeru Leyline Spawner: City of Gold",
                "须弥地脉刷怪笼：赤金的城墟",
                List.of("minecraft:desert")
        ),
        FONTAINE_WATERFALL_WEN(
                "fontaine_waterfall_wen_leyline_spawner",
                "Fontaine Leyline Spawner: Waterfall Wen",
                "枫丹地脉刷怪笼：临瀑之城",
                List.of("minecraft:stony_shore")
        ),
        FONTAINE_DENOUEMENT_OF_SIN(
                "fontaine_denouement_sin_leyline_spawner",
                "Fontaine Leyline Spawner: Denouement of Sin",
                "枫丹地脉刷怪笼：罪祸的终末",
                List.of("minecraft:beach")
        ),
        FONTAINE_FADED_THEATER(
                "fontaine_faded_theater_leyline_spawner",
                "Fontaine Leyline Spawner: Faded Theater",
                "枫丹地脉刷怪笼：褪色的剧场",
                List.of("minecraft:deep_ocean", "minecraft:cold_ocean", "minecraft:deep_cold_ocean")
        ),
        NATLAN_DERELICT_MASONRY_DOCK(
                "natlan_derelict_masonry_dock_leyline_spawner",
                "Natlan Leyline Spawner: Derelict Masonry Dock",
                "纳塔地脉刷怪笼：荒废砌造坞",
                List.of("minecraft:eroded_badlands")
        ),
        NATLAN_RAINBOW_SANCTUM(
                "natlan_rainbow_sanctum_leyline_spawner",
                "Natlan Leyline Spawner: Sanctum of Rainbow Spirits",
                "纳塔地脉刷怪笼：虹灵的净土",
                List.of("minecraft:badlands")
        ),
        NOD_KRAI_MOONCHILDS_TREASURES(
                "nod_krai_moonchilds_treasures_leyline_spawner",
                "Nod-Krai Leyline Spawner: Moonchild's Treasures",
                "挪德卡莱地脉刷怪笼：月童的库藏",
                List.of("minecraft:end_midlands")
        ),
        NOD_KRAI_FROSTLADEN_MACHINERY(
                "nod_krai_frostladen_machinery_leyline_spawner",
                "Nod-Krai Leyline Spawner: Frostladen Machinery",
                "挪德卡莱地脉刷怪笼：霜凝的机枢",
                List.of("minecraft:end_highlands")
        ),
        SNEZHNAYA_INVERTED_GLACIER(
                "snezhnaya_inverted_glacier_leyline_spawner",
                "Snezhnaya Leyline Spawner: Inverted Glacier",
                "至冬地脉刷怪笼：逆悬的冰河",
                List.of("minecraft:frozen_river")
        );

        private final String id;
        private final String englishName;
        private final String chineseName;
        private final List<String> defaultBiomes;
        private ModConfigSpec.BooleanValue generate;
        private ModConfigSpec.ConfigValue<List<? extends String>> biomes;
        private ModConfigSpec.ConfigValue<String> monsterPool;

        LeylineSpawner(String id, String englishName, String chineseName, List<String> defaultBiomes) {
            this.id = id;
            this.englishName = englishName;
            this.chineseName = chineseName;
            this.defaultBiomes = defaultBiomes;
        }

        private void define(ModConfigSpec.Builder builder) {
            builder.comment(englishName, chineseName).push(id);
            generate = builder.define("generate", true);
            biomes = builder.defineListAllowEmpty("biomes", defaultBiomes, TeyvatArtifactsConfig::isValidBiomeSelector);
            builder.pop();
        }

        private void defineMonsterPool(ModConfigSpec.Builder builder) {
            monsterPool = builder.define(id, "", TeyvatArtifactsConfig::isValidMonsterPoolName);
        }

        public String id() {
            return id;
        }

        public boolean shouldGenerate() {
            return getBoolean(ENABLE_ALL_LEYLINE_SPAWNER_GENERATION, true) && getBoolean(generate, true);
        }

        public List<String> configuredBiomes() {
            if (!shouldGenerate()) {
                return List.of();
            }
            try {
                return biomes.get().stream().map(String::valueOf).toList();
            } catch (IllegalStateException ignored) {
                return defaultBiomes;
            }
        }

        private String configuredMonsterPool() {
            return getString(monsterPool, "");
        }

        public static LeylineSpawner byId(String id) {
            for (LeylineSpawner spawner : values()) {
                if (spawner.id.equals(id)) {
                    return spawner;
                }
            }
            return null;
        }
    }

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment(
                "Teyvat Artifacts common configuration.",
                "提瓦特饰品通用配置。"
        );
        GIVE_INITIATE_ARTIFACTS_ON_FIRST_SPAWN = builder
                .comment(
                        "Give the Initiate two-piece artifact set to players the first time they join the world.",
                        "玩家首次进入世界时，是否自动给予初学者二件套。"
                )
                .define("giveInitiateArtifactsOnFirstSpawn", true);

        builder.comment(
                "Leyline Spawner natural generation settings.",
                "Changes affect newly generated chunks. Restart the game or server after editing this section.",
                "地脉刷怪笼自然生成配置。",
                "修改本分组后请重启游戏或服务器；改动只影响新生成的区块。"
        ).push("leylineSpawnerGeneration");
        ENABLE_ALL_LEYLINE_SPAWNER_GENERATION = builder.comment(
                "Master switch with the highest priority. When false, no Leyline Spawner can generate regardless of individual settings.",
                "最高优先级总开关。关闭后，无论单独选项如何设置，所有地脉刷怪笼都不会自然生成。"
        ).define("enableAllLeylineSpawnerGeneration", true);
        builder.comment(
                "Individual Leyline Spawner settings.",
                "各地脉刷怪笼的单独生成设置。",
                "Whether this Leyline Spawner can generate naturally.",
                "该地脉刷怪笼是否可以自然生成。",
                "Complete biome selector list for this Leyline Spawner. Add or remove entries to change its generation biomes.",
                "Entries may be biome IDs such as minecraft:meadow or biome tags beginning with #.",
                "该地脉刷怪笼的完整生成群系列表；可通过添加或删除条目修改生成群系。",
                "条目可以是 minecraft:meadow 形式的群系 ID，也可以是以 # 开头的群系标签。"
        ).push("spawners");
        for (LeylineSpawner spawner : LeylineSpawner.values()) {
            spawner.define(builder);
        }
        builder.pop(2);

        builder.comment(
                "Leyline Spawner trial monster pool settings. Restart the game or server after editing monster pool files or these selections.",
                "地脉刷怪笼试炼怪物池配置。修改怪物池文件或此处选择后，请重启游戏或服务器。"
        ).push("leylineTrialMonsters");
        DEFAULT_LEYLINE_MONSTER_POOL = builder.comment(
                "Default pool file name without .json. All Leyline Spawners use this pool unless overridden below.",
                "默认怪物池文件名，不含 .json。除非在下方单独覆盖，否则所有地脉刷怪笼都使用该怪物池。"
        ).define("defaultPool", "default", TeyvatArtifactsConfig::isValidMonsterPoolName);
        GLOW_LEYLINE_TRIAL_MOBS = builder.comment(
                "Give every Leyline trial mob 90 seconds of Glowing when it spawns, including passengers.",
                "试炼怪物生成时是否获得 90 秒发光效果，包括骑乘者。"
        ).define("glowTrialMobs", true);
        builder.comment(
                "Per-spawner pool overrides. Leave an entry empty to inherit defaultPool.",
                "各地脉刷怪笼的怪物池覆盖选项。留空表示继承 defaultPool。"
        ).push("spawners");
        for (LeylineSpawner spawner : LeylineSpawner.values()) {
            spawner.defineMonsterPool(builder);
        }
        builder.pop(2);
        builder.comment(
                "Leyline trial access and reward costs. Changes take effect without regenerating structures.",
                "地脉试炼准入与领奖费用。修改后无需重新生成结构。"
        ).push("leylineTrialAccess");
        REQUIRE_LEYLINE_ADVANCEMENTS = builder.comment(
                "Require all advancement IDs listed below before a player may start a trial.",
                "开启后，玩家必须完成下列所有进度才能启动试炼。"
        ).define("requireAdvancements", true);
        LEYLINE_REQUIRED_ADVANCEMENTS = builder.comment(
                "Complete list of required advancement IDs. Empty list means no advancement requirement.",
                "所需进度 ID 的完整列表；清空列表表示不要求进度。"
        ).defineListAllowEmpty("requiredAdvancements", List.of("minecraft:end/kill_dragon", "minecraft:nether/summon_wither"),
                value -> value instanceof String id && ResourceLocation.tryParse(id) != null);
        REQUIRE_LEYLINE_ACTIVATION_ITEM = builder.comment(
                "Require the configured item in the hand used to start a trial.",
                "启动试炼时是否必须在交互用的手中持有下方物品。"
        ).define("requireActivationItem", false);
        LEYLINE_ACTIVATION_ITEM = builder.comment(
                "Required item ID or item tag prefixed with #, used only when requireActivationItem is true.",
                "启动所需物品 ID，或以 # 开头的物品标签；仅开启 requireActivationItem 后生效。"
        ).define("activationItem", "minecraft:nether_star", TeyvatArtifactsConfig::isValidBiomeSelector);
        CONSUME_LEYLINE_ACTIVATION_ITEM = builder.comment(
                "Consume one activation item when the trial begins (creative players do not consume items).",
                "开始试炼时是否消耗一个启动物品（创造模式玩家不消耗）。"
        ).define("consumeActivationItem", false);
        LEYLINE_REWARD_XP_COST = builder.comment(
                "Experience points per normal reward claim. Default 160 is the XP to reach level 10, not 10 player levels. Crystal Core consumes twice this amount (320 by default).",
                "每次普通领奖消耗的经验值点数。默认 160 点是从 0 级升到 10 级所需经验，并非直接扣 10 级；晶核消耗其两倍（默认 320 点）。"
        ).defineInRange("rewardExperiencePoints", 160, 0, 1000000);
        builder.pop();
        builder.comment("Leyline Spawner artifact reward rarity settings.", "地脉刷怪笼圣遗物奖励品质配置。")
                .push("leylineRewards");
        LEYLINE_FIVE_STAR_CHANCE_PERCENT = builder.comment(
                "Five-star chance per artifact reward, in percent (0-100). Four-star chance is 100 minus this value.",
                "Applies to all Leyline Spawners, including Condensed Resin rewards; does not affect chest loot or existing artifacts.",
                "每件圣遗物奖励的五星概率，百分比范围 0 至 100；四星概率为 100 减去此值。",
                "适用于所有地脉刷怪笼及浓缩树脂领奖；不影响战利品箱或已经生成的圣遗物。"
        ).defineInRange("fiveStarChancePercent", 50.0D, 0.0D, 100.0D);
        builder.pop();
        builder.comment(
                "Artifact substats. Existing values stay unchanged; edits affect new rolls, enhancements and refreshes.",
                "Each pool entry is attribute ID, base value, operation (1=addition, 2=multiply base, 3=multiply total). Repeated rolls are allowed.",
                "圣遗物副词条。已抽取的数值不会随配置变化；修改影响新的抽取、强化增量和刷新。",
                "属性池每项格式：属性ID,基础值,运算方式（1=加算，2=基础值乘算，3=最终乘算）。允许重复抽中。"
        ).push("artifactSubstats");
        SUBSTAT_COUNTS = builder.comment("Base substat roll counts for one through five stars. Duplicate attributes with the same operation merge into one line and add one stack each.", "一至五星的基础副词条抽取次数；同属性、同运算方式的重复抽取会合为一条，每次重复计作一次叠加。")
                .defineList("countsByStar", List.<Number>of(1, 1, 2, 2, 3), value -> value instanceof Number number && number.doubleValue() == number.intValue() && number.intValue() >= 0 && number.intValue() <= 16);
        SUBSTAT_COUNT_BONUSES = builder.comment("Maximum random additional substat rolls by star, inclusive; 1 means +0 or +1.", "各星级随机增加的副词条抽取次数上限（含）；1 表示随机 +0 或 +1。")
                .defineList("extraCountsByStar", List.<Number>of(1, 1, 1, 1, 1), value -> value instanceof Number number && number.doubleValue() == number.intValue() && number.intValue() >= 0 && number.intValue() <= 16);
        SUBSTAT_STAR_MULTIPLIERS = builder.comment("Base roll multiplier for each star rank.", "各星级抽取基准倍率。")
                .defineList("multipliersByStar", List.<Number>of(0.7D, 0.75D, 0.8D, 0.9D, 1.0D), value -> value instanceof Number number && Double.isFinite(number.doubleValue()) && number.doubleValue() >= 0);
        SUBSTAT_STAR_VARIANCES = builder.comment("Independent plus/minus variance for each star rank; 1.0 +/- 0.1 means 90%-110% of base.", "各星级独立的正负波动；1.0 ± 0.1 即基础值的 90% 至 110%。")
                .defineList("variancesByStar", List.<Number>of(0.1D, 0.1D, 0.1D, 0.1D, 0.1D), value -> value instanceof Number number && Double.isFinite(number.doubleValue()) && number.doubleValue() >= 0);
        SUBSTAT_POOL = builder.comment("Editable pool; remove, change or add entries. 0.04 means 4% for percentage attributes. Unavailable attributes are skipped.", "可增删修改属性池；百分比属性的 0.04 表示 4%。不存在的属性会跳过。")
                .defineListAllowEmpty("pool", List.of(
                        "teyvat_artifacts:damage_bonus,0.04,2",
                        "teyvat_artifacts:damage_reduction,0.04,2",
                        "minecraft:generic.max_health,2,1",
                        "teyvat_artifacts:armor_ignore,0.06,2",
                        "minecraft:generic.attack_speed,0.05,2",
                        "minecraft:player.entity_interaction_range,0.3,1",
                        "minecraft:generic.movement_speed,0.04,2",
                        "apothic_attributes:crit_chance,0.04,1",
                        "apothic_attributes:crit_damage,0.08,1",
                        "minecraft:generic.luck,1.5,1",
                        "irons_spellbooks:spell_power,0.05,2",
                        "irons_spellbooks:max_mana,20,1",
                        "irons_spellbooks:cooldown_reduction,0.04,2",
                        "irons_spellbooks:cast_time_reduction,0.04,2",
                        "irons_spellbooks:mana_regen,0.06,2"
                ), value -> value instanceof String entry && ArtifactSubstat.isValidPoolEntry(entry));
        builder.pop();
        builder.comment(
                "Artifact main stats. Value = initial value for the star rank + enhancement increment * enhancements used.",
                "No random value variance. Definition edits also affect existing artifacts; their IDs and enhancement counts are retained.",
                "圣遗物主词条。数值 = 对应星级初始值 + 单次强化增量 * 已强化次数；没有随机波动。",
                "修改定义也会改变已有圣遗物的主词条数值；保留已保存的词条 ID 和强化次数。"
        ).push("artifactMainStats");
        MAIN_STAT_DEFINITIONS = builder.comment(
                "Format: stat ID, attribute ID, operation, 1-star initial, 2-star initial, 3-star initial, 4-star initial, 5-star initial, enhancement increment.",
                "Operations: 1=addition, 2=multiply base, 3=multiply total. Percentage amounts use decimals: 0.01 means 1%.",
                "Add a unique definition here, then add its stat ID to the desired slot pools below. Only registered attributes can take effect.",
                "格式：词条ID,属性ID,运算方式,一星初始值,二星初始值,三星初始值,四星初始值,五星初始值,单次强化增量。",
                "运算方式：1=加算，2=基础值乘算，3=最终乘算。百分比用小数填写：0.01 表示 1%。",
                "在此添加唯一的词条定义，再将其词条 ID 加入下方部位池。只能使用游戏或已安装模组注册的属性。"
        ).defineListAllowEmpty("definitions", ArtifactStat.defaultDefinitions(),
                value -> value instanceof String raw && ArtifactStat.isValidDefinition(raw));
        builder.comment(
                "Allowed main stat IDs for each slot. Add or remove IDs to customize the pools; repeating an ID increases its selection weight.",
                "An empty pool disables the main stat for that slot. Unavailable attributes are skipped without adding required mods.",
                "各部位允许抽取的主词条 ID；可增删条目，重复填写同一 ID 可提高抽中权重。",
                "空列表关闭该部位的主词条；不存在的属性会跳过，不会增加必需前置。"
        ).push("pools");
        for (ArtifactSlot slot : ArtifactSlot.values()) {
            MAIN_STAT_POOLS.put(slot, builder.defineListAllowEmpty(slot.id(), ArtifactStat.defaultPoolForSlot(slot),
                    value -> value instanceof String id && ArtifactStat.isValidId(id)));
        }
        builder.pop(2);
        builder.comment("Alchemy Synthesis Table settings.", "炼金合成台设置。").push("alchemyTable");
        ALCHEMY_AUTOMATIC_INTERVAL_TICKS = builder.comment(
                "Interval between automatic batches while powered, in game ticks; 20 ticks = 1 second.",
                "接收红石信号时的自动批量炼金间隔，单位为游戏刻；20 刻等于 1 秒。"
        ).defineInRange("automaticIntervalTicks", 20, 1, 1200);
        builder.pop();
        SPEC = builder.build();
    }

    private TeyvatArtifactsConfig() {
    }

    public static boolean giveInitiateArtifactsOnFirstSpawn() {
        return GIVE_INITIATE_ARTIFACTS_ON_FIRST_SPAWN.get();
    }

    public static String leylineMonsterPoolForSpawner(String spawnerId) {
        LeylineSpawner spawner = LeylineSpawner.byId(spawnerId);
        String override = spawner == null ? "" : spawner.configuredMonsterPool().trim();
        return override.isEmpty() ? getString(DEFAULT_LEYLINE_MONSTER_POOL, "default") : override;
    }

    public static boolean glowLeylineTrialMobs() {
        return getBoolean(GLOW_LEYLINE_TRIAL_MOBS, true);
    }

    public static double leylineFiveStarChance() {
        try {
            return LEYLINE_FIVE_STAR_CHANCE_PERCENT.get() / 100.0D;
        } catch (IllegalStateException ignored) {
            return 0.5D;
        }
    }

    private static boolean isValidBiomeSelector(Object value) {
        if (!(value instanceof String selector) || selector.isBlank()) {
            return false;
        }
        String resourceId = selector.startsWith("#") ? selector.substring(1) : selector;
        return ResourceLocation.tryParse(resourceId) != null;
    }

    private static boolean isValidMonsterPoolName(Object value) {
        if (!(value instanceof String name)) {
            return false;
        }
        return name.isEmpty() || name.matches("[a-z0-9_.-]+");
    }

    private static boolean getBoolean(ModConfigSpec.BooleanValue value, boolean fallback) {
        if (value == null) {
            return fallback;
        }
        try {
            return value.get();
        } catch (IllegalStateException ignored) {
            return fallback;
        }
    }

    private static String getString(ModConfigSpec.ConfigValue<String> value, String fallback) {
        if (value == null) {
            return fallback;
        }
        try {
            String configured = value.get();
            return configured == null || configured.isBlank() ? fallback : configured;
        } catch (IllegalStateException ignored) {
            return fallback;
        }
    }
}
