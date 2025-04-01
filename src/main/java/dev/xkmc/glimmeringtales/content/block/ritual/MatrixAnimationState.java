package dev.xkmc.glimmeringtales.content.block.ritual;

import dev.xkmc.glimmeringtales.content.item.rune.SpellCoreItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class MatrixAnimationState {

	private static final int MIN_SPEED = 4, MAX_SPEED = 44;
	private static final float W = 0.75f;

	private final List<ItemStack> cachedStacks = new ArrayList<>();

	private int speed = MIN_SPEED, nextSpeed = MIN_SPEED;
	private float angle = 0;
	private boolean prevState = false;

	public float getTime(float pTick) {
		int diff = nextSpeed - speed;
		return angle + W * speed * pTick * (1 + diff * pTick / 2f);
	}

	public boolean tick(boolean crafting) {
		boolean change = prevState != crafting;
		int diff = nextSpeed - speed;
		angle += W * speed * (1 + diff / 2f);
		speed = nextSpeed;
		nextSpeed = crafting ? Math.min(MAX_SPEED, speed + 1) : Math.max(MIN_SPEED, speed - 1);
		prevState = crafting;
		return change;
	}

	public void setup(List<ItemStack> list) {
		cachedStacks.clear();
		cachedStacks.addAll(list);
	}

	public void release(Level level, BlockPos pos, NatureCoreBlockEntity be) {
		int col = -1;
		if (!cachedStacks.isEmpty() && cachedStacks.getFirst().getItem() instanceof SpellCoreItem item) {
			var aff = item.getAffinity(level);
			if (aff != null && aff.affinity().size() == 1) {
				var e = new ArrayList<>(aff.affinity().entrySet()).getFirst().getKey();
				col = e.getColor();
			}
		}
		var center = pos.above().getCenter();
		MatrixParticleHelper.complete(level, center, col);
		cachedStacks.clear();
	}

}
