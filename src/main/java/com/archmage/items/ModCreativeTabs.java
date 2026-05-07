package com.archmage.items;

import com.archmage.Archmage;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Archmage.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ARCHMAGE_TAB = TABS.register("archmage_tab",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.archmage"))
            .icon(() -> new ItemStack(ModItems.LIGHTNING_SHARD.get()))
            .displayItems((params, output) -> {
                // Materials
                output.accept(ModItems.LIGHTNING_SHARD.get());
                output.accept(ModItems.FROST_SHARD.get());
                output.accept(ModItems.EMBER_SHARD.get());
                output.accept(ModItems.TERRA_SHARD.get());
                output.accept(ModItems.RADIANT_SHARD.get());
                output.accept(ModItems.VOID_SHARD.get());
                output.accept(ModItems.MITHRIL_INGOT.get());
                output.accept(ModItems.ADAMANTITE_INGOT.get());

                // Ores
                output.accept(ModBlocks.LIGHTNING_ORE_ITEM.get());
                output.accept(ModBlocks.FROST_ORE_ITEM.get());
                output.accept(ModBlocks.EMBER_ORE_ITEM.get());
                output.accept(ModBlocks.TERRA_ORE_ITEM.get());
                output.accept(ModBlocks.RADIANT_ORE_ITEM.get());
                output.accept(ModBlocks.VOID_ORE_ITEM.get());

                // Lightning Swords
                output.accept(ModItems.LIGHTNING_SWORD_STONE.get());
                output.accept(ModItems.LIGHTNING_SWORD_IRON.get());
                output.accept(ModItems.LIGHTNING_SWORD_GOLD.get());
                output.accept(ModItems.LIGHTNING_SWORD_MITHRIL.get());
                output.accept(ModItems.LIGHTNING_SWORD_ADAMANTITE.get());
                output.accept(ModItems.LIGHTNING_SWORD_DIAMOND.get());

                // Water Swords
                output.accept(ModItems.WATER_SWORD_STONE.get());
                output.accept(ModItems.WATER_SWORD_IRON.get());
                output.accept(ModItems.WATER_SWORD_GOLD.get());
                output.accept(ModItems.WATER_SWORD_MITHRIL.get());
                output.accept(ModItems.WATER_SWORD_ADAMANTITE.get());
                output.accept(ModItems.WATER_SWORD_DIAMOND.get());

                // Fire Swords
                output.accept(ModItems.FIRE_SWORD_STONE.get());
                output.accept(ModItems.FIRE_SWORD_IRON.get());
                output.accept(ModItems.FIRE_SWORD_GOLD.get());
                output.accept(ModItems.FIRE_SWORD_MITHRIL.get());
                output.accept(ModItems.FIRE_SWORD_ADAMANTITE.get());
                output.accept(ModItems.FIRE_SWORD_DIAMOND.get());

                // Earth Swords
                output.accept(ModItems.EARTH_SWORD_STONE.get());
                output.accept(ModItems.EARTH_SWORD_IRON.get());
                output.accept(ModItems.EARTH_SWORD_GOLD.get());
                output.accept(ModItems.EARTH_SWORD_MITHRIL.get());
                output.accept(ModItems.EARTH_SWORD_ADAMANTITE.get());
                output.accept(ModItems.EARTH_SWORD_DIAMOND.get());

                // Holy Swords
                output.accept(ModItems.HOLY_SWORD_STONE.get());
                output.accept(ModItems.HOLY_SWORD_IRON.get());
                output.accept(ModItems.HOLY_SWORD_GOLD.get());
                output.accept(ModItems.HOLY_SWORD_MITHRIL.get());
                output.accept(ModItems.HOLY_SWORD_ADAMANTITE.get());
                output.accept(ModItems.HOLY_SWORD_DIAMOND.get());

                // Void Swords
                output.accept(ModItems.VOID_SWORD_STONE.get());
                output.accept(ModItems.VOID_SWORD_IRON.get());
                output.accept(ModItems.VOID_SWORD_GOLD.get());
                output.accept(ModItems.VOID_SWORD_MITHRIL.get());
                output.accept(ModItems.VOID_SWORD_ADAMANTITE.get());
                output.accept(ModItems.VOID_SWORD_DIAMOND.get());

                // Staves
                output.accept(ModItems.LIGHTNING_STAFF.get());
                output.accept(ModItems.LIGHTNING_STAFF_L.get());
                output.accept(ModItems.WATER_STAFF.get());
                output.accept(ModItems.WATER_STAFF_L.get());
                output.accept(ModItems.FIRE_STAFF.get());
                output.accept(ModItems.FIRE_STAFF_L.get());
                output.accept(ModItems.EARTH_STAFF.get());
                output.accept(ModItems.EARTH_STAFF_L.get());
                output.accept(ModItems.HOLY_STAFF.get());
                output.accept(ModItems.HOLY_STAFF_L.get());
                output.accept(ModItems.VOID_STAFF.get());
                output.accept(ModItems.VOID_STAFF_L.get());
            })
            .build()
    );

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
