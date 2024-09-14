package dev.xkmc.glimmeringtales.init.data.spell.thunder;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.RuneBlock;
import dev.xkmc.glimmeringtales.content.engine.instance.LightningInstance;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class ThunderSpells {

	public static final NatureSpellBuilder BUILDER = GTRegistries.THUNDER
			.build(GlimmeringTales.loc("thunder")).cost(100).mob(16, 1)
			.block(ThunderSpells::gen, GTItems.RUNE_THUNDER, RuneBlock::offset,
					(b, e) -> b.add(GTItems.STRUCK_LOG, BlockSpell.of(e)),
					(b, e) -> b.add(Blocks.LIGHTNING_ROD, BlockSpell.of(e))
			).lang("Thunder").desc(
					"[Block] Create a lightning strike",
					"Create a lightning strike in target position, inflicting %s multiple times",
					SpellTooltipData.of(GTEngine.THUNDER)
			).graph("T<->SFO");

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.TRIDENT_THUNDER.value(),
						DoubleVariable.of("1"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new LightningInstance(DoubleVariable.of("5"))));

	}


}
