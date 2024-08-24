package dev.xkmc.glimmeringtales.content.capability;

import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2core.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

@SerialClass
public class PlayerManaCapability extends PlayerCapabilityTemplate<PlayerManaCapability> {

	@SerialField
	private double mana;

	@Override
	public void tick(Player player) {
		if (player instanceof ServerPlayer sp && sp.tickCount % 20 == 0) {
			double max = sp.getAttributeValue(GTRegistries.MAX_MANA);
			double regen = sp.getAttributeValue(GTRegistries.MANA_REGEN);
			if (mana >= max) mana = max;
			else {
				if (!(mana >= 0)) mana = 0;
				mana = Math.min(max, mana + regen);
				GTRegistries.MANA.type().network.toClient(sp);
			}
		}
	}

	public double getMana() {
		return mana;
	}

	public boolean consume(Player player, double consume) {
		if (consume > mana) {
			return false;
		}
		mana -= consume;
		if (player instanceof ServerPlayer sp)
			GTRegistries.MANA.type().network.toClient(sp);
		return true;
	}

}