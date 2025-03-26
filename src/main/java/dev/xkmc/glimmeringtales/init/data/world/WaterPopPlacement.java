package dev.xkmc.glimmeringtales.init.data.world;

import dev.xkmc.glimmeringtales.content.block.crop.PopFruit;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.List;
import java.util.function.Supplier;

public class WaterPopPlacement extends PopPlacement {

	public WaterPopPlacement(String id, Supplier<PopFruit> block, int rarity, int count) {
		super(id, block, rarity, count);
	}

	@Override
	public BlockPredicate getSpawnLocation() {
		return BlockPredicate.allOf(BlockPredicate.matchesBlocks(Blocks.WATER),
				BlockPredicate.not(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.DIRT)));
	}

	public void placed(BootstrapContext<PlacedFeature> ctx, Holder<ConfiguredFeature<?, ?>> cf) {
		ctx.register(placeKey, new PlacedFeature(cf, List.of(
				RarityFilter.onAverageOnceEvery(rarity),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				BiomeFilter.biome()
		)));
	}

}
