package dev.xkmc.glimmeringtales.content.research.core;

import net.minecraft.core.Holder;

public record ResearchDependency(
		Holder<HexGraphData> parent, ResearchDependency.Type type
) {

	public enum Type {
		MAIN, BRANCH, NEXT
	}

}
