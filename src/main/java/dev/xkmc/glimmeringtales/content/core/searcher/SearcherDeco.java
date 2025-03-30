package dev.xkmc.glimmeringtales.content.core.searcher;

import dev.xkmc.glimmeringtales.content.item.tool.IBlockSearcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class SearcherDeco implements IItemDecorator {

	@Override
	public boolean render(GuiGraphics g, Font font, ItemStack stack, int x, int y) {
		if (!(stack.getItem() instanceof IBlockSearcher item)) return false;
		var player = Minecraft.getInstance().player;
		if (player == null) return false;
		MutableBoolean bool = new MutableBoolean(false);
		BlockSearcher.iterate(player, e -> {
			if (e == item.getSearcher()) bool.setValue(true);
		});
		if (bool.isFalse()) return false;
		String s = "" + item.getSearcher().count();
		g.pose().pushPose();
		g.pose().translate(0, 0, 250);
		g.drawString(font, s, x + 17 - font.width(s), y + 9, 0xffffff7f);
		g.pose().popPose();
		return true;
	}
}
