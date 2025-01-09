package dev.xkmc.glimmeringtales.content.item.curio;

import dev.xkmc.l2damagetracker.contents.damage.DamageState;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class DamageTypeCurioItem extends AttributeCurioItem {

	private final Factory state;
	private final Supplier<MutableComponent> lang;

	public DamageTypeCurioItem(Properties properties, Factory state, Supplier<MutableComponent> lang) {
		super(properties);
		this.state = state;
		this.lang = lang;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
		list.add(lang.get().withStyle(ChatFormatting.GOLD));
	}

	@Nullable
	public DamageState getState(Holder<DamageType> type) {
		return state.getState(type);
	}

	public interface Factory {

		@Nullable
		DamageState getState(Holder<DamageType> type);

	}

}
