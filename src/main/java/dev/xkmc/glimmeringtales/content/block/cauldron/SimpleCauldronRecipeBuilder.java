package dev.xkmc.glimmeringtales.content.block.cauldron;

import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2core.serial.recipe.BaseRecipeBuilder;
import net.minecraft.world.item.ItemStack;

public class SimpleCauldronRecipeBuilder extends BaseRecipeBuilder<
		SimpleCauldronRecipeBuilder, SimpleCauldronRecipe,
		CauldronRecipe<?>, CauldronInput
		> {

	public SimpleCauldronRecipeBuilder(ItemStack result, int time) {
		super(GTRecipes.RSC_SIMPLE.get(), result.getItem());
		this.recipe.itemResult = result;
		this.recipe.time = time;
	}

}
