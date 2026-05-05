package com.archmage.bosses;

import com.archmage.elements.ElementType;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

/**
 * Base class for all 6 Archmage bosses.
 *
 * Phase system:
 *   Phase 1 (100%–67% HP): Standard attacks
 *   Phase 2 (66%–34% HP): New mechanics unlock, speed increases
 *   Phase 3 (33%–0% HP): Desperate mode — highest damage, special abilities
 *
 * Holy + Void bosses override isImmuneToElementalWeakness() = true,
 * meaning no element deals 2x damage to them.
 * They compensate with richer loot and prestige unlock on death.
 */
public abstract class ElementalBoss extends Monster {

    protected final ElementType element;
    protected int phase = 1;

    // Boss health bar shown to all nearby players
    private final ServerBossEvent bossEvent;

    protected ElementalBoss(ElementType element, EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.element = element;

        // Prestige bosses are harder and award more XP
        this.xpReward = element.isPrestige() ? 2000 : 500;

        this.bossEvent = new ServerBossEvent(
            Component.literal(getBossName()),
            BossEvent.BossBarColor.PURPLE,
            BossEvent.BossBarOverlay.PROGRESS
        );
    }

    /** Display name shown in the boss health bar */
    protected abstract String getBossName();

    /**
     * Called each time the boss transitions to the next phase.
     * Override to add phase-specific mechanics (summon adds, speed boost, etc.)
     */
    protected abstract void onPhaseTransition(int newPhase);

    /**
     * Prestige bosses (Holy/Void) are immune to elemental damage multipliers.
     * No element is super effective against them — skill over stats.
     */
    public boolean isImmuneToElementalWeakness() {
        return element.isPrestige();
    }

    /**
     * Called when this boss dies. Subclasses should:
     * 1. Drop unique loot
     * 2. Trigger world events (unlock altar, open portal, etc.)
     * 3. If prestige boss — call MasterySystem.unlockPrestige(player)
     */
    protected abstract void onBossDeath(Player killer);

    // --- Boss bar lifecycle ---

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossEvent.removePlayer(player);
    }

    // --- Phase transition logic ---

    @Override
    public void tick() {
        super.tick();

        // Update boss bar progress
        if (!level().isClientSide) {
            bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            checkPhaseTransition();
        }
    }

    private void checkPhaseTransition() {
        float hpPercent = this.getHealth() / this.getMaxHealth();
        int expectedPhase = hpPercent > 0.66f ? 1 : hpPercent > 0.33f ? 2 : 3;

        if (expectedPhase > phase) {
            phase = expectedPhase;
            onPhaseTransition(phase);
        }
    }

    public int getCurrentPhase() { return phase; }
    public ElementType getElement() { return element; }
}
