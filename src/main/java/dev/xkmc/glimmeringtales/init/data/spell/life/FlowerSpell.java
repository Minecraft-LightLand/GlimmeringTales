package dev.xkmc.glimmeringtales.init.data.spell.life;

import dev.xkmc.glimmeringtales.content.core.analysis.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.engine.processor.EffectCloudInstance;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;

public class FlowerSpell  {
	public static final NatureSpellBuilder BUILDER = GTRegistries.LIFE.get()
			.build(GlimmeringTales.loc("flower")).cost(40)
			.spell(ctx -> NatureSpellEntry.ofBlock(flower(ctx),Items.ALLIUM, 1030))
			.block((b, e) -> b.add(BlockTags.FLOWERS, new BlockSpell(e, true, 1)))
			.lang("Flower").desc(
					"[Block] Release of healing stasis potion",
					"Release of healing stasis potion",
					SpellTooltipData.of(
					)
			);

	private static ConfiguredEngine<?> flower(NatureSpellBuilder ctx) {
		return new EffectCloudInstance(
				Potions.HEALING,
				DoubleVariable.of("3"),
				IntVariable.of("10")
		);
	}

}