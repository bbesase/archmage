package com.archmage.items;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

/**
 * Custom item tiers that sit between vanilla tiers in the progression.
 *
 * Stat reference (vanilla):
 *   Stone:     uses=131, speed=4,  damage=1, enchant=5
 *   Iron:      uses=250, speed=6,  damage=2, enchant=14
 *   Diamond:   uses=1561,speed=8,  damage=3, enchant=10
 *   Netherite: uses=2031,speed=9,  damage=4, enchant=15
 */
public class ArchmageTier {

    /** Sits between Iron and Diamond. Crafted from Mithril Ingots (overworld ore). */
    public static final ForgeTier MITHRIL = new ForgeTier(
            3,      // harvest level (same as diamond)
            800,    // uses (durability)
            7.5f,   // mining speed
            2.5f,   // attack damage bonus
            12,     // enchantability
            net.minecraft.tags.BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(ModItems.MITHRIL_INGOT.get())
    );

    /** Sits between Mithril and Diamond. Crafted from Adamantite Ingots (deep overworld ore). */
    public static final ForgeTier ADAMANTITE = new ForgeTier(
            3,      // harvest level
            1200,   // uses (durability)
            8.0f,   // mining speed
            3.5f,   // attack damage bonus
            10,     // enchantability
            net.minecraft.tags.BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(ModItems.ADAMANTITE_INGOT.get())
    );

    // TODO: VOLTITE tier — requires Thunder Realm material, added when dimension is built
}
