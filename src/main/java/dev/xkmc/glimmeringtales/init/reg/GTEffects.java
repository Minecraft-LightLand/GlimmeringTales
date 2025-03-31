package dev.xkmc.glimmeringtales.init.reg;

import dev.xkmc.glimmeringtales.content.effect.GTEffect;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2core.init.reg.registrate.PotionBuilder;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.alchemy.Potions;

public class GTEffects {

	public static final SimpleEntry<MobEffect> MANA_RECOVERY;
	public static final SimpleEntry<MobEffect> MANA_DEPLETION;
	public static final SimpleEntry<MobEffect> MANA_EXPANSION;
	public static final PotionBuilder BUILDER;

	static {
		MANA_RECOVERY = new SimpleEntry<>(GlimmeringTales.REGISTRATE.effect("mana_recovery",
				() -> new GTEffect(MobEffectCategory.BENEFICIAL, 0xff6A41CA)
						.addAttributeModifier(GTRegistries.MANA_REGEN, GlimmeringTales.loc("mana_recovery"), 20,
								AttributeModifier.Operation.ADD_VALUE),
				"Increase mana restoration rate").lang(MobEffect::getDescriptionId, "Mana Recovery").register());

		MANA_DEPLETION = new SimpleEntry<>(GlimmeringTales.REGISTRATE.effect("mana_depletion",
				() -> new GTEffect(MobEffectCategory.HARMFUL, 0xff000000)
						.addAttributeModifier(GTRegistries.MANA_REGEN, GlimmeringTales.loc("mana_depletion"), -0.25,
								AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
				"Decrease mana restoration rate").lang(MobEffect::getDescriptionId, "Mana Depletion").register());

		MANA_EXPANSION = new SimpleEntry<>(GlimmeringTales.REGISTRATE.effect("mana_expansion",
				() -> new GTEffect(MobEffectCategory.BENEFICIAL, 0xffffffff)
						.addAttributeModifier(GTRegistries.MANA_REGEN, GlimmeringTales.loc("mana_expansion"), 0.25,
								AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
				"Increase max mana").lang(MobEffect::getDescriptionId, "Mana Expansion").register());

	}

	static {
		BUILDER = new PotionBuilder(L2Complements.REGISTRATE);
		var base = BUILDER.regPotion("mana_recovery", "mana_recovery", MANA_RECOVERY, Potions.AWKWARD, PopFruitType.POP_FRUIT, 1000, 0);
		BUILDER.regPotion("long_mana_recovery", "mana_recovery", MANA_RECOVERY, base, PopFruitType.BLOSSOM_POP_FRUIT, 2000, 0);
		BUILDER.regPotion("strong_mana_recovery", "mana_recovery", MANA_RECOVERY, base, PopFruitType.OCEAN_POP_FRUIT, 1000, 1);
		GlimmeringTales.REGISTRATE.addRegisterCallback(Registries.ITEM, () -> BUILDER.regTab(GTItems.TAB.key()));
	}

	public static void register() {

	}


}
