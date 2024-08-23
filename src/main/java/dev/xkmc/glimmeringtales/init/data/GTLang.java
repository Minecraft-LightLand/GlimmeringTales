package dev.xkmc.glimmeringtales.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum GTLang {
	TOOLTIP_FILL("tooltip.fill_crystal", "Progress: %s / %s", 2),
	TOOLTIP_BLOCK("tooltip.consume_block", "Right click %s to absorb essence", 1),
	TOOLTIP_VINE("tooltip.nearby_grow", "Grows when adjacent crops grow to maturity naturally", 0),
	TOOLTIP_AFFINITY("tooltip.affinity", "[%s] affinity: %s", 2),
	TOOLTIP_SPELL("tooltip.spell", "%s - [%s]", 2),
	TOOLTIP_COST("tooltip.cost", "Spell cost: %s", 1),
	TOOLTIP_COST_CONT("tooltip.cost_cont", "Spell cost: %s per tick", 1),
	TOOLTIP_COST_CAPPED("tooltip.cost_capped", "Spell cost: %s per tick, at most %s", 2),

	JEI_STRIKE_ITEM("jei.strike_item", "Lightning Strikes Item", 0),
	JEI_STRIKE_BLOCK("jei.strike_block", "Lightning Strikes Block", 0),
	JEI_TRANSFORM("jei.transform", "Item Special Transformation", 0),
	JEI_RITUAL("jei.ritual", "Ritual", 0),
	;

	final String id, def;
	final int count;

	GTLang(String id, String def, int count) {
		this.id = GlimmeringTales.MODID + "." + id;
		this.def = def;
		this.count = count;
	}

	public MutableComponent get(Object... objs) {
		if (objs.length != this.count) {
			throw new IllegalArgumentException("for " + this.name() + ": expect " + this.count + " parameters, got " + objs.length);
		} else {
			return Component.translatable(this.id, objs);
		}
	}

	public static void addTranslations(RegistrateLangProvider pvd) {
		for (var e : values()) {
			pvd.add(e.id, e.def);
		}
	}

}
