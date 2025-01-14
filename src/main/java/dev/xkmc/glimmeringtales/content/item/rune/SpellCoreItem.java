package dev.xkmc.glimmeringtales.content.item.rune;

import dev.xkmc.glimmeringtales.content.core.spell.ElementAffinity;
import dev.xkmc.glimmeringtales.content.core.spell.SpellInfo;
import dev.xkmc.glimmeringtales.content.item.materials.LightningImmuneItem;
import dev.xkmc.glimmeringtales.content.item.wand.SpellCastContext;
import dev.xkmc.glimmeringtales.init.data.GTConfigs;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpellCoreItem extends LightningImmuneItem implements IBlockSpellItem {

	public SpellCoreItem(Properties prop) {
		super(prop);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		var level = ctx.level();
		if (level == null) return;
		var aff = getAffinity(level);
		if (aff == null) return;
		for (var e : aff.affinity().entrySet()) {
			Component val = Component.literal((int) Math.round(e.getValue() * 100) + "%")
					.withStyle(ChatFormatting.BLUE);
			list.add(GTLang.TOOLTIP_AFFINITY.get(e.getKey().coloredDesc(), val).withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public int entityTrace() {
		return 0;
	}

	@Nullable
	public ElementAffinity getAffinity(Level level) {
		return GTRegistries.AFFINITY.get(level.registryAccess(), builtInRegistryHolder());
	}

	public int getColor(Level level) {
		var aff = getAffinity(level);
		if (aff != null && aff.affinity().entrySet().size() == 1) {
			var e = new ArrayList<>(aff.affinity().entrySet()).getFirst().getKey();
			return e.getColor();
		}
		return -1;
	}

	@Override
	public SpellInfo getSpellInfo(Player player) {
		var ctx = BlockSpellContext.blockSpellContext(player, range());
		if (ctx == null) return SpellInfo.EMPTY;
		var spell = GTRegistries.BLOCK.get(player.level().registryAccess(), ctx.state().getBlockHolder());
		if (spell == null) return SpellInfo.EMPTY;
		var nature = spell.spell().value();
		var aff = getAffinity(player.level());
		if (aff == null || !aff.affinity().containsKey(nature.elem())) return SpellInfo.EMPTY;
		return SpellInfo.ofBlock(spell, aff);
	}

	public boolean castSpell(SpellCastContext user) {
		var ctx = BlockSpellContext.blockSpellContext(user.user(), range());
		if (ctx == null) return false;
		var spell = GTRegistries.BLOCK.get(user.level().registryAccess(), ctx.state().getBlockHolder());
		if (spell == null) return false;
		var nature = spell.spell().value();
		var aff = getAffinity(user.level());
		if (aff == null || !aff.affinity().containsKey(nature.elem()))
			return false;
		if (!user.level().isClientSide()) {
			if (spell.breakBlock())
				user.level().removeBlock(ctx.pos(), false);
		}
		return execute(nature, ctx.ctx(), user, aff, 0, false);
	}

	public int range() {
		return GTConfigs.SERVER.wandInteractionDistance.get();
	}

	public ModelResourceLocation model() {
		return ModelResourceLocation.standalone(BuiltInRegistries.ITEM.getKey(this).withPath(e -> "item/" + e + "_core"));
	}

}
