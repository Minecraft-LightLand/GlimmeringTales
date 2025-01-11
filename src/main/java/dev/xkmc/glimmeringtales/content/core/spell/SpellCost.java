package dev.xkmc.glimmeringtales.content.core.spell;

import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public record SpellCost(double focus, double mana) {

	public static final SpellCost ZERO = new SpellCost(0, 0);

	public MutableComponent manaText(double factor) {
		return Component.literal(Math.round(mana() * factor) + "");
	}

	public MutableComponent focusText(double factor) {
		double val = focus() * factor;
		int num = (int) Math.round(val);
		if (val < 5 && Math.abs(val - num) > 0.1) {
			return Component.literal(Math.round(val * 10) * 0.1 + "");
		}
		return Component.literal(num + "");
	}

	public void addCostInfo(List<Component> list, Player player) {
		var mana = GTRegistries.MANA.type().getExisting(player).orElse(null);
		if (mana == null) return;
		list.add(addLine(player, GTRegistries.MAX_MANA, GTLang.OVERLAY_MANA, mana(), mana.getMana()));
		list.add(addLine(player, GTRegistries.MAX_FOCUS, GTLang.OVERLAY_FOCUS, focus(), mana.getFocus()));
	}

	private Component addLine(Player player, Holder<Attribute> attr, GTLang lang, double cost, double val) {
		double max = player.getAttributeValue(attr);
		var cmval = Component.literal("" + Math.round(val))
				.withStyle(val < cost ? ChatFormatting.RED : val < max ? ChatFormatting.GREEN : ChatFormatting.AQUA);
		var cmmax = Component.literal("" + Math.round(max)).withStyle(ChatFormatting.AQUA);
		return lang.get(cmval, cmmax).withStyle(ChatFormatting.LIGHT_PURPLE);
	}

}
