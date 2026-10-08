package com.guoche.teyvat_artifacts;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class AlchemyTableScreen extends AbstractContainerScreen<AlchemyTableMenu> {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(
            TeyvatArtifacts.MODID, "textures/gui/alchemy_table.png");
    private Button transmute;

    public AlchemyTableScreen(AlchemyTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 222;
        inventoryLabelY = 122;
    }

    @Override
    protected void init() {
        super.init();
        transmute = addRenderableWidget(Button.builder(Component.translatable("gui.teyvat_artifacts.alchemy"),
                button -> {
                    if (minecraft != null && minecraft.gameMode != null) {
                        minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0);
                    }
                }).bounds(leftPos + 8, topPos + 82, 70, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        transmute.active = menu.canTransmute();
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
        for (int tier = 0; tier < 3; tier++) {
            int x = leftPos + 98 + tier * 24;
            if (!menu.getSlot(AlchemyTableBlockEntity.INPUT_SLOTS + tier).hasItem()
                    && mouseX >= x && mouseX < x + 16 && mouseY >= topPos + 84 && mouseY < topPos + 100) {
                graphics.renderTooltip(font, material(tier), mouseX, mouseY);
            }
        }
    }

    private static ItemStack material(int tier) {
        return new ItemStack(switch (tier) {
            case 0 -> TeyvatArtifacts.SANCTIFYING_UNCTION.get();
            case 1 -> TeyvatArtifacts.SANCTIFYING_ESSENCE.get();
            default -> TeyvatArtifacts.SANCTIFYING_ELIXIR.get();
        });
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(BACKGROUND, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        for (int tier = 0; tier < 3; tier++) {
            if (!menu.getSlot(AlchemyTableBlockEntity.INPUT_SLOTS + tier).hasItem()) {
                int x = leftPos + 98 + tier * 24;
                graphics.renderItem(material(tier), x, topPos + 84);
                graphics.fill(x, topPos + 84, x + 16, topPos + 100, 0xA0C6C6C6);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        Component mode = Component.translatable(menu.isPowered()
                ? "gui.teyvat_artifacts.alchemy_automatic" : "gui.teyvat_artifacts.alchemy_manual");
        graphics.drawString(font, mode, 8, 110, 0x404040, false);
    }
}
