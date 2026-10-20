package com.guoche.teyvat_artifacts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;

/** Server-side provenance for a set bonus, kept with the effect through copies and saves. */
public interface ArtifactEffectUpgrade {
    String APPLIED_TAG = "teyvat_artifacts:set_bonus_applied";

    boolean teyvat_artifacts$isSetBonusApplied();

    void teyvat_artifacts$setSetBonusApplied(boolean applied);

    void teyvat_artifacts$setAmplifier(int amplifier);

    MobEffectInstance teyvat_artifacts$getHiddenEffect();

    static void apply(MobEffectInstance effect, int bonus) {
        ArtifactEffectUpgrade state = (ArtifactEffectUpgrade) effect;
        if (bonus <= 0 || state.teyvat_artifacts$isSetBonusApplied()) {
            return;
        }
        // Match the vanilla 1.21 amplifier limit without reducing legacy effects above it.
        if (effect.getAmplifier() < 255) {
            state.teyvat_artifacts$setAmplifier((int) Math.min(255L, (long) effect.getAmplifier() + bonus));
        }
        state.teyvat_artifacts$setSetBonusApplied(true);
    }

    static void write(MobEffectInstance effect, CompoundTag tag, String hiddenKey) {
        ArtifactEffectUpgrade state = (ArtifactEffectUpgrade) effect;
        if (state.teyvat_artifacts$isSetBonusApplied()) {
            tag.putBoolean(APPLIED_TAG, true);
        } else {
            tag.remove(APPLIED_TAG);
        }
        MobEffectInstance hidden = state.teyvat_artifacts$getHiddenEffect();
        if (hidden != null && tag.contains(hiddenKey, 10)) {
            write(hidden, tag.getCompound(hiddenKey), hiddenKey);
        }
    }

    static void read(MobEffectInstance effect, CompoundTag tag, String hiddenKey) {
        if (effect == null) {
            return;
        }
        ArtifactEffectUpgrade state = (ArtifactEffectUpgrade) effect;
        state.teyvat_artifacts$setSetBonusApplied(tag.getBoolean(APPLIED_TAG));
        MobEffectInstance hidden = state.teyvat_artifacts$getHiddenEffect();
        if (hidden != null && tag.contains(hiddenKey, 10)) {
            read(hidden, tag.getCompound(hiddenKey), hiddenKey);
        }
    }
}
