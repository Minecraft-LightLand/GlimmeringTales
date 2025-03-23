package dev.xkmc.glimmeringtales.content.block.crop;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.glimmeringtales.content.block.api.CropGrowListener;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.l2harvester.api.HarvestResult;
import dev.xkmc.l2harvester.api.HarvestableBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LifeCrystalCrop extends CropBlock implements CropGrowListener, HarvestableBlock {

	public static final MapCodec<LifeCrystalCrop> CODEC = simpleCodec(LifeCrystalCrop::new);

	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(0, 0, 0, 16, 2, 16),
			Block.box(0, 0, 0, 16, 3, 16),
			Block.box(0, 0, 0, 16, 4, 16),
			Block.box(0, 0, 0, 16, 7, 16),
			Block.box(0, 0, 0, 16, 9, 16),
			Block.box(0, 0, 0, 16, 11, 16),
			Block.box(0, 0, 0, 16, 12, 16),
			Block.box(0, 0, 0, 16, 13, 16)
	};

	public LifeCrystalCrop(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		list.add(GTLang.TOOLTIP_VINE.get().withStyle(ChatFormatting.GRAY));
	}

	@Override
	public @Nullable HarvestResult getHarvestResult(Level level, BlockState state, BlockPos blockPos) {
		if (state.getValue(AGE) < getMaxAge()) return null;
		return new HarvestResult(Blocks.AIR.defaultBlockState(), List.of(GTItems.CRYSTAL_LIFE.asStack()));
	}

	public MapCodec<LifeCrystalCrop> codec() {
		return CODEC;
	}

	protected ItemLike getBaseSeedId() {
		return GTItems.CRYSTAL_VINE.asItem();
	}

	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE_BY_AGE[getAge(state)];
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
	}

	@Override
	public void onNeighborGrow(ServerLevel level, BlockState state, BlockPos pos, BlockState source) {
		if (source.getBlock() instanceof CropBlock block) {
			if (block.getAge(source) == block.getMaxAge()) {
				int i = getAge(state);
				if (i >= getMaxAge()) return;
				if (CommonHooks.canCropGrow(level, pos, state, true)) {
					level.setBlock(pos, getStateForAge(i + 1), 2);
					CommonHooks.fireCropGrowPost(level, pos, state);
				}
			}
		}
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return false;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
	}

	public static void buildState(DataGenContext<Block, LifeCrystalCrop> ctx, RegistrateBlockstateProvider pvd) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(AGE);
			String id = ctx.getName() + "_" + age;
			return ConfiguredModel.builder().modelFile(pvd.models()
					.cross(id, pvd.modLoc("block/crop/" + id)).renderType("cutout")).build();
		});
	}

	public static void builtLoot(RegistrateBlockLootTables pvd, LifeCrystalCrop block) {
		var helper = new LootHelper(pvd);
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(
				LootItem.lootTableItem(GTItems.CRYSTAL_LIFE.asItem())
						.when(helper.intState(block, AGE, 7))
						.otherwise(LootItem.lootTableItem(GTItems.CRYSTAL_VINE.asItem()))
		)));
	}

}
