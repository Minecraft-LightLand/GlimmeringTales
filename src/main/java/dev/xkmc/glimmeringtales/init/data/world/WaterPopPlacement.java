package dev.xkmc.glimmeringtales.init.data.world;

import dev.xkmc.glimmeringtales.content.block.crop.PopFruit;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.function.Supplier;

public class WaterPopPlacement extends PopPlacement {

	public WaterPopPlacement(String id, Supplier<PopFruit> block, int rarity, int count) {
		super(id, block, rarity, count);
	}

	@Override
	public BlockPredicate getSpawnLocation() {
		return BlockPredicate.allOf(BlockPredicate.matchesBlocks(Blocks.WATER),
				BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRAVEL, Blocks.SAND, Blocks.DIRT));
	}

	@Override
	public PlacementModifier location() {
		return PlacementUtils.HEIGHTMAP_TOP_SOLID;
	}

}
