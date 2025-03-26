package dev.xkmc.glimmeringtales.content.core.spell;

public record DefaultAffinity() implements IAffinityProvider {

	public static final IAffinityProvider INS = new DefaultAffinity();

	@Override
	public double get(SpellElement elem) {
		return 0;
	}

}
