package dev.xkmc.glimmeringtales.content.block.misc;

import dev.xkmc.l2library.init.FlagMarker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class StruckLogBlock extends RotatedPillarBlock {

	public StruckLogBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter) {
		level.removeBlock(pos, false);
		LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
		assert bolt != null;
		bolt.addTag(FlagMarker.LIGHTNING);
		bolt.moveTo(Vec3.atBottomCenterOf(pos));
		bolt.setCause(igniter instanceof ServerPlayer sp ? sp : null);
		level.addFreshEntity(bolt);
	}


	@Override
	protected ItemInteractionResult useItemOn(
			ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
	) {
		if (!stack.is(Items.FLINT_AND_STEEL) && !stack.is(Items.FIRE_CHARGE)) {
			return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
		} else {
			onCaughtFire(state, level, pos, hitResult.getDirection(), player);
			Item item = stack.getItem();
			if (stack.is(Items.FLINT_AND_STEEL)) {
				stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
			} else {
				stack.consume(1, player);
			}

			player.awardStat(Stats.ITEM_USED.get(item));
			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	@Override
	protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
		if (!level.isClientSide) {
			BlockPos blockpos = hit.getBlockPos();
			Entity entity = projectile.getOwner();
			if (projectile.isOnFire() && projectile.mayInteract(level, blockpos)) {
				onCaughtFire(state, level, blockpos, null, entity instanceof LivingEntity e ? e : null);
			}
		}
	}

}