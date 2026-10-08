package com.guoche.teyvat_artifacts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.stream.IntStream;

public class AlchemyTableBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {
    public static final int INPUT_SLOTS = 27;
    public static final int SLOT_COUNT = INPUT_SLOTS + 3;
    private static final int[] INPUT_INDICES = IntStream.range(0, INPUT_SLOTS).toArray();
    private static final int[] OUTPUT_INDICES = IntStream.range(INPUT_SLOTS, SLOT_COUNT).toArray();
    private static final int[] ALL_INDICES = IntStream.range(0, SLOT_COUNT).toArray();
    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private final IItemHandler[] handlers = new IItemHandler[7];
    
    private boolean needsProcessing = true;
    private boolean statusDirty = true;
    private boolean conversionPossible;

    public AlchemyTableBlockEntity(BlockPos pos, BlockState state) {
        super(AlchemyTableContent.BLOCK_ENTITY.get(), pos, state);
    }

    public IItemHandler itemHandler(Direction side) {
        int index = side == null ? 6 : side.get3DDataValue();
        if (handlers[index] == null) {
            handlers[index] = new AlchemyTableItemHandler(this, side);
        }
        return handlers[index];
    }

    public ContainerData menuData() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return index == 0 ? (getBlockState().getValue(AlchemyTableBlock.POWERED) ? 1 : 0)
                        : (canTransmute() ? 1 : 0);
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public void requestProcessing() {
        needsProcessing = true;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AlchemyTableBlockEntity table) {
        if (level.isClientSide || !state.getValue(AlchemyTableBlock.POWERED) || !table.needsProcessing) {
            return;
        }
        int interval = TeyvatArtifactsConfig.ALCHEMY_AUTOMATIC_INTERVAL_TICKS.get();
        if (Math.floorMod(level.getGameTime() + pos.asLong(), interval) == 0) {
            table.transmute();
            table.needsProcessing = false;
        }
    }

    private Item outputItem(int tier) {
        return switch (tier) {
            case 0 -> TeyvatArtifacts.SANCTIFYING_UNCTION.get();
            case 1 -> TeyvatArtifacts.SANCTIFYING_ESSENCE.get();
            default -> TeyvatArtifacts.SANCTIFYING_ELIXIR.get();
        };
    }

    private int outputValue() {
        return items.get(INPUT_SLOTS).getCount()
                + items.get(INPUT_SLOTS + 1).getCount() * 4
                + items.get(INPUT_SLOTS + 2).getCount() * 16;
    }

    private AlchemyTableMaterials.Counts pack(int value) {
        return AlchemyTableMaterials.pack(value, outputItem(0).getDefaultInstance().getMaxStackSize(),
                outputItem(1).getDefaultInstance().getMaxStackSize(), outputItem(2).getDefaultInstance().getMaxStackSize());
    }

    private static int materialValue(ItemStack artifact) {
        ItemStack result = ArtifactCraftingHelper.assembleMaterialResult(java.util.List.of(artifact));
        if (result.isEmpty()) {
            return 0;
        }
        return result.getCount() * (result.is(TeyvatArtifacts.SANCTIFYING_ESSENCE.get()) ? 4 : 1);
    }

    public boolean canTransmute() {
        if (statusDirty) {
            int value = outputValue();
            AlchemyTableMaterials.Counts normalized = pack(value);
            conversionPossible = normalized != null && !matchesOutputs(normalized);
            for (int slot = 0; slot < INPUT_SLOTS; slot++) {
                int added = materialValue(items.get(slot));
                if (added > 0 && pack(value + added) != null) {
                    conversionPossible = true;
                    break;
                }
            }
            statusDirty = false;
        }
        return conversionPossible;
    }

    private boolean matchesOutputs(AlchemyTableMaterials.Counts counts) {
        return items.get(INPUT_SLOTS).getCount() == counts.unction()
                && items.get(INPUT_SLOTS + 1).getCount() == counts.essence()
                && items.get(INPUT_SLOTS + 2).getCount() == counts.elixir();
    }

    public int transmute() {
        if (level == null || level.isClientSide) {
            return 0;
        }
        int value = outputValue();
        int converted = 0;
        for (int slot = 0; slot < INPUT_SLOTS; slot++) {
            ItemStack artifact = items.get(slot);
            int added = materialValue(artifact);
            if (added == 0 || pack(value + added) == null) {
                continue;
            }
            // The entire output is proven to fit before consuming this artifact.
            value += added;
            artifact.shrink(1);
            converted++;
        }
        AlchemyTableMaterials.Counts result = pack(value);
        if (result != null && (converted > 0 || !matchesOutputs(result))) {
            int[] counts = {result.unction(), result.essence(), result.elixir()};
            for (int tier = 0; tier < 3; tier++) {
                items.set(INPUT_SLOTS + tier, counts[tier] == 0 ? ItemStack.EMPTY
                        : new ItemStack(outputItem(tier), counts[tier]));
            }
            setChanged();
        }
        return converted;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.teyvat_artifacts.alchemy_table");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new AlchemyTableMenu(id, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack removed = ContainerHelper.removeItem(items, slot, amount);
        if (!removed.isEmpty()) {
            setChanged();
        }
        return removed;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack removed = ContainerHelper.takeItem(items, slot);
        if (!removed.isEmpty()) {
            setChanged();
        }
        return removed;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (!stack.isEmpty()) {
            stack.setCount(Math.min(stack.getCount(), slot < INPUT_SLOTS ? 1 : stack.getMaxStackSize()));
        }
        setChanged();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        needsProcessing = true;
        statusDirty = true;
    }

    @Override
    public boolean stillValid(Player player) {
        return level != null && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5,
                        worldPosition.getZ() + 0.5) <= 64;
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot >= 0 && slot < INPUT_SLOTS && stack.getItem() instanceof ArtifactItem;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == null ? ALL_INDICES : side == Direction.DOWN ? OUTPUT_INDICES : INPUT_INDICES;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
        return side != Direction.DOWN && canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return slot >= INPUT_SLOTS && slot < SLOT_COUNT && (side == null || side == Direction.DOWN);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items.clear();
        ContainerHelper.loadAllItems(tag, items, registries);
        needsProcessing = true;
        statusDirty = true;
    }
}
