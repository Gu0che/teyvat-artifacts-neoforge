package com.guoche.teyvat_artifacts;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CrystalCoreItem extends DescriptiveItem {
    public CrystalCoreItem(Properties properties) {
        super(properties, 3);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.sidedSuccess(stack, true);
        }

        int xpCost = LeylineRewardHelper.getCrystalCoreXpCost();
        if (!player.getAbilities().instabuild && player.totalExperience < xpCost) {
            player.displayClientMessage(Component.translatable("message.teyvat_artifacts.crystal_core.not_enough_xp"), true);
            return InteractionResultHolder.fail(stack);
        }

        if (!player.getAbilities().instabuild) {
            player.giveExperiencePoints(-xpCost);
            stack.shrink(1);
        }

        ItemStack resin = new ItemStack(TeyvatArtifacts.CONDENSED_RESIN.get());
        if (!player.getInventory().add(resin)) {
            player.drop(resin, false);
        }
        player.level().playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.7F, 1.2F);
        return InteractionResultHolder.sidedSuccess(stack, false);
    }
}
