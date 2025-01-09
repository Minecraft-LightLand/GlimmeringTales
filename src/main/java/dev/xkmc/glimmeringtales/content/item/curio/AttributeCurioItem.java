package dev.xkmc.glimmeringtales.content.item.curio;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;

public class AttributeCurioItem extends GTCurioItem {

	public AttributeCurioItem(Properties properties) {
		super(properties);
	}

	private Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(RegistryAccess access, ResourceLocation id, ItemStack stack) {
		Multimap<Holder<Attribute>, AttributeModifier> ans = LinkedHashMultimap.create();
		var attrs = GTRegistries.ITEM_ATTR.get(access, stack.getItemHolder());
		if (attrs == null) return ans;
		for (var e : attrs.list()) {
			ans.put(e.attribute(), new AttributeModifier(id, e.value(), e.operation()));
		}
		return ans;
	}

	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext ctx, ResourceLocation id, ItemStack stack) {
		return getAttributeModifiers(getAccess(ctx.entity()), id, stack);
	}

	@Nullable
	private static RegistryAccess getAccess(@Nullable LivingEntity e) {
		if (e != null) {
			return e.level().registryAccess();
		}
		if (FMLEnvironment.dist == Dist.CLIENT) {
			var level = Minecraft.getInstance().level;
			if (level != null)
				return level.registryAccess();
		}
		var server = ServerLifecycleHooks.getCurrentServer();
		if (server != null)
			return server.registryAccess();
		return null;
	}

}
