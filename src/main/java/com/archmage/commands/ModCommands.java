package com.archmage.commands;

import com.archmage.elements.ElementType;
import com.archmage.mastery.MasteryData;
import com.archmage.mastery.MasterySystem;
import com.archmage.mastery.MasteryTier;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.datafixers.util.Pair;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import static com.archmage.Archmage.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommands {

    /** Max biome search radius — beyond this the server would stall on rare biomes. */
    private static final int BIOME_SEARCH_RADIUS = 10_000;

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("archmage")

            // ── /archmage mastery ─────────────────────────────────────────────
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
                                        "Unknown element: " + elementArg +
                                        ". Valid: lightning, water, fire, earth, holy, void"));
                                    return 0;
                                }
                                if (tier == null) {
                                    player.sendSystemMessage(Component.literal(
                                        "Unknown tier: " + tierArg +
                                        ". Valid: apprentice, adept, mage, archmage, elemental_lord"));
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
                                    "Unknown tier: " + tierArg +
                                    ". Valid: apprentice, adept, mage, archmage, elemental_lord"));
                                return 0;
                            }

                            MasteryData data = MasterySystem.get(player);
                            for (ElementType el : ElementType.values()) {
                                data.addXp(el, tier.minXp - data.getXp(el));
                            }
                            player.sendSystemMessage(Component.literal(
                                "Set all elements to " + tier.displayName +
                                " (" + tier.minXp + " xp)"));
                            printMastery(player);
                            return 1;
                        })
                    )
                )
            )

            // ── /archmage tp ──────────────────────────────────────────────────
            .then(Commands.literal("tp")

                // /archmage tp biome <biome_id>
                // Works for any biome — vanilla or modded (e.g. archmage:stormplains, minecraft:plains)
                .then(Commands.literal("biome")
                    .then(Commands.argument("biome", ResourceLocationArgument.id())
                        .suggests((ctx, builder) -> {
                            // Autocomplete all registered biomes
                            ctx.getSource().registryAccess()
                                .registry(Registries.BIOME)
                                .ifPresent(reg -> reg.keySet()
                                    .stream()
                                    .map(ResourceLocation::toString)
                                    .filter(s -> s.contains(builder.getRemaining().toLowerCase()))
                                    .forEach(builder::suggest));
                            return builder.buildFuture();
                        })
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            ResourceLocation biomeLoc = ResourceLocationArgument.getId(ctx, "biome");
                            ResourceKey<Biome> biomeKey = ResourceKey.create(Registries.BIOME, biomeLoc);
                            ServerLevel level = (ServerLevel) player.level();

                            player.sendSystemMessage(Component.literal(
                                "Searching for " + biomeLoc + " (up to " +
                                BIOME_SEARCH_RADIUS + " blocks)..."));

                            // Horizontal step 32, vertical step 64 — same as vanilla /locatebiome
                            Pair<BlockPos, Holder<Biome>> result = level.findClosestBiome3d(
                                holder -> holder.is(biomeKey),
                                player.blockPosition(),
                                BIOME_SEARCH_RADIUS,
                                32, 64
                            );

                            if (result == null) {
                                player.sendSystemMessage(Component.literal(
                                    "§cCould not find biome '" + biomeLoc +
                                    "' within " + BIOME_SEARCH_RADIUS + " blocks."));
                                return 0;
                            }

                            BlockPos found = result.getFirst();
                            // Step up to the surface so the player doesn't land in a cave
                            int surfaceY = level.getHeightmapPos(
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, found).getY();

                            player.teleportTo(found.getX() + 0.5, surfaceY, found.getZ() + 0.5);
                            player.sendSystemMessage(Component.literal(String.format(
                                "§aTeleported to §e%s§a at §f%d, %d, %d",
                                biomeLoc, found.getX(), surfaceY, found.getZ())));
                            return 1;
                        })
                    )
                )
            )

            // ── /archmage spawn ───────────────────────────────────────────────
            .then(Commands.literal("spawn")

                // /archmage spawn blob <type>
                // Spawns the named Archmage blob at the player's location.
                // Entity must be registered as archmage:<type>_blob — auto-works once
                // feat/elemental-blobs wires up the entity types.
                .then(Commands.literal("blob")
                    .then(Commands.argument("type", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            for (String t : new String[]{
                                "lightning", "fire", "ice", "earth", "holy", "void"
                            }) builder.suggest(t);
                            return builder.buildFuture();
                        })
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            String blobType = StringArgumentType.getString(ctx, "type").toLowerCase();
                            ResourceLocation entityLoc =
                                new ResourceLocation(MOD_ID, blobType + "_blob");

                            EntityType<?> entityType =
                                ForgeRegistries.ENTITY_TYPES.getValue(entityLoc);

                            if (entityType == null) {
                                player.sendSystemMessage(Component.literal(
                                    "§cUnknown blob type '§e" + blobType +
                                    "§c'. Valid: lightning, fire, ice, earth, holy, void." +
                                    " (Blob entities may not be registered yet.)"));
                                return 0;
                            }

                            ServerLevel level = (ServerLevel) player.level();
                            Entity entity = entityType.create(level);
                            if (entity == null) {
                                player.sendSystemMessage(Component.literal(
                                    "§cFailed to create blob entity."));
                                return 0;
                            }

                            entity.moveTo(player.getX(), player.getY(), player.getZ(), 0f, 0f);
                            level.addFreshEntity(entity);
                            player.sendSystemMessage(Component.literal(
                                "§aSpawned §e" + blobType + " blob§a!"));
                            return 1;
                        })
                    )
                )
            )
        );
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static void printMastery(ServerPlayer player) {
        MasteryData data = MasterySystem.get(player);
        player.sendSystemMessage(Component.literal("=== Archmage Mastery ==="));
        player.sendSystemMessage(Component.literal(
            "Active Element: " + data.getActiveElement().id));
        for (ElementType el : ElementType.values()) {
            int xp = data.getXp(el);
            MasteryTier tier = data.getTier(el);
            int pct = tier == MasteryTier.ELEMENTAL_LORD
                ? 100 : (int)(tier.progress(xp) * 100);
            player.sendSystemMessage(Component.literal(String.format(
                "  %s: %s (%d xp) [%d%% to next]",
                el.id, tier.displayName, xp, pct)));
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
