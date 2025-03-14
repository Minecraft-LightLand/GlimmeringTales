package dev.xkmc.glimmeringtales.content.block.altar;

import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class SideRitualBlockEntity extends BaseRitualBlockEntity implements TickableBlockEntity {

	@SerialField
	private BlockPos core = null;

	public SideRitualBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	private void setLink(@Nullable BlockPos pos) {
		core = pos;
		sync();
		setChanged();
	}

	@Override
	public void tick() {
		if (level == null || level.isClientSide()) return;
		if (level.getGameTime() % PerformanceConstants.CHECK_INTERVAL != 0) return;
		if (core == null) return;
		if (level.getBlockEntity(core) instanceof CoreRitualBlockEntity be) {
			if (be.isLinked(this, getBlockPos())) {
				return;
			}
		}
		setLink(null);
	}

	public boolean isLinked(CoreRitualBlockEntity be, BlockPos pos) {
		return core != null && core.equals(pos);
	}

	@Nullable
	public BlockPos getLink() {
		return core;
	}

	public void establishLink(CoreRitualBlockEntity be, BlockPos pos) {
		if (level == null || level.isClientSide()) return;
		if (core != null) {
			if (level.getBlockEntity(core) instanceof CoreRitualBlockEntity old) {
				old.removeLink(this, pos);
			}
		}
		setLink(pos);
	}

	public void removeLink(CoreRitualBlockEntity be, BlockPos pos) {
		if (isLinked(be, pos)) setLink(null);
	}

	@Override
	public boolean locked() {
		if (level == null || core == null) return false;
		if (level.getBlockEntity(core) instanceof CoreRitualBlockEntity be)
			return be.locked();
		return false;
	}

	@Override
	public void onReplaced() {
		if (level == null || level.isClientSide() || core == null) return;
		if (level.getBlockEntity(core) instanceof CoreRitualBlockEntity be) {
			be.removeLink(this, getBlockPos());
		}
	}

	@Override
	public void onPlaced() {
		if (level == null || level.isClientSide()) return;
		core = null;
		int r = PerformanceConstants.range();
		BlockPos self = getBlockPos();
		BlockPos ansPos = null;
		CoreRitualBlockEntity ansBE = null;
		int sqr = Integer.MAX_VALUE;
		for (int x = -r; x <= r; x++) {
			for (int y = -r; y <= r; y++) {
				for (int z = -r; z <= r; z++) {
					BlockPos pos = self.offset(x, y, z);
					if (level.getBlockEntity(pos) instanceof CoreRitualBlockEntity be) {
						if (be.locked()) continue;
						int isqr = x * x + y * y + z * z;
						if (isqr < sqr) {
							sqr = isqr;
							ansPos = pos;
							ansBE = be;
						}
					}
				}
			}
		}
		if (ansPos != null && ansBE != null) {
			setLink(ansPos);
			ansBE.establishLink(this, self);
		}
	}

}
