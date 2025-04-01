package dev.xkmc.glimmeringtales.content.core.searcher;

import dev.xkmc.glimmeringtales.content.item.tool.IBlockSearcher;
import dev.xkmc.l2core.base.effects.api.SimpleIcon;
import dev.xkmc.l2core.events.ClientEffectRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

public class BlockSearcher {

	public static void iterate(Player player, Consumer<BlockSearcher> cons) {
		Set<BlockSearcher> set = new HashSet<>();
		iterate(set, player.getMainHandItem(), cons);
		iterate(set, player.getOffhandItem(), cons);
	}

	private static void iterate(Set<BlockSearcher> set, ItemStack stack, Consumer<BlockSearcher> cons) {
		if (stack.getItem() instanceof IBlockSearcher item) {
			var s = item.getSearcher();
			if (set.contains(s)) return;
			set.add(s);
			cons.accept(s);
		}
	}

	private final IBlockSearcher source;
	private final Map<BlockPos, Entry> map = new LinkedHashMap<>();
	private final Block target;
	private final TagKey<Block> hint;
	private final int minY, maxY;
	private final int bonusChance, bonusTrial, br;
	private final IntSupplier radius, trial;

	public BlockSearcher(IBlockSearcher source, Block target, TagKey<Block> hint, int minY, int maxY, IntSupplier radius, IntSupplier trial, int bonusChance, int bonusTrial, int br) {
		this.source = source;
		this.target = target;
		this.hint = hint;
		this.minY = minY;
		this.maxY = maxY;
		this.radius = radius;
		this.trial = trial;
		this.bonusChance = bonusChance;
		this.bonusTrial = bonusTrial;
		this.br = br;
	}

	public void tick(Player player) {
		var level = player.level();
		var pos = player.blockPosition();
		RandomSource rand = RandomSource.create();
		BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
		int bonus = bonusChance;
		int trial = this.trial.getAsInt();
		int radius = this.radius.getAsInt();
		for (int i = 0; i < trial; i++) {
			rand(pos, p, radius, rand);
			var state = level.getBlockState(p);
			if (state.is(target)) {
				new Entry(level, p.immutable()).put();
			} else if (state.is(hint)) {
				if (bonus <= 0) continue;
				bonus--;
				var ip = p.immutable();
				search(level, ip, p, bonusTrial, br, rand);
			}
		}
		search(level, pos, p, trial / 4, 16, rand);
		if (Minecraft.getInstance().isPaused()) return;
		map.entrySet().removeIf(ent -> ent.getValue().shouldRemove(player, level));
	}

	private void search(Level level, BlockPos ip, BlockPos.MutableBlockPos p, int trial, int br, RandomSource rand) {
		for (int i = 0; i < trial; i++) {
			rand(ip, p, br, rand);
			BlockState state = level.getBlockState(p);
			if (state.is(target)) {
				new Entry(level, p.immutable()).put();
			}
		}
	}

	private void rand(BlockPos o, BlockPos.MutableBlockPos p, int r, RandomSource rand) {
		int bx = o.getX() + rand.nextIntBetweenInclusive(-r, r);
		int bz = o.getZ() + rand.nextIntBetweenInclusive(-r, r);
		int by = maxY < minY + r * 2 ? rand.nextIntBetweenInclusive(minY, maxY) :
				Math.clamp(o.getY(), minY + r, maxY - r) + rand.nextIntBetweenInclusive(-r, r);
		p.set(bx, by, bz);
	}

	public void render() {
		for (var e : map.keySet()) {
			ClientEffectRenderEvents.addIcon(SimpleIcon.of(source.id(), e.getCenter()));
		}
	}

	public int count() {
		return map.size();
	}

	private class Entry {

		private final Level level;
		private final BlockPos pos;
		private int life = 600;

		private Entry(Level level, BlockPos pos) {
			this.level = level;
			this.pos = pos;
		}

		public void put() {
			if (map.size() >= 99) return;
			map.put(pos, this);
		}

		public boolean shouldRemove(Player player, Level level) {
			if (level != this.level) return true;
			var p = player.blockPosition();
			int rad = radius.getAsInt();
			if (pos.distSqr(new BlockPos(p.getX(), pos.getY(), p.getZ())) > rad * rad) return true;
			if (!level.getBlockState(pos).is(target)) return true;
			life--;
			return life <= 0;
		}
	}

}
