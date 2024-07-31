package dev.xkmc.glimmeringtales.content.recipe;

import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2core.serial.recipe.BaseRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class StrikeBlockRecipeBuilder extends BaseRecipeBuilder<StrikeBlockRecipeBuilder, StrikeBlockRecipe, StrikeBlockRecipe, StrikeBlockRecipe.Inv> {

	public StrikeBlockRecipeBuilder(Block in, Block out, ItemStack result) {
		super(GTRecipes.RS_STRIKE_BLOCK.get(), out.asItem());
		this.recipe.ingredient = in;
		this.recipe.transformTo = out;
		this.recipe.result = result;
	}

}