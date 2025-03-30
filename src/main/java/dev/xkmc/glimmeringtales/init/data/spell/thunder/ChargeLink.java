package dev.xkmc.glimmeringtales.init.data.spell.thunder;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.engine.filter.InvulFrameFilter;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.iterator.LinearIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.CastAtProcessor;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.FilteredProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxBallSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;

import java.util.List;
import java.util.Map;

public class ChargeLink {

	public static final NatureSpellBuilder BUILDER = GTRegistries.THUNDER
			.build(GlimmeringTales.loc("charge_link")).focusAndCost(100, 600).mob(16, 1)
			.damageCustom(msg -> new DamageType(msg, 0.1f),
					"%s is electrocuted by charge link",
					"%s is electrocuted by %s with charge link",
					DamageTypeTags.IS_LIGHTNING)
			.projectile(ChargeLink::proj)
			.spell(ctx -> new SpellAction(gen(ctx), GTItems.CHARGE_LINK.get(), 2002,
					SpellCastType.INSTANT, SpellTriggerType.FACING_FRONT)
			).lang("Charge Link").desc(
					"[Ranged] Create",//TODO
					"Create",//TODO
					SpellTooltipData.of(EngineRegistry.DAMAGE)
			).graph(ChargeBurst.BUILDER);

	private static final DoubleVariable STRIKE = DoubleVariable.of("5");

	public static ProjectileConfig proj(NatureSpellBuilder ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.tick(new SimpleParticleInstance(ParticleTypes.END_ROD, DoubleVariable.ZERO))
				.hit(new DamageProcessor(ctx.damage(), STRIKE, true, false))
				.hit(hitRoot(ctx))
				.size(DoubleVariable.of("0.5"))
				.motion(SimpleMotion.ZERO)
				.build();
	}

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.TRIDENT_THUNDER.value(),
						DoubleVariable.of("1"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				), new CustomProjectileShoot(
						DoubleVariable.of("3"),
						ctx.proj,
						IntVariable.of("rand(20,22)"),
						false, false,
						Map.of()
				).move(OffsetModifier.of("0", "-0.2", "0"))
		));
	}

	private static EntityProcessor<?> hitRoot(NatureSpellBuilder ctx) {
		var dmg = new DamageProcessor(ctx.damage(), STRIKE, true, false);
		EntityProcessor<?> link = link(4, new SimpleParticleInstance(ParticleTypes.END_ROD, DoubleVariable.ZERO));
		var invul = new InvulFrameFilter(IntVariable.of("0"));
		var range = DoubleVariable.of("8");
		return new CastAtProcessor(
				CastAtProcessor.PosType.CENTER,
				CastAtProcessor.DirType.UP,
				new ProcessorEngine(
						SelectionType.ENEMY_NO_FAMILY,
						new ApproxBallSelector(range),
						List.of(new FilteredProcessor(invul, List.of(link, dmg, new CastAtProcessor(
								CastAtProcessor.PosType.CENTER,
								CastAtProcessor.DirType.UP,
								new ProcessorEngine(
										SelectionType.ENEMY_NO_FAMILY,
										new ApproxBallSelector(range),
										List.of(new FilteredProcessor(invul, List.of(link, dmg), List.of())))
										.delay(IntVariable.of("3"))
										.withVariables("mx", "PosX", "my", "PosY", "mz", "PosZ")
						)), List.of())))
						.delay(IntVariable.of("2"))
						.withVariables("mx", "PosX", "my", "PosY", "mz", "PosZ")
		);
	}

	private static EntityProcessor<?> link(double density, ConfiguredEngine<?> ins) {
		return new CastAtProcessor(
				CastAtProcessor.PosType.CENTER,
				CastAtProcessor.DirType.UP,
				new LinearIterator(DoubleVariable.of("step"), IntVariable.of("count"), false, ins)
						.move(SetDirectionModifier.of("mx-PosX", "my-PosY", "mz-PosZ"))
						.withVariables(
								"dist", "sqrt((mx-PosX)^2+(my-PosY)^2+(mz-PosZ)^2)",
								"count", "floor(dist*" + density + ")+1",
								"step", "dist/count"
						)
		);
	}


}
