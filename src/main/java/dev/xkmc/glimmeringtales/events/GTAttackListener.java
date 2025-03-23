package dev.xkmc.glimmeringtales.events;

import dev.xkmc.glimmeringtales.compat.misc.GTCurioCompat;
import dev.xkmc.glimmeringtales.content.item.curio.DamageTypeCurioItem;
import dev.xkmc.glimmeringtales.init.data.GTDamageTypeGen;
import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.damage.DamageState;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeRoot;

public class GTAttackListener implements AttackListener {

	@Override
	public void onCreateSource(CreateSourceEvent event) {
		var type = DamageTypeRoot.of(event.getOriginal());
		if (type == null) return;
		var attacker = event.getAttacker();
		var level = attacker.level();
		var old = type.getHolder(level);
		if (old.is(GTDamageTypeGen.SPELL)) {
			var list = GTCurioCompat.getAll(attacker, DamageTypeCurioItem.class);
			for (var e : list) {
				DamageState state = e.getState(old);
				if (state != null && type.validState(state)) {
					event.enable(state);
				}
			}
		}
	}

	@Override
	public boolean onAttack(DamageData.Attack data) {
		var attacker = data.getAttacker();
		if (attacker == null) return false;
		if (data.getSource().is(GTDamageTypeGen.SPELL)) {
			return attacker == data.getTarget() ||
					attacker.isAlliedTo(data.getTarget()) ||
					data.getTarget().isAlliedTo(attacker);
		}
		return false;
	}

}
