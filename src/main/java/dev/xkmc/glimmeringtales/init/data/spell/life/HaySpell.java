package dev.xkmc.glimmeringtales.init.data.spell.life;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.ResearchBonus;
import dev.xkmc.glimmeringtales.content.core.spell.RuneBlock;
import dev.xkmc.glimmeringtales.content.engine.processor.ProcreationProcessor;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.SphereRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.selector.ApproxBallSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class HaySpell {

	public static final NatureSpellBuilder BUILDER = GTRegistries.LIFE
			.build(GlimmeringTales.loc("procreation")).focusAndCost(60, 300)
			.block(ctx -> procreation(ctx, 4), GTItems.RUNE_HAYBALE, RuneBlock::offset,
					(b, e) -> b.add(Blocks.HAY_BLOCK, BlockSpell.cost(e)))
			.lang("Procreation").desc(
					"[Block] Breed nearby animals",
					"Feed all nearby animals",
					SpellTooltipData.of()
			).graph(ResearchBonus.adv2(11), "O->EF", "EF->L");

	private static ConfiguredEngine<?> procreation(NatureSpellBuilder ctx, double r) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.PLAYER_LEVELUP,
						DoubleVariable.of("1"),
						DoubleVariable.of("5+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new ProcessorEngine(SelectionType.ALL,
						new ApproxBallSelector(DoubleVariable.of(r + "")),
						List.of(new ProcreationProcessor(IntVariable.of("4")))
				),
				new DelayedIterator(
						IntVariable.of("10"),
						IntVariable.of("2"),
						new SphereRandomIterator(
								DoubleVariable.of("5"),
								IntVariable.of("20"),
								new SimpleParticleInstance(
										ParticleTypes.HEART,
										DoubleVariable.of("0.5")
								),
								null
						), null
				)
		));
	}

}
