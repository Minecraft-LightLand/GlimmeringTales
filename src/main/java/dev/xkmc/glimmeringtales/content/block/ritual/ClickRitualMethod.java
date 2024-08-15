package dev.xkmc.glimmeringtales.content.block.ritual;

import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import dev.xkmc.l2modularblock.mult.UseWithoutItemBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ClickRitualMethod implements UseItemOnBlockMethod, UseWithoutItemBlockMethod {

	@Override
	public ItemInteractionResult useItemOn(
			ItemStack stack, BlockState state,
			Level level, BlockPos pos, Player pl,
			InteractionHand hand, BlockHitResult result
	) {
		if (!(level.getBlockEntity(pos) instanceof BaseRitualBlockEntity be))
			return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
		if (!be.getItem().isEmpty())
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		if (stack.isEmpty())
			return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
		be.setItem(stack.split(1));
		return ItemInteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult useWithoutItem(
			BlockState state, Level level, BlockPos pos, Player pl, BlockHitResult result
	) {
		if (!(level.getBlockEntity(pos) instanceof BaseRitualBlockEntity be))
			return InteractionResult.PASS;
		if (be.getItem().isEmpty())
			return InteractionResult.PASS;
		ItemStack stack = be.getItem();
		be.setItem(ItemStack.EMPTY);
		if (pl.getItemInHand(InteractionHand.MAIN_HAND).isEmpty())
			pl.setItemInHand(InteractionHand.MAIN_HAND, stack);
		else pl.getInventory().placeItemBackInInventory(stack);
		return InteractionResult.SUCCESS;
	}

}