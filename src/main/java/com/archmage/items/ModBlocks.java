package com.archmage.items;

import com.archmage.Archmage;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, Archmage.MOD_ID);

    public static final RegistryObject<Block> LIGHTNING_ORE = BLOCKS.register("lightning_ore",
        () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(3.0f, 3.0f)
            .sound(SoundType.STONE)));

    public static final RegistryObject<Item> LIGHTNING_ORE_ITEM = ModItems.ITEMS.register("lightning_ore",
        () -> new BlockItem(LIGHTNING_ORE.get(), new Item.Properties()));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
