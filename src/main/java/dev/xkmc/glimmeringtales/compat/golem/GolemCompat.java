package dev.xkmc.glimmeringtales.compat.golem;

import dev.xkmc.glimmeringtales.content.entity.hostile.MobSpellHelper;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.mob_weapon_api.registry.WeaponStatus;
import dev.xkmc.modulargolems.content.entity.humanoid.weapon.GolemWeaponRegistry;

public class GolemCompat {

	public static void init() {
		GolemWeaponRegistry.HUMANOID.register(GlimmeringTales.loc("wand"),
				(le, stack, hand) ->
						WeaponStatus.RANGED.withPriority(80)
								.of(MobSpellHelper.getSpell(le, stack) != null),
				GolemSpellGoal::new
		);

	}

}
