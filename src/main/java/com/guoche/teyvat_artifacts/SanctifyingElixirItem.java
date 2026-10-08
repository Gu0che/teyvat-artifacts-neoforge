package com.guoche.teyvat_artifacts;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SanctifyingElixirItem extends SanctifyingMaterialItem {
    public SanctifyingElixirItem(Properties properties) {
        super(properties, 5);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack target = player.getOffhandItem();
        if (hand != InteractionHand.MAIN_HAND || !ArtifactItemData.canRerollArtifactStat(target)) {
            return InteractionResultHolder.pass(stack);
        }

        if (!level.isClientSide && target.getItem() instanceof ArtifactItem artifact) {
            reroll(stack, target, artifact, player);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        ItemStack target = slot.getItem();
        if (action != ClickAction.SECONDARY || stack.isEmpty() || target.isEmpty() || !ArtifactItemData.canRerollArtifactStat(target)) {
            return false;
        }

        if (!player.level().isClientSide && target.getItem() instanceof ArtifactItem artifact) {
            reroll(stack, target, artifact, player);
            slot.setChanged();
            player.containerMenu.broadcastChanges();
        }

        return true;
    }

    private static void reroll(ItemStack stack, ItemStack target, ArtifactItem artifact, Player player) {
        ArtifactStat stat = ArtifactItemData.rerollArtifactStat(target, artifact, player.getRandom());
        if (stat == null) {
            return;
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        player.getInventory().setChanged();
        player.containerMenu.broadcastChanges();
        player.displayClientMessage(
                Component.translatable(
                        "message.teyvat_artifacts.sanctifying_elixir.rerolled",
                        stat.modifierComponent(stat.amountForStars(
                                ArtifactItemData.getStars(target), ArtifactItemData.getEnhancementsUsed(target)))
                ),
                true
        );
        player.level().playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.6F, 1.4F);
    }
}
