package dev.xkmc.glimmeringtales.content.research.core;

import dev.xkmc.glimmeringtales.content.core.spell.ResearchBonus;
import dev.xkmc.glimmeringtales.content.core.spell.SpellElement;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public record HexGraphData(
		Item icon,
		@Nullable ResearchDependency parent,
		LinkedHashMap<String, SpellElement> map,
		ArrayList<String> flows,
		ArrayList<ResearchBonus> bonuses
) {

}
