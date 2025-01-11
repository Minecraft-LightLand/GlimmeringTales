package dev.xkmc.glimmeringtales.init.data.spell.snow;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.ResearchBonus;
import dev.xkmc.glimmeringtales.content.engine.particle.FarParticleRenderData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.logic.RandomVariableLogic;
import dev.xkmc.l2magic.content.engine.modifier.*;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.PushProcessor;
import dev.xkmc.l2magic.content.engine.selector.ArcCubeSelector;
import dev.xkmc.l2magic.content.engine.selector.LinearCubeSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.motion.MovePosMotion;
import dev.xkmc.l2magic.content.particle.engine.CustomParticleInstance;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.l2magic.content.particle.engine.SimpleParticleData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;

import java.util.List;

public class SnowStorm {

	public static final NatureSpellBuilder WINTER_STORM = GTRegistries.SNOW
			.build(GlimmeringTales.loc("winter_storm")).focusAndCost(2, 5).mob(5, 1).damageFreeze()
			.spell(ctx -> new SpellAction(winterStorm(ctx, 4, 1.5, 1),
					GTItems.WINTER_STORM.asItem(), 100,
					SpellCastType.CONTINUOUS, SpellTriggerType.SELF_POS
			)).lang("Winter Storm").desc(
					"[Continuous] Create a circle of storm",
					"Continuous Attack: Create a circle of storm, dealing %s, inflict %s, and push enemies away",
					SpellTooltipData.damageAndEffect()
			).graph(ResearchBonus.small4(24), "SF->LE", "LE->OT", "OT->SF");

	public static final NatureSpellBuilder SNOW_TORNADO = GTRegistries.SNOW
			.build(GlimmeringTales.loc("snow_tornado")).focusAndCost(1, 5).mob(6, 1).damageFreeze()
			.spell(ctx -> new SpellAction(tornado(ctx),
					GTItems.SNOW_TORNADO.asItem(), 100,
					SpellCastType.CONTINUOUS, SpellTriggerType.FACING_FRONT
			)).lang("Snow Tornado").desc(
					"[Continuous] Create a circle of storm",
					"Continuous Attack: Create snow tornado in front of you, dealing %s and inflict %s",
					SpellTooltipData.damageAndEffect()
			).graph(ResearchBonus.small4(23), "S->OT", "OT->LEF", "LEF->S");

	private static final DoubleVariable WS_DMG = DoubleVariable.of("4");
	private static final DoubleVariable ST_DMG = DoubleVariable.of("4");

	private static ConfiguredEngine<?> winterStorm(NatureSpellBuilder ctx, double r, double y, double size) {
		return new ListLogic(List.of(
				new PredicateLogic(
						BooleanVariable.of("TickUsing%2==0"),
						new SoundInstance(
								SoundEvents.BREEZE_IDLE_GROUND,
								DoubleVariable.of("1"),
								DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
						), null
				),
				new PredicateLogic(
						BooleanVariable.of("TickUsing>=10"),
						new ProcessorEngine(SelectionType.ENEMY,
								new ArcCubeSelector(
										IntVariable.of("11"),
										DoubleVariable.of(r + ""),
										DoubleVariable.of(size * 2 + ""),
										DoubleVariable.of("-180"),
										DoubleVariable.of("-180+360/12*11")
								),
								List.of(
										new DamageProcessor(ctx.damage(), WS_DMG, true, true),
										new PushProcessor(
												DoubleVariable.of("0.1"),
												DoubleVariable.of("75"),
												DoubleVariable.ZERO,
												PushProcessor.Type.TO_CENTER
										),
										new EffectProcessor(
												LCEffects.ICE,
												IntVariable.of("100"),
												IntVariable.of("0"),
												false, false
										)
								)), null),
				new DelayedIterator(
						IntVariable.of("10"),
						IntVariable.of("2"),
						new RingRandomIterator(
								DoubleVariable.of((r - size) + ""),
								DoubleVariable.of((r + size) + ""),
								DoubleVariable.of("-180"),
								DoubleVariable.of("180"),
								IntVariable.of("5"),
								new SimpleParticleInstance(
										ParticleTypes.SNOWFLAKE,
										DoubleVariable.of("0.5")
								).move(RotationModifier.of("75"),
										OffsetModifier.of("0", "rand(" + (y - size) + "," + (y + size) + ")", "0")
								), null
						), null
				)
		));
	}

	private static ConfiguredEngine<?> tornado(NatureSpellBuilder ctx) {
		double vsp = 0.5;
		int life = 20;
		double rate = Math.tan(10 * Mth.DEG_TO_RAD);
		double w = vsp * 180 / Math.PI / 2;
		double ir = 0.3;
		String radius = ir + "+TickCount*" + vsp * rate;
		String angle = w / (vsp * rate) + "*(log(" + radius + ")+log(" + ir + "))";
		return new ListLogic(List.of(
				new PredicateLogic(
						BooleanVariable.of("TickUsing%2==0"),
						new SoundInstance(
								SoundEvents.BREEZE_SLIDE,
								DoubleVariable.of("1"),
								DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
						), null
				),
				new PredicateLogic(
						BooleanVariable.of("TickUsing>=10"),
						new ProcessorEngine(SelectionType.ENEMY,
								new LinearCubeSelector(
										IntVariable.of("6"),
										DoubleVariable.of("1.5")
								),
								List.of(
										new DamageProcessor(ctx.damage(), ST_DMG, true, true),
										new PushProcessor(
												DoubleVariable.of("0.1"),
												DoubleVariable.ZERO,
												DoubleVariable.ZERO,
												PushProcessor.Type.TO_CENTER
										),
										new EffectProcessor(
												LCEffects.ICE,
												IntVariable.of("100"),
												IntVariable.of("0"),
												false, false
										)
								)), null),
				new DelayedIterator(
						IntVariable.of("10"),
						IntVariable.of("1"),
						new RandomVariableLogic("r", 1,
								new RingRandomIterator(
										DoubleVariable.of(ir + ""),
										DoubleVariable.of(ir + ""),
										DoubleVariable.of("-180"),
										DoubleVariable.of("180"),
										IntVariable.of("3"),
										new RandomVariableLogic("r", 1,
												new CustomParticleInstance(
														DoubleVariable.of("0"),
														DoubleVariable.of("0.15"),
														IntVariable.of("" + life),
														true,
														new MovePosMotion(List.of(
																ForwardOffsetModifier.of("-" + ir),
																RotationModifier.of(angle),
																ForwardOffsetModifier.of(radius),
																new Normal2DirModifier(),
																ForwardOffsetModifier.of("TickCount*" + vsp)
														)),
														new FarParticleRenderData(new SimpleParticleData(
																RenderTypePreset.NORMAL,
																ParticleTypes.SNOWFLAKE
														), DoubleVariable.of("r0+1"))
												)
										).move(NormalOffsetModifier.of("rand(" + (-vsp) + "," + vsp + ")")), null
								).move(new Dir2NormalModifier())
						), null
				)
		));
	}

}
