package com.archmage.spells;

import com.archmage.elements.ElementType;
import com.archmage.mastery.MasteryTier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Abstract base for all Archmage spells.
 *
 * JS equivalent: an abstract class with a cast() method
 * that subclasses must implement — like an interface with defaults.
 *
 * Each element has 4 spells:
 *   - Basic (Apprentice)
 *   - Advanced x2 (Adept/Mage)
 *   - Legendary (Elemental Lord)
 */
public abstract class SpellBase {

    public final String id;
    public final String displayName;
    public final ElementType element;
    public final MasteryTier requiredTier; // minimum mastery to cast
    public final int manaCost;
    public final int cooldownTicks; // 20 ticks = 1 second

    protected SpellBase(String id, String displayName, ElementType element,
                        MasteryTier requiredTier, int manaCost, int cooldownTicks) {
        this.id = id;
        this.displayName = displayName;
        this.element = element;
        this.requiredTier = requiredTier;
        this.manaCost = manaCost;
        this.cooldownTicks = cooldownTicks;
    }

    /**
     * The actual spell logic. Called on the server side only.
     * @param player The caster
     * @param level  The world
     */
    public abstract void cast(Player player, Level level);

    /**
     * Spawn visual hit effects (particles, sounds).
     * Called client-side at the point of impact.
     * Override in each spell for custom VFX.
     */
    public void spawnHitEffects(Level level, double x, double y, double z) {}

    /**
     * Whether this is a legendary spell (requires Elemental Lord tier).
     * Legendary spells drop only from prestige bosses.
     */
    public boolean isLegendary() {
        return requiredTier == MasteryTier.ELEMENTAL_LORD;
    }
}
