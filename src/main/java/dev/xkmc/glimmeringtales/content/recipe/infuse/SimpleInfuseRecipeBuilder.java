package dev.xkmc.glimmeringtales.content.recipe.infuse;

import dev.xkmc.glimmeringtales.content.block.infuser.InfuserItemContainer;
import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2core.serial.recipe.BaseRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class SimpleInfuseRecipeBuilder extends BaseRecipeBuilder<
		SimpleInfuseRecipeBuilder, SimpleInfuseRecipe,
		InfuseRecipe<?>, InfuserItemContainer
		> {

	public SimpleInfuseRecipeBuilder(Item crystal, Ingredient input, ItemStack result, int time) {
		super(GTRecipes.RSI_SIMPLE.get(), result.getItem());
		this.recipe.crystal = Ingredient.of(crystal);
		this.recipe.input = input;
		this.recipe.result = result;
		this.recipe.time = time;
	}

}
