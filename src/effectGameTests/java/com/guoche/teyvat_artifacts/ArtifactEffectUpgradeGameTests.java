package com.guoche.teyvat_artifacts;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import top.theillusivec4.curios.api.CuriosApi;

@GameTestHolder(TeyvatArtifacts.MODID)
@PrefixGameTestTemplate(false)
public class ArtifactEffectUpgradeGameTests {
    private static final String TEMPLATE = "effect_upgrade_empty";

    private static Player equipped(GameTestHelper helper, boolean healing) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Item flower = healing ? TeyvatArtifacts.MAIDEN_BELOVED_FLOWER.get() : TeyvatArtifacts.RETRACING_BOLIDE_FLOWER.get();
        Item feather = healing ? TeyvatArtifacts.MAIDEN_BELOVED_FEATHER.get() : TeyvatArtifacts.RETRACING_BOLIDE_FEATHER.get();
        var inventory = CuriosApi.getCuriosInventory(player).orElseThrow();
        inventory.getStacksHandler("flower").orElseThrow().getStacks().setStackInSlot(0, new ItemStack(flower));
        inventory.getStacksHandler("feather").orElseThrow().getStacks().setStackInSlot(0, new ItemStack(feather));
        return player;
    }

    private static CompoundTag save(MobEffectInstance effect) {
        return (CompoundTag) effect.save();
    }

    @GameTest(template = TEMPLATE)
    public static void freshResistanceAndRefresh(GameTestHelper helper) {
        Player player = equipped(helper, false);
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0));
        helper.assertTrue(player.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() == 1, "First grant must be resistance II");
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0));
        MobEffectInstance effect = player.getEffect(MobEffects.DAMAGE_RESISTANCE);
        helper.assertTrue(effect.getAmplifier() == 1 && effect.getDuration() == 400, "Fresh refresh must remain II and extend duration");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE)
    public static void selfCopyWhileIterating(GameTestHelper helper) {
        Player player = equipped(helper, false);
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0));
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0));
        // Ensure an iterator still has a next element after resistance is replayed,
        // independent of the HashMap's order (a last-entry mutation could go unnoticed).
        var iterator = player.getActiveEffects().iterator();
        iterator.next();
        player.addEffect(new MobEffectInstance(player.getEffect(MobEffects.DAMAGE_RESISTANCE)));
        iterator.next();
        for (int copy = 0; copy < 30; copy++) {
            for (MobEffectInstance effect : player.getActiveEffects()) {
                player.addEffect(new MobEffectInstance(effect));
            }
        }
        helper.assertTrue(player.getActiveEffects().size() == 3, "Self-copy must retain every effect without invalidating the iterator");
        helper.assertTrue(player.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() == 1, "30 replays must not raise resistance");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE)
    public static void copyAndSaveIntoDifferentPlayer(GameTestHelper helper) {
        Player original = equipped(helper, false);
        original.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0));
        MobEffectInstance copy = new MobEffectInstance(original.getEffect(MobEffects.DAMAGE_RESISTANCE));
        Player target = equipped(helper, false);
        target.addEffect(copy);
        helper.assertTrue(target.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() == 1, "Copy into another equipped player must remain II");
        CompoundTag saved = save(copy);
        helper.assertTrue(saved.getBoolean(ArtifactEffectUpgrade.APPLIED_TAG), "Save must contain provenance");
        Player restored = equipped(helper, false);
        restored.addEffect(MobEffectInstance.load(saved));
        helper.assertTrue(restored.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() == 1, "Saved/restored effect must remain II");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE)
    public static void legacyExactReplayPreservesLevel(GameTestHelper helper) {
        Player player = equipped(helper, false);
        // Simulate an already active old effect with no way to recover its original level.
        MobEffectInstance legacy = new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 12);
        player.getActiveEffectsMap().put(legacy.getEffect(), legacy);
        for (MobEffectInstance effect : player.getActiveEffects()) {
            player.addEffect(new MobEffectInstance(effect));
        }
        helper.assertTrue(player.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() == 12, "Legacy XIII must neither increase nor be guessed downwards");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE)
    public static void healingBonusAlsoSurvivesReplay(GameTestHelper helper) {
        Player player = equipped(helper, true);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 0));
        helper.assertTrue(player.getEffect(MobEffects.REGENERATION).getAmplifier() == 2, "Maiden bonus must grant regeneration III");
        for (int i = 0; i < 20; i++) {
            for (MobEffectInstance effect : player.getActiveEffects()) {
                player.addEffect(new MobEffectInstance(effect));
            }
        }
        helper.assertTrue(player.getEffect(MobEffects.REGENERATION).getAmplifier() == 2, "Healing replays must not stack");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE)
    public static void hiddenEffectAndMergeProvenance(GameTestHelper helper) {
        MobEffectInstance lasting = new MobEffectInstance(MobEffects.REGENERATION, 400, 0);
        ArtifactEffectUpgrade.apply(lasting, 1);
        MobEffectInstance temporary = new MobEffectInstance(MobEffects.REGENERATION, 3, 3);
        ArtifactEffectUpgrade.apply(temporary, 1);
        lasting.update(temporary);
        MobEffectInstance restored = MobEffectInstance.load(save(lasting));
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        for (int i = 0; i < 3; i++) {
            restored.tick(player, () -> {});
        }
        helper.assertTrue(restored.getAmplifier() == 1, "Hidden lasting effect must resume after temporary effect");
        ArtifactEffectUpgrade.apply(restored, 1);
        helper.assertTrue(restored.getAmplifier() == 1, "Hidden provenance must survive NBT and expiration");
        restored.update(new MobEffectInstance(MobEffects.REGENERATION, 500, 5));
        helper.assertTrue(!((ArtifactEffectUpgrade) restored).teyvat_artifacts$isSetBonusApplied(), "Unboosted stronger replacement must reset provenance");
        ArtifactEffectUpgrade.apply(restored, 1);
        helper.assertTrue(restored.getAmplifier() == 6, "A new stronger unboosted effect must still qualify");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE)
    public static void disabledBonusAndAmplifierLimit(GameTestHelper helper) {
        MobEffectInstance effect = new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 0, true, false, false);
        effect.getCures().clear();
        ArtifactEffectUpgrade.apply(effect, 0);
        helper.assertTrue(effect.getAmplifier() == 0 && !((ArtifactEffectUpgrade) effect).teyvat_artifacts$isSetBonusApplied(), "Zero bonus must do nothing");
        ArtifactEffectUpgrade.apply(effect, Integer.MAX_VALUE);
        helper.assertTrue(effect.getCures().isEmpty(), "Custom cure data must survive upgrade");
        helper.assertTrue(effect.getAmplifier() == 255, "Huge config values must clamp without integer overflow");
        helper.assertTrue(effect.getDuration() == -1 && effect.isAmbient() && !effect.isVisible() && !effect.showIcon(), "Duration and display flags must survive upgrade");
        helper.succeed();
    }
}
