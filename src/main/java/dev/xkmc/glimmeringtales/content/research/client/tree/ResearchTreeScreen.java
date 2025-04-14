package dev.xkmc.glimmeringtales.content.research.client.tree;

import dev.xkmc.glimmeringtales.content.research.client.base.WindowBox;
import dev.xkmc.glimmeringtales.content.research.core.SpellResearch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ResearchTreeScreen extends Screen {

	private static final Component TITLE = Component.empty();

	public final Screen parent;
	public final ResearchTree tree;
	public final TreeNodeGui graph;

	private double accurate_mouse_x, accurate_mouse_y;
	private boolean isScrolling = false;

	public ResearchTreeScreen(ResearchTree tree) {
		super(TITLE);
		parent = Minecraft.getInstance().screen;
		this.tree = tree;
		this.graph = new TreeNodeGui(this);
	}

	public void init() {
		int sw = this.width;
		int sh = this.height;
		int h = (int) (Math.min(sw / 300d, sh / 200d) * 150);
		int w = (int) (h * 1.5);
		int x0 = (sw - w) / 2;
		int y0 = (sh - h) / 2;
		graph.box.setSize(this, x0, y0, w, h, 8);
		graph.initScale();
	}

	public void focusOn(SpellResearch product) {
		var node = tree.allNodes.get(product.getId());
		if (node != null) {
			graph.scrollTo(node.getX(), node.getY());
		}
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float partial) {
		int col_bg = 0xFFC0C0C0;
		int col_m0 = 0xFF808080;
		int col_m1 = 0xFFFFFFFF;
		super.renderBackground(g, mx, my, partial);
		super.render(g, 0, 0, partial);
		if (Math.abs(accurate_mouse_x - mx) > 1)
			accurate_mouse_x = mx;
		if (Math.abs(accurate_mouse_y - my) > 1)
			accurate_mouse_y = my;
		graph.box.render(g, 0, col_bg, WindowBox.RenderType.FILL);
		graph.box.startClip(g);
		graph.render(g, accurate_mouse_x, accurate_mouse_y, partial);
		graph.box.endClip(g);
		graph.box.render(g, 8, col_m1, WindowBox.RenderType.MARGIN);
		graph.box.render(g, 2, col_m0, WindowBox.RenderType.MARGIN);
		graph.renderHover(g, mx, my);
	}

	@Override
	public void tick() {
		super.tick();
		graph.tick();
	}

	public void mouseMoved(double mx, double my) {
		if (isScrolling)
			return;
		this.accurate_mouse_x = mx;
		this.accurate_mouse_y = my;
	}

	public boolean mouseDragged(double x0, double y0, int button, double dx, double dy) {
		if (button != 0) {
			isScrolling = false;
			return false;
		} else {
			if (graph.box.isMouseIn(x0, y0, 0)) {
				isScrolling = true;
				graph.scroll(dx, dy);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(double x0, double y0, int button) {
		return super.mouseReleased(x0, y0, button);
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (graph.box.isMouseIn(mx, my, 0) && graph.mouseClicked(mx, my, button))
			return true;
		return super.mouseClicked(mx, my, button);
	}

	@Override
	public boolean mouseScrolled(double mx, double my, double ignored, double amount) {
		if (graph.box.isMouseIn(mx, my, 0) && graph.mouseScrolled(mx, my, amount))
			return true;
		return super.mouseScrolled(mx, my, amount, ignored);
	}

	@Override
	public boolean charTyped(char ch, int type) {
		return super.charTyped(ch, type);
	}

	@Override
	public boolean keyPressed(int key, int scan, int modifier) {
		return super.keyPressed(key, scan, modifier);
	}

	@Override
	public void onClose() {
		if (this.minecraft != null && this.minecraft.screen == this && this.parent != null)
			this.minecraft.setScreen(this.parent);
	}

}
