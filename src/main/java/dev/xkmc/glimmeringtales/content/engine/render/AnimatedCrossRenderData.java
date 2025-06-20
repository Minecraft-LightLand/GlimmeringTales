package dev.xkmc.glimmeringtales.content.engine.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.fastprojectileapi.render.ProjTypeHolder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderData;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderType;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import net.minecraft.resources.ResourceLocation;

public record AnimatedCrossRenderData(
		ResourceLocation texture, int rate, int max
) implements ProjectileRenderData<AnimatedCrossRenderData> {

	public static final MapCodec<AnimatedCrossRenderData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ResourceLocation.CODEC.fieldOf("texture").forGetter(AnimatedCrossRenderData::texture),
			Codec.INT.fieldOf("rate").forGetter(AnimatedCrossRenderData::rate),
			Codec.INT.fieldOf("max").forGetter(AnimatedCrossRenderData::max)
	).apply(i, AnimatedCrossRenderData::new));

	@Override
	public ProjectileRenderType<AnimatedCrossRenderData> type() {
		return GTEngine.PR_CHARGE.get();
	}

	@Override
	public ProjectileRenderer resolve(EngineContext ctx) {
		return new AnimatedCrossTextureRenderer(texture, rate, max);
	}

	public void buildRenderer() {
		ProjTypeHolder.wrap(new AnimatedCrossSpriteType(texture));
	}

}
