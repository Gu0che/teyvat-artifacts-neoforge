package com.guoche.teyvat_artifacts;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class ArtifactTooltipHelper {
    private static final String DESCRIPTION_KEY_SUFFIX = ".artifact_desc";

    private ArtifactTooltipHelper() {
    }

    public static void appendDescription(ItemStack stack, List<Component> tooltip) {
        String key = stack.getDescriptionId() + DESCRIPTION_KEY_SUFFIX;
        Language language = Language.getInstance();
        if (!language.has(key)) {
            return;
        }

        // Direct tooltip callers may supply no item-name line.
        int insertIndex = Math.min(1, tooltip.size());
        if (!Screen.hasShiftDown()) {
            tooltip.add(insertIndex, Component.translatable("tooltip.teyvat_artifacts.hold_shift_for_description")
                    .withStyle(ChatFormatting.DARK_GRAY));
            return;
        }

        for (String line : language.getOrDefault(key).split("\\n")) {
            tooltip.add(insertIndex++, Component.literal(line).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }
}
