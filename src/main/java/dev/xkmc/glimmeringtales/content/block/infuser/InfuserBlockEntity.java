package dev.xkmc.glimmeringtales.content.block.infuser;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2core.base.tile.BaseContainerListener;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class InfuserBlockEntity extends BaseBlockEntity
		implements TickableBlockEntity, BaseContainerListener {

	@SerialField
	private final InfuserItemContainer items = new InfuserItemContainer(2).setMax(1).add(this);

	private final IItemHandler handler;

	public InfuserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		handler = new InvWrapper(items);
	}

	@Override
	public void tick() {

	}

	@Override
	public void notifyTile() {
		sync();
		setChanged();
	}

	public IItemHandler getCapability(@Nullable Direction dir) {
		return handler;
	}

}
