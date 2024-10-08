package dev.xkmc.glimmeringtales.content.block.ritual;

import dev.xkmc.glimmeringtales.content.block.altar.SideRitualBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class NatureSideBlockEntity extends SideRitualBlockEntity {

	public NatureSideBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public float getStackScale(float pTick) {
		var core = getLink();
		if (core != null && level != null && level.getBlockEntity(core) instanceof NatureCoreBlockEntity be) {
			return be.progress(pTick);
		}
		return 1;
	}

}
