package net.lugo.lightoverlay.renderers;

import net.lugo.lightoverlay.LightOverlay;
import net.lugo.lightoverlay.OverlayRenderer;
import net.lugo.lightoverlay.util.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;

public class CrossOverlayRenderer extends OverlayRenderer {
    private static final Identifier CROSS_TEXTURE = Identifier.of(LightOverlay.MOD_ID, "textures/cross.png");

    public CrossOverlayRenderer() {
        super(RenderLayers.LIGHT_OVERLAY_RENDERLAYER, CROSS_TEXTURE);
    }

    @Override
    protected void onAddBlock(Matrix4f positionMatrix, float rf, float gf, float bf, int lightLevel, BlockPos pos) {
        getMatrixStack().translate(0, 1E-3, 0);

        VertexConsumer vc = this.vertexConsumer;
        vc.vertex(positionMatrix, 0, 1, 0).color(rf, gf, bf, 1f).texture(0f, 0f).light(0, 0);
        vc.vertex(positionMatrix, 0, 1, 1).color(rf, gf, bf, 1f).texture(0f, 1f).light(0, 0);
        vc.vertex(positionMatrix, 1, 1, 1).color(rf, gf, bf, 1f).texture(1f, 1f).light(0, 0);
        vc.vertex(positionMatrix, 1, 1, 0).color(rf, gf, bf, 1f).texture(1f, 0f).light(0, 0);
    }
}