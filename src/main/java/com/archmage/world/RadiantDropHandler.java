package com.archmage.world;

import com.archmage.items.ModItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import static com.archmage.Archmage.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RadiantDropHandler {

    private static final Random RANDOM = new Random();

    // Evokers have a 70% chance to drop 1 radiant shard on death (rare mob, high reward)
    // Witches have a 30% chance to drop 1 radiant shard on death
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        boolean isEvoker = event.getEntity() instanceof Evoker;
        boolean isWitch = event.getEntity() instanceof Witch;

        if (!isEvoker && !isWitch) return;

        float roll = RANDOM.nextFloat();
        float threshold = isEvoker ? 0.70f : 0.30f;

        if (roll < threshold) {
            ItemEntity drop = new ItemEntity(
                event.getEntity().level(),
                event.getEntity().getX(),
                event.getEntity().getY(),
                event.getEntity().getZ(),
                new ItemStack(ModItems.RADIANT_SHARD.get(), 1)
            );
            event.getEntity().level().addFreshEntity(drop);
        }
    }
}
