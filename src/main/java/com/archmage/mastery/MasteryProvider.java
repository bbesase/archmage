package com.archmage.mastery;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Tells Forge how to attach MasteryData to a player and how to save/load it.
 *
 * JS analogy: this is like a class that implements a storage adapter —
 * it wraps MasteryData and handles the read/write (NBT) contract with Forge.
 */
public class MasteryProvider implements ICapabilitySerializable<CompoundTag> {

    private final MasteryData data = new MasteryData();
    private final LazyOptional<MasteryData> optional = LazyOptional.of(() -> data);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return MasteryCapability.MASTERY.orEmpty(cap, optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return data.save();
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        MasteryData loaded = MasteryData.load(tag);
        // Copy loaded values into the existing instance so the LazyOptional reference stays valid
        data.copyFrom(loaded);
    }
}
