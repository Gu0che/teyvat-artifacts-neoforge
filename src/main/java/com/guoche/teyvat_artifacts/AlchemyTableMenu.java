package com.guoche.teyvat_artifacts;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AlchemyTableMenu extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData data;
    private final AlchemyTableBlockEntity table;

    public AlchemyTableMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, new SimpleContainer(AlchemyTableBlockEntity.SLOT_COUNT),
                new SimpleContainerData(2), null);
    }

    public AlchemyTableMenu(int id, Inventory playerInventory, AlchemyTableBlockEntity table) {
        this(id, playerInventory, table, table.menuData(), table);
    }

    private AlchemyTableMenu(int id, Inventory playerInventory, Container inventory,
                             ContainerData data, AlchemyTableBlockEntity table) {
        super(AlchemyTableContent.MENU.get(), id);
        checkContainerSize(inventory, AlchemyTableBlockEntity.SLOT_COUNT);
        checkContainerDataCount(data, 2);
        this.inventory = inventory;
        this.data = data;
        this.table = table;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, row * 9 + column, 8 + column * 18, 18 + row * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return stack.getItem() instanceof ArtifactItem;
                    }

                    @Override
                    public int getMaxStackSize() {
                        return 1;
                    }
                });
            }
        }
        for (int i = 0; i < 3; i++) {
            addSlot(new Slot(inventory, AlchemyTableBlockEntity.INPUT_SLOTS + i, 98 + i * 24, 84) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
        }
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, 9 + row * 9 + column, 8 + column * 18, 134 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, 8 + column * 18, 192));
        }
        addDataSlots(data);
    }

    public boolean isPowered() {
        return data.get(0) != 0;
    }

    public boolean canTransmute() {
        return data.get(1) != 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return inventory.stillValid(player);
    }

    @Override
    public boolean clickMenuButton(Player player, int button) {
        if (button != 0 || table == null || player.containerMenu != this || !stillValid(player)) {
            return false;
        }
        table.transmute();
        broadcastChanges();
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= slots.size()) {
            return ItemStack.EMPTY;
        }
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        int machineSlots = AlchemyTableBlockEntity.SLOT_COUNT;
        if (index < machineSlots) {
            if (!moveItemStackTo(stack, machineSlots, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (stack.getItem() instanceof ArtifactItem) {
            if (!moveItemStackTo(stack, 0, AlchemyTableBlockEntity.INPUT_SLOTS, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < machineSlots + 27) {
            if (!moveItemStackTo(stack, machineSlots + 27, slots.size(), false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, machineSlots, machineSlots + 27, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        return original;
    }
}
