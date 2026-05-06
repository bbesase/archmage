package com.archmage;

import com.archmage.core.ElementRegistry;
import com.archmage.elements.ElementType;
import com.archmage.items.ModBlocks;
import com.archmage.items.ModItems;
import com.archmage.entities.ModEntities;
import com.archmage.world.ModBiomes;
import com.archmage.spells.ArchmageSpells;
import com.archmage.mastery.MasteryBonusHandler;
import com.archmage.mastery.MasteryCapabilityEvents;
import com.archmage.mastery.MasterySystem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Archmage — main mod entry point.
 * Think of this like index.js: it bootstraps everything.
 */
@Mod(Archmage.MOD_ID)
public class Archmage {

    public static final String MOD_ID = "archmage";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Archmage() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register all mod content (like importing and calling init() on each module)
        ElementRegistry.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModBiomes.register(modEventBus);
        ArchmageSpells.register(modEventBus);
        MasterySystem.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new MasteryBonusHandler());
        MinecraftForge.EVENT_BUS.register(new MasteryCapabilityEvents());
        LOGGER.info("Archmage initialized. The elements await.");
    }
}
