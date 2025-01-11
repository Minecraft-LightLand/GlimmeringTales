package dev.xkmc.glimmeringtales.content.core.spell;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltip;
import dev.xkmc.glimmeringtales.content.research.core.PlayerResearch;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.l2core.util.Proxy;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record SpellInfo(@Nullable Holder<NatureSpell> spell,
						IAffinityProvider affinity,
						boolean advanced, boolean consume) {

	public static final SpellInfo EMPTY = new SpellInfo(null, DefaultAffinity.INS, false, false);

	public static SpellInfo ofSpell(@Nullable Holder<NatureSpell> spell) {
		if (spell == null) return EMPTY;
		return new SpellInfo(spell, DefaultAffinity.INS, true, false);
	}

	public static SpellInfo ofRune(@Nullable RuneBlock rune) {
		if (rune == null) return EMPTY;
		return new SpellInfo(rune.spell(), DefaultAffinity.INS, false, false);
	}

	public static SpellInfo ofBlock(BlockSpell block, ElementAffinity aff) {
		return new SpellInfo(block.spell(), aff, false, block.breakBlock());
	}

	public SpellCost getCost(LivingEntity player, ItemStack wand) {
		if (spell == null) return SpellCost.ZERO;
		var ns = spell.value();
		return ns.manaCost(player, affinity().getFinalAffinity(ns.elem(), player, wand));
	}

	public List<Component> getCastTooltip(Player player, ItemStack wand, ItemStack core) {
		if (spell == null) return List.of();
		var ns = spell.value();
		var id = spell.unwrapKey().orElseThrow();
		List<Component> list = new ArrayList<>();
		var cost = getCost(player, wand);
		ns.addDescription(list, cost, advanced());
		SpellTooltip.get(player.level(), ns).brief(id, list);
		cost.addCostInfo(list, player);
		if (consume) {
			list.add(GTLang.OVERLAY_DESTROY.get().withStyle(ChatFormatting.RED));
		}
		return list;
	}

	public void runeItemDesc(Level level, List<Component> list) {
		if (spell == null) return;
		var ns = spell.value();
		var id = spell.unwrapKey().orElseThrow();
		ns.addDescription(list, ns.manaCost(null, 1), advanced());
		list.add(SpellTooltip.get(level, ns).format(id));
		if (ns.mob() != null) {
			list.add(GTLang.TOOLTIP_MOB_USE.get().withStyle(ChatFormatting.RED));
		}
		if (ns.graph() == null) return;
		var pl = Proxy.getPlayer();
		if (pl == null) return;
		var research = PlayerResearch.of(pl).get(ns.graph().unwrapKey().orElseThrow().location());
		if (research == null) return;
		research.getFullDesc(list, ns.graph().value().bonuses());
	}

}
