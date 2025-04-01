package dev.xkmc.glimmeringtales.content.item.curio;

import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class GTCurioItem extends Item implements ICurioItem {

	private static final boolean ENABLE_UNIQUE_CHECK = true;

	public GTCurioItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean canEquip(SlotContext ctx, ItemStack stack) {
		if (!ENABLE_UNIQUE_CHECK || !stack.is(GTTagGen.UNIQUE)) return true;
		var le = ctx.entity();
		var opt = CuriosApi.getCuriosInventory(le);
		if (opt.isEmpty()) return false;
		var list = opt.get().findCurios(this);
		if (list.isEmpty()) return true;
		if (list.size() != 1) return false;
		var slot = list.getFirst().slotContext();
		return slot.identifier().equals(ctx.identifier()) && slot.index() == ctx.index();
	}

}
