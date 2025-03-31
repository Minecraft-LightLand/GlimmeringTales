package dev.xkmc.glimmeringtales.init.data;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2core.util.ConfigInit;
import net.neoforged.neoforge.common.ModConfigSpec;

public class GTConfigs {

	public static class Client extends ConfigInit {

		public final ModConfigSpec.IntValue resonatorSearchRadius;
		public final ModConfigSpec.IntValue resonatorSearchTrialsPerTick;

		public Client(Builder builder) {
			markL2();
			builder.push("resonator", "Resonator properties");
			resonatorSearchRadius = builder.text("Amethyst Resonator search radius")
					.defineInRange("resonatorSearchRadius", 64, 16, 128);
			resonatorSearchTrialsPerTick = builder.text("Amethyst Resonator search trials per tick")
					.defineInRange("resonatorSearchTrialsPerTick", 500, 16, 10000);
			builder.pop();

		}
	}

	public static class Server extends ConfigInit {

		public final ModConfigSpec.IntValue crystalOfFlameRequirement;
		public final ModConfigSpec.IntValue crystalOfWinterstormRequirement;
		public final ModConfigSpec.IntValue wandInteractionDistance;
		public final ModConfigSpec.IntValue ritualRange;
		public final ModConfigSpec.DoubleValue popFruitExplosionChanceOnEaten;
		public final ModConfigSpec.DoubleValue popFruitBonemealGrowChance;
		public final ModConfigSpec.IntValue popFruitBiomeGrowRarity;
		public final ModConfigSpec.IntValue popFruitBiomeGrowFactor;
		public final ModConfigSpec.IntValue popFruitAdjacentCrystalBoost;

		public Server(Builder builder) {
			markL2();
			wandInteractionDistance = builder
					.text("Wand interaction range")
					.defineInRange("wandInteractionDistance", 24, 4, 64);
			ritualRange = builder
					.text("Range for ritual blocks to check for each other")
					.defineInRange("ritualRange", 3, 1, 16);
			builder.push("materials", "Material properties");
			crystalOfFlameRequirement = builder
					.text("Crystal of Flame: Lava consumption")
					.defineInRange("crystalOfFlameRequirement", 64, 1, 1000);
			crystalOfWinterstormRequirement = builder
					.text("Crystal of Winterstorm: Powder Snow consumption")
					.defineInRange("crystalOfWinterstormRequirement", 64, 1, 1000);
			builder.pop();
			builder.push("pop_fruit","Pop Fruit");
			popFruitExplosionChanceOnEaten = builder
					.text("Chance for Pop Fruit to explosion on consumed")
					.defineInRange("popFruitExplosionChanceOnEaten", 0.1, 0, 1);
			popFruitBonemealGrowChance = builder
					.text("Chance for Pop Fruit to grow on bone meal")
					.defineInRange("popFruitBonemealGrowChance", 0.05, 0, 1);
			popFruitBiomeGrowRarity = builder
					.text("Pop Fruit has one in X chance to grow on random tick")
					.defineInRange("popFruitBiomeGrowRarity", 100, 10, 1000);
			popFruitBiomeGrowFactor = builder
					.text("Pop Fruit growth speed multiplier in correct biome")
					.defineInRange("popFruitBiomeGrowFactor", 3, 0, 100);
			popFruitAdjacentCrystalBoost = builder
					.text("Pop Fruit growth speed multiplier when there is adjacent crystal vine")
					.defineInRange("popFruitAdjacentCrystalBoost", 1, 0, 10);
			builder.pop();
		}
	}

	public static final Client CLIENT = GlimmeringTales.REGISTRATE.registerClient(Client::new);
	public static final Server SERVER = GlimmeringTales.REGISTRATE.registerSynced(Server::new);

}
