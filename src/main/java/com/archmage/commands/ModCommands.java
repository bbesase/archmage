package com.archmage.commands;

import com.archmage.elements.ElementType;
import com.archmage.mastery.MasteryData;
import com.archmage.mastery.MasterySystem;
import com.archmage.mastery.MasteryTier;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.archmage.Archmage.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("archmage")
            .then(Commands.literal("mastery")
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    MasteryData data = MasterySystem.get(player);

                    player.sendSystemMessage(Component.literal("=== Archmage Mastery ==="));
                    player.sendSystemMessage(Component.literal(
                        "Active Element: " + data.getActiveElement().id));

                    for (ElementType el : ElementType.values()) {
                        int xp = data.getXp(el);
                        MasteryTier tier = data.getTier(el);
                        int pct = tier.maxXp == Integer.MAX_VALUE ? 100
                            : (int)(tier.progress(xp) * 100);

                        String line = String.format("  %s: %s (%d xp) [%d%% to next]",
                            el.id, tier.displayName, xp,
                            tier == MasteryTier.ELEMENTAL_LORD ? 100 : pct);

                        player.sendSystemMessage(Component.literal(line));
                    }
                    return 1;
                })
            )
        );
    }
}
