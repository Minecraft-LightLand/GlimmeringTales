package dev.xkmc.glimmeringtales.content.research.tree;

import dev.xkmc.glimmeringtales.content.research.core.PlayerResearch;
import dev.xkmc.glimmeringtales.content.research.logic.AbstractHex;
import dev.xkmc.glimmeringtales.content.research.logic.HexDirection;
import dev.xkmc.glimmeringtales.content.research.logic.HexHalfResult;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResearchTree extends AbstractHex {

	private final Player player;
	final PlayerResearch data;

	ResearchNode root;
	private final Map<ResourceLocation, ResearchNode> allNodes = new LinkedHashMap<>();
	final Map<HexHalfResult, ResearchNode> locatedNodes = new LinkedHashMap<>();

	public ResearchTree(Player player, PlayerResearch data) {
		this.player = player;
		this.data = data;
		init();
	}

	public void init() {
		allNodes.clear();
		locatedNodes.clear();
		root = null;
		var reg = player.level().registryAccess().registryOrThrow(GTRegistries.GRAPH);
		for (var e : reg.holders().toList()) {
			var node = new ResearchNode(this, e);
			allNodes.put(node.id, node);
			if (e.value().parent() == null) {
				if (root == null)
					root = node;
			}
		}
		for (var e : allNodes.values()) {
			var par = e.hex.value().parent();
			if (par != null) {
				var parId = par.parent().unwrapKey().orElseThrow().location();
				var parNode = allNodes.get(parId);
				if (parNode != null) {
					parNode.add(par.type(), e);
				}
			}
		}
		if (root == null) return;
		root.init(0, 0);
	}

	public @Nullable ResearchNode getElementOnHex(double x, double y) {
		// row number relative to center in rectangular grid
		int row = (int) Math.floor(y / HEIGHT + 0.5);
		// relative y coordinate of the point in rectangular grid
		double rel_y = y - row * HEIGHT;
		// cell number relative to center in rectangular grid
		int cell = (int) Math.floor(x / WIDTH + 0.5 - row * 0.5);
		// relative x coordinate of the point in rectangular grid
		double rel_x = x - (Math.abs(row) * 0.5 + cell) * WIDTH;

		double xoff = WIDTH / 4.0;
		double yoff = HEIGHT / 6.0;

		HexDirection dire = rel_y > 0 ?
				rel_x > 0 ? HexDirection.LOWER_RIGHT : HexDirection.LOWER_LEFT :
				rel_x > 0 ? HexDirection.UPPER_RIGHT : HexDirection.UPPER_LEFT;

		rel_x = Math.abs(rel_x) - xoff;
		rel_y = Math.abs(rel_y) - yoff * 2;
		if (rel_x > 0 && rel_y > 0 && rel_x / xoff + rel_y / yoff > 1) {
			cell += dire.getCellOffset(0, row, cell);
			row += dire.getRowOffset();
		}
		HexHalfResult pos = new HexHalfResult(row, cell);
		return locatedNodes.get(pos);
	}

	/**
	 * get the X position of a cell relative to the center
	 */
	public double getX(int row, int cell) {
		return cell * WIDTH + row * WIDTH / 2;
	}

	/**
	 * get the Y position of a cell relative to the center
	 */
	public double getY(int row, int cell) {
		return row * HEIGHT;
	}

}
