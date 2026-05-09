package com.archmage.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.archmage.Archmage.MOD_ID;

/**
 * Drives the Stormplains lightning mechanic.
 *
 * Every server tick, each player standing inside a Stormplains biome
 * has a 1-in-400 chance (~1 strike per 20 s on average) of triggering
 * an extra lightning bolt somewhere within 64 blocks of them.
 *
 * The bolt uses vanilla lightning damage (~5 HP) — painful but not lethal
 * on full health, per the design spec.  It strikes the surface so it
 * never spawns inside a mountain or cave.
 */
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StormplainsHandler {

    /** Average gap between strikes for each player in the biome (ticks @ 20 TPS = ~20 s). */
    private static final int STRIKE_CHANCE = 400;

    /** Maximum horizontal distance from player where a bolt can land (blocks). */
    private static final int STRIKE_RADIUS = 64;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        for (ServerLevel level : event.getServer().getAllLevels()) {
            // Only fire in the overworld — dimension is not yet added, nothing to skip elsewhere
            if (!level.dimension().equals(Level.OVERWORLD)) continue;

            for (ServerPlayer player : level.players()) {
                // Is this player inside a Stormplains biome?
                if (!level.getBiome(player.blockPosition()).is(ModBiomes.STORMPLAINS)) continue;

                // Random strike — roughly one per STRIKE_CHANCE ticks per player
                if (level.random.nextInt(STRIKE_CHANCE) != 0) continue;

                spawnStrike(level, player);
            }
        }
    }

    private static void spawnStrike(ServerLevel level, ServerPlayer player) {
        int range = STRIKE_RADIUS;
        int offsetX = level.random.nextInt(range * 2) - range;
        int offsetZ = level.random.nextInt(range * 2) - range;

        int x = player.getBlockX() + offsetX;
        int z = player.getBlockZ() + offsetZ;

        // Step up to the surface so the bolt doesn't spawn underground
        BlockPos surface = level.getHeightmapPos(
            Heightmap.Types.MOTION_BLOCKING,
            new BlockPos(x, 0, z)
        );

        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt == null) return;

        bolt.moveTo(surface.getX() + 0.5, surface.getY(), surface.getZ() + 0.5);
        // visualOnly = false so the bolt deals damage — this is intentional
        bolt.setVisualOnly(false);
        level.addFreshEntity(bolt);
    }
}
