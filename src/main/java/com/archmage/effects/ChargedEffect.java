package com.archmage.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class ChargedEffect extends MobEffect {

    private static final UUID MODIFIER_ID = UUID.fromString("a3b4c5d6-e7f8-9012-3456-78901234abcd");
    private static final double ATTACK_SPEED_BONUS = 0.3; // +30% attack speed

    public ChargedEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFF44);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, MODIFIER_ID.toString(),
            ATTACK_SPEED_BONUS, AttributeModifier.Operation.MULTIPLY_TOTAL);
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
