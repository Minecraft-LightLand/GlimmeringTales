package dev.xkmc.glimmeringtales.content.research.render;

import dev.xkmc.glimmeringtales.content.research.core.ResearchState;
import dev.xkmc.glimmeringtales.content.research.core.SpellResearch;
import dev.xkmc.glimmeringtales.content.research.logic.HexCell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MagicHexScreen extends Screen {

	private static final Component TITLE = Component.empty();

	public final Screen parent;
	public final SpellResearch product;
	public final HexGraphGui graph;
	public final HexResultGui result;

	public HexStatus.Save save = HexStatus.Save.YES;
	public HexStatus.Compile compile = HexStatus.Compile.EDITING;

	private double accurate_mouse_x, accurate_mouse_y;
	private boolean isScrolling = false;

	public MagicHexScreen(SpellResearch product) {
		super(TITLE);
		parent = Minecraft.getInstance().screen;
		this.product = product;
		this.graph = new HexGraphGui(this);
		this.result = new HexResultGui(this);
	}

	public void init() {
		int sw = this.width;
		int sh = this.height;
		int w = 300;
		int h = 200;
		int x0 = (sw - w) / 2;
		int y0 = (sh - h) / 2;
		graph.box.setSize(this, x0, y0, 200, 200, 8);
		result.box.setSize(this, x0 + 200, y0, 100, 200, 8);
		if (product.usable()) {
			graph.compile();
			updated();
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

		result.box.render(g, 0, col_bg, WindowBox.RenderType.FILL);
		result.render(g, accurate_mouse_x, accurate_mouse_y, partial);
		result.box.render(g, 8, col_m1, WindowBox.RenderType.MARGIN);
		result.box.render(g, 2, col_m0, WindowBox.RenderType.MARGIN);

		graph.renderHover(g, mx, my);
	}

	@Override
	public void tick() {
		super.tick();
		result.tick();
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
			} else if (result.box.isMouseIn(x0, y0, 0)) {
				return result.mouseDragged(x0, y0, button, dx, dy);
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(double x0, double y0, int button) {
		if (result.mouseReleased(x0, y0, button))
			return true;
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
		if (graph.charTyped(ch))
			return true;
		return super.charTyped(ch, type);
	}

	protected void updated() {
		save();
	}

	private void save() {
		save = HexStatus.Save.NO;
		boolean pass = test();
		getCost();
		if (product.getState() == ResearchState.COMPLETED) {
			if (!pass)
				return;
			if (result.cost > product.getCost())
				return;
		}
		forceSave(pass);
	}

	void forceSave(boolean pass) {
		save = HexStatus.Save.YES;
		product.updateBestSolution(graph.handler, result.data, pass ? result.cost : -1);
		product.save();
	}

	private boolean test() {
		compile = HexStatus.Compile.EDITING;
		if (graph.error != null)
			compile = HexStatus.Compile.ERROR;
		if (graph.flow != null) {
			compile = HexStatus.Compile.FAILED;
			var ans = graph.check(result.data, product.getGraph());
			if (ans) compile = HexStatus.Compile.COMPLETE;
			return ans;
		}
		return false;
	}

	private void getCost() {
		result.cost = 0;
		HexCell cell = new HexCell(graph.handler, 0, 0);
		for (cell.row = 0; cell.row < graph.handler.getRowCount(); cell.row++)
			for (cell.cell = 0; cell.cell < graph.handler.getCellCount(cell.row); cell.cell++) {
				if (cell.exists())
					result.cost++;
			}
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
