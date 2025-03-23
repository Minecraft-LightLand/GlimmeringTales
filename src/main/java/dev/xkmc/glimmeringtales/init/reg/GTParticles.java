package dev.xkmc.glimmeringtales.init.reg;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.Val;
import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class GTParticles {

	public static final Val<SimpleParticleType> DARK_RAIN;

	static {
		DARK_RAIN = GlimmeringTales.REGISTRATE.particle("dark_rain", () -> new SimpleParticleType(false),
				() -> L2Registrate.ParticleSupplier.spriteSet(() -> BubblePopParticle.Provider::new)
		);
	}

	public static void register() {

	}

}
