package com.archmage.mastery;

/**
 * Five mastery tiers per element.
 * Each tier unlocks spells, gear, and passive buffs.
 *
 * Think of this like XP levels in an RPG skill tree.
 */
public enum MasteryTier {

    APPRENTICE    (0,     500,             "Apprentice",     "Basic spells unlocked"),
    ADEPT         (500,   2000,            "Adept",          "Advanced spells + elemental armor"),
    MAGE          (2000,  5000,            "Mage",           "Passive buffs: ignite/freeze/shock/root"),
    ARCHMAGE      (5000,  10000,           "Archmage",       "Legendary weapons + armor access"),
    ELEMENTAL_LORD(10000, Integer.MAX_VALUE,"Elemental Lord", "Full mastery — legendary spells + aura");

    public final int minXp;
    public final int maxXp;
    public final String displayName;
    public final String unlockDescription;

    MasteryTier(int minXp, int maxXp, String displayName, String unlockDescription) {
        this.minXp = minXp;
        this.maxXp = maxXp;
        this.displayName = displayName;
        this.unlockDescription = unlockDescription;
    }

    /** Get tier for a given XP amount — like a switch/case on XP thresholds */
    public static MasteryTier forXp(int xp) {
        for (MasteryTier tier : values()) {
            if (xp >= tier.minXp && xp < tier.maxXp) return tier;
        }
        return ELEMENTAL_LORD;
    }

    /**
     * Progress 0.0–1.0 within this tier.
     * Useful for drawing a progress bar in the HUD.
     */
    public float progress(int xp) {
        if (maxXp == Integer.MAX_VALUE) return 1.0f;
        return (float)(xp - minXp) / (maxXp - minXp);
    }

    public boolean isAtLeast(MasteryTier other) {
        return this.ordinal() >= other.ordinal();
    }
}
