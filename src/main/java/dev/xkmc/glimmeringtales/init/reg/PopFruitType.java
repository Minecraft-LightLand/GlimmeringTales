package dev.xkmc.glimmeringtales.init.reg;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.glimmeringtales.content.block.crop.*;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.Locale;
import java.util.function.UnaryOperator;

public enum PopFruitType implements ItemLike {
	POP_FRUIT(PopFruit::new, b -> b.nutrition(6).saturationModifier(0.6f)
			.effect(() -> new MobEffectInstance(GTEffects.MANA_RECOVERY, 500, 0), 1)),
	BLOSSOM_POP_FRUIT(BlossomPopFruit::new, b -> b.nutrition(4).saturationModifier(0.8f)
			.effect(() -> new MobEffectInstance(GTEffects.MANA_RECOVERY, 500, 0), 1)
			.effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 500, 0), 1)),
	OCEAN_POP_FRUIT(OceanPopFruit::new, b -> b.nutrition(4).saturationModifier(0.6f)
			.effect(() -> new MobEffectInstance(GTEffects.MANA_RECOVERY, 500, 0), 1)
			.effect(() -> new MobEffectInstance(MobEffects.CONDUIT_POWER, 500, 0), 1)),
	;

	public final BlockEntry<? extends AbstractPopFruit> block;

	<T extends AbstractPopFruit> PopFruitType(NonNullFunction<BlockBehaviour.Properties, T> factory, UnaryOperator<FoodProperties.Builder> foodBuilder) {
		String name = name().toLowerCase(Locale.ROOT);
		FoodProperties food = foodBuilder.apply(new FoodProperties.Builder()).build();
		block = GlimmeringTales.REGISTRATE.block(name, factory)
				.properties(p -> p.mapColor(MapColor.PLANT).randomTicks().instabreak().noCollission()
						.sound(SoundType.CROP).pushReaction(PushReaction.DESTROY))
				.item(PopFruitItem::new)
				.properties(p -> p.food(food))
				.tag(GTTagGen.POP_FRUIT)
				.model((ctx, pvd) ->
						pvd.generated(ctx, pvd.modLoc("item/crop/" + ctx.getName())))
				.lang(RegistrateLangProvider.toEnglishName(name)).build()
				.blockstate((ctx, pvd) -> ctx.get().buildState(ctx, pvd))
				.loot((pvd, block) -> block.builtLoot(pvd, block))
				.tag(BlockTags.CROPS, GTTagGen.POP_FRUIT_BLOCK)
				.register();
	}

	public AbstractPopFruit get() {
		return block.get();
	}

	@Override
	public Item asItem() {
		return block.asItem();
	}

	public ItemStack asStack() {
		return block.asStack();
	}

	public static void register() {
	}

}
