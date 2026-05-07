package com.archmage.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

// Reduces armor — mob takes more damage from all sources
public class VulnerableEffect extends MobEffect {

    private static final UUID MODIFIER_ID = UUID.fromString("e7f8a9b0-c1d2-3456-7890-123456abcdef");
    private static final double ARMOR_REDUCTION = -6.0;

    public VulnerableEffect() {
        super(MobEffectCategory.HARMFUL, 0xFFDD00);
        this.addAttributeModifier(Attributes.ARMOR, MODIFIER_ID.toString(),
            ARMOR_REDUCTION, AttributeModifier.Operation.ADDITION);
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
