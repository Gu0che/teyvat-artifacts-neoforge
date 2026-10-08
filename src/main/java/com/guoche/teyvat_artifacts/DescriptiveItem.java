package com.guoche.teyvat_artifacts;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;

import java.util.List;

public class DescriptiveItem extends Item {
    private static final String DESCRIPTION_KEY_SUFFIX = ".artifact_desc";
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
        appendDescription(stack, tooltip);
    }

    private static void appendDescription(ItemStack stack, List<Component> tooltip) {
        String key = stack.getDescriptionId() + DESCRIPTION_KEY_SUFFIX;
        if (!Language.getInstance().has(key)) {
            return;
        }

        if (!Screen.hasShiftDown()) {
            tooltip.add(1, Component.translatable("tooltip.teyvat_artifacts.hold_shift_for_description").withStyle(ChatFormatting.DARK_GRAY));
            return;
        }

        String description = Language.getInstance().getOrDefault(key);
        int insertIndex = 1;
        for (String line : description.split("\\n")) {
            tooltip.add(insertIndex++, Component.literal(line).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }
}
