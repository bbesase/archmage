package com.archmage.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

// Reduces movement speed and attack speed — general hindrance
public class CursedEffect extends MobEffect {

    private static final UUID SPEED_ID  = UUID.fromString("f8a9b0c1-d2e3-4567-8901-234567abcdef");
    private static final UUID ASPD_ID   = UUID.fromString("a9b0c1d2-e3f4-5678-9012-345678abcdef");

    public CursedEffect() {
        super(MobEffectCategory.HARMFUL, 0x550055);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_ID.toString(),
            -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, ASPD_ID.toString(),
            -0.20, AttributeModifier.Operation.MULTIPLY_TOTAL);
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
