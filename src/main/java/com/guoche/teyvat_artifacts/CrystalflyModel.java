package com.guoche.teyvat_artifacts;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CrystalflyModel extends HierarchicalModel<Crystalfly> {
    private static final float DEG_TO_RAD = (float) (Math.PI / 180.0D);
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "crystalfly"),
            "main"
    );

    private final ModelPart root;
    private final ModelPart crystalflyRoot;
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public CrystalflyModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.crystalflyRoot = root.getChild("root");
        this.leftWing = this.crystalflyRoot.getChild("left_wing");
        this.rightWing = this.crystalflyRoot.getChild("right_wing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        PartDefinition crystalflyRoot = root.addOrReplaceChild(
                "root",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-0.5F, 19.5F, 0.5F, -52.5F * DEG_TO_RAD, 180.0F * DEG_TO_RAD, 0.0F)
        );
        crystalflyRoot.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -2.5F, 0.5F, 1.0F, 5.0F, 1.0F),
                PartPose.ZERO
        );
        crystalflyRoot.addOrReplaceChild(
                "left_wing",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(0.0F, -3.5F, -0.5F, 4.0F, 4.0F, 1.0F)
                        .texOffs(0, 0).addBox(0.0F, 0.5F, -0.5F, 3.0F, 3.0F, 1.0F),
                PartPose.offset(0.5F, 0.0F, 0.0F)
        );
        crystalflyRoot.addOrReplaceChild(
                "right_wing",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, -3.5F, -0.5F, 4.0F, 4.0F, 1.0F)
                        .texOffs(0, 0).addBox(-3.0F, 0.5F, -0.5F, 3.0F, 3.0F, 1.0F),
                PartPose.offset(-0.5F, 0.0F, 0.0F)
        );
        return LayerDefinition.create(meshDefinition, 16, 16);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Crystalfly entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        float phase = (ageInTicks * 0.05F) % 1.0F;
        float wingRotation = sampleWingRotation(phase);
        this.leftWing.yRot = wingRotation * DEG_TO_RAD;
        this.rightWing.yRot = -wingRotation * DEG_TO_RAD;
        if (!entity.isResting()) {
            this.crystalflyRoot.y += sampleRootBob(phase);
        }
    }

    private static float sampleWingRotation(float phase) {
        if (phase < 0.25F) {
            return Mth.lerp(phase / 0.25F, -67.5F, 0.5F);
        }
        if (phase < 0.5F) {
            return Mth.lerp((phase - 0.25F) / 0.25F, 0.5F, 57.5F);
        }
        if (phase < 0.75F) {
            return Mth.lerp((phase - 0.5F) / 0.25F, 57.5F, -0.5F);
        }
        return Mth.lerp((phase - 0.75F) / 0.25F, -0.5F, -62.5F);
    }

    private static float sampleRootBob(float phase) {
        if (phase < 0.5F) {
            return Mth.lerp(phase / 0.5F, 0.0F, 0.15F);
        }
        return Mth.lerp((phase - 0.5F) / 0.5F, 0.15F, 0.0F);
    }
}
