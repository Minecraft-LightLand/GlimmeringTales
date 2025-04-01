package dev.xkmc.glimmeringtales.content.item.tool;

import dev.xkmc.glimmeringtales.content.core.searcher.BlockSearcher;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTConfigs;
import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;

public class AmethystResonator extends Item implements IBlockSearcher {

	private static final ResourceLocation TEX = GlimmeringTales.loc("textures/icon/amethyst_cluster.png");

	public final BlockSearcher AMETHYST = new BlockSearcher(this,
			Blocks.BUDDING_AMETHYST, GTTagGen.AMETHYST, -60, 40,
			GTConfigs.CLIENT.resonatorSearchRadius,
			GTConfigs.CLIENT.resonatorSearchTrialsPerTick,
			5, 40, 3);

	public AmethystResonator(Properties properties) {
		super(properties);
	}

	@Override
	public BlockSearcher getSearcher() {
		return AMETHYST;
	}

	@Override
	public ResourceLocation id() {
		return TEX;
	}

}
