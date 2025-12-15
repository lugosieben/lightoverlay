package net.lugo.lightoverlay.util;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import net.lugo.lightoverlay.LightOverlay;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

public final class RenderLayers {
    public static final RenderPipeline LIGHT_OVERLAY_PIPELINE = RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(LightOverlay.MOD_ID, "pipeline/light_overlay"))
                    .withCull(true)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(true)
                    .build();

    public static final Function<Identifier, RenderType> LIGHT_OVERLAY_RENDERLAYER = Util.memoize((Identifier texture) -> RenderType.create(
        LightOverlay.MOD_ID + "/light_overlay",
        RenderSetup.builder(
                LIGHT_OVERLAY_PIPELINE)
                .withTexture("Sampler0", texture)
                .affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup()));

}