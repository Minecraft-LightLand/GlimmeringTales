package dev.xkmc.glimmeringtales.content.block.crop;

import dev.xkmc.glimmeringtales.content.item.materials.IFoodItem;
import dev.xkmc.glimmeringtales.init.data.GTConfigs;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.l2library.content.explosion.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class PopFruitItem extends ItemNameBlockItem {

	public PopFruitItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity le) {
		ItemStack ans = super.finishUsingItem(stack, level, le);
		if (level.isClientSide) return ans;
		double rate = GTConfigs.SERVER.popFruitExplosionChanceOnEaten.getAsDouble();
		if (!(getBlock() instanceof PopFruit pop) || le.getRandom().nextDouble() > rate) return ans;
		ExplosionHandler.explode(new BaseExplosion(
				new BaseExplosionContext(level, le.getX(), le.getEyeY() - 0.1, le.getZ(), 1),
				new VanillaExplosionContext(null, null, null, false, Explosion.BlockInteraction.KEEP),
				pop::onExplosionAffecting, ParticleExplosionContext.of(1)
		));
		pop.onExplode(level, BlockPos.containing(le.getX(), le.getEyeY() - 0.1, le.getZ()), 1);
		return ans;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		IFoodItem.getFoodEffects(stack, list);
		double rate = GTConfigs.SERVER.popFruitExplosionChanceOnEaten.getAsDouble();
		int perc = (int) Math.round(rate * 100);
		list.add(GTLang.CHANCE_EXPLODE.get(perc).withStyle(ChatFormatting.GRAY));
	}

}
