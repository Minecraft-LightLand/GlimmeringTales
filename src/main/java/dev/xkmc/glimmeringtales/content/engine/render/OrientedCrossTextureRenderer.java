package dev.xkmc.glimmeringtales.content.engine.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.fastprojectileapi.render.ProjTypeHolder;
import dev.xkmc.l2magic.content.entity.core.LMProjectile;
import dev.xkmc.l2magic.content.entity.renderer.LMProjectileRenderer;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public record OrientedCrossTextureRenderer(
		ResourceLocation texture
) implements ProjectileRenderer {

	@Override
	public ResourceLocation getTexture() {
		return texture;
	}

	@Override
	public void render(LMProjectile e, LMProjectileRenderer<?> r, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		pose.pushPose();
		pose.mulPose(Axis.YP.rotationDegrees(90 - Mth.lerp(pTick, e.yRotO, e.getYRot())));
		pose.mulPose(Axis.ZP.rotationDegrees(90 + Mth.lerp(pTick, e.xRotO, e.getXRot())));
		ProjTypeHolder.wrap(new OrientedCrossSpriteType(texture)).create(r, e, pose, pTick);
		pose.popPose();

	}

}
