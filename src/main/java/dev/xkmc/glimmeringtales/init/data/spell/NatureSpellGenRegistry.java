package dev.xkmc.glimmeringtales.init.data.spell;

import dev.xkmc.glimmeringtales.init.data.spell.earth.*;
import dev.xkmc.glimmeringtales.init.data.spell.flame.*;
import dev.xkmc.glimmeringtales.init.data.spell.life.*;
import dev.xkmc.glimmeringtales.init.data.spell.ocean.CoralReefSpell;
import dev.xkmc.glimmeringtales.init.data.spell.ocean.OceanShelter;
import dev.xkmc.glimmeringtales.init.data.spell.ocean.SpongeSpell;
import dev.xkmc.glimmeringtales.init.data.spell.snow.*;
import dev.xkmc.glimmeringtales.init.data.spell.thunder.ChargeBurst;
import dev.xkmc.glimmeringtales.init.data.spell.thunder.ThunderSpells;
import dev.xkmc.glimmeringtales.init.data.spell.thunder.Thunderstorm;

import java.util.ArrayList;
import java.util.List;

public class NatureSpellGenRegistry {

	public static final List<NatureSpellEntry> LIST = new ArrayList<>();

	static {

		// earth
		LIST.addAll(List.of(
				AmethystSpells.BUILDER,
				DripstoneSpells.BUILDER,
				QuartzSpells.BUILDER,
				ClaySpells.BUILDER,
				SandSpells.BUILDER,
				GravelSpells.BUILDER,
				StoneSpells.BUILDER
		));

		// vine
		LIST.addAll(List.of(
				BambooSpell.BUILDER,
				VinesSpell.BUILDER,
				HaySpell.BUILDER,
				CactusSpell.BUILDER,
				FlowerSpell.BUILDER
		));

		// others
		LIST.addAll(List.of(
				NetherrackSpells.BUILDER,
				SoulSandSpells.BUILDER,
				MagmaSpells.BUILDER,

				SnowSpells.BUILDER,
				PowderSnowSpell.BUILDER,
				IceSpells.ICE,
				IceSpells.PACK_ICE,
				IceSpells.BLUE_ICE,

				CoralReefSpell.BUILDER,
				SpongeSpell.BUILDER,

				ThunderSpells.BUILDER
		));

		// advanced
		LIST.addAll(List.of(
				FlamePentagram.HELL_MARK,
				FlamePentagram.LAVA_BURST,
				FlameDash.BUILDER,
				SnowStorm.WINTER_STORM,
				SnowStorm.SNOW_TORNADO,
				IcyFlash.BUILDER,
				StoneBridge.BUILDER,
				AmethystPenetration.BUILDER,
				Earthquake.BUILDER,
				Meteor.BUILDER,
				OceanShelter.BUILDER,
				Thunderstorm.BUILDER,
				ChargeBurst.BUILDER
		));
	}

}
