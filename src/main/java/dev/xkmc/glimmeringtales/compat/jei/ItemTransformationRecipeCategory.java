package dev.xkmc.glimmeringtales.compat.jei;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2core.compat.jei.BaseRecipeCategory;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ItemTransformationRecipeCategory extends BaseRecipeCategory<ItemTransformation, ItemTransformationRecipeCategory> {

	protected static final ResourceLocation BG = GlimmeringTales.loc("textures/jei/background.png");

	public ItemTransformationRecipeCategory() {
		super(GlimmeringTales.loc("transformation"), ItemTransformation.class);
	}

	public ItemTransformationRecipeCategory init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 0, 0, 72, 18);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, GTItems.CRYSTAL_NATURE.asStack());
		return this;
	}

	@Override
	public Component getTitle() {
		return GTLang.JEI_TRANSFORM.get();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ItemTransformation recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStack(recipe.from());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 1).addItemStack(recipe.to());
	}

}
