package dev.xkmc.glimmeringtales.init.data.spell;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.content.research.core.HexGraphData;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import net.minecraft.data.worldgen.BootstrapContext;

public class GTSpells {

	public static void genSpells(BootstrapContext<SpellAction> ctx) {
		for (var e : NatureSpellGenRegistry.LIST) {
			e.register(ctx);
		}
	}

	public static void genProjectiles(BootstrapContext<ProjectileConfig> ctx) {
		for (var e : NatureSpellGenRegistry.LIST) {
			e.registerProjectile(ctx);
		}
	}

	public static void genNature(BootstrapContext<NatureSpell> ctx) {
		for (var e : NatureSpellGenRegistry.LIST) {
			e.regNature(ctx);
		}
	}

	public static void genMap(RegistrateDataMapProvider pvd) {
		var block = pvd.builder(GTRegistries.BLOCK.reg());
		var item = pvd.builder(GTRegistries.RUNE_BLOCK.reg());
		for (var e : NatureSpellGenRegistry.LIST) {
			e.regBlock(block);
			e.regRune(item);
		}
	}

	public static void addLang(RegistrateLangProvider pvd) {
		for (var e : NatureSpellGenRegistry.LIST) {
			e.genLang(pvd);
		}
	}

	public static void genGraph(BootstrapContext<HexGraphData> ctx) {
		for (var e : NatureSpellGenRegistry.LIST) {
			e.regGraph(ctx);
		}
	}

}
