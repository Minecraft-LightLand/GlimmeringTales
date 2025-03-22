package dev.xkmc.glimmeringtales.init.data;

import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import dev.xkmc.glimmeringtales.compat.misc.PatchouliCompat;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2core.serial.advancements.AdvancementGenerator;
import dev.xkmc.l2core.serial.advancements.CriterionBuilder;
import net.minecraft.advancements.critereon.PlayerTrigger;

public class GTAdvGen {

	public static void genAdvancements(RegistrateAdvancementProvider pvd) {
		AdvancementGenerator gen = new AdvancementGenerator(pvd, GlimmeringTales.MODID);
		gen.new TabBuilder("tales").root("root", GTItems.CRYSTAL_EARTH.get(),
						CriterionBuilder.one(PlayerTrigger.TriggerInstance.tick()),
						"Welcome to Glimmering Tales", "Guide to Glimmering Tales")
				.root().patchouli(GlimmeringTales.REGISTRATE,
						CriterionBuilder.one(PlayerTrigger.TriggerInstance.tick()),
						PatchouliCompat.HELPER,
						"Intro to Glimmering Tales", "Read the glimmering guide");
	}
}