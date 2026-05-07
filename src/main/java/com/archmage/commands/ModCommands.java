package com.archmage.commands;

import com.archmage.elements.ElementType;
import com.archmage.mastery.MasteryData;
import com.archmage.mastery.MasterySystem;
import com.archmage.mastery.MasteryTier;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
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

                // /archmage mastery — show current state
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    printMastery(player);
                    return 1;
                })

                // /archmage mastery set <element> <tier>
                .then(Commands.literal("set")
                    .then(Commands.argument("element", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            for (ElementType el : ElementType.values())
                                builder.suggest(el.id);
                            return builder.buildFuture();
                        })
                        .then(Commands.argument("tier", StringArgumentType.word())
                            .suggests((ctx, builder) -> {
                                for (MasteryTier t : MasteryTier.values())
                                    builder.suggest(t.name().toLowerCase());
                                return builder.buildFuture();
                            })
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                String elementArg = StringArgumentType.getString(ctx, "element");
                                String tierArg    = StringArgumentType.getString(ctx, "tier");

                                ElementType element = parseElement(elementArg);
                                MasteryTier tier    = parseTier(tierArg);

                                if (element == null) {
                                    player.sendSystemMessage(Component.literal(
                                        "Unknown element: " + elementArg + ". Valid: lightning, water, fire, earth, holy, void"));
                                    return 0;
                                }
                                if (tier == null) {
                                    player.sendSystemMessage(Component.literal(
                                        "Unknown tier: " + tierArg + ". Valid: apprentice, adept, mage, archmage, elemental_lord"));
                                    return 0;
                                }

                                MasteryData data = MasterySystem.get(player);
                                data.addXp(element, tier.minXp - data.getXp(element));
                                player.sendSystemMessage(Component.literal(
                                    "Set " + element.id + " mastery to " + tier.displayName +
                                    " (" + tier.minXp + " xp)"));
                                return 1;
                            })
                        )
                    )
                )

                // /archmage mastery setall <tier>
                .then(Commands.literal("setall")
                    .then(Commands.argument("tier", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            for (MasteryTier t : MasteryTier.values())
                                builder.suggest(t.name().toLowerCase());
                            return builder.buildFuture();
                        })
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            String tierArg = StringArgumentType.getString(ctx, "tier");
                            MasteryTier tier = parseTier(tierArg);

                            if (tier == null) {
                                player.sendSystemMessage(Component.literal(
                                    "Unknown tier: " + tierArg + ". Valid: apprentice, adept, mage, archmage, elemental_lord"));
                                return 0;
                            }

                            MasteryData data = MasterySystem.get(player);
                            for (ElementType el : ElementType.values()) {
                                data.addXp(el, tier.minXp - data.getXp(el));
                            }
                            player.sendSystemMessage(Component.literal(
                                "Set all elements to " + tier.displayName + " (" + tier.minXp + " xp)"));
                            printMastery(player);
                            return 1;
                        })
                    )
                )
            )
        );
    }

    private static void printMastery(ServerPlayer player) {
        MasteryData data = MasterySystem.get(player);
        player.sendSystemMessage(Component.literal("=== Archmage Mastery ==="));
        player.sendSystemMessage(Component.literal("Active Element: " + data.getActiveElement().id));
        for (ElementType el : ElementType.values()) {
            int xp = data.getXp(el);
            MasteryTier tier = data.getTier(el);
            int pct = tier == MasteryTier.ELEMENTAL_LORD ? 100 : (int)(tier.progress(xp) * 100);
            player.sendSystemMessage(Component.literal(String.format(
                "  %s: %s (%d xp) [%d%% to next]", el.id, tier.displayName, xp, pct)));
        }
    }

    private static ElementType parseElement(String input) {
        for (ElementType el : ElementType.values())
            if (el.id.equalsIgnoreCase(input)) return el;
        return null;
    }

    private static MasteryTier parseTier(String input) {
        for (MasteryTier t : MasteryTier.values())
            if (t.name().equalsIgnoreCase(input)) return t;
        return null;
    }
}
