package dev.xkmc.glimmeringtales.compat.apoth;

import dev.shadowsoffire.apotheosis.Apoth.Components;
import dev.shadowsoffire.apotheosis.Apoth.Items;
import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.affix.AffixType;
import dev.shadowsoffire.apotheosis.loot.LootCategory;
import dev.shadowsoffire.apotheosis.loot.LootRarity;
import dev.shadowsoffire.apotheosis.loot.LootRule;
import dev.shadowsoffire.apotheosis.loot.RarityRegistry;
import dev.shadowsoffire.apotheosis.tiers.TieredWeights;
import dev.shadowsoffire.apotheosis.tiers.WorldTier;
import dev.shadowsoffire.placebo.util.data.DynamicRegistryProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Unbreakable;
import org.spongepowered.include.com.google.common.base.Preconditions;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class RarityProvider extends DynamicRegistryProvider<LootRarity> {
	public RarityProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(new PackOutput(output.getOutputFolder().getParent().getParent().getParent().resolve("ignored/")),
				registries, RarityRegistry.INSTANCE);
	}

	public String getName() {
		return "Rarities";
	}

	public void generate() {
		this.addRarity("common", TextColor.fromRgb(8421504), Items.COMMON_MATERIAL, (b) -> {
			return b.sortIndex(300).weights(TieredWeights.builder().with(WorldTier.HAVEN, 600, 0.0F).with(WorldTier.FRONTIER, 290, 0.0F).with(WorldTier.ASCENT, 100, 0.0F).with(WorldTier.SUMMIT, 0, 0.0F).with(WorldTier.PINNACLE, 0, 0.0F)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.ChancedLootRule(0.25F, new LootRule.AffixLootRule(AffixType.STAT)));
		});
		this.addRarity("uncommon", TextColor.fromRgb(3407667), Items.UNCOMMON_MATERIAL, (b) -> {
			return b.sortIndex(400).weights(TieredWeights.builder().with(WorldTier.HAVEN, 360, 2.5F).with(WorldTier.FRONTIER, 600, 0.0F).with(WorldTier.ASCENT, 300, 0.0F).with(WorldTier.SUMMIT, 120, 0.0F).with(WorldTier.PINNACLE, 0, 0.0F)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.ChancedLootRule(0.5F, new LootRule.AffixLootRule(AffixType.BASIC_EFFECT))).rule(new LootRule.SocketLootRule(0, 1));
		});
		this.addRarity("rare", TextColor.fromRgb(5592575), Items.RARE_MATERIAL, (b) -> {
			return b.sortIndex(500).weights(TieredWeights.builder().with(WorldTier.HAVEN, 40, 5.0F).with(WorldTier.FRONTIER, 100, 5.0F).with(WorldTier.ASCENT, 500, 2.5F).with(WorldTier.SUMMIT, 290, 2.5F).with(WorldTier.PINNACLE, 100, 0.0F)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.AffixLootRule(AffixType.BASIC_EFFECT)).rule(new LootRule.ChancedLootRule(0.35F, new LootRule.AffixLootRule(AffixType.BASIC_EFFECT))).rule(new LootRule.SocketLootRule(0, 2)).rule(new LootRule.DurabilityLootRule(0.1F, 0.25F));
		});
		this.addRarity("epic", TextColor.fromRgb(12255419), Items.EPIC_MATERIAL, (b) -> {
			return b.sortIndex(600).weights(TieredWeights.builder().with(WorldTier.HAVEN, 0, 0.0F).with(WorldTier.FRONTIER, 10, 0.0F).with(WorldTier.ASCENT, 100, 5.0F).with(WorldTier.SUMMIT, 540, 5.0F).with(WorldTier.PINNACLE, 650, 2.5F)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.AffixLootRule(AffixType.BASIC_EFFECT)).rule(new LootRule.ChancedLootRule(0.25F, new LootRule.AffixLootRule(AffixType.BASIC_EFFECT))).rule(new LootRule.AffixLootRule(AffixType.ABILITY)).rule(new LootRule.SocketLootRule(1, 3)).rule(new LootRule.DurabilityLootRule(0.25F, 0.55F));
		});
		this.addRarity("mythic", TextColor.fromRgb(15560724), Items.MYTHIC_MATERIAL, (b) -> {
			return b.sortIndex(700).weights(TieredWeights.builder().with(WorldTier.HAVEN, 0, 0.0F).with(WorldTier.FRONTIER, 0, 0.0F).with(WorldTier.ASCENT, 0, 0.0F).with(WorldTier.SUMMIT, 50, 5.0F).with(WorldTier.PINNACLE, 250, 10.0F)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.AffixLootRule(AffixType.BASIC_EFFECT)).rule(new LootRule.AffixLootRule(AffixType.BASIC_EFFECT)).rule(new LootRule.AffixLootRule(AffixType.ABILITY)).rule(new LootRule.SelectLootRule(0.95F, new LootRule.SocketLootRule(1, 3), new LootRule.SocketLootRule(4, 4))).rule(new LootRule.SelectLootRule(0.99F, new LootRule.DurabilityLootRule(0.45F, 0.75F), new LootRule.ComponentLootRule(DataComponentPatch.builder().set(DataComponents.UNBREAKABLE, new Unbreakable(true)).remove(Components.DURABILITY_BONUS).build())));
		});
		this.addRarity("ancient", TextColor.fromRgb(15560724), Items.BOSS_SUMMONER, (b) -> {
			return b.sortIndex(800).weights(TieredWeights.builder().with(WorldTier.HAVEN, 0, 0.0F).with(WorldTier.FRONTIER, 0, 0.0F).with(WorldTier.ASCENT, 0, 0.0F).with(WorldTier.SUMMIT, 50, 5.0F).with(WorldTier.PINNACLE, 250, 10.0F)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.AffixLootRule(AffixType.STAT)).rule(new LootRule.AffixLootRule(AffixType.BASIC_EFFECT)).rule(new LootRule.AffixLootRule(AffixType.BASIC_EFFECT)).rule(new LootRule.AffixLootRule(AffixType.ABILITY)).rule(new LootRule.SelectLootRule(0.95F, new LootRule.SocketLootRule(1, 3), new LootRule.SocketLootRule(4, 4))).rule(new LootRule.SelectLootRule(0.99F, new LootRule.DurabilityLootRule(0.45F, 0.75F), new LootRule.ComponentLootRule(DataComponentPatch.builder().set(DataComponents.UNBREAKABLE, new Unbreakable(true)).remove(Components.DURABILITY_BONUS).build())));
		});
	}

	static <T> LootRule componentRule(DataComponentType<T> type, T value) {
		return new LootRule.ComponentLootRule(DataComponentPatch.builder().set(type, value).build());
	}

	void addRarity(String id, TextColor color, Holder<Item> material, UnaryOperator<RarityBuilder> config) {
		this.add(Apotheosis.loc(id), ((RarityBuilder) config.apply(builder(color, material))).build());
	}

	public static RarityBuilder builder(TextColor color, Holder<Item> material) {
		return new RarityBuilder(color, material);
	}

	public static class RarityBuilder {
		private final TextColor color;
		private final Holder<Item> material;
		private TieredWeights weights;
		private final List<LootRule> rules = new ArrayList();
		private final Map<LootCategory, List<LootRule>> overrides = new IdentityHashMap();
		private int index = 1000;

		public RarityBuilder(TextColor color, Holder<Item> material) {
			this.color = color;
			this.material = material;
		}

		public RarityBuilder weights(TieredWeights.Builder builder) {
			this.weights = builder.build();
			return this;
		}

		public RarityBuilder rule(LootRule rule) {
			this.rules.add(rule);
			return this;
		}

		public RarityBuilder override(LootCategory category, UnaryOperator<RuleListBuilder> config) {
			final List<LootRule> list = new ArrayList();
			config.apply(new RuleListBuilder() {
				public RuleListBuilder rule(LootRule rule) {
					list.add(rule);
					return this;
				}
			});
			this.overrides.put(category, list);
			return this;
		}

		public RarityBuilder sortIndex(int index) {
			this.index = index;
			return this;
		}

		public LootRarity build() {
			Preconditions.checkNotNull(this.weights);
			Preconditions.checkArgument(this.rules.size() > 0);
			return new LootRarity(this.color, this.material, this.weights, this.rules, this.overrides, this.index);
		}
	}

	public interface RuleListBuilder {
		RuleListBuilder rule(LootRule var1);
	}
}
