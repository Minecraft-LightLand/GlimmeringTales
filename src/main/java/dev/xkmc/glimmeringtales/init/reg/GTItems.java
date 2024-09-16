package dev.xkmc.glimmeringtales.init.reg;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.glimmeringtales.content.block.crop.LifeCrystalCrop;
import dev.xkmc.glimmeringtales.content.block.misc.*;
import dev.xkmc.glimmeringtales.content.block.ritual.*;
import dev.xkmc.glimmeringtales.content.item.curio.AttributeCurioItem;
import dev.xkmc.glimmeringtales.content.item.curio.AttributeData;
import dev.xkmc.glimmeringtales.content.item.curio.DamageTypeCurioItem;
import dev.xkmc.glimmeringtales.content.item.materials.AmethystResonator;
import dev.xkmc.glimmeringtales.content.item.materials.DepletedItem;
import dev.xkmc.glimmeringtales.content.item.rune.BlockRuneItem;
import dev.xkmc.glimmeringtales.content.item.rune.SpellCoreItem;
import dev.xkmc.glimmeringtales.content.item.rune.SpellRuneItem;
import dev.xkmc.glimmeringtales.content.item.wand.*;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTConfigs;
import dev.xkmc.glimmeringtales.init.data.GTDamageStates;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.l2backpack.init.L2Backpack;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import dev.xkmc.l2core.init.reg.varitem.VarHolder;
import dev.xkmc.l2core.init.reg.varitem.VarItemInit;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class GTItems {

	public static final SimpleEntry<CreativeModeTab> TAB = GlimmeringTales.REGISTRATE
			.buildL2CreativeTab("glimmeringtales", "Glimmering Tales", e ->
					e.icon(GTItems.CRYSTAL_EARTH::asStack));

	public static final VarItemInit<AttributeCurioItem> CURIOS;
	public static final VarItemInit<BlockRuneItem> RUNES;
	public static final VarItemInit<SpellRuneItem> SPELLS;
	public static final VarItemInit<WandHandleItem> WAND_HANDLES;

	public static final List<IWandCoreItem> CORES = new ArrayList<>();
	public static final List<WandHandleItem> HANDLES = new ArrayList<>();

	public static final ItemEntry<SpellCoreItem> CRYSTAL_NATURE,
			CRYSTAL_LIFE, CRYSTAL_FLAME, CRYSTAL_EARTH, CRYSTAL_WINTERSTORM,
			CRYSTAL_OCEAN, CRYSTAL_THUNDER;
	public static final ItemEntry<DepletedItem> DEPLETED_FLAME, DEPLETED_WINTERSTORM;
	public static final BlockEntry<LifeCrystalCrop> CRYSTAL_VINE;
	public static final BlockEntry<StruckLogBlock> STRUCK_LOG;
	public static final BlockEntry<LeavesBlock> STRUCK_LEAVES;
	public static final BlockEntry<SaplingBlock> STRUCK_SAPLING;
	public static final ItemEntry<AmethystResonator> RESONATOR;

	public static final BlockEntry<DelegateBlock> RITUAL_ALTAR, RITUAL_MATRIX;
	public static final BlockEntityEntry<NatureSideBlockEntity> ALTAR_BE;
	public static final BlockEntityEntry<NatureCoreBlockEntity> MATRIX_BE;

	public static final ItemEntry<RuneWandItem> WAND;
	public static final MenuEntry<WandMenu> WAND_MENU;

	public static final ItemEntry<DamageTypeCurioItem> GLOVE_OF_SORCERER, GLOVE_OF_ABYSS, GLOVE_OF_OCEAN, GLOVE_OF_THUNDER;

	public static final VarHolder<BlockRuneItem>
			RUNE_BAMBOO, RUNE_CACTUS, RUNE_FLOWER, RUNE_VINE, RUNE_HAYBALE,
			RUNE_SAND, RUNE_GRAVEL, RUNE_QUARTZ, RUNE_CLAY, RUNE_STONE, RUNE_DRIPSTONE, RUNE_AMETHYST,
			RUNE_MAGMA, RUNE_NETHERRACK, RUNE_SOUL_SAND,
			RUNE_SNOW, RUNE_ICE, RUNE_PACKED_ICE, RUNE_BLUE_ICE, RUNE_POWDER_SNOW,
			RUNE_SPONGE, RUNE_CORAL_REEF,
			RUNE_THUNDER;

	public static final VarHolder<SpellRuneItem>
			HELL_MARK, LAVA_BURST, FLAME_DASH,
			WINTER_STORM, SNOW_TORNADO, ICY_FLASH,
			STONE_BRIDGE, AMETHYST_PENETRATION, EARTHQUAKE, METEOR,
			OCEAN_SHELTER,
			THUNDERSTORM, CHARGE_BURST;

	public static final VarHolder<WandHandleItem>
			WOOD_WAND, LIFE_WAND, GOLD_WAND, OCEAN_WAND,
			THUNDER_WAND, NETHER_WAND;


	public static final BlockEntry<SelfDestroyTransparent> FAKE_GLASS;
	public static final BlockEntry<DelegateBlock> CLAY_CARPET, FAKE_STONE, FAKE_BAMBOO,
			MAGMA_STONE, MAGMA_DEEPSLATE, MAGMA_NETHERRACK;
	public static final BlockEntry<Block> DUMMY_METEOR;

	private static final DCReg DC = DCReg.of(GlimmeringTales.REG);

	public static final DCVal<Integer> PROGRESS = DC.intVal("progress");
	public static final DCVal<Holder<Item>> WAND_HANDLE = DC.registry("handle", BuiltInRegistries.ITEM);

	public enum Curios implements ItemLike {
		GOLDEN_RING("ring", AttributeData.of(
				AttributeData.base(GTRegistries.MAX_MANA, 0.1)
		)),
		RING_OF_REGENERATION("ring", AttributeData.of(
				AttributeData.base(GTRegistries.MAX_MANA, 0.1),
				AttributeData.base(GTRegistries.MANA_REGEN, 0.3)
		)),
		RING_OF_NATURE("ring", AttributeData.of(
				AttributeData.base(GTRegistries.MAX_MANA, 0.1),
				AttributeData.add(GTRegistries.EARTH.attr(), 0.1),
				AttributeData.add(GTRegistries.LIFE.attr(), 0.1),
				AttributeData.add(GTRegistries.FLAME.attr(), 0.1),
				AttributeData.add(GTRegistries.SNOW.attr(), 0.1)
		)),
		RING_OF_EARTH("ring", AttributeData.of(
				AttributeData.base(GTRegistries.MAX_MANA, 0.2),
				AttributeData.add(GTRegistries.EARTH.attr(), 0.5)
		)),
		RING_OF_LIFE("ring", AttributeData.of(
				AttributeData.base(GTRegistries.MAX_MANA, 0.2),
				AttributeData.add(GTRegistries.LIFE.attr(), 0.5)
		)),
		RING_OF_FLAME("ring", AttributeData.of(
				AttributeData.base(GTRegistries.MAX_MANA, 0.2),
				AttributeData.add(GTRegistries.FLAME.attr(), 0.5)
		)),
		RING_OF_SNOW("ring", AttributeData.of(
				AttributeData.base(GTRegistries.MAX_MANA, 0.2),
				AttributeData.add(GTRegistries.SNOW.attr(), 0.5)
		)),
		RING_OF_OCEAN("ring", AttributeData.of(
				AttributeData.base(GTRegistries.MAX_MANA, 0.2),
				AttributeData.add(GTRegistries.OCEAN.attr(), 0.5)
		)),
		RING_OF_THUNDER("ring", AttributeData.of(
				AttributeData.base(GTRegistries.MAX_MANA, 0.3),
				AttributeData.add(GTRegistries.THUNDER.attr(), 0.5)
		)),
		CHARM_OF_STRENGTH("charm", AttributeData.of(
				AttributeData.add(L2DamageTracker.MAGIC_FACTOR, 0.5)
		)),
		CHARM_OF_CAPACITY("charm", AttributeData.of(
				AttributeData.base(GTRegistries.MAX_MANA, 0.5)
		)),
		CHARM_OF_REGENERATION("charm", AttributeData.of(
				AttributeData.base(GTRegistries.MANA_REGEN, 0.5)
		)),
		CHARM_OF_NATURE("charm", AttributeData.of(
				AttributeData.base(GTRegistries.MANA_REGEN, 0.1)
		)),
		CHARM_OF_EARTH("charm", AttributeData.of(
				AttributeData.base(GTRegistries.MANA_REGEN, 0.1),
				AttributeData.add(L2DamageTracker.REDUCTION, -0.2)
		)),
		CHARM_OF_LIFE("charm", AttributeData.of(
				AttributeData.base(GTRegistries.MANA_REGEN, 0.1),
				AttributeData.add(L2DamageTracker.REGEN, 0.5)
		)),
		CHARM_OF_FLAME("charm", AttributeData.of(
				AttributeData.add(L2DamageTracker.FIRE_FACTOR, 1),
				AttributeData.add(L2DamageTracker.EXPLOSION_FACTOR, 1)
		)),
		CHARM_OF_SNOW("charm", AttributeData.of(
				AttributeData.add(L2DamageTracker.REDUCTION, -0.1),
				AttributeData.add(L2DamageTracker.FREEZING_FACTOR, 1)
		)),
		CHARM_OF_OCEAN("charm", AttributeData.of(
				AttributeData.base(GTRegistries.MANA_REGEN, 0.1),
				AttributeData.add(L2DamageTracker.MAGIC_FACTOR, 0.5)
		)),
		CHARM_OF_THUNDER("charm", AttributeData.of(
				AttributeData.base(GTRegistries.MANA_REGEN, 0.2),
				AttributeData.add(L2DamageTracker.LIGHTNING_FACTOR, 1)
		)),
		;


		public final VarHolder<AttributeCurioItem> item;

		Curios(String part, AttributeData data) {
			item = curio(name().toLowerCase(Locale.ROOT), part, data);
		}

		@Override
		public Item asItem() {
			return item.asItem();
		}

		private static void register() {

		}

	}

	static {

		{
			CRYSTAL_NATURE = core("crystal_of_nature");
			CRYSTAL_EARTH = core("crystal_of_earth");
			CRYSTAL_LIFE = core("crystal_of_life");
			CRYSTAL_FLAME = core("crystal_of_flame");
			CRYSTAL_WINTERSTORM = core("crystal_of_winterstorm");
			CRYSTAL_OCEAN = core("crystal_of_ocean");
			CRYSTAL_THUNDER = core("crystal_of_thunder");
			DEPLETED_FLAME = GlimmeringTales.REGISTRATE.item("depleted_crystal_of_flame", p ->
					new DepletedItem(p, () -> Blocks.LAVA, GTConfigs.SERVER.crystalOfFlameRequirement,
							CRYSTAL_FLAME::get, () -> SoundEvents.BUCKET_FILL_LAVA)
			).register();
			DEPLETED_WINTERSTORM = GlimmeringTales.REGISTRATE.item("depleted_crystal_of_winterstorm", p ->
					new DepletedItem(p, () -> Blocks.POWDER_SNOW, GTConfigs.SERVER.crystalOfWinterstormRequirement,
							CRYSTAL_WINTERSTORM::get, () -> SoundEvents.BUCKET_FILL_POWDER_SNOW)
			).register();

			CRYSTAL_VINE = GlimmeringTales.REGISTRATE.block("crystal_vine", LifeCrystalCrop::new)
					.properties(p -> p.mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak()
							.sound(SoundType.CROP).pushReaction(PushReaction.DESTROY))
					.item(ItemNameBlockItem::new).model((ctx, pvd) -> pvd.generated(ctx))
					.lang("Seed of Nature").build()
					.blockstate(LifeCrystalCrop::buildState)
					.loot(LifeCrystalCrop::builtLoot)
					.tag(BlockTags.CROPS)
					.register();

			STRUCK_LOG = GlimmeringTales.REGISTRATE.block("struck_log", p ->
							new StruckLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)))
					.blockstate((ctx, pvd) -> pvd.logBlock(ctx.get()))
					.tag(BlockTags.LOGS_THAT_BURN).simpleItem().register();

			STRUCK_LEAVES = GlimmeringTales.REGISTRATE.block("struck_leaves", p ->
							new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(),
							pvd.models().leaves(ctx.getName(), pvd.modLoc("block/" + ctx.getName()))))
					.tag(BlockTags.LEAVES)
					.loot((pvd, block) -> pvd.add(block, pvd.createLeavesDrops(block, Blocks.OAK_SAPLING, 1 / 20f, 1 / 16f, 1 / 12f, 1 / 10f)))
					.simpleItem()
					.register();

			STRUCK_SAPLING = GlimmeringTales.REGISTRATE.block("struck_sapling", p -> new SaplingBlock(
							new TreeGrower("struck_sapling", Optional.empty(), Optional.of(GTWorldGen.CF_TREE), Optional.empty()),
							BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models()
							.cross(ctx.getName(), pvd.modLoc("block/" + ctx.getName()))
							.renderType("cutout")))
					.tag(BlockTags.SAPLINGS)
					.item().model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("block/" + ctx.getName()))).tag(ItemTags.SAPLINGS).build()
					.register();

			RESONATOR = GlimmeringTales.REGISTRATE.item("amethyst_resonator", p ->
							new AmethystResonator(p.stacksTo(1)))
					.register();
		}

		{
			RITUAL_ALTAR = GlimmeringTales.REGISTRATE.block("ritual_altar", p ->
							DelegateBlock.newBaseBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).lightLevel(s -> 11)
									.noOcclusion(), RitualBlock.ITEM, RitualBlock.LINK, RitualBlock.SIDE))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/ritual_altar")))
							.texture("all", "block/" + ctx.getName())
							.texture("particle", pvd.mcLoc("block/deepslate"))
							.renderType("cutout")))
					.simpleItem()
					.register();

			ALTAR_BE = GlimmeringTales.REGISTRATE.blockEntity("ritual_altar", NatureSideBlockEntity::new)
					.validBlock(RITUAL_ALTAR)
					.renderer(() -> RitualRenderer::new)
					.register();

			RITUAL_MATRIX = GlimmeringTales.REGISTRATE.block("ritual_matrix", p ->
							DelegateBlock.newBaseBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).lightLevel(s -> 11).noOcclusion(),
									RitualBlock.ITEM, RitualBlock.LINK, RitualBlock.START, RitualBlock.CORE, RitualBlock.GUIDE))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/ritual_matrix")))
							.texture("all", "block/" + ctx.getName())
							.texture("particle", pvd.mcLoc("block/deepslate"))
							.renderType("cutout")))
					.simpleItem()
					.register();

			MATRIX_BE = GlimmeringTales.REGISTRATE.blockEntity("ritual_matrix", NatureCoreBlockEntity::new)
					.validBlock(RITUAL_MATRIX)
					.renderer(() -> MatrixRenderer::new)
					.register();
		}

		{
			WAND = GlimmeringTales.REGISTRATE.item("wand",
							p -> new RuneWandItem(p.stacksTo(1).fireResistant()))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName())
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/wand")))
							.guiLight(BlockModel.GuiLight.FRONT)
							.texture("particle", "minecraft:item/stick"))
					.clientExtension(() -> () -> GTBEWLR.EXTENSIONS)
					.tab(TAB.key(), (m, x) -> m.get().fillCreativeTabs(x))
					.lang(" Wand")
					.register();

			WAND_MENU = L2Backpack.REGISTRATE.menu("wand", WandMenu::fromNetwork, () -> WandScreen::new).register();

		}

		{
			GLOVE_OF_SORCERER = GlimmeringTales.REGISTRATE.item("glove_of_sorcerer",
							p -> new DamageTypeCurioItem(p.stacksTo(1).fireResistant(),
									e -> GTDamageStates.MAGIC, GTLang.TOOLTIP_MAGIC::get))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(GTTagGen.curio("hands"), GTTagGen.UNIQUE)
					.dataMap(GTRegistries.ITEM_ATTR.reg(), AttributeData.of(
							AttributeData.add(L2DamageTracker.MAGIC_FACTOR, 0.25)
					)).lang("Glove of Sorcerer")
					.register();

			GLOVE_OF_ABYSS = GlimmeringTales.REGISTRATE.item("glove_of_abyss",
							p -> new DamageTypeCurioItem(p.stacksTo(1).fireResistant(),
									e -> DefaultDamageState.BYPASS_MAGIC, GTLang.TOOLTIP_ABYSS::get))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(GTTagGen.curio("hands"), GTTagGen.UNIQUE)
					.dataMap(GTRegistries.ITEM_ATTR.reg(), AttributeData.of(
							AttributeData.total(GTRegistries.MANA_REGEN, -0.5)
					)).lang("Glove of Abyss")
					.register();

			GLOVE_OF_OCEAN = GlimmeringTales.REGISTRATE.item("glove_of_ocean",
							p -> new DamageTypeCurioItem(p.stacksTo(1).fireResistant(),
									e -> e.is(GTRegistries.OCEAN.damgeTag()) ? DefaultDamageState.BYPASS_COOLDOWN : null,
									() -> GTLang.TOOLTIP_COOLDOWN.get(GTRegistries.OCEAN.get().coloredDesc())))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(GTTagGen.curio("hands"), GTTagGen.UNIQUE)
					.dataMap(GTRegistries.ITEM_ATTR.reg(), AttributeData.of(
							AttributeData.total(GTRegistries.MANA_REGEN, -0.25)
					)).lang("Glove of Ocean")
					.register();

			GLOVE_OF_THUNDER = GlimmeringTales.REGISTRATE.item("glove_of_thunder",
							p -> new DamageTypeCurioItem(p.stacksTo(1).fireResistant(),
									e -> e.is(GTRegistries.THUNDER.damgeTag()) ? DefaultDamageState.BYPASS_COOLDOWN : null,
									() -> GTLang.TOOLTIP_COOLDOWN.get(GTRegistries.THUNDER.get().coloredDesc())))
					.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
					.tag(GTTagGen.curio("hands"), GTTagGen.UNIQUE)
					.dataMap(GTRegistries.ITEM_ATTR.reg(), AttributeData.of(
							AttributeData.total(GTRegistries.MANA_REGEN, -0.25)
					)).lang("Glove of Thunder")
					.register();
		}

		{

			CURIOS = VarItemInit.setup(GlimmeringTales.REGISTRATE, GlimmeringTales.loc("curios"),
					e -> new AttributeCurioItem(new Item.Properties().stacksTo(1).fireResistant()),
					(rl, b) -> b.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/curio/" + ctx.getName())))
							.lang(RegistrateLangProvider.toEnglishName(rl.getPath()))
			);

			RUNES = VarItemInit.setup(GlimmeringTales.REGISTRATE, GlimmeringTales.loc("block_runes"),
					e -> new BlockRuneItem(new Item.Properties().fireResistant()),
					(rl, b) -> b.tag(GTTagGen.RUNE).model((ctx, pvd) -> {
								pvd.generated(ctx, pvd.modLoc("item/rune/" + ctx.getName()));
								pvd.getBuilder(ctx.getName() + "_core").parent(
												new ModelFile.UncheckedModelFile(pvd.modLoc("custom/rune_core")))
										.texture("all", pvd.modLoc("item/rune/" + ctx.getName()))
										.renderType("cutout");
							})
							.onRegister(CORES::add)
			);

			SPELLS = VarItemInit.setup(GlimmeringTales.REGISTRATE, GlimmeringTales.loc("spell_runes"),
					e -> new SpellRuneItem(new Item.Properties().fireResistant(), e),
					(rl, b) -> b.tag(GTTagGen.SPELL)
							.lang("Rune: " + RegistrateLangProvider.toEnglishName(rl.getPath()))
							.model((ctx, pvd) -> {
								pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName()));
								pvd.getBuilder(ctx.getName() + "_core").parent(
												new ModelFile.UncheckedModelFile(pvd.modLoc("custom/rune_core")))
										.texture("all", pvd.modLoc("item/spell/" + ctx.getName()))
										.renderType("cutout");
							}).onRegister(CORES::add)
			);

			WAND_HANDLES = VarItemInit.setup(GlimmeringTales.REGISTRATE, GlimmeringTales.loc("wand_handles"),
					e -> new WandHandleItem(new Item.Properties()),
					(rl, b) -> b.removeTab(TAB.key())
							.model((ctx, pvd) -> {
								pvd.handheld(ctx, pvd.modLoc("item/handle/" + ctx.getName()));
								pvd.getBuilder(ctx.getName() + "_icon").parent(
												new ModelFile.UncheckedModelFile(pvd.mcLoc("item/handheld")))
										.texture("layer0", pvd.modLoc("item/handle/" + ctx.getName()));
								pvd.getBuilder(ctx.getName() + "_handle").parent(
												new ModelFile.UncheckedModelFile(pvd.modLoc("custom/" + ctx.getName())))
										.texture("all", pvd.modLoc("item/wand/" + ctx.getName()))
										.renderType("cutout");
							})
							.onRegister(HANDLES::add)
			);

			Curios.register();

		}

		{

			RUNE_BAMBOO = rune("bamboo", "Rune: Bamboo");
			RUNE_CACTUS = rune("cactus", "Rune: Cactus");
			RUNE_FLOWER = rune("flower", "Rune: Flower");
			RUNE_VINE = rune("vine", "Rune: Vine");
			RUNE_HAYBALE = rune("hay_bale", "Rune: Hay Bale");

			RUNE_SAND = rune("sand", "Rune: Sand");
			RUNE_GRAVEL = rune("gravel", "Rune: Gravel");
			RUNE_CLAY = rune("clay", "Rune: Clay");
			RUNE_STONE = rune("stone", "Rune: Stone");
			RUNE_QUARTZ = rune("quartz", "Rune: Quartz");
			RUNE_DRIPSTONE = rune("dripstone", "Rune: Stalactite");
			RUNE_AMETHYST = rune("amethyst", "Rune: Amethyst");

			RUNE_MAGMA = rune("magma", "Rune: Magma Block");
			RUNE_NETHERRACK = rune("netherrack", "Rune: Netherrack");
			RUNE_SOUL_SAND = rune("soul_sand", "Rune: Soul Sand");

			RUNE_SNOW = rune("snow", "Rune: Snow");
			RUNE_POWDER_SNOW = rune("powder_snow", "Rune: Powder Snow");
			RUNE_ICE = rune("ice", "Rune: Ice");
			RUNE_PACKED_ICE = rune("packed_ice", "Rune: Packed Ice");
			RUNE_BLUE_ICE = rune("blue_ice", "Rune: Blue Ice");

			RUNE_SPONGE = rune("sponge", "Rune: Sponge");
			RUNE_CORAL_REEF = rune("coral_reef", "Rune: Coral Reef");

			RUNE_THUNDER = rune("thunder", "Rune: Thunder");

			HELL_MARK = spell("hell_mark");
			LAVA_BURST = spell("lava_burst");
			FLAME_DASH = spell("flame_dash");
			WINTER_STORM = spell("winter_storm");
			SNOW_TORNADO = spell("snow_tornado");
			ICY_FLASH = spell("icy_flash");
			STONE_BRIDGE = spell("stone_bridge");
			AMETHYST_PENETRATION = spell("amethyst_penetration");
			EARTHQUAKE = spell("earthquake");
			METEOR = spell("meteor");
			OCEAN_SHELTER = spell("ocean_shelter");
			THUNDERSTORM = spell("thunderstorm");
			CHARGE_BURST = spell("charge_burst");

			WOOD_WAND = handle("wood_wand", 0.25f, 0.75f, "Wooden");
			LIFE_WAND = handle("life_wand", 0.25f, 0.87f, "Bamboo");
			GOLD_WAND = handle("gold_wand", 0.25f, 0.75f, "Golden");
			OCEAN_WAND = handle("ocean_wand", 0.25f, 0.92f, "Ocean");
			THUNDER_WAND = handle("thunder_wand", 0.23f, 0.70f, "Thunder");
			NETHER_WAND = handle("nether_wand", 0.22f, 0.75f, "Nether");
		}

		{
			CLAY_CARPET = GlimmeringTales.REGISTRATE.block("clay_carpet",
							p -> DelegateBlock.newBaseBlock(p, new SelfDestroyImpl(), new ClayCarpetImpl(),
									new StuckEntityMethod(new Vec3(0.05, 1f, 0.05))))
					.properties(p -> p.mapColor(MapColor.CLAY).strength(0.6f).speedFactor(0.2F).jumpFactor(0.2f)
							.sound(SoundType.GRAVEL).pushReaction(PushReaction.DESTROY).noLootTable())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().carpet(ctx.getName(), pvd.mcLoc("block/clay"))))
					.register();

			FAKE_STONE = GlimmeringTales.REGISTRATE.block("stone",
							p -> DelegateBlock.newBaseBlock(p, new SelfDestroyImpl()))
					.properties(p -> BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noLootTable())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().cubeAll(ctx.getName(), pvd.mcLoc("block/stone"))))
					.register();

			FAKE_GLASS = GlimmeringTales.REGISTRATE.block("glass", SelfDestroyTransparent::new)
					.properties(p -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noLootTable())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(),
							pvd.models().withExistingParent(ctx.getName(), pvd.mcLoc("block/glass")).renderType("cutout")))
					.register();

			FAKE_BAMBOO = GlimmeringTales.REGISTRATE.block("bamboo",
							p -> DelegateBlock.newBaseBlock(p, new SelfDestroyImpl(), new BamBooImpl()))
					.properties(p -> BlockBehaviour.Properties.of()
							.mapColor(MapColor.PLANT).forceSolidOn().randomTicks().instabreak()
							.strength(1.0F).sound(SoundType.BAMBOO).noOcclusion()
							.dynamicShape().offsetType(BlockBehaviour.OffsetType.XZ)
							.ignitedByLava().pushReaction(PushReaction.DESTROY).noLootTable())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(),
							pvd.models().withExistingParent(ctx.getName(), pvd.mcLoc("block/bamboo1_age1")).renderType("cutout")))
					.register();

			DUMMY_METEOR = GlimmeringTales.REGISTRATE.block("meteor", p ->
							new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noLootTable()))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get())).register();

			MAGMA_STONE = magma("stone");
			MAGMA_DEEPSLATE = magma("deepslate");
			MAGMA_NETHERRACK = magma("netherrack");

		}

	}

	private static ItemEntry<SpellCoreItem> core(String id) {
		var ans = GlimmeringTales.REGISTRATE.item(id, SpellCoreItem::new)
				.model((ctx, pvd) -> {
					pvd.generated(ctx, pvd.modLoc("item/crystal/" + ctx.getName()));
					pvd.getBuilder(ctx.getName() + "_core").parent(
									new ModelFile.UncheckedModelFile(pvd.modLoc("custom/rune_core")))
							.texture("all", pvd.modLoc("item/core/" + ctx.getName()))
							.renderType("cutout");
				})
				.tag(GTTagGen.CRYSTAL)
				.onRegister(CORES::add)
				.register();
		return ans;
	}

	private static VarHolder<BlockRuneItem> rune(String id, String name) {
		return RUNES.add(new VarHolder<>(id, (rl, b) -> b.lang(name)));
	}

	private static VarHolder<SpellRuneItem> spell(String id) {
		return SPELLS.add(new VarHolder<>(id, (rl, b) -> b));
	}

	private static VarHolder<WandHandleItem> handle(String id, float size, float offset, String name) {
		return WAND_HANDLES.add(new VarHolder<>(id, (rl, b) -> b
				.dataMap(GTRegistries.WAND_MODEL.reg(), new WandData(size, offset)).lang(name)));
	}

	private static VarHolder<AttributeCurioItem> curio(String id, String part, AttributeData data) {
		return CURIOS.add(new VarHolder<>(id, (rl, b) -> b
				.dataMap(GTRegistries.ITEM_ATTR.reg(), data)
				.tag(GTTagGen.curio(part), GTTagGen.UNIQUE)));
	}

	private static BlockEntry<DelegateBlock> magma(String id) {
		return GlimmeringTales.REGISTRATE.block("magma_block_for_" + id,
						p -> DelegateBlock.newBaseBlock(p, new MagmaImpl(), new SelfReplaceImpl()))
				.properties(p -> BlockBehaviour.Properties.ofFullCopy(Blocks.MAGMA_BLOCK).noLootTable())
				.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().cubeAll(ctx.getName(), pvd.mcLoc("block/magma"))))
				.tag(GTTagGen.FAKE_MAGMA)
				.lang("Magma Block")
				.register();
	}

	public static void register() {

	}

}
