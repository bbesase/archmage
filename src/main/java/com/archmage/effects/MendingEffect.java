package com.archmage.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class MendingEffect extends MobEffect {

    private static final int HEAL_INTERVAL_TICKS = 40; // heal every 2 seconds
    private static final float HEAL_AMOUNT = 1.0f;     // 0.5 hearts per tick

    public MendingEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFAA);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.getHealth() < entity.getMaxHealth()) {
            entity.heal(HEAL_AMOUNT * (amplifier + 1));
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % HEAL_INTERVAL_TICKS == 0;
    }
}
