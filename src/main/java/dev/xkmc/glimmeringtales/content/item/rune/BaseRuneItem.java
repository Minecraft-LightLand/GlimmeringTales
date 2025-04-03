package dev.xkmc.glimmeringtales.content.item.rune;

import dev.xkmc.glimmeringtales.content.core.spell.SpellInfo;
import dev.xkmc.glimmeringtales.content.item.wand.IWandCoreItem;
import dev.xkmc.glimmeringtales.init.data.GTConfigs;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public abstract class BaseRuneItem extends Item implements IWandCoreItem {

	public BaseRuneItem(Properties properties) {
		super(properties);
	}

	@Override
	public int entityTrace() {
		return GTConfigs.SERVER.wandInteractionDistance.get();
	}

	@Override
	public SpellInfo getSpellInfo(Player player) {
		return getSpellInfo(player.level().registryAccess());
	}

	public abstract SpellInfo getSpellInfo(RegistryAccess access);

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		var level = ctx.level();
		if (level == null) return;
		getSpellInfo(level.registryAccess()).runeItemDesc(level, list, flag.hasShiftDown());
	}

	public ModelResourceLocation model() {
		return ModelResourceLocation.standalone(BuiltInRegistries.ITEM.getKey(this).withPath(e -> "item/" + e + "_core"));
	}

}
