package dev.xkmc.glimmeringtales.init.data;

import dev.xkmc.glimmeringtales.content.item.wand.WandHandleItem;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2backpack.content.common.BaseBagItem;
import dev.xkmc.l2core.init.reg.varitem.VarHolder;
import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import dev.xkmc.l2hostility.content.config.WeaponConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GTHostilityGen extends ConfigDataProvider {

	public GTHostilityGen(DataGenerator generator, CompletableFuture<HolderLookup.Provider> pvd) {
		super(generator, pvd, "Glimmering Tales x L2Hostility");
	}

	@Override
	public void add(Collector collector) {
		var config = new WeaponConfig();
		var empty = new WeaponConfig.ItemConfig(new ArrayList<>(List.of(ItemStack.EMPTY)), 0, 1000);
		var lv1 = new WeaponConfig.ItemConfig(new ArrayList<>(List.of(
				of(GTItems.GOLD_WAND, GTItems.RUNE_SAND),
				of(GTItems.GOLD_WAND, GTItems.RUNE_GRAVEL),
				of(GTItems.GOLD_WAND, GTItems.AMETHYST_PENETRATION),
				of(GTItems.ENDER_WAND, GTItems.RUNE_POWDER_SNOW),
				of(GTItems.ENDER_WAND, GTItems.RUNE_PACKED_ICE),
				of(GTItems.ENDER_WAND, GTItems.WINTER_STORM)
		)), 150, 100);
		var lv2 = new WeaponConfig.ItemConfig(new ArrayList<>(List.of(
				of(GTItems.GOLD_WAND, GTItems.RUNE_AMETHYST),
				of(GTItems.GOLD_WAND, GTItems.EARTHQUAKE),
				of(GTItems.NETHER_WAND, GTItems.HELL_MARK),
				of(GTItems.ENDER_WAND, GTItems.RUNE_BLUE_ICE),
				of(GTItems.ENDER_WAND, GTItems.SNOW_TORNADO),
				of(GTItems.OCEAN_WAND, GTItems.OCEAN_SHELTER)
		)), 250, 50);
		var lv3 = new WeaponConfig.ItemConfig(new ArrayList<>(List.of(
				of(GTItems.GOLD_WAND, GTItems.METEOR),
				of(GTItems.NETHER_WAND, GTItems.SPARK_BURST),
				of(GTItems.NETHER_WAND, GTItems.SOUL_BURST),
				of(GTItems.OCEAN_WAND, GTItems.ILLUSORY_FIELD),
				of(GTItems.OCEAN_WAND, GTItems.DARK_RAIN)
		)), 300, 50);
		var lv4 = new WeaponConfig.ItemConfig(new ArrayList<>(List.of(
				of(GTItems.THUNDER_WAND, GTItems.THUNDERSTORM),
				of(GTItems.THUNDER_WAND, GTItems.CHARGE_BURST),
				of(GTItems.THUNDER_WAND, GTItems.RUNE_THUNDER),
				of(GTItems.THUNDER_WAND, GTItems.THUNDER_SURGE)
		)), 350, 50);
		config.special_weapons.put(HolderSet.direct(
				EntityType.ZOMBIE.builtInRegistryHolder(),
				EntityType.HUSK.builtInRegistryHolder(),
				EntityType.SKELETON.builtInRegistryHolder(),
				EntityType.ZOMBIFIED_PIGLIN.builtInRegistryHolder(),
				EntityType.STRAY.builtInRegistryHolder(),
				EntityType.BOGGED.builtInRegistryHolder(),
				EntityType.WITHER_SKELETON.builtInRegistryHolder()
		), new ArrayList<>(List.of(empty, lv1, lv2, lv3, lv4)));
		collector.add(L2Hostility.WEAPON, GlimmeringTales.loc("spells"), config);
	}

	public static ItemStack of(VarHolder<WandHandleItem> wand, VarHolder<?> spell) {
		ItemStack ans = GTItems.WAND.asStack();
		GTItems.WAND_HANDLE.set(ans, wand.item());
		BaseBagItem.setItems(ans, List.of(spell.item().asStack()));
		return ans;
	}

}
