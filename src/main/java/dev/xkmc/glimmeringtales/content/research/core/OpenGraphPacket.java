package dev.xkmc.glimmeringtales.content.research.core;

import dev.xkmc.glimmeringtales.content.research.client.graph.HexStatus;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record OpenGraphPacket(
		ResourceLocation id
) implements SerialPacketBase<OpenGraphPacket> {

	@Override
	public void handle(Player player) {
		HexStatus.openEditor(player, id);
	}

}
