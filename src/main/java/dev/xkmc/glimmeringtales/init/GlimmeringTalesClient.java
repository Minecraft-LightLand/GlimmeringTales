package dev.xkmc.glimmeringtales.init;

import dev.xkmc.glimmeringtales.content.core.searcher.SearcherDeco;
import dev.xkmc.glimmeringtales.content.item.wand.SpellCastingOverlay;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.util.Optional;

@EventBusSubscriber(value = Dist.CLIENT, modid = GlimmeringTales.MODID, bus = EventBusSubscriber.Bus.MOD)
public class GlimmeringTalesClient {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
		});
	}

	@SubscribeEvent
	public static void onOverlayRegister(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.CROSSHAIR, GlimmeringTales.loc("mana"), new SpellCastingOverlay());
	}


	@SubscribeEvent
	public static void registerItemDecoration(RegisterItemDecorationsEvent event) {
		event.register(GTItems.RESONATOR.get(), new SearcherDeco());
	}


	@SubscribeEvent
	public static void onModelLoad(ModelEvent.RegisterAdditional event) {
		for (var item : GTItems.CORES) {
			event.register(item.model());
		}
		for (var item : GTItems.HANDLES) {
			event.register(item.icon());
			event.register(item.model());
			Optional.ofNullable(item.shadow()).ifPresent(event::register);
		}
	}

}
