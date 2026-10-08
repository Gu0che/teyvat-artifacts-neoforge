package com.guoche.teyvat_artifacts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.Direction;

public class MondstadtThornyCrownLeylineSpawnerFeature extends Feature<NoneFeatureConfiguration> {
    public MondstadtThornyCrownLeylineSpawnerFeature(com.mojang.serialization.Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        BlockPos placePos = findPlacementPos(level, origin);
        if (placePos == null) {
            return false;
        }

        BlockState state = LeylineSpawnerContent.MONDSTADT_THORNY_CROWN_LEYLINE_SPAWNER.get().defaultBlockState();
        level.setBlock(placePos, state, Block.UPDATE_ALL);
        return true;
    }

    private BlockPos findPlacementPos(WorldGenLevel level, BlockPos origin) {
        if (canPlace(level, origin)) {
            return origin;
        }

        BlockPos above = origin.above();
        if (canPlace(level, above)) {
            return above;
        }

        return null;
    }

    private boolean canPlace(WorldGenLevel level, BlockPos pos) {
        BlockState current = level.getBlockState(pos);
        BlockState below = level.getBlockState(pos.below());
        return current.canBeReplaced() && below.isFaceSturdy(level, pos.below(), Direction.UP);
    }
}
