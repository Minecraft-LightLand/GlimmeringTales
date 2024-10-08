package dev.xkmc.glimmeringtales.content.item.wand;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.l2core.util.ServerProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.apache.commons.lang3.function.Consumers;
import org.joml.Quaternionf;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GTBEWLR extends BlockEntityWithoutLevelRenderer {

	public static final Supplier<BlockEntityWithoutLevelRenderer> INSTANCE = Suppliers.memoize(() ->
			new GTBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
					Minecraft.getInstance().getEntityModels()));

	public static final IClientItemExtensions EXTENSIONS = new IClientItemExtensions() {

		@Override
		public BlockEntityWithoutLevelRenderer getCustomRenderer() {
			return INSTANCE.get();
		}

	};

	public GTBEWLR(BlockEntityRenderDispatcher dispatcher, EntityModelSet set) {
		super(dispatcher, set);
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext type, PoseStack pose,
							 MultiBufferSource bufferSource, int light, int overlay) {
		if (stack.getItem() instanceof RuneWandItem) {
			renderWand(stack, type, pose, bufferSource, light, overlay);
		}
	}

	public void renderWand(ItemStack stack, ItemDisplayContext type, PoseStack pose,
						   MultiBufferSource bufferSource, int light, int overlay) {
		var manager = Minecraft.getInstance().getModelManager();
		var handle = RuneWandItem.getHandle(stack);
		if (type == ItemDisplayContext.GUI) {
			render(stack, pose, bufferSource, light, overlay,
					manager.getModel(handle.icon()), Consumers.nop());
			return;
		}
		render(stack, pose, bufferSource, light, overlay,
				manager.getModel(handle.model()), GTBEWLR::wandHandle);
		var sel = RuneWandItem.getCore(stack);
		if (sel.getItem() instanceof IWandCoreItem core) {
			render(sel, pose, bufferSource, light, overlay,
					manager.getModel(core.model()), p -> wandCore(handle, p));
		}
	}

	private static void wandHandle(PoseStack pose) {
		pose.rotateAround(Axis.YP.rotationDegrees(90), 0.5f, 0.5f, 0.5f);
	}

	private static void wandCore(WandHandleItem item, PoseStack pose) {
		var pl = Minecraft.getInstance().player;
		if (pl == null) return;
		float tick = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true) + pl.tickCount;
		var data = item.data(ServerProxy.getRegistryAccess());
		float size = data.size();
		pose.translate(0.5, 0.5, 0.5);
		pose.translate(0, data.offset(), 0);
		pose.rotateAround(Axis.YP.rotationDegrees(tick * 20), 0, 0, 0);
		pose.rotateAround(new Quaternionf().rotateTo(1, 1, 1, 0, 1, 0), 0, 0, 0);
		pose.scale(size, size, size);
		pose.translate(-0.5, -0.5, -0.5);
	}

	public void render(ItemStack stack, PoseStack pose, MultiBufferSource buffer,
					   int light, int overlay, BakedModel baked, Consumer<PoseStack> transform) {
		ItemRenderer ir = Minecraft.getInstance().getItemRenderer();
		if (!stack.isEmpty()) {
			pose.pushPose();
			transform.accept(pose);
			for (var model : baked.getRenderPasses(stack, true)) {
				for (var rendertype : model.getRenderTypes(stack, true)) {
					if (rendertype.name.startsWith("glimmeringtales_ender")) rendertype = RenderType.END_GATEWAY;
					VertexConsumer vc = ItemRenderer.getFoilBufferDirect(buffer, rendertype, true, stack.hasFoil());
					ir.renderModelLists(model, stack, light, overlay, pose, vc);
				}
			}
			pose.popPose();
		}
	}

}
