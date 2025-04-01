package dev.xkmc.glimmeringtales.init.data.world;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public abstract class Placement {

	private final String id;

	public final ResourceKey<PlacedFeature> placeKey;
	public final TagKey<Biome> biomeTag;


	public Placement(String id) {
		this.id = id;
		placeKey = ResourceKey.create(Registries.PLACED_FEATURE, GlimmeringTales.loc(id));
		biomeTag = TagKey.create(Registries.BIOME, GlimmeringTales.loc("has_feature/" + id));
	}

	public void biome(BootstrapContext<BiomeModifier> ctx) {
		var biomes = ctx.lookup(Registries.BIOME);
		var features = ctx.lookup(Registries.PLACED_FEATURE);
		HolderSet<Biome> set = biomes.getOrThrow(biomeTag);
		ctx.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, GlimmeringTales.loc(id)),
				new BiomeModifiers.AddFeaturesBiomeModifier(set,
						HolderSet.direct(features.getOrThrow(placeKey)),
						GenerationStep.Decoration.VEGETAL_DECORATION));
	}

}
