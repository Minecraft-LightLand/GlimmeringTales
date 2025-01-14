package dev.xkmc.glimmeringtales.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.glimmeringtales.content.item.rune.SpellCoreItem;
import dev.xkmc.glimmeringtales.content.recipe.craft.WandRecipeBuilder;
import dev.xkmc.glimmeringtales.content.recipe.infuse.SimpleInfuseRecipeBuilder;
import dev.xkmc.glimmeringtales.content.recipe.ritual.SimpleRitualRecipeBuilder;
import dev.xkmc.glimmeringtales.content.recipe.thunder.StrikeBlockRecipeBuilder;
import dev.xkmc.glimmeringtales.content.recipe.thunder.StrikeItemRecipeBuilder;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2complements.init.registrate.LCItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.function.BiFunction;

public class GTRecipeGen {

	public static void onRecipeGen(RegistrateRecipeProvider pvd) {
		{

			unlock(pvd, new StrikeItemRecipeBuilder(Ingredient.of(Items.LAPIS_LAZULI),
					GTItems.CRYSTAL_NATURE.asStack())::unlockedBy, Items.LAPIS_LAZULI)
					.save(pvd);

			unlock(pvd, new StrikeBlockRecipeBuilder(Blocks.BUDDING_AMETHYST, Blocks.AIR,
					GTItems.CRYSTAL_EARTH.asStack())::unlockedBy, Items.AMETHYST_SHARD)
					.save(pvd);

			unlock(pvd, new StrikeBlockRecipeBuilder(ItemTags.LOGS_THAT_BURN, GTItems.STRUCK_LOG.get(),
					ItemStack.EMPTY)::unlockedBy, Items.OAK_LOG)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GTItems.DEPLETED_FLAME.get())::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern("AAA").pattern("ABA").pattern("AAA")
					.define('A', Items.MAGMA_BLOCK)
					.define('B', GTItems.CRYSTAL_NATURE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GTItems.DEPLETED_WINTERSTORM.get())::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern("ACA").pattern("BXB").pattern("ACA")
					.define('A', Items.ICE)
					.define('B', Items.PACKED_ICE)
					.define('C', Items.BLUE_ICE)
					.define('X', GTItems.CRYSTAL_NATURE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GTItems.CRYSTAL_VINE.get())::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern("AAA").pattern("ABA").pattern("AAA")
					.define('A', Tags.Items.SEEDS)
					.define('B', GTItems.CRYSTAL_NATURE)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(Items.CONDUIT, GTItems.CRYSTAL_OCEAN)::unlockedBy, Items.CONDUIT)
					.side(Items.PRISMARINE, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_NATURE, GTItems.CRYSTAL_THUNDER)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.side(GTItems.CRYSTAL_EARTH, 1)
					.side(GTItems.CRYSTAL_FLAME, 1)
					.side(GTItems.CRYSTAL_WINTERSTORM, 1)
					.side(GTItems.CRYSTAL_OCEAN, 1)
					.side(GTItems.STRUCK_LOG, 4)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GTItems.RESONATOR.get())::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern(" A ").pattern(" BA").pattern("I  ")
					.define('A', Items.AMETHYST_SHARD)
					.define('B', GTItems.CRYSTAL_NATURE)
					.define('I', Items.IRON_INGOT)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GTItems.RITUAL_ALTAR.get())::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern("DGD").pattern("ANA").pattern("DWD")
					.define('A', Items.AMETHYST_SHARD)
					.define('G', Items.GOLD_INGOT)
					.define('N', GTItems.CRYSTAL_NATURE)
					.define('W', GTItems.STRUCK_LOG)
					.define('D', Items.DEEPSLATE_BRICKS)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GTItems.RITUAL_MATRIX.get())::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.pattern("GGG").pattern("ANA").pattern("DWD")
					.define('A', Items.DIAMOND)
					.define('G', Items.GOLD_INGOT)
					.define('N', GTItems.CRYSTAL_EARTH)
					.define('W', GTItems.STRUCK_LOG)
					.define('D', Items.DEEPSLATE_BRICKS)
					.save(pvd);
		}

		// infuse
		{
			//life - misc
			{
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.STICK, Items.DEAD_BUSH, 100);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_BUSH, Items.OAK_SAPLING, 100);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.MANGROVE_ROOTS, Items.VINE, 100);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.POISONOUS_POTATO, Items.POTATO, 40);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DIRT, Items.GRASS_BLOCK, 100);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.COARSE_DIRT, Items.DIRT, 100);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.SAND, Items.COARSE_DIRT, 100);
			}

			// life - coral
			{
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_BRAIN_CORAL, Items.BRAIN_CORAL, 100);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_BUBBLE_CORAL, Items.BUBBLE_CORAL, 100);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_FIRE_CORAL, Items.FIRE_CORAL, 100);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_HORN_CORAL, Items.HORN_CORAL, 100);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_TUBE_CORAL, Items.TUBE_CORAL, 100);

				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_BRAIN_CORAL_FAN, Items.BRAIN_CORAL_FAN, 160);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_BUBBLE_CORAL_FAN, Items.BUBBLE_CORAL_FAN, 160);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_FIRE_CORAL_FAN, Items.FIRE_CORAL_FAN, 160);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_HORN_CORAL_FAN, Items.HORN_CORAL_FAN, 160);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_TUBE_CORAL_FAN, Items.TUBE_CORAL_FAN, 160);

				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_BRAIN_CORAL_BLOCK, Items.BRAIN_CORAL_BLOCK, 200);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_BUBBLE_CORAL_BLOCK, Items.BUBBLE_CORAL_BLOCK, 200);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_FIRE_CORAL_BLOCK, Items.FIRE_CORAL_BLOCK, 200);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_HORN_CORAL_BLOCK, Items.HORN_CORAL_BLOCK, 200);
				infuse(pvd, GTItems.CRYSTAL_LIFE.get(), Items.DEAD_TUBE_CORAL_BLOCK, Items.TUBE_CORAL_BLOCK, 200);
			}

			// earth - transmute
			{
				infuse(pvd, GTItems.CRYSTAL_EARTH.get(), Items.REDSTONE, Items.GLOWSTONE_DUST, 20);
				infuse(pvd, GTItems.CRYSTAL_EARTH.get(), Items.GLOWSTONE_DUST, Items.REDSTONE, 20);
				infuse(pvd, GTItems.CRYSTAL_EARTH.get(), Items.SAND, Items.RED_SAND, 10);
				infuse(pvd, GTItems.CRYSTAL_EARTH.get(), Items.RED_SAND, Items.SAND, 10);
				infuse(pvd, GTItems.CRYSTAL_EARTH.get(), Items.SOUL_SAND, Items.SOUL_SOIL, 10);
				infuse(pvd, GTItems.CRYSTAL_EARTH.get(), Items.SOUL_SOIL, Items.SOUL_SAND, 10);
				infuse(pvd, GTItems.CRYSTAL_EARTH.get(), Items.STONE, Items.DEEPSLATE, 10);
				infuse(pvd, GTItems.CRYSTAL_EARTH.get(), Items.DEEPSLATE, Items.STONE, 10);
			}

			// earth - crystallize
			{
				infuse(pvd, GTItems.CRYSTAL_EARTH.get(), Items.COBBLED_DEEPSLATE, Items.DEEPSLATE, 10);
				infuse(pvd, GTItems.CRYSTAL_EARTH.get(), Items.COBBLESTONE, Items.STONE, 10);
				infuse(pvd, GTItems.CRYSTAL_EARTH.get(), Items.GLASS, Items.QUARTZ, 100);
				infuse(pvd, GTItems.CRYSTAL_EARTH.get(), Items.QUARTZ, Items.AMETHYST_SHARD, 40);
				infuse(pvd, GTItems.CRYSTAL_EARTH.get(), Items.AMETHYST_SHARD, Items.LAPIS_LAZULI, 40);
				infuse(pvd, GTItems.CRYSTAL_EARTH.get(), Items.PRISMARINE_SHARD, Items.COPPER_INGOT, 40);
			}

			// fire
			{
				infuse(pvd, GTItems.CRYSTAL_FLAME.get(), Items.SANDSTONE, Items.NETHERRACK, 20);
				infuse(pvd, GTItems.CRYSTAL_FLAME.get(), Items.COBBLESTONE, Items.BLACKSTONE, 40);
				infuse(pvd, GTItems.CRYSTAL_FLAME.get(), Items.NETHERRACK, Items.MAGMA_BLOCK, 100);
				infuse(pvd, GTItems.CRYSTAL_FLAME.get(), Items.SLIME_BALL, Items.MAGMA_CREAM, 100);
			}

			// snow
			{
				infuse(pvd, GTItems.CRYSTAL_WINTERSTORM.get(), Items.SNOW_BLOCK, Items.ICE, 20);
				infuse(pvd, GTItems.CRYSTAL_WINTERSTORM.get(), Items.ICE, Items.PACKED_ICE, 80);
				infuse(pvd, GTItems.CRYSTAL_WINTERSTORM.get(), Items.PACKED_ICE, Items.BLUE_ICE, 200);
				infuse(pvd, GTItems.CRYSTAL_WINTERSTORM.get(), Items.WATER_BUCKET, Items.POWDER_SNOW_BUCKET, 200);
			}

			// ocean
			{
				infuse(pvd, GTItems.CRYSTAL_OCEAN.get(), Items.FLINT, Items.PRISMARINE_SHARD, 40);
				infuse(pvd, GTItems.CRYSTAL_OCEAN.get(), Items.ARMADILLO_SCUTE, Items.TURTLE_SCUTE, 100);
				infuse(pvd, GTItems.CRYSTAL_OCEAN.get(), Items.SHULKER_SHELL, Items.NAUTILUS_SHELL, 100);
				infuse(pvd, GTItems.CRYSTAL_OCEAN.get(), Items.COBBLESTONE, Items.PRISMARINE, 200);
			}

			// thunder
			{
				infuse(pvd, GTItems.CRYSTAL_THUNDER.get(), ItemTags.LOGS, GTItems.STRUCK_LOG, 20);
			}

		}

		// ring
		{

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GTItems.Curios.GOLDEN_RING)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern("GGG").pattern("GNG").pattern("GGG")
					.define('G', Items.GOLD_INGOT)
					.define('N', GTItems.CRYSTAL_NATURE)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.Curios.GOLDEN_RING, GTItems.Curios.RING_OF_REGENERATION)::unlockedBy, GTItems.Curios.GOLDEN_RING.asItem())
					.side(GTItems.CRYSTAL_LIFE, 1)
					.side(Items.AMETHYST_SHARD, 3)
					.side(Items.GLOWSTONE_DUST, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.Curios.GOLDEN_RING, GTItems.Curios.RING_OF_NATURE)::unlockedBy, GTItems.Curios.GOLDEN_RING.asItem())
					.side(GTItems.CRYSTAL_NATURE, 1)
					.side(Items.REDSTONE, 3)
					.side(Items.LAPIS_LAZULI, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.Curios.GOLDEN_RING, GTItems.Curios.RING_OF_EARTH)::unlockedBy, GTItems.Curios.GOLDEN_RING.asItem())
					.side(GTItems.CRYSTAL_EARTH, 1)
					.side(GTItems.CRYSTAL_NATURE, 3)
					.side(Items.DIAMOND, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.Curios.GOLDEN_RING, GTItems.Curios.RING_OF_LIFE)::unlockedBy, GTItems.Curios.GOLDEN_RING.asItem())
					.side(GTItems.CRYSTAL_LIFE, 1)
					.side(GTItems.CRYSTAL_NATURE, 3)
					.side(Items.BAMBOO, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.Curios.GOLDEN_RING, GTItems.Curios.RING_OF_FLAME)::unlockedBy, GTItems.Curios.GOLDEN_RING.asItem())
					.side(GTItems.CRYSTAL_FLAME, 1)
					.side(GTItems.CRYSTAL_NATURE, 3)
					.side(Items.BLAZE_POWDER, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.Curios.GOLDEN_RING, GTItems.Curios.RING_OF_SNOW)::unlockedBy, GTItems.Curios.GOLDEN_RING.asItem())
					.side(GTItems.CRYSTAL_WINTERSTORM, 1)
					.side(GTItems.CRYSTAL_NATURE, 3)
					.side(Items.BLUE_ICE, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.Curios.GOLDEN_RING, GTItems.Curios.RING_OF_OCEAN)::unlockedBy, GTItems.Curios.GOLDEN_RING.asItem())
					.side(GTItems.CRYSTAL_OCEAN, 1)
					.side(GTItems.CRYSTAL_NATURE, 3)
					.side(Items.NAUTILUS_SHELL, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.Curios.GOLDEN_RING, GTItems.Curios.RING_OF_THUNDER)::unlockedBy, GTItems.Curios.GOLDEN_RING.asItem())
					.side(GTItems.CRYSTAL_THUNDER, 1)
					.side(GTItems.CRYSTAL_NATURE, 3)
					.side(GTItems.STRUCK_LOG, 4)
					.save(pvd);
		}

		// elemental charm
		{

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GTItems.Curios.CHARM_OF_NATURE)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern("LNS").pattern("NDN").pattern("LNL")
					.define('S', Items.STRING)
					.define('D', Items.DIAMOND)
					.define('N', GTItems.CRYSTAL_NATURE)
					.define('L', Items.LAPIS_LAZULI)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.Curios.CHARM_OF_NATURE, GTItems.Curios.CHARM_OF_EARTH)::unlockedBy, GTItems.Curios.CHARM_OF_NATURE.asItem())
					.side(GTItems.CRYSTAL_EARTH, 1)
					.side(LCItems.EXPLOSION_SHARD, 3)
					.side(Items.NETHERITE_SCRAP, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.Curios.CHARM_OF_NATURE, GTItems.Curios.CHARM_OF_LIFE)::unlockedBy, GTItems.Curios.CHARM_OF_NATURE.asItem())
					.side(GTItems.CRYSTAL_LIFE, 1)
					.side(LCMats.TOTEMIC_GOLD.getIngot(), 3)
					.side(Items.BAMBOO, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.Curios.CHARM_OF_NATURE, GTItems.Curios.CHARM_OF_FLAME)::unlockedBy, GTItems.Curios.CHARM_OF_NATURE.asItem())
					.side(GTItems.CRYSTAL_FLAME, 1)
					.side(LCItems.SOUL_FLAME, 3)
					.side(Items.BLAZE_POWDER, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.Curios.CHARM_OF_NATURE, GTItems.Curios.CHARM_OF_SNOW)::unlockedBy, GTItems.Curios.CHARM_OF_NATURE.asItem())
					.side(GTItems.CRYSTAL_WINTERSTORM, 1)
					.side(LCItems.HARD_ICE, 3)
					.side(Items.BLUE_ICE, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.Curios.CHARM_OF_NATURE, GTItems.Curios.CHARM_OF_OCEAN)::unlockedBy, GTItems.Curios.CHARM_OF_NATURE.asItem())
					.side(GTItems.CRYSTAL_OCEAN, 1)
					.side(LCMats.POSEIDITE.getIngot(), 3)
					.side(Items.NAUTILUS_SHELL, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.Curios.CHARM_OF_NATURE, GTItems.Curios.CHARM_OF_THUNDER)::unlockedBy, GTItems.Curios.CHARM_OF_NATURE.asItem())
					.side(GTItems.CRYSTAL_THUNDER, 1)
					.side(LCItems.STORM_CORE, 3)
					.side(LCItems.CAPTURED_WIND, 4)
					.save(pvd);
		}

		// misc curios
		{
			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_OCEAN, GTItems.Curios.CHARM_OF_STRENGTH)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.side(GTItems.CRYSTAL_NATURE, 2)
					.side(Items.AMETHYST_SHARD, 2)
					.side(Items.GOLD_INGOT, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.Curios.CHARM_OF_CAPACITY)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.side(GTItems.CRYSTAL_NATURE, 2)
					.side(Items.AMETHYST_SHARD, 2)
					.side(Items.GOLD_INGOT, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_LIFE, GTItems.Curios.CHARM_OF_REGENERATION)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.side(GTItems.CRYSTAL_NATURE, 2)
					.side(Items.AMETHYST_SHARD, 2)
					.side(Items.GOLD_INGOT, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_OCEAN, GTItems.GLOVE_OF_SORCERER)::unlockedBy, GTItems.CRYSTAL_OCEAN.get())
					.side(GTItems.CRYSTAL_NATURE, 2)
					.side(Items.DRAGON_BREATH, 2)
					.side(LCItems.SOUL_FLAME, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_THUNDER, GTItems.GLOVE_OF_ABYSS)::unlockedBy, GTItems.CRYSTAL_THUNDER.get())
					.side(LCItems.WARDEN_BONE_SHARD, 2)
					.side(LCItems.VOID_EYE, 2)
					.side(LCItems.RESONANT_FEATHER, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_OCEAN, GTItems.GLOVE_OF_OCEAN)::unlockedBy, GTItems.CRYSTAL_OCEAN.get())
					.side(LCItems.CAPTURED_WIND, 2)
					.side(Items.CONDUIT, 2)
					.side(LCItems.STORM_CORE, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_THUNDER, GTItems.GLOVE_OF_THUNDER)::unlockedBy, GTItems.CRYSTAL_OCEAN.get())
					.side(LCMats.POSEIDITE.getIngot(), 2)
					.side(LCItems.RESONANT_FEATHER, 2)
					.side(LCItems.STORM_CORE, 4)
					.save(pvd);

		}

		// wand
		{
			unlock(pvd, new WandRecipeBuilder(GTItems.WOOD_WAND, 1)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern("  O").pattern(" I ").pattern("I  ")
					.define('O', GTItems.CRYSTAL_NATURE)
					.define('I', Items.STICK)
					.save(pvd);

			unlock(pvd, new WandRecipeBuilder(GTItems.LIFE_WAND, 1)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern(" VO").pattern("VGV").pattern("IV ")
					.define('O', GTItems.CRYSTAL_NATURE)
					.define('G', LCMats.TOTEMIC_GOLD.getIngot())
					.define('I', Items.BAMBOO)
					.define('V', Items.VINE)
					.save(pvd);

			unlock(pvd, new WandRecipeBuilder(GTItems.GOLD_WAND, 1)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern(" RO").pattern("RIR").pattern("IR ")
					.define('O', GTItems.CRYSTAL_NATURE)
					.define('I', Items.GOLD_INGOT)
					.define('R', Items.REDSTONE)
					.save(pvd);

			unlock(pvd, new WandRecipeBuilder(GTItems.OCEAN_WAND, 1)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern(" VO").pattern("VIV").pattern("IV ")
					.define('O', GTItems.CRYSTAL_NATURE)
					.define('I', LCMats.POSEIDITE.getIngot())
					.define('V', Items.PRISMARINE_CRYSTALS)
					.save(pvd);

			unlock(pvd, new WandRecipeBuilder(GTItems.THUNDER_WAND, 1)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern(" VO").pattern("VLV").pattern("IV ")
					.define('O', GTItems.CRYSTAL_NATURE)
					.define('L', LCMats.TOTEMIC_GOLD.getIngot())
					.define('I', LCMats.POSEIDITE.getIngot())
					.define('V', LCItems.HARD_ICE)
					.save(pvd);

			unlock(pvd, new WandRecipeBuilder(GTItems.NETHER_WAND, 1)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern(" WO").pattern("FIC").pattern("IV ")
					.define('O', GTItems.CRYSTAL_NATURE)
					.define('I', Items.BLAZE_ROD)
					.define('W', Items.WEEPING_VINES)
					.define('C', LCItems.BLACKSTONE_CORE)
					.define('V', Items.TWISTING_VINES)
					.define('F', LCItems.SOUL_FLAME)
					.save(pvd);

			unlock(pvd, new WandRecipeBuilder(GTItems.ENDER_WAND, 1)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern(" WO").pattern("FIC").pattern("IV ")
					.define('O', GTItems.CRYSTAL_NATURE)
					.define('I', Items.END_ROD)
					.define('W', Items.CHORUS_FLOWER)
					.define('C', LCItems.VOID_EYE)
					.define('V', LCMats.SHULKERATE.getIngot())
					.define('F', LCItems.HARD_ICE)
					.save(pvd);
		}

		// life
		{

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_LIFE, GTItems.RUNE_BAMBOO)::unlockedBy, GTItems.CRYSTAL_LIFE.get())
					.side(Items.BAMBOO, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_LIFE, GTItems.RUNE_CACTUS)::unlockedBy, GTItems.CRYSTAL_LIFE.get())
					.side(Items.CACTUS, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_LIFE, GTItems.RUNE_FLOWER)::unlockedBy, GTItems.CRYSTAL_LIFE.get())
					.side(ItemTags.FLOWERS, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_LIFE, GTItems.RUNE_VINE)::unlockedBy, GTItems.CRYSTAL_LIFE.get())
					.side(Items.VINE, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_LIFE, GTItems.RUNE_HAYBALE)::unlockedBy, GTItems.CRYSTAL_LIFE.get())
					.side(Items.HAY_BLOCK, 4)
					.side(Items.CARROT, 4)
					.save(pvd);

		}

		// earth
		{
			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_SAND)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Tags.Items.SANDS, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_GRAVEL)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Tags.Items.GRAVELS, 4)
					.side(Items.FLINT, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_QUARTZ)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Items.QUARTZ_BLOCK, 4)
					.side(Items.GLASS, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_CLAY)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Items.CLAY, 4)
					.side(Items.MUD, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_STONE)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Items.STONE, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_DRIPSTONE)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Items.DRIPSTONE_BLOCK, 4)
					.side(Items.POINTED_DRIPSTONE, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_AMETHYST)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Items.AMETHYST_BLOCK, 4)
					.side(Items.AMETHYST_SHARD, 4)
					.save(pvd);

		}

		// flame
		{

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_FLAME, GTItems.RUNE_MAGMA)::unlockedBy, GTItems.CRYSTAL_FLAME.get())
					.side(Items.MAGMA_BLOCK, 4)
					.side(Items.MAGMA_CREAM, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_FLAME, GTItems.RUNE_NETHERRACK)::unlockedBy, GTItems.CRYSTAL_FLAME.get())
					.side(Items.NETHER_BRICKS, 4)
					.side(Items.NETHER_BRICK, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_FLAME, GTItems.RUNE_SOUL_SAND)::unlockedBy, GTItems.CRYSTAL_FLAME.get())
					.side(Items.SOUL_SAND, 4)
					.side(Items.SOUL_SOIL, 4)
					.save(pvd);

		}

		// snow
		{
			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_WINTERSTORM, GTItems.RUNE_SNOW)::unlockedBy, GTItems.CRYSTAL_WINTERSTORM.get())
					.side(Items.SNOW_BLOCK, 4)
					.side(Items.SNOWBALL, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_WINTERSTORM, GTItems.RUNE_ICE)::unlockedBy, GTItems.CRYSTAL_WINTERSTORM.get())
					.side(Items.ICE, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_WINTERSTORM, GTItems.RUNE_PACKED_ICE)::unlockedBy, GTItems.CRYSTAL_WINTERSTORM.get())
					.side(Items.PACKED_ICE, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_WINTERSTORM, GTItems.RUNE_BLUE_ICE)::unlockedBy, GTItems.CRYSTAL_WINTERSTORM.get())
					.side(Items.BLUE_ICE, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_WINTERSTORM, GTItems.RUNE_POWDER_SNOW)::unlockedBy, GTItems.CRYSTAL_WINTERSTORM.get())
					.side(Items.POWDER_SNOW_BUCKET, 4)
					.side(Items.SNOWBALL, 4)
					.save(pvd);
		}

		// ocean, thunder
		{

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_OCEAN, GTItems.RUNE_SPONGE)::unlockedBy, GTItems.CRYSTAL_OCEAN.get())
					.side(Items.SPONGE, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_OCEAN, GTItems.RUNE_CORAL_REEF)::unlockedBy, GTItems.CRYSTAL_OCEAN.get())
					.side(Items.BRAIN_CORAL_BLOCK, 1)
					.side(Items.BUBBLE_CORAL_BLOCK, 1)
					.side(Items.FIRE_CORAL_BLOCK, 1)
					.side(Items.HORN_CORAL_BLOCK, 1)
					.side(Items.TUBE_CORAL_BLOCK, 1)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_THUNDER, GTItems.RUNE_THUNDER)::unlockedBy, GTItems.CRYSTAL_THUNDER.get())
					.side(GTItems.STRUCK_LOG, 8)
					.save(pvd);
		}

		// spells
		{


			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_FLAME, GTItems.HELL_MARK)::unlockedBy, GTItems.CRYSTAL_FLAME.get())
					.side(GTItems.RUNE_NETHERRACK, 1)
					.side(GTItems.RUNE_DRIPSTONE, 1)
					.side(LCItems.SOUL_CHARGE, 2)
					.side(Items.BLAZE_POWDER, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_FLAME, GTItems.LAVA_BURST)::unlockedBy, GTItems.CRYSTAL_FLAME.get())
					.side(GTItems.RUNE_MAGMA, 1)
					.side(GTItems.RUNE_STONE, 1)
					.side(LCItems.STRONG_CHARGE, 2)
					.side(Items.BLAZE_POWDER, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_FLAME, GTItems.FLAME_DASH)::unlockedBy, GTItems.CRYSTAL_FLAME.get())
					.side(GTItems.RUNE_NETHERRACK, 1)
					.side(GTItems.RUNE_CACTUS, 1)
					.side(LCItems.CAPTURED_WIND, 2)
					.side(Items.BLAZE_POWDER, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_WINTERSTORM, GTItems.WINTER_STORM)::unlockedBy, GTItems.CRYSTAL_WINTERSTORM.get())
					.side(GTItems.RUNE_BLUE_ICE, 1)
					.side(GTItems.RUNE_GRAVEL, 1)
					.side(LCItems.HARD_ICE, 2)
					.side(Items.SNOW_BLOCK, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_WINTERSTORM, GTItems.SNOW_TORNADO)::unlockedBy, GTItems.CRYSTAL_WINTERSTORM.get())
					.side(GTItems.RUNE_POWDER_SNOW, 1)
					.side(GTItems.RUNE_SAND, 1)
					.side(LCItems.HARD_ICE, 2)
					.side(Items.SNOW_BLOCK, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_WINTERSTORM, GTItems.ICY_FLASH)::unlockedBy, GTItems.CRYSTAL_WINTERSTORM.get())
					.side(GTItems.RUNE_SNOW, 1)
					.side(GTItems.RUNE_BLUE_ICE, 1)
					.side(LCItems.HARD_ICE, 2)
					.side(Items.ENDER_PEARL, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.STONE_BRIDGE)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(GTItems.RUNE_STONE, 1)
					.side(Items.STONE, 7)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.AMETHYST_PENETRATION)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(GTItems.RUNE_AMETHYST, 4)
					.side(LCItems.EXPLOSION_SHARD, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.EARTHQUAKE)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(GTItems.RUNE_STONE, 1)
					.side(GTItems.RUNE_GRAVEL, 1)
					.side(LCItems.BLACKSTONE_CORE, 2)
					.side(Items.ANVIL, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.METEOR)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(GTItems.RUNE_MAGMA, 1)
					.side(GTItems.RUNE_QUARTZ, 1)
					.side(LCItems.BLACKSTONE_CORE, 2)
					.side(Items.ANVIL, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_OCEAN, GTItems.OCEAN_SHELTER)::unlockedBy, GTItems.CRYSTAL_OCEAN.get())
					.side(GTItems.RUNE_CORAL_REEF, 1)
					.side(GTItems.RUNE_SPONGE, 1)
					.side(GTItems.RUNE_FLOWER, 1)
					.side(GTItems.RUNE_VINE, 1)
					.side(Items.SEA_LANTERN, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_THUNDER, GTItems.THUNDERSTORM)::unlockedBy, GTItems.CRYSTAL_THUNDER.get())
					.side(GTItems.RUNE_ICE, 1)
					.side(GTItems.RUNE_CORAL_REEF, 1)
					.side(GTItems.RUNE_THUNDER, 2)
					.side(LCMats.POSEIDITE.getIngot(), 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_THUNDER, GTItems.CHARGE_BURST)::unlockedBy, GTItems.CRYSTAL_THUNDER.get())
					.side(GTItems.RUNE_THUNDER, 1)
					.side(GTItems.RUNE_DRIPSTONE, 1)
					.side(LCMats.POSEIDITE.getIngot(), 2)
					.side(LCItems.STRONG_CHARGE, 4)
					.save(pvd);

		}

	}

	public static void infuse(RegistrateRecipeProvider pvd, SpellCoreItem crystal, ItemLike in, ItemLike out, int time) {
		var reg = BuiltInRegistries.ITEM;
		String path = reg.getKey(crystal).getPath().substring(11) + "_infused_" + reg.getKey(in.asItem()).getPath();
		unlock(pvd, new SimpleInfuseRecipeBuilder(crystal, Ingredient.of(in),
				out.asItem().getDefaultInstance(), time)::unlockedBy, crystal)
				.save(pvd, GlimmeringTales.loc(path));
	}

	public static void infuse(RegistrateRecipeProvider pvd, SpellCoreItem crystal, TagKey<Item> in, ItemLike out, int time) {
		var reg = BuiltInRegistries.ITEM;
		String path = reg.getKey(crystal).getPath().substring(11) + "_infused_" + in.location().getPath();
		unlock(pvd, new SimpleInfuseRecipeBuilder(crystal, Ingredient.of(in),
				out.asItem().getDefaultInstance(), time)::unlockedBy, crystal)
				.save(pvd, GlimmeringTales.loc(path));
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, Criterion<InventoryChangeTrigger.TriggerInstance>, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCriterion(pvd));
	}


}
