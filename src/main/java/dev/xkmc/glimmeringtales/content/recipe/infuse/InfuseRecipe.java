package dev.xkmc.glimmeringtales.content.recipe.infuse;

import dev.xkmc.glimmeringtales.content.block.infuser.InfuserItemContainer;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.marker.SerialClass;

@SerialClass
public abstract class InfuseRecipe<T extends InfuseRecipe<T>>
		extends BaseRecipe<T, InfuseRecipe<?>, InfuserItemContainer> {

	public InfuseRecipe(RecType<T, InfuseRecipe<?>, InfuserItemContainer> fac) {
		super(fac);
	}

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

}
