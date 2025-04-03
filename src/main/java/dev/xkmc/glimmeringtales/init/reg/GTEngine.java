package dev.xkmc.glimmeringtales.init.reg;

import dev.xkmc.glimmeringtales.content.engine.filter.InvulFrameFilter;
import dev.xkmc.glimmeringtales.content.engine.instance.*;
import dev.xkmc.glimmeringtales.content.engine.particle.FarParticleRenderData;
import dev.xkmc.glimmeringtales.content.engine.predicate.MeltBlockPredicate;
import dev.xkmc.glimmeringtales.content.engine.processor.PassiveHealProcessor;
import dev.xkmc.glimmeringtales.content.engine.processor.ProcreationProcessor;
import dev.xkmc.glimmeringtales.content.engine.processor.StackingEffectProcessor;
import dev.xkmc.glimmeringtales.content.engine.render.*;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.core.FilterType;
import dev.xkmc.l2magic.content.engine.core.PredicateType;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderType;
import dev.xkmc.l2magic.content.particle.engine.ParticleRenderType;
import dev.xkmc.l2magic.init.registrate.EngineReg;

public class GTEngine {

	private static final EngineReg REG = new EngineReg(GlimmeringTales.REGISTRATE);


	public static final Val<EngineType<EffectCloudInstance>> EFFECT_CLOUD = REG.reg("effect_cloud", () -> EffectCloudInstance.CODEC);
	public static final Val<EngineType<MeltBlockInstance>> MELT_BLOCK = REG.reg("melt_block", () -> MeltBlockInstance.CODEC);
	public static final Val<EngineType<LightningInstance>> THUNDER = REG.reg("thunder", () -> LightningInstance.CODEC);
	public static final Val<EngineType<RemoveLiquidInstance>> SPONGE = REG.reg("remove_liquid", () -> RemoveLiquidInstance.CODEC);
	public static final Val<EngineType<GTKnockBlock>> KNOCK = REG.reg("knock_block", () -> GTKnockBlock.CODEC);

	public static final Val<PredicateType<MeltBlockPredicate>> MELT_TEST = REG.reg("melt_test", () -> MeltBlockPredicate.CODEC);

	public static final Val<ProcessorType<StackingEffectProcessor>> EP_STACK = REG.reg("stacking", () -> StackingEffectProcessor.CODEC);
	public static final Val<ProcessorType<ProcreationProcessor>> PROCREATION = REG.reg("procreation", () -> ProcreationProcessor.CODEC);
	public static final Val<ProcessorType<PassiveHealProcessor>> HEAL = REG.reg("heal_interval", () -> PassiveHealProcessor.CODEC);

	public static final Val<FilterType<InvulFrameFilter>> INVUL = REG.reg("invulnerability_frame", () -> InvulFrameFilter.CODEC);

	public static final Val<ProjectileRenderType<VerticalRenderData>> PR_VERTICAL = REG.reg("vertical", () -> VerticalRenderData.CODEC);
	public static final Val<ProjectileRenderType<OrientedCrossRenderData>> PR_CROSS = REG.reg("oriented_cross", () -> OrientedCrossRenderData.CODEC);
	public static final Val<ProjectileRenderType<InflatingRenderData>> PR_BUBBLE = REG.reg("inflating", () -> InflatingRenderData.CODEC);
	public static final Val<ProjectileRenderType<AnimatedCrossRenderData>> PR_CHARGE = REG.reg("animated_cross", () -> AnimatedCrossRenderData.CODEC);
	public static final Val<ProjectileRenderType<FakeBlockRenderData>> PR_BLOCK = REG.reg("block", () -> FakeBlockRenderData.CODEC);

	public static final Val<ParticleRenderType<FarParticleRenderData>> PR_FAR = REG.reg("far", () -> FarParticleRenderData.CODEC);

	public static void register() {

	}

}
