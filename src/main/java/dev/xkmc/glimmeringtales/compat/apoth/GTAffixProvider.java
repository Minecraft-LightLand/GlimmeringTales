
package dev.xkmc.glimmeringtales.compat.apoth;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.affix.Affix;
import dev.shadowsoffire.apotheosis.affix.AffixRegistry;
import dev.shadowsoffire.apotheosis.affix.AffixType;
import dev.shadowsoffire.apotheosis.affix.AttributeAffix;
import dev.shadowsoffire.apotheosis.loot.LootRarity;
import dev.shadowsoffire.apotheosis.loot.RarityRegistry;
import dev.shadowsoffire.apothic_attributes.api.ALObjects;
import dev.shadowsoffire.placebo.util.data.DynamicRegistryProvider;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import org.spongepowered.include.com.google.common.base.Preconditions;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class GTAffixProvider extends DynamicRegistryProvider<Affix> {

	public record AffixEntry(String modid, String type, String id, String desc,
							 Holder<Attribute> attribute, AttributeModifier.Operation op,
							 float min, float max, float slope) {

		public AffixEntry(String id, String desc,
						  Holder<Attribute> attribute, AttributeModifier.Operation op,
						  float min, float max, float slope) {
			this(GlimmeringTales.MODID, "wand", id, desc, attribute, op, min, max, slope);
		}

		public void genLang(RegistrateLangProvider pvd) {
			pvd.add("affix." + modid + ":" + type + "/attribute/" + id, RegistrateLangProvider.toEnglishName(id));
			pvd.add("affix." + modid + ":" + type + "/attribute/" + id + ".suffix", desc);
		}

		private void gen(GTAffixProvider provider, LootRarity... rarities) {
			provider.addAttribute(type, id, attribute, op, b -> build(b, rarities));
		}

		private AttributeAffix.Builder build(AttributeAffix.Builder builder, LootRarity... rarities) {
			builder.definition(AffixType.STAT, 100, 0).categories(ApothCompat.WAND);
			for (int i = 0; i < rarities.length; i++)
				builder.value(rarities[i], min * (1 + slope * i), max * (1 + slope * i));
			return builder;
		}

	}

	public static final List<AffixEntry> LIST = List.of(
			new AffixEntry("intricate", "of Critical Thinking",
					ALObjects.Attributes.CRIT_CHANCE, Operation.ADD_VALUE,
					0.1f, 0.2f, 0.3f),
			new AffixEntry("lacerating", "of Surgical Precision",
					ALObjects.Attributes.CRIT_DAMAGE, Operation.ADD_VALUE,
					0.1f, 0.2f, 0.3f),
			new AffixEntry("magical", "of Spell Casting",
					L2DamageTracker.MAGIC_FACTOR, Operation.ADD_VALUE,
					0.2f, 0.4f, 0.3f),
			new AffixEntry("sharpened", "of Spike Shooting",
					ALObjects.Attributes.PROJECTILE_DAMAGE, Operation.ADD_VALUE,
					0.2f, 0.4f, 0.3f),
			new AffixEntry("fiery", "of Fire Ball",
					L2DamageTracker.FIRE_FACTOR, Operation.ADD_VALUE,
					0.3f, 0.6f, 0.3f),
			new AffixEntry("explosive", "of Explosion",
					L2DamageTracker.EXPLOSION_FACTOR, Operation.ADD_VALUE,
					0.3f, 0.6f, 0.3f),
			new AffixEntry("freezing", "of Freezing",
					L2DamageTracker.FREEZING_FACTOR, Operation.ADD_VALUE,
					0.4f, 0.8f, 0.3f),
			new AffixEntry("thunder", "of Lightning",
					L2DamageTracker.LIGHTNING_FACTOR, Operation.ADD_VALUE,
					0.4f, 0.8f, 0.3f),
			new AffixEntry("life_affinity", "of Life Affinity",
					GTRegistries.LIFE.attr(), Operation.ADD_VALUE,
					0.3f, 0.5f, 0.3f),
			new AffixEntry("earth_affinity", "of Earth Affinity",
					GTRegistries.EARTH.attr(), Operation.ADD_VALUE,
					0.3f, 0.5f, 0.3f),
			new AffixEntry("flame_affinity", "of Flame Affinity",
					GTRegistries.FLAME.attr(), Operation.ADD_VALUE,
					0.3f, 0.5f, 0.3f),
			new AffixEntry("snow_affinity", "of Snow Affinity",
					GTRegistries.SNOW.attr(), Operation.ADD_VALUE,
					0.3f, 0.5f, 0.3f),
			new AffixEntry("ocean_affinity", "of Ocean Affinity",
					GTRegistries.OCEAN.attr(), Operation.ADD_VALUE,
					0.3f, 0.5f, 0.3f),
			new AffixEntry("thunder_affinity", "of Thunder Affinity",
					GTRegistries.THUNDER.attr(), Operation.ADD_VALUE,
					0.3f, 0.5f, 0.3f)
	);

	public GTAffixProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, AffixRegistry.INSTANCE);
	}

	public String getName() {
		return "Affixes";
	}

	public void generate() {
		LootRarity common = rarity("common");
		LootRarity uncommon = rarity("uncommon");
		LootRarity rare = rarity("rare");
		LootRarity epic = rarity("epic");
		LootRarity mythic = rarity("mythic");
		//LootRarity ancient = rarity("ancient");
		for (var e : LIST) e.gen(this, common, uncommon, rare, epic, mythic);
		futures.add(CompletableFuture.runAsync(Objects.requireNonNull(RarityRegistry.INSTANCE)::validateExistingHolders));
		futures.add(CompletableFuture.runAsync(Objects.requireNonNull(AffixRegistry.INSTANCE)::validateExistingHolders));
	}

	private void addAttribute(String type, String name, Holder<Attribute> attribute, AttributeModifier.Operation op, UnaryOperator<AttributeAffix.Builder> config) {
		AttributeAffix.Builder builder = new AttributeAffix.Builder(attribute, op);
		config.apply(builder);
		this.add(GlimmeringTales.loc(type + "/attribute/" + name), builder.build());
	}

	private static LootRarity rarity(String path) {
		return Preconditions.checkNotNull(RarityRegistry.INSTANCE.getValue(Apotheosis.loc(path)));
	}

}
