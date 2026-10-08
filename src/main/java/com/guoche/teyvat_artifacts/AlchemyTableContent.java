package com.guoche.teyvat_artifacts;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

public final class AlchemyTableContent {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TeyvatArtifacts.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TeyvatArtifacts.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TeyvatArtifacts.MODID);
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, TeyvatArtifacts.MODID);
    private static final DeferredRegister<MapCodec<? extends net.minecraft.world.level.block.Block>> BLOCK_TYPES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_TYPE, TeyvatArtifacts.MODID);
    private static final DeferredHolder<MapCodec<? extends net.minecraft.world.level.block.Block>, MapCodec<AlchemyTableBlock>> BLOCK_CODEC =
            BLOCK_TYPES.register("alchemy_table", () -> AlchemyTableBlock.CODEC);

    public static final DeferredBlock<AlchemyTableBlock> BLOCK =
            BLOCKS.register("alchemy_table", () -> new AlchemyTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL).strength(3.5F).sound(SoundType.METAL).noOcclusion()
                    .requiresCorrectToolForDrops().pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Item, AlchemyTableItem> ITEM =
            ITEMS.register("alchemy_table", () -> new AlchemyTableItem(BLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AlchemyTableBlockEntity>> BLOCK_ENTITY =
            BLOCK_ENTITIES.register("alchemy_table", () -> BlockEntityType.Builder.of(
                    AlchemyTableBlockEntity::new, BLOCK.get()).build(null));
    public static final DeferredHolder<MenuType<?>, MenuType<AlchemyTableMenu>> MENU =
            MENUS.register("alchemy_table", () -> new MenuType<>(AlchemyTableMenu::new, FeatureFlags.DEFAULT_FLAGS));

    private AlchemyTableContent() {
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        MENUS.register(bus);
        BLOCK_TYPES.register(bus);
        bus.addListener(AlchemyTableContent::registerCapabilities);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BLOCK_ENTITY.get(),
                (table, side) -> table.itemHandler(side));
    }

}
