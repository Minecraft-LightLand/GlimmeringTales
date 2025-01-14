package dev.xkmc.glimmeringtales.content.block.infuser;

import dev.xkmc.glimmeringtales.content.item.rune.SpellCoreItem;
import dev.xkmc.glimmeringtales.content.recipe.infuse.InfuseRecipe;
import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2core.base.tile.BaseContainerListener;
import dev.xkmc.l2modularblock.tile_api.BlockContainer;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SerialClass
public class InfuserBlockEntity extends BaseBlockEntity
		implements TickableBlockEntity, BaseContainerListener, BlockContainer {

	@SerialField
	protected final InfuserItemContainer items = new InfuserItemContainer(2).setMax(1).add(this);

	private final IItemHandler handler;

	@SerialField
	private int totalDuration, remainingTime;
	@SerialField
	private ResourceLocation recipeId;

	private RecipeHolder<InfuseRecipe<?>> recipe;

	private boolean doRecipeCheck = true;

	public InfuserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		handler = new InvWrapper(items);
	}

	@Override
	public void tick() {
		if (level == null) return;
		if (level.isClientSide()) {
			if (recipeId != null) {
				int col = -1;
				if (items.getItem(0).getItem() instanceof SpellCoreItem crystal)
					col = crystal.getColor(level) | 0xff000000;
				var opt = ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, col);
				Vec3 p = getBlockPos().getCenter();
				level.addParticle(opt, p.x, p.y - 0.3, p.z, 0, 0, 0);
			}
			return;
		}
		if (doRecipeCheck || recipeId != null && recipe == null) {
			updateRecipe();
		}
		if (recipe != null) {
			if (remainingTime > 0) remainingTime--;
			if (remainingTime <= 0) {
				ItemStack ans = recipe.value().assemble(items, level.registryAccess());
				items.setItem(1, ans);
				remainingTime = 0;
				totalDuration = 0;
				recipeId = null;
				recipe = null;
				doRecipeCheck = true;
				sync();
				setChanged();
			}
		}
	}

	private void updateRecipe() {
		doRecipeCheck = false;
		if (level == null) return;
		var recipe = level.getRecipeManager().getRecipeFor(GTRecipes.RT_INFUSE.get(), items, level);
		if (recipe.isEmpty()) {
			this.recipe = null;
			recipeId = null;
			totalDuration = 0;
			remainingTime = 0;
		} else {
			if (recipeId != null && recipe.get().id().equals(recipeId)) {
				this.recipe = recipe.get();
			} else {
				this.recipe = recipe.get();
				recipeId = recipe.get().id();
				remainingTime = totalDuration = recipe.get().value().time;
			}
		}
		sync();
		setChanged();
	}

	@Override
	public void notifyTile() {
		doRecipeCheck = true;
		sync();
		setChanged();
	}

	@Override
	public List<Container> getContainers() {
		return List.of(items);
	}

	public IItemHandler getItemHandler(@Nullable Direction dir) {
		return handler;
	}

}
