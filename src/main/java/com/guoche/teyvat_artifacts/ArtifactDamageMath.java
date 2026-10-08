package com.guoche.teyvat_artifacts;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;

import java.util.Optional;

public final class ArtifactDamageMath {
    private static final TagKey<DamageType> TACZ_BULLETS = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("tacz", "bullets"));
    private static final TagKey<DamageType> FORGE_MAGIC = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("forge", "is_magic"));
    private static final TagKey<DamageType> NEOFORGE_MAGIC = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("neoforge", "is_magic"));
    private static final TagKey<DamageType> MAGIC_DAMAGE = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "magic_damage"));

    public enum SpellSchool {
        LIGHTNING("lightning", "irons_spellbooks", "lightning_spell_power"),
        FIRE("fire", "irons_spellbooks", "fire_spell_power"),
        ICE("ice", "irons_spellbooks", "ice_spell_power"),
        AQUA("aqua", "traveloptics", "aqua_spell_power"),
        NATURE("nature", "irons_spellbooks", "nature_spell_power"),
        EVOCATION("evocation", "irons_spellbooks", "evocation_spell_power"),
        ELDRITCH("eldritch", "irons_spellbooks", "eldritch_spell_power"),
        ENDER("ender", "irons_spellbooks", "ender_spell_power"),
        HOLY("holy", "irons_spellbooks", "holy_spell_power"),
        BLOOD("blood", "irons_spellbooks", "blood_spell_power"),
        ABYSSAL("abyssal", "cataclysm_spellbooks", "abyssal_spell_power"),
        GEO("geo", "gtbcs_geomancy_plus", "geo_spell_power"),
        SOUND("sound", "familiarslib", "sound_spell_power"),
        TECHNOMANCY("technomancy", "cataclysm_spellbooks", "technomancy_spell_power"),
        WIND("wind", "wind_spellbooks", "wind_spell_power"),
        SPIRIT_MEDIUMSHIP("spirit_mediumship", "spirit_mediumship_spell", "spirit_mediumship_spell_power"),
        ESOTERIC("esoteric", "esoteric_spell", "esoteric_spell_power"),
        ARS_NOUVEAU("ars_nouveau", "ars_nouveau_spellbooks", "ars_nouveau_spell_power"),
        JUJUTSU("jujutsu", "jujutsu_spelling", "jujutsu_spell_power"),
        ANNIHILATION("annihilation", "legendary_spellbooks", "annihilation_power");

        private final TagKey<DamageType> damageTypeTag;
        private final ResourceLocation spellPowerAttributeId;

        SpellSchool(String tagPath, String attributeNamespace, String attributePath) {
            this.damageTypeTag = TagKey.create(
                    Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, tagPath)
            );
            this.spellPowerAttributeId = ResourceLocation.fromNamespaceAndPath(attributeNamespace, attributePath);
        }

        public TagKey<DamageType> damageTypeTag() {
            return damageTypeTag;
        }

        public ResourceLocation spellPowerAttributeId() {
            return spellPowerAttributeId;
        }
    }

    private ArtifactDamageMath() {
    }

    public static Optional<Holder.Reference<Attribute>> resolveSpellPowerAttribute(SpellSchool school) {
        if (school == null) {
            return Optional.empty();
        }
        return BuiltInRegistries.ATTRIBUTE.getHolder(
                ResourceKey.create(Registries.ATTRIBUTE, school.spellPowerAttributeId())
        );
    }

    public static float applyOutgoingDamageMultiplier(DamageSource source, float amount) {
        Player player = resolvePlayer(source);
        if (player == null) {
            return amount;
        }

        return amount * getOutgoingDamageMultiplier(player);
    }

    public static float getOutgoingDamageMultiplier(Player player) {
        return Math.max(0.0F, (float) player.getAttributeValue(TeyvatArtifacts.DAMAGE_BONUS));
    }

    public static float applyIncomingDamageReduction(Player player, float amount) {
        float reduction = Mth.clamp((float) player.getAttributeValue(TeyvatArtifacts.DAMAGE_REDUCTION) - 1.0F, 0.0F, 0.95F);
        return amount * (1.0F - reduction);
    }

    public static float applyArmorIgnore(DamageSource source, LivingEntity target, float amount, float preArmorAmount) {
        return applyArmorIgnore(source, target, amount, preArmorAmount, 0.0F);
    }

    public static float applyArmorIgnore(DamageSource source, LivingEntity target, float amount, float preArmorAmount, float extraArmorIgnore) {
        Player player = resolvePlayer(source);
        if (player == null || !canApplyArmorIgnore(source, target, amount)) {
            return amount;
        }

        float armorIgnore = Mth.clamp(getArmorIgnore(player) + extraArmorIgnore, 0.0F, 1.0F);
        float armor = target.getArmorValue();
        if (armorIgnore <= 0.0F || armor <= 0.0F || preArmorAmount <= 0.0F || amount <= 0.0F) {
            return amount;
        }

        float toughness = (float) target.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
        float fullArmorDamage = getDamageAfterArmor(preArmorAmount, armor, toughness);
        if (fullArmorDamage <= 0.0F) {
            return amount;
        }

        float piercedArmorDamage = getDamageAfterArmor(preArmorAmount, armor * (1.0F - armorIgnore), toughness * (1.0F - armorIgnore));
        float multiplier = piercedArmorDamage / fullArmorDamage;
        return Math.max(amount, amount * multiplier);
    }

    public static float getArmorIgnore(Player player) {
        return Mth.clamp((float) player.getAttributeValue(TeyvatArtifacts.ARMOR_IGNORE) - 1.0F, 0.0F, 1.0F);
    }

    public static boolean shouldTrackArmorIgnore(DamageSource source, LivingEntity target, float amount) {
        return shouldTrackArmorIgnore(source, target, amount, 0.0F);
    }

    public static boolean shouldTrackArmorIgnore(DamageSource source, LivingEntity target, float amount, float extraArmorIgnore) {
        Player player = resolvePlayer(source);
        return player != null && getArmorIgnore(player) + extraArmorIgnore > 0.0F && canApplyArmorIgnore(source, target, amount);
    }

    private static boolean canApplyArmorIgnore(DamageSource source, LivingEntity target, float amount) {
        return source != null
                && target != null
                && amount > 0.0F
                && target.getArmorValue() > 0
                && !source.is(DamageTypeTags.BYPASSES_ARMOR);
    }

    public static Player resolvePlayer(DamageSource source) {
        if (source == null) {
            return null;
        }

        if (source.getEntity() instanceof Player player) {
            return player;
        }

        Entity directEntity = source.getDirectEntity();
        if (directEntity instanceof Player player) {
            return player;
        }

        if (directEntity instanceof Projectile projectile && projectile.getOwner() instanceof Player player) {
            return player;
        }

        return null;
    }

    public static boolean isProjectileDamage(DamageSource source) {
        return source != null && (source.getDirectEntity() instanceof Projectile || source.is(DamageTypeTags.IS_PROJECTILE) || source.is(TACZ_BULLETS) || isTaczBulletDamage(source));
    }

    public static boolean isMeleeDamage(DamageSource source) {
        Player player = resolvePlayer(source);
        return player != null && !isProjectileDamage(source) && !isSpellDamage(source) && source.getDirectEntity() == player;
    }

    public static boolean isSpellDamage(DamageSource source) {
        if (source == null) {
            return false;
        }

        return source.is(DamageTypes.MAGIC)
                || source.is(DamageTypes.INDIRECT_MAGIC)
                || source.is(FORGE_MAGIC)
                || source.is(NEOFORGE_MAGIC)
                || source.is(MAGIC_DAMAGE);
    }

    public static boolean isSpellDamageOfSchool(DamageSource source, SpellSchool school) {
        return source != null && school != null && source.is(school.damageTypeTag());
    }

    private static boolean isTaczBulletDamage(DamageSource source) {
        ResourceLocation damageTypeId = getDamageTypeId(source);
        return damageTypeId != null && "tacz".equals(damageTypeId.getNamespace()) && damageTypeId.getPath().startsWith("bullet");
    }

    private static ResourceLocation getDamageTypeId(DamageSource source) {
        return source.typeHolder().unwrapKey().map(ResourceKey::location).orElse(null);
    }

    public static float applyLuckyReduction(float amount, boolean hasLuckyTwoPiece, float reduction) {
        return hasLuckyTwoPiece ? amount * Math.max(0.0F, 1.0F - reduction) : amount;
    }

    private static float getDamageAfterArmor(float damage, float armor, float toughness) {
        float armorCurve = 2.0F + toughness / 4.0F;
        float effectiveArmor = Mth.clamp(armor - damage / armorCurve, armor * 0.2F, 20.0F);
        return damage * (1.0F - effectiveArmor / 25.0F);
    }
}
