package dev.xkmc.glimmeringtales.init.data.spell.earth;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.ResearchBonus;
import dev.xkmc.glimmeringtales.content.engine.render.FakeBlockRenderData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.iterator.SphereRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.logic.RandomVariableLogic;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.CastAtProcessor;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.KnockBackProcessor;
import dev.xkmc.l2magic.content.engine.processor.PropertyProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxBallSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.BoundingData;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import net.minecraft.sounds.SoundEvents;

import java.util.List;
import java.util.Map;

public class Meteor {

	public static final NatureSpellBuilder BUILDER = GTRegistries.EARTH
			.build(GlimmeringTales.loc("meteor")).focusAndCost(160, 1000)
			.damageExplosion().projectile(Meteor::proj)
			.spell(e -> new SpellAction(starfall(e), GTItems.METEOR.get(), 2010,
					SpellCastType.INSTANT, SpellTriggerType.TARGET_POS))
			.lang("Meteor").desc(
					"[Ranged] Create a meteor falling at target",
					"Create a meteor falling slowly, dealing %s on impact",
					SpellTooltipData.damage()
			).graph(ResearchBonus.small4(35), "T<->LEFSO");

	private static ProjectileConfig proj(NatureSpellBuilder ctx) {
		return ProjectileConfig.builder(SelectionType.ALL)
				.tick(tick())
				.land(land(ctx)).size(new BoundingData(DoubleVariable.of("1"), true))
				.hit(new CastAtProcessor(CastAtProcessor.PosType.ORIGINAL, CastAtProcessor.DirType.ORIGINAL, land(ctx)))
				.renderer(new FakeBlockRenderData(GTItems.DUMMY_METEOR.getDefaultState(), DoubleVariable.of("2")))
				.build();
	}

	private static ConfiguredEngine<?> tick() {
		return new SphereRandomIterator(
				DoubleVariable.of("1.5"),
				IntVariable.of("100"),
				new DustParticleInstance(
						ColorVariable.Static.of(0xFF0000),
						DoubleVariable.of("0.5"),
						DoubleVariable.of("0.2"),
						IntVariable.of("rand(20,30)")
				), null
		);
	}

	private static ConfiguredEngine<?> land(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new RingRandomIterator(
						DoubleVariable.of("0"),
						DoubleVariable.of("2"),
						DoubleVariable.of("0"),
						DoubleVariable.of("360"),
						IntVariable.of("500"),
						new RandomVariableLogic("r", 2,
								new DustParticleInstance(
										ColorVariable.Static.of(0xFF0000),
										DoubleVariable.of("1"),
										DoubleVariable.of("0.2+r1"),
										IntVariable.of("40")
								).move(RotationModifier.of("0", "45*r0"))
						), null
				).move(SetDirectionModifier.of("1", "0", "0")),
				new ProcessorEngine(
						SelectionType.ENEMY,
						new ApproxBallSelector(DoubleVariable.of("8")),
						List.of(
								new DamageProcessor(ctx.damage(), DoubleVariable.of("20"), true, true),
								new PropertyProcessor(
										PropertyProcessor.Type.IGNITE,
										IntVariable.of("200")
								),
								new KnockBackProcessor(
										DoubleVariable.of("2"),
										DoubleVariable.of("45"),
										DoubleVariable.ZERO
								)
						)
				),
				new SoundInstance(
						SoundEvents.GENERIC_EXPLODE.value(),
						DoubleVariable.of("5"),
						DoubleVariable.ZERO
				)
		)).move(ForwardOffsetModifier.of("1"));
	}

	private static ConfiguredEngine<?> starfall(NatureSpellBuilder ctx) {
		var shadow = new RingRandomIterator(
				DoubleVariable.ZERO,
				DoubleVariable.of("1.5"),
				DoubleVariable.ZERO,
				DoubleVariable.of("360"),
				IntVariable.of("100"),
				new DustParticleInstance(
						ColorVariable.Static.of(0x000000),
						DoubleVariable.of("1"),
						DoubleVariable.ZERO,
						IntVariable.of("55")
				), null
		);
		return new ListLogic(List.of(shadow, new CustomProjectileShoot(DoubleVariable.of("0.4"), ctx.proj,
				IntVariable.of("200"), false, false, Map.of()
		).move(SetDirectionModifier.of("0", "-1", "0"), ForwardOffsetModifier.of("-8"))));
	}

}
