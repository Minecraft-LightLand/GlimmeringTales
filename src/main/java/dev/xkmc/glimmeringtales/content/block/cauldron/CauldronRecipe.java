package dev.xkmc.glimmeringtales.content.block.cauldron;

import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;

@SerialClass
public abstract class CauldronRecipe<T extends CauldronRecipe<T>>
		extends BaseRecipe<T, CauldronRecipe<?>, CauldronInput> {

	@SerialField
	public int time;

	public CauldronRecipe(RecType<T, CauldronRecipe<?>, CauldronInput> fac) {
		super(fac);
	}

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

}
