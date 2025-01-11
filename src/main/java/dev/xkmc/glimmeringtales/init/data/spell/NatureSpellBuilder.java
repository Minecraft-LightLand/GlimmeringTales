package dev.xkmc.glimmeringtales.init.data.spell;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.content.core.description.SpellTooltip;
import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.*;
import dev.xkmc.glimmeringtales.content.entity.hostile.MobCastingData;
import dev.xkmc.glimmeringtales.content.research.core.HexGraphData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTDamageTypeGen;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2core.util.MathHelper;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NatureSpellBuilder extends NatureSpellEntry {

	private static int order = 1000;

	private static int getOrder() {
		return order++;
	}

	private final ResourceLocation id;
	private final GTRegistries.ElemEntry elem;

	public DataGenCachedHolder<ProjectileConfig> proj;
	public DataGenCachedHolder<SpellAction> spell;
	public DataGenCachedHolder<NatureSpell> nature;
	public DataGenCachedHolder<HexGraphData> graph;
	public ResourceKey<DamageType> damage;

	private Function<NatureSpellBuilder, ProjectileConfig> projectileFactory;
	private Function<NatureSpellBuilder, SpellAction> spellFactory;
	private Function<Holder<SpellAction>, NatureSpell> natureFactory;
	private Function<ResourceLocation, String> langFactory;
	private Function<Holder<NatureSpell>, RuneBlock> runeFactory;
	private Supplier<HexGraphData> graphFactory;
	private SpellDamageEntry damageEntry;
	private SpellDesc desc;
	private ItemLike icon;
	private MobCastingData mob;

	private DataGenContext cache;

	private final List<BiConsumer<BlockSpellBuilder, Holder<NatureSpell>>> blockFactories = new ArrayList<>();

	public NatureSpellBuilder(ResourceLocation id, GTRegistries.ElemEntry elem) {
		this.id = id;
		this.elem = elem;
	}

	public final NatureSpellBuilder damageFreeze() {
		return damageVanilla(() -> new DamageType("freeze", 0, DamageEffects.FREEZING), GTDamageTypeGen.freeze());
	}

	public final NatureSpellBuilder damageFire() {
		return damageVanilla(() -> new DamageType("onFire", 0, DamageEffects.BURNING), DamageTypeTags.IS_FIRE);
	}

	public final NatureSpellBuilder damageExplosion() {
		return damageVanilla(() -> new DamageType("explosion", 0.1f), DamageTypeTags.IS_EXPLOSION);
	}

	@SafeVarargs
	public final NatureSpellBuilder damageVanilla(Supplier<DamageType> def, TagKey<DamageType>... tags) {
		damage = damage(id);
		this.damageEntry = new SpellDamageEntry(msg -> def.get(), null, null, MathHelper.merge(tags, elem.damgeTag()));
		return this;
	}

	@SafeVarargs
	public final NatureSpellBuilder damageCustom(Function<String, DamageType> def, String noPlayer, String withPlayer, TagKey<DamageType>... tags) {
		damage = damage(id);
		this.damageEntry = new SpellDamageEntry(def, noPlayer, withPlayer, MathHelper.merge(tags, elem.damgeTag()));
		return this;
	}

	public NatureSpellBuilder projectile(Function<NatureSpellBuilder, ProjectileConfig> factory) {
		proj = projectile(id);
		this.projectileFactory = factory;
		return this;
	}

	public NatureSpellBuilder spell(Function<NatureSpellBuilder, SpellAction> factory) {
		spell = spell(id);
		this.spellFactory = factory;
		return this;
	}

	@Deprecated(forRemoval = true)
	public NatureSpellBuilder cost(int cost) {
		return cost(cost, 0);
	}

	@Deprecated(forRemoval = true)
	public NatureSpellBuilder cost(int cost, int max) {
		return focusAndCost(10, cost, max);
	}

	public NatureSpellBuilder focusAndCost(int focus, int cost) {
		return focusAndCost(focus, cost, 0);
	}

	public NatureSpellBuilder focusAndCost(int focus, int cost, int max) {
		nature = nature(id);
		this.natureFactory = e -> new NatureSpell(e, elem.get(), focus, cost, max,
				desc == null ? warnEmpty() : desc.data, mob, graph);
		return this;
	}

	public NatureSpellBuilder mob(int range, double factor) {
		return mob(range, factor, 40);
	}

	public NatureSpellBuilder mob(int range, double factor, int maxTime) {
		this.mob = new MobCastingData(range, maxTime, factor);
		return this;
	}

	@SafeVarargs
	public final NatureSpellBuilder block(
			Function<NatureSpellBuilder, ConfiguredEngine<?>> action, ItemLike icon,
			Function<Holder<NatureSpell>, RuneBlock> item,
			BiConsumer<BlockSpellBuilder, Holder<NatureSpell>>... cons
	) {
		spell = spell(id);
		this.icon = icon;
		this.spellFactory = ctx -> ofBlock(action.apply(ctx), icon, getOrder());
		this.runeFactory = item;
		this.blockFactories.addAll(List.of(cons));
		return this;
	}

	public NatureSpellBuilder graph(NatureSpellBuilder other) {
		graph = other.graph;
		return this;
	}

	/**
	 * L - life, E - earth, F - flame, S - snow, O - ocean, T - thunder
	 */
	public NatureSpellBuilder graph(ArrayList<ResearchBonus> bonus, String... strs) {
		this.graph = graph(id);
		this.graphFactory = () -> {
			LinkedHashMap<String, SpellElement> map = new LinkedHashMap<>();
			for (var s : strs) {
				for (int i = 0; i < s.length(); i++) {
					char ch = s.charAt(i);
					switch (ch) {
						case 'L' -> map.put("L", GTRegistries.LIFE.get());
						case 'E' -> map.put("E", GTRegistries.EARTH.get());
						case 'F' -> map.put("F", GTRegistries.FLAME.get());
						case 'S' -> map.put("S", GTRegistries.SNOW.get());
						case 'O' -> map.put("O", GTRegistries.OCEAN.get());
						case 'T' -> map.put("T", GTRegistries.THUNDER.get());
						case '<', '>', '-', '|' -> {
						}
						default -> throw new IllegalArgumentException(
								"Char %c is illegal in flow %s for spell %s".formatted(ch, s, id)
						);
					}
				}
			}
			return new HexGraphData(map, new ArrayList<>(List.of(strs)), bonus);
		};
		return this;
	}

	public NatureSpellBuilder lang(String name) {
		return lang(e -> name);
	}

	public NatureSpellBuilder lang(Function<ResourceLocation, String> factory) {
		this.langFactory = factory;
		return this;
	}

	public NatureSpellBuilder desc(String brief, String detail, SpellTooltipData data) {
		desc = new SpellDesc(brief, detail, data);
		return this;
	}

	// fetch

	public Holder<DamageType> damage() {
		return cache.damage(damage);
	}

	// registration

	@Override
	public void regNature(BootstrapContext<NatureSpell> ctx) {
		cache = new DataGenContext(ctx);
		nature.gen(ctx, natureFactory.apply(spell));
		cache = null;
		try {
			new SpellTooltip(nature.value().spell().value().action(), desc.data).verify();
		} catch (Exception e) {
			GlimmeringTales.LOGGER.error("Spell {} failed description check", id);
			GlimmeringTales.LOGGER.throwing(e);
		}
	}

	@Override
	public void regBlock(DataMapProvider.Builder<BlockSpell, Block> builder) {
		var b = new BlockSpellBuilder(builder);
		for (var e : blockFactories) {
			e.accept(b, nature);
		}
	}

	@Override
	public void regRune(DataMapProvider.Builder<RuneBlock, Item> item) {
		if (runeFactory != null)
			item.add(icon.asItem().builtInRegistryHolder(), runeFactory.apply(nature), false);
	}

	@Override
	public void regGraph(BootstrapContext<HexGraphData> ctx) {
		if (graphFactory == null) return;
		ctx.register(graph.key, graphFactory.get());
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		cache = new DataGenContext(ctx);
		spellFactory.apply(this).verifyOnBuild(ctx, spell);
		cache = null;
	}

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(id), langFactory.apply(id));
		if (damageEntry != null) {
			if (damageEntry.noPlayer != null) {
				pvd.add("death.attack." + id.toLanguageKey(), damageEntry.noPlayer);
			}
			if (damageEntry.withPlayer != null) {
				pvd.add("death.attack." + id.toLanguageKey() + ".player", damageEntry.withPlayer);
			}
		}
		if (desc != null) {
			pvd.add(SpellTooltipData.brief(id), desc.brief);
			pvd.add(SpellTooltipData.detail(id), desc.detail);
		}
	}

	@Override
	public void registerDamage(GTDamageTypeGen gen) {
		if (damageEntry != null) {
			gen.genDamage(damage, () -> damageEntry.def().apply(id.toLanguageKey()), damageEntry.tags());
		}
	}

	@Override
	public void registerProjectile(BootstrapContext<ProjectileConfig> ctx) {
		if (projectileFactory != null && proj != null) {
			cache = new DataGenContext(ctx);
			projectileFactory.apply(this).verifyOnBuild(ctx, proj);
			cache = null;
		}
	}

	private SpellTooltipData warnEmpty() {
		GlimmeringTales.LOGGER.error("Spell {} does not have description setup", id);
		return SpellTooltipData.of();
	}

	@Nullable
	private HexGraphData warnNoGraph() {
		GlimmeringTales.LOGGER.error("Spell {} does not have graph setup", id);
		return null;
	}

	public record BlockSpellBuilder(DataMapProvider.Builder<BlockSpell, Block> builder) {

		public void add(Block block, BlockSpell spell) {
			builder.add(block.builtInRegistryHolder(), spell, false);
		}

		public void add(Holder<Block> block, BlockSpell spell) {
			builder.add(block, spell, false);
		}

		public void add(TagKey<Block> block, BlockSpell spell) {
			builder.add(block, spell, false);
		}

	}

	public record SpellDamageEntry(
			Function<String, DamageType> def,
			@Nullable String noPlayer, @Nullable String withPlayer,
			TagKey<DamageType>... tags
	) {

		@SafeVarargs
		public SpellDamageEntry {
		}

	}

	public record SpellDesc(String brief, String detail, SpellTooltipData data) {

	}

}
