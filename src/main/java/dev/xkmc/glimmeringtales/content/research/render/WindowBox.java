package dev.xkmc.glimmeringtales.content.research.render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public class WindowBox {

	private Screen parent;
	public int x, y, w, h, margin;

	public void setSize(Screen parent, int x, int y, int w, int h, int sh) {
		this.x = x + sh;
		this.y = y + sh;
		this.w = w - sh * 2;
		this.h = h - sh * 2;
		this.margin = sh;
		this.parent = parent;
	}

	public boolean isMouseIn(double mx, double my, int sh) {
		return mx > x - sh && mx < x + w + sh && my > y - sh && my < y + h + sh;
	}

	public void render(GuiGraphics g, int sh, int color, RenderType type) {
		if (type == RenderType.FILL) {
			g.fill(x - sh, y - sh, x + w + sh, y + h + sh, color);
		} else if (type == RenderType.MARGIN) {
			g.fill(x - sh, y - sh, x + w + sh, y, color);
			g.fill(x - sh, y + h, x + w + sh, y + h + sh, color);
			g.fill(x - sh, y, x, y + h, color);
			g.fill(x + w, y, x + w + sh, y + h, color);
		} else {
			g.fill(0, 0, parent.width, y, color);
			g.fill(0, y + h, parent.width, parent.height, color);
			g.fill(0, y, x, y + h, color);
			g.fill(x + w, y, parent.width, y + h, color);
		}
	}

	public void startClip(GuiGraphics g) {
		g.enableScissor(x, y, x + w, y + h);
	}

	public void endClip(GuiGraphics g) {
		g.disableScissor();
	}

	public enum RenderType {
		MARGIN, FILL, MARGIN_ALL
	}

}
