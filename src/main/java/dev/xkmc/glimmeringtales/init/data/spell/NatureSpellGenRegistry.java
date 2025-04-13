package dev.xkmc.glimmeringtales.init.data.spell;

import dev.xkmc.glimmeringtales.init.data.spell.earth.*;
import dev.xkmc.glimmeringtales.init.data.spell.flame.*;
import dev.xkmc.glimmeringtales.init.data.spell.life.*;
import dev.xkmc.glimmeringtales.init.data.spell.ocean.*;
import dev.xkmc.glimmeringtales.init.data.spell.snow.*;
import dev.xkmc.glimmeringtales.init.data.spell.thunder.*;

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
				StoneBridge.BUILDER,
				AmethystPenetration.BUILDER,
				Earthquake.BUILDER,
				Meteor.BUILDER,
				FlamePentagram.HELL_MARK,
				FlamePentagram.LAVA_BURST,
				FlameDash.BUILDER,
				SparkBurst.BUILDER,
				SoulBurst.BUILDER,
				SnowStorm.WINTER_STORM,
				SnowStorm.SNOW_TORNADO,
				IcyFlash.BUILDER,
				OceanShelter.BUILDER,
				IllusoryField.BUILDER,
				DarkRain.BUILDER,
				Thunderstorm.BUILDER,
				ChargeBurst.BUILDER,
				ThunderSurge.BUILDER,
				ChargeLink.BUILDER
		));
	}

}
