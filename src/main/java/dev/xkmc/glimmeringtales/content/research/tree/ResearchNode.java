package dev.xkmc.glimmeringtales.content.research.tree;

import dev.xkmc.glimmeringtales.content.research.core.HexGraphData;
import dev.xkmc.glimmeringtales.content.research.core.ResearchDependency;
import dev.xkmc.glimmeringtales.content.research.core.SpellResearch;
import dev.xkmc.glimmeringtales.content.research.logic.HexHalfResult;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ResearchNode {

	private final ResearchTree tree;
	final Holder<HexGraphData> hex;
	final ResourceLocation id;
	ResearchNode main, branch, next;
	HexHalfResult pos;
	SpellResearch data;

	ResearchNode(ResearchTree tree, Holder<HexGraphData> hex) {
		this.tree = tree;
		this.hex = hex;
		this.id = hex.unwrapKey().orElseThrow().location();
		data = tree.data.get(id);
	}

	public void add(ResearchDependency.Type type, ResearchNode node) {
		switch (type) {
			case MAIN -> {
				if (main != null) return;
				main = node;
			}
			case BRANCH -> {
				if (branch != null) return;
				branch = node;
			}
			case NEXT -> {
				if (next != null) return;
				next = node;
			}
		}
	}

	public void init(int x, int y) {
		pos = new HexHalfResult(y, x);
		if (tree.locatedNodes.containsKey(pos)) return;
		tree.locatedNodes.put(pos, this);
		if (main != null) {
			main.init(x + 1, y);
		}
		if (branch != null) {
			branch.init(x, y + 1);
		}
		if (next != null) {
			next.init(x + 1, y - 1);
		}
	}

	public double getX() {
		return tree.getX(pos.row(), pos.cell());
	}

	public double getY() {
		return tree.getY(pos.row(), pos.cell());
	}

	public Item getIcon() {
		return hex.value().icon();
	}

	public boolean unlocked() {
		return data.usable();
	}
}
