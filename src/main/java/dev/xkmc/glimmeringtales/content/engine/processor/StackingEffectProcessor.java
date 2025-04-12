package dev.xkmc.glimmeringtales.content.engine.processor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2complements.content.effect.StackingEffect;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.processor.SimpleServerProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectedEntities;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public record StackingEffectProcessor(
		Holder<MobEffect> eff,
		IntVariable duration,
		IntVariable max
) implements SimpleServerProcessor<StackingEffectProcessor> {

	public static final MapCodec<StackingEffectProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(e -> e.eff),
			IntVariable.codec("duration", e -> e.duration),
			IntVariable.codec("max", e -> e.max)
	).apply(i, StackingEffectProcessor::new));

	@Override
	public ProcessorType<StackingEffectProcessor> type() {
		return GTEngine.EP_STACK.get();
	}

	@Override
	public void process(SelectedEntities le, EngineContext ctx) {
		int dur = duration.eval(ctx);
		int amp = max.eval(ctx);
		for (var e : le.living()) {
			StackingEffect.addTo(eff, e, dur, amp, ctx.user().user());
		}

	}

}
