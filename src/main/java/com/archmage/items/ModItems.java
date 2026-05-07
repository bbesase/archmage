package com.archmage.items;

import com.archmage.Archmage;
import com.archmage.elements.ElementType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, Archmage.MOD_ID);

    // --- Crafting Materials ---
    public static final RegistryObject<Item> MITHRIL_INGOT    = ITEMS.register("mithril_ingot",    () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ADAMANTITE_INGOT = ITEMS.register("adamantite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LIGHTNING_SHARD  = ITEMS.register("lightning_shard",  () -> new Item(new Item.Properties()));
    // TODO: VOLTITE_INGOT — drops from Thunder Realm boss

    // --- Lightning Swords ---
    public static final RegistryObject<Item> LIGHTNING_SWORD_STONE      = ITEMS.register("lightning_sword_stone",      () -> new ElementalSword(ElementType.LIGHTNING, Tiers.STONE));
    public static final RegistryObject<Item> LIGHTNING_SWORD_IRON       = ITEMS.register("lightning_sword_iron",       () -> new ElementalSword(ElementType.LIGHTNING, Tiers.IRON));
    public static final RegistryObject<Item> LIGHTNING_SWORD_GOLD       = ITEMS.register("lightning_sword_gold",       () -> new ElementalSword(ElementType.LIGHTNING, Tiers.GOLD));
    public static final RegistryObject<Item> LIGHTNING_SWORD_MITHRIL    = ITEMS.register("lightning_sword_mithril",    () -> new ElementalSword(ElementType.LIGHTNING, ArchmageTier.MITHRIL));
    public static final RegistryObject<Item> LIGHTNING_SWORD_ADAMANTITE = ITEMS.register("lightning_sword_adamantite", () -> new ElementalSword(ElementType.LIGHTNING, ArchmageTier.ADAMANTITE));
    public static final RegistryObject<Item> LIGHTNING_SWORD_DIAMOND    = ITEMS.register("lightning_sword_diamond",    () -> new ElementalSword(ElementType.LIGHTNING, Tiers.DIAMOND));
    // TODO: LIGHTNING_SWORD_VOLTITE — endgame, requires Thunder Realm material

    // --- Staves — one standard + one legendary per element ---
    public static final RegistryObject<Item> LIGHTNING_STAFF   = ITEMS.register("lightning_staff",   () -> new ElementalStaff(ElementType.LIGHTNING, false));
    public static final RegistryObject<Item> LIGHTNING_STAFF_L = ITEMS.register("fulmine_staff",     () -> new ElementalStaff(ElementType.LIGHTNING, true));
    public static final RegistryObject<Item> WATER_STAFF       = ITEMS.register("water_staff",       () -> new ElementalStaff(ElementType.WATER, false));
    public static final RegistryObject<Item> WATER_STAFF_L     = ITEMS.register("glacial_scepter",   () -> new ElementalStaff(ElementType.WATER, true));
    public static final RegistryObject<Item> FIRE_STAFF        = ITEMS.register("fire_staff",        () -> new ElementalStaff(ElementType.FIRE, false));
    public static final RegistryObject<Item> FIRE_STAFF_L      = ITEMS.register("cinderstaff",       () -> new ElementalStaff(ElementType.FIRE, true));
    public static final RegistryObject<Item> EARTH_STAFF       = ITEMS.register("earth_staff",       () -> new ElementalStaff(ElementType.EARTH, false));
    public static final RegistryObject<Item> EARTH_STAFF_L     = ITEMS.register("earthen_maul",      () -> new ElementalStaff(ElementType.EARTH, true));
    public static final RegistryObject<Item> HOLY_STAFF        = ITEMS.register("holy_staff",        () -> new ElementalStaff(ElementType.HOLY, false));
    public static final RegistryObject<Item> HOLY_STAFF_L      = ITEMS.register("staff_of_eternal",  () -> new ElementalStaff(ElementType.HOLY, true));
    public static final RegistryObject<Item> VOID_STAFF        = ITEMS.register("void_staff",        () -> new ElementalStaff(ElementType.VOID, false));
    public static final RegistryObject<Item> VOID_STAFF_L      = ITEMS.register("staff_of_unmaking", () -> new ElementalStaff(ElementType.VOID, true));

    public static void register(IEventBus bus) { ITEMS.register(bus); }
}
