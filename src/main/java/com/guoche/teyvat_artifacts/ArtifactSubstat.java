package com.guoche.teyvat_artifacts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record ArtifactSubstat(ResourceLocation attributeId, double amount, int operation, double base, int enhancementCount) {
    public static final Codec<ArtifactSubstat> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("attribute").forGetter(ArtifactSubstat::attributeId),
            Codec.DOUBLE.fieldOf("amount").forGetter(ArtifactSubstat::amount),
            Codec.INT.fieldOf("operation").forGetter(ArtifactSubstat::operation),
            Codec.DOUBLE.optionalFieldOf("base", 0.0D).forGetter(ArtifactSubstat::base),
            Codec.INT.optionalFieldOf("enhancement_count", 0).forGetter(ArtifactSubstat::enhancementCount)
    ).apply(instance, ArtifactSubstat::new));

    public ArtifactSubstat {
        enhancementCount = Math.max(0, enhancementCount);
    }

    public ArtifactSubstat(ResourceLocation attributeId, double amount, int operation, double base) {
        this(attributeId, amount, operation, base, 0);
    }

    private record PoolEntry(ResourceLocation attributeId, double base, int operation) {
    }

    public static boolean isValidPoolEntry(String entry) {
        return parse(entry) != null;
    }

    private static PoolEntry parse(String entry) {
        String[] parts = entry.split(",", -1);
        if (parts.length != 3) return null;
        ResourceLocation id = ResourceLocation.tryParse(parts[0].trim());
        if (id == null || id.equals(ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "display_damage_bonus"))) return null;
        try {
            double base = Double.parseDouble(parts[1].trim());
            int operation = Integer.parseInt(parts[2].trim());
            return Double.isFinite(base) && operation >= 1 && operation <= 3
                    ? new PoolEntry(id, base, operation) : null;
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public static Holder<Attribute> resolve(ResourceLocation id) {
        return BuiltInRegistries.ATTRIBUTE.getHolder(ResourceKey.create(Registries.ATTRIBUTE, id)).orElse(null);
    }

    public AttributeModifier.Operation modifierOperation() {
        return switch (operation) {
            case 1 -> AttributeModifier.Operation.ADD_VALUE;
            case 2 -> AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
            case 3 -> AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
            default -> AttributeModifier.Operation.ADD_VALUE;
        };
    }

    public static List<ArtifactSubstat> roll(int stars, RandomSource random) {
        int index = ArtifactItemData.clampStars(stars) - 1;
        int baseCount = configuredInt(TeyvatArtifactsConfig.SUBSTAT_COUNTS, index, new int[]{1, 1, 2, 2, 3});
        int extra = configuredInt(TeyvatArtifactsConfig.SUBSTAT_COUNT_BONUSES, index, new int[]{1, 1, 1, 1, 1});
        int count = Math.min(32, baseCount + random.nextInt(Math.min(16, Math.max(0, extra)) + 1));
        List<PoolEntry> pool = new ArrayList<>();
        try {
            for (String raw : TeyvatArtifactsConfig.SUBSTAT_POOL.get()) {
                PoolEntry entry = parse(raw);
                if (entry != null && resolve(entry.attributeId()) != null) pool.add(entry);
            }
        } catch (IllegalStateException ignored) {
            return List.of();
        }
        if (pool.isEmpty()) return List.of();
        List<ArtifactSubstat> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            PoolEntry entry = pool.get(random.nextInt(pool.size()));
            result.add(new ArtifactSubstat(entry.attributeId(), entry.base() * rollFactor(stars, random), entry.operation(), entry.base()));
        }
        return mergeDuplicates(List.copyOf(result));
    }


    private record StatKey(ResourceLocation attributeId, int operation) {
    }

    public static List<ArtifactSubstat> mergeDuplicates(List<ArtifactSubstat> substats) {
        boolean hasDuplicates = false;
        for (int i = 1; i < substats.size() && !hasDuplicates; i++) {
            ArtifactSubstat current = substats.get(i);
            for (int j = 0; j < i; j++) {
                ArtifactSubstat previous = substats.get(j);
                if (current.attributeId().equals(previous.attributeId()) && current.operation() == previous.operation()) {
                    hasDuplicates = true;
                    break;
                }
            }
        }
        if (!hasDuplicates) return substats;

        Map<StatKey, ArtifactSubstat> merged = new LinkedHashMap<>();
        for (ArtifactSubstat current : substats) {
            StatKey key = new StatKey(current.attributeId(), current.operation());
            ArtifactSubstat previous = merged.get(key);
            if (previous == null) {
                merged.put(key, current);
                continue;
            }
            // Separate total multipliers compose rather than add.
            double amount = current.operation() == 3
                    ? (1.0D + previous.amount()) * (1.0D + current.amount()) - 1.0D
                    : previous.amount() + current.amount();
            double base = previous.base() != 0.0D ? previous.base() : current.base();
            merged.put(key, new ArtifactSubstat(current.attributeId(), amount, current.operation(), base,
                    previous.enhancementCount() + current.enhancementCount() + 1));
        }
        return List.copyOf(merged.values());
    }

    public static ArtifactSubstat enhance(ArtifactSubstat current, int stars, RandomSource random) {
        double base = current.base();
        try {
            PoolEntry firstMatch = null;
            for (String raw : TeyvatArtifactsConfig.SUBSTAT_POOL.get()) {
                PoolEntry entry = parse(raw);
                if (entry != null && entry.attributeId().equals(current.attributeId()) && entry.operation() == current.operation()) {
                    if (entry.base() == current.base()) {
                        firstMatch = entry;
                        break;
                    }
                    if (firstMatch == null) firstMatch = entry;
                }
            }
            if (firstMatch != null) base = firstMatch.base();
        } catch (IllegalStateException ignored) {
        }
        if (base == 0.0D) {
            double multiplier = configuredDouble(TeyvatArtifactsConfig.SUBSTAT_STAR_MULTIPLIERS,
                    ArtifactItemData.clampStars(stars) - 1, new double[]{0.7, 0.75, 0.8, 0.9, 1});
            base = current.amount() / Math.max(0.01D, multiplier);
        }
        double amount = current.amount() + base * rollFactor(stars, random);
        return new ArtifactSubstat(current.attributeId(), amount, current.operation(), base, current.enhancementCount() + 1);
    }

    private static double rollFactor(int stars, RandomSource random) {
        int index = ArtifactItemData.clampStars(stars) - 1;
        double multiplier = configuredDouble(TeyvatArtifactsConfig.SUBSTAT_STAR_MULTIPLIERS, index, new double[]{0.7, 0.75, 0.8, 0.9, 1});
        double variance = configuredDouble(TeyvatArtifactsConfig.SUBSTAT_STAR_VARIANCES, index, new double[]{0.1, 0.1, 0.1, 0.1, 0.1});
        return Math.max(0, multiplier + (random.nextDouble() * 2 - 1) * variance);
    }

    private static int configuredInt(net.neoforged.neoforge.common.ModConfigSpec.ConfigValue<List<? extends Number>> setting, int index, int[] fallback) {
        try {
            List<? extends Number> values = setting.get();
            return index < values.size() ? Math.max(0, values.get(index).intValue()) : fallback[index];
        } catch (IllegalStateException ignored) {
            return fallback[index];
        }
    }

    private static double configuredDouble(net.neoforged.neoforge.common.ModConfigSpec.ConfigValue<List<? extends Number>> setting, int index, double[] fallback) {
        try {
            List<? extends Number> values = setting.get();
            return index < values.size() ? Math.max(0, values.get(index).doubleValue()) : fallback[index];
        } catch (IllegalStateException ignored) {
            return fallback[index];
        }
    }
}
