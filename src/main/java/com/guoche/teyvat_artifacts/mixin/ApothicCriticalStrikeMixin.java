package com.guoche.teyvat_artifacts.mixin;

import com.guoche.teyvat_artifacts.ArtifactEvents;
import dev.shadowsoffire.apothic_attributes.impl.AttributeEvents;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = AttributeEvents.class, remap = false)
public abstract class ApothicCriticalStrikeMixin {
    @ModifyVariable(method = "apothCriticalStrike", at = @At(value = "STORE", ordinal = 0), ordinal = 0, remap = false)
    private double teyvat_artifacts$adjustCritChance(double chance, LivingIncomingDamageEvent event) {
        return ArtifactEvents.adjustApothicCritChance(event.getSource(), event.getEntity(), chance);
    }

    @ModifyVariable(method = "apothCriticalStrike", at = @At(value = "STORE", ordinal = 0), ordinal = 0, remap = false)
    private float teyvat_artifacts$adjustCritDamage(float damage, LivingIncomingDamageEvent event) {
        return ArtifactEvents.adjustApothicCritDamage(event.getSource(), damage);
    }

    @Redirect(method = "apothCriticalStrike", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/event/entity/living/LivingIncomingDamageEvent;setAmount(F)V", remap = false), remap = false)
    private void teyvat_artifacts$recordCrit(LivingIncomingDamageEvent event, float amount) {
        float previous = event.getAmount();
        event.setAmount(amount);
        if (amount > previous) {
            ArtifactEvents.onApothicCritical(event.getSource(), event.getEntity());
        }
    }
}
