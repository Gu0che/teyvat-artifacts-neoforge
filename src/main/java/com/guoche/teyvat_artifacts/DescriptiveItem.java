package com.guoche.teyvat_artifacts;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;

import java.util.List;

public class DescriptiveItem extends Item {
    private final int fixedStars;

    public DescriptiveItem(Properties properties) {
        this(properties, 0);
    }

    public DescriptiveItem(Properties properties, int fixedStars) {
        super(properties);
        this.fixedStars = fixedStars > 0 ? ArtifactItemData.clampStars(fixedStars) : 0;
    }

    @Override
    public Component getName(ItemStack stack) {
        Component name = super.getName(stack);
        return fixedStars > 0 ? name.copy().withStyle(ArtifactItemData.getStarColor(fixedStars)) : name;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        ArtifactTooltipHelper.appendDescription(stack, tooltip);
    }
}
