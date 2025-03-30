package dev.xkmc.glimmeringtales.init.reg;

import dev.xkmc.glimmeringtales.content.effect.GTEffect;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class GTEffects {

	public static final SimpleEntry<MobEffect> MANA_RECOVERY;
	public static final SimpleEntry<MobEffect> MANA_DEPLETION;
	public static final SimpleEntry<MobEffect> MANA_EXPANSION;

	static {
		MANA_RECOVERY = new SimpleEntry<>(GlimmeringTales.REGISTRATE.effect("mana_recovery",
				() -> new GTEffect(MobEffectCategory.BENEFICIAL, 0xffffffff)
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

	public static void register() {

	}

}
