package com.guoche.teyvat_artifacts;

import dev.shadowsoffire.apothic_attributes.api.ALObjects;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.entity.player.SweepAttackEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioCanEquipEvent;
import top.theillusivec4.curios.api.event.CurioChangeEvent;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import static com.guoche.teyvat_artifacts.ArtifactSetConfig.Value.*;
import static com.guoche.teyvat_artifacts.ArtifactSetConfig.*;

public final class ArtifactEvents {
    private static final int LOOTR_NOT_PRESENT = -1;
    private static final int LOOTR_ALREADY_OPENED = 0;
    private static final int LOOTR_UNOPENED = 1;
    private static final int VIRIDESCENT_SWEEP_WINDOW_TICKS = 5;
    private static final int LONG_NIGHT_UNDEAD_TICKS = 10 * 20;
    private static final double CHARGED_ARROW_LIGHTNING_RADIUS = 3.0D;
    private static final double GOLDEN_TROUPE_AGGRO_RADIUS = 12.0D;
    private static final double CINDER_CITY_PARTY_DAMAGE_RADIUS = 16.0D;
    private static final float CHARGED_ARROW_LIGHTNING_DAMAGE = 5.0F;
    private static final TagKey<Item> SHIELD_ITEM_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "tools/shield"));
    private static final String STARTER_ARTIFACTS_GRANTED_KEY = TeyvatArtifacts.MODID + ".starter_artifacts_granted";
    private static final String WITHER_WANDERERS_TROUPE_REWARD_KEY = TeyvatArtifacts.MODID + ".wither_wanderers_troupe_reward";
    private static final String ENDER_DRAGON_GLADIATORS_FINALE_REWARD_KEY = TeyvatArtifacts.MODID + ".ender_dragon_gladiators_finale_reward";
    private static final String MIRACLE_BOOST_TICKS_KEY = TeyvatArtifacts.MODID + ".miracle_boost_ticks";
    private static final String MIRACLE_COOLDOWN_TICKS_KEY = TeyvatArtifacts.MODID + ".miracle_cooldown_ticks";
    private static final String INSTRUCTOR_ARMOR_IGNORE_TICKS_KEY = TeyvatArtifacts.MODID + ".instructor_armor_ignore_ticks";
    private static final String EXILE_ATTACK_SPEED_TICKS_KEY = TeyvatArtifacts.MODID + ".exile_attack_speed_ticks";
    private static final String MARTIAL_DAMAGE_TICKS_KEY = TeyvatArtifacts.MODID + ".martial_damage_ticks";
    private static final String GAMBLER_PROJECTILE_CHARGE_KEY = TeyvatArtifacts.MODID + ".gambler_projectile_charge";
    private static final String GAMBLER_COOLDOWN_TICKS_KEY = TeyvatArtifacts.MODID + ".gambler_cooldown_ticks";
    private static final String THUNDERSOOTHER_DAMAGE_TICKS_KEY = TeyvatArtifacts.MODID + ".thundersoother_damage_ticks";
    private static final String THUNDERSOOTHER_DAMAGE_STACKS_KEY = TeyvatArtifacts.MODID + ".thundersoother_damage_stacks";
    private static final String LAVAWALKER_DAMAGE_TICKS_KEY = TeyvatArtifacts.MODID + ".lavawalker_damage_ticks";
    private static final String LAVAWALKER_DAMAGE_STACKS_KEY = TeyvatArtifacts.MODID + ".lavawalker_damage_stacks";
    private static final String NOBLESSE_DAMAGE_TICKS_KEY = TeyvatArtifacts.MODID + ".noblesse_damage_ticks";
    private static final String BLOODSTAINED_DAMAGE_TICKS_KEY = TeyvatArtifacts.MODID + ".bloodstained_damage_ticks";
    private static final String MAIDEN_HEAL_TICKS_KEY = TeyvatArtifacts.MODID + ".maiden_heal_ticks";
    private static final String VIRIDESCENT_ATTACK_SPEED_TICKS_KEY = TeyvatArtifacts.MODID + ".viridescent_attack_speed_ticks";
    private static final String VIRIDESCENT_SWEEP_TICKS_KEY = TeyvatArtifacts.MODID + ".viridescent_sweep_ticks";
    private static final String VIRIDESCENT_SWEEP_PRIMARY_KEY = TeyvatArtifacts.MODID + ".viridescent_sweep_primary";
    private static final String ARCHAIC_PETRA_DAMAGE_TICKS_KEY = TeyvatArtifacts.MODID + ".archaic_petra_damage_ticks";
    private static final String PALE_FLAME_TICKS_KEY = TeyvatArtifacts.MODID + ".pale_flame_ticks";
    private static final String PALE_FLAME_STACKS_KEY = TeyvatArtifacts.MODID + ".pale_flame_stacks";
    private static final String SHIMENAWA_DAMAGE_TICKS_KEY = TeyvatArtifacts.MODID + ".shimenawa_damage_ticks";
    private static final String HUSK_STACKS_KEY = TeyvatArtifacts.MODID + ".husk_stacks";
    private static final String HUSK_STACK_COOLDOWN_TICKS_KEY = TeyvatArtifacts.MODID + ".husk_stack_cooldown_ticks";
    private static final String HUSK_STACK_DECAY_TICKS_KEY = TeyvatArtifacts.MODID + ".husk_stack_decay_ticks";
    private static final String OCEAN_RECORD_TICKS_KEY = TeyvatArtifacts.MODID + ".ocean_record_ticks";
    private static final String OCEAN_HEALING_RECORDED_KEY = TeyvatArtifacts.MODID + ".ocean_healing_recorded";
    private static final String OCEAN_DAMAGE_TICKS_KEY = TeyvatArtifacts.MODID + ".ocean_damage_ticks";
    private static final String OCEAN_DAMAGE_BONUS_KEY = TeyvatArtifacts.MODID + ".ocean_damage_bonus";
    private static final String OCEAN_REGEN_COOLDOWN_TICKS_KEY = TeyvatArtifacts.MODID + ".ocean_regen_cooldown_ticks";
    private static final String VERMILLION_CRIT_TICKS_KEY = TeyvatArtifacts.MODID + ".vermillion_crit_ticks";
    private static final String VERMILLION_DAMAGE_TICKS_KEY = TeyvatArtifacts.MODID + ".vermillion_damage_ticks";
    private static final String VERMILLION_DAMAGE_STACKS_KEY = TeyvatArtifacts.MODID + ".vermillion_damage_stacks";
    private static final String ECHOES_FAILURES_KEY = TeyvatArtifacts.MODID + ".echoes_failures";
    private static final String DESERT_DAMAGE_TICKS_KEY = TeyvatArtifacts.MODID + ".desert_damage_ticks";
    private static final String HEART_OF_DEPTH_CRIT_TICKS_KEY = TeyvatArtifacts.MODID + ".heart_of_depth_crit_ticks";
    private static final String NYMPH_MELEE_TICKS_KEY = TeyvatArtifacts.MODID + ".nymph_melee_ticks";
    private static final String NYMPH_CRIT_TICKS_KEY = TeyvatArtifacts.MODID + ".nymph_crit_ticks";
    private static final String NYMPH_PROJECTILE_TICKS_KEY = TeyvatArtifacts.MODID + ".nymph_projectile_ticks";
    private static final String NYMPH_SPELL_TICKS_KEY = TeyvatArtifacts.MODID + ".nymph_spell_ticks";
    private static final String VOURUKASHA_STACK_KEY_PREFIX = TeyvatArtifacts.MODID + ".vourukasha_stack_";
    private static final String MARECHAUSSEE_TICKS_KEY = TeyvatArtifacts.MODID + ".marechaussee_ticks";
    private static final String MARECHAUSSEE_STACKS_KEY = TeyvatArtifacts.MODID + ".marechaussee_stacks";
    private static final String SONG_TICKS_KEY = TeyvatArtifacts.MODID + ".song_ticks";
    private static final String SONG_STACKS_KEY = TeyvatArtifacts.MODID + ".song_stacks";
    private static final Map<DamageSource, LivingEntity> APOTHIC_CRITICALS = new WeakHashMap<>();
    private static final Map<Player, VanillaCritical> VANILLA_CRITICALS = new WeakHashMap<>();
    private record VanillaCritical(int targetId, int tick) {}
    private static final String NIGHTTIME_WHISPERS_DAMAGE_TICKS_KEY = TeyvatArtifacts.MODID + ".nighttime_whispers_damage_ticks";
    private static final String UNFINISHED_REVERIE_DISABLED_TICKS_KEY = TeyvatArtifacts.MODID + ".unfinished_reverie_disabled_ticks";
    private static final String CINDER_CITY_TOTEM_COOLDOWN_TICKS_KEY = TeyvatArtifacts.MODID + ".cinder_city_totem_cooldown_ticks";
    private static final String CINDER_CITY_PARTY_DAMAGE_TICKS_KEY = TeyvatArtifacts.MODID + ".cinder_city_party_damage_ticks";
    private static final String LONG_NIGHT_UNDEAD_TICKS_KEY = TeyvatArtifacts.MODID + ".long_night_undead_ticks";
    private static final String DEEP_GALLERIES_DAMAGE_COUNT_KEY = TeyvatArtifacts.MODID + ".deep_galleries_damage_count";
    private static final String DEEP_GALLERIES_FORCED_CRIT_KEY = TeyvatArtifacts.MODID + ".deep_galleries_forced_crit";
    private static final String SILKEN_MOON_TICKS_KEY = TeyvatArtifacts.MODID + ".silken_moon_ticks";
    private static final String SILKEN_MOON_STACKS_KEY = TeyvatArtifacts.MODID + ".silken_moon_stacks";
    private static final String RISING_WINDS_DAMAGE_TICKS_KEY = TeyvatArtifacts.MODID + ".rising_winds_damage_ticks";
    private static final String DISENCHANTMENT_NONCRIT_TICKS_KEY = TeyvatArtifacts.MODID + ".disenchantment_noncrit_ticks";
    private static final String DISENCHANTMENT_NONCRIT_STACKS_KEY = TeyvatArtifacts.MODID + ".disenchantment_noncrit_stacks";
    private static final String SCARLET_NO_HEAL_UNTIL_KEY = TeyvatArtifacts.MODID + ".scarlet_no_heal_until";
    private static final ThreadLocal<Boolean> UPGRADING_EFFECT = ThreadLocal.withInitial(() -> false);

    private static final ResourceLocation ADVENTURER_TWO_PIECE_MAX_HEALTH =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "adventurer_two_piece_max_health");
    private static final ResourceLocation INSTRUCTOR_TWO_PIECE_ARMOR_IGNORE =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "instructor_two_piece_armor_ignore");
    private static final ResourceLocation INSTRUCTOR_FOUR_PIECE_ARMOR_IGNORE =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "instructor_four_piece_armor_ignore");
    private static final ResourceLocation EXILE_TWO_PIECE_ATTACK_SPEED =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "exile_two_piece_attack_speed");
    private static final ResourceLocation EXILE_FOUR_PIECE_ATTACK_SPEED =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "exile_four_piece_attack_speed");
    private static final ResourceLocation SCHOLAR_TWO_PIECE_COOLDOWN_REDUCTION =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "scholar_two_piece_cooldown_reduction");
    private static final ResourceLocation SCHOLAR_TWO_PIECE_CAST_TIME_REDUCTION =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "scholar_two_piece_cast_time_reduction");
    private static final ResourceLocation SCHOLAR_FOUR_PIECE_MAX_MANA =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "scholar_four_piece_max_mana");
    private static final ResourceLocation DEFENDERS_FOUR_PIECE_KNOCKBACK_RESISTANCE =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "defenders_four_piece_knockback_resistance");
    private static final ResourceLocation VIRIDESCENT_TWO_PIECE_MOVEMENT_SPEED =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "viridescent_two_piece_movement_speed");
    private static final ResourceLocation VIRIDESCENT_FOUR_PIECE_ATTACK_SPEED =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "viridescent_four_piece_attack_speed");
    private static final ResourceLocation RETRACING_BOLIDE_TWO_PIECE_ARMOR =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "retracing_bolide_two_piece_armor");
    private static final ResourceLocation TENACITY_TWO_PIECE_MAX_HEALTH =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "tenacity_two_piece_max_health");
    private static final ResourceLocation TENACITY_FOUR_PIECE_MAX_HEALTH =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "tenacity_four_piece_max_health");
    private static final ResourceLocation EMBLEM_TWO_PIECE_ATTACK_SPEED =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "emblem_two_piece_attack_speed");
    private static final ResourceLocation HUSK_TWO_PIECE_ARMOR =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "husk_two_piece_armor");
    private static final ResourceLocation HUSK_STACK_ARMOR =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "husk_stack_armor");
    private static final ResourceLocation DESERT_PAVILION_ATTACK_SPEED =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "desert_pavilion_attack_speed");
    private static final ResourceLocation VOURUKASHA_TWO_PIECE_MAX_HEALTH =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "vourukasha_two_piece_max_health");
    private static final ResourceLocation CINDER_CITY_MAX_HEALTH =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "cinder_city_max_health");
    private static final ResourceLocation SILKEN_MOON_ATTACK_SPEED =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "silken_moon_attack_speed");
    private static final ResourceLocation RISING_WINDS_ATTACK_SPEED =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "rising_winds_attack_speed");
    private static final ResourceLocation LITTLE_WITCH_DICTIONARY_MAX_HEALTH =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "little_witch_dictionary_max_health");
    private static final ResourceLocation LITTLE_WITCH_DICTIONARY_MOVEMENT_SPEED =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "little_witch_dictionary_movement_speed");
    private static final ResourceLocation LITTLE_WITCH_DICTIONARY_DAMAGE_BONUS =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "little_witch_dictionary_damage_bonus");
    private static final ResourceLocation DISPLAY_DAMAGE_BONUS =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "display_damage_bonus");
    private static final ResourceLocation SET_CRIT_CHANCE =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "set_crit_chance");
    private static final ResourceLocation SET_CRIT_DAMAGE =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "set_crit_damage");
    private static final ResourceLocation LITTLE_WITCH_DICTIONARY_MAX_MANA =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "little_witch_dictionary_max_mana");
    private static final ResourceLocation LITTLE_WITCH_DICTIONARY_COOLDOWN_REDUCTION =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "little_witch_dictionary_cooldown_reduction");
    private static final ResourceLocation LITTLE_WITCH_DICTIONARY_CAST_TIME_REDUCTION =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "little_witch_dictionary_cast_time_reduction");
    private static final ResourceLocation LITTLE_WITCH_DICTIONARY_MANA_REGEN =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "little_witch_dictionary_mana_regen");
    private static final ResourceLocation IRON_MAX_MANA_ATTRIBUTE =
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "max_mana");
    private static final ResourceLocation IRON_MANA_REGEN_ATTRIBUTE =
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "mana_regen");
    private static final ResourceLocation IRON_COOLDOWN_REDUCTION_ATTRIBUTE =
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "cooldown_reduction");
    private static final ResourceLocation IRON_CAST_TIME_REDUCTION_ATTRIBUTE =
            ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "cast_time_reduction");
    private static final ResourceLocation IRON_SPELLBOOKS_SET_COOLDOWN_REDUCTION =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "iron_spellbooks_set_cooldown_reduction");

    private ArtifactEvents() {
    }

    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(TeyvatArtifacts.ANEMO_CRYSTALFLY.get(), Crystalfly.createCrystalflyAttributes().build());
        event.put(TeyvatArtifacts.ICE_CRYSTALFLY.get(), Crystalfly.createCrystalflyAttributes().build());
        event.put(TeyvatArtifacts.GEO_CRYSTALFLY.get(), Crystalfly.createCrystalflyAttributes().build());
        event.put(TeyvatArtifacts.ELECTRO_CRYSTALFLY.get(), Crystalfly.createCrystalflyAttributes().build());
        event.put(TeyvatArtifacts.DENDRO_CRYSTALFLY.get(), Crystalfly.createCrystalflyAttributes().build());
        event.put(TeyvatArtifacts.HYDRO_CRYSTALFLY.get(), Crystalfly.createCrystalflyAttributes().build());
        event.put(TeyvatArtifacts.PYRO_CRYSTALFLY.get(), Crystalfly.createCrystalflyAttributes().build());
    }

    public static void onRegisterSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        registerCrystalflySpawnPlacement(event, TeyvatArtifacts.ANEMO_CRYSTALFLY.get());
        registerCrystalflySpawnPlacement(event, TeyvatArtifacts.ICE_CRYSTALFLY.get());
        registerCrystalflySpawnPlacement(event, TeyvatArtifacts.GEO_CRYSTALFLY.get());
        registerCrystalflySpawnPlacement(event, TeyvatArtifacts.ELECTRO_CRYSTALFLY.get());
        registerCrystalflySpawnPlacement(event, TeyvatArtifacts.DENDRO_CRYSTALFLY.get());
        registerCrystalflySpawnPlacement(event, TeyvatArtifacts.HYDRO_CRYSTALFLY.get());
        registerCrystalflySpawnPlacement(event, TeyvatArtifacts.PYRO_CRYSTALFLY.get());
    }

    private static void registerCrystalflySpawnPlacement(RegisterSpawnPlacementsEvent event, EntityType<Crystalfly> entityType) {
        event.register(
                entityType,
                SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Crystalfly::checkCrystalflySpawnRules,
                RegisterSpawnPlacementsEvent.Operation.AND
        );
    }

    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, TeyvatArtifacts.DAMAGE_BONUS);
        event.add(EntityType.PLAYER, TeyvatArtifacts.DISPLAY_DAMAGE_BONUS);
        event.add(EntityType.PLAYER, TeyvatArtifacts.DAMAGE_REDUCTION);
        event.add(EntityType.PLAYER, TeyvatArtifacts.ARMOR_IGNORE);
    }

    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof BlockItem blockItem) {
            event.getToolTip().addAll(ArtifactSourceInfo.leylineRewards(blockItem.getBlock()));
        }
        if (!(stack.getItem() instanceof ArtifactItem artifact)) {
            return;
        }

        Player player = event.getEntity();
        int pieces = countEquippedPiecesNow(player, artifact.getSet());
        List<Component> tooltip = event.getToolTip();

        ArtifactItemData.appendRarityTooltip(stack, tooltip);
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable(artifact.getSet().nameKey()).withStyle(ChatFormatting.GOLD));
        if (artifact.getSet().hasOnePiece()) {
            tooltip.add(Component.translatable(artifact.getSet().onePieceKey()).withStyle(pieces >= 1 ? ChatFormatting.GREEN : ChatFormatting.DARK_GRAY));
        }
        if (artifact.getSet().hasTwoPiece()) {
            tooltip.add(Component.translatable(
                    artifact.getSet().twoPieceKey(),
                    ArtifactSetConfig.tooltipArguments(artifact.getSet(), 2)
            ).withStyle(pieces >= 2 ? ChatFormatting.GREEN : ChatFormatting.DARK_GRAY));
        }
        if (artifact.getSet().hasFourPiece()) {
            tooltip.add(Component.translatable(
                    artifact.getSet().fourPieceKey(),
                    ArtifactSetConfig.tooltipArguments(artifact.getSet(), 4)
            ).withStyle(pieces >= 4 ? ChatFormatting.GREEN : ChatFormatting.DARK_GRAY));
        }
    }

    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        giveStarterArtifacts(event.getEntity());
        if (event.getEntity() instanceof ServerPlayer player) {
            WitchGiftProgress.onLogin(player);
        }
        syncSetBonuses(event.getEntity());
    }

    public static void onPlayerClone(PlayerEvent.Clone event) {
        CompoundTag originalData = getPersistedData(event.getOriginal());
        if (originalData.contains(STARTER_ARTIFACTS_GRANTED_KEY, Tag.TAG_BYTE)) {
            getPersistedData(event.getEntity()).putBoolean(STARTER_ARTIFACTS_GRANTED_KEY, originalData.getBoolean(STARTER_ARTIFACTS_GRANTED_KEY));
        }
        WitchGiftProgress.copyPersistentData(event.getOriginal(), event.getEntity());
        if (event.getEntity() instanceof ServerPlayer player) {
            WitchGiftProgress.onLogin(player);
        }
        syncSetBonuses(event.getEntity());
    }

    public static void onAdvancementEarned(AdvancementEvent.AdvancementEarnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            WitchGiftProgress.onAdvancementEarned(player, event.getAdvancement().id());
            syncSetBonuses(player);
        }
    }

    public static void onCurioCanEquip(CurioCanEquipEvent event) {
        ItemStack stack = event.getStack();
        if ((stack.getItem() instanceof ArtifactItem artifact && !artifact.canEquip(event.getSlotContext(), stack))
                || (stack.getItem() instanceof WitchGiftItem gift && !gift.canEquip(event.getSlotContext(), stack))) {
            event.setEquipResult(TriState.FALSE);
        }
    }

    public static void onCurioChange(CurioChangeEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (player instanceof ServerPlayer serverPlayer) {
                ArtifactAdvancementTracker.check(serverPlayer);
            }
            syncSetBonuses(player);
        }
    }

    public static void onPlayerTickPost(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }

        tickSetBonusTimers(player);
        if (player instanceof ServerPlayer serverPlayer && player.tickCount % 20 == 0) {
            WitchGiftProgress.onPlayerTick(serverPlayer);
            ArtifactAdvancementTracker.check(serverPlayer);
        }
        applyTickingSetEffects(player, countEquippedSets(player));
        syncSetBonuses(player);
    }

    public static void onMobEffectAdded(MobEffectEvent.Added event) {
        if (UPGRADING_EFFECT.get() || !(event.getEntity() instanceof Player player) || player.level().isClientSide) {
            return;
        }

        MobEffectInstance effect = event.getEffectInstance();
        EnumMap<ArtifactSet, Integer> counts = countEquippedSets(player);
        int amplifierBonus = 0;
        if (isHealingEffect(effect)) {
            if (hasSet(counts, ArtifactSet.TRAVELING_DOCTOR, 2)) {
                amplifierBonus += integer(TRAVELING_DOCTOR_2_HEALING_LEVEL_BONUS);
            }
            if (hasSet(counts, ArtifactSet.MAIDEN_BELOVED, 2)) {
                amplifierBonus += integer(MAIDEN_2_HEALING_LEVEL_BONUS);
            }
            if (hasSet(counts, ArtifactSet.OCEAN_HUED_CLAM, 2)) {
                amplifierBonus += integer(OCEAN_CLAM_2_HEALING_LEVEL_BONUS);
            }
            if (hasSet(counts, ArtifactSet.SONG_OF_DAYS_PAST, 2)) {
                amplifierBonus += integer(SONG_2_HEALING_LEVEL_BONUS);
            }
        }
        if (isResistanceEffect(effect) && hasSet(counts, ArtifactSet.RETRACING_BOLIDE, 2)) {
            amplifierBonus += integer(RETRACING_BOLIDE_2_RESISTANCE_LEVEL_BONUS);
        }
        if (amplifierBonus <= 0) {
            return;
        }

        UPGRADING_EFFECT.set(true);
        try {
            player.removeEffect(effect.getEffect());
            player.addEffect(new MobEffectInstance(
                    effect.getEffect(),
                    effect.getDuration(),
                    effect.getAmplifier() + amplifierBonus,
                    effect.isAmbient(),
                    effect.isVisible(),
                    effect.showIcon()
            ));
        } finally {
            UPGRADING_EFFECT.set(false);
        }
    }

    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (event.getHand() != InteractionHand.MAIN_HAND || player.level().isClientSide) {
            return;
        }

        if (countEquippedPieces(player, ArtifactSet.ADVENTURER) < 4) {
            return;
        }

        BlockEntity blockEntity = event.getLevel().getBlockEntity(event.getPos());
        int lootrState = getLootrOpenState(blockEntity, player);
        if (lootrState == LOOTR_ALREADY_OPENED) {
            return;
        }

        if (lootrState == LOOTR_UNOPENED || (lootrState == LOOTR_NOT_PRESENT && isUnopenedVanillaLootContainer(blockEntity))) {
            player.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, effectAmplifier(ADVENTURER_4_INSTANT_HEAL_LEVEL)));
        }
    }

    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) {
            return;
        }

        if (isScarletHealingBlocked(entity)) {
            event.setAmount(0.0F);
            return;
        }

        if (!(entity instanceof Player player)) {
            return;
        }

        if (event.getAmount() <= 0.0F) {
            return;
        }

        EnumMap<ArtifactSet, Integer> counts = countEquippedSets(player);
        float amount = event.getAmount();
        if (hasSet(counts, ArtifactSet.MAIDEN_BELOVED, 4)) {
            float extraHeal = (float) number(MAIDEN_4_EXTRA_HEAL);
            if (player.getHealth() < player.getMaxHealth() * percent(MAIDEN_4_LOW_HEALTH_THRESHOLD)) {
                extraHeal *= (float) number(MAIDEN_4_LOW_HEALTH_MULTIPLIER);
            }
            if (hasTimer(player, MAIDEN_HEAL_TICKS_KEY)) {
                extraHeal *= (float) number(MAIDEN_4_CRIT_MULTIPLIER);
            }
            amount += extraHeal;
        }

        float effectiveHeal = Math.min(amount, Math.max(0.0F, player.getMaxHealth() - player.getHealth()));
        if (effectiveHeal > 0.0F) {
            if (hasSet(counts, ArtifactSet.OCEAN_HUED_CLAM, 4) && hasTimer(player, OCEAN_RECORD_TICKS_KEY)) {
                addOceanHealing(player, effectiveHeal);
            }
            if (hasSet(counts, ArtifactSet.MARECHAUSSEE_HUNTER, 4)) {
                addTimedStack(player, MARECHAUSSEE_TICKS_KEY, MARECHAUSSEE_STACKS_KEY, ticks(MARECHAUSSEE_4_DURATION), integer(MARECHAUSSEE_4_MAX_STACKS));
            }
            if (hasSet(counts, ArtifactSet.SONG_OF_DAYS_PAST, 4)) {
                addTimedStack(player, SONG_TICKS_KEY, SONG_STACKS_KEY, ticks(SONG_4_DURATION), integer(SONG_4_MAX_STACKS));
            }
        }

        event.setAmount(amount);
    }

    public static void onLivingItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) {
            return;
        }

        if (event.getItem().has(DataComponents.FOOD)) {
            getPersistedData(player).remove(UNFINISHED_REVERIE_DISABLED_TICKS_KEY);
        }
    }

    public static void onLivingShieldBlock(LivingShieldBlockEvent event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) {
            return;
        }

        if (event.getBlocked() && event.getBlockedDamage() > 0.0F && countEquippedPieces(player, ArtifactSet.ARCHAIC_PETRA) >= 4) {
            setTimer(player, ARCHAIC_PETRA_DAMAGE_TICKS_KEY, ticks(ARCHAIC_PETRA_4_DURATION));
        }
    }

    public static void onSweepAttack(SweepAttackEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide || !event.isSweeping()) {
            return;
        }

        if (!(event.getTarget() instanceof LivingEntity target) || countEquippedPieces(player, ArtifactSet.VIRIDESCENT_VENERER) < 4) {
            return;
        }

        setTimer(player, VIRIDESCENT_ATTACK_SPEED_TICKS_KEY, ticks(VIRIDESCENT_4_DURATION));
        markViridescentSweep(player, target);
    }

    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }

        float damage = ArtifactDamageMath.applyArmorIgnore(
                event.getSource(),
                event.getEntity(),
                event.getNewDamage(),
                event.getOriginalDamage(),
                getSetArmorIgnoreBonus(event.getSource(), event.getEntity())
        );
        if (event.getEntity() instanceof Player player) {
            damage = applyIncomingSetDamage(event.getSource(), player, damage);
            damage = ArtifactDamageMath.applyIncomingDamageReduction(player, damage);
        }
        damage = ArtifactDamageMath.applyOutgoingDamageMultiplier(event.getSource(), damage);
        damage = applyOutgoingSetDamage(event.getSource(), event.getEntity(), damage);
        if (event.getEntity() instanceof Player player) {
            damage = applyCinderCityTotemProtection(player, damage);
            if (damage > 0.0F) {
                triggerDamageTakenSetBonuses(player);
            }
        }
        event.setNewDamage(damage);
    }

    public static void onLivingDrops(LivingDropsEvent event) {
        if (LeylineTrialDrops.shouldSuppressDrops(event.getEntity())) {
            event.getDrops().clear();
        }
    }

    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (LeylineTrialDrops.isTrialMob(event.getEntity()) && event.getSource().is(DamageTypeTags.IS_FALL)) {
            event.setCanceled(true);
        }
    }

    public static void onLivingExperienceDrop(LivingExperienceDropEvent event) {
        if (LeylineTrialDrops.shouldSuppressDrops(event.getEntity())) {
            event.setDroppedExperience(0);
        }
    }
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide) {
            return;
        }

        if (target instanceof ServerPlayer deadPlayer) {
            LeylineTrialSessions.participantDied(deadPlayer);
        }

        Player player = ArtifactDamageMath.resolvePlayer(event.getSource());
        if (player == null) {
            return;
        }

        tryDropFirstBossArtifactSet(target, player);

        if (countEquippedPieces(player, ArtifactSet.BLOODSTAINED_CHIVALRY) >= 4) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, ticks(BLOODSTAINED_4_DURATION), effectAmplifier(BLOODSTAINED_4_REGENERATION_LEVEL)));
            setTimer(player, BLOODSTAINED_DAMAGE_TICKS_KEY, ticks(BLOODSTAINED_4_DURATION));
        }

        if (!ArtifactDamageMath.isProjectileDamage(event.getSource()) || countEquippedPieces(player, ArtifactSet.GAMBLER) < 4) {
            return;
        }

        CompoundTag data = getPersistedData(player);
        if (data.getInt(GAMBLER_COOLDOWN_TICKS_KEY) > 0) {
            return;
        }

        data.putBoolean(GAMBLER_PROJECTILE_CHARGE_KEY, true);
        data.putInt(GAMBLER_COOLDOWN_TICKS_KEY, ticks(GAMBLER_4_COOLDOWN));
    }

    private static void tryDropFirstBossArtifactSet(LivingEntity target, Player player) {
        if (target instanceof WitherBoss) {
            dropFirstBossArtifactSet(target, player, WITHER_WANDERERS_TROUPE_REWARD_KEY, ArtifactSet.WANDERERS_TROUPE);
        } else if (target instanceof EnderDragon) {
            dropFirstBossArtifactSet(target, player, ENDER_DRAGON_GLADIATORS_FINALE_REWARD_KEY, ArtifactSet.GLADIATORS_FINALE);
        }
    }

    private static void dropFirstBossArtifactSet(LivingEntity target, Player player, String rewardKey, ArtifactSet set) {
        CompoundTag data = getPersistedData(player);
        if (data.getBoolean(rewardKey)) {
            return;
        }

        data.putBoolean(rewardKey, true);
        for (ArtifactSlot slot : ArtifactSlot.values()) {
            ItemStack stack = ArtifactChestLootModifier.createRolledStack(set, slot, player.getRandom(), 5);
            if (!stack.isEmpty()) {
                target.spawnAtLocation(stack);
            }
        }
    }

    public static void onPlayerXpPickup(PlayerXpEvent.PickupXp event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) {
            return;
        }

        if (countEquippedPieces(player, ArtifactSet.LUCKY) >= 4) {
            player.heal((float) number(LUCKY_4_HEAL_PER_ORB));
        }
    }

    public static void onCriticalHit(CriticalHitEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide || !event.isVanillaCritical()) {
            return;
        }
        LivingEntity target = event.getTarget() instanceof LivingEntity livingEntity ? livingEntity : null;
        if (target != null) {
            VANILLA_CRITICALS.put(player, new VanillaCritical(target.getId(), player.tickCount));
        }
        triggerCriticalSetBonuses(player, countEquippedSets(player));
        syncSetBonuses(player);
    }

    public static double adjustApothicCritChance(DamageSource source, LivingEntity target, double chance) {
        Player player = ArtifactDamageMath.resolvePlayer(source);
        if (player == null || player.level().isClientSide) {
            return chance;
        }
        EnumMap<ArtifactSet, Integer> counts = countEquippedSets(player);
        chance += getSetCritRate(player, counts, target) - getSyncedCritBonus(player, ALObjects.Attributes.CRIT_CHANCE, SET_CRIT_CHANCE);
        if (hasSet(counts, ArtifactSet.RESOLUTION_OF_SOJOURNER, 4)
                && ((new DamageKindContext(source, player).meleeDamage() && player.getAttackStrengthScale(0.5F) >= 1.0F)
                    || isFullyChargedArrow(source))) {
            chance += percent(RESOLUTION_4_CRIT_RATE);
        }
        if (hasSet(counts, ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, 4)
                && getPersistedData(player).getBoolean(DEEP_GALLERIES_FORCED_CRIT_KEY)) {
            chance = Math.max(1.0D, chance);
        }
        return chance;
    }

    public static float adjustApothicCritDamage(DamageSource source, float damage) {
        Player player = ArtifactDamageMath.resolvePlayer(source);
        if (player == null || player.level().isClientSide) {
            return damage;
        }
        EnumMap<ArtifactSet, Integer> counts = countEquippedSets(player);
        return (float) (damage + getSetCritDamage(player, counts)
                - getSyncedCritBonus(player, ALObjects.Attributes.CRIT_DAMAGE, SET_CRIT_DAMAGE));
    }

    private static double getSyncedCritBonus(Player player, Holder<Attribute> attribute, ResourceLocation modifierId) {
        AttributeInstance instance = player.getAttribute(attribute);
        AttributeModifier modifier = instance == null ? null : instance.getModifier(modifierId);
        return modifier == null ? 0.0D : modifier.amount();
    }

    public static void onApothicCritical(DamageSource source, LivingEntity target) {
        Player player = ArtifactDamageMath.resolvePlayer(source);
        if (player == null || player.level().isClientSide) {
            return;
        }
        APOTHIC_CRITICALS.put(source, target);
        EnumMap<ArtifactSet, Integer> counts = countEquippedSets(player);
        consumeDeepGalleriesForcedCritical(player, counts);
        if (!isVanillaCritical(source, player, target)) {
            triggerCriticalSetBonuses(player, counts);
        }
    }

    private static boolean isVanillaCritical(DamageSource source, Player player, LivingEntity target) {
        VanillaCritical pending = VANILLA_CRITICALS.get(player);
        return source.is(DamageTypes.PLAYER_ATTACK) && pending != null
                && pending.tick() == player.tickCount && pending.targetId() == target.getId();
    }

    private static boolean consumeCriticalDamage(DamageSource source, Player player, LivingEntity target) {
        boolean apothicCritical = APOTHIC_CRITICALS.remove(source) == target;
        boolean vanillaCritical = isVanillaCritical(source, player, target);
        if (vanillaCritical) {
            VANILLA_CRITICALS.remove(player);
        }
        return apothicCritical || vanillaCritical;
    }

    private static void syncSetBonuses(Player player) {
        EnumMap<ArtifactSet, Integer> counts = countEquippedSets(player);
        syncAdventurerTwoPiece(player, counts);
        syncModifier(player, TeyvatArtifacts.ARMOR_IGNORE, INSTRUCTOR_TWO_PIECE_ARMOR_IGNORE, percent(INSTRUCTOR_2_ARMOR_IGNORE), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasSet(counts, ArtifactSet.INSTRUCTOR, 2));
        syncModifier(player, TeyvatArtifacts.ARMOR_IGNORE, INSTRUCTOR_FOUR_PIECE_ARMOR_IGNORE, percent(INSTRUCTOR_4_ARMOR_IGNORE), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasSet(counts, ArtifactSet.INSTRUCTOR, 4) && hasTimer(player, INSTRUCTOR_ARMOR_IGNORE_TICKS_KEY));
        syncModifier(player, Attributes.ATTACK_SPEED, EXILE_TWO_PIECE_ATTACK_SPEED, percent(EXILE_2_ATTACK_SPEED), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasSet(counts, ArtifactSet.THE_EXILE, 2));
        syncModifier(player, Attributes.ATTACK_SPEED, EXILE_FOUR_PIECE_ATTACK_SPEED, percent(EXILE_4_ATTACK_SPEED), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasSet(counts, ArtifactSet.THE_EXILE, 4) && hasTimer(player, EXILE_ATTACK_SPEED_TICKS_KEY));
        syncScholarBonuses(player, counts);
        syncModifier(player, Attributes.KNOCKBACK_RESISTANCE, DEFENDERS_FOUR_PIECE_KNOCKBACK_RESISTANCE, percent(DEFENDERS_WILL_4_KNOCKBACK_RESISTANCE), AttributeModifier.Operation.ADD_VALUE, hasSet(counts, ArtifactSet.DEFENDERS_WILL, 4));
        syncModifier(player, Attributes.MOVEMENT_SPEED, VIRIDESCENT_TWO_PIECE_MOVEMENT_SPEED, percent(VIRIDESCENT_2_MOVEMENT_SPEED), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasSet(counts, ArtifactSet.VIRIDESCENT_VENERER, 2));
        syncModifier(player, Attributes.ATTACK_SPEED, VIRIDESCENT_FOUR_PIECE_ATTACK_SPEED, percent(VIRIDESCENT_4_ATTACK_SPEED), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasSet(counts, ArtifactSet.VIRIDESCENT_VENERER, 4) && hasTimer(player, VIRIDESCENT_ATTACK_SPEED_TICKS_KEY));
        syncModifier(player, Attributes.ARMOR, RETRACING_BOLIDE_TWO_PIECE_ARMOR, percent(RETRACING_BOLIDE_2_ARMOR), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasSet(counts, ArtifactSet.RETRACING_BOLIDE, 2));
        syncModifier(player, Attributes.MAX_HEALTH, TENACITY_TWO_PIECE_MAX_HEALTH, number(TENACITY_2_MAX_HEALTH), AttributeModifier.Operation.ADD_VALUE, hasSet(counts, ArtifactSet.TENACITY_OF_THE_MILLELITH, 2));
        syncModifier(player, Attributes.MAX_HEALTH, TENACITY_FOUR_PIECE_MAX_HEALTH, getOnlinePlayerCount(player) * number(TENACITY_4_HEALTH_PER_PLAYER), AttributeModifier.Operation.ADD_VALUE, hasSet(counts, ArtifactSet.TENACITY_OF_THE_MILLELITH, 4));
        syncModifier(player, Attributes.ATTACK_SPEED, EMBLEM_TWO_PIECE_ATTACK_SPEED, percent(EMBLEM_2_ATTACK_SPEED), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasSet(counts, ArtifactSet.EMBLEM_OF_SEVERED_FATE, 2));
        syncModifier(player, Attributes.ARMOR, HUSK_TWO_PIECE_ARMOR, percent(HUSK_2_ARMOR), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasSet(counts, ArtifactSet.HUSK_OF_OPULENT_DREAMS, 2));
        syncModifier(player, Attributes.ARMOR, HUSK_STACK_ARMOR, getHuskStacks(player) * percent(HUSK_4_ARMOR_PER_STACK), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasSet(counts, ArtifactSet.HUSK_OF_OPULENT_DREAMS, 4) && getHuskStacks(player) > 0);
        syncModifier(player, Attributes.ATTACK_SPEED, DESERT_PAVILION_ATTACK_SPEED, percent(DESERT_4_ATTACK_SPEED), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasSet(counts, ArtifactSet.DESERT_PAVILION_CHRONICLE, 4) && hasTimer(player, DESERT_DAMAGE_TICKS_KEY));
        syncModifier(player, Attributes.MAX_HEALTH, VOURUKASHA_TWO_PIECE_MAX_HEALTH, number(VOURUKASHA_2_MAX_HEALTH), AttributeModifier.Operation.ADD_VALUE, hasSet(counts, ArtifactSet.VOURUKASHAS_GLOW, 2));
        syncModifier(player, Attributes.MAX_HEALTH, CINDER_CITY_MAX_HEALTH, -percent(CINDER_CITY_2_MAX_HEALTH_REDUCTION), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, hasSet(counts, ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, 2));
        syncModifier(player, Attributes.ATTACK_SPEED, SILKEN_MOON_ATTACK_SPEED, percent(SILKEN_MOON_2_ATTACK_SPEED), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasSet(counts, ArtifactSet.SILKEN_MOONS_SERENADE, 2));
        syncModifier(player, Attributes.ATTACK_SPEED, RISING_WINDS_ATTACK_SPEED, percent(RISING_WINDS_4_DICTIONARY_ATTACK_SPEED), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasSet(counts, ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, 4) && hasTimer(player, RISING_WINDS_DAMAGE_TICKS_KEY) && hasLittleWitchDictionary(player));
        syncLittleWitchDictionaryBonuses(player);
        syncIronSpellbookCooldownReduction(player, counts);
        double critChance = getSetCritRate(player, counts, null);
        syncModifier(player, ALObjects.Attributes.CRIT_CHANCE, SET_CRIT_CHANCE,
                critChance, AttributeModifier.Operation.ADD_VALUE, critChance != 0.0D);
        double critDamage = getSetCritDamage(player, counts);
        syncModifier(player, ALObjects.Attributes.CRIT_DAMAGE, SET_CRIT_DAMAGE,
                critDamage, AttributeModifier.Operation.ADD_VALUE, critDamage != 0.0D);
        syncDisplayDamageBonus(player, counts);
        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    private static void syncDisplayDamageBonus(Player player, EnumMap<ArtifactSet, Integer> counts) {
        float multiplier = ArtifactDamageMath.getOutgoingDamageMultiplier(player) * getDisplaySetDamageMultiplier(player, counts);
        syncDisplayDamageMultiplier(player, multiplier);
    }

    private static void syncDisplayDamageMultiplier(Player player, float multiplier) {
        syncModifier(player, TeyvatArtifacts.DISPLAY_DAMAGE_BONUS, DISPLAY_DAMAGE_BONUS,
                Math.max(0.0D, multiplier), AttributeModifier.Operation.ADD_VALUE, true);
    }

    private static float getDisplaySetDamageMultiplier(Player player, EnumMap<ArtifactSet, Integer> counts) {
        float multiplier = 1.0F;
        if (hasSet(counts, ArtifactSet.NIGHTTIME_WHISPERS, 2)) {
            multiplier *= multiplier(NIGHTTIME_WHISPERS_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.NIGHTTIME_WHISPERS, 4) && hasTimer(player, NIGHTTIME_WHISPERS_DAMAGE_TICKS_KEY)) {
            multiplier *= hasArmorOrOffhandShield(player) ? multiplier(NIGHTTIME_WHISPERS_4_ARMORED_DAMAGE) : multiplier(NIGHTTIME_WHISPERS_4_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.UNFINISHED_REVERIE, 2)) {
            multiplier *= multiplier(UNFINISHED_REVERIE_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.UNFINISHED_REVERIE, 4) && !hasTimer(player, UNFINISHED_REVERIE_DISABLED_TICKS_KEY)) {
            multiplier *= multiplier(UNFINISHED_REVERIE_4_DAMAGE);
        }
        if (hasTimer(player, CINDER_CITY_PARTY_DAMAGE_TICKS_KEY)) {
            multiplier *= multiplier(CINDER_CITY_4_PARTY_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.OBSIDIAN_CODEX, 2) && isNightOrNonOverworld(player)) {
            multiplier *= multiplier(OBSIDIAN_CODEX_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, 2)) {
            multiplier *= multiplier(DEEP_GALLERIES_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.AUBADE_OF_MORNINGSTAR_AND_MOON, 4)) {
            multiplier *= multiplier(AUBADE_4_DAMAGE);
            if (isNightOrNonOverworld(player)) {
                multiplier *= multiplier(AUBADE_4_NIGHT_DAMAGE);
            }
        }
        if (hasSet(counts, ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, 2)) {
            multiplier *= multiplier(RISING_WINDS_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, 4) && hasTimer(player, RISING_WINDS_DAMAGE_TICKS_KEY)) {
            multiplier *= multiplier(RISING_WINDS_4_DAMAGE);
            if (hasLittleWitchDictionary(player)) {
                multiplier *= multiplier(RISING_WINDS_4_DICTIONARY_DAMAGE);
            }
        }
        if (hasSet(counts, ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, 2)) {
            multiplier *= multiplier(DISENCHANTMENT_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, 4)) {
            multiplier *= 1.0F + getDisenchantmentDamageBonus(player);
        }
        if (hasSet(counts, ArtifactSet.HEART_OF_THE_FURNACE, 2)) {
            multiplier *= multiplier(FURNACE_HEART_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.HEART_OF_THE_FURNACE, 4)) {
            multiplier *= 1.0F + getCoalStacks(player) * percent(FURNACE_HEART_4_DAMAGE_PER_COAL_STACK);
        }
        if (hasSet(counts, ArtifactSet.NOBLESSE_OBLIGE, 4) && hasTimer(player, NOBLESSE_DAMAGE_TICKS_KEY)) {
            multiplier *= multiplier(NOBLESSE_4_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.BLOODSTAINED_CHIVALRY, 4) && hasTimer(player, BLOODSTAINED_DAMAGE_TICKS_KEY)) {
            multiplier *= multiplier(BLOODSTAINED_4_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.ARCHAIC_PETRA, 4) && hasTimer(player, ARCHAIC_PETRA_DAMAGE_TICKS_KEY)) {
            multiplier *= multiplier(ARCHAIC_PETRA_4_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.RETRACING_BOLIDE, 4)) {
            multiplier *= 1.0F + player.getArmorValue() * percent(RETRACING_BOLIDE_4_DAMAGE_PER_ARMOR);
        }
        if (hasSet(counts, ArtifactSet.TENACITY_OF_THE_MILLELITH, 4)) {
            multiplier *= 1.0F + getOnlinePlayerCount(player) * percent(TENACITY_4_DAMAGE_PER_PLAYER);
        }
        if (hasSet(counts, ArtifactSet.PALE_FLAME, 4)) {
            multiplier *= 1.0F + getPaleFlameStacks(player) * percent(PALE_FLAME_4_DAMAGE_PER_STACK);
        }
        if (hasSet(counts, ArtifactSet.SHIMENAWAS_REMINISCENCE, 2)) {
            multiplier *= multiplier(SHIMENAWA_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.SHIMENAWAS_REMINISCENCE, 4) && hasTimer(player, SHIMENAWA_DAMAGE_TICKS_KEY)) {
            multiplier *= multiplier(SHIMENAWA_4_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.HUSK_OF_OPULENT_DREAMS, 4)) {
            multiplier *= 1.0F + getHuskStacks(player) * percent(HUSK_4_DAMAGE_PER_STACK);
        }
        if (hasSet(counts, ArtifactSet.OCEAN_HUED_CLAM, 4) && hasTimer(player, OCEAN_DAMAGE_TICKS_KEY)) {
            multiplier *= 1.0F + getPersistedData(player).getFloat(OCEAN_DAMAGE_BONUS_KEY);
        }
        if (hasSet(counts, ArtifactSet.VERMILLION_HEREAFTER, 2)) {
            multiplier *= multiplier(VERMILLION_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.VERMILLION_HEREAFTER, 4) && hasTimer(player, VERMILLION_CRIT_TICKS_KEY)) {
            multiplier *= multiplier(VERMILLION_4_CRIT_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.VERMILLION_HEREAFTER, 4)) {
            multiplier *= 1.0F + getTimedStacks(player, VERMILLION_DAMAGE_STACKS_KEY, integer(VERMILLION_4_MAX_STACKS)) * percent(VERMILLION_4_HEALTH_LOSS_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.ECHOES_OF_AN_OFFERING, 2)) {
            multiplier *= multiplier(ECHOES_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.DESERT_PAVILION_CHRONICLE, 2)) {
            multiplier *= multiplier(DESERT_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.DESERT_PAVILION_CHRONICLE, 4) && hasTimer(player, DESERT_DAMAGE_TICKS_KEY)) {
            multiplier *= multiplier(DESERT_4_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.NYMPHS_DREAM, 2)) {
            multiplier *= multiplier(NYMPH_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.NYMPHS_DREAM, 4)) {
            multiplier *= getNymphDamageMultiplier(player);
        }
        if (hasSet(counts, ArtifactSet.SONG_OF_DAYS_PAST, 4)) {
            multiplier *= 1.0F + getTimedStacks(player, SONG_STACKS_KEY, integer(SONG_4_MAX_STACKS)) * percent(SONG_4_DAMAGE_PER_STACK);
        }
        if (hasSet(counts, ArtifactSet.RESOLUTION_OF_SOJOURNER, 2)) {
            multiplier *= multiplier(RESOLUTION_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.BRAVE_HEART, 2)) {
            multiplier *= multiplier(BRAVE_HEART_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.MARTIAL_ARTIST, 2)) {
            multiplier *= multiplier(MARTIAL_ARTIST_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.MARTIAL_ARTIST, 4) && hasTimer(player, MARTIAL_DAMAGE_TICKS_KEY)) {
            multiplier *= multiplier(MARTIAL_ARTIST_4_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.GLADIATORS_FINALE, 2)) {
            multiplier *= multiplier(GLADIATOR_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.WANDERERS_TROUPE, 2)) {
            multiplier *= multiplier(WANDERER_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.PRAYERS_FOR_THUNDER, 1) && player.level().isThundering()) {
            multiplier *= 1.10F;
        }
        return multiplier;
    }

    private static void syncAdventurerTwoPiece(Player player, EnumMap<ArtifactSet, Integer> counts) {
        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }

        syncModifier(player, Attributes.MAX_HEALTH, ADVENTURER_TWO_PIECE_MAX_HEALTH,
                number(ADVENTURER_2_MAX_HEALTH), AttributeModifier.Operation.ADD_VALUE,
                hasSet(counts, ArtifactSet.ADVENTURER, 2));
        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    private static void syncModifier(Player player, Holder<Attribute> attribute, ResourceLocation modifierId, double amount, AttributeModifier.Operation operation, boolean active) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) {
            return;
        }

        AttributeModifier existing = instance.getModifier(modifierId);
        if (active) {
            if (existing == null || existing.amount() != amount || existing.operation() != operation) {
                instance.addOrUpdateTransientModifier(new AttributeModifier(modifierId, amount, operation));
            }
        } else if (existing != null) {
            instance.removeModifier(modifierId);
        }
    }

    private static void syncLittleWitchDictionaryBonuses(Player player) {
        boolean active = hasLittleWitchDictionary(player);
        int weight = active ? WitchGiftProgress.getAdvancementWeight(player) : 0;
        boolean hasBonus = active && weight > 0;
        syncModifier(player, Attributes.MAX_HEALTH, LITTLE_WITCH_DICTIONARY_MAX_HEALTH, weight * 0.1D, AttributeModifier.Operation.ADD_VALUE, hasBonus);
        syncModifier(player, Attributes.MOVEMENT_SPEED, LITTLE_WITCH_DICTIONARY_MOVEMENT_SPEED, weight * 0.001D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasBonus);
        syncModifier(player, TeyvatArtifacts.DAMAGE_BONUS, LITTLE_WITCH_DICTIONARY_DAMAGE_BONUS, weight * 0.001D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasBonus);
        syncOptionalModifier(player, IRON_MAX_MANA_ATTRIBUTE, LITTLE_WITCH_DICTIONARY_MAX_MANA, weight * 2.0D, AttributeModifier.Operation.ADD_VALUE, hasBonus);
        syncOptionalModifier(player, IRON_COOLDOWN_REDUCTION_ATTRIBUTE, LITTLE_WITCH_DICTIONARY_COOLDOWN_REDUCTION, weight * 0.001D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasBonus);
        syncOptionalModifier(player, IRON_CAST_TIME_REDUCTION_ATTRIBUTE, LITTLE_WITCH_DICTIONARY_CAST_TIME_REDUCTION, weight * 0.005D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasBonus);
        syncOptionalModifier(player, IRON_MANA_REGEN_ATTRIBUTE, LITTLE_WITCH_DICTIONARY_MANA_REGEN, weight * 0.005D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasBonus);
    }

    private static void syncScholarBonuses(Player player, EnumMap<ArtifactSet, Integer> counts) {
        boolean hasTwoPiece = hasSet(counts, ArtifactSet.SCHOLAR, 2);
        boolean hasFourPiece = hasSet(counts, ArtifactSet.SCHOLAR, 4);
        syncOptionalModifier(player, IRON_COOLDOWN_REDUCTION_ATTRIBUTE, SCHOLAR_TWO_PIECE_COOLDOWN_REDUCTION, percent(SCHOLAR_2_COOLDOWN_REDUCTION), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasTwoPiece);
        syncOptionalModifier(player, IRON_CAST_TIME_REDUCTION_ATTRIBUTE, SCHOLAR_TWO_PIECE_CAST_TIME_REDUCTION, percent(SCHOLAR_2_CAST_TIME_REDUCTION), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, hasTwoPiece);
        syncOptionalModifier(player, IRON_MAX_MANA_ATTRIBUTE, SCHOLAR_FOUR_PIECE_MAX_MANA, number(SCHOLAR_4_MAX_MANA), AttributeModifier.Operation.ADD_VALUE, hasFourPiece);
    }

    private static void syncOptionalModifier(Player player, ResourceLocation attributeId, ResourceLocation modifierId, double amount, AttributeModifier.Operation operation, boolean active) {
        BuiltInRegistries.ATTRIBUTE
                .getHolder(ResourceKey.create(Registries.ATTRIBUTE, attributeId))
                .ifPresent(attribute -> syncModifier(player, attribute, modifierId, amount, operation, active));
    }

    private static void syncIronSpellbookCooldownReduction(Player player, EnumMap<ArtifactSet, Integer> counts) {
        double amount = getIronSpellbookCooldownReduction(counts);
        syncOptionalModifier(
                player,
                IRON_COOLDOWN_REDUCTION_ATTRIBUTE,
                IRON_SPELLBOOKS_SET_COOLDOWN_REDUCTION,
                amount,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                amount > 0.0D
        );
    }

    private static double getIronSpellbookCooldownReduction(EnumMap<ArtifactSet, Integer> counts) {
        double amount = 0.0D;
        if (hasSet(counts, ArtifactSet.THUNDERING_FURY, 4)) amount = Math.max(amount, percent(THUNDERING_FURY_4_COOLDOWN_REDUCTION));
        if (hasSet(counts, ArtifactSet.CRIMSON_WITCH_OF_FLAMES, 4)) amount = Math.max(amount, percent(CRIMSON_WITCH_4_COOLDOWN_REDUCTION));
        if (hasSet(counts, ArtifactSet.BLIZZARD_STRAYER, 4)) amount = Math.max(amount, percent(BLIZZARD_STRAYER_4_COOLDOWN_REDUCTION));
        if (hasSet(counts, ArtifactSet.FLOWER_OF_PARADISE_LOST, 4)) amount = Math.max(amount, percent(PARADISE_LOST_4_COOLDOWN_REDUCTION));
        if (hasSet(counts, ArtifactSet.DEEPWOOD_MEMORIES, 4)) amount = Math.max(amount, percent(DEEPWOOD_4_COOLDOWN_REDUCTION));
        if (hasSet(counts, ArtifactSet.GOLDEN_TROUPE, 4)) amount = Math.max(amount, percent(GOLDEN_TROUPE_4_COOLDOWN_REDUCTION));
        if (hasSet(counts, ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, 4)) amount = Math.max(amount, percent(HARMONIC_WHIMSY_4_COOLDOWN_REDUCTION));
        if (hasSet(counts, ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING, 4)) amount = Math.max(amount, percent(SKYS_UNVEILING_4_COOLDOWN_REDUCTION));
        if (hasSet(counts, ArtifactSet.CELESTIAL_GIFT, 4)) amount = Math.max(amount, percent(CELESTIAL_GIFT_4_COOLDOWN_REDUCTION));
        if (hasSet(counts, ArtifactSet.SCARLET_PROOF, 4)) amount = Math.max(amount, percent(SCARLET_PROOF_4_COOLDOWN_REDUCTION));
        return amount;
    }

    private static final class DamageKindContext {
        private final DamageSource source;
        private final Player player;
        private final EnumMap<ArtifactDamageMath.SpellSchool, Boolean> spellSchools = new EnumMap<>(ArtifactDamageMath.SpellSchool.class);
        private Boolean projectileDamage;
        private Boolean spellDamage;
        private Boolean meleeDamage;

        private DamageKindContext(DamageSource source, Player player) {
            this.source = source;
            this.player = player;
        }

        private boolean projectileDamage() {
            if (projectileDamage == null) {
                projectileDamage = ArtifactDamageMath.isProjectileDamage(source);
            }
            return projectileDamage;
        }

        private boolean spellDamage() {
            if (spellDamage == null) {
                spellDamage = ArtifactDamageMath.isSpellDamage(source);
            }
            return spellDamage;
        }

        private boolean meleeDamage() {
            if (meleeDamage == null) {
                meleeDamage = source != null && player != null && !projectileDamage() && !spellDamage() && source.getDirectEntity() == player;
            }
            return meleeDamage;
        }

        private boolean spellSchool(ArtifactDamageMath.SpellSchool school) {
            return spellSchools.computeIfAbsent(school, key -> ArtifactDamageMath.isSpellDamageOfSchool(source, key));
        }
    }

    private static float applyIncomingSetDamage(DamageSource source, Player player, float amount) {
        EnumMap<ArtifactSet, Integer> counts = countEquippedSets(player);
        DamageKindContext damageKind = new DamageKindContext(source, null);
        if (isLightningDamage(source) && (hasSet(counts, ArtifactSet.PRAYERS_FOR_THUNDER, 1) || hasSet(counts, ArtifactSet.THUNDERSOOTHER, 2))) {
            return 0.0F;
        }
        if (isFireDamage(source) && (hasSet(counts, ArtifactSet.PRAYERS_FOR_ILLUMINATION, 1) || hasSet(counts, ArtifactSet.LAVAWALKER, 2))) {
            return 0.0F;
        }
        if (isFreezingDamage(source) && hasSet(counts, ArtifactSet.PRAYERS_TO_SPRINGTIME, 1)) {
            return 0.0F;
        }

        float result = amount;
        result = ArtifactDamageMath.applyLuckyReduction(result, hasSet(counts, ArtifactSet.LUCKY, 2), percent(LUCKY_2_DAMAGE_REDUCTION));

        if (isMiracleDamage(source) && hasSet(counts, ArtifactSet.TINY_MIRACLE, 2)) {
            result *= getMiracleReductionMultiplier(player, counts);
        }
        if (isFallDamage(source) && hasSet(counts, ArtifactSet.VIRIDESCENT_VENERER, 2)) {
            result *= remaining(VIRIDESCENT_2_FALL_REDUCTION);
        }
        if (hasSet(counts, ArtifactSet.DEFENDERS_WILL, 2)) {
            result *= remaining(DEFENDERS_WILL_2_REDUCTION);
        }
        if (hasSet(counts, ArtifactSet.DEFENDERS_WILL, 4)) {
            result *= remaining(DEFENDERS_WILL_4_REDUCTION);
        }
        if (hasSet(counts, ArtifactSet.PRAYERS_TO_SPRINGTIME, 1) && isColdBiome(player)) {
            result *= 0.90F;
        }
        if (hasSet(counts, ArtifactSet.THUNDERSOOTHER, 2) && damageKind.spellSchool(ArtifactDamageMath.SpellSchool.LIGHTNING)) {
            result *= remaining(THUNDERSOOTHER_2_SPELL_REDUCTION);
        }
        if (hasSet(counts, ArtifactSet.LAVAWALKER, 2) && damageKind.spellSchool(ArtifactDamageMath.SpellSchool.FIRE)) {
            result *= remaining(LAVAWALKER_2_SPELL_REDUCTION);
        }
        if (hasSet(counts, ArtifactSet.HUSK_OF_OPULENT_DREAMS, 2) && damageKind.spellDamage()) {
            result *= remaining(HUSK_2_MAGIC_REDUCTION);
        }
        if (hasSet(counts, ArtifactSet.HEART_OF_DEPTH, 4) && isInWaterOrRainyWeather(player)) {
            result *= remaining(HEART_OF_DEPTH_4_WATER_REDUCTION);
        }

        return result;
    }

    private static float applyOutgoingSetDamage(DamageSource source, LivingEntity target, float amount) {
        Player player = ArtifactDamageMath.resolvePlayer(source);
        if (player == null) {
            return amount;
        }

        EnumMap<ArtifactSet, Integer> counts = countEquippedSets(player);
        float result = amount;
        DamageKindContext damageKind = new DamageKindContext(source, player);
        boolean criticalDamage = consumeCriticalDamage(source, player, target);
        if (criticalDamage) {
            consumeDeepGalleriesForcedCritical(player, counts);
        }
        if (hasSet(counts, ArtifactSet.THUNDERSOOTHER, 4) && damageKind.spellSchool(ArtifactDamageMath.SpellSchool.LIGHTNING)) {
            triggerElementalSurge(player, THUNDERSOOTHER_DAMAGE_TICKS_KEY, THUNDERSOOTHER_DAMAGE_STACKS_KEY,
                    THUNDERSOOTHER_4_DURATION, THUNDERSOOTHER_4_MAX_STACKS);
        }
        if (hasSet(counts, ArtifactSet.LAVAWALKER, 4) && damageKind.spellSchool(ArtifactDamageMath.SpellSchool.FIRE)) {
            triggerElementalSurge(player, LAVAWALKER_DAMAGE_TICKS_KEY, LAVAWALKER_DAMAGE_STACKS_KEY,
                    LAVAWALKER_4_DURATION, LAVAWALKER_4_MAX_STACKS);
        }

        if (hasSet(counts, ArtifactSet.NIGHTTIME_WHISPERS, 2)) {
            result *= multiplier(NIGHTTIME_WHISPERS_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.NIGHTTIME_WHISPERS, 4) && hasTimer(player, NIGHTTIME_WHISPERS_DAMAGE_TICKS_KEY)) {
            result *= hasArmorOrOffhandShield(player) ? multiplier(NIGHTTIME_WHISPERS_4_ARMORED_DAMAGE) : multiplier(NIGHTTIME_WHISPERS_4_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.UNFINISHED_REVERIE, 2)) {
            result *= multiplier(UNFINISHED_REVERIE_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.UNFINISHED_REVERIE, 4) && !hasTimer(player, UNFINISHED_REVERIE_DISABLED_TICKS_KEY)) {
            result *= multiplier(UNFINISHED_REVERIE_4_DAMAGE);
        }
        if (hasTimer(player, CINDER_CITY_PARTY_DAMAGE_TICKS_KEY)) {
            result *= multiplier(CINDER_CITY_4_PARTY_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.OBSIDIAN_CODEX, 2) && isNightOrNonOverworld(player)) {
            result *= multiplier(OBSIDIAN_CODEX_2_DAMAGE);
        }
        if (isLongNightUndeadTarget(player, counts, target)) {
            result *= multiplier(LONG_NIGHT_2_UNDEAD_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, 2)) {
            result *= multiplier(DEEP_GALLERIES_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, 4) && damageKind.meleeDamage() && hasNoCritRate(player, counts)) {
            result *= multiplier(DEEP_GALLERIES_4_MELEE_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.SILKEN_MOONS_SERENADE, 4) && damageKind.meleeDamage()) {
            result *= 1.0F + getTimedStacks(player, SILKEN_MOON_STACKS_KEY, integer(SILKEN_MOON_4_MAX_STACKS)) * percent(SILKEN_MOON_4_DAMAGE_PER_STACK);
        }
        if (hasSet(counts, ArtifactSet.AUBADE_OF_MORNINGSTAR_AND_MOON, 4)) {
            result *= multiplier(AUBADE_4_DAMAGE);
            if (isNightOrNonOverworld(player)) {
                result *= multiplier(AUBADE_4_NIGHT_DAMAGE);
            }
        }
        if (hasSet(counts, ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, 2)) {
            result *= multiplier(RISING_WINDS_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, 4) && hasTimer(player, RISING_WINDS_DAMAGE_TICKS_KEY)) {
            result *= multiplier(RISING_WINDS_4_DAMAGE);
            if (hasLittleWitchDictionary(player)) {
                result *= multiplier(RISING_WINDS_4_DICTIONARY_DAMAGE);
            }
        }
        if (hasSet(counts, ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, 2)) {
            result *= multiplier(DISENCHANTMENT_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, 4)) {
            if (hasArmor(target)) {
                result *= multiplier(DISENCHANTMENT_4_ARMORED_DAMAGE);
            }
            result *= 1.0F + getDisenchantmentDamageBonus(player);
        }
        if (hasSet(counts, ArtifactSet.HEART_OF_THE_FURNACE, 2)) {
            result *= multiplier(FURNACE_HEART_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.HEART_OF_THE_FURNACE, 4)) {
            result *= 1.0F + getCoalStacks(player) * percent(FURNACE_HEART_4_DAMAGE_PER_COAL_STACK);
        }
        if (hasSet(counts, ArtifactSet.NOBLESSE_OBLIGE, 4) && hasTimer(player, NOBLESSE_DAMAGE_TICKS_KEY)) {
            result *= multiplier(NOBLESSE_4_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.BLOODSTAINED_CHIVALRY, 2) && !damageKind.spellDamage()) {
            result *= multiplier(BLOODSTAINED_2_NON_SPELL_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.BLOODSTAINED_CHIVALRY, 4) && hasTimer(player, BLOODSTAINED_DAMAGE_TICKS_KEY)) {
            result *= multiplier(BLOODSTAINED_4_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.HEART_OF_DEPTH, 4)) {
            if (hasTimer(player, HEART_OF_DEPTH_CRIT_TICKS_KEY) && damageKind.meleeDamage()) {
                result *= multiplier(HEART_OF_DEPTH_4_MELEE_DAMAGE);
            }
            if (isInWaterOrRainyWeather(player)) {
                result *= multiplier(HEART_OF_DEPTH_4_WATER_DAMAGE);
            }
        }
        if (hasSet(counts, ArtifactSet.ARCHAIC_PETRA, 4) && hasTimer(player, ARCHAIC_PETRA_DAMAGE_TICKS_KEY)) {
            result *= multiplier(ARCHAIC_PETRA_4_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.RETRACING_BOLIDE, 4)) {
            result *= 1.0F + player.getArmorValue() * percent(RETRACING_BOLIDE_4_DAMAGE_PER_ARMOR);
        }
        if (hasSet(counts, ArtifactSet.TENACITY_OF_THE_MILLELITH, 4)) {
            result *= 1.0F + getOnlinePlayerCount(player) * percent(TENACITY_4_DAMAGE_PER_PLAYER);
        }
        if (hasSet(counts, ArtifactSet.PALE_FLAME, 2) && damageKind.meleeDamage()) {
            float bonus = percent(PALE_FLAME_2_MELEE_DAMAGE);
            if (hasPaleFlameFullStacks(player, counts)) {
                bonus *= multiplier(PALE_FLAME_4_TWO_PIECE_INCREASE);
            }
            result *= 1.0F + bonus;
        }
        if (hasSet(counts, ArtifactSet.PALE_FLAME, 4)) {
            result *= 1.0F + getPaleFlameStacks(player) * percent(PALE_FLAME_4_DAMAGE_PER_STACK);
        }
        if (hasSet(counts, ArtifactSet.SHIMENAWAS_REMINISCENCE, 2)) {
            result *= multiplier(SHIMENAWA_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.SHIMENAWAS_REMINISCENCE, 4) && hasTimer(player, SHIMENAWA_DAMAGE_TICKS_KEY)) {
            result *= multiplier(SHIMENAWA_4_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.HUSK_OF_OPULENT_DREAMS, 4)) {
            result *= 1.0F + getHuskStacks(player) * percent(HUSK_4_DAMAGE_PER_STACK);
        }
        if (hasSet(counts, ArtifactSet.OCEAN_HUED_CLAM, 4) && hasTimer(player, OCEAN_DAMAGE_TICKS_KEY)) {
            result *= 1.0F + getPersistedData(player).getFloat(OCEAN_DAMAGE_BONUS_KEY);
        }
        if (hasSet(counts, ArtifactSet.VERMILLION_HEREAFTER, 2)) {
            result *= multiplier(VERMILLION_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.VERMILLION_HEREAFTER, 4) && hasTimer(player, VERMILLION_CRIT_TICKS_KEY)) {
            result *= multiplier(VERMILLION_4_CRIT_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.VERMILLION_HEREAFTER, 4)) {
            result *= 1.0F + getTimedStacks(player, VERMILLION_DAMAGE_STACKS_KEY, integer(VERMILLION_4_MAX_STACKS)) * percent(VERMILLION_4_HEALTH_LOSS_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.ECHOES_OF_AN_OFFERING, 2)) {
            result *= multiplier(ECHOES_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.ECHOES_OF_AN_OFFERING, 4) && damageKind.meleeDamage()) {
            result = applyEchoesDamage(player, result);
        }
        if (hasSet(counts, ArtifactSet.GILDED_DREAMS, 4) && target != null) {
            result *= 1.0F + target.getArmorValue() * percent(GILDED_4_DAMAGE_PER_ARMOR);
        }
        if (hasSet(counts, ArtifactSet.DESERT_PAVILION_CHRONICLE, 2)) {
            result *= multiplier(DESERT_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.DESERT_PAVILION_CHRONICLE, 4) && hasTimer(player, DESERT_DAMAGE_TICKS_KEY)) {
            result *= multiplier(DESERT_4_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.NYMPHS_DREAM, 2)) {
            result *= multiplier(NYMPH_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.NYMPHS_DREAM, 4)) {
            result *= getNymphDamageMultiplier(player);
        }
        if (hasSet(counts, ArtifactSet.MARECHAUSSEE_HUNTER, 2) && damageKind.meleeDamage()) {
            result *= multiplier(MARECHAUSSEE_2_MELEE_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.SONG_OF_DAYS_PAST, 4)) {
            result *= 1.0F + getTimedStacks(player, SONG_STACKS_KEY, integer(SONG_4_MAX_STACKS)) * percent(SONG_4_DAMAGE_PER_STACK);
        }
        if (hasSet(counts, ArtifactSet.RESOLUTION_OF_SOJOURNER, 2)) {
            result *= multiplier(RESOLUTION_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.BRAVE_HEART, 2)) {
            result *= multiplier(BRAVE_HEART_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.MARTIAL_ARTIST, 2)) {
            result *= multiplier(MARTIAL_ARTIST_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.MARTIAL_ARTIST, 4) && hasTimer(player, MARTIAL_DAMAGE_TICKS_KEY)) {
            result *= multiplier(MARTIAL_ARTIST_4_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.SCHOLAR, 4) && damageKind.spellDamage()) {
            result *= multiplier(SCHOLAR_4_SPELL_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.THUNDERSOOTHER, 4) && hasTimer(player, THUNDERSOOTHER_DAMAGE_TICKS_KEY)) {
            result *= getElementalSurgeMultiplier(player, THUNDERSOOTHER_DAMAGE_STACKS_KEY, THUNDERSOOTHER_4_MAX_STACKS, THUNDERSOOTHER_4_DAMAGE_PER_STACK);
        }
        if (hasSet(counts, ArtifactSet.LAVAWALKER, 4) && hasTimer(player, LAVAWALKER_DAMAGE_TICKS_KEY)) {
            result *= getElementalSurgeMultiplier(player, LAVAWALKER_DAMAGE_STACKS_KEY, LAVAWALKER_4_MAX_STACKS, LAVAWALKER_4_DAMAGE_PER_STACK);
        }
        if (hasSet(counts, ArtifactSet.GLADIATORS_FINALE, 2)) {
            result *= multiplier(GLADIATOR_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.GLADIATORS_FINALE, 4) && damageKind.meleeDamage()) {
            result *= multiplier(GLADIATOR_4_MELEE_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.WANDERERS_TROUPE, 2)) {
            result *= multiplier(WANDERER_2_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.WANDERERS_TROUPE, 4) && (damageKind.projectileDamage() || damageKind.spellDamage())) {
            result *= multiplier(WANDERER_4_PROJECTILE_SPELL_DAMAGE);
        }
        result = applySchoolSpellDamageBonus(result, target, counts, ArtifactSet.THUNDERING_FURY, damageKind, ArtifactDamageMath.SpellSchool.LIGHTNING);
        result = applySchoolSpellDamageBonus(result, target, counts, ArtifactSet.CRIMSON_WITCH_OF_FLAMES, damageKind, ArtifactDamageMath.SpellSchool.FIRE);
        result = applySchoolSpellDamageBonus(result, target, counts, ArtifactSet.BLIZZARD_STRAYER, damageKind, ArtifactDamageMath.SpellSchool.ICE);
        result = applySchoolSpellDamageBonus(result, target, counts, ArtifactSet.DEEPWOOD_MEMORIES, damageKind, ArtifactDamageMath.SpellSchool.NATURE);
        result = applySchoolSpellDamageBonus(result, target, counts, ArtifactSet.GOLDEN_TROUPE, damageKind, ArtifactDamageMath.SpellSchool.EVOCATION);
        result = applySchoolSpellDamageBonus(result, target, counts, ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, damageKind, ArtifactDamageMath.SpellSchool.ELDRITCH);
        result = applySchoolSpellDamageBonus(result, target, counts, ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING, damageKind, ArtifactDamageMath.SpellSchool.ENDER);
        result = applySchoolSpellDamageBonus(result, target, counts, ArtifactSet.CELESTIAL_GIFT, damageKind, ArtifactDamageMath.SpellSchool.HOLY);
        result = applySchoolSpellDamageBonus(result, target, counts, ArtifactSet.SCARLET_PROOF, damageKind, ArtifactDamageMath.SpellSchool.BLOOD);
        result = applyGeneralSpellDamageBonus(result, target, counts, ArtifactSet.FLOWER_OF_PARADISE_LOST, damageKind);
        if (hasSet(counts, ArtifactSet.PRAYERS_FOR_THUNDER, 1) && player.level().isThundering()) {
            result *= 1.10F;
        }
        if (hasSet(counts, ArtifactSet.BRAVE_HEART, 4) && target.getHealth() > target.getMaxHealth() * percent(BRAVE_HEART_4_TARGET_HEALTH_THRESHOLD)) {
            result *= multiplier(BRAVE_HEART_4_DAMAGE);
        }
        if ((hasSet(counts, ArtifactSet.GAMBLER, 2) || hasSet(counts, ArtifactSet.GAMBLER, 4)) && damageKind.projectileDamage()) {
            if (hasSet(counts, ArtifactSet.GAMBLER, 2)) {
                result *= multiplier(GAMBLER_2_PROJECTILE_DAMAGE);
            }
            CompoundTag data = getPersistedData(player);
            if (hasSet(counts, ArtifactSet.GAMBLER, 4) && data.getBoolean(GAMBLER_PROJECTILE_CHARGE_KEY)) {
                data.remove(GAMBLER_PROJECTILE_CHARGE_KEY);
                result *= multiplier(GAMBLER_4_PROJECTILE_DAMAGE);
            }
        }
        if (isFullyChargedArrow(source)) {
            triggerChargedArrowEffects(player, target, counts);
        }
        if (hasSet(counts, ArtifactSet.VIRIDESCENT_VENERER, 4) && damageKind.meleeDamage() && isActiveViridescentSweepDamage(player, target)) {
            result = Math.max(result, getViridescentSweepDamageFloor(player));
        }
        if (target != null && result > 0.0F) {
            triggerHuskStack(player, counts);
            if (hasSet(counts, ArtifactSet.NYMPHS_DREAM, 4)) {
                triggerNymphSetBonuses(player, counts, damageKind.meleeDamage(), damageKind.projectileDamage(), damageKind.spellDamage());
            }
            triggerNewDamageDealtSetBonuses(player, counts, criticalDamage);
        }

        if (amount > 0.0F) {
            syncDisplayDamageMultiplier(player,
                    ArtifactDamageMath.getOutgoingDamageMultiplier(player) * result / amount);
        }

        return result;
    }

    private static void triggerDamageTakenSetBonuses(Player player) {
        EnumMap<ArtifactSet, Integer> counts = countEquippedSets(player);
        if (hasSet(counts, ArtifactSet.OCEAN_HUED_CLAM, 4)) {
            triggerOceanHuedClam(player);
        }
        if (hasSet(counts, ArtifactSet.VERMILLION_HEREAFTER, 4)) {
            addTimedStack(player, VERMILLION_DAMAGE_TICKS_KEY, VERMILLION_DAMAGE_STACKS_KEY, ticks(VERMILLION_4_DURATION), integer(VERMILLION_4_MAX_STACKS));
        }
        if (hasSet(counts, ArtifactSet.VOURUKASHAS_GLOW, 4)) {
            addVourukashaStack(player);
        }
        if (hasSet(counts, ArtifactSet.MARECHAUSSEE_HUNTER, 4)) {
            addTimedStack(player, MARECHAUSSEE_TICKS_KEY, MARECHAUSSEE_STACKS_KEY, ticks(MARECHAUSSEE_4_DURATION), integer(MARECHAUSSEE_4_MAX_STACKS));
        }
    }

    private static void triggerOceanHuedClam(Player player) {
        CompoundTag data = getPersistedData(player);
        if (data.getInt(OCEAN_REGEN_COOLDOWN_TICKS_KEY) > 0) {
            return;
        }

        data.putInt(OCEAN_RECORD_TICKS_KEY, ticks(OCEAN_CLAM_4_RECORD_DURATION));
        data.putFloat(OCEAN_HEALING_RECORDED_KEY, 0.0F);
        data.putInt(OCEAN_REGEN_COOLDOWN_TICKS_KEY, ticks(OCEAN_CLAM_4_RECORD_DURATION));
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, ticks(OCEAN_CLAM_4_RECORD_DURATION), effectAmplifier(OCEAN_CLAM_4_REGENERATION_LEVEL)));
    }

    private static void addOceanHealing(Player player, float amount) {
        CompoundTag data = getPersistedData(player);
        data.putFloat(OCEAN_HEALING_RECORDED_KEY, data.getFloat(OCEAN_HEALING_RECORDED_KEY) + amount);
    }

    private static float applyEchoesDamage(Player player, float amount) {
        CompoundTag data = getPersistedData(player);
        float baseChance = percent(ECHOES_4_BASE_CHANCE);
        float chanceIncrease = percent(ECHOES_4_FAILURE_CHANCE);
        int maxFailures = chanceIncrease <= 0.0F ? 0 : Math.max(0, Mth.ceil((1.0F - baseChance) / chanceIncrease));
        int failures = Mth.clamp(data.getInt(ECHOES_FAILURES_KEY), 0, maxFailures);
        float chance = Mth.clamp(baseChance + failures * chanceIncrease, 0.0F, 1.0F);
        if (player.getRandom().nextFloat() < chance) {
            data.remove(ECHOES_FAILURES_KEY);
            return amount * multiplier(ECHOES_4_DAMAGE);
        }

        data.putInt(ECHOES_FAILURES_KEY, Mth.clamp(failures + 1, 0, maxFailures));
        return amount;
    }

    private static void triggerHuskStack(Player player, EnumMap<ArtifactSet, Integer> counts) {
        if (!hasSet(counts, ArtifactSet.HUSK_OF_OPULENT_DREAMS, 4)) {
            return;
        }

        CompoundTag data = getPersistedData(player);
        if (data.getInt(HUSK_STACK_COOLDOWN_TICKS_KEY) > 0) {
            return;
        }

        int oldStacks = getHuskStacks(player);
        int stacks = Mth.clamp(oldStacks + 1, 1, integer(HUSK_4_MAX_STACKS));
        data.putInt(HUSK_STACKS_KEY, stacks);
        data.putInt(HUSK_STACK_COOLDOWN_TICKS_KEY, ticks(HUSK_4_TRIGGER_INTERVAL));
        data.putInt(HUSK_STACK_DECAY_TICKS_KEY, ticks(HUSK_4_DECAY_INTERVAL));
        if (stacks != oldStacks) {
            syncSetBonuses(player);
        }
    }

    private static int getHuskStacks(Player player) {
        return Mth.clamp(getPersistedData(player).getInt(HUSK_STACKS_KEY), 0, integer(HUSK_4_MAX_STACKS));
    }

    private static void triggerNymphSetBonuses(Player player, EnumMap<ArtifactSet, Integer> counts, boolean meleeDamage, boolean projectileDamage, boolean spellDamage) {
        if (!hasSet(counts, ArtifactSet.NYMPHS_DREAM, 4)) {
            return;
        }

        if (meleeDamage) {
            markNymphTrigger(player, NYMPH_MELEE_TICKS_KEY);
        }
        if (projectileDamage) {
            markNymphTrigger(player, NYMPH_PROJECTILE_TICKS_KEY);
        }
        if (spellDamage) {
            markNymphTrigger(player, NYMPH_SPELL_TICKS_KEY);
        }
    }

    private static void markNymphTrigger(Player player, String key) {
        getPersistedData(player).putInt(key, ticks(NYMPH_4_DURATION));
    }

    private static float getNymphDamageMultiplier(Player player) {
        CompoundTag data = getPersistedData(player);
        int activeTypes = 0;
        if (data.getInt(NYMPH_MELEE_TICKS_KEY) > 0) {
            activeTypes++;
        }
        if (data.getInt(NYMPH_CRIT_TICKS_KEY) > 0) {
            activeTypes++;
        }
        if (data.getInt(NYMPH_PROJECTILE_TICKS_KEY) > 0) {
            activeTypes++;
        }
        if (data.getInt(NYMPH_SPELL_TICKS_KEY) > 0) {
            activeTypes++;
        }

        if (activeTypes >= 3) {
            return multiplier(NYMPH_4_THREE_STACK_DAMAGE);
        }
        if (activeTypes == 2) {
            return multiplier(NYMPH_4_TWO_STACK_DAMAGE);
        }
        return activeTypes == 1 ? multiplier(NYMPH_4_ONE_STACK_DAMAGE) : 1.0F;
    }

    private static void addTimedStack(Player player, String ticksKey, String stacksKey, int ticks, int maxStacks) {
        CompoundTag data = getPersistedData(player);
        int stacks = Mth.clamp(data.getInt(stacksKey) + 1, 1, maxStacks);
        data.putInt(stacksKey, stacks);
        data.putInt(ticksKey, ticks);
    }

    private static int getTimedStacks(Player player, String stacksKey, int maxStacks) {
        return Mth.clamp(getPersistedData(player).getInt(stacksKey), 0, maxStacks);
    }

    private static void addVourukashaStack(Player player) {
        CompoundTag data = getPersistedData(player);
        int replacement = 0;
        int shortestTicks = Integer.MAX_VALUE;
        for (int index = 0; index < integer(VOURUKASHA_4_MAX_STACKS); index++) {
            String key = getVourukashaStackKey(index);
            int ticks = data.getInt(key);
            if (ticks <= 0) {
                data.putInt(key, ticks(VOURUKASHA_4_DURATION));
                return;
            }
            if (ticks < shortestTicks) {
                shortestTicks = ticks;
                replacement = index;
            }
        }
        data.putInt(getVourukashaStackKey(replacement), ticks(VOURUKASHA_4_DURATION));
    }

    private static int getVourukashaStacks(Player player) {
        CompoundTag data = getPersistedData(player);
        int stacks = 0;
        for (int index = 0; index < integer(VOURUKASHA_4_MAX_STACKS); index++) {
            if (data.getInt(getVourukashaStackKey(index)) > 0) {
                stacks++;
            }
        }
        return stacks;
    }

    private static String getVourukashaStackKey(int index) {
        return VOURUKASHA_STACK_KEY_PREFIX + index;
    }

    private static void triggerElementalSurge(Player player, String ticksKey, String stacksKey, ArtifactSetConfig.Value duration, ArtifactSetConfig.Value maxStacks) {
        CompoundTag data = getPersistedData(player);
        int stacks = Mth.clamp(data.getInt(stacksKey) + 1, 1, integer(maxStacks));
        data.putInt(ticksKey, ticks(duration));
        data.putInt(stacksKey, stacks);
    }

    private static float getElementalSurgeMultiplier(Player player, String stacksKey, ArtifactSetConfig.Value maxStacks, ArtifactSetConfig.Value damagePerStack) {
        int stacks = Mth.clamp(getPersistedData(player).getInt(stacksKey), 0, integer(maxStacks));
        return 1.0F + stacks * percent(damagePerStack);
    }

    private static boolean consumeDeepGalleriesForcedCritical(Player player, EnumMap<ArtifactSet, Integer> counts) {
        CompoundTag data = getPersistedData(player);
        if (!hasSet(counts, ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, 4)) {
            data.remove(DEEP_GALLERIES_FORCED_CRIT_KEY);
            data.remove(DEEP_GALLERIES_DAMAGE_COUNT_KEY);
            return false;
        }
        if (!data.getBoolean(DEEP_GALLERIES_FORCED_CRIT_KEY)) {
            return false;
        }

        data.remove(DEEP_GALLERIES_FORCED_CRIT_KEY);
        return true;
    }

    private static boolean hasNoCritRate(Player player, EnumMap<ArtifactSet, Integer> counts) {
        double chance = player.getAttributeValue(ALObjects.Attributes.CRIT_CHANCE)
                - getSyncedCritBonus(player, ALObjects.Attributes.CRIT_CHANCE, SET_CRIT_CHANCE)
                + getSetCritRate(player, counts, null);
        return chance <= 0.050001D;
    }

    private static boolean isNight(Player player) {
        long dayTime = player.level().getDayTime() % 24000L;
        return dayTime >= 13000L && dayTime <= 23000L;
    }

    private static boolean isNightOrNonOverworld(Player player) {
        return isNight(player) || player.level().dimension() != Level.OVERWORLD;
    }

    private static boolean hasArmor(LivingEntity target) {
        return target != null && target.getArmorValue() > 0;
    }

    private static boolean isLongNightUndeadTarget(Player player, EnumMap<ArtifactSet, Integer> counts, LivingEntity target) {
        if (target == null || !hasSet(counts, ArtifactSet.LONG_NIGHTS_OATH, 2)) {
            return false;
        }
        if (target.getType().is(EntityTypeTags.UNDEAD)) {
            return true;
        }
        return hasSet(counts, ArtifactSet.LONG_NIGHTS_OATH, 4)
                && hasTimer(player, LONG_NIGHT_UNDEAD_TICKS_KEY)
                && target.getHealth() < target.getMaxHealth() * percent(LONG_NIGHT_4_HEALTH_THRESHOLD);
    }

    private static void triggerNewDamageDealtSetBonuses(Player player, EnumMap<ArtifactSet, Integer> counts, boolean criticalDamage) {
        if (hasSet(counts, ArtifactSet.UNFINISHED_REVERIE, 4)) {
            getPersistedData(player).putInt(UNFINISHED_REVERIE_DISABLED_TICKS_KEY, ticks(UNFINISHED_REVERIE_4_DISABLED_DURATION));
        }
        if (hasSet(counts, ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, 4)) {
            reduceTimer(player, CINDER_CITY_TOTEM_COOLDOWN_TICKS_KEY, ticks(CINDER_CITY_4_COOLDOWN_REDUCTION));
        }
        if (hasSet(counts, ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, 4)) {
            addDeepGalleriesDamageCount(player);
        }
        if (hasSet(counts, ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, 4)) {
            setTimer(player, RISING_WINDS_DAMAGE_TICKS_KEY, ticks(RISING_WINDS_4_DURATION));
        }
        if (hasSet(counts, ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, 4)) {
            if (criticalDamage) {
                clearDisenchantmentStacks(player);
            } else {
                addTimedStack(player, DISENCHANTMENT_NONCRIT_TICKS_KEY, DISENCHANTMENT_NONCRIT_STACKS_KEY,
                        ticks(DISENCHANTMENT_4_DURATION), getDisenchantmentMaxStacks());
            }
        }
    }

    private static void addDeepGalleriesDamageCount(Player player) {
        CompoundTag data = getPersistedData(player);
        int required = Math.max(1, integer(DEEP_GALLERIES_4_HITS_REQUIRED));
        int count = Mth.clamp(data.getInt(DEEP_GALLERIES_DAMAGE_COUNT_KEY) + 1, 0, required);
        if (count >= required) {
            data.putInt(DEEP_GALLERIES_DAMAGE_COUNT_KEY, 0);
            data.putBoolean(DEEP_GALLERIES_FORCED_CRIT_KEY, true);
        } else {
            data.putInt(DEEP_GALLERIES_DAMAGE_COUNT_KEY, count);
        }
    }

    private static void clearDisenchantmentStacks(Player player) {
        CompoundTag data = getPersistedData(player);
        data.remove(DISENCHANTMENT_NONCRIT_TICKS_KEY);
        data.remove(DISENCHANTMENT_NONCRIT_STACKS_KEY);
    }

    private static int getDisenchantmentMaxStacks() {
        float perStack = percent(DISENCHANTMENT_4_DAMAGE_PER_STACK);
        return perStack <= 0.0F ? 1 : Math.max(1, Mth.ceil(percent(DISENCHANTMENT_4_MAX_DAMAGE) / perStack));
    }

    private static float getDisenchantmentDamageBonus(Player player) {
        float stacked = getTimedStacks(player, DISENCHANTMENT_NONCRIT_STACKS_KEY, getDisenchantmentMaxStacks())
                * percent(DISENCHANTMENT_4_DAMAGE_PER_STACK);
        return Math.min(percent(DISENCHANTMENT_4_MAX_DAMAGE), stacked);
    }

    private static float applyCinderCityTotemProtection(Player player, float damage) {
        if (damage <= 0.0F) {
            return damage;
        }

        EnumMap<ArtifactSet, Integer> counts = countEquippedSets(player);
        if (!hasSet(counts, ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, 2)
                || damage < player.getHealth()
                || getPersistedData(player).getInt(CINDER_CITY_TOTEM_COOLDOWN_TICKS_KEY) > 0) {
            return damage;
        }

        triggerCinderCityTotem(player, hasSet(counts, ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY, 4));
        return 0.0F;
    }

    private static void triggerCinderCityTotem(Player player, boolean hasFourPiece) {
        CompoundTag data = getPersistedData(player);
        data.putInt(CINDER_CITY_TOTEM_COOLDOWN_TICKS_KEY, getCinderCityTotemCooldownTicks(player));
        player.setHealth(1.0F);
        player.removeAllEffects();
        player.clearFire();
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 45 * 20, 1));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 5 * 20, 1));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 40 * 20, 0));
        player.level().broadcastEntityEvent(player, (byte) 35);
        if (hasFourPiece) {
            applyCinderCityPartyDamage(player);
        }
    }

    private static int getCinderCityTotemCooldownTicks(Player player) {
        int seconds = Mth.floor((float) number(CINDER_CITY_2_COOLDOWN_BASE)
                + Math.max(0.0F, player.getMaxHealth() - (float) number(CINDER_CITY_2_COOLDOWN_HEALTH_OFFSET))
                * (float) number(CINDER_CITY_2_COOLDOWN_PER_HEALTH));
        return seconds * 20;
    }

    private static void applyCinderCityPartyDamage(Player player) {
        player.level().players().stream()
                .filter(nearby -> nearby.distanceToSqr(player) <= CINDER_CITY_PARTY_DAMAGE_RADIUS * CINDER_CITY_PARTY_DAMAGE_RADIUS)
                .forEach(nearby -> getPersistedData(nearby).putInt(CINDER_CITY_PARTY_DAMAGE_TICKS_KEY, ticks(CINDER_CITY_4_DURATION)));
    }

    private static void reduceTimer(Player player, String key, int ticks) {
        CompoundTag data = getPersistedData(player);
        int current = data.getInt(key);
        if (current <= ticks) {
            data.remove(key);
        } else {
            data.putInt(key, current - ticks);
        }
    }

    private static int getCoalStacks(Player player) {
        int coal = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(Items.COAL)) {
                coal += stack.getCount();
            }
        }
        for (ItemStack stack : player.getInventory().offhand) {
            if (stack.is(Items.COAL)) {
                coal += stack.getCount();
            }
        }
        return coal / 64;
    }

    private static boolean hasArmorOrOffhandShield(Player player) {
        return player.getArmorValue() > 0 || isShieldStack(player.getOffhandItem());
    }

    private static boolean hasLittleWitchDictionary(Player player) {
        boolean[] found = {false};
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> handler.getStacksHandler("witch_gift").ifPresent(stacksHandler -> {
            IDynamicStackHandler stacks = stacksHandler.getStacks();
            for (int index = 0; index < stacks.getSlots(); index++) {
                if (stacks.getStackInSlot(index).is(TeyvatArtifacts.THE_LITTLE_WITCHS_DICTIONARY.get())) {
                    found[0] = true;
                    return;
                }
            }
        }));
        return found[0];
    }

    private static void blockScarletHealing(LivingEntity target) {
        target.getPersistentData().putLong(SCARLET_NO_HEAL_UNTIL_KEY, target.level().getGameTime() + ticks(SCARLET_PROOF_4_NO_HEAL_DURATION));
    }

    private static boolean isScarletHealingBlocked(LivingEntity entity) {
        CompoundTag data = entity.getPersistentData();
        long until = data.getLong(SCARLET_NO_HEAL_UNTIL_KEY);
        if (until <= entity.level().getGameTime()) {
            data.remove(SCARLET_NO_HEAL_UNTIL_KEY);
            return false;
        }
        return true;
    }

    private static float applySchoolSpellDamageBonus(float amount, LivingEntity target, EnumMap<ArtifactSet, Integer> counts, ArtifactSet set, DamageKindContext damageKind, ArtifactDamageMath.SpellSchool school) {
        if (target == null || !hasSet(counts, set, 2) || !damageKind.spellSchool(school)) {
            return amount;
        }

        boolean glowingBeforeHit = target.hasEffect(MobEffects.GLOWING);
        float result = amount;
        result *= multiplier(getSchoolSpellDamageValue(set));
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, ticks(getSchoolGlowingDurationValue(set)), 0));
        if (hasSet(counts, set, 4) && glowingBeforeHit) {
            result *= multiplier(getSchoolGlowingDamageValue(set));
        }

        return result;
    }

    private static float applyGeneralSpellDamageBonus(float amount, LivingEntity target, EnumMap<ArtifactSet, Integer> counts, ArtifactSet set, DamageKindContext damageKind) {
        if (target == null || !hasSet(counts, set, 2) || !damageKind.spellDamage()) {
            return amount;
        }

        boolean glowingBeforeHit = target.hasEffect(MobEffects.GLOWING);
        float result = amount * multiplier(PARADISE_LOST_2_SPELL_DAMAGE);
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, ticks(PARADISE_LOST_2_GLOWING_DURATION), 0));
        if (hasSet(counts, set, 4) && glowingBeforeHit) {
            result *= multiplier(PARADISE_LOST_4_GLOWING_DAMAGE);
        }
        return result;
    }

    private static ArtifactSetConfig.Value getSchoolSpellDamageValue(ArtifactSet set) {
        return switch (set) {
            case THUNDERING_FURY -> THUNDERING_FURY_2_SPELL_DAMAGE;
            case CRIMSON_WITCH_OF_FLAMES -> CRIMSON_WITCH_2_SPELL_DAMAGE;
            case BLIZZARD_STRAYER -> BLIZZARD_STRAYER_2_SPELL_DAMAGE;
            case DEEPWOOD_MEMORIES -> DEEPWOOD_2_SPELL_DAMAGE;
            case GOLDEN_TROUPE -> GOLDEN_TROUPE_2_SPELL_DAMAGE;
            case FRAGMENT_OF_HARMONIC_WHIMSY -> HARMONIC_WHIMSY_2_SPELL_DAMAGE;
            case NIGHT_OF_THE_SKYS_UNVEILING -> SKYS_UNVEILING_2_SPELL_DAMAGE;
            case CELESTIAL_GIFT -> CELESTIAL_GIFT_2_SPELL_DAMAGE;
            case SCARLET_PROOF -> SCARLET_PROOF_2_SPELL_DAMAGE;
            default -> throw new IllegalArgumentException("No school spell damage configuration for " + set.id());
        };
    }

    private static ArtifactSetConfig.Value getSchoolGlowingDurationValue(ArtifactSet set) {
        return switch (set) {
            case THUNDERING_FURY -> THUNDERING_FURY_2_GLOWING_DURATION;
            case CRIMSON_WITCH_OF_FLAMES -> CRIMSON_WITCH_2_GLOWING_DURATION;
            case BLIZZARD_STRAYER -> BLIZZARD_STRAYER_2_GLOWING_DURATION;
            case DEEPWOOD_MEMORIES -> DEEPWOOD_2_GLOWING_DURATION;
            case GOLDEN_TROUPE -> GOLDEN_TROUPE_2_GLOWING_DURATION;
            case FRAGMENT_OF_HARMONIC_WHIMSY -> HARMONIC_WHIMSY_2_GLOWING_DURATION;
            case NIGHT_OF_THE_SKYS_UNVEILING -> SKYS_UNVEILING_2_GLOWING_DURATION;
            case CELESTIAL_GIFT -> CELESTIAL_GIFT_2_GLOWING_DURATION;
            case SCARLET_PROOF -> SCARLET_PROOF_2_GLOWING_DURATION;
            default -> throw new IllegalArgumentException("No school glowing duration configuration for " + set.id());
        };
    }

    private static ArtifactSetConfig.Value getSchoolGlowingDamageValue(ArtifactSet set) {
        return switch (set) {
            case THUNDERING_FURY -> THUNDERING_FURY_4_GLOWING_DAMAGE;
            case CRIMSON_WITCH_OF_FLAMES -> CRIMSON_WITCH_4_GLOWING_DAMAGE;
            case BLIZZARD_STRAYER -> BLIZZARD_STRAYER_4_GLOWING_DAMAGE;
            case DEEPWOOD_MEMORIES -> DEEPWOOD_4_GLOWING_DAMAGE;
            case GOLDEN_TROUPE -> GOLDEN_TROUPE_4_GLOWING_DAMAGE;
            case FRAGMENT_OF_HARMONIC_WHIMSY -> HARMONIC_WHIMSY_4_GLOWING_DAMAGE;
            case NIGHT_OF_THE_SKYS_UNVEILING -> SKYS_UNVEILING_4_GLOWING_DAMAGE;
            case CELESTIAL_GIFT -> CELESTIAL_GIFT_4_GLOWING_DAMAGE;
            case SCARLET_PROOF -> SCARLET_PROOF_4_GLOWING_DAMAGE;
            default -> throw new IllegalArgumentException("No school glowing damage configuration for " + set.id());
        };
    }

    private static boolean isFullyChargedArrow(DamageSource source) {
        return source != null && source.getDirectEntity() instanceof AbstractArrow arrow && arrow.isCritArrow();
    }

    private static void triggerChargedArrowEffects(Player player, LivingEntity target, EnumMap<ArtifactSet, Integer> counts) {
        if (target == null || target.level().isClientSide) {
            return;
        }

        if (hasSet(counts, ArtifactSet.THUNDERING_FURY, 4)) {
            strikeLightning(player, target);
            applyPunishingArrowEffects(target, THUNDERING_FURY_4_DEBUFF_DURATION, THUNDERING_FURY_4_WEAKNESS_LEVEL, THUNDERING_FURY_4_SLOWNESS_LEVEL);
        }
        if (hasSet(counts, ArtifactSet.CRIMSON_WITCH_OF_FLAMES, 4)) {
            ignite(target, (int) Math.round(number(CRIMSON_WITCH_4_IGNITE_DURATION)));
            applyPunishingArrowEffects(target, CRIMSON_WITCH_4_DEBUFF_DURATION, CRIMSON_WITCH_4_WEAKNESS_LEVEL, CRIMSON_WITCH_4_SLOWNESS_LEVEL);
        }
        if (hasSet(counts, ArtifactSet.BLIZZARD_STRAYER, 4)) {
            target.setTicksFrozen(ticks(BLIZZARD_STRAYER_4_FREEZE_DURATION));
            applyPunishingArrowEffects(target, BLIZZARD_STRAYER_4_DEBUFF_DURATION, BLIZZARD_STRAYER_4_WEAKNESS_LEVEL, BLIZZARD_STRAYER_4_SLOWNESS_LEVEL);
        }
        if (hasSet(counts, ArtifactSet.FLOWER_OF_PARADISE_LOST, 4)) {
            applyPunishingArrowEffects(target, PARADISE_LOST_4_DEBUFF_DURATION, PARADISE_LOST_4_WEAKNESS_LEVEL, PARADISE_LOST_4_SLOWNESS_LEVEL);
        }
        if (hasSet(counts, ArtifactSet.DEEPWOOD_MEMORIES, 4)) {
            target.addEffect(new MobEffectInstance(MobEffects.POISON, ticks(DEEPWOOD_4_POISON_DURATION), effectAmplifier(DEEPWOOD_4_POISON_LEVEL)));
            applyPunishingArrowEffects(target, DEEPWOOD_4_DEBUFF_DURATION, DEEPWOOD_4_WEAKNESS_LEVEL, DEEPWOOD_4_SLOWNESS_LEVEL);
        }
        if (hasSet(counts, ArtifactSet.GOLDEN_TROUPE, 4)) {
            directNearbyMobsAtTarget(target);
            applyPunishingArrowEffects(target, GOLDEN_TROUPE_4_DEBUFF_DURATION, GOLDEN_TROUPE_4_WEAKNESS_LEVEL, GOLDEN_TROUPE_4_SLOWNESS_LEVEL);
        }
        if (hasSet(counts, ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY, 4)) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, ticks(HARMONIC_WHIMSY_4_WITHER_DURATION), effectAmplifier(HARMONIC_WHIMSY_4_WITHER_LEVEL)));
            applyPunishingArrowEffects(target, HARMONIC_WHIMSY_4_DEBUFF_DURATION, HARMONIC_WHIMSY_4_WEAKNESS_LEVEL, HARMONIC_WHIMSY_4_SLOWNESS_LEVEL);
        }
        if (hasSet(counts, ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING, 4)) {
            swapPositions(player, target);
            applyPunishingArrowEffects(target, SKYS_UNVEILING_4_DEBUFF_DURATION, SKYS_UNVEILING_4_WEAKNESS_LEVEL, SKYS_UNVEILING_4_SLOWNESS_LEVEL);
        }
        if (hasSet(counts, ArtifactSet.CELESTIAL_GIFT, 4)) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, ticks(CELESTIAL_GIFT_4_WITHER_DURATION), effectAmplifier(CELESTIAL_GIFT_4_WITHER_LEVEL)));
            applyPunishingArrowEffects(target, CELESTIAL_GIFT_4_DEBUFF_DURATION, CELESTIAL_GIFT_4_WEAKNESS_LEVEL, CELESTIAL_GIFT_4_SLOWNESS_LEVEL);
        }
        if (hasSet(counts, ArtifactSet.SCARLET_PROOF, 4)) {
            blockScarletHealing(target);
            applyPunishingArrowEffects(target, SCARLET_PROOF_4_DEBUFF_DURATION, SCARLET_PROOF_4_WEAKNESS_LEVEL, SCARLET_PROOF_4_SLOWNESS_LEVEL);
        }
    }

    private static void strikeLightning(Player player, LivingEntity target) {
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(target.level());
        if (lightning != null) {
            lightning.moveTo(target.getX(), target.getY(), target.getZ());
            lightning.setVisualOnly(true);
            target.level().addFreshEntity(lightning);
        }

        target.level()
                .getEntitiesOfClass(
                        LivingEntity.class,
                        target.getBoundingBox().inflate(CHARGED_ARROW_LIGHTNING_RADIUS),
                        entity -> entity.isAlive() && entity != player
                )
                .forEach(entity -> entity.hurt(target.level().damageSources().lightningBolt(), CHARGED_ARROW_LIGHTNING_DAMAGE));
    }

    private static void applyPunishingArrowEffects(LivingEntity target, ArtifactSetConfig.Value duration, ArtifactSetConfig.Value weaknessLevel, ArtifactSetConfig.Value slownessLevel) {
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, ticks(duration), 0));
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, ticks(duration), effectAmplifier(weaknessLevel)));
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ticks(duration), effectAmplifier(slownessLevel)));
    }

    private static void ignite(LivingEntity target, int seconds) {
        target.igniteForSeconds(seconds);
    }

    private static void swapPositions(Player player, LivingEntity target) {
        double playerX = player.getX();
        double playerY = player.getY();
        double playerZ = player.getZ();
        float playerYRot = player.getYRot();
        float playerXRot = player.getXRot();
        player.teleportTo(target.getX(), target.getY(), target.getZ());
        target.teleportTo(playerX, playerY, playerZ);
        player.setYRot(target.getYRot());
        player.setXRot(target.getXRot());
        target.setYRot(playerYRot);
        target.setXRot(playerXRot);
    }

    private static void directNearbyMobsAtTarget(LivingEntity target) {
        target.level()
                .getEntitiesOfClass(
                        Mob.class,
                        target.getBoundingBox().inflate(GOLDEN_TROUPE_AGGRO_RADIUS),
                        mob -> mob.isAlive() && mob != target && mob.getType() != EntityType.WITHER
                )
                .forEach(mob -> mob.setTarget(target));
    }

    private static float getMiracleReductionMultiplier(Player player, EnumMap<ArtifactSet, Integer> counts) {
        CompoundTag data = getPersistedData(player);
        if (hasSet(counts, ArtifactSet.TINY_MIRACLE, 4)) {
            if (data.getInt(MIRACLE_BOOST_TICKS_KEY) > 0) {
                return remaining(TINY_MIRACLE_4_REDUCTION);
            }
            if (data.getInt(MIRACLE_COOLDOWN_TICKS_KEY) <= 0) {
                data.putInt(MIRACLE_BOOST_TICKS_KEY, ticks(TINY_MIRACLE_4_DURATION));
                data.putInt(MIRACLE_COOLDOWN_TICKS_KEY, ticks(TINY_MIRACLE_4_COOLDOWN));
                return remaining(TINY_MIRACLE_4_REDUCTION);
            }
        }

        return remaining(TINY_MIRACLE_2_REDUCTION);
    }

    private static float getSetCritRate(Player player, EnumMap<ArtifactSet, Integer> counts, LivingEntity target) {
        float critRate = 0.0F;
        if (hasSet(counts, ArtifactSet.BERSERKER, 2)) {
            critRate += percent(BERSERKER_2_CRIT_RATE);
        }
        if (hasSet(counts, ArtifactSet.BERSERKER, 4) && player.getHealth() < player.getMaxHealth() * percent(BERSERKER_4_HEALTH_THRESHOLD)) {
            critRate += percent(BERSERKER_4_CRIT_RATE);
        }
        if (hasSet(counts, ArtifactSet.ARCHAIC_PETRA, 4) && hasTimer(player, ARCHAIC_PETRA_DAMAGE_TICKS_KEY)) {
            critRate += percent(ARCHAIC_PETRA_4_CRIT_RATE);
        }
        if (hasSet(counts, ArtifactSet.MARECHAUSSEE_HUNTER, 4)) {
            critRate += getTimedStacks(player, MARECHAUSSEE_STACKS_KEY, integer(MARECHAUSSEE_4_MAX_STACKS)) * percent(MARECHAUSSEE_4_CRIT_RATE_PER_STACK);
        }
        if (hasSet(counts, ArtifactSet.OBSIDIAN_CODEX, 4) && isNightOrNonOverworld(player)) {
            critRate += percent(OBSIDIAN_CODEX_4_CRIT_RATE);
        }
        if (hasSet(counts, ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS, 4) && hasTimer(player, RISING_WINDS_DAMAGE_TICKS_KEY) && hasLittleWitchDictionary(player)) {
            critRate += percent(RISING_WINDS_4_DICTIONARY_CRIT_RATE);
        }
        if (hasSet(counts, ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW, 4) && hasArmor(target)) {
            critRate += percent(DISENCHANTMENT_4_CRIT_RATE);
        }

        return critRate;
    }

    private static float getSetCritDamage(Player player, EnumMap<ArtifactSet, Integer> counts) {
        float critDamage = 0.0F;
        if (hasSet(counts, ArtifactSet.NOBLESSE_OBLIGE, 2)) {
            critDamage += percent(NOBLESSE_2_CRIT_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.ARCHAIC_PETRA, 4) && hasTimer(player, ARCHAIC_PETRA_DAMAGE_TICKS_KEY)) {
            critDamage += percent(ARCHAIC_PETRA_4_CRIT_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.EMBLEM_OF_SEVERED_FATE, 4)) {
            critDamage += getIncreasedAttackSpeedPercent(player) * (float) number(EMBLEM_4_CRIT_DAMAGE_RATIO);
        }
        if (hasSet(counts, ArtifactSet.VOURUKASHAS_GLOW, 4)) {
            critDamage += percent(VOURUKASHA_4_CRIT_DAMAGE) * (1.0F + getVourukashaStacks(player) * percent(VOURUKASHA_4_STACK_INCREASE));
        }
        if (hasSet(counts, ArtifactSet.OBSIDIAN_CODEX, 4) && isNightOrNonOverworld(player)) {
            critDamage += percent(OBSIDIAN_CODEX_4_CRIT_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES, 4) && hasNoCritRate(player, counts)) {
            critDamage += percent(DEEP_GALLERIES_4_CRIT_DAMAGE);
        }
        if (hasSet(counts, ArtifactSet.HEART_OF_DEPTH, 4) && hasTimer(player, HEART_OF_DEPTH_CRIT_TICKS_KEY)) {
            critDamage += percent(HEART_OF_DEPTH_4_CRIT_DAMAGE);
        }

        return critDamage;
    }

    private static void triggerCriticalSetBonuses(Player player, EnumMap<ArtifactSet, Integer> counts) {
        if (hasSet(counts, ArtifactSet.TRAVELING_DOCTOR, 4)) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, ticks(TRAVELING_DOCTOR_4_DURATION), effectAmplifier(TRAVELING_DOCTOR_4_REGENERATION_LEVEL)));
        }
        if (hasSet(counts, ArtifactSet.INSTRUCTOR, 4)) {
            setTimer(player, INSTRUCTOR_ARMOR_IGNORE_TICKS_KEY, ticks(INSTRUCTOR_4_DURATION));
        }
        if (hasSet(counts, ArtifactSet.THE_EXILE, 4)) {
            setTimer(player, EXILE_ATTACK_SPEED_TICKS_KEY, ticks(EXILE_4_DURATION));
        }
        if (hasSet(counts, ArtifactSet.MARTIAL_ARTIST, 4)) {
            setTimer(player, MARTIAL_DAMAGE_TICKS_KEY, ticks(MARTIAL_ARTIST_4_DURATION));
        }
        if (hasSet(counts, ArtifactSet.NOBLESSE_OBLIGE, 4)) {
            setTimer(player, NOBLESSE_DAMAGE_TICKS_KEY, ticks(NOBLESSE_4_DURATION));
        }
        if (hasSet(counts, ArtifactSet.MAIDEN_BELOVED, 4)) {
            setTimer(player, MAIDEN_HEAL_TICKS_KEY, ticks(MAIDEN_4_CRIT_DURATION));
        }
        if (hasSet(counts, ArtifactSet.PALE_FLAME, 4)) {
            addPaleFlameStack(player);
        }
        if (hasSet(counts, ArtifactSet.SHIMENAWAS_REMINISCENCE, 4) && !hasTimer(player, SHIMENAWA_DAMAGE_TICKS_KEY) && player.getFoodData().getFoodLevel() >= integer(SHIMENAWA_4_FOOD_THRESHOLD)) {
            setTimer(player, SHIMENAWA_DAMAGE_TICKS_KEY, ticks(SHIMENAWA_4_DURATION));
        }
        if (hasSet(counts, ArtifactSet.VERMILLION_HEREAFTER, 4)) {
            setTimer(player, VERMILLION_CRIT_TICKS_KEY, ticks(VERMILLION_4_DURATION));
        }
        if (hasSet(counts, ArtifactSet.DESERT_PAVILION_CHRONICLE, 4)) {
            setTimer(player, DESERT_DAMAGE_TICKS_KEY, ticks(DESERT_4_DURATION));
        }
        if (hasSet(counts, ArtifactSet.NYMPHS_DREAM, 4)) {
            markNymphTrigger(player, NYMPH_CRIT_TICKS_KEY);
        }
        if (hasSet(counts, ArtifactSet.NIGHTTIME_WHISPERS, 4)) {
            setTimer(player, NIGHTTIME_WHISPERS_DAMAGE_TICKS_KEY, ticks(NIGHTTIME_WHISPERS_4_DURATION));
        }
        if (hasSet(counts, ArtifactSet.LONG_NIGHTS_OATH, 4)) {
            setTimer(player, LONG_NIGHT_UNDEAD_TICKS_KEY, LONG_NIGHT_UNDEAD_TICKS);
        }
        if (hasSet(counts, ArtifactSet.HEART_OF_DEPTH, 4)) {
            setTimer(player, HEART_OF_DEPTH_CRIT_TICKS_KEY, ticks(HEART_OF_DEPTH_4_CRIT_DURATION));
        }
    }

    private static void applyTickingSetEffects(Player player, EnumMap<ArtifactSet, Integer> counts) {
        if (hasSet(counts, ArtifactSet.HEART_OF_DEPTH, 2) && player.isUnderWater()) {
            refreshEffect(player, MobEffects.WATER_BREATHING, 220, 0);
            refreshEffect(player, MobEffects.NIGHT_VISION, 220, 0);
        }
        if (hasSet(counts, ArtifactSet.PRAYERS_FOR_DESTINY, 1)) {
            refreshEffect(player, MobEffects.WATER_BREATHING, 220, 0);
            if (player.isInWaterOrRain()) {
                refreshEffect(player, MobEffects.DOLPHINS_GRACE, 220, 0);
            }
        }
        if (hasSet(counts, ArtifactSet.PRAYERS_FOR_ILLUMINATION, 1)) {
            player.clearFire();
        }
        if (hasSet(counts, ArtifactSet.PRAYERS_TO_SPRINGTIME, 1)) {
            player.setTicksFrozen(0);
        }
        if (hasSet(counts, ArtifactSet.ARCHAIC_PETRA, 2) && isHoldingShield(player)) {
            refreshEffect(player, MobEffects.DAMAGE_RESISTANCE, 220, effectAmplifier(ARCHAIC_PETRA_2_RESISTANCE_LEVEL));
            player.getCooldowns().removeCooldown(Items.SHIELD);
        }
    }

    private static void refreshEffect(Player player, Holder<MobEffect> effect, int duration, int amplifier) {
        MobEffectInstance current = player.getEffect(effect);
        if (current == null || current.getAmplifier() < amplifier || current.getDuration() < duration - 20) {
            player.addEffect(new MobEffectInstance(effect, duration, amplifier, true, false, true));
        }
    }

    private static boolean isHealingEffect(MobEffectInstance effect) {
        return effect.getEffect().equals(MobEffects.HEAL) || effect.getEffect().equals(MobEffects.REGENERATION);
    }

    private static boolean isResistanceEffect(MobEffectInstance effect) {
        return effect.getEffect().equals(MobEffects.DAMAGE_RESISTANCE);
    }

    private static boolean isMiracleDamage(DamageSource source) {
        return isFireDamage(source) || isDrowningDamage(source) || isFreezingDamage(source);
    }

    private static boolean isFireDamage(DamageSource source) {
        return source != null && source.is(DamageTypeTags.IS_FIRE);
    }

    private static boolean isDrowningDamage(DamageSource source) {
        return source != null && source.is(DamageTypeTags.IS_DROWNING);
    }

    private static boolean isFreezingDamage(DamageSource source) {
        return source != null && source.is(DamageTypeTags.IS_FREEZING);
    }

    private static boolean isLightningDamage(DamageSource source) {
        return source != null && source.is(DamageTypeTags.IS_LIGHTNING);
    }

    private static boolean isFallDamage(DamageSource source) {
        return source != null && source.is(DamageTypeTags.IS_FALL);
    }

    private static boolean isColdBiome(Player player) {
        BlockPos pos = player.blockPosition();
        return player.level().getBiome(pos).value().coldEnoughToSnow(pos);
    }

    private static boolean isHoldingShield(Player player) {
        return isShieldStack(player.getMainHandItem()) || isShieldStack(player.getOffhandItem());
    }

    private static boolean isInWaterOrRainyWeather(Player player) {
        return player.isInWater() || player.level().isRaining();
    }

    private static boolean isShieldStack(ItemStack stack) {
        return !stack.isEmpty() && (stack.is(SHIELD_ITEM_TAG) || stack.is(Items.SHIELD));
    }

    private static int getOnlinePlayerCount(Player player) {
        return player.getServer() == null ? player.level().players().size() : player.getServer().getPlayerCount();
    }

    private static float getIncreasedAttackSpeedPercent(Player player) {
        AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
        if (attackSpeed == null || attackSpeed.getBaseValue() <= 0.0D) {
            return 0.0F;
        }

        return Math.max(0.0F, (float) (player.getAttributeValue(Attributes.ATTACK_SPEED) / attackSpeed.getBaseValue() - 1.0D));
    }

    private static float getSetArmorIgnoreBonus(DamageSource source, LivingEntity target) {
        Player player = ArtifactDamageMath.resolvePlayer(source);
        if (player == null || target == null) {
            return 0.0F;
        }

        EnumMap<ArtifactSet, Integer> counts = countEquippedSets(player);
        float bonus = 0.0F;
        if (hasSet(counts, ArtifactSet.GILDED_DREAMS, 2)) {
            bonus += percent(GILDED_2_ARMOR_IGNORE);
        }
        if (hasSet(counts, ArtifactSet.AUBADE_OF_MORNINGSTAR_AND_MOON, 2)) {
            bonus += percent(AUBADE_2_ARMOR_IGNORE);
        }
        if (ArtifactDamageMath.isMeleeDamage(source) && hasSet(counts, ArtifactSet.SILKEN_MOONS_SERENADE, 4)) {
            addTimedStack(player, SILKEN_MOON_TICKS_KEY, SILKEN_MOON_STACKS_KEY, ticks(SILKEN_MOON_4_DURATION), integer(SILKEN_MOON_4_MAX_STACKS));
            bonus += getTimedStacks(player, SILKEN_MOON_STACKS_KEY, integer(SILKEN_MOON_4_MAX_STACKS)) * percent(SILKEN_MOON_4_ARMOR_IGNORE_PER_STACK);
        }
        if (ArtifactDamageMath.isMeleeDamage(source) && hasSet(counts, ArtifactSet.VIRIDESCENT_VENERER, 4) && isActiveViridescentSweepDamage(player, target)) {
            bonus += percent(VIRIDESCENT_4_ARMOR_IGNORE);
        }
        return Mth.clamp(bonus, 0.0F, 1.0F);
    }

    private static void markViridescentSweep(Player player, LivingEntity primaryTarget) {
        CompoundTag data = getPersistedData(player);
        data.putInt(VIRIDESCENT_SWEEP_TICKS_KEY, VIRIDESCENT_SWEEP_WINDOW_TICKS);
        data.putUUID(VIRIDESCENT_SWEEP_PRIMARY_KEY, primaryTarget.getUUID());
    }

    private static boolean isActiveViridescentSweepDamage(Player player, LivingEntity target) {
        if (target == null) {
            return false;
        }

        CompoundTag data = getPersistedData(player);
        return data.getInt(VIRIDESCENT_SWEEP_TICKS_KEY) > 0
                && data.hasUUID(VIRIDESCENT_SWEEP_PRIMARY_KEY)
                && !target.getUUID().equals(data.getUUID(VIRIDESCENT_SWEEP_PRIMARY_KEY));
    }

    private static float getViridescentSweepDamageFloor(Player player) {
        return Math.max(0.0F, (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE)
                * player.getAttackStrengthScale(0.5F) * percent(VIRIDESCENT_4_SWEEP_DAMAGE));
    }

    private static int getPaleFlameStacks(Player player) {
        return Mth.clamp(getPersistedData(player).getInt(PALE_FLAME_STACKS_KEY), 0, integer(PALE_FLAME_4_MAX_STACKS));
    }

    private static boolean hasPaleFlameFullStacks(Player player, EnumMap<ArtifactSet, Integer> counts) {
        return hasSet(counts, ArtifactSet.PALE_FLAME, 4) && getPaleFlameStacks(player) >= integer(PALE_FLAME_4_MAX_STACKS);
    }

    private static void addPaleFlameStack(Player player) {
        CompoundTag data = getPersistedData(player);
        data.putInt(PALE_FLAME_STACKS_KEY, Mth.clamp(data.getInt(PALE_FLAME_STACKS_KEY) + 1, 1, integer(PALE_FLAME_4_MAX_STACKS)));
        data.putInt(PALE_FLAME_TICKS_KEY, ticks(PALE_FLAME_4_DURATION));
    }

    private static void tickSetBonusTimers(Player player) {
        CompoundTag data = getPersistedData(player);
        tickDown(data, MIRACLE_BOOST_TICKS_KEY);
        tickDown(data, MIRACLE_COOLDOWN_TICKS_KEY);
        tickDown(data, INSTRUCTOR_ARMOR_IGNORE_TICKS_KEY);
        tickDown(data, EXILE_ATTACK_SPEED_TICKS_KEY);
        tickDown(data, MARTIAL_DAMAGE_TICKS_KEY);
        tickDown(data, GAMBLER_COOLDOWN_TICKS_KEY);
        tickDown(data, NOBLESSE_DAMAGE_TICKS_KEY);
        tickDown(data, BLOODSTAINED_DAMAGE_TICKS_KEY);
        tickDown(data, MAIDEN_HEAL_TICKS_KEY);
        tickDown(data, VIRIDESCENT_ATTACK_SPEED_TICKS_KEY);
        tickDown(data, ARCHAIC_PETRA_DAMAGE_TICKS_KEY);
        tickViridescentSweep(data);
        tickPaleFlame(data);
        tickShimenawa(player, data);
        tickDownWithStacks(data, THUNDERSOOTHER_DAMAGE_TICKS_KEY, THUNDERSOOTHER_DAMAGE_STACKS_KEY);
        tickDownWithStacks(data, LAVAWALKER_DAMAGE_TICKS_KEY, LAVAWALKER_DAMAGE_STACKS_KEY);
        tickDown(data, HUSK_STACK_COOLDOWN_TICKS_KEY);
        tickHuskStacks(player, data);
        tickOceanDamage(data);
        tickOceanRecord(data);
        tickDown(data, OCEAN_REGEN_COOLDOWN_TICKS_KEY);
        tickDown(data, VERMILLION_CRIT_TICKS_KEY);
        tickDownWithStacks(data, VERMILLION_DAMAGE_TICKS_KEY, VERMILLION_DAMAGE_STACKS_KEY);
        tickDown(data, DESERT_DAMAGE_TICKS_KEY);
        tickDown(data, HEART_OF_DEPTH_CRIT_TICKS_KEY);
        tickDown(data, NYMPH_MELEE_TICKS_KEY);
        tickDown(data, NYMPH_CRIT_TICKS_KEY);
        tickDown(data, NYMPH_PROJECTILE_TICKS_KEY);
        tickDown(data, NYMPH_SPELL_TICKS_KEY);
        tickVourukashaStacks(data);
        tickDownWithStacks(data, MARECHAUSSEE_TICKS_KEY, MARECHAUSSEE_STACKS_KEY);
        tickDownWithStacks(data, SONG_TICKS_KEY, SONG_STACKS_KEY);
        tickDown(data, NIGHTTIME_WHISPERS_DAMAGE_TICKS_KEY);
        tickDown(data, UNFINISHED_REVERIE_DISABLED_TICKS_KEY);
        tickDown(data, CINDER_CITY_TOTEM_COOLDOWN_TICKS_KEY);
        tickDown(data, CINDER_CITY_PARTY_DAMAGE_TICKS_KEY);
        tickDown(data, LONG_NIGHT_UNDEAD_TICKS_KEY);
        tickDownWithStacks(data, SILKEN_MOON_TICKS_KEY, SILKEN_MOON_STACKS_KEY);
        tickDown(data, RISING_WINDS_DAMAGE_TICKS_KEY);
        tickDownWithStacks(data, DISENCHANTMENT_NONCRIT_TICKS_KEY, DISENCHANTMENT_NONCRIT_STACKS_KEY);
    }

    private static void tickHuskStacks(Player player, CompoundTag data) {
        int stacks = Mth.clamp(data.getInt(HUSK_STACKS_KEY), 0, integer(HUSK_4_MAX_STACKS));
        if (stacks <= 0) {
            data.remove(HUSK_STACKS_KEY);
            data.remove(HUSK_STACK_DECAY_TICKS_KEY);
            return;
        }

        int ticks = data.getInt(HUSK_STACK_DECAY_TICKS_KEY);
        if (ticks <= 1) {
            stacks = Mth.clamp(stacks - 1, 0, integer(HUSK_4_MAX_STACKS));
            if (stacks > 0) {
                data.putInt(HUSK_STACKS_KEY, stacks);
                data.putInt(HUSK_STACK_DECAY_TICKS_KEY, ticks(HUSK_4_DECAY_INTERVAL));
            } else {
                data.remove(HUSK_STACKS_KEY);
                data.remove(HUSK_STACK_DECAY_TICKS_KEY);
            }
            syncSetBonuses(player);
        } else {
            data.putInt(HUSK_STACK_DECAY_TICKS_KEY, ticks - 1);
        }
    }

    private static void tickOceanDamage(CompoundTag data) {
        int ticks = data.getInt(OCEAN_DAMAGE_TICKS_KEY);
        if (ticks <= 1) {
            data.remove(OCEAN_DAMAGE_TICKS_KEY);
            data.remove(OCEAN_DAMAGE_BONUS_KEY);
        } else {
            data.putInt(OCEAN_DAMAGE_TICKS_KEY, ticks - 1);
        }
    }

    private static void tickOceanRecord(CompoundTag data) {
        int ticks = data.getInt(OCEAN_RECORD_TICKS_KEY);
        if (ticks <= 0) {
            return;
        }

        if (ticks <= 1) {
            float healed = Math.max(0.0F, data.getFloat(OCEAN_HEALING_RECORDED_KEY));
            data.remove(OCEAN_RECORD_TICKS_KEY);
            data.remove(OCEAN_HEALING_RECORDED_KEY);
            if (healed > 0.0F) {
                data.putFloat(OCEAN_DAMAGE_BONUS_KEY, healed * percent(OCEAN_CLAM_4_DAMAGE_PER_HEAL));
                data.putInt(OCEAN_DAMAGE_TICKS_KEY, ticks(OCEAN_CLAM_4_DAMAGE_DURATION));
            }
        } else {
            data.putInt(OCEAN_RECORD_TICKS_KEY, ticks - 1);
        }
    }

    private static void tickVourukashaStacks(CompoundTag data) {
        for (int index = 0; index < integer(VOURUKASHA_4_MAX_STACKS); index++) {
            tickDown(data, getVourukashaStackKey(index));
        }
    }

    private static void tickDown(CompoundTag data, String key) {
        int ticks = data.getInt(key);
        if (ticks <= 1) {
            data.remove(key);
        } else {
            data.putInt(key, ticks - 1);
        }
    }

    private static void tickDownWithStacks(CompoundTag data, String ticksKey, String stacksKey) {
        int ticks = data.getInt(ticksKey);
        if (ticks <= 1) {
            data.remove(ticksKey);
            data.remove(stacksKey);
        } else {
            data.putInt(ticksKey, ticks - 1);
        }
    }

    private static void tickViridescentSweep(CompoundTag data) {
        tickDown(data, VIRIDESCENT_SWEEP_TICKS_KEY);
        if (data.getInt(VIRIDESCENT_SWEEP_TICKS_KEY) <= 0) {
            data.remove(VIRIDESCENT_SWEEP_PRIMARY_KEY);
        }
    }

    private static void tickPaleFlame(CompoundTag data) {
        int ticks = data.getInt(PALE_FLAME_TICKS_KEY);
        if (ticks <= 1) {
            int stacks = Mth.clamp(data.getInt(PALE_FLAME_STACKS_KEY) - 1, 0, integer(PALE_FLAME_4_MAX_STACKS));
            if (stacks > 0) {
                data.putInt(PALE_FLAME_STACKS_KEY, stacks);
                data.putInt(PALE_FLAME_TICKS_KEY, ticks(PALE_FLAME_4_DURATION));
            } else {
                data.remove(PALE_FLAME_STACKS_KEY);
                data.remove(PALE_FLAME_TICKS_KEY);
            }
        } else {
            data.putInt(PALE_FLAME_TICKS_KEY, ticks - 1);
        }
    }

    private static void tickShimenawa(Player player, CompoundTag data) {
        int ticks = data.getInt(SHIMENAWA_DAMAGE_TICKS_KEY);
        if (ticks <= 0) {
            return;
        }

        if (ticks % 20 == 0 && !player.getAbilities().instabuild) {
            player.getFoodData().setFoodLevel(Math.max(0, player.getFoodData().getFoodLevel() - integer(SHIMENAWA_4_FOOD_DRAIN)));
        }
        tickDown(data, SHIMENAWA_DAMAGE_TICKS_KEY);
    }

    private static void setTimer(Player player, String key, int ticks) {
        getPersistedData(player).putInt(key, ticks);
        syncSetBonuses(player);
    }

    private static boolean hasTimer(Player player, String key) {
        return getPersistedData(player).getInt(key) > 0;
    }

    private static int getLootrOpenState(BlockEntity blockEntity, Player player) {
        if (blockEntity == null) {
            return LOOTR_NOT_PRESENT;
        }

        boolean lootrBlockEntity = isLootrBlockEntity(blockEntity);
        try {
            Class<?> lootrApiClass = Class.forName("noobanidus.mods.lootr.common.api.LootrAPI");
            Class<?> infoProviderClass = Class.forName("noobanidus.mods.lootr.common.api.data.ILootrInfoProvider");
            Object infoProvider = infoProviderClass.isInstance(blockEntity)
                    ? blockEntity
                    : lootrApiClass.getMethod("resolveBlockEntity", BlockEntity.class).invoke(null, blockEntity);

            if (!infoProviderClass.isInstance(infoProvider)) {
                return lootrBlockEntity ? LOOTR_ALREADY_OPENED : LOOTR_NOT_PRESENT;
            }

            Object lootTable = infoProviderClass.getMethod("getInfoLootTable").invoke(infoProvider);
            if (lootTable == null) {
                return LOOTR_ALREADY_OPENED;
            }

            boolean opened = (Boolean) infoProviderClass
                    .getMethod("hasServerOpened", Player.class)
                    .invoke(infoProvider, player);
            return opened ? LOOTR_ALREADY_OPENED : LOOTR_UNOPENED;
        } catch (ReflectiveOperationException | LinkageError | ClassCastException exception) {
            return lootrBlockEntity ? LOOTR_ALREADY_OPENED : LOOTR_NOT_PRESENT;
        }
    }

    private static boolean isLootrBlockEntity(BlockEntity blockEntity) {
        return blockEntity.getClass().getName().startsWith("noobanidus.mods.lootr.");
    }

    private static boolean isUnopenedVanillaLootContainer(BlockEntity blockEntity) {
        return blockEntity instanceof RandomizableContainerBlockEntity container && container.getLootTable() != null;
    }

    private static boolean hasSet(EnumMap<ArtifactSet, Integer> counts, ArtifactSet set, int pieces) {
        return counts.getOrDefault(set, 0) >= pieces;
    }

    private static int countEquippedPieces(Player player, ArtifactSet set) {
        return countEquippedPiecesNow(player, set);
    }

    private static int countEquippedPiecesNow(Player player, ArtifactSet set) {
        if (player == null) {
            return 0;
        }

        return countEquippedSets(player).getOrDefault(set, 0);
    }

    private static EnumMap<ArtifactSet, Integer> countEquippedSets(Player player) {
        EnumMap<ArtifactSet, Integer> counts = new EnumMap<>(ArtifactSet.class);
        if (player == null) {
            return counts;
        }

        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            for (ArtifactSlot slot : ArtifactSlot.values()) {
                handler.getStacksHandler(slot.id()).ifPresent(stacksHandler -> countMatchingArtifacts(stacksHandler.getStacks(), counts));
            }
        });

        return counts;
    }

    private static void countMatchingArtifacts(IDynamicStackHandler stacks, EnumMap<ArtifactSet, Integer> counts) {
        for (int index = 0; index < stacks.getSlots(); index++) {
            ItemStack stack = stacks.getStackInSlot(index);
            if (stack.getItem() instanceof ArtifactItem artifact) {
                counts.merge(artifact.getSet(), 1, Integer::sum);
            }
        }
    }

    private static void giveStarterArtifacts(Player player) {
        if (player.level().isClientSide) {
            return;
        }

        if (!TeyvatArtifactsConfig.giveInitiateArtifactsOnFirstSpawn()) {
            return;
        }

        CompoundTag data = getPersistedData(player);
        if (data.getBoolean(STARTER_ARTIFACTS_GRANTED_KEY)) {
            return;
        }

        data.putBoolean(STARTER_ARTIFACTS_GRANTED_KEY, true);
        equipStarterArtifact(player, ArtifactSlot.FLOWER, createStarterArtifact(TeyvatArtifacts.INITIATE_FLOWER.get()));
        equipStarterArtifact(player, ArtifactSlot.FEATHER, createStarterArtifact(TeyvatArtifacts.INITIATE_FEATHER.get()));
    }

    private static ItemStack createStarterArtifact(ArtifactItem item) {
        ItemStack stack = new ItemStack(item);
        ArtifactItemData.setStars(stack, 1);
        return stack;
    }

    private static void equipStarterArtifact(Player player, ArtifactSlot slot, ItemStack stack) {
        boolean[] equipped = {false};
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> handler.getStacksHandler(slot.id()).ifPresent(stacksHandler -> {
            if (stacksHandler.getSlots() > 0 && stacksHandler.getStacks().getStackInSlot(0).isEmpty()) {
                handler.setEquippedCurio(slot.id(), 0, stack);
                equipped[0] = true;
            }
        }));

        if (!equipped[0] && !player.addItem(stack)) {
            player.drop(stack, false);
        }
    }

    private static CompoundTag getPersistedData(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        if (!persistentData.contains(Player.PERSISTED_NBT_TAG, Tag.TAG_COMPOUND)) {
            persistentData.put(Player.PERSISTED_NBT_TAG, new CompoundTag());
        }

        return persistentData.getCompound(Player.PERSISTED_NBT_TAG);
    }
}
