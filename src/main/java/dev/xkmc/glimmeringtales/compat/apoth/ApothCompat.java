package dev.xkmc.glimmeringtales.compat.apoth;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.shadowsoffire.apotheosis.Apoth;
import dev.shadowsoffire.apotheosis.data.RarityProvider;
import dev.shadowsoffire.apotheosis.loot.LootCategory;
import dev.shadowsoffire.apothic_attributes.api.ALObjects;
import dev.shadowsoffire.apothic_attributes.modifiers.EntitySlotGroup;
import dev.shadowsoffire.placebo.datagen.DataGenBuilder;
import dev.shadowsoffire.placebo.registry.DeferredHelper;
import dev.xkmc.glimmeringtales.content.item.wand.RuneWandItem;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import net.minecraft.data.DataProvider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.function.Predicate;

public class ApothCompat {

	public static final DeferredHelper R = DeferredHelper.create(GlimmeringTales.MODID);

	public static final LootCategory WAND;

	private static LootCategory register(String path, Predicate<ItemStack> filter, EntitySlotGroup slots, int priority) {
		return Apoth.R.custom(path, Apoth.BuiltInRegs.LOOT_CATEGORY.key(), new LootCategory(filter, slots, priority));
	}

	private static LootCategory register(String path, Predicate<ItemStack> filter, EntitySlotGroup slots) {
		return register(path, filter, slots, 2000);
	}

	static {
		WAND = register("wand", (s) -> s.getItem() instanceof RuneWandItem, ALObjects.EquipmentSlotGroups.HAND);
	}

	public static void register(IEventBus bus) {
		bus.register(R);
	}

	public static void data(GatherDataEvent event) {
		DataProvider.INDENT_WIDTH.set(2);
		DataGenBuilder.create(GlimmeringTales.MODID)
				.provider(RarityProvider::new)
				.provider(GTAffixProvider::new)
				.build(event);
	}

	public static void lang(RegistrateLangProvider pvd) {
		for (var e : GTAffixProvider.LIST)
			e.genLang(pvd);
	}
}
