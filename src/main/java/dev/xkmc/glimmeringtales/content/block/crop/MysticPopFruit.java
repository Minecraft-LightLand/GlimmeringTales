package dev.xkmc.glimmeringtales.content.block.crop;

import dev.xkmc.glimmeringtales.init.reg.GTEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;

public class MysticPopFruit extends AbstractPopFruit {

	public MysticPopFruit(Properties properties) {
		super(properties);
	}

	protected boolean onExplosionAffecting(Entity entity) {
		if (entity instanceof LivingEntity le) {
			le.addEffect(new MobEffectInstance(GTEffects.MANA_RECOVERY, 300, 0));
		}
		return entity instanceof Enemy;
	}


}
