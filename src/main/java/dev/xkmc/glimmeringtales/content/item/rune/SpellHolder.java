package dev.xkmc.glimmeringtales.content.item.rune;

import dev.xkmc.glimmeringtales.content.core.spell.DefaultAffinity;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.content.item.wand.ISpellHolder;
import dev.xkmc.glimmeringtales.content.item.wand.SpellCastContext;
import dev.xkmc.l2magic.content.engine.context.SpellContext;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import net.minecraft.core.Holder;

public record SpellHolder(Holder<NatureSpell> spell, int dist) implements ISpellHolder {

	@Override
	public SpellCastType castType() {
		return spell.value().spell().value().castType();
	}

	@Override
	public boolean cast(SpellCastContext user, int useTick, boolean charging) {
		SpellContext ctx = SpellContext.castSpell(user.user(), spell.value().spell().value(), useTick, 1, dist, user.delay());
		if (ctx == null) return false;
		return execute(spell.value(), ctx, user, DefaultAffinity.INS, useTick, charging);
	}

}
