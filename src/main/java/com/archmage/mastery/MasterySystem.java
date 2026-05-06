package com.archmage.mastery;

import com.archmage.elements.ElementType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.IEventBus;

public class MasterySystem {

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(MasteryCapability::register);
    }

    /**
     * Get the mastery data for a player.
     * Reads from the Forge Capability attached to the player entity.
     * Throws if called before capabilities are attached (i.e. during world load).
     */
    public static MasteryData get(Player player) {
        return player.getCapability(MasteryCapability.MASTERY)
                .orElseThrow(() -> new IllegalStateException("Player " + player.getName().getString() + " has no MasteryData capability"));
    }

    /**
     * Unlock both Holy and Void for a player.
     * Called when they defeat all 4 elemental bosses AND activate the Ascension Altar.
     */
    public static void unlockPrestige(Player player) {
        MasteryData data = get(player);
        data.unlockPrestige();
        // TODO: Send unlock packet to client for UI animation
    }

    /**
     * Award XP to a specific element's mastery track.
     * Called from: spell casts, weapon hits/kills, boss damage events.
     */
    public static void awardXp(Player player, ElementType element, int amount) {
        MasteryData data = get(player);
        MasteryTier tierBefore = data.getTier(element);
        data.addXp(element, amount);
        MasteryTier tierAfter = data.getTier(element);

        if (tierAfter != tierBefore) {
            player.displayClientMessage(
                net.minecraft.network.chat.Component.literal(
                    element.id + " mastery increased to " + tierAfter.displayName + "!"
                ),
                false
            );
        }
    }
}
