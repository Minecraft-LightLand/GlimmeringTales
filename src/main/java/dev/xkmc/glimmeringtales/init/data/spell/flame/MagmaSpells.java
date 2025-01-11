package dev.xkmc.glimmeringtales.init.data.spell.flame;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.RuneBlock;
import dev.xkmc.glimmeringtales.content.engine.instance.MeltBlockInstance;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class MagmaSpells {

	public static final NatureSpellBuilder BUILDER = GTRegistries.FLAME
			.build(GlimmeringTales.loc("magma")).focusAndCost(40, 160)
			.block(MagmaSpells::gen, GTItems.RUNE_MAGMA, RuneBlock::of,
					(b, e) -> b.add(Blocks.MAGMA_BLOCK, BlockSpell.of(e)),
					(b, e) -> b.add(GTTagGen.FAKE_MAGMA, BlockSpell.of(e))
			).lang("Meltdown").desc(
					"[Block] Melts stones into magma temporarily",
					"Melts stone, deep slate, and netherrack in a circular area for 10 seconds",
					SpellTooltipData.of()
			).graph(NetherrackSpells.BUILDER);

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.BUCKET_FILL_LAVA,
						DoubleVariable.of("1"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new MeltBlockInstance(IntVariable.of("rand(180,220)")).circular(
						DoubleVariable.of("6"), DoubleVariable.of("2"), false, null,
						BlockTestCondition.Type.REPLACEABLE.get().move(OffsetModifier.ABOVE))
		));

	}


}
