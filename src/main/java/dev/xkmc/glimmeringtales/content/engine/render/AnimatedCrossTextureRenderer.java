package dev.xkmc.glimmeringtales.content.engine.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.fastprojectileapi.render.ProjTypeHolder;
import dev.xkmc.l2magic.content.entity.core.LMProjectile;
import dev.xkmc.l2magic.content.entity.renderer.LMProjectileRenderer;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public record AnimatedCrossTextureRenderer(
		ResourceLocation texture, int rate, int max
) implements ProjectileRenderer {

	@Override
	public ResourceLocation getTexture() {
		return texture;
	}

	@Override
	public void render(LMProjectile e, LMProjectileRenderer<?> r, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		pose.pushPose();
		pose.mulPose(Axis.YP.rotationDegrees(45));
		new AnimatedCrossSpriteType(texture).create(pose, Math.min(e.tickCount / rate, max - 1), max);
		pose.popPose();

	}

}
