package net.lugo.lightoverlay.util;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import net.lugo.lightoverlay.LightOverlay;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public final class RenderLayers {
    public static final RenderPipeline LIGHT_OVERLAY_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_TEX_COLOR_SNIPPET)
                    .withLocation(Identifier.of(LightOverlay.MOD_ID, "pipeline/light_overlay"))
                    .withCull(true)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(true)
                    .build()
    );

    public static final RenderLayer LIGHT_OVERLAY_RENDERLAYER = RenderLayer.of(
            LightOverlay.MOD_ID + "/light_overlay",
            1024,
            false,
            true,
            LIGHT_OVERLAY_PIPELINE,
            RenderLayer.MultiPhaseParameters.builder()
                    .build(false)
    );

    private RenderLayers() { }
}
