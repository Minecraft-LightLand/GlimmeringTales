package dev.xkmc.glimmeringtales.content.research.client.tree;

import dev.xkmc.glimmeringtales.content.research.client.base.AbstractHexGui;
import dev.xkmc.glimmeringtales.content.research.client.base.AbstractScalableGui;
import dev.xkmc.glimmeringtales.content.research.client.graph.HexRenderUtil;
import dev.xkmc.glimmeringtales.content.research.client.graph.MagicHexScreen;
import dev.xkmc.glimmeringtales.content.research.core.ResearchState;
import dev.xkmc.glimmeringtales.content.research.logic.HexHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeGui extends AbstractScalableGui {

	private static final double MARGIN = 0.9, RADIUS = 2 / Math.sqrt(3);

	private static final int COL_BG = 0xFF7F7F7F;
	private static final int COL_ENABLED = 0xFFFFFFFF;
	private static final int COL_DISABLED = 0xFF3F3F3F;
	private static final int COL_HOVER = 0xFFFFFF00;

	private static final int COL_LOCKED = 0xFF5F5F5F;
	private static final int COL_UNLOCKED = 0xFFFF7FFF;
	private static final int COL_COMPLETE = 0xFF7FFFFF;
	private static final int COL_BEST = 0xFFFFFF7F;

	private final ResearchTreeScreen screen;
	ResearchTree tree;
	private int tick;

	public TreeNodeGui(ResearchTreeScreen screen) {
		this.screen = screen;
		tree = screen.tree;
	}

	@Override
	public void initScale() {
		magn = Math.min(box.w, box.h) / 16f;
	}

	public void render(GuiGraphics g, double mx, double my, float partial) {
		double x0 = box.x + box.w / 2d;
		double y0 = box.y + box.h / 2d;
		g.pose().pushPose();
		g.pose().translate(x0 + scrollX, y0 + scrollY, 0);
		var hover = tree.getElementOnHex(getMX(mx), getMY(my));
		renderBG(g, hover);
		double ratio, width, length;
		ratio = 1 / 4d;
		width = RADIUS * ratio * magn;
		length = HexHandler.WIDTH * (1 - ratio) * magn;
		renderPath(g, width, length);
		renderIcons(g);
		g.pose().popPose();
	}

	public void renderHover(GuiGraphics g, double mx, double my) {
		var hover = tree.getElementOnHex(getMX(mx), getMY(my));
		renderTooltip(g, (int) mx, (int) my, hover);
	}

	public boolean mouseClicked(double mx, double my, int button) {
		if (button == 0) {
			var hover = tree.getElementOnHex(getMX(mx), getMY(my));
			if (hover != null && hover.data.getState() != ResearchState.LOCKED) {
				Minecraft.getInstance().setScreen(new MagicHexScreen(screen, hover.data));
			}
		}
		return false;
	}

	public void tick() {
		tick++;
	}

	// --- render code ---

	private void renderBG(GuiGraphics g, @Nullable ResearchNode hover) {
		HexRenderUtil.hex_start(g);
		for (var ent : tree.locatedNodes.entrySet()) {
			var node = ent.getValue();
			double x = node.getX() * magn;
			double y = node.getY() * magn;
			double r = MARGIN * RADIUS * magn;
			var col = switch (node.data.getState()) {
				case LOCKED -> COL_LOCKED;
				case UNLOCKED -> COL_UNLOCKED;
				case COMPLETED -> COL_COMPLETE;
			};
			if (node.data.bested()) {
				col = COL_BEST;
			}
			HexRenderUtil.hex(x, y, r, col);
			HexRenderUtil.hex(x, y, r * 0.8, COL_BG);
		}
		if (hover != null)
			HexRenderUtil.hex(hover.getX() * magn, hover.getY() * magn, RADIUS * magn / 2, COL_HOVER);
		HexRenderUtil.common_end();
	}

	private void renderPath(GuiGraphics g, double width, double length) {
		HexRenderUtil.path_start(g, width, length, HexHandler.WIDTH * magn, 0);
		for (var ent : tree.locatedNodes.entrySet()) {
			var node = ent.getValue();
			double x = node.getX() * magn;
			double y = node.getY() * magn;
			var col = node.unlocked() ? COL_ENABLED : COL_DISABLED;
			if (node.main != null)
				HexRenderUtil.path(x, y, 0, col);
			if (node.branch != null)
				HexRenderUtil.path(x, y, 1, col);
			if (node.next != null) {
				x = node.next.getX() * magn;
				y = node.next.getY() * magn;
				HexRenderUtil.path(x, y, 2, col);
			}
		}
		HexRenderUtil.common_end();
	}

	private void renderIcons(GuiGraphics g) {
		for (var ent : tree.locatedNodes.entrySet()) {
			var node = ent.getValue();
			double x = node.getX() * magn;
			double y = node.getY() * magn;
			AbstractHexGui.drawItem(g, node.getIcon(), x, y, magn / 10);
		}
	}

	private void renderTooltip(GuiGraphics g, int mx, int my, @Nullable ResearchNode hover) {
		if (hover == null) return;
		List<Component> list = new ArrayList<>();
		list.add(hover.hex.value().icon().getDescription());
		hover.data.getFullDesc(list, hover.hex.value().bonuses());
		AbstractHexGui.drawHover(g, list, mx, my);
	}

}
