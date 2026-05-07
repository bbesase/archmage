package com.archmage.world;

import com.archmage.items.ModItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import static com.archmage.Archmage.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VoidDropHandler {

    private static final Random RANDOM = new Random();

    // Endermen have a 50% chance to drop 1 void shard on death
    // Endermites have a 30% chance to drop 1 void shard on death
    // Shulkers have a 30% chance to drop 1 void shard on death
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        boolean isEnderman = event.getEntity() instanceof EnderMan;
        boolean isEndermite = event.getEntity() instanceof Endermite;
        boolean isShulker = event.getEntity() instanceof Shulker;

        if (!isEnderman && !isEndermite && !isShulker) return;

        float roll = RANDOM.nextFloat();
        float threshold = isEnderman ? 0.50f : 0.30f;

        if (roll < threshold) {
            ItemEntity drop = new ItemEntity(
                event.getEntity().level(),
                event.getEntity().getX(),
                event.getEntity().getY(),
                event.getEntity().getZ(),
                new ItemStack(ModItems.VOID_SHARD.get(), 1)
            );
            event.getEntity().level().addFreshEntity(drop);
        }
    }
}
