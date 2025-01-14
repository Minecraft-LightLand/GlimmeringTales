package dev.xkmc.glimmeringtales.compat;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

@JeiPlugin
public class GTJeiPlugin implements IModPlugin {

	public static GTJeiPlugin INSTANCE;

	public final ResourceLocation UID = GlimmeringTales.loc("main");

	public final StrikeBlockRecipeCategory STRIKE_BLOCK = new StrikeBlockRecipeCategory();
	public final StrikeItemRecipeCategory STRIKE_ITEM = new StrikeItemRecipeCategory();
	public final ItemTransformationRecipeCategory TRANSFORM = new ItemTransformationRecipeCategory();
	public final RitualRecipeCategory RITUAL = new RitualRecipeCategory();
	public final InfuseRecipeCategory INFUSE = new InfuseRecipeCategory();

	public IGuiHelper GUI_HELPER;

	public GTJeiPlugin() {
		INSTANCE = this;
	}

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
		GUI_HELPER = helper;
		registration.addRecipeCategories(STRIKE_BLOCK.init(helper));
		registration.addRecipeCategories(STRIKE_ITEM.init(helper));
		registration.addRecipeCategories(TRANSFORM.init(helper));
		registration.addRecipeCategories(RITUAL.init(helper));
		registration.addRecipeCategories(INFUSE.init(helper));
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		var level = Minecraft.getInstance().level;
		assert level != null;
		registration.addRecipes(STRIKE_BLOCK.getRecipeType(), STRIKE_BLOCK.getAll(GTRecipes.RT_STRIKE_BLOCK.get()));
		registration.addRecipes(STRIKE_ITEM.getRecipeType(), STRIKE_ITEM.getAll(GTRecipes.RT_STRIKE_ITEM.get()));
		registration.addRecipes(RITUAL.getRecipeType(), RITUAL.getAll(GTRecipes.RT_RITUAL.get()));
		registration.addRecipes(INFUSE.getRecipeType(), INFUSE.getAll(GTRecipes.RT_INFUSE.get()));
		registration.addRecipes(TRANSFORM.getRecipeType(), List.of(
				new ItemTransformation(GTItems.DEPLETED_FLAME.asStack(), GTItems.CRYSTAL_FLAME.asStack()),
				new ItemTransformation(GTItems.DEPLETED_WINTERSTORM.asStack(), GTItems.CRYSTAL_WINTERSTORM.asStack()),
				new ItemTransformation(GTItems.CRYSTAL_VINE.asStack(), GTItems.CRYSTAL_LIFE.asStack())
		));
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(Items.TRIDENT.getDefaultInstance(), STRIKE_BLOCK.getRecipeType());
		registration.addRecipeCatalyst(Blocks.LIGHTNING_ROD.asItem().getDefaultInstance(), STRIKE_BLOCK.getRecipeType());
		registration.addRecipeCatalyst(Items.TRIDENT.getDefaultInstance(), STRIKE_ITEM.getRecipeType());
		registration.addRecipeCatalyst(Blocks.LIGHTNING_ROD.asItem().getDefaultInstance(), STRIKE_ITEM.getRecipeType());
		registration.addRecipeCatalyst(GTItems.RITUAL_MATRIX.asStack(), RITUAL.getRecipeType());
		registration.addRecipeCatalyst(GTItems.RITUAL_ALTAR.asStack(), RITUAL.getRecipeType());
		registration.addRecipeCatalyst(GTItems.INFUSER.asStack(), INFUSE.getRecipeType());
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
	}

	@Override
	public void registerAdvanced(IAdvancedRegistration registration) {
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
	}

}
