package dev.xkmc.glimmeringtales.content.item.wand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record SpellCastContext(Level level, LivingEntity user, ItemStack wand, int delay, boolean simulate) {

	public static SpellCastContext of(Level level, LivingEntity player, int delay, ItemStack stack) {
		return new SpellCastContext(level, player, stack, delay, false);
	}

	public static SpellCastContext simulate(Level level, LivingEntity player, ItemStack stack) {
		return new SpellCastContext(level, player, stack, 0, true);
	}

}
