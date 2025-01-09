package dev.xkmc.glimmeringtales.init.data.spell.ocean;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.engine.filter.InvulFrameFilter;
import dev.xkmc.glimmeringtales.content.engine.processor.PassiveHealProcessor;
import dev.xkmc.glimmeringtales.content.engine.render.InflatingRenderData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTDamageTypeGen;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.FilteredProcessor;
import dev.xkmc.l2magic.content.engine.selector.BoxSelector;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffects;

import java.util.List;
import java.util.Map;

public class OceanShelter {

	public static final NatureSpellBuilder BUILDER = GTRegistries.OCEAN
			.build(GlimmeringTales.loc("ocean_shelter")).focusAndCost(2, 10).mob(6, 1)
			.damageCustom(msg -> new DamageType(msg, 0.1f, DamageEffects.DROWNING),
					"%s is drowned by magical bubbles",
					"%s is drowned by %s with magical bubbles",
					GTDamageTypeGen.magic())
			.projectile(OceanShelter::proj)
			.spell(ctx -> new SpellAction(gen(ctx), GTItems.OCEAN_SHELTER.get(),
					2000, SpellCastType.CONTINUOUS, SpellTriggerType.FACING_FRONT))
			.lang("Ocean Shelter").desc(
					"[Continuous] Shoot bubbles that hurt enemies and heal allies",
					"Continuously shoot bubbles forward lasting 5 seconds. To enemies, deals %s and inflicts %s. To allies, %s and gives %s",
					SpellTooltipData.of(EngineRegistry.DAMAGE, EngineRegistry.EFFECT, GTEngine.HEAL, EngineRegistry.EFFECT)
			).graph("OS<->LET");

	private static final DoubleVariable DMG = DoubleVariable.of("4");
	private static final ResourceLocation TEX = GlimmeringTales.loc("textures/spell/bubble.png");

	public static ProjectileConfig proj(NatureSpellBuilder ctx) {
		return ProjectileConfig.builder(SelectionType.NONE)
				.tick(new ListLogic(List.of(
						new ProcessorEngine(
								SelectionType.ENEMY_NO_FAMILY,
								new BoxSelector(DoubleVariable.of("1"), DoubleVariable.of("1"), true),
								List.of(new FilteredProcessor(
										new InvulFrameFilter(IntVariable.of("5")),
										List.of(
												new DamageProcessor(ctx.damage(), DMG, true, true),
												new EffectProcessor(MobEffects.WEAKNESS, IntVariable.of("100"), IntVariable.of("1"), true, true)),
										List.of()
								))
						),
						new ProcessorEngine(
								SelectionType.ALLY_AND_FAMILY,
								new BoxSelector(DoubleVariable.of("1"), DoubleVariable.of("1"), true),
								List.of(
										new PassiveHealProcessor(IntVariable.of("10"), DoubleVariable.of("1")),
										new EffectProcessor(MobEffects.CONDUIT_POWER, IntVariable.of("100"), IntVariable.of("0"), true, true)
								)
						)
				)))
				.size(DoubleVariable.of("0.25"))
				.motion(new SimpleMotion(DoubleVariable.of("0.04"), DoubleVariable.ZERO))
				.renderer(new InflatingRenderData(TEX, 0.3, 4))
				.build();
	}

	public static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.BUBBLE_COLUMN_BUBBLE_POP,
						DoubleVariable.of("2"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new CustomProjectileShoot(
						DoubleVariable.of("rand(0.38,0.42)"),
						ctx.proj,
						IntVariable.of("rand(90,110)"),
						false, true,
						Map.of()
				).move(RotationModifier.of("rand(-3,3)", "rand(-3,3)"))
		));

	}

}
