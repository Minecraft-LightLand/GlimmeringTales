package dev.xkmc.glimmeringtales.content.research.core;

import dev.xkmc.glimmeringtales.content.capability.PlayerResearchCapability;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerResearch {

	public static PlayerResearch of(Player player) {
		return new PlayerResearch(player, GTRegistries.RESEARCH.type().getOrCreate(player));
	}

	private final Player player;
	private final PlayerResearchCapability data;

	private final Map<ResourceLocation, SpellResearch> cache = new LinkedHashMap<>();
	private Set<ResourceLocation> validSpells;

	public PlayerResearch(Player player, PlayerResearchCapability data) {
		this.player = player;
		this.data = data;
	}

	public Player player() {
		return player;
	}

	@Nullable
	public SpellResearch get(ResourceLocation id) {
		ResearchData dat = data.get(id);
		return get(id, dat);
	}

	@Nullable
	public SpellResearch get(ResourceLocation id, @Nullable ResearchData dat) {
		var reg = player.level().registryAccess().registryOrThrow(GTRegistries.SPELL);
		if (validSpells == null) {
			validSpells = reg.holders()
					.filter(e -> e.value().graph() != null).map(e -> e.key().location())
					.collect(Collectors.toCollection(LinkedHashSet::new));
		}
		if (!validSpells.contains(id))
			return null;
		if (cache.containsKey(id)) {
			return cache.get(id);
		}
		var spell = reg.get(id);
		if (spell == null) return null;
		var graph = spell.graph();
		if (graph == null) return null;
		if (dat == null) dat = ResearchData.create(graph.map().size());
		var map = HexGraph.create(id, graph.map(), graph.flows());
		SpellResearch ans = new SpellResearch(this, id, dat, map);
		cache.put(id, ans);
		return ans;
	}

	protected void save(ResourceLocation id, ResearchData dat) {
		if (player instanceof ServerPlayer sp) {
			var research = get(id);
			if (research != null) {
				//var handler = research.getSolution();
				//var flow = handler.getMatrix(true);
				//var order = dat.order();
				//var graph = research.getGraph();
				//TODO verify
				//if (order.check(handler, flow, graph, new boolean[6], new boolean[6]))
				data.put(id, dat);
			}
			data.sync(sp);
		} else {
			data.put(id, dat);
			GlimmeringTales.HANDLER.toServer(new GraphToServerPacket(id, dat));
		}
	}

}
