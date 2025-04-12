package dev.xkmc.glimmeringtales.init.data.spell.flame;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.engine.filter.InvulFrameFilter;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.modifier.Dir2NormalModifier;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.FilteredProcessor;
import dev.xkmc.l2magic.content.engine.processor.ProjectileHitEntityProcessor;
import dev.xkmc.l2magic.content.engine.processor.PropertyProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.content.particle.engine.CustomParticleInstance;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.l2magic.content.particle.engine.SimpleParticleData;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;

import java.util.List;
import java.util.Map;

public class SparkBurst {

	public static final NatureSpellBuilder BUILDER = GTRegistries.FLAME
			.build(GlimmeringTales.loc("spark_burst")).focusAndCost(2, 12)
			.mob(12, 0.7, 40, 0)
			.damageVanilla(() -> new DamageType("onFire", 0, DamageEffects.BURNING), DamageTypeTags.IS_FIRE, DamageTypeTags.BYPASSES_COOLDOWN)
			.projectile(SparkBurst::proj)
			.spell(ctx -> new SpellAction(gen(ctx), GTItems.SPARK_BURST.get(),
					2000, SpellCastType.CONTINUOUS, SpellTriggerType.FACING_FRONT))
			.lang("Spark Burst").desc(
					"[Continuous] Shoot sparks that hurts and ignite enemies",
					"Continuously shoot fire sparks forward, deal %s and ignite",
					SpellTooltipData.of(EngineRegistry.DAMAGE)
			).graph(FlamePentagram.HELL_MARK);

	private static final DoubleVariable DMG = DoubleVariable.of("6");

	public static ProjectileConfig proj(NatureSpellBuilder ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.tick(new SimpleParticleInstance(ParticleTypes.FLAME, DoubleVariable.ZERO))
				.hit(new ProjectileHitEntityProcessor())
				.hit(new FilteredProcessor(new InvulFrameFilter(IntVariable.of("5")),
						List.of(new DamageProcessor(ctx.damage(), DMG, true, true)), List.of()))
				.hit(PropertyProcessor.Type.IGNITE.of("100"))
				.size(DoubleVariable.of("0.25"))
				.motion(new SimpleMotion(DoubleVariable.of("0.01"), DoubleVariable.ZERO))
				.build();
	}

	public static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new CustomParticleInstance(
						DoubleVariable.of("-0.1"),
						DoubleVariable.of("0.05"),
						IntVariable.of("10"),
						false,
						SimpleMotion.ZERO,
						new SimpleParticleData(
								RenderTypePreset.LIT,
								ParticleTypes.FLAME
						)
				).move(
						new Dir2NormalModifier(),
						RotationModifier.of("TickUsing*122"),
						ForwardOffsetModifier.of("1")
				),
				new PredicateLogic(
						BooleanVariable.of("TickUsing>=10"),
						actual(ctx),
						null
				)
		)).move(OffsetModifier.of("0", "-0.2", "0"));
	}

	public static ConfiguredEngine<?> actual(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.FIRECHARGE_USE,
						DoubleVariable.of("2"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new CustomProjectileShoot(
						DoubleVariable.of("rand(0.7,0.8)"),
						ctx.proj,
						IntVariable.of("rand(40,60)"),
						false, false,
						Map.of()
				).move(
						OffsetModifier.of("0", "-0.2", "0"),
						RotationModifier.of("rand(-10,10)", "rand(-3,3)")
				)
		));

	}

}
