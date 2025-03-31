package dev.xkmc.glimmeringtales.content.block.crop;

import dev.xkmc.glimmeringtales.init.data.GTLang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class PopFruitClient {

	public static void appendBiomeInfo(List<Component> list, Level level, NaturalPopFruit pop) {
		Player player = Minecraft.getInstance().player;
		if (player == null) return;
		if (level.getBiome(player.blockPosition()).is(pop.getBiomeTag())) {
			list.add(GTLang.BIOME_CORRECT.get().withStyle(ChatFormatting.YELLOW));
		} else {
			list.add(GTLang.BIOME_WRONG.get().withStyle(ChatFormatting.DARK_AQUA));
		}
	}

}
