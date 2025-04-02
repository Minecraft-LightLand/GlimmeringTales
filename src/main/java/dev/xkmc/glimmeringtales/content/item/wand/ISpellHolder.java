package dev.xkmc.glimmeringtales.content.item.wand;

import dev.xkmc.glimmeringtales.content.core.spell.IAffinityProvider;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.l2magic.content.engine.context.SpellContext;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;

public interface ISpellHolder {

	SpellCastType castType();

	boolean cast(SpellCastContext user, int useTick, boolean charging);

	default boolean execute(NatureSpell spell, SpellContext ctx, SpellCastContext user, IAffinityProvider aff, int useTick, boolean charging) {
		double val = aff.getFinalAffinity(spell.elem(), user.user(), user.wand());
		if (spell.consumeMana(user.user(), user.wand(), val, useTick, charging, user.simulate())) {
			if (!user.level().isClientSide() && !user.simulate()) {
				if (charging && !spell.consumeMana(user.user(), user.wand(), val, useTick, charging, true)) {
					charging = false;
				}
				double power = charging ? 0 : ctx.power();
				ctx = new SpellContext(ctx.user(), ctx.origin(), ctx.facing(), ctx.seed(), ctx.tickUsing(), power, user.delay());
				spell.spell().value().execute(spell.spell(), ctx);
			}
			return true;
		}
		return false;
	}

}
