package dev.xkmc.glimmeringtales.content.core.spell;

import java.util.LinkedHashMap;
import java.util.Map;

public record ElementAffinity(LinkedHashMap<SpellElement, Double> affinity) {

	public static ElementAffinity of(Map<SpellElement, Double> map) {
		return new ElementAffinity(new LinkedHashMap<>(map));
	}

	@Deprecated
	public ElementAffinity {

	}

}