package dev.xkmc.glimmeringtales.content.block.crop;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Creeper;

public class PopFruit extends NaturalPopFruit {

	public PopFruit(Properties properties) {
		super(properties);
	}

	protected boolean onExplosionAffecting(Entity entity) {
		if (entity instanceof Pig pig && pig.level() instanceof ServerLevel sl) {
			Creeper e = pig.convertTo(EntityType.CREEPER, false);
			if (e != null) {
				e.finalizeSpawn(sl, sl.getCurrentDifficultyAt(e.blockPosition()), MobSpawnType.CONVERSION, null);
				net.neoforged.neoforge.event.EventHooks.onLivingConvert(pig, e);
			}
			return false;
		}
		return entity instanceof LivingEntity;
	}

}
