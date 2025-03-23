package dev.xkmc.glimmeringtales.content.block.crop;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2core.serial.loot.LootHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

import java.util.List;

public class PopFruit extends CropBlock {

	public static final MapCodec<PopFruit> CODEC = simpleCodec(PopFruit::new);

	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(0, 0, 0, 16, 2, 16),
			Block.box(0, 0, 0, 16, 4, 16),
			Block.box(0, 0, 0, 16, 7, 16),
			Block.box(0, 0, 0, 16, 11, 16),
			Block.box(0, 0, 0, 16, 13, 16)
	};

	public PopFruit(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		list.add(GTLang.TOOLTIP_VINE.get().withStyle(ChatFormatting.GRAY));
	}

	public MapCodec<PopFruit> codec() {
		return CODEC;
	}

	protected ItemLike getBaseSeedId() {
		return GTItems.CRYSTAL_VINE.asItem();
	}

	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE_BY_AGE[getAge(state)];
	}

	public static void buildState(DataGenContext<Block, PopFruit> ctx, RegistrateBlockstateProvider pvd) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(AGE);
			String id = ctx.getName() + "_" + age;
			return ConfiguredModel.builder().modelFile(pvd.models()
					.cross(id, pvd.modLoc("block/crop/" + id)).renderType("cutout")).build();
		});
	}

	public static void builtLoot(RegistrateBlockLootTables pvd, PopFruit block) {
		var helper = new LootHelper(pvd);
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(
				LootItem.lootTableItem(block.asItem())
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
						.when(helper.intState(block, AGE, 7))
						.otherwise(LootItem.lootTableItem(block.asItem()))
		)));
	}

}
