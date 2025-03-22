package dev.xkmc.glimmeringtales.compat.misc;

import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

public class GTCurioCompat {

	public static <T> List<T> getAll(LivingEntity le, Class<T> item) {
		return CuriosApi.getCuriosInventory(le).map(e ->
				e.findCurios(stack -> item.isInstance(stack.getItem()))
						.stream().<T>map(i -> Wrappers.cast(i.stack().getItem())).toList()
		).orElse(List.of());
	}

	public static boolean has(LivingEntity le, Item item) {
		return CuriosApi.getCuriosInventory(le).flatMap(e -> e.findFirstCurio(item)).isPresent();
	}

}
