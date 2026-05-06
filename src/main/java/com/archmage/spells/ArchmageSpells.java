package com.archmage.spells;

import com.archmage.spells.lightning.ArcBoltSpell;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.archmage.Archmage.MOD_ID;

public class ArchmageSpells {

    private static final DeferredRegister<AbstractSpell> SPELLS =
            DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, MOD_ID);

    // Lightning
    public static final RegistryObject<AbstractSpell> ARC_BOLT =
            SPELLS.register("arc_bolt", ArcBoltSpell::new);

    public static void register(IEventBus modEventBus) {
        SPELLS.register(modEventBus);
    }
}
