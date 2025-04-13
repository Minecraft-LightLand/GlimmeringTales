package dev.xkmc.glimmeringtales.content.engine.processor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.processor.SimpleServerProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectedEntities;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;

public record PassiveHealProcessor(
		IntVariable interval,
		DoubleVariable heal
) implements SimpleServerProcessor<PassiveHealProcessor> {

	public static final MapCodec<PassiveHealProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			IntVariable.codec("interval", e -> e.interval),
			DoubleVariable.codec("heal", e -> e.heal)
	).apply(i, PassiveHealProcessor::new));

	@Override
	public ProcessorType<PassiveHealProcessor> type() {
		return GTEngine.HEAL.get();
	}

	@Override
	public void process(SelectedEntities le, EngineContext ctx) {
		int val = interval.eval(ctx);
		float amount = (float) heal.eval(ctx);
		for (var e : le.living()) {
			if (e.tickCount % val == 0)
				e.heal(amount);
		}
	}

}