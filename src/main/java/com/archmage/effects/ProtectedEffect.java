package com.archmage.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class ProtectedEffect extends MobEffect {

    private static final UUID MODIFIER_ID = UUID.fromString("b4c5d6e7-f8a9-0123-4567-890123abcdef");
    private static final double ARMOR_BONUS = 4.0; // +4 armor points

    public ProtectedEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x88BB44);
        this.addAttributeModifier(Attributes.ARMOR, MODIFIER_ID.toString(),
            ARMOR_BONUS, AttributeModifier.Operation.ADDITION);
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
