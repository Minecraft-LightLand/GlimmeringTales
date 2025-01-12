package dev.xkmc.glimmeringtales.content.recipe.infuse;

import dev.xkmc.glimmeringtales.content.block.infuser.InfuserItemContainer;
import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

@SerialClass
public class SimpleInfuseRecipe extends InfuseRecipe<SimpleInfuseRecipe> {

	@SerialField
	public Ingredient crystal = Ingredient.EMPTY;

	@SerialField
	public Ingredient input = Ingredient.EMPTY;

	@SerialField
	public ItemStack result = ItemStack.EMPTY;

	public SimpleInfuseRecipe(RecType<SimpleInfuseRecipe, InfuseRecipe<?>, InfuserItemContainer> fac) {
		super(GTRecipes.RSI_SIMPLE.get());
	}

	@Override
	public boolean matches(InfuserItemContainer cont, Level level) {
		return crystal.test(cont.getItem(0)) && input.test(cont.getItem(1));
	}

	@Override
	public ItemStack assemble(InfuserItemContainer cont, HolderLookup.Provider provider) {
		cont.getItem(0).shrink(1);
		cont.getItem(1).shrink(1);
		return result.copy();
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider provider) {
		return result.copy();
	}

}
