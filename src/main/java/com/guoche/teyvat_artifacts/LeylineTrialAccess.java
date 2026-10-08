package com.guoche.teyvat_artifacts;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.BooleanSupplier;

public final class LeylineTrialAccess {
    private LeylineTrialAccess() {
    }

    public static boolean tryStart(Player player, ItemStack heldStack, BooleanSupplier startTrial) {
        if (TeyvatArtifactsConfig.REQUIRE_LEYLINE_ADVANCEMENTS.get()) {
            if (!(player instanceof ServerPlayer serverPlayer) || !hasRequiredAdvancements(serverPlayer)) {
                player.displayClientMessage(Component.translatable("message.teyvat_artifacts.leyline.locked"), true);
                return true;
            }
        }

        if (TeyvatArtifactsConfig.REQUIRE_LEYLINE_ACTIVATION_ITEM.get() && !matchesActivationItem(heldStack)) {
            player.displayClientMessage(Component.translatable("message.teyvat_artifacts.leyline.requires_item",
                    TeyvatArtifactsConfig.LEYLINE_ACTIVATION_ITEM.get()), true);
            return true;
        }

        boolean started = startTrial.getAsBoolean();
        if (started && TeyvatArtifactsConfig.REQUIRE_LEYLINE_ACTIVATION_ITEM.get()
                && TeyvatArtifactsConfig.CONSUME_LEYLINE_ACTIVATION_ITEM.get()
                && !player.getAbilities().instabuild) {
            heldStack.shrink(1);
        }
        return started;
    }

    private static boolean hasRequiredAdvancements(ServerPlayer player) {
        for (String raw : TeyvatArtifactsConfig.LEYLINE_REQUIRED_ADVANCEMENTS.get()) {
            ResourceLocation id = ResourceLocation.tryParse(raw);
            AdvancementHolder advancement = id == null ? null : player.server.getAdvancements().get(id);
            if (advancement == null || !player.getAdvancements().getOrStartProgress(advancement).isDone()) {
                return false;
            }
        }
        return true;
    }

    private static boolean matchesActivationItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
        String selector = TeyvatArtifactsConfig.LEYLINE_ACTIVATION_ITEM.get();
        boolean tag = selector.startsWith("#");
        ResourceLocation id = ResourceLocation.tryParse(tag ? selector.substring(1) : selector);
        if (id == null) return false;
        return tag ? stack.is(TagKey.create(Registries.ITEM, id)) : BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(id);
    }
}
