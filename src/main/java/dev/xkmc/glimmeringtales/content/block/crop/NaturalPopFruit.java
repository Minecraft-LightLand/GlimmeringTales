package dev.xkmc.glimmeringtales.content.block.crop;

import dev.xkmc.glimmeringtales.init.data.GTConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

public abstract class NaturalPopFruit extends AbstractPopFruit {

	public NaturalPopFruit(Properties properties) {
		super(properties);
	}

	private TagKey<Biome> biomeTag;

	public TagKey<Biome> getBiomeTag() {
		if (biomeTag == null) {
			biomeTag = TagKey.create(Registries.BIOME, BuiltInRegistries.BLOCK.getKey(this).withPrefix("has_feature/"));
		}
		return biomeTag;
	}

	public abstract MutableComponent getBiomeDesc();

	@Override
	protected float getGrowthSpeedBonus(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		return level.getBiome(pos).is(getBiomeTag()) ? GTConfigs.SERVER.popFruitBiomeGrowFactor.getAsInt() : 0;
	}

}
