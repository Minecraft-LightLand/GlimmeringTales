package dev.xkmc.glimmeringtales.compat;

import dev.xkmc.glimmeringtales.content.recipe.infuse.InfuseRecipe;
import dev.xkmc.glimmeringtales.content.recipe.infuse.SimpleInfuseRecipe;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2core.compat.jei.BaseRecipeCategory;
import dev.xkmc.l2serial.util.Wrappers;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class InfuseRecipeCategory extends BaseRecipeCategory<InfuseRecipe<?>, InfuseRecipeCategory> {

	protected static final ResourceLocation BG = GlimmeringTales.loc("textures/jei/background.png");

	public InfuseRecipeCategory() {
		super(GlimmeringTales.loc("infuse"), Wrappers.cast(InfuseRecipe.class));
	}

	public InfuseRecipeCategory init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 62, 54, 72, 36);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, GTItems.INFUSER.asStack());
		return this;
	}

	@Override
	public Component getTitle() {
		return GTLang.JEI_INFUSE.get();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, InfuseRecipe<?> r, IFocusGroup focuses) {
		if (r instanceof SimpleInfuseRecipe recipe) {
			builder.addSlot(RecipeIngredientRole.INPUT, 1, 19).addIngredients(recipe.input).setStandardSlotBackground();
			builder.addSlot(RecipeIngredientRole.CATALYST, 28, 1).addIngredients(recipe.crystal).setStandardSlotBackground();
			builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 19).addItemStack(recipe.result).setStandardSlotBackground();
		}
	}

}
