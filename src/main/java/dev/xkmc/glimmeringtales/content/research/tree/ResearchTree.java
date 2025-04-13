package dev.xkmc.glimmeringtales.content.research.tree;

import dev.xkmc.glimmeringtales.content.research.core.HexGraphData;
import dev.xkmc.glimmeringtales.content.research.core.PlayerResearch;
import dev.xkmc.glimmeringtales.content.research.core.ResearchDependency;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResearchTree {

	private final Player player;
	private final PlayerResearch data;

	private Node root;
	private final Map<ResourceLocation, Node> allNodes = new LinkedHashMap<>();

	public ResearchTree(Player player, PlayerResearch data) {
		this.player = player;
		this.data = data;
	}

	public void init() {
		allNodes.clear();
		root = null;
		var reg = player.level().registryAccess().registryOrThrow(GTRegistries.GRAPH);
		for (var e : reg.holders().toList()) {
			var node = addNode(e);
			if (e.value().parent() == null) {
				if (root == null)
					root = node;
			}
		}
		if (root == null) return;
		root.init(0, 0);
	}

	private Node addNode(Holder<HexGraphData> hex) {
		var node = new Node(hex);
		allNodes.put(node.id, node);
		var par = hex.value().parent();
		if (par != null) {
			var parId = par.parent().unwrapKey().orElseThrow().location();
			var parNode = allNodes.get(parId);
			if (parNode != null) {
				parNode.add(par.type(), node);
			}
		}
		return node;
	}

	private static class Node {

		private final Holder<HexGraphData> hex;
		private final ResourceLocation id;
		private Node main, branch, next;
		private int x, y;

		private Node(Holder<HexGraphData> hex) {
			this.hex = hex;
			this.id = hex.unwrapKey().orElseThrow().location();
		}

		public void add(ResearchDependency.Type type, Node node) {
			switch (type) {
				case MAIN -> {
					if (main == null) return;
					main = node;
				}
				case BRANCH -> {
					if (branch == null) return;
					branch = node;
				}
				case NEXT -> {
					if (next == null) return;
					next = node;
				}
			}
		}

		public void init(int x, int y) {
			this.x = x;
			this.y = y;
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

	}

}
