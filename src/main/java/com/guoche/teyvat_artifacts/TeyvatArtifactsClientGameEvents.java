package com.guoche.teyvat_artifacts;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;

@EventBusSubscriber(modid = TeyvatArtifacts.MODID, value = Dist.CLIENT)
public final class TeyvatArtifactsClientGameEvents {
    private static final ResourceLocation[] STAR_BACKGROUNDS = new ResourceLocation[]{
            null,
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "textures/slot/star_background_1.png"),
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "textures/slot/star_background_2.png"),
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "textures/slot/star_background_3.png"),
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "textures/slot/star_background_4.png"),
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "textures/slot/star_background_5.png")
    };

    private TeyvatArtifactsClientGameEvents() {
    }

    @SubscribeEvent
    public static void renderStarBackgrounds(ContainerScreenEvent.Render.Background event) {
        AbstractContainerScreen<?> screen = event.getContainerScreen();
        int left = screen.getGuiLeft();
        int top = screen.getGuiTop();

        for (Slot slot : screen.getMenu().slots) {
            if (!slot.isActive() || !slot.hasItem()) {
                continue;
            }

            ItemStack stack = slot.getItem();
            ResourceLocation background = getStarBackground(getDisplayStars(stack));
            if (background != null) {
                event.getGuiGraphics().blit(
                        background,
                        left + slot.x,
                        top + slot.y,
                        0.0F,
                        0.0F,
                        16,
                        16,
                        16,
                        16
                );
            }
        }
    }

    private static ResourceLocation getStarBackground(int stars) {
        return stars >= ArtifactItemData.MIN_STARS && stars <= ArtifactItemData.MAX_STARS
                ? STAR_BACKGROUNDS[stars]
                : null;
    }

    private static int getDisplayStars(ItemStack stack) {
        int stars = ArtifactItemData.getDisplayStars(stack);
        if (stars > 0) {
            return stars;
        }
        if (stack.getItem() instanceof SanctifyingMaterialItem material) {
            return material.getFixedStars();
        }
        if (stack.is(TeyvatArtifacts.CONDENSED_RESIN.get())) {
            return 4;
        }
        if (stack.is(TeyvatArtifacts.CRYSTAL_CORE.get())) {
            return 3;
        }
        if (stack.is(TeyvatArtifacts.WITCHS_REVELATION_CASE.get()) || stack.is(TeyvatArtifacts.THE_LITTLE_WITCHS_DICTIONARY.get())) {
            return 5;
        }
        return 0;
    }
}
