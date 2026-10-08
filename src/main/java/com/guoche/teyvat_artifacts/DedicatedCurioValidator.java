package com.guoche.teyvat_artifacts;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

/** Keeps this mod's dedicated Curios slots independent from the shared curios:curio tag. */
public final class DedicatedCurioValidator {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(
            TeyvatArtifacts.MODID, "dedicated_slot");

    private DedicatedCurioValidator() {
    }

    public static void register() {
        CuriosApi.registerCurioPredicate(ID, DedicatedCurioValidator::accepts);
    }

    private static boolean accepts(SlotResult result) {
        String slotId = result.slotContext().identifier();
        ItemStack stack = result.stack();
        if (stack.getItem() instanceof ArtifactItem artifact) {
            return artifact.getSlot().id().equals(slotId);
        }
        return "witch_gift".equals(slotId) && stack.getItem() instanceof WitchGiftItem;
    }
}
