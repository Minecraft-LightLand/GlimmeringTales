package dev.xkmc.glimmeringtales.compat.golem;

import dev.xkmc.glimmeringtales.content.entity.hostile.MobSpellData;
import dev.xkmc.glimmeringtales.content.entity.hostile.MobSpellHelper;
import dev.xkmc.mob_weapon_api.api.ai.IWeaponHolder;
import dev.xkmc.mob_weapon_api.api.goals.IMeleeGoal;
import dev.xkmc.mob_weapon_api.example.goal.SmartRangedAttackGoal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class GolemSpellGoal<E extends Mob> extends SmartRangedAttackGoal<E> implements MobSpellHelper {

	private int useTick = 0, immobileTime = 0;
	private long nextAttackTimestamp;

	public GolemSpellGoal(E mob, IMeleeGoal melee) {
		this(mob, mob instanceof IWeaponHolder h ? h : IWeaponHolder.simple(mob), melee);
	}

	public GolemSpellGoal(E mob, IWeaponHolder holder, IMeleeGoal melee) {
		super(mob, holder, melee, 1, 0);
	}

	@Override
	public boolean mayActivate(ItemStack stack) {
		return MobSpellHelper.getSpell(mob, stack) != null && nextAttackTimestamp <= mob.level().getGameTime();
	}

	@Override
	public boolean canUse() {
		return super.canUse() && getSpell() != null && nextAttackTimestamp <= mob.level().getGameTime();
	}

	@Override
	public double range(ItemStack stack) {
		return spellRange(getSpell());
	}

	public double spellRange(@Nullable MobSpellData spell) {
		double range = spell == null ? 8 : spell.mob().idealRange();
		var ins = mob.getAttribute(Attributes.FOLLOW_RANGE);
		if (ins != null) range = Math.min(range, ins.getValue());
		return range;
	}

	@Override
	public void performRangedAttack(LivingEntity target, float v, ItemStack stack, InteractionHand hand) {
	}

	@Override
	public void start() {
		super.start();
		useTick = 0;
	}

	@Override
	public void stop() {
		if (useTick > 0) {
			var spell = getSpell();
			if (spell != null) {
				nextAttackTimestamp = mob.level().getGameTime() + spell.getCooldown(useTick);
			}
		}
		useTick = 0;
		if (mob.isUsingItem()) {
			mob.stopUsingItem();
		}
		super.stop();
	}

	@Override
	public void setUseTick(int tick) {
		useTick = tick;
	}

	@Override
	public void setImmobile(int tick) {
		immobileTime = tick;
	}

	@Override
	public int modifyDelay(int delay) {
		return 0;
	}

	protected int attack(LivingEntity target, boolean withinRange) {
		return attack(mob, target, useTick, withinRange);
	}

	@Nullable
	public MobSpellData getSpell() {
		return MobSpellHelper.getSpell(mob, holder.getWeaponHand());
	}

	public void tick() {
		this.doMelee();
		LivingEntity target = this.mob.getTarget();
		if (immobileTime > 0) {
			immobileTime--;
			mob.getNavigation().stop();
			if (target != null) {
				this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
			}
		} else {
			this.strafing();
		}
		var spell = getSpell();
		if (spell == null || target == null) return;
		double dist = mob.distanceTo(target);
		long timestamp = mob.level().getGameTime();
		if (nextAttackTimestamp <= timestamp) {
			nextAttackTimestamp = timestamp + attack(target, dist <= spellRange(spell) && seeTime > 0);
		}
	}

}
