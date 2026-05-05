package com.archmage.mastery;

import com.archmage.elements.ElementType;
import net.minecraft.nbt.CompoundTag;

import java.util.EnumMap;
import java.util.Map;

/**
 * Per-player mastery state. Stored in NBT (Minecraft's equivalent of localStorage).
 *
 * JS equivalent:
 *   {
 *     activeElement: "fire",
 *     xp: { lightning: 1200, water: 0, fire: 3400, earth: 0, holy: 0, void: 0 },
 *     prestigeUnlocked: false
 *   }
 */
public class MasteryData {

    private final Map<ElementType, Integer> xpMap = new EnumMap<>(ElementType.class);
    private ElementType activeElement = ElementType.LIGHTNING;

    // Prestige unlock flags — set true when player meets conditions
    private boolean holyUnlocked = false;
    private boolean voidUnlocked = false;

    public MasteryData() {
        // Init all elements at 0 XP — like Object.fromEntries(elements.map(e => [e, 0]))
        for (ElementType el : ElementType.values()) {
            xpMap.put(el, 0);
        }
    }

    // --- XP & Tier ---

    public int getXp(ElementType element) {
        return xpMap.getOrDefault(element, 0);
    }

    public void addXp(ElementType element, int amount) {
        xpMap.merge(element, amount, Integer::sum);
    }

    public MasteryTier getTier(ElementType element) {
        return MasteryTier.forXp(getXp(element));
    }

    public boolean meetsRequirement(ElementType element, MasteryTier required) {
        return getTier(element).isAtLeast(required);
    }

    // --- Active Element ---

    public ElementType getActiveElement() { return activeElement; }

    public void setActiveElement(ElementType element) {
        if (element.isPrestige() && !isPrestigeUnlocked(element)) {
            throw new IllegalStateException("Prestige element " + element.id + " is not yet unlocked.");
        }
        this.activeElement = element;
    }

    // --- Prestige Unlock ---

    public boolean isPrestigeUnlocked(ElementType element) {
        if (!element.isPrestige()) return true; // non-prestige always available
        return element == ElementType.HOLY ? holyUnlocked : voidUnlocked;
    }

    public void unlockPrestige() {
        this.holyUnlocked = true;
        this.voidUnlocked = true;
    }

    // --- NBT Serialization (save/load from disk) ---

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("activeElement", activeElement.id);
        tag.putBoolean("holyUnlocked", holyUnlocked);
        tag.putBoolean("voidUnlocked", voidUnlocked);
        for (ElementType el : ElementType.values()) {
            tag.putInt("xp_" + el.id, getXp(el));
        }
        return tag;
    }

    public static MasteryData load(CompoundTag tag) {
        MasteryData data = new MasteryData();
        data.holyUnlocked = tag.getBoolean("holyUnlocked");
        data.voidUnlocked = tag.getBoolean("voidUnlocked");
        String activeId = tag.getString("activeElement");
        for (ElementType el : ElementType.values()) {
            data.xpMap.put(el, tag.getInt("xp_" + el.id));
            if (el.id.equals(activeId)) data.activeElement = el;
        }
        return data;
    }
}
