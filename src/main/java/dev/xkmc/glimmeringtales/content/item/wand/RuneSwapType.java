package dev.xkmc.glimmeringtales.content.item.wand;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2backpack.content.quickswap.common.EntryRenderContext;
import dev.xkmc.l2backpack.content.quickswap.entry.ISwapEntry;
import dev.xkmc.l2backpack.content.quickswap.type.MatcherSwapType;
import dev.xkmc.l2backpack.init.data.LBConfig;
import dev.xkmc.l2itemselector.overlay.SelectionSideBar;
import dev.xkmc.l2itemselector.overlay.TextBox;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class RuneSwapType extends MatcherSwapType {
	private static final ResourceLocation COPPER = GlimmeringTales.loc("copper_frame");
	private static final ResourceLocation GOLD = GlimmeringTales.loc("gold_frame");
	private static final ResourceLocation WOOD = GlimmeringTales.loc("wood_frame");
	private static final ResourceLocation SELECTED = GlimmeringTales.loc("selected");

	public RuneSwapType() {
		super(GlimmeringTales.MODID + "_rune", true);
	}

	@Override
	public boolean activePopup() {
		return LBConfig.CLIENT.popupArrowOnSwitch.get();
	}

	@Override
	public boolean match(ItemStack stack) {
		return stack.getItem() instanceof RuneWandItem;
	}

	@Override
	public boolean isAvailable(Player player, ISwapEntry<?> token) {
		return !token.getStack().isEmpty();
	}

	@Override
	public void renderSelected(SelectionSideBar.Context ctx, Player player, ISwapEntry<?> token, EntryRenderContext entry) {
		boolean selected = entry.selected();
		ItemStack core = token.getStack();
		ItemStack wand = token.token().stack();
		ResourceLocation TEX ;
		String s =((TranslatableContents)wand.getItem().getName(wand).getContents()).getKey();
		if(s.equals("item.glimmeringtales.gold_wand")){
			TEX = GOLD;
		}  else if(s.equals("item.glimmeringtales.wood_wand")){
			TEX = WOOD;
		}else{
			TEX = COPPER;
		}

		int i = entry.i();
		int size = 9;
		int h = ctx.g().guiHeight();
		int w = ctx.g().guiWidth();
		int r = h / 4 + (int) (entry.easing() * w / 2);
		int dx = w / 2 + (int) (Math.sin(2 * Math.PI * i / size) * r);
		int dy = h / 2 - (int) (Math.cos(2 * Math.PI * i / size) * r);
		ctx.renderItem(core, dx - 8, dy - 8);
		ctx.g().blitSprite(TEX, dx - 12, dy - 12, 24, 24);
		if (selected) {
			ctx.g().blitSprite(SELECTED, dx - 12, dy - 12, 24, 24);
			if (!core.isEmpty()) {
				TextBox box = switch (i) {
					case 1, 2, 3, 4 -> new TextBox(ctx.g(), 0, 1, dx + 16, dy, -1);
					case 5, 6, 7, 8 -> new TextBox(ctx.g(), 2, 1, dx - 16, dy, -1);
					default -> new TextBox(ctx.g(), 1, 2, dx, dy - 16, -1);
				};
				box.renderLongText(ctx.font(), List.of(core.getHoverName()));
				if (core.getItem() instanceof IWandCoreItem item) {
					TextBox desc = i <= 5 ?
							new TextBox(ctx.g(), 0, 1, 20, h / 2, w / 4) :
							new TextBox(ctx.g(), 2, 1, w - 20, h / 2, w / 4);
					desc.renderLongText(ctx.font(), item.getCastTooltip(player, wand, core));
				}
			}
		}

	}

}
