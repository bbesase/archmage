package com.archmage.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class EmpoweredEffect extends MobEffect {

    private static final UUID MODIFIER_ID = UUID.fromString("c5d6e7f8-a9b0-1234-5678-901234abcdef");
    private static final double DAMAGE_BONUS = 0.25; // +25% attack damage

    public EmpoweredEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x9933CC);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, MODIFIER_ID.toString(),
            DAMAGE_BONUS, AttributeModifier.Operation.MULTIPLY_TOTAL);
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
