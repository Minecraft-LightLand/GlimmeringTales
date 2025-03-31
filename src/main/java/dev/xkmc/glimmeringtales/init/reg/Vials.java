package dev.xkmc.glimmeringtales.init.reg;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.shadowsoffire.apothic_attributes.api.ALObjects;
import dev.xkmc.glimmeringtales.content.item.materials.VialItem;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public enum Vials implements ItemLike {
	VIAL_OF_NATURE(() -> new MobEffectInstance(GTEffects.MANA_RECOVERY, 1000, 0)),
	VIAL_OF_BLOSSOM(() -> new MobEffectInstance(GTEffects.MANA_RECOVERY, 2000, 0),
			() -> new MobEffectInstance(ALObjects.MobEffects.REGENERATION, 1000, 0)),
	VIAL_OF_OCEAN(() -> new MobEffectInstance(GTEffects.MANA_RECOVERY, 1000, 1),
			() -> new MobEffectInstance(MobEffects.CONDUIT_POWER, 1000, 0)),
	MYSTIC_VIAL(
			() -> new MobEffectInstance(GTEffects.MANA_RECOVERY, 1000, 2)
	);

	public final ItemEntry<VialItem> item;

	@SafeVarargs
	Vials(Supplier<MobEffectInstance>... effects) {
		item = GlimmeringTales.REGISTRATE.item(name().toLowerCase(Locale.ROOT), p -> new VialItem(p, List.of(effects)))
				.model((ctx, pvd) ->
						pvd.generated(ctx, pvd.modLoc("item/vial/" + ctx.getName())))
				.register();
	}

	@Override
	public Item asItem() {
		return item.asItem();
	}


	public ItemStack asStack() {
		return item.asStack();
	}

	public static void register() {

	}

}
