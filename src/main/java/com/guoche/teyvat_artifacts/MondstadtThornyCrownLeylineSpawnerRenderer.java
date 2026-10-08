package com.guoche.teyvat_artifacts;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MondstadtThornyCrownLeylineSpawnerRenderer implements BlockEntityRenderer<MondstadtThornyCrownLeylineSpawnerBlockEntity> {
    private static final int TICKS_PER_DISPLAY_ITEM = 20;
    private final ItemRenderer itemRenderer;

    public MondstadtThornyCrownLeylineSpawnerRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(MondstadtThornyCrownLeylineSpawnerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();
        int poolSize = LeylineRewardHelper.getThornyCrownDisplayPoolSize();
        if (level == null || poolSize <= 0) {
            return;
        }

        int seed = (int) blockEntity.getBlockPos().asLong();
        int index = Math.floorMod((int) (level.getGameTime() / TICKS_PER_DISPLAY_ITEM) + seed, poolSize);
        ItemStack displayStack = LeylineRewardHelper.createThornyCrownDisplayStack(index);
        if (displayStack.isEmpty()) {
            return;
        }

        float rotation = (level.getGameTime() + partialTick) * 4.0F;
        float bob = (float) Math.sin((level.getGameTime() + partialTick) / 12.0F) * 0.035F;
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.53F + bob, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.scale(0.55F, 0.55F, 0.55F);
        itemRenderer.renderStatic(displayStack, ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, level, seed);
        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(MondstadtThornyCrownLeylineSpawnerBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos()).inflate(1.0D);
    }
}
