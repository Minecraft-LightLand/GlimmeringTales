package dev.xkmc.glimmeringtales.content.engine.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.render.ProjTypeHolder;
import dev.xkmc.l2magic.content.entity.core.LMProjectile;
import dev.xkmc.l2magic.content.entity.renderer.LMProjectileRenderer;
import dev.xkmc.l2magic.content.entity.renderer.LMProjectileType;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public record InflatingTextureRenderer(
		ResourceLocation texture,
		double initial, double rate
) implements ProjectileRenderer {

	@Override
	public ResourceLocation getTexture() {
		return texture;
	}

	@Override
	public void render(LMProjectile e, LMProjectileRenderer<?> r, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		pose.pushPose();
		pose.translate(0.0F, e.getBbHeight() / 2.0F, 0.0F);
		float p = 1f * e.tickCount / e.lifetime();
		float scale = (float) (1 - (1 - initial) * Math.exp(-rate * p));
		pose.scale(scale, scale, scale);
		ProjTypeHolder.wrap(new LMProjectileType(texture)).create(r, e, pose, pTick);
		pose.popPose();

	}

}
