package com.archmage.items;

import com.archmage.elements.ElementType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Elemental melee sword. Bonus damage and mastery XP are handled by
 * MasteryBonusHandler via the archmage:melee + archmage:element/* tags.
 * This class only defines item stats and tooltip.
 */
public class ElementalSword extends SwordItem {

    private final ElementType element;

    public ElementalSword(ElementType element, Tier tier) {
        super(tier, 3, -2.4f, new Properties().stacksTo(1));
        this.element = element;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Element: " + element.id));
        tooltip.add(Component.literal("Melee attacks grant " + element.id + " mastery XP"));
    }
}
