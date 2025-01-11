package dev.xkmc.glimmeringtales.init.data.spell.flame;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.ResearchBonus;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.LinearIterator;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.logic.RandomVariableLogic;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RandomOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.BlockParticleInstance;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.KnockBackProcessor;
import dev.xkmc.l2magic.content.engine.processor.PropertyProcessor;
import dev.xkmc.l2magic.content.engine.processor.PushProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FlamePentagram {

	public static final NatureSpellBuilder HELL_MARK = GTRegistries.FLAME
			.build(GlimmeringTales.loc("hell_mark")).focusAndCost(100, 400).mob(16, 1)
			.damageFire()
			.spell(ctx -> new SpellAction(flameBurst(ctx),
					GTItems.HELL_MARK.asItem(), 200,
					SpellCastType.INSTANT, SpellTriggerType.TARGET_POS
			)).lang("Hell Mark").desc(
					"[Ranged] Form a flame circle",
					"Create a pentagram on target position and inflict %s to enemies within",
					SpellTooltipData.damage()
			).graph(ResearchBonus.small4(21), "E->SF", "L->OT", "SO->E", "FT->L");

	public static final NatureSpellBuilder LAVA_BURST = GTRegistries.FLAME
			.build(GlimmeringTales.loc("lava_burst")).focusAndCost(5, 20, 30)
			.damageExplosion()
			.spell(ctx -> new SpellAction(earthquake(ctx),
					GTItems.LAVA_BURST.asItem(), 300,
					SpellCastType.CHARGE, SpellTriggerType.HORIZONTAL_FACING
			)).lang("Lava Burst").desc(
					"[Charge] Cause several bursts in the front",
					"Charge attack: create up to 3 arcs of pentagram marks in front of you and inflict %s to enemies within.",
					SpellTooltipData.damage()
			).graph(ResearchBonus.small4(26), "E<->SF", "SF<->OT", "OT<->L");

	private static final DoubleVariable HM_DMG = DoubleVariable.of("8");
	private static final DoubleVariable LB_DMG = DoubleVariable.of("10");

	private static ConfiguredEngine<?> flameBurst(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new DelayedIterator(
						IntVariable.of("5"),
						IntVariable.of("8"),
						new SoundInstance(
								SoundEvents.FIRECHARGE_USE,
								DoubleVariable.of("1"),
								DoubleVariable.of("rand(-0.1,0.1)+rand(-0.1,0.1)")
						), null
				),
				star(4, 0.3).move(
						new SetDirectionModifier(
								DoubleVariable.of("1"),
								DoubleVariable.ZERO,
								DoubleVariable.ZERO),
						RotationModifier.of("rand(0,360)")
				),
				new DelayedIterator(
						IntVariable.of("40"),
						IntVariable.of("1"),
						new ListLogic(List.of(
								new ProcessorEngine(SelectionType.ENEMY,
										new ApproxCylinderSelector(
												DoubleVariable.of("4"),
												DoubleVariable.of("6")
										), List.of(
										new DamageProcessor(ctx.damage(), HM_DMG, true, false),
										new PushProcessor(
												DoubleVariable.of("0.1"),
												DoubleVariable.ZERO,
												DoubleVariable.ZERO,
												PushProcessor.Type.UNIFORM
										),
										new PropertyProcessor(
												PropertyProcessor.Type.IGNITE,
												IntVariable.of("100")
										)
								)).move(SetDirectionModifier.UP),
								new RingRandomIterator(
										DoubleVariable.of("0"),
										DoubleVariable.of("4"),
										DoubleVariable.of("-180"),
										DoubleVariable.of("180"),
										IntVariable.of("10"),
										new RandomVariableLogic(
												"r", 4,
												new PredicateLogic(
														BooleanVariable.of("r2<0.25"),
														new SimpleParticleInstance(
																ParticleTypes.SOUL,
																DoubleVariable.of("0.5+r3*0.2")
														),
														new SimpleParticleInstance(
																ParticleTypes.FLAME,
																DoubleVariable.of("0.5+r3*0.2")
														)
												).move(new SetDirectionModifier(
														DoubleVariable.of("(r0-0.5)*0.2"),
														DoubleVariable.of("1"),
														DoubleVariable.of("(r1-0.5)*0.2")
												))
										), "i"
								))), null
				)
		));
	}

	private static ConfiguredEngine<?> earthquake(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON,
						DoubleVariable.of("1"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new PredicateLogic(
						BooleanVariable.of("Power==0"),
						new RingRandomIterator(
								DoubleVariable.of("0.5"),
								DoubleVariable.of("1"),
								DoubleVariable.of("-180"),
								DoubleVariable.of("180"),
								IntVariable.of("5*min(TickUsing/10,3)"),
								new SimpleParticleInstance(
										ParticleTypes.SMALL_FLAME,
										DoubleVariable.of("0.3")
								).move(RotationModifier.of("135", "rand(-15*min(floor(TickUsing/10),3),0)"),
										ForwardOffsetModifier.of("-4")
								), null
						),
						earthquakeStart(ctx)
				)
		));
	}

	private static ConfiguredEngine<?> earthquakeStart(NatureSpellBuilder ctx) {
		return new DelayedIterator(
				IntVariable.of("min(TickUsing/10,3)"),
				IntVariable.of("10"),
				new RandomVariableLogic("r", 2,
						new LoopIterator(
								IntVariable.of("3+i*2"),
								new ListLogic(List.of(
										new SoundInstance(
												SoundEvents.GENERIC_EXPLODE.value(),
												DoubleVariable.of("1"),
												DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
										),
										star(2, 0.2).move(RotationModifier.of("rand(0,360)")),
										new ProcessorEngine(SelectionType.ENEMY,
												new ApproxCylinderSelector(
														DoubleVariable.of("4"),
														DoubleVariable.of("2")
												), List.of(
												new DamageProcessor(ctx.damage(), LB_DMG, true, true),
												KnockBackProcessor.of("2")
										)),
										new RingRandomIterator(
												DoubleVariable.of("0"),
												DoubleVariable.of("2"),
												DoubleVariable.of("-180"),
												DoubleVariable.of("180"),
												IntVariable.of("100"),
												new BlockParticleInstance(
														Blocks.STONE,
														DoubleVariable.of("0.5+rand(0,0.4)"),
														DoubleVariable.of("0.5"),
														IntVariable.of("rand(20,40)"),
														true
												).move(new SetDirectionModifier(
														DoubleVariable.ZERO,
														DoubleVariable.of("1"),
														DoubleVariable.ZERO)
												), null
										)
								)).move(RotationModifier.of("180/(3+i*2)*(j+(r0+r1)/2)-90"),
										ForwardOffsetModifier.of("6*i+4"),
										new RandomOffsetModifier(
												RandomOffsetModifier.Type.SPHERE,
												DoubleVariable.of("0.1"),
												DoubleVariable.ZERO,
												DoubleVariable.of("0.1")
										)
								).delay(IntVariable.of("abs(i+1-j)*1")), "j"
						)
				), "i"
		);
	}

	private static ConfiguredEngine<?> star(double radius, double step) {
		int linestep = (int) Math.round(1.9 * radius / step);
		int circlestep = (int) Math.round(radius * Math.PI * 2 / step);
		return new ListLogic(List.of(
				new LoopIterator(
						IntVariable.of("5"),
						new LinearIterator(
								DoubleVariable.of(radius * 1.9 / linestep + ""),
								Vec3.ZERO,
								DoubleVariable.ZERO,
								IntVariable.of(linestep + 1 + ""),
								true,
								new SimpleParticleInstance(
										ParticleTypes.FLAME,
										DoubleVariable.ZERO
								),
								null
						).move(
								RotationModifier.of("72*ri"),
								ForwardOffsetModifier.of(radius + ""),
								RotationModifier.of("162")
						), "ri"
				),
				new LoopIterator(
						IntVariable.of(circlestep + ""),
						new SimpleParticleInstance(
								ParticleTypes.FLAME,
								DoubleVariable.ZERO
						).move(RotationModifier.of(360d / circlestep + "*ri"),
								ForwardOffsetModifier.of(radius + "")
						), "ri"
				)
		));
	}

}
