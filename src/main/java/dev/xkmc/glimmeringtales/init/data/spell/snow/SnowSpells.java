package dev.xkmc.glimmeringtales.init.data.spell.snow;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.RuneBlock;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.RandomVariableLogic;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.content.entity.renderer.OrientedRenderData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import java.util.List;
import java.util.Map;

public class SnowSpells {

	public static final NatureSpellBuilder BUILDER = GTRegistries.SNOW
			.build(GlimmeringTales.loc("snow")).focusAndCost(40, 120)
			.damageFreeze()
			.projectile(SnowSpells::proj)
			.block(SnowSpells::gen, GTItems.RUNE_SNOW, RuneBlock::of,
					(b, e) -> b.add(GTTagGen.SNOW, BlockSpell.cost(e)))
			.lang("Snowball Festival").desc(
					"[Block] Splash snowballs everywhere",
					"Create a semisphere of snowballs, dealing %s",
					SpellTooltipData.damage()
			);

	private static final ResourceLocation TEX = GlimmeringTales.loc("textures/spell/snowball.png");
	private static final DoubleVariable DMG = DoubleVariable.of("2");

	public static ProjectileConfig proj(NatureSpellBuilder ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.tick(new SimpleParticleInstance(
						ParticleTypes.SNOWFLAKE,
						DoubleVariable.ZERO
				).move(ForwardOffsetModifier.of("-0.2")))
				.hit(new DamageProcessor(
						ctx.damage(), DMG, true, true
				)).size(DoubleVariable.of("0.25"))
				.motion(SimpleMotion.BREAKING)
				.renderer(new OrientedRenderData(TEX))
				.build();
	}

	public static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		int phi = 12;
		int theta = 12;
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.SNOW_BREAK,
						DoubleVariable.of("10"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new LoopIterator(
						IntVariable.of("" + phi),
						new RandomVariableLogic("r", 1,
								new LoopIterator(
										IntVariable.of("" + theta),
										new CustomProjectileShoot(
												DoubleVariable.of("1"), ctx.proj,
												IntVariable.of("100"),
												false, false,
												Map.of()
										).move(new RotationModifier(
												DoubleVariable.of(360 / theta + "*j+r0*360"),
												DoubleVariable.of(90 / phi + "*(i+0.5)")
										)), "j"
								)
						), "i"
				).move(
						OffsetModifier.of("0", "0.55", "0"),
						SetDirectionModifier.of("1", "0", "0")
				)));

	}

}
