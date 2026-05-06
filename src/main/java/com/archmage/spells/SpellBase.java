package com.archmage.spells;

import com.archmage.elements.ElementType;
import com.archmage.mastery.MasteryTier;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;

/**
 * Base for all Archmage spells. Extends Iron's Spells AbstractSpell and adds
 * our ElementType and MasteryTier so the mastery system can gate spell access
 * and award XP on cast.
 */
public abstract class SpellBase extends AbstractSpell {

    public final ElementType element;
    public final MasteryTier requiredTier;

    protected SpellBase(ElementType element, MasteryTier requiredTier) {
        this.element = element;
        this.requiredTier = requiredTier;
    }

    public boolean isLegendary() {
        return requiredTier == MasteryTier.ELEMENTAL_LORD;
    }
}
