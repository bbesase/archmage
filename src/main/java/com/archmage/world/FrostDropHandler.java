package com.archmage.world;

import com.archmage.items.ModItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import static com.archmage.Archmage.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FrostDropHandler {

    private static final Random RANDOM = new Random();

    // Drowned have a 40% chance to drop 1 frost shard on death
    // Any mob that dies while underwater has a 15% chance to drop 1 frost shard
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        boolean isDrowned = event.getEntity() instanceof Drowned;
        boolean isUnderwater = event.getEntity().isUnderWater();

        float roll = RANDOM.nextFloat();

        if (isDrowned && roll < 0.40f) {
            spawnDrop(event);
        } else if (!isDrowned && isUnderwater && roll < 0.15f) {
            spawnDrop(event);
        }
    }

    private static void spawnDrop(LivingDeathEvent event) {
        ItemEntity drop = new ItemEntity(
            event.getEntity().level(),
            event.getEntity().getX(),
            event.getEntity().getY(),
            event.getEntity().getZ(),
            new ItemStack(ModItems.FROST_SHARD.get(), 1)
        );
        event.getEntity().level().addFreshEntity(drop);
    }
}
