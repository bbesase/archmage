package com.archmage.world;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

/**
 * TerraBlender region that injects the 4 Archmage elemental biomes into
 * overworld terrain generation.
 *
 * Weight 4 means roughly 4 / (vanilla_weight + 4) of the world uses this
 * region. Inside this region, specific vanilla biome slots are replaced with
 * our elemental variants, so the replacement is proportional to how often
 * those vanilla biomes normally appear.
 */
public class ArchmageRegion extends Region {

    public ArchmageRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry,
                          Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        addModifiedVanillaOverworldBiomes(mapper, builder -> {
            // ── Lightning ────────────────────────────────────────────────────
            // Stormplains: open flat terrain, exposed to sky → replaces Plains
            // and Windswept Hills (elevated open biomes similarly exposed).
            builder.replaceBiome(Biomes.PLAINS,           ModBiomes.STORMPLAINS);
            builder.replaceBiome(Biomes.WINDSWEPT_HILLS,  ModBiomes.STORMPLAINS);

            // ── Water / Ice ──────────────────────────────────────────────────
            // Frozen Wastes: arctic expanse → replaces cold flat biomes.
            builder.replaceBiome(Biomes.SNOWY_PLAINS,     ModBiomes.FROZEN_WASTES);
            builder.replaceBiome(Biomes.ICE_SPIKES,       ModBiomes.FROZEN_WASTES);

            // ── Fire ─────────────────────────────────────────────────────────
            // Scorched Wasteland: arid hot terrain → replaces hot/dry biomes.
            builder.replaceBiome(Biomes.DESERT,           ModBiomes.SCORCHED_WASTELAND);
            builder.replaceBiome(Biomes.BADLANDS,         ModBiomes.SCORCHED_WASTELAND);

            // ── Earth ────────────────────────────────────────────────────────
            // Ancient Grove: dense ancient forest → replaces dense/dark biomes.
            builder.replaceBiome(Biomes.JUNGLE,           ModBiomes.ANCIENT_GROVE);
            builder.replaceBiome(Biomes.DARK_FOREST,      ModBiomes.ANCIENT_GROVE);
        });
    }
}
