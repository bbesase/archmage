package com.archmage.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

// Full stop — extreme movement penalty + stops AI navigation each tick
public class FrozenEffect extends MobEffect {

    private static final UUID SPEED_ID = UUID.fromString("b0c1d2e3-f4a5-6789-0123-456789abcdef");

    public FrozenEffect() {
        super(MobEffectCategory.HARMFUL, 0xAAEEFF);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_ID.toString(),
            -0.99, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // Stop AI navigation every tick so mobs can't path around the speed penalty
        if (entity instanceof net.minecraft.world.entity.Mob mob) {
            mob.getNavigation().stop();
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap map, int amplifier) {
        super.addAttributeModifiers(entity, map, amplifier);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap map, int amplifier) {
        super.removeAttributeModifiers(entity, map, amplifier);
    }
}
