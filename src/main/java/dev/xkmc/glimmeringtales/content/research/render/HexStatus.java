package dev.xkmc.glimmeringtales.content.research.render;

import dev.xkmc.glimmeringtales.content.research.core.PlayerResearch;
import dev.xkmc.glimmeringtales.content.research.tree.ResearchTree;
import dev.xkmc.glimmeringtales.content.research.tree.ResearchTreeScreen;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Locale;

public class HexStatus {

	public enum Save implements GTLang.EnumLang {
		YES(ChatFormatting.GREEN), NO(ChatFormatting.RED);

		public final ChatFormatting col;

		Save(ChatFormatting col) {
			this.col = col;
		}

		public String defText() {
			return this == YES ? "Saved" : "Not Saved";
		}

		public int getColor() {
			return col.getColor();
		}

	}

	public enum Compile implements GTLang.EnumLang {
		COMPLETE(ChatFormatting.GREEN),
		FAILED(ChatFormatting.DARK_PURPLE),
		EDITING(ChatFormatting.BLUE),
		ERROR(ChatFormatting.RED);

		public final ChatFormatting col;

		Compile(ChatFormatting col) {
			this.col = col;
		}

		public int getColor() {
			return col.getColor();
		}

		@Override
		public String defText() {
			return name().toLowerCase(Locale.ROOT);
		}
	}

	public static void openEditor(Player player, ResourceLocation id) {
		var data = PlayerResearch.of(player);
		var research = data.get(id);
		if (research == null) {
			Minecraft.getInstance().setScreen(new ResearchTreeScreen(new ResearchTree(player, data)));
			return;
		}
		Minecraft.getInstance().setScreen(new MagicHexScreen(research));
	}

}
