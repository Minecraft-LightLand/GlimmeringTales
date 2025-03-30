package dev.xkmc.glimmeringtales.content.core.description;

import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.l2magic.content.engine.core.Verifiable;
import dev.xkmc.l2magic.content.engine.extension.*;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.*;

public class SpellTooltip extends ExtensionEntry<SpellTooltip, SpellTooltip.DescHolder> {

	public record DescHolder(NatureSpell spell) implements ExtensionKey<SpellTooltip, DescHolder> {

		@Override
		public Verifiable getEntry() {
			return spell.spell().value().action();
		}

		@Override
		public SpellTooltip create() {
			return new SpellTooltip(getEntry(), spell.tooltip());
		}

	}

	private static final ExtensionTypeManager<SpellTooltip, DescHolder> MANAGER = new ExtensionTypeManager<>();

	public static SpellTooltip get(Level level, NatureSpell spell) {
		return MANAGER.get(new DescHolder(spell));
	}

	private final SpellTooltipData components;
	private final LinkedHashMap<ExtensionHolder<?>, List<IExtended<?>>> map = new LinkedHashMap<>();

	public SpellTooltip(Verifiable spell, SpellTooltipData components) {
		super(spell);
		this.components = components;
		analyze();
	}

	@Override
	public boolean match(DescHolder holder) {
		return entry == holder.getEntry() && components == holder.spell().tooltip();
	}

	@Override
	protected void initAnalysis() {
		map.clear();
		for (var e : components.list()) {
			map.put(e.type(), new ArrayList<>());
		}
	}

	@Override
	protected void process(IExtended<?> p) {
		if (map.containsKey(p.type())) {
			map.get(p.type()).add(p);
		}
	}

	public Component format(ResourceKey<NatureSpell> key) {
		return components.format(key.location(), this);
	}

	public void brief(ResourceKey<NatureSpell> key, List<Component> list) {
		components.brief(key.location(), list, this);
	}

	public void verify() {
		SpellDataStack stack = asStack();
		for (int i = 0; i < components.list().size(); i++) {
			var type = components.list().get(i).type();
			var ext = type.get(Component.class);
			ext.process(Wrappers.cast(stack.get(type)));
		}
	}

	public SpellDataStack asStack() {
		LinkedHashMap<ExtensionHolder<?>, Queue<IExtended<?>>> ans = new LinkedHashMap<>();
		for (var ent : map.entrySet()) {
			ans.put(ent.getKey(), new ArrayDeque<>(ent.getValue()));
		}
		return new SpellDataStack(ans);
	}

}
