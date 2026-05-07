package com.archmage.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

// Mini stun — very short duration, stops movement and attacks
public class ShockedEffect extends MobEffect {

    private static final UUID SPEED_ID = UUID.fromString("c1d2e3f4-a5b6-7890-1234-567890abcdef");
    private static final UUID ASPD_ID  = UUID.fromString("d2e3f4a5-b6c7-8901-2345-678901abcdef");

    public ShockedEffect() {
        super(MobEffectCategory.HARMFUL, 0xFFFF00);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_ID.toString(),
            -0.99, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, ASPD_ID.toString(),
            -0.99, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
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
