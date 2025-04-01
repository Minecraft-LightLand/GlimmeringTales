package dev.xkmc.glimmeringtales.content.block.ritual;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.context.UserContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RandomDirModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Set;

public class MatrixParticleHelper {

	public static void complete(Level level, Vec3 center, int col) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		ConfiguredEngine<?> dust = new LoopIterator(
				IntVariable.of("300"),
				new PredicateLogic(BooleanVariable.of("rand(0,2)<1"),
						new DustParticleInstance(
								ColorVariable.Static.of(col),
								DoubleVariable.of("0.5"),
								DoubleVariable.of("0.2"),
								IntVariable.of("40")
						),
						new SimpleParticleInstance(
								ParticleTypes.END_ROD,
								DoubleVariable.of("0.2")
						)
				).move(
						new RandomDirModifier(),
						ForwardOffsetModifier.of("0.05")
				), null
		);
		var build = new BuilderContext(GlimmeringTales.LOGGER, "", Set.of(), false);
		dust.verify(build);
		var ctx = new EngineContext(
				new UserContext(level, player, null, null),
				new LocationContext(center, LocationContext.UP, Vec3.ZERO),
				level.random, Map.of());
		dust.execute(ctx);
	}

}
