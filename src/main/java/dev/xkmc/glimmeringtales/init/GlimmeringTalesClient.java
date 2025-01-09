package dev.xkmc.glimmeringtales.init;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.xkmc.glimmeringtales.content.core.searcher.SearcherDeco;
import dev.xkmc.glimmeringtales.content.item.wand.SpellCastingOverlay;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.event.RegisterNamedRenderTypesEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

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
	public static void onModelRenderType(RegisterNamedRenderTypesEvent event) {
		event.register(GlimmeringTales.loc("ender"), RenderType.CUTOUT, GTRenderTypes.ENDER_ENTITY);
	}

	@SubscribeEvent
	public static void onModelLoad(ModelEvent.RegisterAdditional event) {
		for (var item : GTItems.CORES) {
			event.register(item.model());
		}
		for (var item : GTItems.HANDLES) {
			event.register(item.icon());
			event.register(item.model());
		}
	}

	private static abstract class GTRenderTypes extends RenderType {

		public static final RenderType ENDER_ENTITY = create(
				"glimmeringtales_ender_entity",
				DefaultVertexFormat.NEW_ENTITY,
				VertexFormat.Mode.QUADS,
				1536,
				true,
				false,
				RenderType.CompositeState.builder()
						.setShaderState(RENDERTYPE_ENTITY_CUTOUT_SHADER)
						.setTransparencyState(NO_TRANSPARENCY)
						.setLightmapState(LIGHTMAP)
						.setOverlayState(OVERLAY)
						.createCompositeState(true)
		);

		public GTRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
			super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
		}
	}

}
