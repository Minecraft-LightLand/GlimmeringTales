package dev.xkmc.glimmeringtales.content.entity.hostile;

import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.content.core.spell.SpellCost;
import dev.xkmc.glimmeringtales.content.item.wand.ISpellHolder;
import net.minecraft.world.item.ItemStack;

public record MobSpellData(
		ItemStack wand, NatureSpell spell, ISpellHolder holder, MobCastingData mob, SpellCost cost, double regen
) {

	public int getCooldown(int useTick) {
		int maxTick = spell().maxConsumeTick();
		int cost = maxTick > 0 ? Math.min(useTick, maxTick) : Math.max(1, useTick);
		double totalCost = cost * Math.max(cost().mana() * 20 / regen(), cost().focus());
		return Math.max(20, (int) (totalCost * mob.timeFactor()));
	}

}
