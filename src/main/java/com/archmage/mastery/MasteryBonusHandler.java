package com.archmage.mastery;

import com.archmage.elements.ElementType;
import com.archmage.util.ArchmageTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * Applies mastery-based damage bonuses and awards XP when players attack with
 * element-tagged weapons. Registered on the Forge event bus in Archmage.java.
 *
 * Weapon must have BOTH a type tag (melee/caster/ranged) AND an element tag
 * (element/lightning, etc.) to trigger any effect.
 */
public class MasteryBonusHandler {

    // XP per hit and per kill, by weapon type
    private static final int XP_MELEE_HIT    = 3;
    private static final int XP_MELEE_KILL   = 15;
    private static final int XP_RANGED_HIT   = 4;
    private static final int XP_RANGED_KILL  = 20;

    // Flat bonus damage added per mastery tier [APPRENTICE, ADEPT, MAGE, ARCHMAGE, ELEMENTAL_LORD]
    private static final float[] MELEE_BONUS  = { 0f, 0.5f, 1.5f, 3.0f, 5.0f, 8.0f };
    private static final float[] RANGED_BONUS = { 0f, 0.5f, 1.5f, 2.5f, 4.0f, 6.0f };

    // Number of chains for ranged at each tier (Mage=1, Archmage=2, Elemental Lord=3)
    private static final int[] RANGED_CHAINS  = { 0, 1, 1, 2, 2, 3 };

    // Each chain hop deals 60% of the previous hit's damage
    private static final float CHAIN_FALLOFF = 0.6f;
    private static final float CHAIN_RADIUS  = 8f;

    // TODO: Caster spell damage bonus (+0/15/30/50/75% per tier) will be wired in
    // when Iron's Spells spell power attributes are integrated (feat/mastery-spell-power).

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof Player player)) return;
        if (player.level().isClientSide) return;

        ItemStack weapon = player.getMainHandItem();
        ElementType element = getWeaponElement(weapon);
        if (element == null) return;

        MasteryData mastery = MasterySystem.get(player);
        int tier = mastery.getTier(element).ordinal();

        if (weapon.is(ArchmageTags.MELEE)) {
            float bonus = MELEE_BONUS[Math.min(tier, MELEE_BONUS.length - 1)];
            if (bonus > 0) event.setAmount(event.getAmount() + bonus);
            MasterySystem.awardXp(player, element, XP_MELEE_HIT);
        } else if (weapon.is(ArchmageTags.RANGED) && event.getSource().getDirectEntity() instanceof AbstractArrow) {
            float bonus = RANGED_BONUS[Math.min(tier, RANGED_BONUS.length - 1)];
            if (bonus > 0) event.setAmount(event.getAmount() + bonus);
            int chains = RANGED_CHAINS[Math.min(tier, RANGED_CHAINS.length - 1)];
            if (chains > 0) applyChain(player, event.getEntity(), event.getAmount(), chains);
            MasterySystem.awardXp(player, element, XP_RANGED_HIT);
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof Player player)) return;
        if (player.level().isClientSide) return;

        ItemStack weapon = player.getMainHandItem();
        ElementType element = getWeaponElement(weapon);
        if (element == null) return;

        if (weapon.is(ArchmageTags.MELEE)) {
            MasterySystem.awardXp(player, element, XP_MELEE_KILL);
        } else if (weapon.is(ArchmageTags.RANGED)) {
            MasterySystem.awardXp(player, element, XP_RANGED_KILL);
        }
    }

    /** Returns the element for this weapon, or null if it has no element tag. */
    private ElementType getWeaponElement(ItemStack stack) {
        for (ElementType element : ElementType.values()) {
            if (stack.is(ArchmageTags.elementTag(element))) return element;
        }
        return null;
    }

    /** Recursively bounces damage to the nearest unhit target within CHAIN_RADIUS. */
    private void applyChain(Player player, Entity origin, float damage, int chainsLeft) {
        if (chainsLeft <= 0) return;
        float chainDamage = damage * CHAIN_FALLOFF;

        List<Entity> nearby = origin.level().getEntities(
                origin,
                AABB.ofSize(origin.position(), CHAIN_RADIUS * 2, CHAIN_RADIUS * 2, CHAIN_RADIUS * 2),
                e -> e != origin && e != player && e.isAlive() && e.isPickable()
        );

        if (nearby.isEmpty()) return;
        Entity next = nearby.get(0);
        next.hurt(player.level().damageSources().playerAttack(player), chainDamage);
        applyChain(player, next, chainDamage, chainsLeft - 1);
    }
}
