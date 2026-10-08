package com.guoche.teyvat_artifacts;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.AttributeTooltipContext;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtifactItem extends Item implements ICurioItem {
    private static final String DESCRIPTION_KEY_SUFFIX = ".artifact_desc";

    private final ArtifactSet set;
    private final ArtifactSlot slot;
    private final ResourceLocation modifierId;

    public ArtifactItem(ArtifactSet set, ArtifactSlot slot, Properties properties) {
        super(properties.stacksTo(1));
        this.set = set;
        this.slot = slot;
        this.modifierId = ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, set.id() + "_" + slot.id() + "_bonus");
    }

    public ArtifactSet getSet() {
        return set;
    }

    public ArtifactSlot getSlot() {
        return slot;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slot.id().equals(slotContext.identifier());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        appendArtifactDescription(stack, tooltip);
        if (ArtifactItemData.getStars(stack) > 0) {
            tooltip.add(1, Component.translatable("tooltip.teyvat_artifacts.remaining_enhancements",
                    ArtifactItemData.getRemainingEnhancements(stack)).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        int stars = ArtifactItemData.getStars(stack);
        if (stars > 0) {
            return Component.translatable(this.getDescriptionId(stack)).withStyle(ArtifactItemData.getStarColor(stars));
        }

        return super.getName(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!level.isClientSide && entity instanceof Player player) {
            ArtifactItemData.ensureStars(stack, this, player.getRandom());
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ensureStarsFromSlotContext(slotContext, stack);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ensureStarsFromSlotContext(slotContext, stack);
    }

    @Override
    public boolean canSync(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canWalkOnPowderedSnow(SlotContext slotContext, ItemStack stack) {
        return canEquip(slotContext, stack) && !slotContext.cosmetic() && set == ArtifactSet.PRAYERS_TO_SPRINGTIME;
    }

    @Override
    public CompoundTag writeSyncData(SlotContext slotContext, ItemStack stack) {
        return ArtifactItemData.writeSyncData(stack);
    }

    @Override
    public void readSyncData(SlotContext slotContext, CompoundTag tag, ItemStack stack) {
        ArtifactItemData.readSyncData(stack, tag);
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, TooltipContext context, ItemStack stack) {
        TooltipFlag flag = context instanceof AttributeTooltipContext attributeContext ? attributeContext.flag() : TooltipFlag.NORMAL;
        return buildAttributesTooltip(tooltips, stack, flag);
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        return buildAttributesTooltip(tooltips, stack, TooltipFlag.NORMAL);
    }

    private List<Component> buildAttributesTooltip(List<Component> tooltips, ItemStack stack, TooltipFlag flag) {
        if (ArtifactItemData.getStars(stack) <= 0) return tooltips;
        SlotContext context = new SlotContext(slot.id(), null, 0, false, true);
        Map<ResourceLocation, MutableComponent> lines = new HashMap<>();
        for (var entry : getAttributeModifiers(context, modifierId, stack).entries()) {
            lines.put(entry.getValue().id(), entry.getKey().value().toComponent(entry.getValue(), flag));
        }

        List<Component> result = new ArrayList<>();
        result.add(Component.empty());
        result.add(Component.translatable("curios.modifiers." + slot.id()).withStyle(ChatFormatting.GOLD));
        MutableComponent main = lines.get(modifierId);
        if (main != null) {
            result.add(Component.translatable("tooltip.teyvat_artifacts.main_stat").withStyle(ChatFormatting.YELLOW));
            result.add(Component.literal(" ").append(main.withStyle(ChatFormatting.YELLOW)));
        }
        boolean substatHeaderAdded = false;
        List<ArtifactSubstat> substats = ArtifactItemData.getSubstats(stack);
        for (int index = 0; index < substats.size(); index++) {
            MutableComponent line = lines.get(substatModifierId(index));
            if (line == null) continue;
            if (!substatHeaderAdded) {
                result.add(Component.translatable("tooltip.teyvat_artifacts.substats").withStyle(ChatFormatting.GRAY));
                substatHeaderAdded = true;
            }
            int count = substats.get(index).enhancementCount();
            line.withStyle(count > 0 ? ArtifactItemData.getStarColor(count) : ChatFormatting.GRAY);
            if (count > 0) line.append(Component.translatable("tooltip.teyvat_artifacts.substat_enhancements", count));
            result.add(Component.literal(" ").append(line));
        }
        return result;
    }

    private ResourceLocation substatModifierId(int index) {
        return ResourceLocation.fromNamespaceAndPath(TeyvatArtifacts.MODID, set.id() + "_" + slot.id() + "_substat_" + index);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        if (!canEquip(slotContext, stack) || slotContext.cosmetic()) {
            return ImmutableMultimap.of();
        }
        ensureStarsFromSlotContext(slotContext, stack);
        int stars = ArtifactItemData.getStars(stack);
        if (stars <= 0) {
            return ImmutableMultimap.of();
        }

        ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();
        addRolledStatModifier(builder, stack, stars);
        List<ArtifactSubstat> substats = ArtifactItemData.getSubstats(stack);
        for (int index = 0; index < substats.size(); index++) {
            ArtifactSubstat substat = substats.get(index);
            Holder<Attribute> attribute = ArtifactSubstat.resolve(substat.attributeId());
            if (attribute == null) continue;
            ResourceLocation substatId = substatModifierId(index);
            builder.put(attribute, new AttributeModifier(substatId, substat.amount(), substat.modifierOperation()));
        }
        return builder.build();
    }

    private void addRolledStatModifier(ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder, ItemStack stack, int stars) {
        ArtifactStat stat = ArtifactItemData.getArtifactStat(stack);
        if (stat == null || !ArtifactStat.canRollOnSlot(slot, stat)) {
            return;
        }

        Holder<Attribute> attribute = ArtifactSubstat.resolve(stat.attributeId());
        if (attribute != null) {
            builder.put(attribute, new AttributeModifier(modifierId,
                    stat.amountForStars(stars, ArtifactItemData.getEnhancementsUsed(stack)), stat.modifierOperation()));
        }
    }

    private void ensureStarsFromSlotContext(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        if (wearer != null && !wearer.level().isClientSide) {
            ArtifactItemData.ensureStars(stack, this, wearer.getRandom());
        }
    }

    private static void appendArtifactDescription(ItemStack stack, List<Component> tooltip) {
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

}
