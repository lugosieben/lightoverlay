package net.lugo.lightoverlay.renderers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lugo.lightoverlay.LightOverlay;
import net.lugo.lightoverlay.OverlayRenderer;
import net.lugo.lightoverlay.util.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

public class CrossOverlayRenderer extends OverlayRenderer {
    private static final Identifier CROSS_TEXTURE = Identifier.fromNamespaceAndPath(LightOverlay.MOD_ID, "textures/cross.png");

    public CrossOverlayRenderer() {
        super(RenderTypes.LIGHT_OVERLAY_RENDERTYPE.apply(CROSS_TEXTURE));
    }

    @Override
    protected void onAddBlock(Matrix4f positionMatrix, float rf, float gf, float bf, int lightLevel, BlockPos pos) {
        getMatrixStack().translate(0, 1E-3, 0);

        VertexConsumer vc = this.vertexConsumer;
        vc.addVertex(positionMatrix, 0, 1, 0).setColor(rf, gf, bf, 1f).setUv(0f, 0f).setUv2(0, 0);
        vc.addVertex(positionMatrix, 0, 1, 1).setColor(rf, gf, bf, 1f).setUv(0f, 1f).setUv2(0, 0);
        vc.addVertex(positionMatrix, 1, 1, 1).setColor(rf, gf, bf, 1f).setUv(1f, 1f).setUv2(0, 0);
        vc.addVertex(positionMatrix, 1, 1, 0).setColor(rf, gf, bf, 1f).setUv(1f, 0f).setUv2(0, 0);
    }
}