package dev.xkmc.glimmeringtales.content.item.wand;

import dev.xkmc.glimmeringtales.content.core.spell.ElementAffinity;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WandHandleItem extends Item {

	public WandHandleItem(Properties prop) {
		super(prop);
	}

	public ModelResourceLocation model() {
		return ModelResourceLocation.standalone(BuiltInRegistries.ITEM.getKey(this).withPath(e -> "item/" + e + "_handle"));
	}

	public ModelResourceLocation icon() {
		return ModelResourceLocation.standalone(BuiltInRegistries.ITEM.getKey(this).withPath(e -> "item/" + e + "_icon"));
	}


	public WandData data(@Nullable RegistryAccess access) {
		if (access == null) return WandData.DEF;
		var ans = GTRegistries.WAND_MODEL.get(access, builtInRegistryHolder());
		return ans == null ? WandData.DEF : ans;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		var level = ctx.level();
		if (level == null) return;
		appendAffinityDesc(level, list);
	}

	public void appendAffinityDesc(Level level, List<Component> list) {
		var aff = getAffinity(level);
		if (aff == null) return;
		for (var e : aff.affinity().entrySet()) {
			Component val = Component.literal("+" + (int) Math.round(e.getValue() * 100) + "%")
					.withStyle(ChatFormatting.BLUE);
			list.add(GTLang.TOOLTIP_AFFINITY.get(e.getKey().coloredDesc(), val).withStyle(ChatFormatting.GRAY));
		}
	}

	@Nullable
	public ElementAffinity getAffinity(Level level) {
		return GTRegistries.AFFINITY.get(level.registryAccess(), builtInRegistryHolder());
	}

	public ResourceLocation getFrame() {
		return BuiltInRegistries.ITEM.getKey(this);
	}

}
