package dev.xkmc.glimmeringtales.content.core.description;

import dev.xkmc.glimmeringtales.content.engine.instance.GTKnockBlock;
import dev.xkmc.glimmeringtales.content.engine.instance.LightningInstance;
import dev.xkmc.glimmeringtales.content.engine.processor.PassiveHealProcessor;
import dev.xkmc.glimmeringtales.content.engine.processor.StackingEffectProcessor;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.extension.Extension;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringUtil;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.common.Tags;

import java.util.OptionalDouble;

public class SpellTooltipRegistry {

	public static void init() {
		EngineRegistry.DAMAGE.get().add(new DamageTooltip());
		EngineRegistry.EFFECT.get().add(new EffectTooltip());
		GTEngine.KNOCK.get().add(new FallingTooltip());
		GTEngine.EP_STACK.get().add(new StackingTooltip());
		GTEngine.THUNDER.get().add(new LightningTooltip());
		GTEngine.HEAL.get().add(new HealTooltip());
	}

	public abstract static class Tooltip<T> extends Extension<Component, T> {

		public Tooltip() {
			super(Component.class);
		}

		protected static MutableComponent damageStart(OptionalDouble dmg) {
			return dmg.isEmpty() ? Component.empty() : GTLang.DESC_DMG.get(
					Component.literal((int) dmg.getAsDouble() + "").withStyle(ChatFormatting.DARK_AQUA)
			).append(GTLang.DESC_SPACE.get());
		}

	}

	static class DamageTooltip extends Tooltip<DamageProcessor> {

		@Override
		public Component process(DamageProcessor e) {
			var dmg = e.damage().exp().getAsConstant();
			var type = e.damageType();
			MutableComponent ans = damageStart(dmg);
			test(ans, type, DamageTypeTags.IS_FIRE, GTLang.DESC_FIRE, ChatFormatting.RED);
			test(ans, type, DamageTypeTags.IS_EXPLOSION, GTLang.DESC_EXPLOSION, ChatFormatting.GOLD);
			test(ans, type, DamageTypeTags.IS_FREEZING, GTLang.DESC_FREEZING, ChatFormatting.AQUA);
			test(ans, type, DamageTypeTags.IS_PROJECTILE, GTLang.DESC_PROJECTILE, ChatFormatting.WHITE);
			test(ans, type, DamageTypeTags.IS_LIGHTNING, GTLang.DESC_LIGHTNING, ChatFormatting.YELLOW);
			test(ans, type, Tags.DamageTypes.IS_MAGIC, GTLang.DESC_MAGIC, ChatFormatting.LIGHT_PURPLE);
			return ans.append(GTLang.DESC_DAMAGE.get());
		}

		private void test(MutableComponent ans, Holder<DamageType> type, TagKey<DamageType> key, GTLang lang, ChatFormatting formatting) {
			if (type.is(key)) {
				ans.append(lang.get().withStyle(formatting)).append(GTLang.DESC_SPACE.get());
			}
		}

	}

	static class FallingTooltip extends Tooltip<GTKnockBlock> {

		@Override
		public Component process(GTKnockBlock e) {
			var dpb = e.damagePerBlock().exp().getAsConstant();
			var max = e.maxDamage().exp().getAsConstant();
			var spe = e.speed().exp().getAsConstant();
			OptionalDouble dmg = OptionalDouble.empty();
			if (dpb.isPresent() && spe.isPresent() && max.isPresent()) {
				dmg = OptionalDouble.of(damage(dpb.getAsDouble(), spe.getAsDouble(), max.getAsDouble()));
			}
			MutableComponent ans = damageStart(dmg);
			ans.append(GTLang.DESC_FALLING_BLOCK.get().withStyle(ChatFormatting.RED)).append(GTLang.DESC_SPACE.get());
			return ans.append(GTLang.DESC_DAMAGE.get());
		}

		private double damage(double dpb, double spe, double max) {
			double g = 0.04;
			double f = 0.02;
			double p = g / f;
			double t = Math.log((spe / p + 1)) / f;
			double h = (spe - g * t) / f;
			return h < 1 ? 0 : Math.min(max, dpb * (h - 1));
		}

	}

	static class LightningTooltip extends Tooltip<LightningInstance> {

		@Override
		public Component process(LightningInstance e) {
			var dmg = e.damage().exp().getAsConstant();
			MutableComponent ans = damageStart(dmg);
			ans.append(GTLang.DESC_LIGHTNING.get().withStyle(ChatFormatting.YELLOW)).append(GTLang.DESC_SPACE.get());
			return ans.append(GTLang.DESC_DAMAGE.get());
		}

	}

	static class EffectTooltip extends Tooltip<EffectProcessor> {

		@Override
		public Component process(EffectProcessor e) {
			int amp = (int) e.amplifier().exp().getAsConstant().orElse(0d);
			int dur = (int) e.duration().exp().getAsConstant().orElse(0d);
			var eff = e.eff();
			return getTooltip(eff, amp, dur);
		}

	}

	static class StackingTooltip extends Tooltip<StackingEffectProcessor> {

		@Override
		public Component process(StackingEffectProcessor e) {
			int dur = (int) e.duration().exp().getAsConstant().orElse(0d);
			var eff = e.eff();
			return getTooltip(eff, 0, dur);
		}

	}

	static class HealTooltip extends Tooltip<PassiveHealProcessor> {

		@Override
		public Component process(PassiveHealProcessor e) {
			var dur = e.interval().exp().getAsConstant();
			var val = e.heal().exp().getAsConstant();
			MutableComponent comp = GTLang.HP.get();
			if (dur.isPresent() && val.isPresent()) {
				int ans = (int) (20 * val.getAsDouble() / dur.getAsDouble());
				comp = Component.empty().append(Component.literal(ans + "").withStyle(ChatFormatting.AQUA))
						.append(GTLang.DESC_SPACE.get()).append(comp);
			}
			return GTLang.DESC_HEAL.get(comp).withStyle(ChatFormatting.GRAY);
		}

	}

	private static Component getTooltip(Holder<MobEffect> eff, int amp, int dur) {
		MutableComponent ans = Component.translatable(eff.value().getDescriptionId());
		MobEffect mobeffect = eff.value();
		if (amp > 0) {
			ans = Component.translatable("potion.withAmplifier", ans,
					Component.translatable("potion.potency." + amp));
		}
		if (dur > 20) {
			ans = Component.translatable("potion.withDuration", ans,
					Component.literal(StringUtil.formatTickDuration(dur, 20)));
		}
		return ans.withStyle(mobeffect.getCategory().getTooltipFormatting());
	}

}
