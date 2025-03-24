package dev.xkmc.glimmeringtales.init.reg;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.glimmeringtales.content.block.crop.BlossomPopFruit;
import dev.xkmc.glimmeringtales.content.block.crop.OceanPopFruit;
import dev.xkmc.glimmeringtales.content.block.crop.PopFruit;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.Locale;

public enum PopFruitType {
	POP_FRUIT(PopFruit::new),
	BLOSSOM_POP_FRUIT(BlossomPopFruit::new),
	OCEAN_POP_FRUIT(OceanPopFruit::new);

	public final BlockEntry<? extends PopFruit> block;

	<T extends PopFruit> PopFruitType(NonNullFunction<BlockBehaviour.Properties, T> factory) {
		String name = name().toLowerCase(Locale.ROOT);
		FoodProperties.Builder builder = new FoodProperties.Builder();
		builder.nutrition(4).saturationModifier(0.6f);
		FoodProperties food = builder.build();
		block = GlimmeringTales.REGISTRATE.block(name, factory)
				.properties(p -> p.mapColor(MapColor.PLANT).randomTicks().instabreak().noCollission()
						.sound(SoundType.CROP).pushReaction(PushReaction.DESTROY))
				.item(ItemNameBlockItem::new)
				.properties(p -> p.food(food))
				.model((ctx, pvd) -> pvd.generated(ctx))
				.lang(RegistrateLangProvider.toEnglishName(name)).build()
				.blockstate((ctx, pvd) -> ctx.get().buildState(ctx, pvd))
				.loot((pvd, block) -> block.builtLoot(pvd, block))
				.tag(BlockTags.CROPS)
				.register();
	}

	public static void register() {
	}

}
