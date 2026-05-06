package com.archmage.items;

import com.archmage.elements.ElementType;
import com.archmage.mastery.MasteryData;
import com.archmage.mastery.MasterySystem;
import com.archmage.mastery.MasteryTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * One staff per element, in two tiers: standard and legendary.
 *
 * Standard staves are craftable from elemental boss drops.
 * Legendary staves require ARCHMAGE mastery to equip and
 * are only obtainable from prestige boss loot tables.
 *
 * Right-click casts the player's currently active spell for this element
 * and awards mastery XP.
 */
public class ElementalStaff extends Item {

    private final ElementType element;
    private final boolean isLegendary;

    public ElementalStaff(ElementType element, boolean isLegendary) {
        super(new Properties()
            .stacksTo(1)
            .durability(isLegendary ? 2000 : 500)
        );
        this.element = element;
        this.isLegendary = isLegendary;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // Never run game logic client-side — same pattern as checking req.isServer() in JS
        if (level.isClientSide) return InteractionResultHolder.pass(player.getItemInHand(hand));

        MasteryData mastery = MasterySystem.get(player);

        // Guard: player must be attuned to the right element
        if (mastery.getActiveElement() != this.element) {
            player.displayClientMessage(
                Component.literal("You must attune to " + element.id + " to wield this staff."),
                true // true = show in action bar, not chat
            );
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }

        // Guard: legendary staves need Archmage mastery
        if (isLegendary && !mastery.meetsRequirement(element, MasteryTier.ARCHMAGE)) {
            player.displayClientMessage(
                Component.literal("You must reach Archmage mastery in " + element.id + " first."),
                true
            );
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }

        // TODO: Wire staff casting into Iron's Spells casting system
        // For now, award XP directly as a placeholder
        mastery.addXp(this.element, isLegendary ? 25 : 10);

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Element: " + element.id));
        if (isLegendary) {
            tooltip.add(Component.literal("⭐ Legendary — requires Archmage mastery"));
        }
        tooltip.add(Component.literal("Right-click to cast your active " + element.id + " spell"));
    }
}
