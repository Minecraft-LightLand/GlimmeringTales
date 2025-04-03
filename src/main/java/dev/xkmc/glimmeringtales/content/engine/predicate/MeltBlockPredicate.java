package dev.xkmc.glimmeringtales.content.engine.predicate;

import com.mojang.serialization.MapCodec;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ContextPredicate;
import dev.xkmc.l2magic.content.engine.core.PredicateType;
import net.minecraft.core.BlockPos;

public record MeltBlockPredicate() implements ContextPredicate<MeltBlockPredicate> {

	public static final MapCodec<MeltBlockPredicate> CODEC = MapCodec.unit(MeltBlockPredicate::new);

	@Override
	public PredicateType<MeltBlockPredicate> type() {
		return GTEngine.MELT_TEST.get();
	}

	@Override
	public boolean test(EngineContext ctx) {
		var level = ctx.user().level();
		var pos = BlockPos.containing(ctx.loc().pos());
		var state = level.getBlockState(pos);
		return GTRegistries.MELT.get(level.registryAccess(), state.getBlockHolder()) != null;
	}

}