package dev.xkmc.glimmeringtales.content.item.rune;

import dev.xkmc.glimmeringtales.content.core.spell.RuneBlock;
import dev.xkmc.l2magic.content.engine.context.SpellContext;
import dev.xkmc.l2magic.content.engine.helper.Orientation;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public record BlockSpellContext(SpellContext ctx, BlockState state, BlockPos pos) {

	@Nullable
	public static BlockSpellContext blockSpellContext(LivingEntity user, int distance, int delay) {
		Level level = user.level();
		Vec3 start = user.getEyePosition();
		Vec3 forward = SpellContext.getForward(user);
		Vec3 end = start.add(forward.scale(distance));
		BlockHitResult bhit = level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, user));
		if (bhit.getType() == HitResult.Type.MISS) return null;
		var pos = bhit.getBlockPos();
		var ori = Orientation.regular().asNormal();
		long seed = 0L;
		if (!level.isClientSide()) {
			seed = ThreadLocalRandom.current().nextLong();
		}
		var ctx = new SpellContext(user, pos.getCenter(), ori, seed, 0, 1, delay);
		return new BlockSpellContext(ctx, level.getBlockState(pos), pos);
	}


	@Nullable
	public static BlockSpellContext entitySpellContext(LivingEntity user, int distance, RuneBlock spell, int delay) {
		Level level = user.level();
		Vec3 start = user.getEyePosition();
		Vec3 forward = SpellContext.getForward(user);
		Vec3 end = start.add(forward.scale(distance));
		AABB box = (new AABB(start, end)).inflate(1.0);
		BlockHitResult bhit = level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE,
				spell.targetLiquid() ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, user));
		var ehit = ProjectileUtil.getEntityHitResult(level, user, start, end, box, e -> true);
		BlockPos pos;
		if (ehit != null && ehit.getType() != HitResult.Type.MISS) {
			pos = ehit.getEntity().blockPosition().below().offset(0, spell.noBlockOffset(), 0);
		} else if (bhit.getType() != HitResult.Type.MISS) {
			pos = bhit.getBlockPos().relative(bhit.getDirection(), spell.noBlockOffset());
		} else if (spell.allowSelf()) {
			pos = user.blockPosition().below();
		} else return null;
		var ori = Orientation.regular().asNormal();
		long seed = 0L;
		if (!level.isClientSide()) {
			seed = ThreadLocalRandom.current().nextLong();
		}
		var ctx = new SpellContext(user, pos.getCenter(), ori, seed, 0, 1, delay);
		return new BlockSpellContext(ctx, level.getBlockState(pos), pos);
	}

}
