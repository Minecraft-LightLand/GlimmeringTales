package dev.xkmc.glimmeringtales.content.engine.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import dev.xkmc.fastprojectileapi.render.ProjectileRenderHelper;
import dev.xkmc.fastprojectileapi.render.ProjectileRenderer;
import dev.xkmc.fastprojectileapi.render.RenderableProjectileType;
import dev.xkmc.l2magic.content.entity.renderer.LMRenderStates;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public record OrientedCrossSpriteType(ResourceLocation tex)
		implements RenderableProjectileType<OrientedCrossSpriteType, OrientedCrossSpriteType.Ins> {

	@Override
	public void start(MultiBufferSource buffer, Iterable<Ins> list) {
		VertexConsumer vc = buffer.getBuffer(LMRenderStates.solid(tex));
		for (var e : list) {
			e.tex(vc);
		}
	}

	@Override
	public void create(ProjectileRenderer r, SimplifiedProjectile e, PoseStack pose, float pTick) {
		PoseStack.Pose mat = pose.last().copy();
		ProjectileRenderHelper.add(this, new Ins(mat, 1, 0));
	}

	public void create(PoseStack pose, int index, int max) {
		PoseStack.Pose mat = pose.last().copy();
		ProjectileRenderHelper.add(this, new Ins(mat, 1f / max, 1f * index / max));
	}

	public record Ins(PoseStack.Pose m4, float scale, float offset) {

		private static final float S2 = (float) Math.sqrt(0.5) * 0.5f;

		public void tex(VertexConsumer vc) {
			vertex(vc, m4, -1, 0, 0, 0, 0);
			vertex(vc, m4, 0, 1, 0, 1, 0);
			vertex(vc, m4, 1, 0, 0, 1, 1);
			vertex(vc, m4, 0, -1, 0, 0, 1);

			vertex(vc, m4, 0, 0, -1, 0, 0);
			vertex(vc, m4, 0, 1, 0, 1, 0);
			vertex(vc, m4, 0, 0, 1, 1, 1);
			vertex(vc, m4, 0, -1, 0, 0, 1);
		}

		private void vertex(VertexConsumer vc, PoseStack.Pose m4, int x, int y, int z, int u, int v) {
			vc.addVertex(m4, x * S2, y * S2, z * S2).setUv(u, v * scale + offset);
		}

	}
}