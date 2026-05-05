package com.archmage.elements;

import java.util.EnumSet;
import java.util.Set;

/**
 * The six elements of Archmage.
 * Each element knows its own strengths and weaknesses.
 *
 * JS equivalent: a const ELEMENTS object like:
 *   { LIGHTNING: { strongAgainst: ['WATER'], weakTo: ['EARTH'], color: 0xFFD700 } }
 */
public enum ElementType {

    LIGHTNING("lightning", 0xFFD700) {
        @Override public Set<ElementType> strongAgainst() { return EnumSet.of(WATER); }
        @Override public Set<ElementType> weakTo()        { return EnumSet.of(EARTH); }
    },
    WATER("water", 0x4FC3F7) {
        @Override public Set<ElementType> strongAgainst() { return EnumSet.of(FIRE); }
        @Override public Set<ElementType> weakTo()        { return EnumSet.of(LIGHTNING); }
    },
    FIRE("fire", 0xFF6B35) {
        @Override public Set<ElementType> strongAgainst() { return EnumSet.of(EARTH); }
        @Override public Set<ElementType> weakTo()        { return EnumSet.of(WATER); }
    },
    EARTH("earth", 0x8BC34A) {
        @Override public Set<ElementType> strongAgainst() { return EnumSet.of(LIGHTNING); }
        @Override public Set<ElementType> weakTo()        { return EnumSet.of(FIRE); }
    },
    HOLY("holy", 0xFFF9C4) {
        @Override public Set<ElementType> strongAgainst() { return EnumSet.of(VOID); }
        @Override public Set<ElementType> weakTo()        { return EnumSet.of(VOID); }
        @Override public boolean isPrestige()             { return true; }
    },
    VOID("void", 0x4A148C) {
        @Override public Set<ElementType> strongAgainst() { return EnumSet.of(HOLY); }
        @Override public Set<ElementType> weakTo()        { return EnumSet.of(HOLY); }
        @Override public boolean isPrestige()             { return true; }
    };

    public final String id;
    public final int color; // hex color for particles + UI tinting

    ElementType(String id, int color) {
        this.id = id;
        this.color = color;
    }

    public abstract Set<ElementType> strongAgainst();
    public abstract Set<ElementType> weakTo();

    /** Holy and Void are prestige elements — unlocked late game */
    public boolean isPrestige() { return false; }

    /**
     * Damage multiplier — like a Pokemon type chart.
     *   2.0x = super effective
     *   0.5x = not very effective
     *   1.0x = neutral
     */
    public float getDamageMultiplier(ElementType defender) {
        if (this.strongAgainst().contains(defender)) return 2.0f;
        if (this.weakTo().contains(defender))        return 0.5f;
        return 1.0f;
    }
}
