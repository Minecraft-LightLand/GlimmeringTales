package dev.xkmc.glimmeringtales.content.block.infuser;

import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class InfuserBlock implements ShapeBlockMethod, UseItemOnBlockMethod {

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player pl, InteractionHand hand, BlockHitResult result) {
		if (level.getBlockEntity(pos) instanceof InfuserBlockEntity be) {
			if (stack.isEmpty()) {
				if (!be.items.getItem(1).isEmpty()) {
					pl.setItemInHand(InteractionHand.MAIN_HAND, be.items.getItem(1));
					be.items.setItem(1, ItemStack.EMPTY);
					return ItemInteractionResult.SUCCESS;
				}
				if (!be.items.getItem(0).isEmpty()) {
					pl.setItemInHand(InteractionHand.MAIN_HAND, be.items.getItem(0));
					be.items.setItem(0, ItemStack.EMPTY);
					return ItemInteractionResult.SUCCESS;
				}
				return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
			}
			if (!level.isClientSide()) {
				int slot = stack.is(GTTagGen.CRYSTAL) ? 0 : 1;
				ItemStack prev = be.items.getItem(slot);
				be.items.setItem(slot, stack.split(1));
				if (!prev.isEmpty())
					pl.getInventory().placeItemBackInInventory(prev);
				return ItemInteractionResult.SUCCESS;
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

}
