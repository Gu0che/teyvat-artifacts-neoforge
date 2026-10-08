package com.guoche.teyvat_artifacts;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class CrystalflyRenderer extends MobRenderer<Crystalfly, CrystalflyModel> {
    private static final ResourceLocation ANEMO_TEXTURE = texture("anemo_crystalfly");
    private static final Map<EntityType<?>, ResourceLocation> TEXTURES = Map.of(
            TeyvatArtifacts.ANEMO_CRYSTALFLY.get(), ANEMO_TEXTURE,
            TeyvatArtifacts.ICE_CRYSTALFLY.get(), texture("cryo_crystalfly"),
            TeyvatArtifacts.GEO_CRYSTALFLY.get(), texture("geo_crystalfly"),
            TeyvatArtifacts.ELECTRO_CRYSTALFLY.get(), texture("electro_crystalfly"),
            TeyvatArtifacts.DENDRO_CRYSTALFLY.get(), texture("dendro_crystalfly"),
            TeyvatArtifacts.HYDRO_CRYSTALFLY.get(), texture("hydro_crystalfly"),
            TeyvatArtifacts.PYRO_CRYSTALFLY.get(), texture("pyro_crystalfly")
    );

    public CrystalflyRenderer(EntityRendererProvider.Context context) {
        super(context, new CrystalflyModel(context.bakeLayer(CrystalflyModel.LAYER_LOCATION)), 0.15F);
    }

    @Override
    public ResourceLocation getTextureLocation(Crystalfly entity) {
        return TEXTURES.getOrDefault(entity.getType(), ANEMO_TEXTURE);
    }

    private static ResourceLocation texture(String name) {
        return ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "textures/entity/crystalfly/" + name + ".png");
    }
}
