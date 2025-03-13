package dev.xkmc.glimmeringtales.content.block.cauldron;

import dev.xkmc.l2core.base.tile.BaseTank;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record CauldronInput(CauldronItemContainer items, BaseTank fluids) implements RecipeInput {

	@Override
	public ItemStack getItem(int index) {
		return items.getItem(index);
	}

	@Override
	public int size() {
		return items.getContainerSize();
	}

}
