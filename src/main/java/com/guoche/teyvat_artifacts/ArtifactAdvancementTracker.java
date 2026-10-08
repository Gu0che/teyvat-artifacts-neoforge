package com.guoche.teyvat_artifacts;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public final class ArtifactAdvancementTracker {
    private static final ResourceLocation ROOT_ADVANCEMENT = id("root");
    private static final ResourceLocation FIRST_ARTIFACT_ADVANCEMENT = id("origin_of_all_evil");
    private static final ResourceLocation ARTIFACT_SET_INDEX_ADVANCEMENT = id("artifact_sets/index");
    private static final Map<ResourceLocation, List<ArtifactSet>> ARTIFACT_SET_GROUPS = Map.ofEntries(
            Map.entry(id("artifact_sets/common"), List.of(
                    ArtifactSet.INITIATE,
                    ArtifactSet.LUCKY,
                    ArtifactSet.ADVENTURER,
                    ArtifactSet.TRAVELING_DOCTOR,
                    ArtifactSet.RESOLUTION_OF_SOJOURNER,
                    ArtifactSet.TINY_MIRACLE,
                    ArtifactSet.BERSERKER,
                    ArtifactSet.INSTRUCTOR,
                    ArtifactSet.THE_EXILE,
                    ArtifactSet.DEFENDERS_WILL,
                    ArtifactSet.BRAVE_HEART,
                    ArtifactSet.MARTIAL_ARTIST,
                    ArtifactSet.GAMBLER,
                    ArtifactSet.SCHOLAR,
                    ArtifactSet.GLADIATORS_FINALE,
                    ArtifactSet.WANDERERS_TROUPE,
                    ArtifactSet.PRAYERS_FOR_THUNDER,
                    ArtifactSet.PRAYERS_FOR_DESTINY,
                    ArtifactSet.PRAYERS_FOR_ILLUMINATION,
                    ArtifactSet.PRAYERS_TO_SPRINGTIME
            )),
            Map.entry(id("artifact_sets/mondstadt"), List.of(
                    ArtifactSet.THUNDERING_FURY,
                    ArtifactSet.THUNDERSOOTHER,
                    ArtifactSet.PALE_FLAME,
                    ArtifactSet.TENACITY_OF_THE_MILLELITH,
                    ArtifactSet.CELESTIAL_GIFT,
                    ArtifactSet.DISENCHANTMENT_IN_DEEP_SHADOW,
                    ArtifactSet.BLIZZARD_STRAYER,
                    ArtifactSet.HEART_OF_DEPTH,
                    ArtifactSet.MAIDEN_BELOVED,
                    ArtifactSet.VIRIDESCENT_VENERER
            )),
            Map.entry(id("artifact_sets/liyue"), List.of(
                    ArtifactSet.BLOODSTAINED_CHIVALRY,
                    ArtifactSet.NOBLESSE_OBLIGE,
                    ArtifactSet.ARCHAIC_PETRA,
                    ArtifactSet.RETRACING_BOLIDE,
                    ArtifactSet.ECHOES_OF_AN_OFFERING,
                    ArtifactSet.VERMILLION_HEREAFTER,
                    ArtifactSet.CRIMSON_WITCH_OF_FLAMES,
                    ArtifactSet.LAVAWALKER
            )),
            Map.entry(id("artifact_sets/inazuma"), List.of(
                    ArtifactSet.EMBLEM_OF_SEVERED_FATE,
                    ArtifactSet.SHIMENAWAS_REMINISCENCE,
                    ArtifactSet.HUSK_OF_OPULENT_DREAMS,
                    ArtifactSet.OCEAN_HUED_CLAM
            )),
            Map.entry(id("artifact_sets/sumeru"), List.of(
                    ArtifactSet.NYMPHS_DREAM,
                    ArtifactSet.VOURUKASHAS_GLOW,
                    ArtifactSet.DEEPWOOD_MEMORIES,
                    ArtifactSet.GILDED_DREAMS,
                    ArtifactSet.DESERT_PAVILION_CHRONICLE,
                    ArtifactSet.FLOWER_OF_PARADISE_LOST
            )),
            Map.entry(id("artifact_sets/fontaine"), List.of(
                    ArtifactSet.NIGHTTIME_WHISPERS,
                    ArtifactSet.SONG_OF_DAYS_PAST,
                    ArtifactSet.GOLDEN_TROUPE,
                    ArtifactSet.MARECHAUSSEE_HUNTER,
                    ArtifactSet.FRAGMENT_OF_HARMONIC_WHIMSY,
                    ArtifactSet.UNFINISHED_REVERIE
            )),
            Map.entry(id("artifact_sets/natlan"), List.of(
                    ArtifactSet.FINALE_OF_THE_DEEP_GALLERIES,
                    ArtifactSet.LONG_NIGHTS_OATH,
                    ArtifactSet.OBSIDIAN_CODEX,
                    ArtifactSet.SCROLL_OF_THE_HERO_OF_CINDER_CITY
            )),
            Map.entry(id("artifact_sets/nod_krai"), List.of(
                    ArtifactSet.A_DAY_CARVED_FROM_RISING_WINDS,
                    ArtifactSet.AUBADE_OF_MORNINGSTAR_AND_MOON,
                    ArtifactSet.NIGHT_OF_THE_SKYS_UNVEILING,
                    ArtifactSet.SILKEN_MOONS_SERENADE
            )),
            Map.entry(id("artifact_sets/snezhnaya"), List.of(
                    ArtifactSet.HEART_OF_THE_FURNACE,
                    ArtifactSet.SCARLET_PROOF
            ))
    );

    private static Map<ArtifactSet, EnumSet<ArtifactSlot>> requiredSlots;

    private ArtifactAdvancementTracker() {
    }

    public static void check(ServerPlayer player) {
        EnumMap<ArtifactSet, EnumSet<ArtifactSlot>> ownedSlots = collectOwnedSlots(player);
        if (ownedSlots.isEmpty()) {
            return;
        }

        grantLayoutAdvancements(player);
        grantAdvancement(player, FIRST_ARTIFACT_ADVANCEMENT);

        for (Map.Entry<ArtifactSet, EnumSet<ArtifactSlot>> entry : getRequiredSlots().entrySet()) {
            ResourceLocation advancementId = setAdvancement(entry.getKey());
            EnumSet<ArtifactSlot> owned = ownedSlots.get(entry.getKey());
            if (owned != null) {
                for (ArtifactSlot slot : owned) {
                    if (entry.getValue().contains(slot)) {
                        awardCriterion(player, advancementId, slot.id());
                    }
                }
            }
            if (owned != null && owned.containsAll(entry.getValue())) {
                awardGroupCriterion(player, entry.getKey());
            }
        }

        awardIndexCriterion(player);
    }

    private static void grantLayoutAdvancements(ServerPlayer player) {
        grantAdvancement(player, ROOT_ADVANCEMENT);
    }

    private static void awardGroupCriterion(ServerPlayer player, ArtifactSet set) {
        for (Map.Entry<ResourceLocation, List<ArtifactSet>> group : ARTIFACT_SET_GROUPS.entrySet()) {
            if (group.getValue().contains(set)) {
                awardCriterion(player, group.getKey(), set.id());
                return;
            }
        }
    }

    private static void awardIndexCriterion(ServerPlayer player) {
        for (ArtifactSet set : getRequiredSlots().keySet()) {
            if (hasAdvancement(player, setAdvancement(set))) {
                awardCriterion(player, ARTIFACT_SET_INDEX_ADVANCEMENT, set.id());
            }
        }
    }

    private static EnumMap<ArtifactSet, EnumSet<ArtifactSlot>> collectOwnedSlots(ServerPlayer player) {
        EnumMap<ArtifactSet, EnumSet<ArtifactSlot>> ownedSlots = new EnumMap<>(ArtifactSet.class);
        for (ItemStack stack : player.getInventory().items) {
            collectArtifactStack(stack, ownedSlots);
        }
        for (ItemStack stack : player.getInventory().offhand) {
            collectArtifactStack(stack, ownedSlots);
        }
        for (ItemStack stack : player.getInventory().armor) {
            collectArtifactStack(stack, ownedSlots);
        }
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            for (ArtifactSlot slot : ArtifactSlot.values()) {
                handler.getStacksHandler(slot.id()).ifPresent(stacksHandler -> collectArtifactStacks(stacksHandler.getStacks(), ownedSlots));
            }
        });
        return ownedSlots;
    }

    private static void collectArtifactStacks(IDynamicStackHandler stacks, EnumMap<ArtifactSet, EnumSet<ArtifactSlot>> ownedSlots) {
        for (int index = 0; index < stacks.getSlots(); index++) {
            collectArtifactStack(stacks.getStackInSlot(index), ownedSlots);
        }
    }

    private static void collectArtifactStack(ItemStack stack, EnumMap<ArtifactSet, EnumSet<ArtifactSlot>> ownedSlots) {
        if (stack.getItem() instanceof ArtifactItem artifact) {
            ownedSlots.computeIfAbsent(artifact.getSet(), ignored -> EnumSet.noneOf(ArtifactSlot.class)).add(artifact.getSlot());
        }
    }

    private static Map<ArtifactSet, EnumSet<ArtifactSlot>> getRequiredSlots() {
        if (requiredSlots == null) {
            EnumMap<ArtifactSet, EnumSet<ArtifactSlot>> discoveredSlots = new EnumMap<>(ArtifactSet.class);
            for (Item item : BuiltInRegistries.ITEM) {
                collectRequiredSlot(item, discoveredSlots);
            }
            requiredSlots = discoveredSlots;
        }
        return requiredSlots;
    }

    private static void collectRequiredSlot(Item item, EnumMap<ArtifactSet, EnumSet<ArtifactSlot>> slots) {
        if (item instanceof ArtifactItem artifact) {
            slots.computeIfAbsent(artifact.getSet(), ignored -> EnumSet.noneOf(ArtifactSlot.class)).add(artifact.getSlot());
        }
    }

    private static boolean hasAdvancement(ServerPlayer player, ResourceLocation id) {
        AdvancementHolder advancement = player.server.getAdvancements().get(id);
        return advancement != null && player.getAdvancements().getOrStartProgress(advancement).isDone();
    }

    private static void grantAdvancement(ServerPlayer player, ResourceLocation id) {
        AdvancementHolder advancement = player.server.getAdvancements().get(id);
        if (advancement == null) {
            return;
        }

        for (String criterion : advancement.value().criteria().keySet()) {
            player.getAdvancements().award(advancement, criterion);
        }
    }

    private static void awardCriterion(ServerPlayer player, ResourceLocation id, String criterion) {
        AdvancementHolder advancement = player.server.getAdvancements().get(id);
        if (advancement != null && advancement.value().criteria().containsKey(criterion)) {
            player.getAdvancements().award(advancement, criterion);
        }
    }

    private static ResourceLocation setAdvancement(ArtifactSet set) {
        return id("artifact_sets/" + set.id());
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, path);
    }
}
