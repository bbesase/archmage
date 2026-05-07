package com.archmage.ailment;

import com.archmage.elements.ElementType;
import com.archmage.mastery.MasteryTier;

/**
 * Defines both phases of each element's ailment.
 *
 * Phase 1: proc chance on hit, requires Adept+ mastery in that element.
 * Phase 2: triggers when flat damage threshold is reached while Phase 1 is active.
 *
 * Lightning is special — Phase 1 buffs the player (no mob threshold).
 * Shock is a separate roll on each hit.
 */
public enum ElementalAilment {

    //                       element            p1Chance  shockChance  threshold  p1Dur  p2Dur
    LIGHTNING (ElementType.LIGHTNING,            0.25f,    0.10f,       0,         5,     2  ),
    ICE       (ElementType.WATER,                0.30f,    0f,          40,        6,     4  ),
    FIRE      (ElementType.FIRE,                 0.30f,    0f,          50,        6,     5  ),
    EARTH     (ElementType.EARTH,                0.25f,    0f,          45,        8,     6  ),
    HOLY      (ElementType.HOLY,                 0.20f,    0f,          60,        8,     6  ),
    VOID      (ElementType.VOID,                 0.25f,    0f,          55,        6,     5  );

    public final ElementType element;
    /** Chance to apply Phase 1 on hit (0.0–1.0) */
    public final float phase1Chance;
    /** Lightning only — chance to shock on hit */
    public final float shockChance;
    /** Flat damage needed while Phase 1 is active to trigger Phase 2. 0 = lightning special case. */
    public final int damageThreshold;
    /** Phase 1 duration in seconds */
    public final int phase1DurationSecs;
    /** Phase 2 duration in seconds */
    public final int phase2DurationSecs;

    /** Minimum mastery required to proc ailments for this element */
    public static final MasteryTier UNLOCK_TIER = MasteryTier.ADEPT;

    /** Multiplier applied to thresholds for elite/boss mobs */
    public static final float ELITE_THRESHOLD_MULTIPLIER = 4.0f;

    ElementalAilment(ElementType element, float phase1Chance, float shockChance,
                     int damageThreshold, int phase1DurationSecs, int phase2DurationSecs) {
        this.element = element;
        this.phase1Chance = phase1Chance;
        this.shockChance = shockChance;
        this.damageThreshold = damageThreshold;
        this.phase1DurationSecs = phase1DurationSecs;
        this.phase2DurationSecs = phase2DurationSecs;
    }

    public static ElementalAilment forElement(ElementType element) {
        for (ElementalAilment a : values()) {
            if (a.element == element) return a;
        }
        return null;
    }

    public int durationTicks(int secs) { return secs * 20; }
}
