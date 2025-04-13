package dev.xkmc.glimmeringtales.content.research.core;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

public record ResearchDependency(
		Holder<HexGraphData> parent, ResearchDependency.Type type
) {

	public ResourceLocation getId() {
		return parent.unwrapKey().orElseThrow().location();
	}

	public enum Type {
		MAIN, BRANCH, NEXT
	}

}
