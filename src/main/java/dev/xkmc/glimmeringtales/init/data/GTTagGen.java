package dev.xkmc.glimmeringtales.init.data;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2core.init.L2TagGen;
import dev.xkmc.l2menustacker.init.L2MSTagGen;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import top.theillusivec4.curios.api.CuriosApi;

public class GTTagGen {

	public static final ProviderType<RegistrateTagsProvider.Impl<Biome>> BIOME = L2TagGen.getProvider(Registries.BIOME);
	public static final ProviderType<RegistrateTagsProvider.Impl<NatureSpell>> NATURE_SPELL = L2TagGen.getProvider(GTRegistries.SPELL);

	public static final TagKey<Item> CRYSTAL = item("crystal");
	public static final TagKey<Item> RUNE = item("rune");
	public static final TagKey<Item> SPELL = item("spell");
	public static final TagKey<Item> CORE = item("core");
	public static final TagKey<Item> UNIQUE = item("unique_curios");
	public static final TagKey<Item> POP_FRUIT = item("pop_fruit");

	public static final TagKey<Block> AMETHYST = block("amethyst");
	public static final TagKey<Block> QUARTZ = block("quartz");
	public static final TagKey<Block> VINE = block("vine");
	public static final TagKey<Block> BAMBOO = block("bamboo");
	public static final TagKey<Block> SNOW = block("snow");
	public static final TagKey<Block> POP_FRUIT_BLOCK = block("pop_fruit");

	public static final TagKey<Block> FAKE_MAGMA = block("fake_magma");

	public static final TagKey<EntityType<?>> CASTER = TagKey.create(Registries.ENTITY_TYPE, GlimmeringTales.loc("spell_caster"));

	public static final TagKey<NatureSpell> GROUNDED = TagKey.create(GTRegistries.SPELL, GlimmeringTales.loc("grounded_spells"));

	public static void genItemTag(RegistrateItemTagsProvider pvd) {
		pvd.addTag(CORE).addTags(CRYSTAL, RUNE, SPELL);
		pvd.addTag(L2MSTagGen.QUICK_ACCESS).addTags(RUNE, SPELL);

	}

	public static void genEntityTag(RegistrateTagsProvider.IntrinsicImpl<EntityType<?>> pvd) {
		pvd.addTag(CASTER).add(
				EntityType.ZOMBIE,
				EntityType.HUSK,
				EntityType.SKELETON,
				EntityType.ZOMBIFIED_PIGLIN,
				EntityType.STRAY,
				EntityType.BOGGED,
				EntityType.WITHER_SKELETON
		);
	}

	public static void genBlockTag(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(SNOW).add(Blocks.SNOW, Blocks.SNOW_BLOCK);

		pvd.addTag(AMETHYST).add(
				Blocks.AMETHYST_BLOCK, Blocks.AMETHYST_CLUSTER, Blocks.BUDDING_AMETHYST,
				Blocks.SMALL_AMETHYST_BUD, Blocks.MEDIUM_AMETHYST_BUD, Blocks.LARGE_AMETHYST_BUD
		);

		pvd.addTag(QUARTZ).add(
				Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_BRICKS, Blocks.QUARTZ_PILLAR,
				Blocks.SMOOTH_QUARTZ, Blocks.CHISELED_QUARTZ_BLOCK,
				Blocks.QUARTZ_SLAB, Blocks.SMOOTH_QUARTZ_SLAB,
				Blocks.QUARTZ_STAIRS, Blocks.SMOOTH_QUARTZ_STAIRS
		);

		pvd.addTag(VINE).add(
				Blocks.VINE, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT, Blocks.TWISTING_VINES,
				Blocks.TWISTING_VINES_PLANT, Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT
		);

		pvd.addTag(BAMBOO).add(
				Blocks.BAMBOO, Blocks.BAMBOO_BLOCK, Blocks.BAMBOO_BUTTON, Blocks.BAMBOO_DOOR,
				Blocks.BAMBOO_FENCE, Blocks.BAMBOO_FENCE_GATE, Blocks.BAMBOO_HANGING_SIGN,
				Blocks.BAMBOO_MOSAIC, Blocks.BAMBOO_MOSAIC_SLAB, Blocks.BAMBOO_MOSAIC_STAIRS,
				Blocks.BAMBOO_WALL_SIGN, Blocks.BAMBOO_TRAPDOOR, Blocks.BAMBOO_SAPLING,
				Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_PRESSURE_PLATE, Blocks.STRIPPED_BAMBOO_BLOCK,
				Blocks.POTTED_BAMBOO
		);

	}

	public static TagKey<Block> block(String id) {
		return TagKey.create(Registries.BLOCK, GlimmeringTales.loc(id));
	}

	public static TagKey<Item> curio(String id) {
		return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, id));
	}

	public static TagKey<Item> item(String id) {
		return TagKey.create(Registries.ITEM, GlimmeringTales.loc(id));
	}

}
