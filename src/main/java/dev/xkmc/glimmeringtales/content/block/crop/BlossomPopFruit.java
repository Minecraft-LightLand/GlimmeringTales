package dev.xkmc.glimmeringtales.content.block.crop;

import dev.xkmc.glimmeringtales.init.data.GTLang;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class BlossomPopFruit extends NaturalPopFruit {

	public BlossomPopFruit(Properties properties) {
		super(properties);
	}

	@Override
	protected boolean onExplosionAffecting(Entity entity) {
		if (entity instanceof LivingEntity le) {
			if (le.isInvertedHealAndHarm())
				return true;
			le.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
		}
		return false;
	}

	@Override
	protected void onExplode(Level level, BlockPos pos, int r) {
		super.onExplode(level, pos, r);
	}

	public MutableComponent getBiomeDesc() {
		return GTLang.BIOME_FLOWERY.get();
	}

}
