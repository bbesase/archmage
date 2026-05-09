package com.archmage.world;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import terrablender.api.SurfaceRuleManager;

import static com.archmage.Archmage.MOD_ID;

/**
 * Custom surface block rules for Archmage's 4 elemental biomes.
 * Registered via TerraBlender's SurfaceRuleManager during FMLCommonSetupEvent.
 *
 *  Stormplains       — coarse dirt surface / dirt subsurface  (dry, cracked feel)
 *  Scorched Wasteland — red sand surface / sandstone below     (desert-wasteland)
 *  Frozen Wastes     — snow block surface / packed ice below   (arctic)
 *  Ancient Grove     — podzol surface / dirt below             (ancient dark forest)
 */
public class ArchmageSurfaceRules {

    public static void register() {
        SurfaceRuleManager.addSurfaceRules(
            SurfaceRuleManager.RuleCategory.OVERWORLD,
            MOD_ID,
            buildRules()
        );
    }

    private static SurfaceRules.RuleSource buildRules() {
        return SurfaceRules.sequence(

            // ── Stormplains ────────────────────────────────────────────────────
            SurfaceRules.ifTrue(
                SurfaceRules.isBiome(ModBiomes.STORMPLAINS),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        SurfaceRules.ON_FLOOR,
                        SurfaceRules.state(Blocks.COARSE_DIRT.defaultBlockState())
                    ),
                    SurfaceRules.ifTrue(
                        SurfaceRules.DEEP_UNDER_FLOOR,
                        SurfaceRules.state(Blocks.DIRT.defaultBlockState())
                    )
                )
            ),

            // ── Scorched Wasteland ─────────────────────────────────────────────
            SurfaceRules.ifTrue(
                SurfaceRules.isBiome(ModBiomes.SCORCHED_WASTELAND),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        SurfaceRules.ON_FLOOR,
                        SurfaceRules.state(Blocks.RED_SAND.defaultBlockState())
                    ),
                    SurfaceRules.ifTrue(
                        SurfaceRules.DEEP_UNDER_FLOOR,
                        SurfaceRules.state(Blocks.RED_SANDSTONE.defaultBlockState())
                    )
                )
            ),

            // ── Frozen Wastes ──────────────────────────────────────────────────
            SurfaceRules.ifTrue(
                SurfaceRules.isBiome(ModBiomes.FROZEN_WASTES),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        SurfaceRules.ON_FLOOR,
                        SurfaceRules.state(Blocks.SNOW_BLOCK.defaultBlockState())
                    ),
                    SurfaceRules.ifTrue(
                        SurfaceRules.DEEP_UNDER_FLOOR,
                        SurfaceRules.state(Blocks.PACKED_ICE.defaultBlockState())
                    )
                )
            ),

            // ── Ancient Grove ──────────────────────────────────────────────────
            SurfaceRules.ifTrue(
                SurfaceRules.isBiome(ModBiomes.ANCIENT_GROVE),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        SurfaceRules.ON_FLOOR,
                        SurfaceRules.state(Blocks.PODZOL.defaultBlockState())
                    ),
                    SurfaceRules.ifTrue(
                        SurfaceRules.DEEP_UNDER_FLOOR,
                        SurfaceRules.state(Blocks.DIRT.defaultBlockState())
                    )
                )
            )
        );
    }
}
