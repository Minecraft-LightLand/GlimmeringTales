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
		double manaRecover = cost * cost().mana() * 20 / regen() - useTick;
		double focusRecover = cost * cost().focus();
		double factor = cost().researchable() ? 1 : mob.timeFactor();
		return Math.max(20, (int) (Math.max(manaRecover, focusRecover) * factor));
	}

}
