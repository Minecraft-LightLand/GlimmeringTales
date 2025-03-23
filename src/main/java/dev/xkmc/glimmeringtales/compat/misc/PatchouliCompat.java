package dev.xkmc.glimmeringtales.compat.misc;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2core.compat.patchouli.PatchouliHelper;
import net.minecraft.world.item.Items;

public class PatchouliCompat {

	public static PatchouliHelper HELPER;

	public static void gen() {
		HELPER = new PatchouliHelper(GlimmeringTales.REGISTRATE, "glimmering_fate").buildModel()
				.buildShapelessRecipe(e -> e.requires(Items.BOOK).requires(GTItems.CRYSTAL_NATURE), () -> Items.BOOK)
				.buildBook("Glimmering Tales Guide",
						"Welcome to Glimmering Tales, a nature-themed magic mod",
						1, GTItems.TAB.key());
	}

}
