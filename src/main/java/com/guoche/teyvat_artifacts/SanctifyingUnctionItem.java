package com.guoche.teyvat_artifacts;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Locale;

public class SanctifyingUnctionItem extends SanctifyingMaterialItem {
    public SanctifyingUnctionItem(Properties properties) {
        super(properties, 3);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack target = player.getOffhandItem();
        if (hand != InteractionHand.MAIN_HAND || !player.isShiftKeyDown()
                || !ArtifactItemData.canRerollSubstats(target)) {
            return InteractionResultHolder.pass(stack);
        }
        if (!level.isClientSide) reroll(stack, target, player);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private static void reroll(ItemStack stack, ItemStack target, Player player) {
        boolean resetMainOnly = ArtifactItemData.getSubstats(target).isEmpty()
                && ArtifactItemData.getEnhancementsUsed(target) > 0;
        List<ArtifactSubstat> rolled = ArtifactItemData.rerollSubstats(target, player.getRandom());
        if (rolled.isEmpty() && !resetMainOnly) {
            player.displayClientMessage(Component.translatable("message.teyvat_artifacts.sanctifying_unction.empty"), true);
            return;
        }
        if (!player.getAbilities().instabuild) stack.shrink(1);
        player.getInventory().setChanged();
        player.containerMenu.broadcastChanges();
        MutableComponent message = Component.translatable("message.teyvat_artifacts.sanctifying_unction.rerolled");
        for (ArtifactSubstat substat : rolled) {
            Holder<Attribute> attribute = ArtifactSubstat.resolve(substat.attributeId());
            Component name = attribute == null ? Component.literal(substat.attributeId().toString())
                    : Component.translatable(attribute.value().getDescriptionId());
            boolean percent = substat.operation() != 1 || substat.attributeId().getPath().equals("crit_chance")
                    || substat.attributeId().getPath().equals("crit_damage");
            String value = String.format(Locale.ROOT, "%+.2f%s", substat.amount() * (percent ? 100 : 1), percent ? "%" : "");
            message.append(Component.literal("\n- ")).append(name).append(Component.literal(": " + value));
        }
        player.displayClientMessage(message, false);
        player.level().playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.6F, 1.4F);
    }
}
