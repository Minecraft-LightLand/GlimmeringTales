package dev.xkmc.glimmeringtales.init.data.spell.thunder;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.iterator.LinearIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.CastAtProcessor;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxBallSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class ChargeLink {

	public static final NatureSpellBuilder BUILDER = GTRegistries.THUNDER
			.build(GlimmeringTales.loc("charge_link")).focusAndCost(180, 1800).mob(16, 1)
			.damageCustom(msg -> new DamageType(msg, 0.1f),
					"%s is electrocuted by charge link",
					"%s is electrocuted by %s with charge link",
					DamageTypeTags.IS_LIGHTNING)
			.spell(ctx -> new SpellAction(gen(ctx), GTItems.THUNDER_SURGE.get(), 2002,//TODO
					SpellCastType.INSTANT, SpellTriggerType.FACING_FRONT)
			).lang("Charge Link").desc(
					"[Ranged] Create",//TODO
					"Create",//TODO
					SpellTooltipData.of(GTEngine.THUNDER)
			).graph(Thunderstorm.BUILDER);

	private static final DoubleVariable STRIKE = DoubleVariable.of("5");

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.TRIDENT_THUNDER.value(),
						DoubleVariable.of("1"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				)
		));
	}

	private static EntityProcessor<?> hit(NatureSpellBuilder ctx) {
		return new CastAtProcessor(
				CastAtProcessor.PosType.CENTER,
				CastAtProcessor.DirType.UP,
				new ProcessorEngine(
						SelectionType.ENEMY_NO_FAMILY,
						new ApproxBallSelector(DoubleVariable.of("6")),
						List.of(
								new DamageProcessor(ctx.damage(), STRIKE, true, false),
								new CastAtProcessor(
										CastAtProcessor.PosType.CENTER,
										CastAtProcessor.DirType.UP,
										new LinearIterator(
												DoubleVariable.of("step"), Vec3.ZERO, DoubleVariable.ZERO,
												IntVariable.of("count"), false,
												new SimpleParticleInstance(
														ParticleTypes.CRIT,
														DoubleVariable.ZERO
												), null
										).move(new SetDirectionModifier(
												DoubleVariable.of("mx-PosX"),
												DoubleVariable.of("my-PosY"),
												DoubleVariable.of("mz-PosZ"))
										).withVariables(Map.of(
												"step", DoubleVariable.of("dist/count"),
												"count", DoubleVariable.of("floor(dist*4)"),
												"dist", DoubleVariable.of("sqrt((mx-PosX)^2+(my-PosY)^2+(mz-PosZ)^2)")
										))
								)
						)
				).withVariables(Map.of(
						"mx", DoubleVariable.of("PosX"),
						"my", DoubleVariable.of("PosY"),
						"mz", DoubleVariable.of("PosZ")
				)));
	}


}
