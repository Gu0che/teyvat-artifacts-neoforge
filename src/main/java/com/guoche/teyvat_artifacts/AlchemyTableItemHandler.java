package com.guoche.teyvat_artifacts;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

final class AlchemyTableItemHandler implements IItemHandler {
    private final AlchemyTableBlockEntity table;
    private final Direction side;
    private final SidedInvWrapper delegate;

    AlchemyTableItemHandler(AlchemyTableBlockEntity table, Direction side) {
        this.table = table;
        this.side = side;
        this.delegate = new SidedInvWrapper(table, side) {
            @Override
            public int getSlotLimit(int slot) {
                int actual = actualSlot(slot);
                return actual < 0 ? 0 : actual < AlchemyTableBlockEntity.INPUT_SLOTS ? 1 : table.getMaxStackSize();
            }
        };
    }

    private int actualSlot(int slot) {
        if (slot < 0 || slot >= getSlots()) {
            return -1;
        }
        return side == null ? slot : table.getSlotsForFace(side)[slot];
    }

    @Override
    public int getSlots() {
        return delegate.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return actualSlot(slot) < 0 ? ItemStack.EMPTY : delegate.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return actualSlot(slot) < 0 ? stack : delegate.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        int actual = actualSlot(slot);
        if (actual < 0 || amount <= 0 || !table.canTakeItemThroughFace(actual, table.getItem(actual), side)) {
            return ItemStack.EMPTY;
        }
        // SidedInvWrapper skips extraction rules for null-side callers; enforce them here too.
        return delegate.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return delegate.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        int actual = actualSlot(slot);
        return actual >= 0 && table.canPlaceItemThroughFace(actual, stack, side);
    }
}
