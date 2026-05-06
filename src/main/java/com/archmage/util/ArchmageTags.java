package com.archmage.util;

import com.archmage.elements.ElementType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ArchmageTags {

    // Weapon type tags
    public static final TagKey<Item> MELEE  = tag("melee");
    public static final TagKey<Item> CASTER = tag("caster");
    public static final TagKey<Item> RANGED = tag("ranged");

    // Element tags
    public static final TagKey<Item> ELEMENT_LIGHTNING = tag("element/lightning");
    public static final TagKey<Item> ELEMENT_FIRE      = tag("element/fire");
    public static final TagKey<Item> ELEMENT_WATER     = tag("element/water");
    public static final TagKey<Item> ELEMENT_EARTH     = tag("element/earth");
    public static final TagKey<Item> ELEMENT_HOLY      = tag("element/holy");
    public static final TagKey<Item> ELEMENT_VOID      = tag("element/void");

    public static TagKey<Item> elementTag(ElementType element) {
        return switch (element) {
            case LIGHTNING -> ELEMENT_LIGHTNING;
            case FIRE      -> ELEMENT_FIRE;
            case WATER     -> ELEMENT_WATER;
            case EARTH     -> ELEMENT_EARTH;
            case HOLY      -> ELEMENT_HOLY;
            case VOID      -> ELEMENT_VOID;
        };
    }

    private static TagKey<Item> tag(String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("archmage", path));
    }
}
