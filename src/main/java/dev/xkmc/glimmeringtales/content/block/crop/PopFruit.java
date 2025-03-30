package dev.xkmc.glimmeringtales.content.block.crop;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.l2library.content.explosion.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

public class PopFruit extends CropBlock {

	private static boolean sensitiveTo(Entity e) {
		if (e instanceof LivingEntity) {
			return !(e instanceof Animal);
		}
		if (e instanceof Projectile)
			return true;
		if (e instanceof FallingBlockEntity)
			return true;
		return e instanceof VehicleEntity;
	}

	public static final MapCodec<PopFruit> CODEC = simpleCodec(PopFruit::new);
	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
	public static final int MAX_AGE = 3;

	public static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(7, 0, 7, 9, 1, 9),
			Block.box(6, 0, 6, 10, 2, 10),
			Block.box(5, 0, 5, 11, 4, 11),
			Block.box(4, 0, 4, 12, 6, 12)
	};

	public PopFruit(Properties properties) {
		super(properties);
	}

	public IntegerProperty getAgeProperty() {
		return AGE;
	}

	public int getMaxAge() {
		return MAX_AGE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(BlockTags.DIRT) || super.mayPlaceOn(state, level, pos);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return super.canSurvive(state, level, pos);
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!level.isAreaLoaded(pos, 1)) return;
		int i = getAge(state);
		if (i >= getMaxAge()) return;
		float f = getGrowthSpeed(state, level, pos);
		if (net.neoforged.neoforge.common.CommonHooks.canCropGrow(level, pos, state, random.nextInt((int) (25F / f) + 1) == 0)) {
			level.setBlock(pos, getStateForAge(i + 1), 2);
			net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(level, pos, state);
		}
	}

	protected static float getGrowthSpeed(BlockState state, BlockGetter level, BlockPos pos) {
		return 1;
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (level.isClientSide()) return;
		if (!sensitiveTo(entity)) return;
		var aabb = state.getShape(level, pos).bounds().move(pos);
		if (aabb.intersects(entity.getBoundingBox())) {
			level.removeBlock(pos, false);
			int r = state.getValue(AGE) - 1;
			if (r <= 0) return;
			ExplosionHandler.explode(new BaseExplosion(
					new BaseExplosionContext(level, pos.getX() + 0.5, aabb.maxY, pos.getZ() + 0.5, r),
					new VanillaExplosionContext(null, null, null, false, Explosion.BlockInteraction.KEEP),
					this::onExplosionAffecting, ParticleExplosionContext.of(r)
			));
			onExplode(level, pos, r);
		}
	}

	protected boolean onExplosionAffecting(Entity entity) {
		if (entity instanceof Pig pig && pig.level() instanceof ServerLevel sl) {
			Creeper e = pig.convertTo(EntityType.CREEPER, false);
			if (e != null) {
				e.finalizeSpawn(sl, sl.getCurrentDifficultyAt(e.blockPosition()), MobSpawnType.CONVERSION, null);
				net.neoforged.neoforge.event.EventHooks.onLivingConvert(pig, e);
			}
			return false;
		}
		return entity instanceof LivingEntity;
	}

	protected void onExplode(Level level, BlockPos pos, int r) {
	}

	public MapCodec<PopFruit> codec() {
		return CODEC;
	}

	protected ItemLike getBaseSeedId() {
		return asItem();
	}

	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE_BY_AGE[getAge(state)];
	}

	protected int getBonemealAgeIncrease(Level level) {
		return 1;
	}

	public void buildState(DataGenContext<Block, ? extends PopFruit> ctx, RegistrateBlockstateProvider pvd) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(AGE);
			String id = ctx.getName() + "_" + age;
			return ConfiguredModel.builder().modelFile(pvd.models()
					.withExistingParent(id, pvd.modLoc("custom/crop/" + id))
					.texture("all", "block/crop/" + id)
					.texture("particle", "block/crop/" + ctx.getName() + "_particle")
					.renderType("cutout")).build();
		});
	}

	public void builtLoot(RegistrateBlockLootTables pvd, PopFruit block) {
		var helper = new LootHelper(pvd);
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(
				LootItem.lootTableItem(block.asItem())
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
						.when(helper.intState(block, AGE, MAX_AGE))
						.otherwise(LootItem.lootTableItem(block.asItem()))
		)));
	}

}
