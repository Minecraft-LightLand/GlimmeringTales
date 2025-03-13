package dev.xkmc.glimmeringtales.content.block.cauldron;

import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class SimpleCauldronRecipe extends CauldronRecipe<SimpleCauldronRecipe> {

	@SerialField
	public FluidIngredient fluidIngredient = FluidIngredient.empty();
	@SerialField
	public List<Ingredient> itemIngredients = new ArrayList<>();
	@SerialField
	public ItemStack itemResult = ItemStack.EMPTY;
	@SerialField
	public FluidStack fluidResult = FluidStack.EMPTY;

	public SimpleCauldronRecipe() {
		super(GTRecipes.RSC_SIMPLE.get());
	}

	@Override
	public boolean matches(CauldronInput cont, Level level) {
		if (fluidIngredient.isEmpty() ^ cont.fluids().isEmpty())
			return false;
		if (!fluidIngredient.isEmpty()) {
			if (!fluidIngredient.test(cont.fluids().getFluidInTank(0)))
				return false;
		}
		List<ItemStack> items = new ArrayList<>();
		for (int i = 0; i < cont.items().getContainerSize(); i++) {
			ItemStack stack = cont.items().getItem(i);
			if (!stack.isEmpty()) items.add(stack);
		}
		return RecipeMatcher.findMatches(items, itemIngredients) != null;
	}

	@Override
	public ItemStack assemble(CauldronInput cont, HolderLookup.Provider provider) {
		return itemResult.copy();
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider provider) {
		return itemResult.copy();
	}

}
