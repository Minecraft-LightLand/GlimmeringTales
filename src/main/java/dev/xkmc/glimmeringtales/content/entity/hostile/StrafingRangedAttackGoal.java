package dev.xkmc.glimmeringtales.content.entity.hostile;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;

public abstract class StrafingRangedAttackGoal extends Goal {
	protected final Mob mob;
	protected final MobCastingConfig config;

	@Nullable
	private LivingEntity target;
	protected long nextAttackTimestamp = 0;
	protected int seeTime;

	protected int immobileTime;

	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public StrafingRangedAttackGoal(Mob mob, MobCastingConfig config) {
		this.mob = mob;
		this.config = config;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		LivingEntity target = mob.getTarget();
		if (target != null && target.isAlive()) {
			this.target = target;
			return canCastSpell();
		} else {
			return false;
		}
	}

	@Override
	public boolean canContinueToUse() {
		return canUse() || target != null && target.isAlive() && !mob.getNavigation().isDone();
	}

	@Override
	public void start() {
		mob.setAggressive(true);
	}

	@Override
	public void stop() {
		mob.setAggressive(false);
		target = null;
		seeTime = 0;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		if (target == null) return;
		double sqr = getAttackRangeSqr(target);

		double distSqr = mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
		boolean canSee = mob.getSensing().hasLineOfSight(target);
		if (canSee) {
			seeTime++;
		} else {
			seeTime = 0;
		}

		if (immobileTime > 0) {
			immobileTime--;
			strafingTime = -1;
			mob.getNavigation().stop();
			this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
		} else {
			if (distSqr <= sqr && seeTime >= config.stopMovingSeeTime()) {
				mob.getNavigation().stop();
				this.strafingTime++;
			} else {
				mob.getNavigation().moveTo(target, config.speed());
				this.strafingTime = -1;
			}

			if (strafingTime >= config.strafRotateTime()) {
				if (mob.getRandom().nextFloat() < config.switchRotationChance()) {
					strafingClockwise = !strafingClockwise;
				}
				if (mob.getRandom().nextFloat() < config.switchDirectionChance()) {
					strafingBackwards = !strafingBackwards;
				}
				strafingTime = 0;
			}

			if (strafingTime > -1) {
				if (distSqr > sqr * config.stopBackoffRange()) {
					strafingBackwards = false;
				} else if (distSqr < sqr * config.startBackoffRange()) {
					strafingBackwards = true;
				}
				mob.getMoveControl().strafe(strafingBackwards ? -1f : 0.5F, strafingClockwise ? 0.5F : -0.5F);
				if (mob.onGround() && mob.level().getBlockState(mob.blockPosition()).isSolid())
					mob.getJumpControl().jump();
				if (mob.getControlledVehicle() instanceof Mob veh) {
					veh.lookAt(target, 30.0F, 30.0F);
				}
				this.mob.lookAt(target, 30.0F, 30.0F);
			} else {
				this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
			}
		}

		long timestamp = mob.level().getGameTime();

		if (nextAttackTimestamp <= timestamp) {
			nextAttackTimestamp = timestamp + attack(target, distSqr <= sqr && canSee);
		}
	}

	protected abstract double getAttackRangeSqr(LivingEntity target);

	protected abstract int attack(LivingEntity target, boolean withinRange);

	protected abstract boolean canCastSpell();


}
