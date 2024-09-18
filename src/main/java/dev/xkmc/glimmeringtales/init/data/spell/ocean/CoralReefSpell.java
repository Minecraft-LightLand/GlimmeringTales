package dev.xkmc.glimmeringtales.init.data.spell.ocean;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.RuneBlock;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTDamageTypeGen;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.*;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.CastAtProcessor;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxBallSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;

import java.util.List;
import java.util.Map;

public class CoralReefSpell {

	public static final NatureSpellBuilder BUILDER = GTRegistries.OCEAN
			.build(GlimmeringTales.loc("coral_reef")).focusAndCost(30, 80)
			.damageCustom(e -> new DamageType(e, 0.1f, DamageEffects.DROWNING),
					"%s is drowned by coral magic", "%s is drowned by %s's coral magic",
					GTDamageTypeGen.magic())
			.projectile(CoralReefSpell::proj)
			.block(CoralReefSpell::gen, GTItems.RUNE_CORAL_REEF, RuneBlock::of,
					(b, e) -> b.add(BlockTags.CORALS, BlockSpell.cost(e)))
			.lang("Coral Reef").desc(
					"[Block] Create bubbles to attack nearby entities",
					"For all entities in range, create bubble attack dealing %s",
					SpellTooltipData.damage()
			).graph("O->LSF");

	private static final DoubleVariable DMG = DoubleVariable.of("4");

	private static ProjectileConfig proj(NatureSpellBuilder ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.motion(SimpleMotion.DUST)
				.tick(new SimpleParticleInstance(ParticleTypes.BUBBLE_POP, DoubleVariable.ZERO))
				.hit(new DamageProcessor(ctx.damage(), DMG, true, true))
				.build();
	}

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.BUBBLE_COLUMN_BUBBLE_POP,
						DoubleVariable.of("10"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new LoopIterator(
						IntVariable.of("60"),
						new DustParticleInstance(
								ColorVariable.Static.of(0x1F3FDE),
								DoubleVariable.of("0.5"),
								DoubleVariable.of("0.5"),
								IntVariable.of("16")
						).move(
								SetDirectionModifier.of("1", "0", "0"),
								RotationModifier.of("i*6", "10")
						), "i"
				).move(OffsetModifier.ABOVE),
				new DelayedIterator(IntVariable.of("10"), IntVariable.of("2"),
						new ProcessorEngine(
								SelectionType.ENEMY_NO_FAMILY,
								new ApproxBallSelector(DoubleVariable.of("8")),
								List.of(new CastAtProcessor(
										CastAtProcessor.PosType.CENTER, CastAtProcessor.DirType.UP,
										new CustomProjectileShoot(
												DoubleVariable.of("0.4"),
												ctx.proj,
												IntVariable.of("rand(15,30)"),
												false, false,
												Map.of()
										).move(
												new RandomDirModifier(),
												ForwardOffsetModifier.of("-3")
										)))
						), null)
		));

	}

}