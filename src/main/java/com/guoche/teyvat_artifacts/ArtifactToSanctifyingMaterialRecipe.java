package com.guoche.teyvat_artifacts;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class ArtifactToSanctifyingMaterialRecipe extends CustomRecipe {
    public ArtifactToSanctifyingMaterialRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return ArtifactCraftingHelper.isMaterialConversion(input.items());
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return ArtifactCraftingHelper.assembleMaterialResult(input.items());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TeyvatArtifacts.ARTIFACT_TO_SANCTIFYING_MATERIAL_RECIPE.get();
    }
}
