package dev.xkmc.glimmeringtales.content.entity.hostile;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;

import javax.annotation.Nullable;

public class SpellCastGoal extends StrafingRangedAttackGoal implements MobSpellHelper {

	private int useTick = 0;

	public SpellCastGoal(Mob mob, MobCastingConfig config) {
		super(mob, config);
	}

	@Override
	protected double getAttackRangeSqr(LivingEntity target) {
		var spell = getSpell();
		if (spell == null) return 256;
		double range = spell.mob().idealRange();
		var ins = mob.getAttribute(Attributes.FOLLOW_RANGE);
		if (ins != null) range = Math.min(range, ins.getValue());
		return range * range;
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
	protected int attack(LivingEntity target, boolean withinRange) {
		return attack(mob, target, useTick, withinRange);
	}

	@Override
	protected boolean canCastSpell() {
		var spell = getSpell();
		return spell != null;
	}

	@Nullable
	public MobSpellData getSpell() {
		return MobSpellHelper.getSpell(mob, InteractionHand.MAIN_HAND);
	}

}
