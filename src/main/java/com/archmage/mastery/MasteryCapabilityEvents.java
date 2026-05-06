package com.archmage.mastery;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Forge event hooks that keep MasteryData alive through the player lifecycle:
 *   - Attach:   give every new player a fresh MasteryData
 *   - Clone:    copy mastery across death / dimension travel
 *   - LoggedIn: sync data to the client on join
 */
public class MasteryCapabilityEvents {

    /** Called by Forge when it sets up capabilities on any entity — we filter for players. */
    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof Player)) return;
        event.addCapability(MasteryCapability.ID, new MasteryProvider());
    }

    /**
     * Called when a player respawns or travels between dimensions.
     * keepInventory=true means it was a dimension change, not a death.
     * Either way we copy mastery — we never want to wipe progression on death.
     */
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player clone = event.getEntity();

        original.reviveCaps();
        original.getCapability(MasteryCapability.MASTERY).ifPresent(oldData ->
                clone.getCapability(MasteryCapability.MASTERY).ifPresent(newData ->
                        newData.copyFrom(oldData)
                )
        );
        original.invalidateCaps();
    }

    /**
     * Called when a player logs in or changes dimension.
     * TODO: Send a sync packet to the client so the HUD has accurate mastery data.
     */
    @SubscribeEvent
    public void onPlayerLoggedIn(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        // TODO: PacketHandler.sendToPlayer(new SyncMasteryPacket(player), player)
    }
}
