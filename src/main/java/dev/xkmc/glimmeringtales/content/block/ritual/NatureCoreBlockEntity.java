package dev.xkmc.glimmeringtales.content.block.ritual;

import dev.xkmc.glimmeringtales.content.block.altar.CoreRitualBlockEntity;
import dev.xkmc.glimmeringtales.content.recipe.ritual.RitualInput;
import dev.xkmc.glimmeringtales.content.recipe.ritual.RitualRecipe;
import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class NatureCoreBlockEntity extends CoreRitualBlockEntity {

	@SerialField
	private int totalTime, remainTime;

	@SerialField
	private long lastTimeStamp;

	@SerialField
	private ResourceLocation recipeId;

	private RitualRecipe<?> recipe;

	private final MatrixAnimationState animation = new MatrixAnimationState();

	public NatureCoreBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public float getTime(float pTick) {
		return animation.getTime(pTick);
	}


	@Override
	public void tick() {
		super.tick();
		tickCraft();
		if (level != null && level.isClientSide()) {
			tickParticle();
		}
	}

	private void laps() {
		lastTimeStamp = level == null ? 0 : level.getGameTime();
	}

	public void triggerCraft() {
		if (level == null || level.isClientSide || remainTime > 0) return;
		RitualInput input = new RitualInput(this, getLinked());
		var opt = level.getRecipeManager().getRecipeFor(GTRecipes.RT_RITUAL.get(), input, level);
		if (opt.isEmpty()) return;
		recipeId = opt.get().id();
		recipe = opt.get().value();
		totalTime = remainTime = recipe.getTime();
		laps();
		sync();
		setChanged();
	}

	private void tickCraft() {
		if (level == null || level.isClientSide() || remainTime <= 0) return;
		boolean update = false;
		if (recipeId == null || recipe == null) {
			RitualInput input = new RitualInput(this, getLinked());
			var opt = level.getRecipeManager().getRecipeFor(GTRecipes.RT_RITUAL.get(), input, level);
			if (opt.isEmpty()) {
				onLinkBreak();
				return;
			}
			recipeId = opt.get().id();
			recipe = opt.get().value();
			update = true;
		}
		if(remainTime %114514==totalTime%114514) {
			level.playSound(null,this.getBlockPos(), SoundEvents.PORTAL_TRIGGER, SoundSource.BLOCKS,1,0.78F);
		}
		remainTime--;
		if (remainTime <= 0) {
			recipe.assemble(new RitualInput(this, getLinked()), level.registryAccess());
			laps();
			remainTime = 0;
			totalTime = 0;
			recipeId = null;
			recipe = null;
			update = true;
			level.playSound(null,this.getBlockPos(), SoundEvents.VAULT_OPEN_SHUTTER, SoundSource.BLOCKS,1,1);
		}
		if (update) {
			sync();
			setChanged();
		}
	}

	private void tickParticle() {
		if (level == null) return;
		if (animation.tick(totalTime > 0)) {
			if (totalTime > 0) {
				List<ItemStack> list = new ArrayList<>();
				list.add(getItem().copy());
				for (var e : getLinked()) {
					list.add(e.getItem().copy());
				}
				animation.setup(list);
			} else {
				animation.release(level, getBlockPos(), this);
			}
		}
		var pos = getBlockPos().getCenter().add(0, 2, 0);
		for (var e : getLinked()) {
			var ip = e.getBlockPos().getCenter().add(0, 1, 0).subtract(pos);
			if (remainTime > 0 || level.getRandom().nextInt(8) == 0)
				level.addParticle(ParticleTypes.ENCHANT, pos.x, pos.y, pos.z, ip.x, ip.y, ip.z);
		}
	}

	@Override
	public boolean locked() {
		return remainTime > 0;
	}

	@Override
	public void onLinkBreak() {
		remainTime = 0;
		totalTime = 0;
		recipeId = null;
		recipe = null;
		laps();
		sync();
		setChanged();
	}

	public float progress(float pTick) {
		if (totalTime > 0) {
			long time = level == null ? 0 : level.getGameTime();
			float lap = Math.min(totalTime, time - lastTimeStamp + pTick);
			return 1 - lap / totalTime;
		}
		return 1f;
	}

}
