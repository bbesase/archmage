package com.archmage.mastery;

import com.archmage.elements.ElementType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MasteryData — the per-player XP and tier state.
 * Pure logic — no Minecraft classes involved except CompoundTag in save/load,
 * so we test everything except serialization.
 */
public class MasteryDataTest {

    private MasteryData data;

    @BeforeEach
    void setUp() {
        data = new MasteryData();
    }

    @Test
    void testInitialXpIsZeroForAllElements() {
        for (ElementType type : ElementType.values()) {
            assertEquals(0, data.getXp(type),
                "Initial XP for " + type + " should be 0");
        }
    }

    @Test
    void testAddXpIncreases() {
        data.addXp(ElementType.LIGHTNING, 100);
        assertEquals(100, data.getXp(ElementType.LIGHTNING));
    }

    @Test
    void testAddXpAccumulates() {
        data.addXp(ElementType.LIGHTNING, 50);
        data.addXp(ElementType.LIGHTNING, 75);
        assertEquals(125, data.getXp(ElementType.LIGHTNING));
    }

    @Test
    void testElementsAreIndependent() {
        data.addXp(ElementType.LIGHTNING, 1000);
        for (ElementType type : ElementType.values()) {
            if (type != ElementType.LIGHTNING) {
                assertEquals(0, data.getXp(type),
                    "XP for " + type + " should still be 0 after adding LIGHTNING xp");
            }
        }
    }

    @Test
    void testGetTierAtZeroXpReturnsFirstTier() {
        MasteryTier first = MasteryTier.values()[0];
        assertEquals(first, data.getTier(ElementType.LIGHTNING),
            "At 0 XP, tier should be the first tier");
    }

    @Test
    void testGetTierAfterEnoughXp() {
        MasteryTier[] tiers = MasteryTier.values();
        if (tiers.length >= 2) {
            // Add exactly enough XP to reach the second tier
            int targetXp = tiers[1].minXp;
            data.addXp(ElementType.LIGHTNING, targetXp);
            assertEquals(tiers[1], data.getTier(ElementType.LIGHTNING),
                "After adding " + targetXp + " XP, tier should be " + tiers[1]);
        }
    }

    @Test
    void testMeetsRequirementFirstTier() {
        MasteryTier first = MasteryTier.values()[0];
        assertTrue(data.meetsRequirement(ElementType.LIGHTNING, first),
            "Fresh data always meets the first tier requirement");
    }

    @Test
    void testDoesNotMeetHigherRequirementAtZeroXp() {
        MasteryTier[] tiers = MasteryTier.values();
        if (tiers.length >= 2) {
            assertFalse(data.meetsRequirement(ElementType.LIGHTNING, tiers[1]),
                "At 0 XP, should not meet requirement for tier 2+");
        }
    }

    @Test
    void testActiveElementDefaultsToLightning() {
        assertEquals(ElementType.LIGHTNING, data.getActiveElement(),
            "Default active element should be LIGHTNING");
    }

    @Test
    void testSetActiveElementNonPrestige() {
        data.setActiveElement(ElementType.FIRE);
        assertEquals(ElementType.FIRE, data.getActiveElement());
    }

    @Test
    void testSetActiveElementPrestigeThrowsWhenLocked() {
        // Prestige elements (HOLY, VOID) are locked by default
        assertThrows(IllegalStateException.class, () -> data.setActiveElement(ElementType.HOLY),
            "Setting a locked prestige element should throw");
        assertThrows(IllegalStateException.class, () -> data.setActiveElement(ElementType.VOID),
            "Setting a locked prestige element should throw");
    }

    @Test
    void testUnlockPrestigeAllowsPrestigeElements() {
        data.unlockPrestige();
        assertDoesNotThrow(() -> data.setActiveElement(ElementType.HOLY),
            "After unlockPrestige, HOLY should be settable");
        assertDoesNotThrow(() -> data.setActiveElement(ElementType.VOID),
            "After unlockPrestige, VOID should be settable");
    }

    @Test
    void testCopyFrom() {
        data.addXp(ElementType.FIRE, 500);
        data.setActiveElement(ElementType.FIRE);

        MasteryData copy = new MasteryData();
        copy.copyFrom(data);

        assertEquals(500, copy.getXp(ElementType.FIRE), "Copied XP should match");
        assertEquals(ElementType.FIRE, copy.getActiveElement(), "Copied active element should match");
    }
}
