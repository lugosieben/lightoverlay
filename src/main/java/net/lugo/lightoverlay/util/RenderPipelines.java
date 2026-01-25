package net.lugo.lightoverlay.util;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.lugo.lightoverlay.LightOverlay;
import net.minecraft.resources.Identifier;

public final class RenderPipelines {
    public static final RenderPipeline LIGHT_OVERLAY_PIPELINE = RenderPipeline.builder(net.minecraft.client.renderer.RenderPipelines.MATRICES_FOG_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(LightOverlay.MOD_ID, "pipeline/light_overlay"))
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withSampler("Sampler0")
            .withBlend(BlendFunction.TRANSLUCENT)
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
            .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
            .withCull(true)
            .withDepthWrite(false)
            .build();

    public static void registerWithIris() {
        IrisUtil.assignPipeline(LIGHT_OVERLAY_PIPELINE, IrisPipeline.TEXTURED);
    }
}