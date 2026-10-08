package com.guoche.teyvat_artifacts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

@Mod(TeyvatArtifacts.MODID)
public class TeyvatArtifacts {
    public static final String MODID = "teyvat_artifacts";

    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(
            Registries.DATA_COMPONENT_TYPE,
            MODID
    );
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> STARS =
            DATA_COMPONENT_TYPES.registerComponentType(
                    "stars",
                    builder -> builder
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.VAR_INT)
            );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> ARTIFACT_STAT =
            DATA_COMPONENT_TYPES.registerComponentType(
                    "artifact_stat",
                    builder -> builder
                            .persistent(Codec.STRING)
                            .networkSynchronized(ByteBufCodecs.STRING_UTF8)
            );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ArtifactSubstat>>> ARTIFACT_SUBSTATS =
            DATA_COMPONENT_TYPES.registerComponentType(
                    "artifact_substats",
                    builder -> builder
                            .persistent(ArtifactSubstat.CODEC.listOf())
                            .networkSynchronized(ByteBufCodecs.fromCodec(ArtifactSubstat.CODEC.listOf()))
            );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ARTIFACT_ENHANCEMENTS =
            DATA_COMPONENT_TYPES.registerComponentType(
                    "artifact_enhancements",
                    builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
            );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> LITTLE_WITCH_DICTIONARY_ADVANCEMENT_WEIGHT =
            DATA_COMPONENT_TYPES.registerComponentType(
                    "little_witch_dictionary_advancement_weight",
                    builder -> builder
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.VAR_INT)
            );

    public static final DeferredHolder<Attribute, Attribute> DAMAGE_BONUS = ATTRIBUTES.register("damage_bonus",
            () -> new RangedAttribute("attribute.name.teyvat_artifacts.damage_bonus", 1.0D, 0.0D, 1024.0D).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> DISPLAY_DAMAGE_BONUS = ATTRIBUTES.register("display_damage_bonus",
            () -> new RangedAttribute("attribute.name.teyvat_artifacts.display_damage_bonus", 0.0D, 0.0D, 1024.0D).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> DAMAGE_REDUCTION = ATTRIBUTES.register("damage_reduction",
            () -> new RangedAttribute("attribute.name.teyvat_artifacts.damage_reduction", 1.0D, 0.0D, 1024.0D).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> ARMOR_IGNORE = ATTRIBUTES.register("armor_ignore",
            () -> new RangedAttribute("attribute.name.teyvat_artifacts.armor_ignore", 1.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final DeferredItem<WitchGiftItem> WITCHS_REVELATION_CASE =
            ITEMS.register("witchs_revelation_case", () -> new WitchGiftItem("witchs_revelation_case", new Item.Properties()));
    public static final DeferredItem<WitchGiftItem> THE_LITTLE_WITCHS_DICTIONARY =
            ITEMS.register("the_little_witchs_dictionary", () -> new WitchGiftItem("the_little_witchs_dictionary", new Item.Properties()));
    public static final DeferredHolder<EntityType<?>, EntityType<Crystalfly>> ANEMO_CRYSTALFLY = registerCrystalfly("anemo_crystalfly");
    public static final DeferredHolder<EntityType<?>, EntityType<Crystalfly>> ICE_CRYSTALFLY = registerCrystalfly("ice_crystalfly");
    public static final DeferredHolder<EntityType<?>, EntityType<Crystalfly>> GEO_CRYSTALFLY = registerCrystalfly("geo_crystalfly");
    public static final DeferredHolder<EntityType<?>, EntityType<Crystalfly>> ELECTRO_CRYSTALFLY = registerCrystalfly("electro_crystalfly");
    public static final DeferredHolder<EntityType<?>, EntityType<Crystalfly>> DENDRO_CRYSTALFLY = registerCrystalfly("dendro_crystalfly");
    public static final DeferredHolder<EntityType<?>, EntityType<Crystalfly>> HYDRO_CRYSTALFLY = registerCrystalfly("hydro_crystalfly");
    public static final DeferredHolder<EntityType<?>, EntityType<Crystalfly>> PYRO_CRYSTALFLY = registerCrystalfly("pyro_crystalfly");
    public static final DeferredItem<DeferredSpawnEggItem> ANEMO_CRYSTALFLY_SPAWN_EGG =
            ITEMS.register("anemo_crystalfly_spawn_egg", () -> new DeferredSpawnEggItem(ANEMO_CRYSTALFLY, 0x9DEBDA, 0xDFFFF4, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> ICE_CRYSTALFLY_SPAWN_EGG =
            ITEMS.register("ice_crystalfly_spawn_egg", () -> new DeferredSpawnEggItem(ICE_CRYSTALFLY, 0xA7D8FF, 0xE7F7FF, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> GEO_CRYSTALFLY_SPAWN_EGG =
            ITEMS.register("geo_crystalfly_spawn_egg", () -> new DeferredSpawnEggItem(GEO_CRYSTALFLY, 0xD3A64E, 0xFFF0A8, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> ELECTRO_CRYSTALFLY_SPAWN_EGG =
            ITEMS.register("electro_crystalfly_spawn_egg", () -> new DeferredSpawnEggItem(ELECTRO_CRYSTALFLY, 0xB88BFF, 0xF3D6FF, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> DENDRO_CRYSTALFLY_SPAWN_EGG =
            ITEMS.register("dendro_crystalfly_spawn_egg", () -> new DeferredSpawnEggItem(DENDRO_CRYSTALFLY, 0x83C94F, 0xD9F5A8, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> HYDRO_CRYSTALFLY_SPAWN_EGG =
            ITEMS.register("hydro_crystalfly_spawn_egg", () -> new DeferredSpawnEggItem(HYDRO_CRYSTALFLY, 0x5EA8FF, 0xC7F0FF, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> PYRO_CRYSTALFLY_SPAWN_EGG =
            ITEMS.register("pyro_crystalfly_spawn_egg", () -> new DeferredSpawnEggItem(PYRO_CRYSTALFLY, 0xF06C39, 0xFFD27A, new Item.Properties()));
    public static final DeferredItem<CrystalCoreItem> CRYSTAL_CORE =
            ITEMS.register("crystal_core", () -> new CrystalCoreItem(new Item.Properties()));
    public static final DeferredItem<Item> CONDENSED_RESIN =
            ITEMS.register("condensed_resin", () -> new DescriptiveItem(new Item.Properties(), 4));
    public static final DeferredItem<SanctifyingElixirItem> SANCTIFYING_ELIXIR =
            ITEMS.register("sanctifying_elixir", () -> new SanctifyingElixirItem(new Item.Properties()));
    public static final DeferredItem<SanctifyingEssenceItem> SANCTIFYING_ESSENCE =
            ITEMS.register("sanctifying_essence", () -> new SanctifyingEssenceItem(new Item.Properties()));
    public static final DeferredItem<SanctifyingUnctionItem> SANCTIFYING_UNCTION =
            ITEMS.register("sanctifying_unction", () -> new SanctifyingUnctionItem(new Item.Properties()));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ArtifactToSanctifyingMaterialRecipe>> ARTIFACT_TO_SANCTIFYING_MATERIAL_RECIPE =
            RECIPE_SERIALIZERS.register("crafting_special_artifact_to_sanctifying_material", () -> new SimpleCraftingRecipeSerializer<>(ArtifactToSanctifyingMaterialRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ArtifactStarUpgradeRecipe>> ARTIFACT_STAR_UPGRADE_RECIPE =
            RECIPE_SERIALIZERS.register("crafting_special_artifact_star_upgrade", () -> new SimpleCraftingRecipeSerializer<>(ArtifactStarUpgradeRecipe::new));
    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ArtifactChestLootModifier>> ARTIFACT_CHEST_LOOT_MODIFIER =
            LOOT_MODIFIER_SERIALIZERS.register("artifact_chest_rewards", () -> ArtifactChestLootModifier.CODEC);

    public static final DeferredItem<ArtifactItem> INITIATE_FLOWER = registerArtifact("initiate_flower", ArtifactSet.INITIATE, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> INITIATE_FEATHER = registerArtifact("initiate_feather", ArtifactSet.INITIATE, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> LUCKY_FLOWER = registerArtifact("lucky_flower", ArtifactSet.LUCKY, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> LUCKY_FEATHER = registerArtifact("lucky_feather", ArtifactSet.LUCKY, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> LUCKY_SANDS = registerArtifact("lucky_sands", ArtifactSet.LUCKY, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> LUCKY_GOBLET = registerArtifact("lucky_goblet", ArtifactSet.LUCKY, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> LUCKY_CIRCLET = registerArtifact("lucky_circlet", ArtifactSet.LUCKY, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> ADVENTURER_FLOWER = registerArtifact("adventurer_flower", ArtifactSet.ADVENTURER, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> ADVENTURER_FEATHER = registerArtifact("adventurer_feather", ArtifactSet.ADVENTURER, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> ADVENTURER_SANDS = registerArtifact("adventurer_sands", ArtifactSet.ADVENTURER, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> ADVENTURER_GOBLET = registerArtifact("adventurer_goblet", ArtifactSet.ADVENTURER, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> ADVENTURER_CIRCLET = registerArtifact("adventurer_circlet", ArtifactSet.ADVENTURER, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> TRAVELING_DOCTOR_FLOWER = registerArtifact("traveling_doctor_flower", ArtifactSet.TRAVELING_DOCTOR, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> TRAVELING_DOCTOR_FEATHER = registerArtifact("traveling_doctor_feather", ArtifactSet.TRAVELING_DOCTOR, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> TRAVELING_DOCTOR_SANDS = registerArtifact("traveling_doctor_sands", ArtifactSet.TRAVELING_DOCTOR, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> TRAVELING_DOCTOR_GOBLET = registerArtifact("traveling_doctor_goblet", ArtifactSet.TRAVELING_DOCTOR, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> TRAVELING_DOCTOR_CIRCLET = registerArtifact("traveling_doctor_circlet", ArtifactSet.TRAVELING_DOCTOR, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> RESOLUTION_OF_SOJOURNER_FLOWER = registerArtifact("resolution_of_sojourner_flower", ArtifactSet.RESOLUTION_OF_SOJOURNER, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> RESOLUTION_OF_SOJOURNER_FEATHER = registerArtifact("resolution_of_sojourner_feather", ArtifactSet.RESOLUTION_OF_SOJOURNER, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> RESOLUTION_OF_SOJOURNER_SANDS = registerArtifact("resolution_of_sojourner_sands", ArtifactSet.RESOLUTION_OF_SOJOURNER, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> RESOLUTION_OF_SOJOURNER_GOBLET = registerArtifact("resolution_of_sojourner_goblet", ArtifactSet.RESOLUTION_OF_SOJOURNER, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> RESOLUTION_OF_SOJOURNER_CIRCLET = registerArtifact("resolution_of_sojourner_circlet", ArtifactSet.RESOLUTION_OF_SOJOURNER, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> TINY_MIRACLE_FLOWER = registerArtifact("tiny_miracle_flower", ArtifactSet.TINY_MIRACLE, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> TINY_MIRACLE_FEATHER = registerArtifact("tiny_miracle_feather", ArtifactSet.TINY_MIRACLE, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> TINY_MIRACLE_SANDS = registerArtifact("tiny_miracle_sands", ArtifactSet.TINY_MIRACLE, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> TINY_MIRACLE_GOBLET = registerArtifact("tiny_miracle_goblet", ArtifactSet.TINY_MIRACLE, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> TINY_MIRACLE_CIRCLET = registerArtifact("tiny_miracle_circlet", ArtifactSet.TINY_MIRACLE, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> BERSERKER_FLOWER = registerArtifact("berserker_flower", ArtifactSet.BERSERKER, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> BERSERKER_FEATHER = registerArtifact("berserker_feather", ArtifactSet.BERSERKER, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> BERSERKER_SANDS = registerArtifact("berserker_sands", ArtifactSet.BERSERKER, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> BERSERKER_GOBLET = registerArtifact("berserker_goblet", ArtifactSet.BERSERKER, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> BERSERKER_CIRCLET = registerArtifact("berserker_circlet", ArtifactSet.BERSERKER, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> INSTRUCTOR_FLOWER = registerArtifact("instructor_flower", ArtifactSet.INSTRUCTOR, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> INSTRUCTOR_FEATHER = registerArtifact("instructor_feather", ArtifactSet.INSTRUCTOR, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> INSTRUCTOR_SANDS = registerArtifact("instructor_sands", ArtifactSet.INSTRUCTOR, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> INSTRUCTOR_GOBLET = registerArtifact("instructor_goblet", ArtifactSet.INSTRUCTOR, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> INSTRUCTOR_CIRCLET = registerArtifact("instructor_circlet", ArtifactSet.INSTRUCTOR, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> THE_EXILE_FLOWER = registerArtifact("the_exile_flower", ArtifactSet.THE_EXILE, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> THE_EXILE_FEATHER = registerArtifact("the_exile_feather", ArtifactSet.THE_EXILE, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> THE_EXILE_SANDS = registerArtifact("the_exile_sands", ArtifactSet.THE_EXILE, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> THE_EXILE_GOBLET = registerArtifact("the_exile_goblet", ArtifactSet.THE_EXILE, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> THE_EXILE_CIRCLET = registerArtifact("the_exile_circlet", ArtifactSet.THE_EXILE, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> DEFENDERS_WILL_FLOWER = registerArtifact("defenders_will_flower", ArtifactSet.DEFENDERS_WILL, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> DEFENDERS_WILL_FEATHER = registerArtifact("defenders_will_feather", ArtifactSet.DEFENDERS_WILL, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> DEFENDERS_WILL_SANDS = registerArtifact("defenders_will_sands", ArtifactSet.DEFENDERS_WILL, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> DEFENDERS_WILL_GOBLET = registerArtifact("defenders_will_goblet", ArtifactSet.DEFENDERS_WILL, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> DEFENDERS_WILL_CIRCLET = registerArtifact("defenders_will_circlet", ArtifactSet.DEFENDERS_WILL, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> BRAVE_HEART_FLOWER = registerArtifact("brave_heart_flower", ArtifactSet.BRAVE_HEART, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> BRAVE_HEART_FEATHER = registerArtifact("brave_heart_feather", ArtifactSet.BRAVE_HEART, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> BRAVE_HEART_SANDS = registerArtifact("brave_heart_sands", ArtifactSet.BRAVE_HEART, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> BRAVE_HEART_GOBLET = registerArtifact("brave_heart_goblet", ArtifactSet.BRAVE_HEART, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> BRAVE_HEART_CIRCLET = registerArtifact("brave_heart_circlet", ArtifactSet.BRAVE_HEART, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> MARTIAL_ARTIST_FLOWER = registerArtifact("martial_artist_flower", ArtifactSet.MARTIAL_ARTIST, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> MARTIAL_ARTIST_FEATHER = registerArtifact("martial_artist_feather", ArtifactSet.MARTIAL_ARTIST, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> MARTIAL_ARTIST_SANDS = registerArtifact("martial_artist_sands", ArtifactSet.MARTIAL_ARTIST, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> MARTIAL_ARTIST_GOBLET = registerArtifact("martial_artist_goblet", ArtifactSet.MARTIAL_ARTIST, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> MARTIAL_ARTIST_CIRCLET = registerArtifact("martial_artist_circlet", ArtifactSet.MARTIAL_ARTIST, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> GAMBLER_FLOWER = registerArtifact("gambler_flower", ArtifactSet.GAMBLER, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> GAMBLER_FEATHER = registerArtifact("gambler_feather", ArtifactSet.GAMBLER, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> GAMBLER_SANDS = registerArtifact("gambler_sands", ArtifactSet.GAMBLER, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> GAMBLER_GOBLET = registerArtifact("gambler_goblet", ArtifactSet.GAMBLER, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> GAMBLER_CIRCLET = registerArtifact("gambler_circlet", ArtifactSet.GAMBLER, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> SCHOLAR_FLOWER = registerArtifact("scholar_flower", ArtifactSet.SCHOLAR, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> SCHOLAR_FEATHER = registerArtifact("scholar_feather", ArtifactSet.SCHOLAR, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> SCHOLAR_SANDS = registerArtifact("scholar_sands", ArtifactSet.SCHOLAR, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> SCHOLAR_GOBLET = registerArtifact("scholar_goblet", ArtifactSet.SCHOLAR, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> SCHOLAR_CIRCLET = registerArtifact("scholar_circlet", ArtifactSet.SCHOLAR, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> GLADIATORS_FINALE_FLOWER = registerArtifact("gladiators_finale_flower", ArtifactSet.GLADIATORS_FINALE, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> GLADIATORS_FINALE_FEATHER = registerArtifact("gladiators_finale_feather", ArtifactSet.GLADIATORS_FINALE, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> GLADIATORS_FINALE_SANDS = registerArtifact("gladiators_finale_sands", ArtifactSet.GLADIATORS_FINALE, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> GLADIATORS_FINALE_GOBLET = registerArtifact("gladiators_finale_goblet", ArtifactSet.GLADIATORS_FINALE, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> GLADIATORS_FINALE_CIRCLET = registerArtifact("gladiators_finale_circlet", ArtifactSet.GLADIATORS_FINALE, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> WANDERERS_TROUPE_FLOWER = registerArtifact("wanderers_troupe_flower", ArtifactSet.WANDERERS_TROUPE, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> WANDERERS_TROUPE_FEATHER = registerArtifact("wanderers_troupe_feather", ArtifactSet.WANDERERS_TROUPE, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> WANDERERS_TROUPE_SANDS = registerArtifact("wanderers_troupe_sands", ArtifactSet.WANDERERS_TROUPE, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> WANDERERS_TROUPE_GOBLET = registerArtifact("wanderers_troupe_goblet", ArtifactSet.WANDERERS_TROUPE, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> WANDERERS_TROUPE_CIRCLET = registerArtifact("wanderers_troupe_circlet", ArtifactSet.WANDERERS_TROUPE, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> NOBLESSE_OBLIGE_FLOWER = registerArtifact("noblesse_oblige_flower", ArtifactSet.NOBLESSE_OBLIGE, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> NOBLESSE_OBLIGE_FEATHER = registerArtifact("noblesse_oblige_feather", ArtifactSet.NOBLESSE_OBLIGE, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> NOBLESSE_OBLIGE_SANDS = registerArtifact("noblesse_oblige_sands", ArtifactSet.NOBLESSE_OBLIGE, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> NOBLESSE_OBLIGE_GOBLET = registerArtifact("noblesse_oblige_goblet", ArtifactSet.NOBLESSE_OBLIGE, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> NOBLESSE_OBLIGE_CIRCLET = registerArtifact("noblesse_oblige_circlet", ArtifactSet.NOBLESSE_OBLIGE, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> BLOODSTAINED_CHIVALRY_FLOWER = registerArtifact("bloodstained_chivalry_flower", ArtifactSet.BLOODSTAINED_CHIVALRY, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> BLOODSTAINED_CHIVALRY_FEATHER = registerArtifact("bloodstained_chivalry_feather", ArtifactSet.BLOODSTAINED_CHIVALRY, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> BLOODSTAINED_CHIVALRY_SANDS = registerArtifact("bloodstained_chivalry_sands", ArtifactSet.BLOODSTAINED_CHIVALRY, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> BLOODSTAINED_CHIVALRY_GOBLET = registerArtifact("bloodstained_chivalry_goblet", ArtifactSet.BLOODSTAINED_CHIVALRY, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> BLOODSTAINED_CHIVALRY_CIRCLET = registerArtifact("bloodstained_chivalry_circlet", ArtifactSet.BLOODSTAINED_CHIVALRY, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> MAIDEN_BELOVED_FLOWER = registerArtifact("maiden_beloved_flower", ArtifactSet.MAIDEN_BELOVED, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> MAIDEN_BELOVED_FEATHER = registerArtifact("maiden_beloved_feather", ArtifactSet.MAIDEN_BELOVED, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> MAIDEN_BELOVED_SANDS = registerArtifact("maiden_beloved_sands", ArtifactSet.MAIDEN_BELOVED, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> MAIDEN_BELOVED_GOBLET = registerArtifact("maiden_beloved_goblet", ArtifactSet.MAIDEN_BELOVED, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> MAIDEN_BELOVED_CIRCLET = registerArtifact("maiden_beloved_circlet", ArtifactSet.MAIDEN_BELOVED, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> VIRIDESCENT_VENERER_FLOWER = registerArtifact("viridescent_venerer_flower", ArtifactSet.VIRIDESCENT_VENERER, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> VIRIDESCENT_VENERER_FEATHER = registerArtifact("viridescent_venerer_feather", ArtifactSet.VIRIDESCENT_VENERER, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> VIRIDESCENT_VENERER_SANDS = registerArtifact("viridescent_venerer_sands", ArtifactSet.VIRIDESCENT_VENERER, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> VIRIDESCENT_VENERER_GOBLET = registerArtifact("viridescent_venerer_goblet", ArtifactSet.VIRIDESCENT_VENERER, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> VIRIDESCENT_VENERER_CIRCLET = registerArtifact("viridescent_venerer_circlet", ArtifactSet.VIRIDESCENT_VENERER, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> ARCHAIC_PETRA_FLOWER = registerArtifact("archaic_petra_flower", ArtifactSet.ARCHAIC_PETRA, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> ARCHAIC_PETRA_FEATHER = registerArtifact("archaic_petra_feather", ArtifactSet.ARCHAIC_PETRA, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> ARCHAIC_PETRA_SANDS = registerArtifact("archaic_petra_sands", ArtifactSet.ARCHAIC_PETRA, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> ARCHAIC_PETRA_GOBLET = registerArtifact("archaic_petra_goblet", ArtifactSet.ARCHAIC_PETRA, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> ARCHAIC_PETRA_CIRCLET = registerArtifact("archaic_petra_circlet", ArtifactSet.ARCHAIC_PETRA, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> RETRACING_BOLIDE_FLOWER = registerArtifact("retracing_bolide_flower", ArtifactSet.RETRACING_BOLIDE, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> RETRACING_BOLIDE_FEATHER = registerArtifact("retracing_bolide_feather", ArtifactSet.RETRACING_BOLIDE, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> RETRACING_BOLIDE_SANDS = registerArtifact("retracing_bolide_sands", ArtifactSet.RETRACING_BOLIDE, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> RETRACING_BOLIDE_GOBLET = registerArtifact("retracing_bolide_goblet", ArtifactSet.RETRACING_BOLIDE, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> RETRACING_BOLIDE_CIRCLET = registerArtifact("retracing_bolide_circlet", ArtifactSet.RETRACING_BOLIDE, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> TENACITY_OF_THE_MILLELITH_FLOWER = registerArtifact("tenacity_of_the_millelith_flower", ArtifactSet.TENACITY_OF_THE_MILLELITH, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> TENACITY_OF_THE_MILLELITH_FEATHER = registerArtifact("tenacity_of_the_millelith_feather", ArtifactSet.TENACITY_OF_THE_MILLELITH, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> TENACITY_OF_THE_MILLELITH_SANDS = registerArtifact("tenacity_of_the_millelith_sands", ArtifactSet.TENACITY_OF_THE_MILLELITH, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> TENACITY_OF_THE_MILLELITH_GOBLET = registerArtifact("tenacity_of_the_millelith_goblet", ArtifactSet.TENACITY_OF_THE_MILLELITH, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> TENACITY_OF_THE_MILLELITH_CIRCLET = registerArtifact("tenacity_of_the_millelith_circlet", ArtifactSet.TENACITY_OF_THE_MILLELITH, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> PALE_FLAME_FLOWER = registerArtifact("pale_flame_flower", ArtifactSet.PALE_FLAME, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> PALE_FLAME_FEATHER = registerArtifact("pale_flame_feather", ArtifactSet.PALE_FLAME, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> PALE_FLAME_SANDS = registerArtifact("pale_flame_sands", ArtifactSet.PALE_FLAME, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> PALE_FLAME_GOBLET = registerArtifact("pale_flame_goblet", ArtifactSet.PALE_FLAME, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> PALE_FLAME_CIRCLET = registerArtifact("pale_flame_circlet", ArtifactSet.PALE_FLAME, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> SHIMENAWAS_REMINISCENCE_FLOWER = registerArtifact("shimenawas_reminiscence_flower", ArtifactSet.SHIMENAWAS_REMINISCENCE, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> SHIMENAWAS_REMINISCENCE_FEATHER = registerArtifact("shimenawas_reminiscence_feather", ArtifactSet.SHIMENAWAS_REMINISCENCE, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> SHIMENAWAS_REMINISCENCE_SANDS = registerArtifact("shimenawas_reminiscence_sands", ArtifactSet.SHIMENAWAS_REMINISCENCE, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> SHIMENAWAS_REMINISCENCE_GOBLET = registerArtifact("shimenawas_reminiscence_goblet", ArtifactSet.SHIMENAWAS_REMINISCENCE, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> SHIMENAWAS_REMINISCENCE_CIRCLET = registerArtifact("shimenawas_reminiscence_circlet", ArtifactSet.SHIMENAWAS_REMINISCENCE, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> EMBLEM_OF_SEVERED_FATE_FLOWER = registerArtifact("emblem_of_severed_fate_flower", ArtifactSet.EMBLEM_OF_SEVERED_FATE, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> EMBLEM_OF_SEVERED_FATE_FEATHER = registerArtifact("emblem_of_severed_fate_feather", ArtifactSet.EMBLEM_OF_SEVERED_FATE, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> EMBLEM_OF_SEVERED_FATE_SANDS = registerArtifact("emblem_of_severed_fate_sands", ArtifactSet.EMBLEM_OF_SEVERED_FATE, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> EMBLEM_OF_SEVERED_FATE_GOBLET = registerArtifact("emblem_of_severed_fate_goblet", ArtifactSet.EMBLEM_OF_SEVERED_FATE, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> EMBLEM_OF_SEVERED_FATE_CIRCLET = registerArtifact("emblem_of_severed_fate_circlet", ArtifactSet.EMBLEM_OF_SEVERED_FATE, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> HUSK_OF_OPULENT_DREAMS_FLOWER = registerArtifact("husk_of_opulent_dreams_flower", ArtifactSet.HUSK_OF_OPULENT_DREAMS, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> HUSK_OF_OPULENT_DREAMS_FEATHER = registerArtifact("husk_of_opulent_dreams_feather", ArtifactSet.HUSK_OF_OPULENT_DREAMS, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> HUSK_OF_OPULENT_DREAMS_SANDS = registerArtifact("husk_of_opulent_dreams_sands", ArtifactSet.HUSK_OF_OPULENT_DREAMS, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> HUSK_OF_OPULENT_DREAMS_GOBLET = registerArtifact("husk_of_opulent_dreams_goblet", ArtifactSet.HUSK_OF_OPULENT_DREAMS, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> HUSK_OF_OPULENT_DREAMS_CIRCLET = registerArtifact("husk_of_opulent_dreams_circlet", ArtifactSet.HUSK_OF_OPULENT_DREAMS, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> OCEAN_HUED_CLAM_FLOWER = registerArtifact("ocean_hued_clam_flower", ArtifactSet.OCEAN_HUED_CLAM, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> OCEAN_HUED_CLAM_FEATHER = registerArtifact("ocean_hued_clam_feather", ArtifactSet.OCEAN_HUED_CLAM, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> OCEAN_HUED_CLAM_SANDS = registerArtifact("ocean_hued_clam_sands", ArtifactSet.OCEAN_HUED_CLAM, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> OCEAN_HUED_CLAM_GOBLET = registerArtifact("ocean_hued_clam_goblet", ArtifactSet.OCEAN_HUED_CLAM, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> OCEAN_HUED_CLAM_CIRCLET = registerArtifact("ocean_hued_clam_circlet", ArtifactSet.OCEAN_HUED_CLAM, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> VERMILLION_HEREAFTER_FLOWER = registerArtifact("vermillion_hereafter_flower", ArtifactSet.VERMILLION_HEREAFTER, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> VERMILLION_HEREAFTER_FEATHER = registerArtifact("vermillion_hereafter_feather", ArtifactSet.VERMILLION_HEREAFTER, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> VERMILLION_HEREAFTER_SANDS = registerArtifact("vermillion_hereafter_sands", ArtifactSet.VERMILLION_HEREAFTER, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> VERMILLION_HEREAFTER_GOBLET = registerArtifact("vermillion_hereafter_goblet", ArtifactSet.VERMILLION_HEREAFTER, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> VERMILLION_HEREAFTER_CIRCLET = registerArtifact("vermillion_hereafter_circlet", ArtifactSet.VERMILLION_HEREAFTER, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> ECHOES_OF_AN_OFFERING_FLOWER = registerArtifact("echoes_of_an_offering_flower", ArtifactSet.ECHOES_OF_AN_OFFERING, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> ECHOES_OF_AN_OFFERING_FEATHER = registerArtifact("echoes_of_an_offering_feather", ArtifactSet.ECHOES_OF_AN_OFFERING, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> ECHOES_OF_AN_OFFERING_SANDS = registerArtifact("echoes_of_an_offering_sands", ArtifactSet.ECHOES_OF_AN_OFFERING, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> ECHOES_OF_AN_OFFERING_GOBLET = registerArtifact("echoes_of_an_offering_goblet", ArtifactSet.ECHOES_OF_AN_OFFERING, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> ECHOES_OF_AN_OFFERING_CIRCLET = registerArtifact("echoes_of_an_offering_circlet", ArtifactSet.ECHOES_OF_AN_OFFERING, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> DEEPWOOD_MEMORIES_FLOWER = registerArtifact("deepwood_memories_flower", ArtifactSet.DEEPWOOD_MEMORIES, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> DEEPWOOD_MEMORIES_FEATHER = registerArtifact("deepwood_memories_feather", ArtifactSet.DEEPWOOD_MEMORIES, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> DEEPWOOD_MEMORIES_SANDS = registerArtifact("deepwood_memories_sands", ArtifactSet.DEEPWOOD_MEMORIES, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> DEEPWOOD_MEMORIES_GOBLET = registerArtifact("deepwood_memories_goblet", ArtifactSet.DEEPWOOD_MEMORIES, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> DEEPWOOD_MEMORIES_CIRCLET = registerArtifact("deepwood_memories_circlet", ArtifactSet.DEEPWOOD_MEMORIES, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> GILDED_DREAMS_FLOWER = registerArtifact("gilded_dreams_flower", ArtifactSet.GILDED_DREAMS, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> GILDED_DREAMS_FEATHER = registerArtifact("gilded_dreams_feather", ArtifactSet.GILDED_DREAMS, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> GILDED_DREAMS_SANDS = registerArtifact("gilded_dreams_sands", ArtifactSet.GILDED_DREAMS, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> GILDED_DREAMS_GOBLET = registerArtifact("gilded_dreams_goblet", ArtifactSet.GILDED_DREAMS, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> GILDED_DREAMS_CIRCLET = registerArtifact("gilded_dreams_circlet", ArtifactSet.GILDED_DREAMS, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> DESERT_PAVILION_CHRONICLE_FLOWER = registerArtifact("desert_pavilion_chronicle_flower", ArtifactSet.DESERT_PAVILION_CHRONICLE, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> DESERT_PAVILION_CHRONICLE_FEATHER = registerArtifact("desert_pavilion_chronicle_feather", ArtifactSet.DESERT_PAVILION_CHRONICLE, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> DESERT_PAVILION_CHRONICLE_SANDS = registerArtifact("desert_pavilion_chronicle_sands", ArtifactSet.DESERT_PAVILION_CHRONICLE, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> DESERT_PAVILION_CHRONICLE_GOBLET = registerArtifact("desert_pavilion_chronicle_goblet", ArtifactSet.DESERT_PAVILION_CHRONICLE, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> DESERT_PAVILION_CHRONICLE_CIRCLET = registerArtifact("desert_pavilion_chronicle_circlet", ArtifactSet.DESERT_PAVILION_CHRONICLE, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> FLOWER_OF_PARADISE_LOST_FLOWER = registerArtifact("flower_of_paradise_lost_flower", ArtifactSet.FLOWER_OF_PARADISE_LOST, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> FLOWER_OF_PARADISE_LOST_FEATHER = registerArtifact("flower_of_paradise_lost_feather", ArtifactSet.FLOWER_OF_PARADISE_LOST, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> FLOWER_OF_PARADISE_LOST_SANDS = registerArtifact("flower_of_paradise_lost_sands", ArtifactSet.FLOWER_OF_PARADISE_LOST, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> FLOWER_OF_PARADISE_LOST_GOBLET = registerArtifact("flower_of_paradise_lost_goblet", ArtifactSet.FLOWER_OF_PARADISE_LOST, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> FLOWER_OF_PARADISE_LOST_CIRCLET = registerArtifact("flower_of_paradise_lost_circlet", ArtifactSet.FLOWER_OF_PARADISE_LOST, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> NYMPHS_DREAM_FLOWER = registerArtifact("nymphs_dream_flower", ArtifactSet.NYMPHS_DREAM, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> NYMPHS_DREAM_FEATHER = registerArtifact("nymphs_dream_feather", ArtifactSet.NYMPHS_DREAM, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> NYMPHS_DREAM_SANDS = registerArtifact("nymphs_dream_sands", ArtifactSet.NYMPHS_DREAM, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> NYMPHS_DREAM_GOBLET = registerArtifact("nymphs_dream_goblet", ArtifactSet.NYMPHS_DREAM, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> NYMPHS_DREAM_CIRCLET = registerArtifact("nymphs_dream_circlet", ArtifactSet.NYMPHS_DREAM, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> VOURUKASHAS_GLOW_FLOWER = registerArtifact("vourukashas_glow_flower", ArtifactSet.VOURUKASHAS_GLOW, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> VOURUKASHAS_GLOW_FEATHER = registerArtifact("vourukashas_glow_feather", ArtifactSet.VOURUKASHAS_GLOW, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> VOURUKASHAS_GLOW_SANDS = registerArtifact("vourukashas_glow_sands", ArtifactSet.VOURUKASHAS_GLOW, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> VOURUKASHAS_GLOW_GOBLET = registerArtifact("vourukashas_glow_goblet", ArtifactSet.VOURUKASHAS_GLOW, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> VOURUKASHAS_GLOW_CIRCLET = registerArtifact("vourukashas_glow_circlet", ArtifactSet.VOURUKASHAS_GLOW, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> MARECHAUSSEE_HUNTER_FLOWER = registerArtifact("marechaussee_hunter_flower", ArtifactSet.MARECHAUSSEE_HUNTER, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> MARECHAUSSEE_HUNTER_FEATHER = registerArtifact("marechaussee_hunter_feather", ArtifactSet.MARECHAUSSEE_HUNTER, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> MARECHAUSSEE_HUNTER_SANDS = registerArtifact("marechaussee_hunter_sands", ArtifactSet.MARECHAUSSEE_HUNTER, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> MARECHAUSSEE_HUNTER_GOBLET = registerArtifact("marechaussee_hunter_goblet", ArtifactSet.MARECHAUSSEE_HUNTER, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> MARECHAUSSEE_HUNTER_CIRCLET = registerArtifact("marechaussee_hunter_circlet", ArtifactSet.MARECHAUSSEE_HUNTER, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> GOLDEN_TROUPE_FLOWER = registerArtifact("golden_troupe_flower", ArtifactSet.GOLDEN_TROUPE, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> GOLDEN_TROUPE_FEATHER = registerArtifact("golden_troupe_feather", ArtifactSet.GOLDEN_TROUPE, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> GOLDEN_TROUPE_SANDS = registerArtifact("golden_troupe_sands", ArtifactSet.GOLDEN_TROUPE, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> GOLDEN_TROUPE_GOBLET = registerArtifact("golden_troupe_goblet", ArtifactSet.GOLDEN_TROUPE, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> GOLDEN_TROUPE_CIRCLET = registerArtifact("golden_troupe_circlet", ArtifactSet.GOLDEN_TROUPE, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> SONG_OF_DAYS_PAST_FLOWER = registerArtifact("song_of_days_past_flower", ArtifactSet.SONG_OF_DAYS_PAST, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> SONG_OF_DAYS_PAST_FEATHER = registerArtifact("song_of_days_past_feather", ArtifactSet.SONG_OF_DAYS_PAST, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> SONG_OF_DAYS_PAST_SANDS = registerArtifact("song_of_days_past_sands", ArtifactSet.SONG_OF_DAYS_PAST, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> SONG_OF_DAYS_PAST_GOBLET = registerArtifact("song_of_days_past_goblet", ArtifactSet.SONG_OF_DAYS_PAST, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> SONG_OF_DAYS_PAST_CIRCLET = registerArtifact("song_of_days_past_circlet", ArtifactSet.SONG_OF_DAYS_PAST, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> NIGHTTIME_WHISPERS_FLOWER = registerArtifact("nighttime_whispers_flower", ArtifactSet.NIGHTTIME_WHISPERS, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> NIGHTTIME_WHISPERS_FEATHER = registerArtifact("nighttime_whispers_feather", ArtifactSet.NIGHTTIME_WHISPERS, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> NIGHTTIME_WHISPERS_SANDS = registerArtifact("nighttime_whispers_sands", ArtifactSet.NIGHTTIME_WHISPERS, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> NIGHTTIME_WHISPERS_GOBLET = registerArtifact("nighttime_whispers_goblet", ArtifactSet.NIGHTTIME_WHISPERS, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> NIGHTTIME_WHISPERS_CIRCLET = registerArtifact("nighttime_whispers_circlet", ArtifactSet.NIGHTTIME_WHISPERS, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> FRAGMENT_OF_HARMONIC_WHIMSY_FLOWER = registerArtifact("fragment_of_harmonic_whimsy_flower", ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> FRAGMENT_OF_HARMONIC_WHIMSY_FEATHER = registerArtifact("fragment_of_harmonic_whimsy_feather", ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> FRAGMENT_OF_HARMONIC_WHIMSY_SANDS = registerArtifact("fragment_of_harmonic_whimsy_sands", ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> FRAGMENT_OF_HARMONIC_WHIMSY_GOBLET = registerArtifact("fragment_of_harmonic_whimsy_goblet", ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> FRAGMENT_OF_HARMONIC_WHIMSY_CIRCLET = registerArtifact("fragment_of_harmonic_whimsy_circlet", ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> UNFINISHED_REVERIE_FLOWER = registerArtifact("unfinished_reverie_flower", ArtifactSet.UNFINISHED_REVERIE, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> UNFINISHED_REVERIE_FEATHER = registerArtifact("unfinished_reverie_feather", ArtifactSet.UNFINISHED_REVERIE, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> UNFINISHED_REVERIE_SANDS = registerArtifact("unfinished_reverie_sands", ArtifactSet.UNFINISHED_REVERIE, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> UNFINISHED_REVERIE_GOBLET = registerArtifact("unfinished_reverie_goblet", ArtifactSet.UNFINISHED_REVERIE, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> UNFINISHED_REVERIE_CIRCLET = registerArtifact("unfinished_reverie_circlet", ArtifactSet.UNFINISHED_REVERIE, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> SCROLL_OF_THE_HERO_OF_CINDER_CITY_FLOWER = registerArtifact("scroll_of_the_hero_of_cinder_city_flower", ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> SCROLL_OF_THE_HERO_OF_CINDER_CITY_FEATHER = registerArtifact("scroll_of_the_hero_of_cinder_city_feather", ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> SCROLL_OF_THE_HERO_OF_CINDER_CITY_SANDS = registerArtifact("scroll_of_the_hero_of_cinder_city_sands", ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> SCROLL_OF_THE_HERO_OF_CINDER_CITY_GOBLET = registerArtifact("scroll_of_the_hero_of_cinder_city_goblet", ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> SCROLL_OF_THE_HERO_OF_CINDER_CITY_CIRCLET = registerArtifact("scroll_of_the_hero_of_cinder_city_circlet", ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> OBSIDIAN_CODEX_FLOWER = registerArtifact("obsidian_codex_flower", ArtifactSet.OBSIDIAN_CODEX, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> OBSIDIAN_CODEX_FEATHER = registerArtifact("obsidian_codex_feather", ArtifactSet.OBSIDIAN_CODEX, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> OBSIDIAN_CODEX_SANDS = registerArtifact("obsidian_codex_sands", ArtifactSet.OBSIDIAN_CODEX, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> OBSIDIAN_CODEX_GOBLET = registerArtifact("obsidian_codex_goblet", ArtifactSet.OBSIDIAN_CODEX, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> OBSIDIAN_CODEX_CIRCLET = registerArtifact("obsidian_codex_circlet", ArtifactSet.OBSIDIAN_CODEX, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> LONG_NIGHTS_OATH_FLOWER = registerArtifact("long_nights_oath_flower", ArtifactSet.LONG_NIGHTS_OATH, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> LONG_NIGHTS_OATH_FEATHER = registerArtifact("long_nights_oath_feather", ArtifactSet.LONG_NIGHTS_OATH, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> LONG_NIGHTS_OATH_SANDS = registerArtifact("long_nights_oath_sands", ArtifactSet.LONG_NIGHTS_OATH, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> LONG_NIGHTS_OATH_GOBLET = registerArtifact("long_nights_oath_goblet", ArtifactSet.LONG_NIGHTS_OATH, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> LONG_NIGHTS_OATH_CIRCLET = registerArtifact("long_nights_oath_circlet", ArtifactSet.LONG_NIGHTS_OATH, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> FINALE_OF_THE_DEEP_GALLERIES_FLOWER = registerArtifact("finale_of_the_deep_galleries_flower", ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> FINALE_OF_THE_DEEP_GALLERIES_FEATHER = registerArtifact("finale_of_the_deep_galleries_feather", ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> FINALE_OF_THE_DEEP_GALLERIES_SANDS = registerArtifact("finale_of_the_deep_galleries_sands", ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> FINALE_OF_THE_DEEP_GALLERIES_GOBLET = registerArtifact("finale_of_the_deep_galleries_goblet", ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> FINALE_OF_THE_DEEP_GALLERIES_CIRCLET = registerArtifact("finale_of_the_deep_galleries_circlet", ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> NIGHT_OF_THE_SKYS_UNVEILING_FLOWER = registerArtifact("night_of_the_skys_unveiling_flower", ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> NIGHT_OF_THE_SKYS_UNVEILING_FEATHER = registerArtifact("night_of_the_skys_unveiling_feather", ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> NIGHT_OF_THE_SKYS_UNVEILING_SANDS = registerArtifact("night_of_the_skys_unveiling_sands", ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> NIGHT_OF_THE_SKYS_UNVEILING_GOBLET = registerArtifact("night_of_the_skys_unveiling_goblet", ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> NIGHT_OF_THE_SKYS_UNVEILING_CIRCLET = registerArtifact("night_of_the_skys_unveiling_circlet", ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> SILKEN_MOONS_SERENADE_FLOWER = registerArtifact("silken_moons_serenade_flower", ArtifactSet.SILKEN_MOONS_SERENADE, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> SILKEN_MOONS_SERENADE_FEATHER = registerArtifact("silken_moons_serenade_feather", ArtifactSet.SILKEN_MOONS_SERENADE, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> SILKEN_MOONS_SERENADE_SANDS = registerArtifact("silken_moons_serenade_sands", ArtifactSet.SILKEN_MOONS_SERENADE, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> SILKEN_MOONS_SERENADE_GOBLET = registerArtifact("silken_moons_serenade_goblet", ArtifactSet.SILKEN_MOONS_SERENADE, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> SILKEN_MOONS_SERENADE_CIRCLET = registerArtifact("silken_moons_serenade_circlet", ArtifactSet.SILKEN_MOONS_SERENADE, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> AUBADE_OF_MORNINGSTAR_AND_MOON_FLOWER = registerArtifact("aubade_of_morningstar_and_moon_flower", ArtifactSet.AUBADE_OF_MORNINGSTAR_AND_MOON, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> AUBADE_OF_MORNINGSTAR_AND_MOON_FEATHER = registerArtifact("aubade_of_morningstar_and_moon_feather", ArtifactSet.AUBADE_OF_MORNINGSTAR_AND_MOON, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> AUBADE_OF_MORNINGSTAR_AND_MOON_SANDS = registerArtifact("aubade_of_morningstar_and_moon_sands", ArtifactSet.AUBADE_OF_MORNINGSTAR_AND_MOON, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> AUBADE_OF_MORNINGSTAR_AND_MOON_GOBLET = registerArtifact("aubade_of_morningstar_and_moon_goblet", ArtifactSet.AUBADE_OF_MORNINGSTAR_AND_MOON, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> AUBADE_OF_MORNINGSTAR_AND_MOON_CIRCLET = registerArtifact("aubade_of_morningstar_and_moon_circlet", ArtifactSet.AUBADE_OF_MORNINGSTAR_AND_MOON, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> A_DAY_CARVED_FROM_RISING_WINDS_FLOWER = registerArtifact("a_day_carved_from_rising_winds_flower", ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> A_DAY_CARVED_FROM_RISING_WINDS_FEATHER = registerArtifact("a_day_carved_from_rising_winds_feather", ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> A_DAY_CARVED_FROM_RISING_WINDS_SANDS = registerArtifact("a_day_carved_from_rising_winds_sands", ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> A_DAY_CARVED_FROM_RISING_WINDS_GOBLET = registerArtifact("a_day_carved_from_rising_winds_goblet", ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> A_DAY_CARVED_FROM_RISING_WINDS_CIRCLET = registerArtifact("a_day_carved_from_rising_winds_circlet", ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> CELESTIAL_GIFT_FLOWER = registerArtifact("celestial_gift_flower", ArtifactSet.CELESTIAL_GIFT, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> CELESTIAL_GIFT_FEATHER = registerArtifact("celestial_gift_feather", ArtifactSet.CELESTIAL_GIFT, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> CELESTIAL_GIFT_SANDS = registerArtifact("celestial_gift_sands", ArtifactSet.CELESTIAL_GIFT, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> CELESTIAL_GIFT_GOBLET = registerArtifact("celestial_gift_goblet", ArtifactSet.CELESTIAL_GIFT, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> CELESTIAL_GIFT_CIRCLET = registerArtifact("celestial_gift_circlet", ArtifactSet.CELESTIAL_GIFT, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> DISENCHANTMENT_IN_DEEP_SHADOW_FLOWER = registerArtifact("disenchantment_in_deep_shadow_flower", ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> DISENCHANTMENT_IN_DEEP_SHADOW_FEATHER = registerArtifact("disenchantment_in_deep_shadow_feather", ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> DISENCHANTMENT_IN_DEEP_SHADOW_SANDS = registerArtifact("disenchantment_in_deep_shadow_sands", ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> DISENCHANTMENT_IN_DEEP_SHADOW_GOBLET = registerArtifact("disenchantment_in_deep_shadow_goblet", ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> DISENCHANTMENT_IN_DEEP_SHADOW_CIRCLET = registerArtifact("disenchantment_in_deep_shadow_circlet", ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> SCARLET_PROOF_FLOWER = registerArtifact("scarlet_proof_flower", ArtifactSet.SCARLET_PROOF, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> SCARLET_PROOF_FEATHER = registerArtifact("scarlet_proof_feather", ArtifactSet.SCARLET_PROOF, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> SCARLET_PROOF_SANDS = registerArtifact("scarlet_proof_sands", ArtifactSet.SCARLET_PROOF, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> SCARLET_PROOF_GOBLET = registerArtifact("scarlet_proof_goblet", ArtifactSet.SCARLET_PROOF, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> SCARLET_PROOF_CIRCLET = registerArtifact("scarlet_proof_circlet", ArtifactSet.SCARLET_PROOF, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> HEART_OF_THE_FURNACE_FLOWER = registerArtifact("heart_of_the_furnace_flower", ArtifactSet.HEART_OF_THE_FURNACE, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> HEART_OF_THE_FURNACE_FEATHER = registerArtifact("heart_of_the_furnace_feather", ArtifactSet.HEART_OF_THE_FURNACE, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> HEART_OF_THE_FURNACE_SANDS = registerArtifact("heart_of_the_furnace_sands", ArtifactSet.HEART_OF_THE_FURNACE, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> HEART_OF_THE_FURNACE_GOBLET = registerArtifact("heart_of_the_furnace_goblet", ArtifactSet.HEART_OF_THE_FURNACE, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> HEART_OF_THE_FURNACE_CIRCLET = registerArtifact("heart_of_the_furnace_circlet", ArtifactSet.HEART_OF_THE_FURNACE, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> THUNDERSOOTHER_FLOWER = registerArtifact("thundersoother_flower", ArtifactSet.THUNDERSOOTHER, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> THUNDERSOOTHER_FEATHER = registerArtifact("thundersoother_feather", ArtifactSet.THUNDERSOOTHER, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> THUNDERSOOTHER_SANDS = registerArtifact("thundersoother_sands", ArtifactSet.THUNDERSOOTHER, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> THUNDERSOOTHER_GOBLET = registerArtifact("thundersoother_goblet", ArtifactSet.THUNDERSOOTHER, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> THUNDERSOOTHER_CIRCLET = registerArtifact("thundersoother_circlet", ArtifactSet.THUNDERSOOTHER, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> THUNDERING_FURY_FLOWER = registerArtifact("thundering_fury_flower", ArtifactSet.THUNDERING_FURY, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> THUNDERING_FURY_FEATHER = registerArtifact("thundering_fury_feather", ArtifactSet.THUNDERING_FURY, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> THUNDERING_FURY_SANDS = registerArtifact("thundering_fury_sands", ArtifactSet.THUNDERING_FURY, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> THUNDERING_FURY_GOBLET = registerArtifact("thundering_fury_goblet", ArtifactSet.THUNDERING_FURY, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> THUNDERING_FURY_CIRCLET = registerArtifact("thundering_fury_circlet", ArtifactSet.THUNDERING_FURY, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> LAVAWALKER_FLOWER = registerArtifact("lavawalker_flower", ArtifactSet.LAVAWALKER, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> LAVAWALKER_FEATHER = registerArtifact("lavawalker_feather", ArtifactSet.LAVAWALKER, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> LAVAWALKER_SANDS = registerArtifact("lavawalker_sands", ArtifactSet.LAVAWALKER, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> LAVAWALKER_GOBLET = registerArtifact("lavawalker_goblet", ArtifactSet.LAVAWALKER, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> LAVAWALKER_CIRCLET = registerArtifact("lavawalker_circlet", ArtifactSet.LAVAWALKER, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> CRIMSON_WITCH_OF_FLAMES_FLOWER = registerArtifact("crimson_witch_of_flames_flower", ArtifactSet.CRIMSON_WITCH_OF_FLAMES, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> CRIMSON_WITCH_OF_FLAMES_FEATHER = registerArtifact("crimson_witch_of_flames_feather", ArtifactSet.CRIMSON_WITCH_OF_FLAMES, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> CRIMSON_WITCH_OF_FLAMES_SANDS = registerArtifact("crimson_witch_of_flames_sands", ArtifactSet.CRIMSON_WITCH_OF_FLAMES, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> CRIMSON_WITCH_OF_FLAMES_GOBLET = registerArtifact("crimson_witch_of_flames_goblet", ArtifactSet.CRIMSON_WITCH_OF_FLAMES, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> CRIMSON_WITCH_OF_FLAMES_CIRCLET = registerArtifact("crimson_witch_of_flames_circlet", ArtifactSet.CRIMSON_WITCH_OF_FLAMES, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> BLIZZARD_STRAYER_FLOWER = registerArtifact("blizzard_strayer_flower", ArtifactSet.BLIZZARD_STRAYER, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> BLIZZARD_STRAYER_FEATHER = registerArtifact("blizzard_strayer_feather", ArtifactSet.BLIZZARD_STRAYER, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> BLIZZARD_STRAYER_SANDS = registerArtifact("blizzard_strayer_sands", ArtifactSet.BLIZZARD_STRAYER, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> BLIZZARD_STRAYER_GOBLET = registerArtifact("blizzard_strayer_goblet", ArtifactSet.BLIZZARD_STRAYER, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> BLIZZARD_STRAYER_CIRCLET = registerArtifact("blizzard_strayer_circlet", ArtifactSet.BLIZZARD_STRAYER, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> HEART_OF_DEPTH_FLOWER = registerArtifact("heart_of_depth_flower", ArtifactSet.HEART_OF_DEPTH, ArtifactSlot.FLOWER);
    public static final DeferredItem<ArtifactItem> HEART_OF_DEPTH_FEATHER = registerArtifact("heart_of_depth_feather", ArtifactSet.HEART_OF_DEPTH, ArtifactSlot.FEATHER);
    public static final DeferredItem<ArtifactItem> HEART_OF_DEPTH_SANDS = registerArtifact("heart_of_depth_sands", ArtifactSet.HEART_OF_DEPTH, ArtifactSlot.SANDS);
    public static final DeferredItem<ArtifactItem> HEART_OF_DEPTH_GOBLET = registerArtifact("heart_of_depth_goblet", ArtifactSet.HEART_OF_DEPTH, ArtifactSlot.GOBLET);
    public static final DeferredItem<ArtifactItem> HEART_OF_DEPTH_CIRCLET = registerArtifact("heart_of_depth_circlet", ArtifactSet.HEART_OF_DEPTH, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> PRAYERS_FOR_THUNDER_CIRCLET = registerArtifact("prayers_for_thunder_circlet", ArtifactSet.PRAYERS_FOR_THUNDER, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> PRAYERS_FOR_DESTINY_CIRCLET = registerArtifact("prayers_for_destiny_circlet", ArtifactSet.PRAYERS_FOR_DESTINY, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> PRAYERS_FOR_ILLUMINATION_CIRCLET = registerArtifact("prayers_for_illumination_circlet", ArtifactSet.PRAYERS_FOR_ILLUMINATION, ArtifactSlot.CIRCLET);
    public static final DeferredItem<ArtifactItem> PRAYERS_TO_SPRINGTIME_CIRCLET = registerArtifact("prayers_to_springtime_circlet", ArtifactSet.PRAYERS_TO_SPRINGTIME, ArtifactSlot.CIRCLET);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EQUIPMENT_TAB = CREATIVE_MODE_TABS.register(
            MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MODID))
                    .withTabsBefore(CreativeModeTabs.TOOLS_AND_UTILITIES)
                    .icon(() -> LUCKY_FEATHER.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(CRYSTAL_CORE.get());
                        output.accept(CONDENSED_RESIN.get());
                        output.accept(SANCTIFYING_ELIXIR.get());
                        output.accept(SANCTIFYING_ESSENCE.get());
                        output.accept(SANCTIFYING_UNCTION.get());
                        output.accept(AlchemyTableContent.ITEM.get());
                        output.accept(WITCHS_REVELATION_CASE.get());
                        output.accept(THE_LITTLE_WITCHS_DICTIONARY.get());
                        output.accept(INITIATE_FLOWER.get());
                        output.accept(INITIATE_FEATHER.get());
                        output.accept(LUCKY_FLOWER.get());
                        output.accept(LUCKY_FEATHER.get());
                        output.accept(LUCKY_SANDS.get());
                        output.accept(LUCKY_GOBLET.get());
                        output.accept(LUCKY_CIRCLET.get());
                        output.accept(ADVENTURER_FLOWER.get());
                        output.accept(ADVENTURER_FEATHER.get());
                        output.accept(ADVENTURER_SANDS.get());
                        output.accept(ADVENTURER_GOBLET.get());
                        output.accept(ADVENTURER_CIRCLET.get());
                        output.accept(TRAVELING_DOCTOR_FLOWER.get());
                        output.accept(TRAVELING_DOCTOR_FEATHER.get());
                        output.accept(TRAVELING_DOCTOR_SANDS.get());
                        output.accept(TRAVELING_DOCTOR_GOBLET.get());
                        output.accept(TRAVELING_DOCTOR_CIRCLET.get());
                        output.accept(RESOLUTION_OF_SOJOURNER_FLOWER.get());
                        output.accept(RESOLUTION_OF_SOJOURNER_FEATHER.get());
                        output.accept(RESOLUTION_OF_SOJOURNER_SANDS.get());
                        output.accept(RESOLUTION_OF_SOJOURNER_GOBLET.get());
                        output.accept(RESOLUTION_OF_SOJOURNER_CIRCLET.get());
                        output.accept(TINY_MIRACLE_FLOWER.get());
                        output.accept(TINY_MIRACLE_FEATHER.get());
                        output.accept(TINY_MIRACLE_SANDS.get());
                        output.accept(TINY_MIRACLE_GOBLET.get());
                        output.accept(TINY_MIRACLE_CIRCLET.get());
                        output.accept(BERSERKER_FLOWER.get());
                        output.accept(BERSERKER_FEATHER.get());
                        output.accept(BERSERKER_SANDS.get());
                        output.accept(BERSERKER_GOBLET.get());
                        output.accept(BERSERKER_CIRCLET.get());
                        output.accept(INSTRUCTOR_FLOWER.get());
                        output.accept(INSTRUCTOR_FEATHER.get());
                        output.accept(INSTRUCTOR_SANDS.get());
                        output.accept(INSTRUCTOR_GOBLET.get());
                        output.accept(INSTRUCTOR_CIRCLET.get());
                        output.accept(THE_EXILE_FLOWER.get());
                        output.accept(THE_EXILE_FEATHER.get());
                        output.accept(THE_EXILE_SANDS.get());
                        output.accept(THE_EXILE_GOBLET.get());
                        output.accept(THE_EXILE_CIRCLET.get());
                        output.accept(DEFENDERS_WILL_FLOWER.get());
                        output.accept(DEFENDERS_WILL_FEATHER.get());
                        output.accept(DEFENDERS_WILL_SANDS.get());
                        output.accept(DEFENDERS_WILL_GOBLET.get());
                        output.accept(DEFENDERS_WILL_CIRCLET.get());
                        output.accept(BRAVE_HEART_FLOWER.get());
                        output.accept(BRAVE_HEART_FEATHER.get());
                        output.accept(BRAVE_HEART_SANDS.get());
                        output.accept(BRAVE_HEART_GOBLET.get());
                        output.accept(BRAVE_HEART_CIRCLET.get());
                        output.accept(MARTIAL_ARTIST_FLOWER.get());
                        output.accept(MARTIAL_ARTIST_FEATHER.get());
                        output.accept(MARTIAL_ARTIST_SANDS.get());
                        output.accept(MARTIAL_ARTIST_GOBLET.get());
                        output.accept(MARTIAL_ARTIST_CIRCLET.get());
                        output.accept(GAMBLER_FLOWER.get());
                        output.accept(GAMBLER_FEATHER.get());
                        output.accept(GAMBLER_SANDS.get());
                        output.accept(GAMBLER_GOBLET.get());
                        output.accept(GAMBLER_CIRCLET.get());
                        output.accept(SCHOLAR_FLOWER.get());
                        output.accept(SCHOLAR_FEATHER.get());
                        output.accept(SCHOLAR_SANDS.get());
                        output.accept(SCHOLAR_GOBLET.get());
                        output.accept(SCHOLAR_CIRCLET.get());
                        output.accept(GLADIATORS_FINALE_FLOWER.get());
                        output.accept(GLADIATORS_FINALE_FEATHER.get());
                        output.accept(GLADIATORS_FINALE_SANDS.get());
                        output.accept(GLADIATORS_FINALE_GOBLET.get());
                        output.accept(GLADIATORS_FINALE_CIRCLET.get());
                        output.accept(WANDERERS_TROUPE_FLOWER.get());
                        output.accept(WANDERERS_TROUPE_FEATHER.get());
                        output.accept(WANDERERS_TROUPE_SANDS.get());
                        output.accept(WANDERERS_TROUPE_GOBLET.get());
                        output.accept(WANDERERS_TROUPE_CIRCLET.get());
                        output.accept(THUNDERING_FURY_FLOWER.get());
                        output.accept(THUNDERING_FURY_FEATHER.get());
                        output.accept(THUNDERING_FURY_SANDS.get());
                        output.accept(THUNDERING_FURY_GOBLET.get());
                        output.accept(THUNDERING_FURY_CIRCLET.get());
                        output.accept(THUNDERSOOTHER_FLOWER.get());
                        output.accept(THUNDERSOOTHER_FEATHER.get());
                        output.accept(THUNDERSOOTHER_SANDS.get());
                        output.accept(THUNDERSOOTHER_GOBLET.get());
                        output.accept(THUNDERSOOTHER_CIRCLET.get());
                        output.accept(PALE_FLAME_FLOWER.get());
                        output.accept(PALE_FLAME_FEATHER.get());
                        output.accept(PALE_FLAME_SANDS.get());
                        output.accept(PALE_FLAME_GOBLET.get());
                        output.accept(PALE_FLAME_CIRCLET.get());
                        output.accept(TENACITY_OF_THE_MILLELITH_FLOWER.get());
                        output.accept(TENACITY_OF_THE_MILLELITH_FEATHER.get());
                        output.accept(TENACITY_OF_THE_MILLELITH_SANDS.get());
                        output.accept(TENACITY_OF_THE_MILLELITH_GOBLET.get());
                        output.accept(TENACITY_OF_THE_MILLELITH_CIRCLET.get());
                        output.accept(CELESTIAL_GIFT_FLOWER.get());
                        output.accept(CELESTIAL_GIFT_FEATHER.get());
                        output.accept(CELESTIAL_GIFT_SANDS.get());
                        output.accept(CELESTIAL_GIFT_GOBLET.get());
                        output.accept(CELESTIAL_GIFT_CIRCLET.get());
                        output.accept(DISENCHANTMENT_IN_DEEP_SHADOW_FLOWER.get());
                        output.accept(DISENCHANTMENT_IN_DEEP_SHADOW_FEATHER.get());
                        output.accept(DISENCHANTMENT_IN_DEEP_SHADOW_SANDS.get());
                        output.accept(DISENCHANTMENT_IN_DEEP_SHADOW_GOBLET.get());
                        output.accept(DISENCHANTMENT_IN_DEEP_SHADOW_CIRCLET.get());
                        output.accept(BLIZZARD_STRAYER_FLOWER.get());
                        output.accept(BLIZZARD_STRAYER_FEATHER.get());
                        output.accept(BLIZZARD_STRAYER_SANDS.get());
                        output.accept(BLIZZARD_STRAYER_GOBLET.get());
                        output.accept(BLIZZARD_STRAYER_CIRCLET.get());
                        output.accept(HEART_OF_DEPTH_FLOWER.get());
                        output.accept(HEART_OF_DEPTH_FEATHER.get());
                        output.accept(HEART_OF_DEPTH_SANDS.get());
                        output.accept(HEART_OF_DEPTH_GOBLET.get());
                        output.accept(HEART_OF_DEPTH_CIRCLET.get());
                        output.accept(MAIDEN_BELOVED_FLOWER.get());
                        output.accept(MAIDEN_BELOVED_FEATHER.get());
                        output.accept(MAIDEN_BELOVED_SANDS.get());
                        output.accept(MAIDEN_BELOVED_GOBLET.get());
                        output.accept(MAIDEN_BELOVED_CIRCLET.get());
                        output.accept(VIRIDESCENT_VENERER_FLOWER.get());
                        output.accept(VIRIDESCENT_VENERER_FEATHER.get());
                        output.accept(VIRIDESCENT_VENERER_SANDS.get());
                        output.accept(VIRIDESCENT_VENERER_GOBLET.get());
                        output.accept(VIRIDESCENT_VENERER_CIRCLET.get());
                        output.accept(BLOODSTAINED_CHIVALRY_FLOWER.get());
                        output.accept(BLOODSTAINED_CHIVALRY_FEATHER.get());
                        output.accept(BLOODSTAINED_CHIVALRY_SANDS.get());
                        output.accept(BLOODSTAINED_CHIVALRY_GOBLET.get());
                        output.accept(BLOODSTAINED_CHIVALRY_CIRCLET.get());
                        output.accept(NOBLESSE_OBLIGE_FLOWER.get());
                        output.accept(NOBLESSE_OBLIGE_FEATHER.get());
                        output.accept(NOBLESSE_OBLIGE_SANDS.get());
                        output.accept(NOBLESSE_OBLIGE_GOBLET.get());
                        output.accept(NOBLESSE_OBLIGE_CIRCLET.get());
                        output.accept(ARCHAIC_PETRA_FLOWER.get());
                        output.accept(ARCHAIC_PETRA_FEATHER.get());
                        output.accept(ARCHAIC_PETRA_SANDS.get());
                        output.accept(ARCHAIC_PETRA_GOBLET.get());
                        output.accept(ARCHAIC_PETRA_CIRCLET.get());
                        output.accept(RETRACING_BOLIDE_FLOWER.get());
                        output.accept(RETRACING_BOLIDE_FEATHER.get());
                        output.accept(RETRACING_BOLIDE_SANDS.get());
                        output.accept(RETRACING_BOLIDE_GOBLET.get());
                        output.accept(RETRACING_BOLIDE_CIRCLET.get());
                        output.accept(ECHOES_OF_AN_OFFERING_FLOWER.get());
                        output.accept(ECHOES_OF_AN_OFFERING_FEATHER.get());
                        output.accept(ECHOES_OF_AN_OFFERING_SANDS.get());
                        output.accept(ECHOES_OF_AN_OFFERING_GOBLET.get());
                        output.accept(ECHOES_OF_AN_OFFERING_CIRCLET.get());
                        output.accept(VERMILLION_HEREAFTER_FLOWER.get());
                        output.accept(VERMILLION_HEREAFTER_FEATHER.get());
                        output.accept(VERMILLION_HEREAFTER_SANDS.get());
                        output.accept(VERMILLION_HEREAFTER_GOBLET.get());
                        output.accept(VERMILLION_HEREAFTER_CIRCLET.get());
                        output.accept(CRIMSON_WITCH_OF_FLAMES_FLOWER.get());
                        output.accept(CRIMSON_WITCH_OF_FLAMES_FEATHER.get());
                        output.accept(CRIMSON_WITCH_OF_FLAMES_SANDS.get());
                        output.accept(CRIMSON_WITCH_OF_FLAMES_GOBLET.get());
                        output.accept(CRIMSON_WITCH_OF_FLAMES_CIRCLET.get());
                        output.accept(LAVAWALKER_FLOWER.get());
                        output.accept(LAVAWALKER_FEATHER.get());
                        output.accept(LAVAWALKER_SANDS.get());
                        output.accept(LAVAWALKER_GOBLET.get());
                        output.accept(LAVAWALKER_CIRCLET.get());
                        output.accept(EMBLEM_OF_SEVERED_FATE_FLOWER.get());
                        output.accept(EMBLEM_OF_SEVERED_FATE_FEATHER.get());
                        output.accept(EMBLEM_OF_SEVERED_FATE_SANDS.get());
                        output.accept(EMBLEM_OF_SEVERED_FATE_GOBLET.get());
                        output.accept(EMBLEM_OF_SEVERED_FATE_CIRCLET.get());
                        output.accept(SHIMENAWAS_REMINISCENCE_FLOWER.get());
                        output.accept(SHIMENAWAS_REMINISCENCE_FEATHER.get());
                        output.accept(SHIMENAWAS_REMINISCENCE_SANDS.get());
                        output.accept(SHIMENAWAS_REMINISCENCE_GOBLET.get());
                        output.accept(SHIMENAWAS_REMINISCENCE_CIRCLET.get());
                        output.accept(HUSK_OF_OPULENT_DREAMS_FLOWER.get());
                        output.accept(HUSK_OF_OPULENT_DREAMS_FEATHER.get());
                        output.accept(HUSK_OF_OPULENT_DREAMS_SANDS.get());
                        output.accept(HUSK_OF_OPULENT_DREAMS_GOBLET.get());
                        output.accept(HUSK_OF_OPULENT_DREAMS_CIRCLET.get());
                        output.accept(OCEAN_HUED_CLAM_FLOWER.get());
                        output.accept(OCEAN_HUED_CLAM_FEATHER.get());
                        output.accept(OCEAN_HUED_CLAM_SANDS.get());
                        output.accept(OCEAN_HUED_CLAM_GOBLET.get());
                        output.accept(OCEAN_HUED_CLAM_CIRCLET.get());
                        output.accept(NYMPHS_DREAM_FLOWER.get());
                        output.accept(NYMPHS_DREAM_FEATHER.get());
                        output.accept(NYMPHS_DREAM_SANDS.get());
                        output.accept(NYMPHS_DREAM_GOBLET.get());
                        output.accept(NYMPHS_DREAM_CIRCLET.get());
                        output.accept(VOURUKASHAS_GLOW_FLOWER.get());
                        output.accept(VOURUKASHAS_GLOW_FEATHER.get());
                        output.accept(VOURUKASHAS_GLOW_SANDS.get());
                        output.accept(VOURUKASHAS_GLOW_GOBLET.get());
                        output.accept(VOURUKASHAS_GLOW_CIRCLET.get());
                        output.accept(DEEPWOOD_MEMORIES_FLOWER.get());
                        output.accept(DEEPWOOD_MEMORIES_FEATHER.get());
                        output.accept(DEEPWOOD_MEMORIES_SANDS.get());
                        output.accept(DEEPWOOD_MEMORIES_GOBLET.get());
                        output.accept(DEEPWOOD_MEMORIES_CIRCLET.get());
                        output.accept(GILDED_DREAMS_FLOWER.get());
                        output.accept(GILDED_DREAMS_FEATHER.get());
                        output.accept(GILDED_DREAMS_SANDS.get());
                        output.accept(GILDED_DREAMS_GOBLET.get());
                        output.accept(GILDED_DREAMS_CIRCLET.get());
                        output.accept(DESERT_PAVILION_CHRONICLE_FLOWER.get());
                        output.accept(DESERT_PAVILION_CHRONICLE_FEATHER.get());
                        output.accept(DESERT_PAVILION_CHRONICLE_SANDS.get());
                        output.accept(DESERT_PAVILION_CHRONICLE_GOBLET.get());
                        output.accept(DESERT_PAVILION_CHRONICLE_CIRCLET.get());
                        output.accept(FLOWER_OF_PARADISE_LOST_FLOWER.get());
                        output.accept(FLOWER_OF_PARADISE_LOST_FEATHER.get());
                        output.accept(FLOWER_OF_PARADISE_LOST_SANDS.get());
                        output.accept(FLOWER_OF_PARADISE_LOST_GOBLET.get());
                        output.accept(FLOWER_OF_PARADISE_LOST_CIRCLET.get());
                        output.accept(NIGHTTIME_WHISPERS_FLOWER.get());
                        output.accept(NIGHTTIME_WHISPERS_FEATHER.get());
                        output.accept(NIGHTTIME_WHISPERS_SANDS.get());
                        output.accept(NIGHTTIME_WHISPERS_GOBLET.get());
                        output.accept(NIGHTTIME_WHISPERS_CIRCLET.get());
                        output.accept(SONG_OF_DAYS_PAST_FLOWER.get());
                        output.accept(SONG_OF_DAYS_PAST_FEATHER.get());
                        output.accept(SONG_OF_DAYS_PAST_SANDS.get());
                        output.accept(SONG_OF_DAYS_PAST_GOBLET.get());
                        output.accept(SONG_OF_DAYS_PAST_CIRCLET.get());
                        output.accept(GOLDEN_TROUPE_FLOWER.get());
                        output.accept(GOLDEN_TROUPE_FEATHER.get());
                        output.accept(GOLDEN_TROUPE_SANDS.get());
                        output.accept(GOLDEN_TROUPE_GOBLET.get());
                        output.accept(GOLDEN_TROUPE_CIRCLET.get());
                        output.accept(MARECHAUSSEE_HUNTER_FLOWER.get());
                        output.accept(MARECHAUSSEE_HUNTER_FEATHER.get());
                        output.accept(MARECHAUSSEE_HUNTER_SANDS.get());
                        output.accept(MARECHAUSSEE_HUNTER_GOBLET.get());
                        output.accept(MARECHAUSSEE_HUNTER_CIRCLET.get());
                        output.accept(FRAGMENT_OF_HARMONIC_WHIMSY_FLOWER.get());
                        output.accept(FRAGMENT_OF_HARMONIC_WHIMSY_FEATHER.get());
                        output.accept(FRAGMENT_OF_HARMONIC_WHIMSY_SANDS.get());
                        output.accept(FRAGMENT_OF_HARMONIC_WHIMSY_GOBLET.get());
                        output.accept(FRAGMENT_OF_HARMONIC_WHIMSY_CIRCLET.get());
                        output.accept(UNFINISHED_REVERIE_FLOWER.get());
                        output.accept(UNFINISHED_REVERIE_FEATHER.get());
                        output.accept(UNFINISHED_REVERIE_SANDS.get());
                        output.accept(UNFINISHED_REVERIE_GOBLET.get());
                        output.accept(UNFINISHED_REVERIE_CIRCLET.get());
                        output.accept(FINALE_OF_THE_DEEP_GALLERIES_FLOWER.get());
                        output.accept(FINALE_OF_THE_DEEP_GALLERIES_FEATHER.get());
                        output.accept(FINALE_OF_THE_DEEP_GALLERIES_SANDS.get());
                        output.accept(FINALE_OF_THE_DEEP_GALLERIES_GOBLET.get());
                        output.accept(FINALE_OF_THE_DEEP_GALLERIES_CIRCLET.get());
                        output.accept(LONG_NIGHTS_OATH_FLOWER.get());
                        output.accept(LONG_NIGHTS_OATH_FEATHER.get());
                        output.accept(LONG_NIGHTS_OATH_SANDS.get());
                        output.accept(LONG_NIGHTS_OATH_GOBLET.get());
                        output.accept(LONG_NIGHTS_OATH_CIRCLET.get());
                        output.accept(OBSIDIAN_CODEX_FLOWER.get());
                        output.accept(OBSIDIAN_CODEX_FEATHER.get());
                        output.accept(OBSIDIAN_CODEX_SANDS.get());
                        output.accept(OBSIDIAN_CODEX_GOBLET.get());
                        output.accept(OBSIDIAN_CODEX_CIRCLET.get());
                        output.accept(SCROLL_OF_THE_HERO_OF_CINDER_CITY_FLOWER.get());
                        output.accept(SCROLL_OF_THE_HERO_OF_CINDER_CITY_FEATHER.get());
                        output.accept(SCROLL_OF_THE_HERO_OF_CINDER_CITY_SANDS.get());
                        output.accept(SCROLL_OF_THE_HERO_OF_CINDER_CITY_GOBLET.get());
                        output.accept(SCROLL_OF_THE_HERO_OF_CINDER_CITY_CIRCLET.get());
                        output.accept(A_DAY_CARVED_FROM_RISING_WINDS_FLOWER.get());
                        output.accept(A_DAY_CARVED_FROM_RISING_WINDS_FEATHER.get());
                        output.accept(A_DAY_CARVED_FROM_RISING_WINDS_SANDS.get());
                        output.accept(A_DAY_CARVED_FROM_RISING_WINDS_GOBLET.get());
                        output.accept(A_DAY_CARVED_FROM_RISING_WINDS_CIRCLET.get());
                        output.accept(AUBADE_OF_MORNINGSTAR_AND_MOON_FLOWER.get());
                        output.accept(AUBADE_OF_MORNINGSTAR_AND_MOON_FEATHER.get());
                        output.accept(AUBADE_OF_MORNINGSTAR_AND_MOON_SANDS.get());
                        output.accept(AUBADE_OF_MORNINGSTAR_AND_MOON_GOBLET.get());
                        output.accept(AUBADE_OF_MORNINGSTAR_AND_MOON_CIRCLET.get());
                        output.accept(NIGHT_OF_THE_SKYS_UNVEILING_FLOWER.get());
                        output.accept(NIGHT_OF_THE_SKYS_UNVEILING_FEATHER.get());
                        output.accept(NIGHT_OF_THE_SKYS_UNVEILING_SANDS.get());
                        output.accept(NIGHT_OF_THE_SKYS_UNVEILING_GOBLET.get());
                        output.accept(NIGHT_OF_THE_SKYS_UNVEILING_CIRCLET.get());
                        output.accept(SILKEN_MOONS_SERENADE_FLOWER.get());
                        output.accept(SILKEN_MOONS_SERENADE_FEATHER.get());
                        output.accept(SILKEN_MOONS_SERENADE_SANDS.get());
                        output.accept(SILKEN_MOONS_SERENADE_GOBLET.get());
                        output.accept(SILKEN_MOONS_SERENADE_CIRCLET.get());
                        output.accept(HEART_OF_THE_FURNACE_FLOWER.get());
                        output.accept(HEART_OF_THE_FURNACE_FEATHER.get());
                        output.accept(HEART_OF_THE_FURNACE_SANDS.get());
                        output.accept(HEART_OF_THE_FURNACE_GOBLET.get());
                        output.accept(HEART_OF_THE_FURNACE_CIRCLET.get());
                        output.accept(SCARLET_PROOF_FLOWER.get());
                        output.accept(SCARLET_PROOF_FEATHER.get());
                        output.accept(SCARLET_PROOF_SANDS.get());
                        output.accept(SCARLET_PROOF_GOBLET.get());
                        output.accept(SCARLET_PROOF_CIRCLET.get());
                        acceptIronSpellbookArtifacts(output);
                        output.accept(PRAYERS_FOR_THUNDER_CIRCLET.get());
                        output.accept(PRAYERS_FOR_DESTINY_CIRCLET.get());
                        output.accept(PRAYERS_FOR_ILLUMINATION_CIRCLET.get());
                        output.accept(PRAYERS_TO_SPRINGTIME_CIRCLET.get());
                        output.accept(ANEMO_CRYSTALFLY_SPAWN_EGG.get());
                        output.accept(ICE_CRYSTALFLY_SPAWN_EGG.get());
                        output.accept(GEO_CRYSTALFLY_SPAWN_EGG.get());
                        output.accept(ELECTRO_CRYSTALFLY_SPAWN_EGG.get());
                        output.accept(DENDRO_CRYSTALFLY_SPAWN_EGG.get());
                        output.accept(HYDRO_CRYSTALFLY_SPAWN_EGG.get());
                        output.accept(PYRO_CRYSTALFLY_SPAWN_EGG.get());
                        output.accept(LeylineSpawnerContent.MONDSTADT_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.MONDSTADT_RIDGE_WATCH_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.MONDSTADT_THORNY_CROWN_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.MONDSTADT_PEAK_VINDAGNYR_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.MONDSTADT_VALLEY_REMEMBRANCE_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.LIYUE_CLEAR_POOL_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.LIYUE_DOMAIN_GUYUN_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.LIYUE_LOST_VALLEY_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.LIYUE_ZHOU_FORMULA_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.INAZUMA_MOMIJI_DYED_COURT_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.INAZUMA_SLUMBERING_COURT_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.SUMERU_MOLTEN_IRON_FORTRESS_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.SUMERU_SOLITARY_ENLIGHTENMENT_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.SUMERU_CITY_GOLD_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.FONTAINE_WATERFALL_WEN_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.FONTAINE_DENOUEMENT_SIN_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.FONTAINE_FADED_THEATER_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.NATLAN_DERELICT_MASONRY_DOCK_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.NATLAN_RAINBOW_SANCTUM_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.NOD_KRAI_MOONCHILDS_TREASURES_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.NOD_KRAI_FROSTLADEN_MACHINERY_LEYLINE_SPAWNER_ITEM.get());
                        output.accept(LeylineSpawnerContent.SNEZHNAYA_INVERTED_GLACIER_LEYLINE_SPAWNER_ITEM.get());
                    })
                    .build()
    );

    public TeyvatArtifacts(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, TeyvatArtifactsConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, ArtifactSetConfig.SPEC, "teyvat_artifacts-set-bonuses.toml");
        LeylineTrialMonsterPools.initialize();
        DATA_COMPONENT_TYPES.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        ITEMS.register(modEventBus);
        ATTRIBUTES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        LeylineSpawnerContent.register(modEventBus);
        AlchemyTableContent.register(modEventBus);
        DedicatedCurioValidator.register();
        modEventBus.addListener(ArtifactStat::onConfigLoading);
        modEventBus.addListener(ArtifactStat::onConfigReloading);
        modEventBus.addListener(LeylineSpawnerGenerationPack::onAddPackFinders);
        modEventBus.addListener(ArtifactEvents::onEntityAttributeCreation);
        modEventBus.addListener(ArtifactEvents::onRegisterSpawnPlacements);
        modEventBus.addListener(ArtifactEvents::onEntityAttributeModification);
        NeoForge.EVENT_BUS.addListener(ArtifactEvents::onItemTooltip);
        NeoForge.EVENT_BUS.addListener(ArtifactEvents::onPlayerLoggedIn);
        NeoForge.EVENT_BUS.addListener(ArtifactEvents::onPlayerClone);
        NeoForge.EVENT_BUS.addListener(ArtifactEvents::onAdvancementEarned);
        NeoForge.EVENT_BUS.addListener(ArtifactEvents::onCurioChange);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, ArtifactEvents::onCurioCanEquip);
        NeoForge.EVENT_BUS.addListener(ArtifactEvents::onPlayerTickPost);
        NeoForge.EVENT_BUS.addListener(ArtifactEvents::onMobEffectAdded);
        NeoForge.EVENT_BUS.addListener(ArtifactEvents::onRightClickBlock);
        NeoForge.EVENT_BUS.addListener(ArtifactEvents::onLivingHeal);
        NeoForge.EVENT_BUS.addListener(ArtifactEvents::onLivingItemUseFinish);
        NeoForge.EVENT_BUS.addListener(ArtifactEvents::onLivingShieldBlock);
        NeoForge.EVENT_BUS.addListener(ArtifactEvents::onSweepAttack);
        NeoForge.EVENT_BUS.addListener(ArtifactEvents::onLivingIncomingDamage);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, ArtifactEvents::onLivingDamagePre);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, ArtifactEvents::onLivingDrops);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, ArtifactEvents::onLivingExperienceDrop);
        NeoForge.EVENT_BUS.addListener(ArtifactEvents::onLivingDeath);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, ArtifactEvents::onCriticalHit);
        NeoForge.EVENT_BUS.addListener(ArtifactEvents::onPlayerXpPickup);
    }

    private static DeferredItem<ArtifactItem> registerArtifact(String name, ArtifactSet set, ArtifactSlot slot) {
        return ITEMS.register(name, () -> new ArtifactItem(set, slot, new Item.Properties()));
    }

    private static DeferredHolder<EntityType<?>, EntityType<Crystalfly>> registerCrystalfly(String name) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.<Crystalfly>of(Crystalfly::new, MobCategory.AMBIENT)
                .sized(0.6F, 0.6F)
                .clientTrackingRange(5)
                .updateInterval(3)
                .build(MODID + ":" + name));
    }

    private static void acceptIronSpellbookArtifacts(CreativeModeTab.Output output) {
        output.accept(THUNDERSOOTHER_FLOWER.get());
        output.accept(THUNDERSOOTHER_FEATHER.get());
        output.accept(THUNDERSOOTHER_SANDS.get());
        output.accept(THUNDERSOOTHER_GOBLET.get());
        output.accept(THUNDERSOOTHER_CIRCLET.get());
        output.accept(THUNDERING_FURY_FLOWER.get());
        output.accept(THUNDERING_FURY_FEATHER.get());
        output.accept(THUNDERING_FURY_SANDS.get());
        output.accept(THUNDERING_FURY_GOBLET.get());
        output.accept(THUNDERING_FURY_CIRCLET.get());
        output.accept(LAVAWALKER_FLOWER.get());
        output.accept(LAVAWALKER_FEATHER.get());
        output.accept(LAVAWALKER_SANDS.get());
        output.accept(LAVAWALKER_GOBLET.get());
        output.accept(LAVAWALKER_CIRCLET.get());
        output.accept(CRIMSON_WITCH_OF_FLAMES_FLOWER.get());
        output.accept(CRIMSON_WITCH_OF_FLAMES_FEATHER.get());
        output.accept(CRIMSON_WITCH_OF_FLAMES_SANDS.get());
        output.accept(CRIMSON_WITCH_OF_FLAMES_GOBLET.get());
        output.accept(CRIMSON_WITCH_OF_FLAMES_CIRCLET.get());
        output.accept(BLIZZARD_STRAYER_FLOWER.get());
        output.accept(BLIZZARD_STRAYER_FEATHER.get());
        output.accept(BLIZZARD_STRAYER_SANDS.get());
        output.accept(BLIZZARD_STRAYER_GOBLET.get());
        output.accept(BLIZZARD_STRAYER_CIRCLET.get());
        output.accept(DEEPWOOD_MEMORIES_FLOWER.get());
        output.accept(DEEPWOOD_MEMORIES_FEATHER.get());
        output.accept(DEEPWOOD_MEMORIES_SANDS.get());
        output.accept(DEEPWOOD_MEMORIES_GOBLET.get());
        output.accept(DEEPWOOD_MEMORIES_CIRCLET.get());
        output.accept(GOLDEN_TROUPE_FLOWER.get());
        output.accept(GOLDEN_TROUPE_FEATHER.get());
        output.accept(GOLDEN_TROUPE_SANDS.get());
        output.accept(GOLDEN_TROUPE_GOBLET.get());
        output.accept(GOLDEN_TROUPE_CIRCLET.get());
        output.accept(FRAGMENT_OF_HARMONIC_WHIMSY_FLOWER.get());
        output.accept(FRAGMENT_OF_HARMONIC_WHIMSY_FEATHER.get());
        output.accept(FRAGMENT_OF_HARMONIC_WHIMSY_SANDS.get());
        output.accept(FRAGMENT_OF_HARMONIC_WHIMSY_GOBLET.get());
        output.accept(FRAGMENT_OF_HARMONIC_WHIMSY_CIRCLET.get());
        output.accept(NIGHT_OF_THE_SKYS_UNVEILING_FLOWER.get());
        output.accept(NIGHT_OF_THE_SKYS_UNVEILING_FEATHER.get());
        output.accept(NIGHT_OF_THE_SKYS_UNVEILING_SANDS.get());
        output.accept(NIGHT_OF_THE_SKYS_UNVEILING_GOBLET.get());
        output.accept(NIGHT_OF_THE_SKYS_UNVEILING_CIRCLET.get());
        output.accept(CELESTIAL_GIFT_FLOWER.get());
        output.accept(CELESTIAL_GIFT_FEATHER.get());
        output.accept(CELESTIAL_GIFT_SANDS.get());
        output.accept(CELESTIAL_GIFT_GOBLET.get());
        output.accept(CELESTIAL_GIFT_CIRCLET.get());
        output.accept(SCARLET_PROOF_FLOWER.get());
        output.accept(SCARLET_PROOF_FEATHER.get());
        output.accept(SCARLET_PROOF_SANDS.get());
        output.accept(SCARLET_PROOF_GOBLET.get());
        output.accept(SCARLET_PROOF_CIRCLET.get());
        output.accept(HEART_OF_DEPTH_FLOWER.get());
        output.accept(HEART_OF_DEPTH_FEATHER.get());
        output.accept(HEART_OF_DEPTH_SANDS.get());
        output.accept(HEART_OF_DEPTH_GOBLET.get());
        output.accept(HEART_OF_DEPTH_CIRCLET.get());
    }
}
