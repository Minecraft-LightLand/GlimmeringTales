package dev.xkmc.glimmeringtales.content.block.infuser;

import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.l2core.base.tile.BaseContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class InfuserItemContainer extends BaseContainer<InfuserItemContainer> implements RecipeInput {

	public InfuserItemContainer(int size) {
		super(size);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		if (slot == 0) {
			return stack.is(GTTagGen.CRYSTAL);
		}
		return super.canPlaceItem(slot, stack);
	}

	@Override
	public int size() {
		return 2;
	}

}
