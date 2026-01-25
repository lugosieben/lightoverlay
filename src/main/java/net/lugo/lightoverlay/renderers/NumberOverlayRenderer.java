package net.lugo.lightoverlay.renderers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lugo.lightoverlay.LightOverlay;
import net.lugo.lightoverlay.OverlayRenderer;
import net.lugo.lightoverlay.config.ModConfig;
import net.lugo.lightoverlay.util.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

public class NumberOverlayRenderer extends OverlayRenderer {
    private static final Identifier NUMBERS_TEXTURE = Identifier.fromNamespaceAndPath(LightOverlay.MOD_ID, "textures/numbers.png");

    private static final float TILE_SIZE = 1f / 16f;

    public NumberOverlayRenderer() {
        super(RenderPipelines.LIGHT_OVERLAY_PIPELINE, NUMBERS_TEXTURE);
    }

    @Override
    protected void onAddBlock(Matrix4f positionMatrix, float rf, float gf, float bf, int lightLevel, BlockPos pos, boolean isNearby) {
        if (!isNearby && ModConfig.enableIrisFlickerFix) {
            getPoseStack().translate(0, 3E-2, 0);
        } else {
            getPoseStack().translate(0, 5E-3, 0);
        }

        float uStart = lightLevel * TILE_SIZE;
        float uEnd = uStart + TILE_SIZE;

        VertexConsumer vc = this.buffer;
        vc.addVertex(positionMatrix, 0, 1, 0).setColor(rf, gf, bf, 1f).setUv(uStart, 0f).setUv2(0, 0);
        vc.addVertex(positionMatrix, 0, 1, 1).setColor(rf, gf, bf, 1f).setUv(uStart, 1f).setUv2(0, 0);
        vc.addVertex(positionMatrix, 1, 1, 1).setColor(rf, gf, bf, 1f).setUv(uEnd, 1f).setUv2(0, 0);
        vc.addVertex(positionMatrix, 1, 1, 0).setColor(rf, gf, bf, 1f).setUv(uEnd, 0f).setUv2(0, 0);
    }

    @Override
    protected void onStartBatch() {}

    @Override
    protected void onEndBatch() {}
}