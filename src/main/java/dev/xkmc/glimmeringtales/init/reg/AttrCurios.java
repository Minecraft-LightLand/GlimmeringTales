package dev.xkmc.glimmeringtales.init.reg;

import dev.xkmc.glimmeringtales.content.item.curio.AttributeCurioItem;
import dev.xkmc.glimmeringtales.content.item.curio.AttributeData;
import dev.xkmc.l2core.init.reg.varitem.VarHolder;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.Locale;

public enum AttrCurios implements ItemLike {
	GOLDEN_RING("ring", AttributeData.of(
			AttributeData.base(GTRegistries.MAX_MANA, 0.1)
	)),
	RING_OF_REGENERATION("ring", AttributeData.of(
			AttributeData.base(GTRegistries.MAX_MANA, 0.1),
			AttributeData.base(GTRegistries.MANA_REGEN, 0.3)
	)),
	RING_OF_NATURE("ring", AttributeData.of(
			AttributeData.base(GTRegistries.MAX_MANA, 0.1),
			AttributeData.add(GTRegistries.EARTH.attr(), 0.1),
			AttributeData.add(GTRegistries.LIFE.attr(), 0.1),
			AttributeData.add(GTRegistries.FLAME.attr(), 0.1),
			AttributeData.add(GTRegistries.SNOW.attr(), 0.1)
	)),
	RING_OF_EARTH("ring", AttributeData.of(
			AttributeData.base(GTRegistries.MAX_MANA, 0.2),
			AttributeData.add(GTRegistries.EARTH.attr(), 0.5)
	)),
	RING_OF_LIFE("ring", AttributeData.of(
			AttributeData.base(GTRegistries.MAX_MANA, 0.2),
			AttributeData.add(GTRegistries.LIFE.attr(), 0.5)
	)),
	RING_OF_FLAME("ring", AttributeData.of(
			AttributeData.base(GTRegistries.MAX_MANA, 0.2),
			AttributeData.add(GTRegistries.FLAME.attr(), 0.5)
	)),
	RING_OF_SNOW("ring", AttributeData.of(
			AttributeData.base(GTRegistries.MAX_MANA, 0.2),
			AttributeData.add(GTRegistries.SNOW.attr(), 0.5)
	)),
	RING_OF_OCEAN("ring", AttributeData.of(
			AttributeData.base(GTRegistries.MAX_MANA, 0.2),
			AttributeData.add(GTRegistries.OCEAN.attr(), 0.5)
	)),
	RING_OF_THUNDER("ring", AttributeData.of(
			AttributeData.base(GTRegistries.MAX_MANA, 0.3),
			AttributeData.add(GTRegistries.THUNDER.attr(), 0.5)
	)),
	CHARM_OF_STRENGTH("charm", AttributeData.of(
			AttributeData.add(L2DamageTracker.MAGIC_FACTOR, 0.5)
	)),
	CHARM_OF_CAPACITY("charm", AttributeData.of(
			AttributeData.base(GTRegistries.MAX_MANA, 0.5)
	)),
	CHARM_OF_REGENERATION("charm", AttributeData.of(
			AttributeData.base(GTRegistries.MANA_REGEN, 0.5)
	)),
	CHARM_OF_NATURE("charm", AttributeData.of(
			AttributeData.base(GTRegistries.MANA_REGEN, 0.1)
	)),
	CHARM_OF_EARTH("charm", AttributeData.of(
			AttributeData.base(GTRegistries.MANA_REGEN, 0.1),
			AttributeData.add(L2DamageTracker.REDUCTION, -0.2)
	)),
	CHARM_OF_LIFE("charm", AttributeData.of(
			AttributeData.base(GTRegistries.MANA_REGEN, 0.1),
			AttributeData.add(L2DamageTracker.REGEN, 0.5)
	)),
	CHARM_OF_FLAME("charm", AttributeData.of(
			AttributeData.add(L2DamageTracker.FIRE_FACTOR, 1),
			AttributeData.add(L2DamageTracker.EXPLOSION_FACTOR, 1)
	)),
	CHARM_OF_SNOW("charm", AttributeData.of(
			AttributeData.add(L2DamageTracker.REDUCTION, -0.1),
			AttributeData.add(L2DamageTracker.FREEZING_FACTOR, 1)
	)),
	CHARM_OF_OCEAN("charm", AttributeData.of(
			AttributeData.base(GTRegistries.MANA_REGEN, 0.1),
			AttributeData.add(L2DamageTracker.MAGIC_FACTOR, 0.5)
	)),
	CHARM_OF_THUNDER("charm", AttributeData.of(
			AttributeData.base(GTRegistries.MANA_REGEN, 0.2),
			AttributeData.add(L2DamageTracker.LIGHTNING_FACTOR, 1)
	)),
	;


	public final VarHolder<AttributeCurioItem> item;

	AttrCurios(String part, AttributeData data) {
		item = GTItems.curio(name().toLowerCase(Locale.ROOT), part, data);
	}

	@Override
	public Item asItem() {
		return item.asItem();
	}

	public static void register() {

	}

}
