package dev.xkmc.glimmeringtales.content.research.core;

import dev.xkmc.glimmeringtales.content.core.spell.ResearchBonus;
import dev.xkmc.glimmeringtales.content.core.spell.SpellElement;
import dev.xkmc.glimmeringtales.content.research.logic.HexHandler;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class SpellResearch {

	public static final int LOCKED = -2, UNLOCKED = -1;

	private final PlayerResearch player;
	private final ResourceLocation id;
	private final ResearchData data;
	private final HexGraph graph;

	public SpellResearch(PlayerResearch player, ResourceLocation id, ResearchData data, HexGraph graph) {
		this.player = player;
		this.id = id;
		this.data = data;
		this.graph = graph;
	}

	public final boolean unlocked() {
		return data.cost() > LOCKED;
	}

	public final int getCost() {
		return data.cost();
	}

	public final void setUnlock() {
		if (!unlocked()) {
			data.setCost(UNLOCKED);
		}
	}

	public void updateBestSolution(HexHandler hex, HexOrder data, int cost) {
		this.data.update(hex, data, cost);
	}

	public HexHandler getSolution() {
		return data.hex();
	}

	public final boolean usable() {
		return data.cost() > UNLOCKED;
	}

	public ResearchState getState() {
		return switch (data.cost()) {
			case LOCKED -> ResearchState.LOCKED;
			case UNLOCKED -> ResearchState.UNLOCKED;
			default -> ResearchState.COMPLETED;
		};
	}

	public boolean visible() {
		return true;
	}

	public HexOrder getMiscData() {
		return data.order().copy();
	}

	public boolean matchList(List<SpellElement> elem) {
		return elem.equals(data.order().list());
	}

	public HexGraph getGraph() {
		return graph;
	}

	public void save() {
		player.save(id, data);
	}

	public void getFullDesc(List<Component> list, List<ResearchBonus> bonuses) {
		list.add(GTLang.HEX_STATUS.get(getState().getDesc()).withStyle(ChatFormatting.GRAY));
		if (!usable()) return;
		var cost = Component.literal("" + getCost()).withStyle(ChatFormatting.AQUA);
		list.add(GTLang.HEX_COST.get(cost).withStyle(ChatFormatting.GRAY));
		list.add(GTLang.HEX_BONUS.get().withStyle(ChatFormatting.GRAY));
		for (var e : bonuses) {
			var format = getCost() <= e.cost() ? ChatFormatting.YELLOW : ChatFormatting.DARK_GRAY;
			list.add(e.desc().withStyle(format));
			if (getCost() > e.cost()) {
				break;
			}
		}
	}

}
