package dev.xkmc.glimmeringtales.init.data.spell.ocean;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.ResearchBonus;
import dev.xkmc.glimmeringtales.content.engine.filter.InvulFrameFilter;
import dev.xkmc.glimmeringtales.content.engine.processor.PassiveHealProcessor;
import dev.xkmc.glimmeringtales.content.engine.render.InflatingRenderData;
import dev.xkmc.glimmeringtales.content.research.core.ResearchDependency;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTDamageTypeGen;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.data.spell.earth.GravelSpells;
import dev.xkmc.glimmeringtales.init.data.spell.snow.SnowStorm;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.FilteredProcessor;
import dev.xkmc.l2magic.content.engine.selector.BoxSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
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

public class IllusoryField {

	public static final NatureSpellBuilder BUILDER = GTRegistries.OCEAN
			.build(GlimmeringTales.loc("illusory_field")).focusAndCost(100, 600)
			.mob(16, 0.7, 0, 20)
			.damageCustom(msg -> new DamageType(msg, 0.1f, DamageEffects.DROWNING),
					"%s is drowned by magical bubbles",
					"%s is drowned by %s with magical bubbles",
					GTDamageTypeGen.magic())
			.projectile(IllusoryField::proj)
			.spell(IllusoryField::gen, GTItems.ILLUSORY_FIELD,
					SpellCastType.INSTANT, SpellTriggerType.TARGET_POS)
			.lang("Illusory Field").desc(
					"[Ranged] Create bubbles that hurt enemies and heal allies",
					"Create bubbles emerging from ground lasting 5 seconds. To enemies, deals %s and inflicts %s. To allies, %s and gives %s",
					SpellTooltipData.of(EngineRegistry.DAMAGE, EngineRegistry.EFFECT, GTEngine.HEAL, EngineRegistry.EFFECT)
			).graph(SnowStorm.SNOW_TORNADO.asParent(ResearchDependency.Type.BRANCH),
					ResearchBonus.small4(27), "O->LET", "LET->FS", "FS->O");

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
												new DamageProcessor(ctx.damage(), DMG, true, false),
												new EffectProcessor(LCEffects.INCARCERATE, IntVariable.of("100"), IntVariable.of("0"), false, false)),
										List.of()
								))
						),
						new ProcessorEngine(
								SelectionType.ALLY_AND_FAMILY,
								new BoxSelector(DoubleVariable.of("1"), DoubleVariable.of("1"), true),
								List.of(
										new PassiveHealProcessor(IntVariable.of("10"), DoubleVariable.of("1")),
										new EffectProcessor(MobEffects.DAMAGE_RESISTANCE, IntVariable.of("100"), IntVariable.of("1"), false, false)
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
				new DelayedIterator(
						IntVariable.of("40"),
						IntVariable.of("1"),
						new RingRandomIterator(
								DoubleVariable.ZERO,
								DoubleVariable.of("7"),
								IntVariable.of("6"),
								new CustomProjectileShoot(
										DoubleVariable.of("rand(0.04,0.09)"),
										ctx.proj,
										IntVariable.of("rand(90,110)"),
										true, true,
										Map.of()
								).move(SetDirectionModifier.of("rand(-3,3)", "100", "rand(-3,3)"))
						)
				)
		)).mobCastDelay(new RingIterator(DoubleVariable.of("7"), IntVariable.of("256"), false,
				new PredicateLogic(BooleanVariable.of("rand()>0.5"),
						new DustParticleInstance(ColorVariable.Static.of(0xF3D7FF),
								DoubleVariable.of("0.5"), DoubleVariable.ZERO, IntVariable.of("20")),
						new DustParticleInstance(ColorVariable.Static.of(0xBCECFF),
								DoubleVariable.of("0.5"), DoubleVariable.ZERO, IntVariable.of("20"))
				)
		).move(OffsetModifier.ABOVE));

	}

}
