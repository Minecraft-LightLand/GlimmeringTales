package dev.xkmc.glimmeringtales.content.engine.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.fastprojectileapi.render.ProjTypeHolder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.entity.renderer.LMProjectileType;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderData;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderType;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import net.minecraft.resources.ResourceLocation;

public record InflatingRenderData(
		ResourceLocation texture, double initial, double rate
) implements ProjectileRenderData<InflatingRenderData> {

	public static final MapCodec<InflatingRenderData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ResourceLocation.CODEC.fieldOf("texture").forGetter(InflatingRenderData::texture),
			Codec.DOUBLE.fieldOf("initial").forGetter(InflatingRenderData::initial),
			Codec.DOUBLE.fieldOf("rate").forGetter(InflatingRenderData::rate)
	).apply(i, InflatingRenderData::new));

	@Override
	public ProjectileRenderType<InflatingRenderData> type() {
		return GTEngine.PR_BUBBLE.get();
	}

	@Override
	public ProjectileRenderer resolve(EngineContext ctx) {
		return new InflatingTextureRenderer(texture, initial, rate);
	}

	public void buildRenderer() {
		ProjTypeHolder.wrap(new LMProjectileType(texture));
	}

}
