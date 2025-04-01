package dev.xkmc.glimmeringtales.init.data.world;

import dev.xkmc.glimmeringtales.init.reg.GTItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaJungleFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class LargeTreePlacement extends FeaturePlacement {

	public LargeTreePlacement(String id) {
		super(id);
	}

	@Override
	public void feature(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
		ctx.register(featureKey, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
				BlockStateProvider.simple(GTItems.STRUCK_LOG.getDefaultState()),
				new MegaJungleTrunkPlacer(10, 2, 19),
				BlockStateProvider.simple(GTItems.STRUCK_LEAVES.getDefaultState()),
				new MegaJungleFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 2),
				new TwoLayersFeatureSize(1, 1, 2)
		).ignoreVines().build()));
	}

	@Override
	public void placed(BootstrapContext<PlacedFeature> ctx) {
		var cf = ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(featureKey);
		ctx.register(placeKey, new PlacedFeature(cf, VegetationPlacements.treePlacement(
				PlacementUtils.countExtra(0, 1f / 16, 1),
				GTItems.STRUCK_SAPLING.get())));
	}

}
