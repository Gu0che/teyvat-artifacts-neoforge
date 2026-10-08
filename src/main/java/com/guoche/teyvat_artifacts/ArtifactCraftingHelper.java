package com.guoche.teyvat_artifacts;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class ArtifactCraftingHelper {
    private ArtifactCraftingHelper() {
    }

    public static boolean isMaterialConversion(List<ItemStack> input) {
        return !assembleMaterialResult(input).isEmpty();
    }

    public static ItemStack assembleMaterialResult(List<ItemStack> input) {
        List<ItemStack> stacks = nonEmpty(input);
        if (stacks.size() != 1 || !(stacks.getFirst().getItem() instanceof ArtifactItem)) {
            return ItemStack.EMPTY;
        }

        return switch (ArtifactItemData.getStars(stacks.getFirst())) {
            case 2 -> new ItemStack(TeyvatArtifacts.SANCTIFYING_UNCTION.get());
            case 3 -> new ItemStack(TeyvatArtifacts.SANCTIFYING_UNCTION.get(), 2);
            case 4 -> new ItemStack(TeyvatArtifacts.SANCTIFYING_ESSENCE.get());
            case 5 -> new ItemStack(TeyvatArtifacts.SANCTIFYING_ESSENCE.get(), 2);
            default -> ItemStack.EMPTY;
        };
    }

    public static boolean isArtifactUpgrade(List<ItemStack> input) {
        return !assembleUpgradeResult(input).isEmpty();
    }

    public static ItemStack assembleUpgradeResult(List<ItemStack> input) {
        List<ItemStack> stacks = nonEmpty(input);
        if (stacks.size() != 3 || !(stacks.getFirst().getItem() instanceof ArtifactItem artifact)) {
            return ItemStack.EMPTY;
        }

        Item item = stacks.getFirst().getItem();
        int stars = ArtifactItemData.getStars(stacks.getFirst());
        if (stars <= 0 || stars >= artifact.getSet().maxStars()) {
            return ItemStack.EMPTY;
        }

        for (ItemStack stack : stacks) {
            if (stack.getItem() != item || ArtifactItemData.getStars(stack) != stars) {
                return ItemStack.EMPTY;
            }
        }

        ItemStack result = new ItemStack(item);
        ArtifactItemData.setStars(result, stars + 1);
        ArtifactItemData.clearArtifactStat(result);
        return result;
    }

    private static List<ItemStack> nonEmpty(List<ItemStack> input) {
        List<ItemStack> stacks = new ArrayList<>();
        for (ItemStack stack : input) {
            if (!stack.isEmpty()) {
                stacks.add(stack);
            }
        }
        return stacks;
    }
}
