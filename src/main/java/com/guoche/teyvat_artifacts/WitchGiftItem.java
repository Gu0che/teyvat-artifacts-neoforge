package com.guoche.teyvat_artifacts;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.fml.ModList;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Locale;

public class WitchGiftItem extends Item implements ICurioItem {
    private static final String DESCRIPTION_KEY_SUFFIX = ".artifact_desc";
    private static final String LITTLE_WITCH_DICTIONARY_WEIGHT_SYNC_KEY = "LittleWitchDictionaryAdvancementWeight";

    private final ResourceLocation maxHealthModifierId;
    private final ResourceLocation movementSpeedModifierId;
    private final ResourceLocation attackDamageModifierId;
    private final ResourceLocation armorModifierId;

    public WitchGiftItem(String id, Properties properties) {
        super(properties.stacksTo(1));
        this.maxHealthModifierId = ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, id + "_max_health");
        this.movementSpeedModifierId = ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, id + "_movement_speed");
        this.attackDamageModifierId = ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, id + "_attack_damage");
        this.armorModifierId = ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, id + "_armor");
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        appendDescription(stack, tooltip);
        appendLittleWitchDictionaryTooltip(stack, tooltip);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(getDescriptionId(stack)).withStyle(ArtifactItemData.getStarColor(5));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return "witch_gift".equals(slotContext.identifier());
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        if (!canEquip(slotContext, stack) || slotContext.cosmetic() || stack.is(TeyvatArtifacts.THE_LITTLE_WITCHS_DICTIONARY.get())) {
            return ImmutableMultimap.of();
        }

        ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.MAX_HEALTH, new AttributeModifier(maxHealthModifierId, 2.0D, AttributeModifier.Operation.ADD_VALUE));
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(movementSpeedModifierId, 0.05D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(attackDamageModifierId, 1.0D, AttributeModifier.Operation.ADD_VALUE));
        builder.put(Attributes.ARMOR, new AttributeModifier(armorModifierId, 2.0D, AttributeModifier.Operation.ADD_VALUE));
        return builder.build();
    }

    @Override
    public boolean canSync(SlotContext slotContext, ItemStack stack) {
        return stack.is(TeyvatArtifacts.THE_LITTLE_WITCHS_DICTIONARY.get());
    }

    @Override
    public CompoundTag writeSyncData(SlotContext slotContext, ItemStack stack) {
        CompoundTag tag = new CompoundTag();
        tag.putInt(LITTLE_WITCH_DICTIONARY_WEIGHT_SYNC_KEY, ArtifactItemData.getLittleWitchDictionaryWeight(stack));
        return tag;
    }

    @Override
    public void readSyncData(SlotContext slotContext, CompoundTag tag, ItemStack stack) {
        if (tag.contains(LITTLE_WITCH_DICTIONARY_WEIGHT_SYNC_KEY, Tag.TAG_INT)) {
            ArtifactItemData.setLittleWitchDictionaryWeight(stack, tag.getInt(LITTLE_WITCH_DICTIONARY_WEIGHT_SYNC_KEY));
        }
    }

    private static void appendDescription(ItemStack stack, List<Component> tooltip) {
        String key = stack.getDescriptionId() + DESCRIPTION_KEY_SUFFIX;
        if (!Language.getInstance().has(key)) {
            return;
        }

        if (!Screen.hasShiftDown()) {
            tooltip.add(1, Component.translatable("tooltip.teyvat_artifacts.hold_shift_for_description").withStyle(ChatFormatting.DARK_GRAY));
            return;
        }

        String desc = Language.getInstance().getOrDefault(key);
        int insertIndex = 1;
        for (String line : desc.split("\\n")) {
            tooltip.add(insertIndex++, Component.literal(line).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }

    private static void appendLittleWitchDictionaryTooltip(ItemStack stack, List<Component> tooltip) {
        if (!stack.is(TeyvatArtifacts.THE_LITTLE_WITCHS_DICTIONARY.get())) {
            return;
        }

        int weight = ArtifactItemData.getLittleWitchDictionaryWeight(stack);
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("curios.modifiers.witch_gift")
                .withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("tooltip.teyvat_artifacts.the_little_witchs_dictionary.max_health", format(weight * 0.1D))
                .withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("tooltip.teyvat_artifacts.the_little_witchs_dictionary.movement_speed", format(weight * 0.1D))
                .withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("tooltip.teyvat_artifacts.the_little_witchs_dictionary.damage_bonus", format(weight * 0.1D))
                .withStyle(ChatFormatting.BLUE));
        if (ModList.get().isLoaded("irons_spellbooks")) {
            tooltip.add(Component.translatable("tooltip.teyvat_artifacts.the_little_witchs_dictionary.max_mana", format(weight * 2.0D))
                    .withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("tooltip.teyvat_artifacts.the_little_witchs_dictionary.cooldown_reduction", format(weight * 0.1D))
                    .withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("tooltip.teyvat_artifacts.the_little_witchs_dictionary.cast_time_reduction", format(weight * 0.5D))
                    .withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable("tooltip.teyvat_artifacts.the_little_witchs_dictionary.mana_regen", format(weight * 0.5D))
                    .withStyle(ChatFormatting.BLUE));
        }
    }

    private static String format(double value) {
        return String.format(Locale.ROOT, value == Math.rint(value) ? "%.0f" : "%.1f", value);
    }
}
