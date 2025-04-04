package dev.xkmc.glimmeringtales.content.entity.hostile;

import dev.xkmc.glimmeringtales.content.item.rune.BaseRuneItem;
import dev.xkmc.glimmeringtales.content.item.wand.RuneWandItem;
import dev.xkmc.glimmeringtales.content.item.wand.SpellCastContext;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface MobSpellHelper {

	@Nullable
	MobSpellData getSpell();

	void setUseTick(int tick);

	void setImmobile(int tick);

	default int modifyDelay(int delay) {
		return delay;
	}

	default int attack(Mob mob, LivingEntity target, int useTick, boolean withinRange) {
		var mobData = getSpell();
		if (mobData == null) return 20;
		if (!withinRange) {
			if (useTick <= 0) return 0;
			mob.stopUsingItem();
			setUseTick(0);
			return mobData.getCooldown(useTick);
		}
		var action = mobData.spell().spell().value();
		int delay = modifyDelay(mobData.mob().standardDelay());
		var ctx = SpellCastContext.of(mob.level(), mob, delay, mobData.wand());
		int cost;
		if (action.castType() == SpellCastType.INSTANT) {
			mobData.holder().cast(ctx, 0, false);
			cost = 1;
			setImmobile(delay);
		} else {
			if (useTick == 0) {
				mob.startUsingItem(InteractionHand.MAIN_HAND);
			}
			if (useTick < mobData.mob().maxUseTick() + delay) {
				mobData.holder().cast(ctx, useTick, action.castType() == SpellCastType.CHARGE);
				setUseTick(useTick + 1);
				return 0;
			} else {
				if (action.castType() == SpellCastType.CHARGE) {
					mobData.holder().cast(ctx, useTick, false);
				}
				cost = useTick - delay;
				mob.stopUsingItem();
				setUseTick(0);
			}
		}
		return mobData.getCooldown(cost);
	}

	@Nullable
	static MobSpellData getSpell(Mob mob, InteractionHand hand) {
		return getSpell(mob, mob.getItemInHand(hand));
	}

	@Nullable
	static MobSpellData getSpell(LivingEntity mob, ItemStack wand) {
		if (!wand.is(GTItems.WAND)) return null;
		ItemStack core = RuneWandItem.getCore(wand);
		if (core.getItem() instanceof BaseRuneItem item) {
			var holder = item.getSpell(core, mob.level());
			var info = item.getSpellInfo(mob.level().registryAccess());
			if (holder != null && info.spell() != null) {
				var ns = info.spell().value();
				if (ns.mob() != null) {
					var ins = mob.getAttribute(GTRegistries.MANA_REGEN);
					double regen = ins == null ?
							GTRegistries.MANA_REGEN.get().getDefaultValue() :
							ins.getValue();
					return new MobSpellData(wand, ns, holder, ns.mob(), info.getCost(mob, wand), regen);
				}
			}
		}
		return null;
	}

}
