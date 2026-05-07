package com.archmage.world;

import com.archmage.items.ModItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import static com.archmage.Archmage.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EmberDropHandler {

    private static final Random RANDOM = new Random();

    // Blazes have a 60% chance to drop 1 ember shard on death
    // Any mob that dies while on fire has a 20% chance to drop 1 ember shard
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        boolean isBlaze = event.getEntity() instanceof Blaze;
        boolean isOnFire = event.getEntity().isOnFire();

        float roll = RANDOM.nextFloat();

        if (isBlaze && roll < 0.60f) {
            spawnDrop(event);
        } else if (!isBlaze && isOnFire && roll < 0.20f) {
            spawnDrop(event);
        }
    }

    private static void spawnDrop(LivingDeathEvent event) {
        ItemEntity drop = new ItemEntity(
            event.getEntity().level(),
            event.getEntity().getX(),
            event.getEntity().getY(),
            event.getEntity().getZ(),
            new ItemStack(ModItems.EMBER_SHARD.get(), 1)
        );
        event.getEntity().level().addFreshEntity(drop);
    }
}
