package dev.xkmc.glimmeringtales.content.research.core;

import dev.xkmc.glimmeringtales.content.core.spell.ResearchBonus;
import dev.xkmc.glimmeringtales.content.core.spell.SpellElement;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public record HexGraphData(
		LinkedHashMap<String, SpellElement> map,
		ArrayList<String> flows,
		ArrayList<ResearchBonus> bonuses
) {

}
