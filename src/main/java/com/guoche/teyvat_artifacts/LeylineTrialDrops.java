package com.guoche.teyvat_artifacts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

final class LeylineTrialDrops {
    private static final String KEY = TeyvatArtifacts.MODID + ".leyline_trial_mob";

    private LeylineTrialDrops() {
    }

    static void mark(Mob mob) {
        mob.getPersistentData().putBoolean(KEY, true);
        mob.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0, true, false, false));
        if (TeyvatArtifactsConfig.glowLeylineTrialMobs()) {
            mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 90 * 20, 0, true, false, false));
        }
        if (mob instanceof PiglinBrute brute) {
            brute.setImmuneToZombification(true);
        }
    }

    static boolean isTrialMob(LivingEntity entity) {
        return shouldSuppressDrops(entity);
    }

    static boolean shouldSuppressDrops(LivingEntity entity) {
        CompoundTag tag = entity.getPersistentData();
        return entity instanceof Mob && tag.getBoolean(KEY);
    }
}
