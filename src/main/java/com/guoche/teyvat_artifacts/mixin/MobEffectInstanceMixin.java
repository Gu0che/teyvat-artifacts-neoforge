package com.guoche.teyvat_artifacts.mixin;

import com.guoche.teyvat_artifacts.ArtifactEffectUpgrade;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin implements ArtifactEffectUpgrade {
    @Unique
    private boolean teyvat_artifacts$setBonusApplied;

    @Override
    public boolean teyvat_artifacts$isSetBonusApplied() {
        return teyvat_artifacts$setBonusApplied;
    }

    @Override
    public void teyvat_artifacts$setSetBonusApplied(boolean applied) {
        teyvat_artifacts$setBonusApplied = applied;
    }

    @Override
    @Accessor("amplifier")
    public abstract void teyvat_artifacts$setAmplifier(int amplifier);

    @Override
    @Accessor("hiddenEffect")
    public abstract MobEffectInstance teyvat_artifacts$getHiddenEffect();

    @Inject(method = "setDetailsFrom", at = @At("RETURN"))
    private void teyvat_artifacts$copyBonus(MobEffectInstance source, CallbackInfo ci) {
        teyvat_artifacts$setBonusApplied = ((ArtifactEffectUpgrade) source).teyvat_artifacts$isSetBonusApplied();
    }

    // Transfer provenance only when vanilla actually replaces the active amplifier.
    @Inject(method = "update", at = @At(value = "FIELD",
            target = "Lnet/minecraft/world/effect/MobEffectInstance;amplifier:I",
            opcode = 181, shift = At.Shift.AFTER))
    private void teyvat_artifacts$replaceBonus(MobEffectInstance source, CallbackInfoReturnable<Boolean> cir) {
        teyvat_artifacts$setBonusApplied = ((ArtifactEffectUpgrade) source).teyvat_artifacts$isSetBonusApplied();
    }

    @Inject(method = "update", at = @At("RETURN"))
    private void teyvat_artifacts$mergeBonus(MobEffectInstance source, CallbackInfoReturnable<Boolean> cir) {
        MobEffectInstance self = (MobEffectInstance) (Object) this;
        if (self.getAmplifier() == source.getAmplifier()
                && ((ArtifactEffectUpgrade) source).teyvat_artifacts$isSetBonusApplied()) {
            teyvat_artifacts$setBonusApplied = true;
        }
    }

    @Inject(method = "save", at = @At("RETURN"))
    private void teyvat_artifacts$saveBonus(CallbackInfoReturnable<Tag> cir) {
        if (cir.getReturnValue() instanceof CompoundTag tag) {
            ArtifactEffectUpgrade.write((MobEffectInstance) (Object) this, tag, "hidden_effect");
        }
    }

    @Inject(method = "load", at = @At("RETURN"))
    private static void teyvat_artifacts$loadBonus(CompoundTag tag, CallbackInfoReturnable<MobEffectInstance> cir) {
        ArtifactEffectUpgrade.read(cir.getReturnValue(), tag, "hidden_effect");
    }
}
