package dev.xkmc.glimmeringtales.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.glimmeringtales.compat.PatchouliCompat;
import dev.xkmc.glimmeringtales.content.block.altar.BaseRitualBlockEntity;
import dev.xkmc.glimmeringtales.content.block.infuser.InfuserBlockEntity;
import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipRegistry;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.content.research.core.GraphToServerPacket;
import dev.xkmc.glimmeringtales.content.research.core.HexGraphData;
import dev.xkmc.glimmeringtales.content.research.core.OpenGraphPacket;
import dev.xkmc.glimmeringtales.events.GTAttackListener;
import dev.xkmc.glimmeringtales.events.GTClickHandler;
import dev.xkmc.glimmeringtales.init.data.*;
import dev.xkmc.glimmeringtales.init.data.spell.GTSpells;
import dev.xkmc.glimmeringtales.init.reg.*;
import dev.xkmc.l2backpack.content.common.BaseBagItemHandler;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.l2serial.network.PacketHandler;
import dev.xkmc.l2serial.serialization.codec.CodecAdaptor;
import dev.xkmc.l2serial.serialization.custom_handler.Handlers;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.patchouli.api.PatchouliAPI;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GlimmeringTales.MODID)
@EventBusSubscriber(modid = GlimmeringTales.MODID, bus = EventBusSubscriber.Bus.MOD)
public class GlimmeringTales {

	public static final String MODID = "glimmeringtales";
	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			MODID, 1,
			e -> e.create(GraphToServerPacket.class, PacketHandler.NetDir.PLAY_TO_SERVER),
			e -> e.create(OpenGraphPacket.class, PacketHandler.NetDir.PLAY_TO_CLIENT)
	);
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Reg REG = new Reg(MODID);
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);
	public static GTDamageTypeGen DMG_GEN;

	public GlimmeringTales() {
		GTRegistries.register();
		GTItems.register();
		GTRecipes.register();
		GTEngine.register();
		GTEntities.register();
		if (ModList.get().isLoaded(PatchouliAPI.MOD_ID)) {
			PatchouliCompat.gen();
		}
		DMG_GEN = new GTDamageTypeGen(REGISTRATE);
		new GTClickHandler(loc("hex"));
	}

	private static void initHandlers() {
		Handlers.registerReg(NatureSpell.class, GTRegistries.SPELL);
		Handlers.registerReg(HexGraphData.class, GTRegistries.GRAPH);
		Handlers.registerReg(SpellAction.class, EngineRegistry.SPELL);
		Handlers.enableVanilla(Wrappers.cast(ProcessorType.class), EngineRegistry.PROCESSOR.registry().get());
		SpellTooltipRegistry.init();
	}

	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(GlimmeringTales::initHandlers);
		AttackEventHandler.register(3943, new GTAttackListener());
	}

	@SubscribeEvent
	public static void onAttribute(EntityAttributeModificationEvent event) {
		for (var e : event.getTypes()) {
			event.add(e, GTRegistries.MAX_MANA);
			event.add(e, GTRegistries.MANA_REGEN);
			event.add(e, GTRegistries.MAX_FOCUS);
			GTRegistries.ELEMENT.reg().holders().forEach(x -> event.add(e, x.value().getAffinity()));
		}
	}

	@SubscribeEvent
	public static void onDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
		var spell = new CodecAdaptor<>(NatureSpell.class);
		var graph = new CodecAdaptor<>(HexGraphData.class);
		event.dataPackRegistry(GTRegistries.SPELL, spell, spell);
		event.dataPackRegistry(GTRegistries.GRAPH, graph, graph);
	}

	@SubscribeEvent
	public static void registerCap(RegisterCapabilitiesEvent event) {
		event.registerItem(Capabilities.ItemHandler.ITEM, (stack, c) -> new BaseBagItemHandler(stack), GTItems.WAND);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, GTItems.ALTAR_BE.get(), BaseRitualBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, GTItems.MATRIX_BE.get(), BaseRitualBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, GTItems.INFUSER_BE.get(), InfuserBlockEntity::getItemHandler);
	}


	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void gatherDataInit(GatherDataEvent event) {
		initHandlers();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, GTRecipeGen::onRecipeGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, GTTagGen::genItemTag);
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, GTTagGen::genBlockTag);
		REGISTRATE.addDataGenerator(ProviderType.LANG, GTLang::addTranslations);
		REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, GTDataMapGen::genMap);
		REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, GTAdvGen::genAdvancements);
		REGISTRATE.addDataGenerator(ProviderType.LANG, GTSpells::addLang);
		REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, GTSpells::genMap);
		REGISTRATE.addDataGenerator(GTTagGen.BIOME, GTWorldGen::genBiomeTags);
		var init = REGISTRATE.getDataGenInitializer();
		DMG_GEN.generate();
		init.add(EngineRegistry.PROJECTILE, GTSpells::genProjectiles);
		init.add(EngineRegistry.SPELL, GTSpells::genSpells);
		init.add(GTRegistries.SPELL, GTSpells::genNature);
		init.add(GTRegistries.GRAPH, GTSpells::genGraph);
		GTWorldGen.genFeatures(init);
		init.addDependency(ProviderType.DATA_MAP, ProviderType.DYNAMIC);
		init.addDependency(GTTagGen.BIOME, ProviderType.DYNAMIC);

		var gen = event.getGenerator();
		var run = event.includeServer();
		var out = gen.getPackOutput();
		var file = event.getExistingFileHelper();
		var pvd = event.getLookupProvider();
		gen.addProvider(run, new GTSlotGen(out, file, pvd));
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}

}
