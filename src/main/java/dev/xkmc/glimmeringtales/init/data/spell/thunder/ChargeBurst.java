package dev.xkmc.glimmeringtales.init.data.spell.thunder;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.engine.filter.InvulFrameFilter;
import dev.xkmc.glimmeringtales.content.engine.instance.LightningInstance;
import dev.xkmc.glimmeringtales.content.engine.render.AnimatedCrossRenderData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
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
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;

import java.util.List;
import java.util.Map;

public class ChargeBurst {

	public static final NatureSpellBuilder BUILDER = GTRegistries.THUNDER
			.build(GlimmeringTales.loc("charge_burst")).focusAndCost(120, 800).mob(16, 1)
			.damageCustom(msg -> new DamageType(msg, 0.1f),
					"%s is electrocuted by charge burst",
					"%s is electrocuted by %s with charge burst",
					DamageTypeTags.IS_LIGHTNING)
			.projectile(ChargeBurst::proj)
			.spell(ctx -> new SpellAction(gen(ctx), GTItems.CHARGE_BURST.get(), 2002,
					SpellCastType.INSTANT, SpellTriggerType.TARGET_POS)
			).lang("Charge Burst").desc(
					"[Ranged] Create a lightning strike and charge spikes around it",
					"Create a lightning strike on target position, inflicting %s multiple times, then create charge spikes on the ground around it, inflicting %s",
					SpellTooltipData.of(GTEngine.THUNDER, EngineRegistry.DAMAGE)
			).graph("EOFST|");

	private static final ResourceLocation TEX = GlimmeringTales.loc("textures/spell/charge.png");
	private static final DoubleVariable STRIKE = DoubleVariable.of("5");
	private static final DoubleVariable CHARGE = DoubleVariable.of("4");

	private static ProjectileConfig proj(NatureSpellBuilder ctx) {
		return ProjectileConfig.builder(SelectionType.NONE)
				.tick(new ProcessorEngine(
						SelectionType.ENEMY_NO_FAMILY,
						new BoxSelector(DoubleVariable.of("1"), DoubleVariable.of("1"), false),
						List.of(new FilteredProcessor(
								new InvulFrameFilter(IntVariable.of("4")),
								List.of(new DamageProcessor(ctx.damage(), CHARGE, true, false)),
								List.of()
						))
				)).renderer(new AnimatedCrossRenderData(TEX, 2, 8))
				.build();
	}

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.TRIDENT_THUNDER.value(),
						DoubleVariable.of("1"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new LightningInstance(STRIKE),
				new CustomProjectileShoot(DoubleVariable.ZERO, ctx.proj, IntVariable.of("12"), true, true, Map.of())
						.move(OffsetModifier.of("0", "-0.49", "0"))
						.circular(DoubleVariable.of("5"), DoubleVariable.of("2"), false, null,
								BlockTestCondition.Type.BLOCKS_MOTION.get().invert(),
								BlockTestCondition.Type.BLOCKS_MOTION.get().move(OffsetModifier.BELOW)
						)
		));

	}


}
