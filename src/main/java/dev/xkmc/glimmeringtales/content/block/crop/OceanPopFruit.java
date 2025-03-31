package dev.xkmc.glimmeringtales.content.block.crop;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OceanPopFruit extends NaturalPopFruit implements LiquidBlockContainer {

	private static int getLight(BlockState state) {
		return state.getValue(AGE) * 3;
	}

	public static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(6, 0, 6, 10, 3, 10),
			Block.box(4, 0, 4, 12, 5, 12),
			Block.box(2, 0, 2, 14, 8, 14),
			Block.box(0, 0, 0, 16, 11, 16)
	};

	public OceanPopFruit(Properties properties) {
		super(properties.lightLevel(OceanPopFruit::getLight));
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(BlockTags.DIRT) || state.is(Blocks.GRAVEL) || state.is(Blocks.SAND) || super.mayPlaceOn(state, level, pos);
	}

	@Override
	protected boolean canSurviveLight(LevelReader level, BlockPos pos) {
		return true;
	}

	@Override
	public List<Block> plantableOn() {
		return List.of(Blocks.DIRT, Blocks.GRAVEL, Blocks.SAND);
	}

	public MutableComponent getBiomeDesc() {
		return GTLang.BIOME_OCEAN.get();
	}

	@Override
	protected boolean onExplosionAffecting(Entity entity) {
		if (entity instanceof LivingEntity le) {
			le.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 0));
		}
		return false;
	}

	@Override
	protected void onExplode(Level level, BlockPos pos, int r) {
		super.onExplode(level, pos, r);
	}

	public void buildState(DataGenContext<Block, ? extends AbstractPopFruit> ctx, RegistrateBlockstateProvider pvd) {
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
