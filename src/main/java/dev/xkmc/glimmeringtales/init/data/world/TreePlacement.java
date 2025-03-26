package dev.xkmc.glimmeringtales.init.data.world;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Supplier;

public class TreePlacement extends Placement {

	private final int spread;
	private final Supplier<Block> sapling;

	public TreePlacement(String id, int spread, Supplier<Block> sapling) {
		super(id);
		this.spread = spread;
		this.sapling = sapling;
	}

	public void placed(BootstrapContext<PlacedFeature> ctx, Holder<ConfiguredFeature<?, ?>> cf) {
		ctx.register(placeKey, new PlacedFeature(cf, VegetationPlacements.treePlacement(
				PlacementUtils.countExtra(0, 1f / spread, 1),
				sapling.get())));
	}

}
