package com.archmage.world;

import com.archmage.items.ModItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import static com.archmage.Archmage.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TerraDropHandler {

    private static final Random RANDOM = new Random();

    // Silverfish have a 50% chance to drop 1 terra shard on death
    // Spiders have a 25% chance to drop 1 terra shard on death
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        boolean isSilverfish = event.getEntity() instanceof Silverfish;
        boolean isSpider = event.getEntity() instanceof Spider;

        if (!isSilverfish && !isSpider) return;

        float roll = RANDOM.nextFloat();
        float threshold = isSilverfish ? 0.50f : 0.25f;

        if (roll < threshold) {
            ItemEntity drop = new ItemEntity(
                event.getEntity().level(),
                event.getEntity().getX(),
                event.getEntity().getY(),
                event.getEntity().getZ(),
                new ItemStack(ModItems.TERRA_SHARD.get(), 1)
            );
            event.getEntity().level().addFreshEntity(drop);
        }
    }
}
