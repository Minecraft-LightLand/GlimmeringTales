package dev.xkmc.glimmeringtales.compat.jei;

import dev.xkmc.glimmeringtales.content.recipe.thunder.StrikeItemRecipe;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.l2core.compat.jei.BaseRecipeCategory;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class StrikeItemRecipeCategory extends BaseRecipeCategory<StrikeItemRecipe, StrikeItemRecipeCategory> {

	protected static final ResourceLocation BG = GlimmeringTales.loc("textures/jei/background.png");

	public StrikeItemRecipeCategory() {
		super(GlimmeringTales.loc("strike_item"), StrikeItemRecipe.class);
	}

	public StrikeItemRecipeCategory init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 0, 0, 72, 18);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, Items.TRIDENT.getDefaultInstance());
		return this;
	}

	@Override
	public Component getTitle() {
		return GTLang.JEI_STRIKE_ITEM.get();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, StrikeItemRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.ingredient);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 1).addItemStack(recipe.result);
	}

}
