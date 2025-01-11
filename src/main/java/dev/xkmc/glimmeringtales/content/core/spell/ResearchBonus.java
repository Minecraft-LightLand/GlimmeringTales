package dev.xkmc.glimmeringtales.content.core.spell;

import dev.xkmc.glimmeringtales.init.data.GTLang;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public record ResearchBonus(
		int cost, double manaBonus, double focusBonus
) {

	public static ResearchBonus mana(int cost, double mana) {
		return new ResearchBonus(cost, mana, 1);
	}

	public static ResearchBonus focus(int cost, double focus) {
		return new ResearchBonus(cost, 1, focus);
	}

	public static ArrayList<ResearchBonus> simple2(int c1, int c2, double m1, double m2) {
		return new ArrayList<>(List.of(mana(c1, m1), mana(c2, m2)));
	}

	public static ArrayList<ResearchBonus> simple3(int c1, int c2, int c3, double m1, double m2, double f3) {
		return new ArrayList<>(List.of(mana(c1, m1), mana(c2, m2), focus(c3, f3)));
	}

	public static ArrayList<ResearchBonus> simple4(int c1, int c2, int c3, int c4, double m1, double m2, double f3, double f4) {
		return new ArrayList<>(List.of(mana(c1, m1), mana(c2, m2), focus(c3, f3), focus(c4, f4)));
	}

	public static ArrayList<ResearchBonus> base4(int c1, int c2, int c3, int c4) {
		return simple4(c1, c2, c3, c4, 0.7, 0.7, 0.5, 0.5);
	}

	public static ArrayList<ResearchBonus> mid4(int c4) {
		return simple4(300, 150, 80, c4, 0.7, 0.7, 0.5, 0.5);
	}

	public static ArrayList<ResearchBonus> small4(int c4) {
		return simple4(100, 36, c4 + 4, c4, 0.7, 0.7, 0.5, 0.5);
	}

	public static ArrayList<ResearchBonus> base3(int c1, int c2, int c3) {
		return simple3(c1, c2, c3, 0.8, 0.8, 0.5);
	}

	public static ArrayList<ResearchBonus> small3(int c4) {
		return base3(36, c4 + 4, c4);
	}

	public static ArrayList<ResearchBonus> small2(int c4) {
		return simple2(36, c4, 0.8, 0.8);
	}

	public static ArrayList<ResearchBonus> adv2(int c4) {
		return simple2(36, c4, 0.8, 0.5);
	}

	public int modifyMana(int mana) {
		return (int) (mana * manaBonus);
	}

	public int modifyFocus(int focus) {
		return (int) (focus * focusBonus);
	}

	public MutableComponent desc() {
		int mana = (int) Math.round((1 - manaBonus) * 100);
		int focus = (int) Math.round((1 - focusBonus) * 100);
		if (focus == 0) {
			return GTLang.HEX_BONUS_MANA.get(cost, mana);
		}
		if (mana == 0) {
			return GTLang.HEX_BONUS_FOCUS.get(cost, focus);
		}
		return GTLang.HEX_BONUS_BOTH.get(cost, mana, focus);
	}

}
