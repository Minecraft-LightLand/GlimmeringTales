package dev.xkmc.glimmeringtales.content.block.ritual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public class MatrixRenderer<T extends NatureCoreBlockEntity> extends RitualRenderer<T> {

	private static final ResourceLocation END_CRYSTAL_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/end_crystal/end_crystal.png");
	private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(END_CRYSTAL_LOCATION);
	private static final float SIN_45 = (float) Math.sin(Math.PI / 4);

	private final ModelPart glass, cube;

	public MatrixRenderer(BlockEntityRendererProvider.Context context) {
		super(context);

		ModelPart modelpart = context.bakeLayer(ModelLayers.END_CRYSTAL);
		this.glass = modelpart.getChild("glass");
		this.cube = modelpart.getChild("cube");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("glass", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
		partdefinition.addOrReplaceChild("cube", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 64, 32);
	}


	@Override
	public void render(T be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		super.render(be, pTick, pose, buffer, light, overlay);
		light = LightTexture.FULL_BRIGHT;
		pose.pushPose();
		float f1 = be.getTime(pTick);
		float f2 = 0.875F;
		VertexConsumer vc = buffer.getBuffer(RENDER_TYPE);
		pose.pushPose();
		pose.translate(0.5f, -0.5f, 0.5f);
		pose.scale(2.0F, 2.0F, 2.0F);
		pose.translate(0.0F, -0.5F, 0.0F);
		pose.mulPose(Axis.YP.rotationDegrees(f1));
		pose.translate(0.0F, 1.5F, 0.0F);
		pose.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SIN_45, 0.0F, SIN_45));
		this.glass.render(pose, vc, light, overlay);
		pose.scale(f2, f2, f2);
		pose.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SIN_45, 0.0F, SIN_45));
		pose.mulPose(Axis.YP.rotationDegrees(f1));
		this.glass.render(pose, vc, light, overlay);
		pose.scale(f2, f2, f2);
		pose.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SIN_45, 0.0F, SIN_45));
		pose.mulPose(Axis.YP.rotationDegrees(f1));
		this.glass.render(pose, vc, light, overlay);
		pose.popPose();
		pose.popPose();
	}
}