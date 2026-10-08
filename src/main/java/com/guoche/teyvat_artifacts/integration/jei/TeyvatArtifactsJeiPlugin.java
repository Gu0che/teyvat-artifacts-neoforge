package com.guoche.teyvat_artifacts.integration.jei;

import com.guoche.teyvat_artifacts.ArtifactItem;
import com.guoche.teyvat_artifacts.ArtifactSourceInfo;
import com.guoche.teyvat_artifacts.TeyvatArtifacts;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.vanilla.IJeiIngredientInfoRecipe;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public final class TeyvatArtifactsJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "artifact_sources");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<IJeiIngredientInfoRecipe> recipes = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            if (!(item instanceof ArtifactItem artifact)) {
                continue;
            }
            List<FormattedText> description = new ArrayList<>(ArtifactSourceInfo.artifactSources(artifact.getSet()));
            if (description.isEmpty()) {
                continue;
            }
            registration.getIngredientManager().createTypedIngredient(VanillaTypes.ITEM_STACK, new ItemStack(item), false)
                    .ifPresent(ingredient -> recipes.add(new ArtifactInfoRecipe(ingredient, description)));
        }
        registration.getIngredientManager().createTypedIngredient(VanillaTypes.ITEM_STACK,
                        new ItemStack(TeyvatArtifacts.CRYSTAL_CORE.get()))
                .ifPresent(ingredient -> recipes.add(new ArtifactInfoRecipe(ingredient,
                        List.of(Component.translatable("jei.teyvat_artifacts.crystal_core")))));
        registration.addRecipes(RecipeTypes.INFORMATION, recipes);
    }

    private record ArtifactInfoRecipe(ITypedIngredient<ItemStack> ingredient, List<FormattedText> description)
            implements IJeiIngredientInfoRecipe {
        private ArtifactInfoRecipe {
            description = List.copyOf(description);
        }

        @Override
        public List<ITypedIngredient<?>> getIngredients() {
            return List.of(ingredient);
        }

        @Override
        public List<FormattedText> getDescription() {
            return description;
        }
    }
}
