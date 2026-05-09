package com.archmage.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import terrablender.api.Regions;

import static com.archmage.Archmage.MOD_ID;

/**
 * Registers Archmage's 4 elemental overworld biomes and injects them
 * into world generation via TerraBlender.
 *
 * Each biome replaces a thematically similar vanilla biome inside the
 * Archmage TerraBlender region (weight 4 out of the total region pool):
 *
 *   Stormplains      → replaces Plains / Windswept Hills   (lightning ore)
 *   Frozen Wastes    → replaces Snowy Plains / Ice Spikes  (frost ore)
 *   Scorched Wasteland → replaces Desert / Badlands        (ember ore)
 *   Ancient Grove    → replaces Jungle / Dark Forest       (terra ore)
 *
 * Radiant and Void ores have their own separate plans (dimension-based).
 */
public class ModBiomes {

    // ── Resource keys ────────────────────────────────────────────────────────
    public static final ResourceKey<Biome> STORMPLAINS =
            ResourceKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "stormplains"));

    public static final ResourceKey<Biome> FROZEN_WASTES =
            ResourceKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "frozen_wastes"));

    public static final ResourceKey<Biome> SCORCHED_WASTELAND =
            ResourceKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "scorched_wasteland"));

    public static final ResourceKey<Biome> ANCIENT_GROVE =
            ResourceKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "ancient_grove"));

    // ── Registration ─────────────────────────────────────────────────────────
    public static void register(IEventBus bus) {
        bus.addListener(ModBiomes::setup);
    }

    private static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() ->
            Regions.register(new ArchmageRegion(new ResourceLocation(MOD_ID, "overworld"), 4))
        );
    }
}
