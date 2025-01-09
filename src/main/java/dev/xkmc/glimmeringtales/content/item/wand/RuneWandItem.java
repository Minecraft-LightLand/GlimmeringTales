package dev.xkmc.glimmeringtales.content.item.wand;

import com.tterrag.registrate.util.CreativeModeTabModifier;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import dev.xkmc.l2backpack.content.quickswap.common.SimpleMenuPvd;
import dev.xkmc.l2backpack.content.quickswap.common.SingleSwapItem;
import dev.xkmc.l2backpack.content.quickswap.common.SingleSwapToken;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapType;
import dev.xkmc.l2library.content.raytrace.FastItem;
import dev.xkmc.l2library.content.raytrace.IGlowingTarget;
import dev.xkmc.l2library.content.raytrace.RayTraceUtil;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2menustacker.init.L2MSLangData;
import dev.xkmc.l2menustacker.screen.source.PlayerSlot;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RuneWandItem extends SingleSwapItem implements IGlowingTarget, FastItem {

	public static WandHandleItem getHandle(ItemStack stack) {
		var item = GTItems.WAND_HANDLE.getOrDefault(stack, GTItems.WOOD_WAND.item());
		return item.value() instanceof WandHandleItem handle ? handle : GTItems.WOOD_WAND.get();
	}

	public static ItemStack getCore(ItemStack stack) {
		return getItems(stack).get(getSelected(stack));
	}

	public RuneWandItem(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public IQuickSwapToken<?> getTokenOfType(ItemStack stack, LivingEntity le, QuickSwapType type) {
		return type == GTRegistries.SWAP ? new SingleSwapToken(this, stack, type) : null;
	}

	@Override
	public boolean isValidContent(ItemStack stack) {
		return stack.getItem() instanceof IWandCoreItem;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (isSelected && level.isClientSide() && entity instanceof Player player) {
			RayTraceUtil.clientUpdateTarget(player, getDistance(stack));
		}
	}

	@Override
	public int getDistance(ItemStack stack) {
		return getCore(stack).getItem() instanceof IWandCoreItem item ? item.entityTrace() : 0;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity e) {
		return 72000;
	}

	@Override
	public boolean isFast(ItemStack itemStack) {
		return true;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		var spell = getSpell(stack, level);
		if (spell != null) {
			if (spell.castType() == SpellCastType.INSTANT) {
				if (castSpell(stack, level, player, spell, 0, false)) {
					return InteractionResultHolder.consume(stack);
				} else {
					return InteractionResultHolder.fail(stack);
				}
			} else {
				if (checkMana(stack, level, player, spell, 0, spell.castType() == SpellCastType.CHARGE)) {
					return ItemUtils.startUsingInstantly(level, player, hand);
				}
			}
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remain) {
		super.onUseTick(level, user, stack, remain);
		var spell = getSpell(stack, level);
		if (spell != null) {
			boolean pass = false;
			if (spell.castType() == SpellCastType.CONTINUOUS) {
				pass = castSpell(stack, level, user, spell, getUseDuration(stack, user) - remain, false);
			}
			if (spell.castType() == SpellCastType.CHARGE) {
				pass = castSpell(stack, level, user, spell, getUseDuration(stack, user) - remain, true);
			}
			if (!pass) {
				user.useItemRemaining = 0;
				user.stopUsingItem();
			}
		}
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int remain) {
		var spell = getSpell(stack, level);
		if (spell != null) {
			if (spell.castType() == SpellCastType.CHARGE) {
				castSpell(stack, level, user, spell, getUseDuration(stack, user) - remain, false);
			}
		}
	}

	@Nullable
	private ISpellHolder getSpell(ItemStack stack, Level level) {
		var sel = getCore(stack);
		return sel.getItem() instanceof IWandCoreItem item ? item.getSpell(sel, level) : null;
	}

	private boolean castSpell(ItemStack stack, Level level, LivingEntity user, ISpellHolder spell, int useTick, boolean charging) {
		return spell.cast(SpellCastContext.of(level, user, stack), useTick, charging);
	}

	private boolean checkMana(ItemStack stack, Level level, LivingEntity user, ISpellHolder spell, int useTick, boolean charging) {
		return spell.cast(SpellCastContext.simulate(level, user, stack), useTick, charging);
	}

	public void fillCreativeTabs(CreativeModeTabModifier x) {
		for (var e : GTItems.HANDLES) {
			x.accept(GTItems.WAND_HANDLE.set(getDefaultInstance(), e.builtInRegistryHolder()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		}
	}

	@Override
	public Component getName(ItemStack stack) {
		var handle = getHandle(stack);
		return Component.translatable(handle.getDescriptionId()).append(super.getName(stack));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		var level = ctx.level();
		if (level != null) {
			ItemStack core = getCore(stack);
			if (!core.isEmpty()) {
				list.add(GTLang.TOOLTIP_SELECTED.get(core.getHoverName()).withStyle(ChatFormatting.GRAY));
			}
			var handle = getHandle(stack);
			list.add(Component.translatable(handle.getDescriptionId()).append(": "));
			handle.appendAffinityDesc(level, list);
		}
		list.add(L2MSLangData.QUICK_ACCESS.get().withStyle(ChatFormatting.GRAY));
	}

	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.MAINHAND;
	}

	public boolean canEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity) {
		return armorType == EquipmentSlot.MAINHAND;
	}

	public void open(ServerPlayer player, PlayerSlot<?> slot, ItemStack stack) {
		new SimpleMenuPvd(player, slot, this, stack, WandMenu::new).open();
	}

}
