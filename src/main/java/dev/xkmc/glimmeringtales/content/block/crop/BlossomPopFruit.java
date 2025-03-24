package dev.xkmc.glimmeringtales.content.block.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class BlossomPopFruit extends PopFruit{

	public BlossomPopFruit(Properties properties) {
		super(properties);
	}

	@Override
	protected boolean onExplosionAffecting(Entity entity) {
		return super.onExplosionAffecting(entity);
	}

	@Override
	protected void onExplode(Level level, BlockPos pos, int r) {
		super.onExplode(level, pos, r);
	}

}
