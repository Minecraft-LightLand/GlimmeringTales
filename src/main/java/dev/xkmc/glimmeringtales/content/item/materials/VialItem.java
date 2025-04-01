package dev.xkmc.glimmeringtales.content.item.materials;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

public class VialItem extends Item {

	private final List<Supplier<MobEffectInstance>> effects;

	public VialItem(Properties prop, List<Supplier<MobEffectInstance>> effects) {
		super(prop);
		this.effects = effects;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity e) {
		return 10;
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		return ItemUtils.startUsingInstantly(level, player, hand);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		if (!level.isClientSide()) {
			if (entity instanceof Player player) {
				player.getCooldowns().addCooldown(this, 1000);
			}
			for (var e : effects)
				entity.addEffect(e.get());
		}
		return super.finishUsingItem(stack, level, entity);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, context, list, flag);
		for (var e : effects) {
			list.add(IFoodItem.getTooltip(e.get()));
		}
	}
}
