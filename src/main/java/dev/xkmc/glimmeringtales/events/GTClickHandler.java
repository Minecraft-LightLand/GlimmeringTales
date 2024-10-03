package dev.xkmc.glimmeringtales.events;

import dev.xkmc.glimmeringtales.content.item.rune.BaseRuneItem;
import dev.xkmc.glimmeringtales.content.research.core.OpenGraphPacket;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2core.util.Proxy;
import dev.xkmc.l2core.util.ServerProxy;
import dev.xkmc.l2menustacker.click.ReadOnlyStackClickHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class GTClickHandler extends ReadOnlyStackClickHandler {

	public GTClickHandler(ResourceLocation rl) {
		super(rl);
	}

	@Override
	protected void handle(ServerPlayer player, ItemStack stack) {
		var reg = ServerProxy.getRegistryAccess();
		if (reg != null && stack.getItem() instanceof BaseRuneItem rune) {
			var spell = rune.getSpellInfo(reg).spell();
			if (spell != null && spell.value().graph() != null) {
				var id = spell.unwrapKey().orElseThrow().location();
				GlimmeringTales.HANDLER.toClientPlayer(new OpenGraphPacket(id), player);
			}
		}
	}

	@Override
	public boolean isAllowed(ItemStack stack) {
		var reg = ServerProxy.getRegistryAccess();
		if (reg != null && stack.getItem() instanceof BaseRuneItem rune) {
			var spell = rune.getSpellInfo(reg).spell();
			return spell != null && spell.value().graph() != null;
		}
		return false;
	}
}
