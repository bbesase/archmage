package com.archmage.mastery;

import com.archmage.elements.ElementType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Singleton system that manages mastery data for all players.
 *
 * Use MasterySystem.get(player) to read/write a player's mastery anywhere in the codebase.
 * Think of it like a global Redux store keyed by player UUID.
 */
public class MasterySystem {

    public static void register(IEventBus modEventBus) {
        // TODO: Register capability provider so Forge attaches MasteryData to each player
    }

    /**
     * Get the mastery data for a player.
     * This reads from Forge Capabilities (attached per-player NBT data).
     */
    public static MasteryData get(Player player) {
        // TODO: Return player.getCapability(MASTERY_CAPABILITY).orElseThrow()
        // For now, returns a fresh instance (replace with capability impl)
        return new MasteryData();
    }

    /**
     * Unlock both Holy and Void for a player.
     * Called when they defeat all 4 elemental bosses AND activate the Ascension Altar.
     */
    public static void unlockPrestige(Player player) {
        MasteryData data = get(player);
        data.unlockPrestige();
        // TODO: Send unlock packet to client for UI animation
        // TODO: Sync to server NBT
    }

    /**
     * Award XP to a player's active element.
     * Called from: spell casts, kill events, boss damage events.
     */
    public static void awardXp(Player player, ElementType element, int amount) {
        MasteryData data = get(player);
        MasteryTier tierBefore = data.getTier(element);
        data.addXp(element, amount);
        MasteryTier tierAfter = data.getTier(element);

        // Notify player on tier-up
        if (tierAfter != tierBefore) {
            player.displayClientMessage(
                net.minecraft.network.chat.Component.literal(
                    "✨ " + element.id + " mastery increased to " + tierAfter.displayName + "!"
                ),
                false
            );
        }
    }
}
