package dev.xkmc.glimmeringtales.init.data.world;

import com.tterrag.registrate.providers.DataProviderInitializer;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.PopFruitType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.OptionalInt;

public class GTWorldGen {

	public static final ResourceKey<ConfiguredFeature<?, ?>> CF_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE, GlimmeringTales.loc("struck_tree"));
	private static final TreePlacement PF_SPARSE = new TreePlacement("struck_tree_sparse", 40, GTItems.STRUCK_SAPLING::get);
	private static final TreePlacement PF_COMMON = new TreePlacement("struck_tree_common", 16, GTItems.STRUCK_SAPLING::get);
	private static final TreePlacement PF_DENSE = new TreePlacement("struck_tree_dense", 8, GTItems.STRUCK_SAPLING::get);

	private static final FeaturePlacement LARGE = new LargeTreePlacement("struck_large_tree");
	private static final FeaturePlacement POP = new PopPlacement("pop_fruit", PopFruitType.POP_FRUIT::get, 4, 64);
	private static final FeaturePlacement BLOSSOM_POP = new PopPlacement("blossom_pop_fruit", PopFruitType.BLOSSOM_POP_FRUIT::get, 1, 32);
	private static final FeaturePlacement OCEAN_POP = new WaterPopPlacement("ocean_pop_fruit", PopFruitType.OCEAN_POP_FRUIT::get, 8, 48);

	public static void genFeatures(DataProviderInitializer init) {
		init.add(Registries.CONFIGURED_FEATURE, ctx -> {

			ctx.register(CF_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(GTItems.STRUCK_LOG.getDefaultState()),
					new StraightTrunkPlacer(5, 2, 0),
					BlockStateProvider.simple(GTItems.STRUCK_LEAVES.getDefaultState()),
					new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
					new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
			).ignoreVines().build()));

			LARGE.feature(ctx);
			POP.feature(ctx);
			BLOSSOM_POP.feature(ctx);
			OCEAN_POP.feature(ctx);
		});
		init.add(Registries.PLACED_FEATURE, ctx -> {
			var cf = ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(CF_TREE);
			PF_SPARSE.placed(ctx, cf);
			PF_COMMON.placed(ctx, cf);
			PF_DENSE.placed(ctx, cf);
			LARGE.placed(ctx);
			POP.placed(ctx);
			BLOSSOM_POP.placed(ctx);
			OCEAN_POP.placed(ctx);
		});
		init.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ctx -> {
			PF_SPARSE.biome(ctx);
			PF_COMMON.biome(ctx);
			PF_DENSE.biome(ctx);
			LARGE.biome(ctx);
			POP.biome(ctx);
			BLOSSOM_POP.biome(ctx);
			OCEAN_POP.biome(ctx);
		});
	}

	public static void genBiomeTags(RegistrateTagsProvider.Impl<Biome> pvd) {
		pvd.addTag(PF_SPARSE.biomeTag).add(Biomes.PLAINS, Biomes.SNOWY_PLAINS, Biomes.GROVE, Biomes.TAIGA, Biomes.SNOWY_TAIGA)
				.addTag(Tags.Biomes.IS_PLAINS);
		pvd.addTag(PF_COMMON.biomeTag).add(Biomes.SWAMP, Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.WINDSWEPT_FOREST)
				.addTag(Tags.Biomes.IS_FOREST);
		pvd.addTag(PF_DENSE.biomeTag).add(Biomes.JUNGLE, Biomes.DARK_FOREST)
				.addTag(Tags.Biomes.IS_JUNGLE);

		pvd.addTag(LARGE.biomeTag).add(Biomes.JUNGLE)
				.addTag(Tags.Biomes.IS_JUNGLE);
		pvd.addTag(POP.biomeTag).add(Biomes.PLAINS, Biomes.SNOWY_PLAINS, Biomes.GROVE, Biomes.TAIGA, Biomes.SNOWY_TAIGA)
				.addTag(Tags.Biomes.IS_PLAINS);
		pvd.addTag(BLOSSOM_POP.biomeTag).add(Biomes.FLOWER_FOREST, Biomes.SUNFLOWER_PLAINS, Biomes.CHERRY_GROVE, Biomes.MEADOW)
				.addTag(Tags.Biomes.IS_FLORAL);
		pvd.addTag(OCEAN_POP.biomeTag).add(Biomes.DEEP_OCEAN)
				.addTag(Tags.Biomes.IS_OCEAN);
	}


}
