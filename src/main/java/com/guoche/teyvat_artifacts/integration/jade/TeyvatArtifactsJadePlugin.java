package com.guoche.teyvat_artifacts.integration.jade;

import com.guoche.teyvat_artifacts.ArtifactSourceInfo;
import com.guoche.teyvat_artifacts.TeyvatArtifacts;
import com.guoche.teyvat_artifacts.TeyvatArtifactsConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin(TeyvatArtifacts.MODID)
public final class TeyvatArtifactsJadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        for (TeyvatArtifactsConfig.LeylineSpawner spawner : TeyvatArtifactsConfig.LeylineSpawner.values()) {
            Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, spawner.id()));
            if (block != Blocks.AIR) {
                registration.registerBlockComponent(LeylineRewardsProvider.INSTANCE, block.getClass());
            }
        }
        registration.markAsClientFeature(LeylineRewardsProvider.INSTANCE.getUid());
    }

    private enum LeylineRewardsProvider implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public ResourceLocation getUid() {
            return ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "leyline_rewards");
        }

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            tooltip.addAll(ArtifactSourceInfo.leylineRewards(accessor.getBlock()));
        }
    }
}
