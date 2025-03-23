package dev.xkmc.glimmeringtales.init.data.spell.ocean;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTDamageTypeGen;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
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
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;

import java.util.List;
import java.util.Map;

public class DarkRain {

	public static final NatureSpellBuilder BUILDER = GTRegistries.OCEAN
			.build(GlimmeringTales.loc("dark_rain")).focusAndCost(2, 16).mob(16, 1)
			.damageCustom(e -> new DamageType(e, 0.1f, DamageEffects.DROWNING),
					"%s is cursed by dark rain", "%s is cursed by %s's dark rain",
					GTDamageTypeGen.magic())
			.projectile(DarkRain::proj)
			.spell(ctx -> new SpellAction(gen(ctx), GTItems.DARK_RAIN.get(),
					2000, SpellCastType.CONTINUOUS, SpellTriggerType.TARGET_POS))
			.lang("Dark Rain").desc(
					"[Continuous] Create rain that hurt and curse enemies",
					"Create droplet falling from sky, dealing %s and inflicts %s",
					SpellTooltipData.of(EngineRegistry.DAMAGE, EngineRegistry.EFFECT)
			).graph(IllusoryField.BUILDER);

	private static final DoubleVariable DMG = DoubleVariable.of("4");

	private static ProjectileConfig proj(NatureSpellBuilder ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.motion(SimpleMotion.ZERO)
				.tick(new SimpleParticleInstance(ParticleTypes.BUBBLE_POP, DoubleVariable.ZERO))
				.hit(new DamageProcessor(ctx.damage(), DMG, true, false))
				.hit(new EffectProcessor(LCEffects.CURSE, IntVariable.of("600"), IntVariable.of("0"), false, false))
				.size(DoubleVariable.of("0.5"))
				.build();
	}

	public static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.WEATHER_RAIN,
						DoubleVariable.of("2"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new RingRandomIterator(
						DoubleVariable.ZERO,
						DoubleVariable.of("12"),
						DoubleVariable.ZERO,
						DoubleVariable.of("360"),
						IntVariable.of("20"),
						new CustomProjectileShoot(
								DoubleVariable.of("rand(0.4,0.5)"),
								ctx.proj,
								IntVariable.of("rand(20,30)"),
								false, false,
								Map.of()
						).move(
								OffsetModifier.of("0", "7", "0"),
								SetDirectionModifier.of("0", "-1", "0")
						), null
				)
		));

	}

}
