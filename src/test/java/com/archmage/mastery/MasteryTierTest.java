package com.archmage.mastery;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MasteryTier enum correctness.
 * Pure logic — no Minecraft classes involved.
 */
public class MasteryTierTest {

    @Test
    void testTierOrderByMinXp() {
        MasteryTier[] tiers = MasteryTier.values();
        for (int i = 0; i < tiers.length - 1; i++) {
            assertTrue(tiers[i].minXp < tiers[i + 1].minXp,
                "Tier " + tiers[i] + " minXp (" + tiers[i].minXp + ") should be less than "
                    + tiers[i + 1] + " minXp (" + tiers[i + 1].minXp + ")");
        }
    }

    @Test
    void testFirstTierStartsAtZero() {
        MasteryTier first = MasteryTier.values()[0];
        assertEquals(0, first.minXp, "First tier should start at 0 XP");
    }

    @Test
    void testForXp_zeroReturnsFirstTier() {
        MasteryTier first = MasteryTier.values()[0];
        assertEquals(first, MasteryTier.forXp(0));
    }

    @Test
    void testForXp_exactBoundary() {
        // At exactly the minXp of each tier, forXp should return that tier
        for (MasteryTier tier : MasteryTier.values()) {
            assertEquals(tier, MasteryTier.forXp(tier.minXp),
                "forXp(" + tier.minXp + ") should return " + tier);
        }
    }

    @Test
    void testForXp_belowBoundary() {
        MasteryTier[] tiers = MasteryTier.values();
        // Just below the second tier's minXp should return the first tier
        if (tiers.length >= 2 && tiers[1].minXp > 0) {
            assertEquals(tiers[0], MasteryTier.forXp(tiers[1].minXp - 1),
                "Just below tier 2 boundary should return tier 1");
        }
    }

    @Test
    void testForXp_highValueReturnsLastTier() {
        MasteryTier last = MasteryTier.values()[MasteryTier.values().length - 1];
        assertEquals(last, MasteryTier.forXp(Integer.MAX_VALUE),
            "Very high XP should return the last tier");
    }

    @Test
    void testIsAtLeast_sameReturnsTrue() {
        for (MasteryTier tier : MasteryTier.values()) {
            assertTrue(tier.isAtLeast(tier), tier + ".isAtLeast(" + tier + ") should be true");
        }
    }

    @Test
    void testIsAtLeast_higherReturnsFalse() {
        MasteryTier[] tiers = MasteryTier.values();
        if (tiers.length >= 2) {
            assertFalse(tiers[0].isAtLeast(tiers[1]),
                tiers[0] + ".isAtLeast(" + tiers[1] + ") should be false");
        }
    }

    @Test
    void testProgress_firstTierAtMinXp() {
        MasteryTier first = MasteryTier.values()[0];
        float progress = first.progress(first.minXp);
        assertEquals(0.0f, progress, 0.001f, "At minXp progress should be 0");
    }

    @Test
    void testProgress_lastTierReturnsOne() {
        MasteryTier last = MasteryTier.values()[MasteryTier.values().length - 1];
        assertEquals(1.0f, last.progress(last.minXp), 0.001f,
            "Last tier (maxXp = MAX_VALUE) progress should always be 1.0");
    }

    @Test
    void testDisplayNameNotNull() {
        for (MasteryTier tier : MasteryTier.values()) {
            assertNotNull(tier.displayName, tier + " should have a non-null display name");
            assertFalse(tier.displayName.isEmpty(), tier + " display name should not be empty");
        }
    }
}
