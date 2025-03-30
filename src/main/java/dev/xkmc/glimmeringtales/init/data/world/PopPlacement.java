package dev.xkmc.glimmeringtales.init.data.world;

import dev.xkmc.glimmeringtales.content.block.crop.PopFruit;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;
import java.util.function.Supplier;

public class PopPlacement extends FeaturePlacement {

	protected final Supplier<PopFruit> block;
	protected final int rarity, count;

	public PopPlacement(String id, Supplier<PopFruit> block, int rarity, int count) {
		super(id);
		this.block = block;
		this.rarity = rarity;
		this.count = count;
	}

	public BlockPredicate getSpawnLocation() {
		return BlockPredicate.allOf(BlockPredicate.matchesBlocks(Blocks.AIR),
				BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK));
	}

	public PlacementModifier location() {
		return PlacementUtils.HEIGHTMAP;
	}

	public void feature(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
		var state = new RandomizedIntStateProvider(BlockStateProvider.simple(block.get()),
				block.get().getAgeProperty(), UniformInt.of(1, 3));
		ctx.register(featureKey, new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(count, 4, 3,
				PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(state), getSpawnLocation()))));
	}

	public void placed(BootstrapContext<PlacedFeature> ctx) {
		var feature = ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(featureKey);
		ctx.register(placeKey, new PlacedFeature(feature, List.of(
				RarityFilter.onAverageOnceEvery(rarity),
				InSquarePlacement.spread(),
				location(),
				BiomeFilter.biome()
		)));
	}

}
