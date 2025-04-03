package dev.xkmc.glimmeringtales.init.data.spell.thunder;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.engine.instance.LightningInstance;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.IPredicate;
import dev.xkmc.l2magic.content.engine.iterator.RingIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.predicate.BlockInRangePredicate;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

import java.util.List;

public class ThunderSurge {

	public static final NatureSpellBuilder BUILDER = GTRegistries.THUNDER
			.build(GlimmeringTales.loc("thunder_surge")).focusAndCost(160, 1280)
			.mob(32, 0.3, 0, 20)
			.grounded()
			.spell(ctx -> new SpellAction(gen(ctx), GTItems.THUNDER_SURGE.get(), 2002,
					SpellCastType.INSTANT, SpellTriggerType.TARGET_POS, cond(ctx))
			).lang("Thunder Surge").desc(
					"[Ranged] Create dense lightning strikes",
					"Create a series of lightning strikes on ground around target position, inflicting %s multiple times",
					SpellTooltipData.of(GTEngine.THUNDER)
			).graph(Thunderstorm.BUILDER);

	private static final DoubleVariable STRIKE = DoubleVariable.of("5");

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.TRIDENT_THUNDER.value(),
						DoubleVariable.of("1"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new LightningInstance(STRIKE)
						.circular("3", "2", "7", null,
								BlockTestCondition.Type.BLOCKS_MOTION.get().invert(),
								BlockTestCondition.Type.BLOCKS_MOTION.get().move(OffsetModifier.BELOW)
						)
		)).mobCastDelay(new RingIterator(
				DoubleVariable.of("6"), IntVariable.of("40"), false,
				new SimpleParticleInstance(ParticleTypes.END_ROD, DoubleVariable.ZERO)
		));

	}

	private static IPredicate cond(NatureSpellBuilder ctx) {
		return BlockInRangePredicate.circular("3", "2", "3", null,
				BlockTestCondition.Type.BLOCKS_MOTION.get().invert(),
				BlockTestCondition.Type.BLOCKS_MOTION.get().move(OffsetModifier.BELOW)
		);
	}


}
