package com.guoche.teyvat_artifacts;

import net.minecraft.network.chat.Component;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Locale;

public class SanctifyingEssenceItem extends SanctifyingMaterialItem {
    public SanctifyingEssenceItem(Properties properties) {
        super(properties, 4);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack target = player.getOffhandItem();
        if (hand != InteractionHand.MAIN_HAND || !ArtifactItemData.canRerollSubstats(target)) {
            return InteractionResultHolder.pass(stack);
        }
        if (!level.isClientSide) enhance(stack, target, player);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        ItemStack target = slot.getItem();
        if (action != ClickAction.SECONDARY || stack.isEmpty() || !ArtifactItemData.canRerollSubstats(target)) return false;
        if (!player.level().isClientSide) {
            enhance(stack, target, player);
            slot.setChanged();
            player.containerMenu.broadcastChanges();
        }
        return true;
    }

    private static void enhance(ItemStack stack, ItemStack target, Player player) {
        if (ArtifactItemData.getRemainingEnhancements(target) == 0) {
            player.displayClientMessage(Component.translatable("message.teyvat_artifacts.sanctifying_essence.maxed"), true);
            return;
        }
        ArtifactItemData.EnhancementResult result = ArtifactItemData.enhanceRandomSubstat(target, player.getRandom());
        if (result == null) {
            player.displayClientMessage(Component.translatable("message.teyvat_artifacts.sanctifying_essence.empty"), true);
            return;
        }
        if (!player.getAbilities().instabuild) stack.shrink(1);
        player.getInventory().setChanged();
        player.containerMenu.broadcastChanges();
        if (result.mainStat() != null && result.mainAfter() != result.mainBefore()) {
            player.displayClientMessage(Component.translatable("message.teyvat_artifacts.sanctifying_essence.main_enhanced",
                    result.mainStat().modifierComponent(result.mainAfter() - result.mainBefore()),
                    result.mainStat().modifierComponent(result.mainAfter()), ArtifactItemData.getRemainingEnhancements(target))
                    .withStyle(net.minecraft.ChatFormatting.YELLOW), false);
        }
        if (result.after() != null) {
            ArtifactSubstat substat = result.after();
            Holder<Attribute> attribute = ArtifactSubstat.resolve(substat.attributeId());
            Component name = attribute == null ? Component.literal(substat.attributeId().toString())
                    : Component.translatable(attribute.value().getDescriptionId());
            boolean percent = substat.operation() != 1 || substat.attributeId().getPath().equals("crit_chance")
                    || substat.attributeId().getPath().equals("crit_damage");
            String increase = String.format(Locale.ROOT, "%+.2f%s", result.increase() * (percent ? 100 : 1), percent ? "%" : "");
            String total = String.format(Locale.ROOT, "%.2f%s", substat.amount() * (percent ? 100 : 1), percent ? "%" : "");
            player.displayClientMessage(Component.translatable("message.teyvat_artifacts.sanctifying_essence.enhanced",
                    name, substat.enhancementCount(), increase, total, ArtifactItemData.getRemainingEnhancements(target))
                    .withStyle(ArtifactItemData.getStarColor(substat.enhancementCount())), false);
        }
        player.level().playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.6F, 1.4F);
    }
}
