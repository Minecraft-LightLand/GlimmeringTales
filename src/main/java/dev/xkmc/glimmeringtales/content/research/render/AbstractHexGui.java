package dev.xkmc.glimmeringtales.content.research.render;

import dev.xkmc.l2itemselector.overlay.OverlayUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.List;

public class AbstractHexGui {

	public static void drawIcon(GuiGraphics g, ResourceLocation id, double x, double y, float scale) {
		g.pose().pushPose();
		g.pose().translate(x, y, 0);
		g.pose().scale(scale, scale, 0);
		g.blitSprite(id, -8, -8, 16, 16);
		g.pose().popPose();
	}

	public static void drawItem(GuiGraphics g, Item item, double x, double y, float scale) {
		g.pose().pushPose();
		g.pose().translate(x, y, 0);
		g.pose().scale(scale, scale, 1);
		g.renderItem(item.getDefaultInstance(), -8, -8);
		g.pose().popPose();
	}

	public static void drawHover(GuiGraphics g, List<Component> list, int mx, int my) {
		new OverlayUtil(g, mx, my, -1).renderLongText(Minecraft.getInstance().font, list);
	}


}
