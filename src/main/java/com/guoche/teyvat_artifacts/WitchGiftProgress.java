package com.guoche.teyvat_artifacts;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.List;
import java.util.Optional;

public final class WitchGiftProgress {
    private static final ResourceLocation ENTER_NETHER_ADVANCEMENT =
            ResourceLocation.fromNamespaceAndPath("minecraft", "story/enter_the_nether");
    private static final ResourceLocation NETHER_ROOT_ADVANCEMENT =
            ResourceLocation.fromNamespaceAndPath("minecraft", "nether/root");
    private static final ResourceLocation WITCH_GIFT_ADVANCEMENT =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "witch_gift");
    private static final ResourceLocation WITCH_HOMEWORK_ADVANCEMENT =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "witch_homework");
    private static final ResourceLocation TEYVAT_ARTIFACTS_ROOT_ADVANCEMENT =
            ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, "root");
    private static final ResourceLocation END_CITY_TREASURE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "chests/end_city_treasure");
    private static final String WITCH_REVELATION_CASE_GRANTED_KEY =
            TeyvatArtifacts.MODID + ".witch_revelation_case_granted";
    private static final String LITTLE_WITCH_DICTIONARY_END_CHEST_KEY =
            TeyvatArtifacts.MODID + ".little_witch_dictionary_end_chest";
    private static final String LITTLE_WITCH_DICTIONARY_HOMEWORK_KEY =
            TeyvatArtifacts.MODID + ".little_witch_dictionary_homework";
    private static final String ADVANCEMENT_WEIGHT_KEY =
            TeyvatArtifacts.MODID + ".little_witch_dictionary_advancement_weight";
    private static final List<String> COPIED_KEYS = List.of(
            WITCH_REVELATION_CASE_GRANTED_KEY,
            LITTLE_WITCH_DICTIONARY_END_CHEST_KEY,
            LITTLE_WITCH_DICTIONARY_HOMEWORK_KEY,
            ADVANCEMENT_WEIGHT_KEY
    );

    private WitchGiftProgress() {
    }

    public static void onLogin(ServerPlayer player) {
        grantWitchRevelationCaseIfEligible(player);
        grantLittleWitchHomeworkIfPresent(player);
        refreshAdvancementWeight(player);
    }

    public static void onAdvancementEarned(ServerPlayer player, ResourceLocation advancementId) {
        if (ENTER_NETHER_ADVANCEMENT.equals(advancementId) || NETHER_ROOT_ADVANCEMENT.equals(advancementId)) {
            grantWitchRevelationCase(player);
        }
        grantLittleWitchHomeworkIfPresent(player);
        refreshAdvancementWeight(player);
    }

    public static void onPlayerTick(ServerPlayer player) {
        grantWitchRevelationCaseIfEligible(player);
        grantLittleWitchHomeworkIfPresent(player);
        refreshDictionaryStacks(player, getAdvancementWeight(player));
    }

    public static void copyPersistentData(Player original, Player target) {
        CompoundTag originalData = getPersistedData(original);
        CompoundTag targetData = getPersistedData(target);
        for (String key : COPIED_KEYS) {
            if (originalData.contains(key)) {
                targetData.put(key, originalData.get(key).copy());
            }
        }
    }

    public static void tryAddEndChestDictionaryLoot(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (!END_CITY_TREASURE.equals(context.getQueriedLootTableId())) {
            return;
        }

        if (!(context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof ServerPlayer player)) {
            return;
        }

        CompoundTag data = getPersistedData(player);
        if (data.getBoolean(LITTLE_WITCH_DICTIONARY_END_CHEST_KEY) || data.getBoolean(LITTLE_WITCH_DICTIONARY_HOMEWORK_KEY)) {
            return;
        }

        data.putBoolean(LITTLE_WITCH_DICTIONARY_END_CHEST_KEY, true);
        ItemStack stack = new ItemStack(TeyvatArtifacts.THE_LITTLE_WITCHS_DICTIONARY.get());
        ArtifactItemData.setLittleWitchDictionaryWeight(stack, refreshAdvancementWeight(player));
        generatedLoot.add(stack);
    }

    public static int getAdvancementWeight(Player player) {
        return Math.max(0, getPersistedData(player).getInt(ADVANCEMENT_WEIGHT_KEY));
    }

    public static int refreshAdvancementWeight(ServerPlayer player) {
        int weight = calculateAdvancementWeight(player);
        getPersistedData(player).putInt(ADVANCEMENT_WEIGHT_KEY, weight);
        refreshDictionaryStacks(player, weight);
        return weight;
    }

    private static int calculateAdvancementWeight(ServerPlayer player) {
        int weight = 0;
        for (AdvancementHolder advancement : player.server.getAdvancements().getAllAdvancements()) {
            if (!isCountableAdvancement(advancement.id())) {
                continue;
            }

            if (!player.getAdvancements().getOrStartProgress(advancement).isDone()) {
                continue;
            }

            Optional<DisplayInfo> display = advancement.value().display();
            if (display.isEmpty()) {
                continue;
            }

            weight += switch (display.get().getType()) {
                case CHALLENGE -> 5;
                case GOAL -> 1;
                case TASK -> 1;
            };
        }
        return weight;
    }

    private static boolean isCountableAdvancement(ResourceLocation id) {
        if (id.getPath().startsWith("recipes/")) {
            return false;
        }
        if (!TeyvatArtifacts.MODID.equals(id.getNamespace())) {
            return true;
        }

        String path = id.getPath();
        return !path.equals("root");
    }

    private static void grantWitchRevelationCaseIfEligible(ServerPlayer player) {
        if (getPersistedData(player).getBoolean(WITCH_REVELATION_CASE_GRANTED_KEY)) {
            return;
        }

        if (hasAdvancement(player, ENTER_NETHER_ADVANCEMENT) || hasAdvancement(player, NETHER_ROOT_ADVANCEMENT)) {
            grantWitchRevelationCase(player);
        }
    }

    private static void grantWitchRevelationCase(ServerPlayer player) {
        CompoundTag data = getPersistedData(player);
        if (data.getBoolean(WITCH_REVELATION_CASE_GRANTED_KEY)) {
            return;
        }

        data.putBoolean(WITCH_REVELATION_CASE_GRANTED_KEY, true);
        equipWitchGift(player, new ItemStack(TeyvatArtifacts.WITCHS_REVELATION_CASE.get()));
        grantAdvancement(player, TEYVAT_ARTIFACTS_ROOT_ADVANCEMENT);
        grantAdvancement(player, WITCH_GIFT_ADVANCEMENT);
    }

    private static void grantLittleWitchHomeworkIfPresent(ServerPlayer player) {
        CompoundTag data = getPersistedData(player);
        if (data.getBoolean(LITTLE_WITCH_DICTIONARY_HOMEWORK_KEY) || !hasLittleWitchDictionaryAnywhere(player)) {
            return;
        }

        data.putBoolean(LITTLE_WITCH_DICTIONARY_HOMEWORK_KEY, true);
        grantAdvancement(player, TEYVAT_ARTIFACTS_ROOT_ADVANCEMENT);
        grantAdvancement(player, WITCH_HOMEWORK_ADVANCEMENT);
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

    private static void equipWitchGift(Player player, ItemStack stack) {
        boolean[] equipped = {false};
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> handler.getStacksHandler("witch_gift").ifPresent(stacksHandler -> {
            if (stacksHandler.getSlots() > 0 && stacksHandler.getStacks().getStackInSlot(0).isEmpty()) {
                handler.setEquippedCurio("witch_gift", 0, stack);
                equipped[0] = true;
            }
        }));

        if (!equipped[0] && !player.addItem(stack)) {
            player.drop(stack, false);
        }
    }

    private static boolean hasLittleWitchDictionaryAnywhere(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(TeyvatArtifacts.THE_LITTLE_WITCHS_DICTIONARY.get())) {
                return true;
            }
        }
        for (ItemStack stack : player.getInventory().offhand) {
            if (stack.is(TeyvatArtifacts.THE_LITTLE_WITCHS_DICTIONARY.get())) {
                return true;
            }
        }
        for (ItemStack stack : player.getInventory().armor) {
            if (stack.is(TeyvatArtifacts.THE_LITTLE_WITCHS_DICTIONARY.get())) {
                return true;
            }
        }

        boolean[] found = {false};
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> handler.getStacksHandler("witch_gift").ifPresent(stacksHandler -> {
            IDynamicStackHandler stacks = stacksHandler.getStacks();
            for (int index = 0; index < stacks.getSlots(); index++) {
                if (stacks.getStackInSlot(index).is(TeyvatArtifacts.THE_LITTLE_WITCHS_DICTIONARY.get())) {
                    found[0] = true;
                    return;
                }
            }
        }));
        return found[0];
    }

    private static void refreshDictionaryStacks(Player player, int weight) {
        for (ItemStack stack : player.getInventory().items) {
            refreshDictionaryStack(stack, weight);
        }
        for (ItemStack stack : player.getInventory().offhand) {
            refreshDictionaryStack(stack, weight);
        }
        for (ItemStack stack : player.getInventory().armor) {
            refreshDictionaryStack(stack, weight);
        }
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> handler.getStacksHandler("witch_gift").ifPresent(stacksHandler -> {
            IDynamicStackHandler stacks = stacksHandler.getStacks();
            for (int index = 0; index < stacks.getSlots(); index++) {
                refreshDictionaryStack(stacks.getStackInSlot(index), weight);
            }
        }));
    }

    private static void refreshDictionaryStack(ItemStack stack, int weight) {
        if (stack.is(TeyvatArtifacts.THE_LITTLE_WITCHS_DICTIONARY.get())) {
            ArtifactItemData.setLittleWitchDictionaryWeight(stack, weight);
        }
    }

    private static CompoundTag getPersistedData(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        if (!persistentData.contains(Player.PERSISTED_NBT_TAG, Tag.TAG_COMPOUND)) {
            persistentData.put(Player.PERSISTED_NBT_TAG, new CompoundTag());
        }

        return persistentData.getCompound(Player.PERSISTED_NBT_TAG);
    }
}
