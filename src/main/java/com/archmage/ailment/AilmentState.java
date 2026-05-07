package com.archmage.ailment;

/**
 * Tracks the active ailment on a single mob.
 * Stored in AilmentTracker's WeakHashMap — no persistence needed.
 */
public class AilmentState {

    public enum Phase { NONE, PHASE1, PHASE2 }

    public ElementalAilment ailment;
    public Phase phase = Phase.NONE;
    public float damageAccumulated = 0f;
    public long expiryTick = 0L;

    public AilmentState(ElementalAilment ailment, Phase phase, long expiryTick) {
        this.ailment = ailment;
        this.phase = phase;
        this.expiryTick = expiryTick;
    }

    public boolean isExpired(long currentTick) {
        return currentTick >= expiryTick;
    }

    public boolean canBreak(float eliteMultiplier) {
        int threshold = (int)(ailment.damageThreshold * eliteMultiplier);
        return damageAccumulated >= threshold;
    }
}
