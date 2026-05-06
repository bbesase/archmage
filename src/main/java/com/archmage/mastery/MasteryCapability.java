package com.archmage.mastery;

import com.archmage.Archmage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

/**
 * The capability token — think of this as the key in a Map<Key, MasteryData>.
 * Every player gets one MasteryData instance attached under this key.
 */
public class MasteryCapability {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Archmage.MOD_ID, "mastery");

    public static final Capability<MasteryData> MASTERY =
            CapabilityManager.get(new CapabilityToken<>() {});

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(MasteryData.class);
    }
}
