package dev.xkmc.glimmeringtales.init.data.spell.snow;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.RandomOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.KnockBackProcessor;
import dev.xkmc.l2magic.content.engine.processor.TeleportProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.selector.SelfSelector;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;

import java.util.List;

public class IcyFlash {

	public static final NatureSpellBuilder BUILDER = GTRegistries.SNOW
			.build(GlimmeringTales.loc("icy_flash")).focusAndCost(40, 200)
			.damageExplosion()
			.spell(e -> new SpellAction(icyFlash(e), GTItems.ICY_FLASH.get(), 2010,
					SpellCastType.INSTANT, SpellTriggerType.TARGET_POS))
			.lang("Icy Flash").desc(
					"[Range] Teleport to target and deal damage",
					"Teleport to target position, dealing %s to surrounding enemies, and inflict %s",
					SpellTooltipData.damageAndEffect()
			).graph("LEO<->FST");

	private static ConfiguredEngine<?> icyFlash(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new ProcessorEngine(  // TP
						SelectionType.ALL,
						new SelfSelector(),
						List.of(
								new TeleportProcessor(
										DoubleVariable.of("PosX"),
										DoubleVariable.of("PosY"),
										DoubleVariable.of("PosZ")
								)
						)
				),
				new SoundInstance(  // Sound
						SoundEvents.BREEZE_LAND,
						DoubleVariable.of("3"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new ProcessorEngine(  // Damage
						SelectionType.ENEMY,
						new ApproxCylinderSelector(
								DoubleVariable.of("1"),
								DoubleVariable.of("2")
						),
						List.of(
								new DamageProcessor(ctx.damage(),
										DoubleVariable.of("4"), true, true),
								new KnockBackProcessor(
										DoubleVariable.of("0.1"),
										DoubleVariable.ZERO,
										DoubleVariable.ZERO
								),
								new EffectProcessor(
										LCEffects.ICE,
										IntVariable.of("100"),
										IntVariable.of("0"),
										false, false
								)
						)
				),
				new LoopIterator(  // Render
						IntVariable.of("100"),
						new SimpleParticleInstance(
								ParticleTypes.SNOWFLAKE,
								DoubleVariable.of("0.1")
						).move(new RandomOffsetModifier(
										RandomOffsetModifier.Type.RECT,
										DoubleVariable.of("2"),
										DoubleVariable.of("2"),
										DoubleVariable.of("2")
								),
								new SetDirectionModifier(
										DoubleVariable.ZERO,
										DoubleVariable.of("-1"),
										DoubleVariable.ZERO
								)
						),
						null
				)
		));
	}
}
