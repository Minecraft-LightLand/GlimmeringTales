package dev.xkmc.glimmeringtales.content.engine.render;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.fastprojectileapi.render.ProjTypeHolder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderData;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderType;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import net.minecraft.resources.ResourceLocation;

public record VerticalRenderData(
		ResourceLocation texture
) implements ProjectileRenderData<VerticalRenderData> {

	public static final MapCodec<VerticalRenderData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ResourceLocation.CODEC.fieldOf("texture").forGetter(VerticalRenderData::texture)
	).apply(i, VerticalRenderData::new));

	@Override
	public ProjectileRenderType<VerticalRenderData> type() {
		return GTEngine.PR_VERTICAL.get();
	}

	@Override
	public ProjectileRenderer resolve(EngineContext ctx) {
		return new VerticalTextureRenderer(texture);
	}

	public void buildRenderer() {
		ProjTypeHolder.wrap(new VerticalSpriteType(texture));
	}

}
