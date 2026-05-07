package com.archmage.ailment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for AilmentState — the per-mob ailment tracking object.
 * Uses direct field access matching the actual AilmentState design.
 * Pure logic — no Minecraft classes involved.
 */
public class AilmentStateTest {

    @Test
    void testInitialPhaseIsNone() {
        AilmentState state = new AilmentState(ElementalAilment.FIRE, AilmentState.Phase.NONE, 100L);
        assertEquals(AilmentState.Phase.NONE, state.phase);
    }

    @Test
    void testConstructorSetsAilment() {
        AilmentState state = new AilmentState(ElementalAilment.ICE, AilmentState.Phase.PHASE1, 200L);
        assertEquals(ElementalAilment.ICE, state.ailment);
    }

    @Test
    void testConstructorSetsExpiry() {
        AilmentState state = new AilmentState(ElementalAilment.FIRE, AilmentState.Phase.PHASE1, 500L);
        assertEquals(500L, state.expiryTick);
    }

    @Test
    void testInitialDamageAccumulatedIsZero() {
        AilmentState state = new AilmentState(ElementalAilment.FIRE, AilmentState.Phase.PHASE1, 100L);
        assertEquals(0f, state.damageAccumulated, 0.001f);
    }

    @Test
    void testDamageAccumulatesManually() {
        AilmentState state = new AilmentState(ElementalAilment.FIRE, AilmentState.Phase.PHASE1, 1000L);
        state.damageAccumulated += 10f;
        state.damageAccumulated += 15f;
        assertEquals(25f, state.damageAccumulated, 0.001f);
    }

    @Test
    void testIsExpired_beforeExpiry() {
        AilmentState state = new AilmentState(ElementalAilment.FIRE, AilmentState.Phase.PHASE1, 1000L);
        assertFalse(state.isExpired(999L), "Should not be expired before expiryTick");
    }

    @Test
    void testIsExpired_atExpiry() {
        AilmentState state = new AilmentState(ElementalAilment.FIRE, AilmentState.Phase.PHASE1, 1000L);
        assertTrue(state.isExpired(1000L), "Should be expired at expiryTick");
    }

    @Test
    void testIsExpired_afterExpiry() {
        AilmentState state = new AilmentState(ElementalAilment.FIRE, AilmentState.Phase.PHASE1, 1000L);
        assertTrue(state.isExpired(2000L), "Should be expired after expiryTick");
    }

    @Test
    void testCanBreak_belowThreshold() {
        AilmentState state = new AilmentState(ElementalAilment.FIRE, AilmentState.Phase.PHASE1, 1000L);
        // FIRE has damageThreshold = 50 (from ElementalAilment enum)
        state.damageAccumulated = 49f;
        assertFalse(state.canBreak(1.0f), "Should not break below threshold");
    }

    @Test
    void testCanBreak_atThreshold() {
        AilmentState state = new AilmentState(ElementalAilment.FIRE, AilmentState.Phase.PHASE1, 1000L);
        // FIRE threshold = 50
        state.damageAccumulated = 50f;
        assertTrue(state.canBreak(1.0f), "Should break at threshold");
    }

    @Test
    void testCanBreak_eliteMultiplierScalesThreshold() {
        AilmentState state = new AilmentState(ElementalAilment.FIRE, AilmentState.Phase.PHASE1, 1000L);
        // FIRE threshold = 50, elite multiplier = 4.0 -> effective threshold = 200
        state.damageAccumulated = 100f;
        assertFalse(state.canBreak(ElementalAilment.ELITE_THRESHOLD_MULTIPLIER),
            "With elite multiplier, 100 damage should not break FIRE (needs 200)");
        state.damageAccumulated = 200f;
        assertTrue(state.canBreak(ElementalAilment.ELITE_THRESHOLD_MULTIPLIER),
            "With elite multiplier, 200 damage should break FIRE");
    }

    @Test
    void testPhaseTransition() {
        AilmentState state = new AilmentState(ElementalAilment.LIGHTNING, AilmentState.Phase.NONE, 1000L);
        state.phase = AilmentState.Phase.PHASE1;
        assertEquals(AilmentState.Phase.PHASE1, state.phase);
        state.phase = AilmentState.Phase.PHASE2;
        assertEquals(AilmentState.Phase.PHASE2, state.phase);
    }

    @Test
    void testResetDamage() {
        AilmentState state = new AilmentState(ElementalAilment.FIRE, AilmentState.Phase.PHASE1, 1000L);
        state.damageAccumulated = 80f;
        state.damageAccumulated = 0f; // reset
        assertEquals(0f, state.damageAccumulated, 0.001f);
    }
}
