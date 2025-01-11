package dev.xkmc.glimmeringtales.init.data.spell.ocean;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.RuneBlock;
import dev.xkmc.glimmeringtales.content.engine.instance.RemoveLiquidInstance;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.List;

public class SpongeSpell {

	public static final NatureSpellBuilder BUILDER = GTRegistries.OCEAN
			.build(GlimmeringTales.loc("sponge")).focusAndCost(40, 100)
			.block(SpongeSpell::gen, GTItems.RUNE_SPONGE, RuneBlock::liquid,
					(b, e) -> b.add(Blocks.SPONGE, BlockSpell.of(e)),
					(b, e) -> b.add(Blocks.WET_SPONGE, BlockSpell.of(e)))
			.lang("Sponge").desc(
					"[Block] Absorb water nearby",
					"Absorb water nearby, equivalent to range of water",
					SpellTooltipData.of()
			).graph(CoralReefSpell.BUILDER);

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.SPONGE_ABSORB,
						DoubleVariable.of("1"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new RemoveLiquidInstance(
						IntVariable.of("6"),
						IntVariable.of("65"),
						NeoForgeMod.WATER_TYPE.value()
				)
		));

	}

}