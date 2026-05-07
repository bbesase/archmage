package com.archmage.world;

import com.archmage.items.ModItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import static com.archmage.Archmage.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LightningDropHandler {

    private static final Random RANDOM = new Random();

    // Any mob struck by lightning drops 1-2 shards
    @SubscribeEvent
    public static void onLightningStrike(EntityStruckByLightningEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof net.minecraft.world.entity.LivingEntity)) return;

        int count = 1 + RANDOM.nextInt(2); // 1 or 2
        ItemEntity drop = new ItemEntity(
            event.getEntity().level(),
            event.getEntity().getX(),
            event.getEntity().getY(),
            event.getEntity().getZ(),
            new ItemStack(ModItems.LIGHTNING_SHARD.get(), count)
        );
        event.getEntity().level().addFreshEntity(drop);
    }

    // Creeper kills have a 40% chance to drop 1 shard
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Creeper)) return;
        if (RANDOM.nextFloat() > 0.40f) return;

        ItemEntity drop = new ItemEntity(
            event.getEntity().level(),
            event.getEntity().getX(),
            event.getEntity().getY(),
            event.getEntity().getZ(),
            new ItemStack(ModItems.LIGHTNING_SHARD.get(), 1)
        );
        event.getEntity().level().addFreshEntity(drop);
    }
}
