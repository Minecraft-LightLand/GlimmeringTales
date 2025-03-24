package dev.xkmc.glimmeringtales.content.block.crop;

import dev.xkmc.l2library.content.explosion.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class PopFruitItem extends ItemNameBlockItem {

	public PopFruitItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity le) {
		ItemStack ans = super.finishUsingItem(stack, level, le);
		if (!level.isClientSide) {
			if (getBlock() instanceof PopFruit pop) {
				ExplosionHandler.explode(new BaseExplosion(
						new BaseExplosionContext(level, le.getX(), le.getEyeY() - 0.1, le.getZ(), 1),
						new VanillaExplosionContext(null, null, null, false, Explosion.BlockInteraction.KEEP),
						pop::onExplosionAffecting, ParticleExplosionContext.of(1)
				));
				pop.onExplode(level, BlockPos.containing(le.getX(), le.getEyeY() - 0.1, le.getZ()), 1);
			}
		}
		return ans;
	}

}
