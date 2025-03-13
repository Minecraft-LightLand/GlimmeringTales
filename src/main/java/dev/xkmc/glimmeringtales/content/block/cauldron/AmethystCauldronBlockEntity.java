package dev.xkmc.glimmeringtales.content.block.cauldron;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2core.base.tile.BaseContainerListener;
import dev.xkmc.l2core.base.tile.BaseTank;
import dev.xkmc.l2modularblock.tile_api.BlockContainer;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SerialClass
public class AmethystCauldronBlockEntity extends BaseBlockEntity
		implements TickableBlockEntity, BaseContainerListener, BlockContainer {

	@SerialField
	protected final CauldronItemContainer items = new CauldronItemContainer(9).add(this);
	@SerialField
	protected final BaseTank fluids = new BaseTank(1, 1000).add(this);

	private final IItemHandler itemHandler = new InvWrapper(items);

	public AmethystCauldronBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {

	}

	@Override
	public List<Container> getContainers() {
		return List.of(items);
	}

	@Override
	public void notifyTile() {
		sync();
		setChanged();
	}

	public IItemHandler getItemHandler(@Nullable Direction dir) {
		return itemHandler;
	}

	public IFluidHandler getFluidHandler(@Nullable Direction dir) {
		return fluids;
	}

}
