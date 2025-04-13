package dev.xkmc.glimmeringtales.content.research.render;

import dev.xkmc.glimmeringtales.content.core.spell.SpellElement;
import dev.xkmc.glimmeringtales.content.research.core.HexGraph;
import dev.xkmc.glimmeringtales.content.research.core.HexOrder;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import javax.annotation.Nullable;

public class HexResultGui {

	private static final int FLOW_COUNT = 5, PERIOD = 60;
	private static final float SCALE_NODE = 1f;
	private static final float SCALE_FLOW = 0.2f;

	private final Minecraft minecraft = Minecraft.getInstance();
	private final MagicHexScreen screen;
	private final HexGraph graph;

	private int selected = -1;
	private double sele_x = 0;
	private double sele_y = 0;
	private int tick = 0;
	private boolean isDragging = false;

	int cost = 0;
	final HexOrder data;
	final WindowBox box = new WindowBox();

	public HexResultGui(MagicHexScreen screen) {
		this.screen = screen;
		this.graph = screen.product.getGraph();
		data = screen.product.getMiscData();
	}

	private float getScaleNode() {
		return box.w / 100f;
	}

	private float getScaleFlow() {
		return 0.2f * box.w / 100f;
	}

	private float radius() {
		return 30 * box.w / 100f;
	}

	public void render(GuiGraphics g, double mx, double my, float partial) {
		float progress = (tick + partial) / PERIOD % 1;

		float x0 = box.x + box.w / 2f;
		float y0 = box.y + box.w / 2f;
		int hover = within(mx, my);
		for (int i = 0; i < 6; i++) {
			double ri = i * Math.PI / 3;
			double xi = x0 + radius() * Math.cos(ri);
			double yi = y0 + radius() * Math.sin(ri);
			int color = i == hover ? 0xFF808080 : 0xFFFFFFFF;
			HexRenderUtil.renderHex(g, xi, yi, 10 * getScaleNode(), color);
		}

		for (int i = 0; i < 6; i++) {
			SpellElement elem = getElem(i);
			if (elem == null)
				continue;
			double xi = getX(i);
			double yi = getY(i);
			for (int j = 0; j < 6; j++) {
				if (getElem(j) == null || !graph.connected(data.order()[i], data.order()[j]))
					continue;
				double xj = getX(j);
				double yj = getY(j);
				for (int k = 0; k < FLOW_COUNT; k++) {
					double p = (progress + k * 1.0 / FLOW_COUNT) % 1;
					double xp = xi + (xj - xi) * p;
					double yp = yi + (yj - yi) * p;
					AbstractHexGui.drawIcon(g, elem.getIcon(), xp, yp, getScaleFlow());
				}
			}
		}

		for (int i = 0; i < 6; i++) {
			SpellElement elem = getElem(i);
			if (elem == null)
				continue;
			double xi = getX(i);
			double yi = getY(i);
			AbstractHexGui.drawIcon(g, elem.getIcon(), xi, yi, getScaleNode());
		}
		int hi = box.y + box.w + 36;
		Font font = minecraft.font;
		g.drawString(font, screen.save.getDesc(), box.x + 9, hi, screen.save.getColor(), false);
		g.drawString(font, screen.compile.getDesc(), box.x + 9, hi + 9, screen.compile.getColor(), false);
		g.drawString(font, GTLang.HEX_COST.get(cost), box.x + 9, hi + 18, 0xFF000000, false);
	}

	@Nullable
	SpellElement getElem(int i) {
		return data.getElem(graph, i);
	}

	private double getX(int i) {
		if (isDragging && selected == i)
			return sele_x;
		double ri = i * Math.PI / 3;
		float x0 = box.x + box.w / 2f;
		return x0 + radius() * Math.cos(ri);
	}

	private double getY(int i) {
		if (isDragging && selected == i)
			return sele_y;
		double ri = i * Math.PI / 3;
		float y0 = box.y + box.w / 2f;
		return y0 + radius() * Math.sin(ri);
	}

	public void tick() {
		tick++;
		tick %= PERIOD;
	}

	public boolean mouseDragged(double x0, double y0, int button, double dx, double dy) {
		if (!isDragging) {
			isDragging = true;
			int i = within(x0, y0);
			if (i >= 0) {
				if (getElem(i) != null) {
					selected = i;
				}
			}
		}
		if (selected != -1) {
			sele_x = x0;
			sele_y = y0;
		}
		return false;
	}

	public boolean mouseReleased(double x0, double y0, int button) {
		if (isDragging) {
			isDragging = false;
			if (selected != -1) {
				int i = within(x0, y0);
				if (i >= 0) {
					int elem = data.order()[i];
					data.order()[i] = data.order()[selected];
					data.order()[selected] = elem;
					screen.updated();
				}
			}
			selected = -1;
			return true;
		}
		return false;
	}

	private int within(double mx, double my) {
		float x0 = box.x + box.w / 2f;
		float y0 = box.y + box.w / 2f;
		for (int i = 0; i < 6; i++) {
			double ri = i * Math.PI / 3;
			double xi = x0 + radius() * Math.cos(ri);
			double yi = y0 + radius() * Math.sin(ri);
			if (mx > xi - 8 && mx < xi + 8 && my > yi - 8 && my < yi + 8) {
				return i;
			}
		}
		return -1;
	}

}
