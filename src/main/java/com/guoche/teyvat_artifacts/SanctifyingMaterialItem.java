package com.guoche.teyvat_artifacts;

import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;

import java.util.List;

public class SanctifyingMaterialItem extends DescriptiveItem {
    private final int fixedStars;

    public SanctifyingMaterialItem(Properties properties, int fixedStars) {
        super(properties, fixedStars);
        this.fixedStars = ArtifactItemData.clampStars(fixedStars);
    }

    public int getFixedStars() {
        return fixedStars;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        String key = stack.getDescriptionId() + ".usage";
        if (Language.getInstance().has(key)) {
            tooltip.add(1, Component.translatable(key).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }
}
