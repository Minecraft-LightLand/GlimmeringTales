package dev.xkmc.glimmeringtales.content.research.tree;

import dev.xkmc.glimmeringtales.content.research.core.ResearchState;
import dev.xkmc.glimmeringtales.content.research.logic.HexHandler;
import dev.xkmc.glimmeringtales.content.research.render.AbstractHexGui;
import dev.xkmc.glimmeringtales.content.research.render.HexRenderUtil;
import dev.xkmc.glimmeringtales.content.research.render.MagicHexScreen;
import dev.xkmc.glimmeringtales.content.research.render.WindowBox;
import dev.xkmc.l2itemselector.overlay.OverlayUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeGui {

	private static final double MARGIN = 0.9, RADIUS = 2 / Math.sqrt(3);

	private static final int COL_BG = 0xFF808080;
	private static final int COL_ENABLED = 0xFFFFFFFF;
	private static final int COL_DISABLED = 0xFF404040;
	private static final int COL_HOVER = 0xFFFFFF00;

	private final ResearchTreeScreen screen;

	ResearchTree tree;
	final WindowBox box = new WindowBox();

	private float magn = 14;
	private double scrollX, scrollY;
	private int tick;

	public TreeNodeGui(ResearchTreeScreen screen) {
		this.screen = screen;
		tree = screen.tree;
	}

	public void render(GuiGraphics g, double mx, double my, float partial) {
		double x0 = box.x + box.w / 2d;
		double y0 = box.y + box.h / 2d;
		g.pose().pushPose();
		g.pose().translate(x0 + scrollX, y0 + scrollY, 0);
		var hover = tree.getElementOnHex((mx - x0 - scrollX) / magn, (my - y0 - scrollY) / magn);
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
		double x0 = box.x + box.w / 2d;
		double y0 = box.y + box.h / 2d;
		var hover = tree.getElementOnHex((mx - x0 - scrollX) / magn, (my - y0 - scrollY) / magn);
		renderTooltip(g, (int) mx, (int) my, hover);
	}

	public void scroll(double dx, double dy) {
		scrollX += dx;
		scrollY += dy;
	}

	public boolean mouseClicked(double mx, double my, int button) {
		double x0 = box.x + box.w / 2d;
		double y0 = box.y + box.h / 2d;
		if (button == 0) {
			var hover = tree.getElementOnHex((mx - x0 - scrollX) / magn, (my - y0 - scrollY) / magn);
			if (hover != null && hover.data.getState() != ResearchState.LOCKED) {
				Minecraft.getInstance().setScreen(new MagicHexScreen(hover.data));
			}
		}
		return false;
	}

	public void tick() {
		tick++;
	}

	public boolean mouseScrolled(double mx, double my, double amount) {
		magn = Mth.clamp(magn + (float) amount, box.w / 50f, box.w / 10f);
		return true;
	}

	// --- render code ---

	private void renderBG(GuiGraphics g, @Nullable ResearchNode hover) {
		HexRenderUtil.hex_start(g);
		for (var ent : tree.locatedNodes.entrySet()) {
			var node = ent.getValue();
			double x = node.getX() * magn;
			double y = node.getY() * magn;
			double r = MARGIN * RADIUS * magn;
			HexRenderUtil.hex(x, y, r, COL_BG);
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
		new OverlayUtil(g, mx, my, box.w).renderLongText(Minecraft.getInstance().font, list);
	}

}
