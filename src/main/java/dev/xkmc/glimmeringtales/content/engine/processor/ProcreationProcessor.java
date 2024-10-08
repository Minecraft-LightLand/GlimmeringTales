package dev.xkmc.glimmeringtales.content.engine.processor;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.processor.SimpleServerProcessor;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;

import static net.minecraft.world.entity.AgeableMob.getSpeedUpSecondsWhenFeeding;

public record ProcreationProcessor(
		IntVariable num
) implements SimpleServerProcessor<ProcreationProcessor> {

	public static final MapCodec<ProcreationProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			IntVariable.codec("num", e -> e.num)
	).apply(i, ProcreationProcessor::new));

	@Override
	public ProcessorType<ProcreationProcessor> type() {
		return GTEngine.PROCREATION.get();
	}

	@Override
	public void process(Collection<LivingEntity> le, EngineContext ctx) {
		if (!(ctx.user().level() instanceof ServerLevel sl)) return;
		for (var e : le) {
			if (!(e instanceof Animal animal)) continue;
			Player player = ctx.user().user() instanceof Player pl ? pl : null;
			if (!animal.isBaby()) {
				animal.setInLove(player);
			} else {
				animal.ageUp(getSpeedUpSecondsWhenFeeding(-animal.getAge()), true);
			}

		}
	}


}
