package dev.xkmc.glimmeringtales.content.block.crop;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import org.jetbrains.annotations.Nullable;

public class OceanPopFruit extends PopFruit implements LiquidBlockContainer {

	public static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(6, 0, 6, 10, 3, 10),
			Block.box(4, 0, 4, 12, 5, 12),
			Block.box(2, 0, 2, 14, 8, 14),
			Block.box(0, 0, 0, 16, 11, 16)
	};

	public OceanPopFruit(Properties properties) {
		super(properties);
	}

	@Override
	protected boolean onExplosionAffecting(Entity entity) {
		return super.onExplosionAffecting(entity);
	}

	@Override
	protected void onExplode(Level level, BlockPos pos, int r) {
		super.onExplode(level, pos, r);
	}

	public void buildState(DataGenContext<Block, ? extends PopFruit> ctx, RegistrateBlockstateProvider pvd) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(AGE);
			String id = ctx.getName() + "_" + age;
			return ConfiguredModel.builder().modelFile(pvd.models()
					.withExistingParent(id, pvd.modLoc("custom/crop/" + id))
					.texture("all", "block/crop/" + id)
					.texture("particle", "block/crop/" + ctx.getName() + "_particle")
					.renderType("translucent")).build();
		});
	}

	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE_BY_AGE[getAge(state)];
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
		return fluid.is(FluidTags.WATER) && fluid.getAmount() == 8 ? super.getStateForPlacement(ctx) : null;
	}

	@Override
	protected BlockState updateShape(
			BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos
	) {
		BlockState blockstate = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
		if (!blockstate.isAir()) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return blockstate;
	}

	@Override
	protected FluidState getFluidState(BlockState p_154537_) {
		return Fluids.WATER.getSource(false);
	}

	@Override
	public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}

}
