package com.archmage.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class SmolderingEffect extends MobEffect {

    private static final UUID MODIFIER_ID = UUID.fromString("d6e7f8a9-b0c1-2345-6789-012345abcdef");
    private static final double DAMAGE_REDUCTION = -0.20; // -20% attack damage

    public SmolderingEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF6600);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, MODIFIER_ID.toString(),
            DAMAGE_REDUCTION, AttributeModifier.Operation.MULTIPLY_TOTAL);
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
