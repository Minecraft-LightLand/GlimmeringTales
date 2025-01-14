package dev.xkmc.glimmeringtales.content.block.infuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.l2library.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class InfuserBlockRenderer implements BlockEntityRenderer<InfuserBlockEntity> {

	public InfuserBlockRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(InfuserBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		var level = be.getLevel();
		if (level == null) return;
		ItemStack crystal = be.items.getItem(0);
		if (!crystal.isEmpty()) {
			pose.pushPose();
			pose.translate(0.5, 13/32f, 0.5);
			pose.scale(0.4f,0.4f,0.4f);
			pose.mulPose(Axis.XP.rotationDegrees(90));
			Minecraft.getInstance().getItemRenderer().renderStatic(crystal, ItemDisplayContext.GUI, light, overlay, pose, buffer, level, 0);
			pose.popPose();
		}
		ItemStack input = be.items.getItem(1);
		RenderUtils.renderItemAbove(input, 1.25, level, pTick, pose, buffer, light, overlay);
	}

}
