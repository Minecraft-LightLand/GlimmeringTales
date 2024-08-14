package dev.xkmc.glimmeringtales.init.data;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class GTTagGen {

	public static final TagKey<Block> AMETHYST = of("amethyst");
	public static final TagKey<Block> QUARTZ = of("quartz");
	public static final TagKey<Block> VINE = of("vine");

	public static void genBlockTag(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(AMETHYST).add(
				Blocks.AMETHYST_BLOCK, Blocks.AMETHYST_CLUSTER, Blocks.BUDDING_AMETHYST,
				Blocks.SMALL_AMETHYST_BUD, Blocks.MEDIUM_AMETHYST_BUD, Blocks.LARGE_AMETHYST_BUD
		);

		pvd.addTag(QUARTZ).add(
				Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_BRICKS, Blocks.QUARTZ_PILLAR,
				Blocks.SMOOTH_QUARTZ, Blocks.CHISELED_QUARTZ_BLOCK,
				Blocks.QUARTZ_SLAB, Blocks.SMOOTH_QUARTZ_SLAB,
				Blocks.QUARTZ_STAIRS, Blocks.SMOOTH_QUARTZ_STAIRS
		);

		pvd.addTag(VINE).add(
				Blocks.VINE, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT, Blocks.TWISTING_VINES,
				Blocks.TWISTING_VINES_PLANT, Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT
		);

	}

	public static TagKey<Block> of(String id) {
		return TagKey.create(Registries.BLOCK, GlimmeringTales.loc(id));
	}

}