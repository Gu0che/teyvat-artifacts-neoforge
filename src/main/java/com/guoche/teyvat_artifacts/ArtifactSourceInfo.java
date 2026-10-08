package com.guoche.teyvat_artifacts;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ArtifactSourceInfo {
    private ArtifactSourceInfo() {
    }

    public static List<Component> leylineRewards(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        if (!TeyvatArtifacts.MODID.equals(id.getNamespace())) {
            return List.of();
        }
        return describeRewards(RewardSets.BY_SPAWNER.getOrDefault(id.getPath(), List.of()));
    }

    static List<Component> describeRewards(List<ArtifactSet> sets) {
        if (sets.isEmpty()) {
            return List.of();
        }
        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable("tooltip.teyvat_artifacts.leyline_reward_sets").withStyle(ChatFormatting.GRAY));
        for (ArtifactSet set : sets) {
            lines.add(Component.translatable(set.nameKey()).withStyle(ChatFormatting.GOLD));
        }
        return List.copyOf(lines);
    }

    public static List<Component> artifactSources(ArtifactSet set) {
        List<Component> sources = new ArrayList<>();
        if (set == ArtifactSet.INITIATE) {
            sources.add(Component.translatable("jei.teyvat_artifacts.source.starter"));
        }
        if (ArtifactChestLootModifier.isChestLootSet(set)) {
            sources.add(Component.translatable("jei.teyvat_artifacts.source.chest"));
        }
        for (TeyvatArtifactsConfig.LeylineSpawner spawner : TeyvatArtifactsConfig.LeylineSpawner.values()) {
            if (RewardSets.BY_SPAWNER.getOrDefault(spawner.id(), List.of()).contains(set)) {
                sources.add(domainSource(spawner.id()));
            }
        }
        if (set == ArtifactSet.WANDERERS_TROUPE) {
            sources.add(Component.translatable("jei.teyvat_artifacts.source.first_wither"));
        } else if (set == ArtifactSet.GLADIATORS_FINALE) {
            sources.add(Component.translatable("jei.teyvat_artifacts.source.first_dragon"));
        }
        return List.copyOf(sources);
    }

    static Component domainSource(String spawnerId) {
        return Component.translatable("jei.teyvat_artifacts.source.domain",
                Component.translatable("block." + TeyvatArtifacts.MODID + "." + spawnerId));
    }

    // Resolve registered items only after registration; cache sets, never translated strings.
    private static final class RewardSets {
        private static final Map<String, List<ArtifactSet>> BY_SPAWNER = build();

        private static Map<String, List<ArtifactSet>> build() {
            Map<String, List<ArtifactSet>> result = new HashMap<>();
            for (TeyvatArtifactsConfig.LeylineSpawner spawner : TeyvatArtifactsConfig.LeylineSpawner.values()) {
                result.put(spawner.id(), LeylineRewardHelper.getRewardSets(spawner.id()));
            }
            return Map.copyOf(result);
        }
    }
}
