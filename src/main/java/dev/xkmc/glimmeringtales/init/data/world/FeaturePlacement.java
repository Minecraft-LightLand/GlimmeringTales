package dev.xkmc.glimmeringtales.init.data.world;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public abstract class FeaturePlacement extends Placement {

	public final ResourceKey<ConfiguredFeature<?, ?>> featureKey;

	public FeaturePlacement(String id) {
		super(id);
		featureKey = ResourceKey.create(Registries.CONFIGURED_FEATURE, GlimmeringTales.loc(id));
	}

	public abstract void feature(BootstrapContext<ConfiguredFeature<?, ?>> ctx);

	public abstract void placed(BootstrapContext<PlacedFeature> ctx);

}
