package dev.xkmc.glimmeringtales.init.data.spell.life;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.block.ScheduleTick;
import dev.xkmc.l2magic.content.engine.block.SetBlock;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.world.item.Items;

import java.util.List;

public class BambooSpell {
	public static final NatureSpellBuilder BUILDER = GTRegistries.LIFE.get()
			.build(GlimmeringTales.loc("bamboo")).cost(20)
			.spell(ctx -> NatureSpellEntry.ofBlock(gen(ctx), Items.BAMBOO, 1040))
			.block((b, e) -> b.add(GTTagGen.BAMBOO, new BlockSpell(e, false, 1)))
			.lang("Bamboo").desc(
					"[Block] Generate a bamboo cage",
					"Generate a spherical cage of bamboo lasting 5 seconds",
					SpellTooltipData.of()
			);

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SetBlock(GTItems.FAKE_BAMBOO.getDefaultState()),
				new ScheduleTick(IntVariable.of("rand(180,220)"), GTItems.FAKE_BAMBOO.get())
		)).circular(
				DoubleVariable.of("4"),
				DoubleVariable.of("1"),//每个方块放置的时间差距，会随着半径变大而倍增
				false, "i",
				BooleanVariable.of("abs(i_r-4)<2"),//放置方块的条件，距离半径差值小于2，可以看成厚度
				BlockTestCondition.Type.REPLACEABLE.get()
		).move(OffsetModifier.ABOVE);
	}
}