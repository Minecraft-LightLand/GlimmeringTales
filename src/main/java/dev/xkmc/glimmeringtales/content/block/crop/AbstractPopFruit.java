package dev.xkmc.glimmeringtales.content.block.crop;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.glimmeringtales.init.data.GTConfigs;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.l2library.content.explosion.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import net.neoforged.neoforge.common.CommonHooks;

import java.util.List;

public class AbstractPopFruit extends CropBlock {

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

	public static final MapCodec<AbstractPopFruit> CODEC = simpleCodec(AbstractPopFruit::new);
	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
	public static final int MAX_AGE = 3;

	public static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(7, 0, 7, 9, 1, 9),
			Block.box(6, 0, 6, 10, 2, 10),
			Block.box(5, 0, 5, 11, 4, 11),
			Block.box(4, 0, 4, 12, 6, 12)
	};

	public AbstractPopFruit(Properties properties) {
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

	public List<Block> plantableOn() {
		return List.of(Blocks.GRASS_BLOCK, Blocks.DIRT);
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
		float f = 1 + getGrowthSpeedBonus(state, level, pos, random);
		for (Direction dir : Direction.values()) {
			if (dir.getAxis() == Direction.Axis.Y) continue;
			if (level.getBlockState(pos.relative(dir)).is(GTItems.CRYSTAL_VINE)) {
				f += GTConfigs.SERVER.popFruitBiomeGrowFactor.getAsInt();
			}
		}
		int def = GTConfigs.SERVER.popFruitBiomeGrowRarity.getAsInt();
		if (CommonHooks.canCropGrow(level, pos, state, random.nextInt((int) (def / f) + 1) == 0)) {
			level.setBlock(pos, getStateForAge(i + 1), 2);
			CommonHooks.fireCropGrowPost(level, pos, state);
		}
	}

	protected float getGrowthSpeedBonus(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		return 0;
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
		return entity instanceof LivingEntity;
	}

	protected void onExplode(Level level, BlockPos pos, int r) {
	}

	public MapCodec<AbstractPopFruit> codec() {
		return CODEC;
	}

	protected ItemLike getBaseSeedId() {
		return asItem();
	}

	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE_BY_AGE[getAge(state)];
	}

	protected int getBonemealAgeIncrease(Level level) {
		return level.getRandom().nextDouble() < GTConfigs.SERVER.popFruitBonemealGrowChance.getAsDouble() ? 1 : 0;
	}

	public void buildState(DataGenContext<Block, ? extends AbstractPopFruit> ctx, RegistrateBlockstateProvider pvd) {
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

	public void builtLoot(RegistrateBlockLootTables pvd, AbstractPopFruit block) {
		var helper = new LootHelper(pvd);
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(
				LootItem.lootTableItem(block.asItem())
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
						.when(helper.intState(block, AGE, MAX_AGE))
						.otherwise(LootItem.lootTableItem(block.asItem()))
		)));
	}

}
