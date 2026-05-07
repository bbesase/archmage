package com.archmage.effects;

import com.archmage.Archmage;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
        DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Archmage.MOD_ID);

    // --- Player buffs (Phase 1) ---
    public static final RegistryObject<MobEffect> CHARGED   = EFFECTS.register("charged",   ChargedEffect::new);
    public static final RegistryObject<MobEffect> PROTECTED = EFFECTS.register("protected", ProtectedEffect::new);
    public static final RegistryObject<MobEffect> EMPOWERED = EFFECTS.register("empowered", EmpoweredEffect::new);
    public static final RegistryObject<MobEffect> MENDING   = EFFECTS.register("mending",   MendingEffect::new);

    // --- Mob debuffs (Phase 1 on mob) ---
    public static final RegistryObject<MobEffect> SMOLDERING  = EFFECTS.register("smoldering",  SmolderingEffect::new);
    public static final RegistryObject<MobEffect> VULNERABLE  = EFFECTS.register("vulnerable",  VulnerableEffect::new);
    public static final RegistryObject<MobEffect> CURSED      = EFFECTS.register("cursed",      CursedEffect::new);
    public static final RegistryObject<MobEffect> FROZEN      = EFFECTS.register("frozen",      FrozenEffect::new);
    public static final RegistryObject<MobEffect> SHOCKED     = EFFECTS.register("shocked",     ShockedEffect::new);

    public static void register(IEventBus bus) { EFFECTS.register(bus); }
}
