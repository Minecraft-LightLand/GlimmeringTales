package dev.xkmc.glimmeringtales.content.item.wand;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2backpack.content.common.BaseOpenableScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WandScreen extends BaseOpenableScreen<WandMenu> {

	private static final ResourceLocation CORE = GlimmeringTales.loc("wand_core");

	public WandScreen(WandMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {

	}

	@Override
	protected void renderBg(GuiGraphics g, float pt, int mx, int my) {
		super.renderBg(g, pt, mx, my);
		int r = 68;
		int cx = getGuiLeft() + getXSize() / 2 - r / 2;
		int cy = getGuiTop() + 40;
		int t = menu.inventory.player.tickCount / 2 % 12;
		g.blitSprite(CORE, r, r * 12, 0, t * r, cx, cy, 0, r, r);
	}

}
