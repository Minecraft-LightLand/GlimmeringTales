package dev.xkmc.glimmeringtales.content.core.spell;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.entity.hostile.MobCastingData;
import dev.xkmc.glimmeringtales.content.research.core.HexGraphData;
import dev.xkmc.glimmeringtales.content.research.core.PlayerResearch;
import dev.xkmc.glimmeringtales.content.research.core.ResearchState;
import dev.xkmc.glimmeringtales.content.research.core.SpellResearch;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public record NatureSpell(
		Holder<SpellAction> spell,
		SpellElement elem,
		int focus, int cost, int maxConsumeTick,
		SpellTooltipData tooltip,
		@Nullable MobCastingData mob,
		@Nullable Holder<HexGraphData> graph
) {

	private static final int MIN_MANA_COST = 1, CAST_COOLDOWN = 10, BREAK_COOLDOWN = 20;
	private static final double MIN_AFFINITY = 0.2;

	public boolean consumeMana(LivingEntity user, ItemStack stack, double affinity, int useTick, boolean charging, boolean simulate) {
		var consume = manaCost(user, affinity);
		var cast = spell().value().castType();
		if (maxConsumeTick > 0) {
			if (cast == SpellCastType.CONTINUOUS && useTick > maxConsumeTick) consume = SpellCost.ZERO;
			if (cast == SpellCastType.CHARGE && (!charging || useTick > maxConsumeTick)) consume = SpellCost.ZERO;
		}
		if (user instanceof Player player) {
			var mana = GTRegistries.MANA.type().getOrCreate(player);
			if (simulate) return mana.getMana() >= cost && mana.getFocus() >= focus;
			if (!mana.consume(player, consume)) {
				if (!user.level().isClientSide() && (
						spell.value().castType() == SpellCastType.CONTINUOUS ||
								spell.value().castType() == SpellCastType.CHARGE && charging
				)) player.getCooldowns().addCooldown(stack.getItem(), BREAK_COOLDOWN);
				return false;
			}
			if (!user.level().isClientSide() && (
					spell.value().castType() == SpellCastType.INSTANT ||
							spell.value().castType() == SpellCastType.CHARGE && !charging
			)) player.getCooldowns().addCooldown(stack.getItem(), CAST_COOLDOWN);
		}
		return true;
	}

	// ------ Tooltip ------

	private MutableComponent lang() {
		return Component.translatable(SpellAction.lang(spell().unwrapKey().orElseThrow().location()));
	}

	public SpellCost manaCost(@Nullable LivingEntity le, double affinity) {
		if (affinity < MIN_AFFINITY) affinity = MIN_AFFINITY;
		int mana = (int) Math.round(cost / affinity);
		int focus = focus();
		if (le instanceof Player player && graph != null) {
			var g = graph.value();
			SpellResearch research = PlayerResearch.of(player).get(graph.unwrapKey().orElseThrow().location());
			if (research != null && research.getState() == ResearchState.COMPLETED) {
				int cost = research.getCost();
				for (var ent : g.bonuses()) {
					if (cost <= ent.cost()) {
						mana = ent.modifyMana(mana);
						focus = ent.modifyFocus(focus);
					}
				}
			}
		}
		return new SpellCost(focus, Math.max(MIN_MANA_COST, mana));
	}

	void addDescription(List<Component> list, SpellCost consume, boolean advanced) {
		list.add(GTLang.TOOLTIP_SPELL.get(lang().withStyle(ChatFormatting.GOLD),
				elem.coloredDesc()).withStyle(ChatFormatting.GRAY));
		if (advanced) {
			list.add(spell().value().castType().desc());
			list.add(spell().value().triggerType().desc());
		}
		list.add(addLine(GTLang.TOOLTIP_COST, consume::manaText));
		list.add(addLine(GTLang.TOOLTIP_FOCUS, consume::focusText));
	}

	private Component addLine(GTLang lang, Function<Integer, MutableComponent> func) {
		Component val = func.apply(1).withStyle(ChatFormatting.BLUE);
		if (spell().value().castType() != SpellCastType.INSTANT) {
			if (maxConsumeTick <= 0) {
				val = GTLang.TOOLTIP_COST_CONT.get(val).withStyle(ChatFormatting.GRAY);
			} else {
				Component max = func.apply(maxConsumeTick).withStyle(ChatFormatting.BLUE);
				val = GTLang.TOOLTIP_COST_CAPPED.get(val, max).withStyle(ChatFormatting.GRAY);
			}
		}
		return lang.get(val).withStyle(ChatFormatting.YELLOW);
	}

}
