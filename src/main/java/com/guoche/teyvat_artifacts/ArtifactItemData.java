package com.guoche.teyvat_artifacts;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class ArtifactItemData {
    public record EnhancementResult(ArtifactSubstat before, ArtifactSubstat after,
                                    ArtifactStat mainStat, double mainBefore, double mainAfter) {
        public double increase() {
            return after == null ? 0 : after.amount() - before.amount();
        }
    }

    public static final int MIN_STARS = 1;
    public static final int MAX_STARS = 5;

    private static final String STARS_SYNC_KEY = "Stars";
    private static final String STAT_SYNC_KEY = "Stat";
    private static final String SUBSTATS_SYNC_KEY = "Substats";
    private static final String ENHANCEMENTS_SYNC_KEY = "Enhancements";

    private ArtifactItemData() {
    }

    public static int clampStars(int stars) {
        return Math.max(MIN_STARS, Math.min(MAX_STARS, stars));
    }

    public static int getStars(ItemStack stack) {
        Integer stars = stack.get(TeyvatArtifacts.STARS.get());
        return stars == null ? 0 : clampStars(stars);
    }

    public static int getStars(ItemStack stack, int fallbackStars) {
        int stars = getStars(stack);
        return stars > 0 ? stars : clampStars(fallbackStars);
    }

    public static int getDisplayStars(ItemStack stack) {
        return getStars(stack);
    }

    public static void setStars(ItemStack stack, int stars) {
        stack.set(TeyvatArtifacts.STARS.get(), clampStars(stars));
    }

    public static void ensureStars(ItemStack stack, ArtifactItem artifact, RandomSource random) {
        if (stack.isEmpty()) {
            return;
        }

        if (getStars(stack) == 0) {
            setStars(stack, rollStars(artifact.getSet(), random));
        }
        ensureArtifactStat(stack, artifact, random);
        if (stack.get(TeyvatArtifacts.ARTIFACT_SUBSTATS.get()) == null) {
            rerollSubstats(stack, random);
        } else {
            List<ArtifactSubstat> stored = stack.get(TeyvatArtifacts.ARTIFACT_SUBSTATS.get());
            List<ArtifactSubstat> merged = ArtifactSubstat.mergeDuplicates(stored);
            if (merged.size() != stored.size()) stack.set(TeyvatArtifacts.ARTIFACT_SUBSTATS.get(), merged);
        }
    }

    public static ArtifactStat getArtifactStat(ItemStack stack) {
        String id = stack.get(TeyvatArtifacts.ARTIFACT_STAT.get());
        if (id != null && !id.isEmpty()) return ArtifactStat.byId(id);
        // Old flowers and feathers did not store a main stat ID.
        if (stack.getItem() instanceof ArtifactItem artifact
                && (artifact.getSlot() == ArtifactSlot.FLOWER || artifact.getSlot() == ArtifactSlot.FEATHER)) {
            List<ArtifactStat> pool = ArtifactStat.poolForSlot(artifact.getSlot());
            return pool.size() == 1 ? pool.get(0) : null;
        }
        return null;
    }

    public static void setArtifactStat(ItemStack stack, ArtifactStat stat) {
        if (stat != null) {
            stack.set(TeyvatArtifacts.ARTIFACT_STAT.get(), stat.id());
        }
    }

    public static void clearArtifactStat(ItemStack stack) {
        stack.remove(TeyvatArtifacts.ARTIFACT_STAT.get());
    }

    public static List<ArtifactSubstat> getSubstats(ItemStack stack) {
        List<ArtifactSubstat> result = stack.get(TeyvatArtifacts.ARTIFACT_SUBSTATS.get());
        return result == null ? List.of() : ArtifactSubstat.mergeDuplicates(result);
    }

    public static boolean canRerollSubstats(ItemStack stack) {
        return stack.getItem() instanceof ArtifactItem && getStars(stack) > 0;
    }

    public static int getEnhancementsUsed(ItemStack stack) {
        Integer used = stack.get(TeyvatArtifacts.ARTIFACT_ENHANCEMENTS.get());
        return used == null ? 0 : Math.max(0, used);
    }

    public static int getRemainingEnhancements(ItemStack stack) {
        int stars = getStars(stack);
        return stars == 0 ? 0 : Math.max(0, stars - getEnhancementsUsed(stack));
    }

    public static boolean canEnhanceSubstats(ItemStack stack) {
        if (!canRerollSubstats(stack) || getRemainingEnhancements(stack) == 0) return false;
        ArtifactStat main = getArtifactStat(stack);
        boolean growsMain = main != null && main.enhancement() != 0
                && stack.getItem() instanceof ArtifactItem artifact
                && ArtifactStat.canRollOnSlot(artifact.getSlot(), main);
        return growsMain || !enhanceableSubstats(getSubstats(stack)).isEmpty();
    }

    public static EnhancementResult enhanceRandomSubstat(ItemStack stack, RandomSource random) {
        if (!canEnhanceSubstats(stack)) return null;
        List<ArtifactSubstat> substats = new java.util.ArrayList<>(getSubstats(stack));
        List<Integer> choices = enhanceableSubstats(substats);
        ArtifactSubstat before = null;
        ArtifactSubstat enhanced = null;
        if (!choices.isEmpty()) {
            int index = choices.get(random.nextInt(choices.size()));
            before = substats.get(index);
            enhanced = ArtifactSubstat.enhance(before, getStars(stack), random);
            substats.set(index, enhanced);
        }
        ArtifactStat main = getArtifactStat(stack);
        if (!(stack.getItem() instanceof ArtifactItem artifact) || !ArtifactStat.canRollOnSlot(artifact.getSlot(), main)) {
            main = null;
        }
        int used = getEnhancementsUsed(stack);
        double mainBefore = main == null ? 0 : main.amountForStars(getStars(stack), used);
        stack.set(TeyvatArtifacts.ARTIFACT_SUBSTATS.get(), List.copyOf(substats));
        stack.set(TeyvatArtifacts.ARTIFACT_ENHANCEMENTS.get(), used + 1);
        return new EnhancementResult(before, enhanced, main, mainBefore,
                main == null ? 0 : main.amountForStars(getStars(stack), used + 1));
    }

    private static List<Integer> enhanceableSubstats(List<ArtifactSubstat> substats) {
        List<Integer> choices = new java.util.ArrayList<>();
        for (int i = 0; i < substats.size(); i++) {
            if (ArtifactSubstat.resolve(substats.get(i).attributeId()) != null) choices.add(i);
        }
        return choices;
    }

    public static List<ArtifactSubstat> rerollSubstats(ItemStack stack, RandomSource random) {
        List<ArtifactSubstat> result = ArtifactSubstat.roll(getStars(stack, 1), random);
        if (!result.isEmpty() || stack.get(TeyvatArtifacts.ARTIFACT_SUBSTATS.get()) == null || getSubstats(stack).isEmpty()) {
            stack.set(TeyvatArtifacts.ARTIFACT_SUBSTATS.get(), result);
            if (!result.isEmpty() || getEnhancementsUsed(stack) > 0) stack.set(TeyvatArtifacts.ARTIFACT_ENHANCEMENTS.get(), 0);
        }
        return result;
    }

    public static int getLittleWitchDictionaryWeight(ItemStack stack) {
        Integer weight = stack.get(TeyvatArtifacts.LITTLE_WITCH_DICTIONARY_ADVANCEMENT_WEIGHT.get());
        return weight == null ? 0 : Math.max(0, weight);
    }

    public static void setLittleWitchDictionaryWeight(ItemStack stack, int weight) {
        stack.set(TeyvatArtifacts.LITTLE_WITCH_DICTIONARY_ADVANCEMENT_WEIGHT.get(), Math.max(0, weight));
    }

    public static boolean canRerollArtifactStat(ItemStack stack) {
        if (!(stack.getItem() instanceof ArtifactItem artifact)) {
            return false;
        }

        if (getStars(stack) == 0) return false;
        ArtifactStat previous = getArtifactStat(stack);
        return ArtifactStat.poolForSlot(artifact.getSlot()).stream()
                .anyMatch(candidate -> previous == null || !candidate.id().equals(previous.id()));
    }

    public static ArtifactStat rerollArtifactStat(ItemStack stack, ArtifactItem artifact, RandomSource random) {
        ArtifactStat previous = getArtifactStat(stack);
        List<ArtifactStat> choices = ArtifactStat.poolForSlot(artifact.getSlot()).stream()
                .filter(candidate -> previous == null || !candidate.id().equals(previous.id())).toList();
        if (choices.isEmpty()) return null;
        ArtifactStat stat = choices.get(random.nextInt(choices.size()));
        setArtifactStat(stack, stat);
        return stat;
    }

    public static CompoundTag writeSyncData(ItemStack stack) {
        CompoundTag tag = new CompoundTag();
        int stars = getStars(stack);
        if (stars > 0) {
            tag.putInt(STARS_SYNC_KEY, stars);
        }

        String statId = stack.get(TeyvatArtifacts.ARTIFACT_STAT.get());
        if (statId != null && !statId.isEmpty()) {
            tag.putString(STAT_SYNC_KEY, statId);
        }

        List<ArtifactSubstat> substats = stack.get(TeyvatArtifacts.ARTIFACT_SUBSTATS.get());
        if (substats != null) {
            substats = ArtifactSubstat.mergeDuplicates(substats);
            ListTag list = new ListTag();
            for (ArtifactSubstat substat : substats) {
                CompoundTag entry = new CompoundTag();
                entry.putString("Attribute", substat.attributeId().toString());
                entry.putDouble("Amount", substat.amount());
                entry.putInt("Operation", substat.operation());
                entry.putDouble("Base", substat.base());
                entry.putInt("EnhancementCount", substat.enhancementCount());
                list.add(entry);
            }
            tag.put(SUBSTATS_SYNC_KEY, list);
        }

        tag.putInt(ENHANCEMENTS_SYNC_KEY, getEnhancementsUsed(stack));

        return tag;
    }

    public static void readSyncData(ItemStack stack, CompoundTag tag) {
        if (tag.contains(STARS_SYNC_KEY, Tag.TAG_INT)) {
            setStars(stack, tag.getInt(STARS_SYNC_KEY));
        }

        if (tag.contains(STAT_SYNC_KEY, Tag.TAG_STRING)) {
            stack.set(TeyvatArtifacts.ARTIFACT_STAT.get(), tag.getString(STAT_SYNC_KEY));
        }
        if (tag.contains(SUBSTATS_SYNC_KEY, Tag.TAG_LIST)) {
            List<ArtifactSubstat> result = new java.util.ArrayList<>();
            ListTag list = tag.getList(SUBSTATS_SYNC_KEY, Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag entry = list.getCompound(i);
                ResourceLocation id = ResourceLocation.tryParse(entry.getString("Attribute"));
                double amount = entry.getDouble("Amount");
                int operation = entry.getInt("Operation");
                double base = entry.getDouble("Base");
                if (id != null && Double.isFinite(amount) && operation >= 1 && operation <= 3) {
                    result.add(new ArtifactSubstat(id, amount, operation, Double.isFinite(base) ? base : 0.0D,
                            entry.getInt("EnhancementCount")));
                }
            }
            stack.set(TeyvatArtifacts.ARTIFACT_SUBSTATS.get(), ArtifactSubstat.mergeDuplicates(List.copyOf(result)));
        }
        if (tag.contains(ENHANCEMENTS_SYNC_KEY, Tag.TAG_INT)) {
            stack.set(TeyvatArtifacts.ARTIFACT_ENHANCEMENTS.get(), Math.max(0, tag.getInt(ENHANCEMENTS_SYNC_KEY)));
        }
    }

    private static void ensureArtifactStat(ItemStack stack, ArtifactItem artifact, RandomSource random) {
        if (ArtifactStat.poolForSlot(artifact.getSlot()).isEmpty()) {
            return;
        }

        ArtifactStat stat = getArtifactStat(stack);
        if (stat != null && ArtifactStat.canRollOnSlot(artifact.getSlot(), stat)) {
            String id = stack.get(TeyvatArtifacts.ARTIFACT_STAT.get());
            if (id == null || id.isEmpty()) setArtifactStat(stack, stat);
            return;
        }

        setArtifactStat(stack, ArtifactStat.rollForSlot(artifact.getSlot(), random));
    }

    public static int rollStars(ArtifactSet set, RandomSource random) {
        int min = set.minStars();
        int max = set.maxStars();
        return min + random.nextInt(max - min + 1);
    }

    public static int rollLeylineStars(ArtifactSet set, RandomSource random) {
        if (set.minStars() == 4 && set.maxStars() == 5) {
            return random.nextDouble() < TeyvatArtifactsConfig.leylineFiveStarChance() ? 5 : 4;
        }
        return rollStars(set, random);
    }

    public static void appendRarityTooltip(ItemStack stack, List<Component> tooltip) {
        int stars = getDisplayStars(stack);
        if (stars > 0) {
            tooltip.add(Component.translatable(
                    "tooltip.teyvat_artifacts.stars",
                    buildStars(stars)
            ).withStyle(getStarColor(stars)));
        }
    }

    public static String buildStars(int stars) {
        return "★".repeat(clampStars(stars));
    }

    public static ChatFormatting getStarColor(int stars) {
        return switch (clampStars(stars)) {
            case 5 -> ChatFormatting.GOLD;
            case 4 -> ChatFormatting.LIGHT_PURPLE;
            case 3 -> ChatFormatting.BLUE;
            case 2 -> ChatFormatting.GREEN;
            default -> ChatFormatting.WHITE;
        };
    }
}
