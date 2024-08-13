package dev.xkmc.glimmeringtales.init.data.spell;

import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;

public abstract class NatureSpellEntry extends SpellDataGenEntry {

	protected static DataGenCachedHolder<SpellAction> spell(ResourceLocation id) {
		return new DataGenCachedHolder<>(ResourceKey.create(EngineRegistry.SPELL, id));
	}

	protected static DataGenCachedHolder<ProjectileConfig> projectile(ResourceLocation id) {
		return new DataGenCachedHolder<>(ResourceKey.create(EngineRegistry.PROJECTILE, id));
	}

	protected static DataGenCachedHolder<NatureSpell> nature(ResourceLocation id) {
		return new DataGenCachedHolder<>(ResourceKey.create(GTRegistries.SPELL, id));
	}

	public abstract void regNature(BootstrapContext<NatureSpell> ctx);

	public abstract void regBlock(DataMapProvider.Builder<BlockSpell, Block> builder);

}