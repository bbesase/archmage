package com.archmage.spells;

import com.archmage.elements.ElementType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Registry of all spells in the game.
 *
 * JS equivalent: a Map<ElementType, SpellBase[]> where each entry
 * is an array of spells sorted by tier.
 *
 * Usage:
 *   SpellRegistry.castActive(player, level, ElementType.FIRE);
 */
public class SpellRegistry {

    private static final Map<ElementType, List<SpellBase>> SPELLS = new EnumMap<>(ElementType.class);

    public static void register(IEventBus modEventBus) {
        for (ElementType el : ElementType.values()) {
            SPELLS.put(el, new ArrayList<>());
        }
        // TODO: Register all spell implementations here
        // e.g. registerSpell(new ArcBolt());     // Lightning basic
        //      registerSpell(new ChainLightning()); // Lightning advanced
        //      registerSpell(new Thunderstrike()); // Lightning legendary
    }

    public static void registerSpell(SpellBase spell) {
        SPELLS.get(spell.element).add(spell);
    }

    /**
     * Cast the active spell for an element.
     * Returns true if the cast succeeded, false if on cooldown or not enough mana.
     */
    public static boolean castActive(Player player, Level level, ElementType element) {
        List<SpellBase> spells = SPELLS.getOrDefault(element, List.of());
        if (spells.isEmpty()) return false;

        // TODO: Get player's currently selected spell slot for this element
        // For now, cast the first available spell
        SpellBase spell = spells.get(0);
        spell.cast(player, level);
        return true;
    }

    public static List<SpellBase> getSpellsForElement(ElementType element) {
        return SPELLS.getOrDefault(element, List.of());
    }
}
