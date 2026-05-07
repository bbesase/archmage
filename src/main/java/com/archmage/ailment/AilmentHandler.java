package com.archmage.ailment;

import com.archmage.effects.ModEffects;
import com.archmage.elements.ElementType;
import com.archmage.mastery.MasteryData;
import com.archmage.mastery.MasterySystem;
import com.archmage.util.ArchmageTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;
import java.util.WeakHashMap;

import static com.archmage.Archmage.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AilmentHandler {

    private static final WeakHashMap<LivingEntity, AilmentState> MOB_AILMENTS = new WeakHashMap<>();
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        LivingEntity target = event.getEntity();
        float damage = event.getAmount();
        ItemStack held = player.getMainHandItem();

        MasteryData mastery = MasterySystem.get(player);
        long currentTick = player.level().getGameTime();

        // Check each element the player has Adept+ mastery in
        for (ElementType element : ElementType.values()) {
            if (!mastery.meetsRequirement(element, ElementalAilment.UNLOCK_TIER)) continue;
            if (!isElementalWeapon(held, element)) continue;

            ElementalAilment ailment = ElementalAilment.forElement(element);
            if (ailment == null) continue;

            processAilment(player, target, ailment, damage, currentTick);
        }
    }

    private static void processAilment(Player player, LivingEntity target,
                                       ElementalAilment ailment, float damage, long currentTick) {
        // Lightning: player buff + separate shock roll — no mob threshold
        if (ailment == ElementalAilment.LIGHTNING) {
            if (RANDOM.nextFloat() < ailment.phase1Chance) {
                applyChargedToPlayer(player, ailment);
            }
            if (RANDOM.nextFloat() < ailment.shockChance) {
                applyPhase2(target, ailment);
            }
            return;
        }

        AilmentState state = MOB_AILMENTS.get(target);
        float eliteMultiplier = isElite(target) ? ElementalAilment.ELITE_THRESHOLD_MULTIPLIER : 1.0f;

        // Clean up expired states
        if (state != null && state.isExpired(currentTick)) {
            MOB_AILMENTS.remove(target);
            state = null;
        }

        if (state == null || state.phase == AilmentState.Phase.NONE) {
            // Roll for Phase 1
            if (RANDOM.nextFloat() < ailment.phase1Chance) {
                long expiry = currentTick + ailment.durationTicks(ailment.phase1DurationSecs);
                state = new AilmentState(ailment, AilmentState.Phase.PHASE1, expiry);
                MOB_AILMENTS.put(target, state);
                applyPhase1(target, ailment);
            }
        } else if (state.phase == AilmentState.Phase.PHASE1 && state.ailment == ailment) {
            // Accumulate damage toward Phase 2 threshold
            state.damageAccumulated += damage;
            state.expiryTick = currentTick + ailment.durationTicks(ailment.phase1DurationSecs);

            if (state.canBreak(eliteMultiplier)) {
                state.phase = AilmentState.Phase.PHASE2;
                state.expiryTick = currentTick + ailment.durationTicks(ailment.phase2DurationSecs);
                removePhase1(target, ailment);
                applyPhase2(target, ailment);
            }
        } else if (state.ailment != ailment) {
            // Newer ailment overwrites — roll for Phase 1 of the new element
            if (RANDOM.nextFloat() < ailment.phase1Chance) {
                removePhase1(target, state.ailment);
                long expiry = currentTick + ailment.durationTicks(ailment.phase1DurationSecs);
                state = new AilmentState(ailment, AilmentState.Phase.PHASE1, expiry);
                MOB_AILMENTS.put(target, state);
                applyPhase1(target, ailment);
            }
        }
    }

    private static void applyChargedToPlayer(Player player, ElementalAilment ailment) {
        player.addEffect(new MobEffectInstance(
            ModEffects.CHARGED.get(),
            ailment.durationTicks(ailment.phase1DurationSecs), 0, false, true));
    }

    private static void applyPhase1(LivingEntity target, ElementalAilment ailment) {
        int ticks = ailment.durationTicks(ailment.phase1DurationSecs);
        switch (ailment) {
            case ICE  -> target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ticks, 1, false, true));
            case FIRE -> target.addEffect(new MobEffectInstance(ModEffects.SMOLDERING.get(), ticks, 0, false, true));
            case EARTH -> {} // Earth Phase 1 is a player buff — handled by MasteryBonusHandler TODO
            case HOLY  -> {} // Holy Phase 1 is a player heal — handled by MasteryBonusHandler TODO
            case VOID  -> {} // Void Phase 1 is a player buff — handled separately TODO
            default -> {}
        }
    }

    private static void applyPhase2(LivingEntity target, ElementalAilment ailment) {
        int ticks = ailment.durationTicks(ailment.phase2DurationSecs);
        switch (ailment) {
            case LIGHTNING -> target.addEffect(new MobEffectInstance(ModEffects.SHOCKED.get(), ticks, 0, false, true));
            case ICE       -> target.addEffect(new MobEffectInstance(ModEffects.FROZEN.get(), ticks, 0, false, true));
            case FIRE      -> target.setSecondsOnFire(ailment.phase2DurationSecs);
            case EARTH     -> target.addEffect(new MobEffectInstance(MobEffects.POISON, ticks, 0, false, true));
            case HOLY      -> target.addEffect(new MobEffectInstance(ModEffects.VULNERABLE.get(), ticks, 0, false, true));
            case VOID      -> target.addEffect(new MobEffectInstance(ModEffects.CURSED.get(), ticks, 0, false, true));
        }
    }

    private static void removePhase1(LivingEntity target, ElementalAilment ailment) {
        switch (ailment) {
            case ICE  -> target.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            case FIRE -> target.removeEffect(ModEffects.SMOLDERING.get());
            default -> {}
        }
    }

    private static boolean isElementalWeapon(ItemStack stack, ElementType element) {
        return stack.is(ArchmageTags.elementTag(element));
    }

    // Boss/elite detection — anything with more than 100 max health qualifies
    private static boolean isElite(LivingEntity entity) {
        return entity.getMaxHealth() > 100f;
    }
}
