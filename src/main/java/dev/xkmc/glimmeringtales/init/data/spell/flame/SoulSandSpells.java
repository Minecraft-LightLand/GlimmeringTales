package dev.xkmc.glimmeringtales.init.data.spell.flame;

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
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.selector.BoxSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;

import java.util.List;

public class SoulSandSpells {

	public static final NatureSpellBuilder BUILDER = GTRegistries.FLAME
			.build(GlimmeringTales.loc("soul_sand")).focusAndCost(40, 120)
			.damageCustom(e -> new DamageType(e, 0, DamageEffects.BURNING),
					"%s is blazed by ghosts", "%s is blazed by ghosts summoned by %s",
					GTDamageTypeGen.magic(DamageTypeTags.IS_FIRE))
			.block(SoulSandSpells::gen, GTItems.RUNE_SOUL_SAND, RuneBlock::of,
					(b, e) -> b.add(BlockTags.SOUL_FIRE_BASE_BLOCKS, BlockSpell.of(e)))
			.lang("Ghost Spark").desc(
					"[Block] burn enemies in a small area",
					"Create ghost sparks and inflict %s",
					SpellTooltipData.damage()
			).graph(NetherrackSpells.BUILDER);

	private static final DoubleVariable DMG = DoubleVariable.of("4");

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.SOUL_ESCAPE.value(),
						DoubleVariable.of("5"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new SoundInstance(
						SoundEvents.FIRECHARGE_USE,
						DoubleVariable.of("1"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new ProcessorEngine(
						SelectionType.ENEMY_NO_FAMILY,
						new BoxSelector(DoubleVariable.of("2"), DoubleVariable.of("2"), false),
						List.of(new DamageProcessor(ctx.damage(), DMG, true, false))
				),
				new DelayedIterator(
						IntVariable.of("10"),
						IntVariable.of("1"),
						new SimpleParticleInstance(
								ParticleTypes.SOUL_FIRE_FLAME,
								DoubleVariable.of("0.3")
						).move(
								SetDirectionModifier.of("rand(-0.2,0.2)", "1", "rand(-0.2,0.2)"),
								OffsetModifier.of("rand(-0.4,0.4)", "0", "rand(-0.4,0.4)")
						),
						null
				)
		)).move(OffsetModifier.of("0", "0.55", "0"));
	}


}
