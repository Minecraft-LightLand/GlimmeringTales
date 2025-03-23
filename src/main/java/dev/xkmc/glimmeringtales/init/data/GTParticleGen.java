package dev.xkmc.glimmeringtales.init.data;

import dev.xkmc.glimmeringtales.init.reg.GTParticles;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

public class GTParticleGen extends ParticleDescriptionProvider {

	public GTParticleGen(PackOutput output, ExistingFileHelper fileHelper) {
		super(output, fileHelper);
	}

	@Override
	protected void addDescriptions() {
		spriteSet(GTParticles.DARK_RAIN.get(), GTParticles.DARK_RAIN.id(), 5, false);
	}

}
