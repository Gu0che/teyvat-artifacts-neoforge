package com.guoche.teyvat_artifacts;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class ArtifactStarUpgradeRecipe extends CustomRecipe {
    public ArtifactStarUpgradeRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return ArtifactCraftingHelper.isArtifactUpgrade(input.items());
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return ArtifactCraftingHelper.assembleUpgradeResult(input.items());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TeyvatArtifacts.ARTIFACT_STAR_UPGRADE_RECIPE.get();
    }
}
