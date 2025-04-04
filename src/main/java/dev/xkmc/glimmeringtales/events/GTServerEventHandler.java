package dev.xkmc.glimmeringtales.events;

import dev.xkmc.glimmeringtales.content.block.api.CropGrowListener;
import dev.xkmc.glimmeringtales.content.entity.hostile.MobCastingConfig;
import dev.xkmc.glimmeringtales.content.entity.hostile.SpellCastGoal;
import dev.xkmc.glimmeringtales.content.recipe.thunder.StrikeBlockRecipe;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;

@EventBusSubscriber(modid = GlimmeringTales.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GTServerEventHandler {

	@SubscribeEvent
	public static void onCropGrow(CropGrowEvent.Post event) {
		if (!(event.getLevel() instanceof ServerLevel level)) return;
		for (var e : Direction.values()) {
			if (e.getAxis() == Direction.Axis.Y) continue;
			var pos = event.getPos().relative(e);
			var state = level.getBlockState(pos);
			if (state.getBlock() instanceof CropGrowListener block) {
				block.onNeighborGrow(level, state, pos, event.getState());
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityStruck(EntityStruckByLightningEvent event) {
		if (event.getEntity() instanceof ItemEntity ie) {
			var level = ie.level();
			var cont = new SingleRecipeInput(ie.getItem());
			var ans = level.getRecipeManager()
					.getRecipeFor(GTRecipes.RT_STRIKE_ITEM.get(), cont, level);
			ans.ifPresent(holder -> ie.setItem(holder.value().assemble(cont, level.registryAccess())));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onEntityAdded(EntityJoinLevelEvent event) {
		var level = event.getLevel();
		if (level.isClientSide()) return;
		if (event.getEntity() instanceof LightningBolt ie)
			onThunder(level, ie);
		if (event.getEntity() instanceof Mob mob && mob.getType().is(GTTagGen.CASTER)) {
			for (var e : mob.goalSelector.getAvailableGoals()) {
				if (e.getGoal() instanceof RangedAttackGoal ||
						e.getGoal() instanceof MeleeAttackGoal ||
						e.getGoal() instanceof RangedBowAttackGoal<?> ||
						e.getGoal() instanceof RangedCrossbowAttackGoal<?>) {
					mob.goalSelector.addGoal(e.getPriority() - 1, new SpellCastGoal(mob, MobCastingConfig.DEF));
					return;
				}
			}
		}
	}

	private static void onThunder(Level level, LightningBolt ie) {
		BlockPos pos = BlockPos.containing(ie.position().add(0, -1e-6, 0));
		BlockState state = level.getBlockState(pos);
		if (state.isAir()) level.getBlockState(pos = pos.below());
		BlockPos hitPos;
		BlockState hitState;
		if (state.is(Blocks.LIGHTNING_ROD)) {
			hitPos = pos.relative(state.getValue(LightningRodBlock.FACING).getOpposite());
			hitState = level.getBlockState(hitPos);
		} else {
			hitPos = pos;
			hitState = state;
		}
		var cont = new StrikeBlockRecipe.Inv(level, hitState, hitPos);
		var ans = level.getRecipeManager().getRecipeFor(GTRecipes.RT_STRIKE_BLOCK.get(), cont, level);
		if (ans.isEmpty()) return;
		ans.get().value().assemble(cont, level.registryAccess());
	}

}
