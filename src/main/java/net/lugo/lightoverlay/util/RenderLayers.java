package net.lugo.lightoverlay.util;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import net.lugo.lightoverlay.LightOverlay;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

public final class RenderLayers {
    public static final RenderPipeline LIGHT_OVERLAY_PIPELINE = RenderPipeline.builder(RenderPipelines.POSITION_TEX_COLOR_SNIPPET)
                    .withLocation(Identifier.of(LightOverlay.MOD_ID, "pipeline/light_overlay"))
                    .withCull(true)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(true)
                    .build();

    public static final Function<Identifier, RenderLayer> LIGHT_OVERLAY_RENDERLAYER = Util.memoize((Identifier texture) -> RenderLayer.of(
        LightOverlay.MOD_ID + "/light_overlay",
        RenderSetup.builder(
                LIGHT_OVERLAY_PIPELINE)
                .texture("Sampler0", texture)
                .crumbling().outlineMode(RenderSetup.OutlineMode.AFFECTS_OUTLINE).build()));

}