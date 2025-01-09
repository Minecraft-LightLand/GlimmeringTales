package dev.xkmc.glimmeringtales.compat;

import dev.xkmc.glimmeringtales.content.recipe.ritual.RitualRecipe;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2core.compat.jei.BaseRecipeCategory;
import dev.xkmc.l2serial.util.Wrappers;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class RitualRecipeCategory extends BaseRecipeCategory<RitualRecipe<?>, RitualRecipeCategory> {

	protected static final ResourceLocation BG = GlimmeringTales.loc("textures/jei/background.png");

	public RitualRecipeCategory() {
		super(GlimmeringTales.loc("ritual"), Wrappers.cast(RitualRecipe.class));
	}

	public RitualRecipeCategory init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 0, 36, 144, 90);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, GTItems.RITUAL_MATRIX.asStack());
		return this;
	}

	@Override
	public Component getTitle() {
		return GTLang.JEI_RITUAL.get();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RitualRecipe<?> recipe, IFocusGroup focuses) {
		int x0 = 40;
		int y0 = 38;
		int r = 30;
		int[] order = new int[]{0, 4, 2, 6, 1, 3, 5, 7};
		builder.addSlot(RecipeIngredientRole.INPUT, x0 - 8, y0 - 8).addIngredients(recipe.core.ingredient());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 120, y0 - 8).addItemStack(recipe.core.remainder());
		int n = recipe.list.size();
		for (int i = 0; i < n; i++) {
			int t = n == 8 || n == 4 ? order[i] : i;
			var e = recipe.list.get(i);
			var a = t * Math.PI * 2 / n - Math.PI / 2;
			int x = x0 + (int) Math.round(r * Math.cos(a));
			int y = y0 + (int) Math.round(r * Math.sin(a));
			builder.addSlot(RecipeIngredientRole.INPUT, x - 8, y - 8).addIngredients(e.ingredient());
		}
	}

	@Override
	public void draw(RitualRecipe<?> recipe, IRecipeSlotsView view, GuiGraphics g, double mx, double my) {
		int x0 = 40;
		int y0 = 38;
		int r = 30;
		int[] order = new int[]{0, 4, 2, 6, 1, 3, 5, 7};
		g.pose().pushPose();
		g.pose().translate(0, 0, -100);
		g.renderFakeItem(GTItems.RITUAL_MATRIX.asStack(), x0 - 8, y0 + 4);
		g.renderFakeItem(GTItems.RITUAL_MATRIX.asStack(), 120, y0 + 4);
		int n = recipe.list.size();
		for (int i = 0; i < n; i++) {
			int t = n == 8 || n == 4 ? order[i] : i;
			var a = t * Math.PI * 2 / n - Math.PI / 2;
			int x = x0 + (int) Math.round(r * Math.cos(a));
			int y = y0 + (int) Math.round(r * Math.sin(a));
			g.renderFakeItem(GTItems.RITUAL_ALTAR.asStack(), x - 8, y + 4);
		}
		g.pose().popPose();
	}

}
