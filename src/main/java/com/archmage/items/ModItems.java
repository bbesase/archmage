package com.archmage.items;
import com.archmage.Archmage;
import com.archmage.elements.ElementType;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/** All item registrations. Like a big export const from an items module. */
public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, Archmage.MOD_ID);

    // Staves — one standard + one legendary per element
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
