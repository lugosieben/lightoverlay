package net.lugo.lightoverlay.renderers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lugo.lightoverlay.LightOverlay;
import net.lugo.overlaylib.OverlayRenderer;
import net.lugo.overlaylib.util.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

public class MarkerOverlayRenderer extends OverlayRenderer {
    private static final Identifier MARKER_TEXTURE = Identifier.fromNamespaceAndPath(LightOverlay.MOD_ID, "textures/marker.png");

    public MarkerOverlayRenderer() {
        super(RenderPipelines.POSITION_TEX_COLOR_FOG, MARKER_TEXTURE, true);
    }

    @Override
    protected void onStartBatch() {}

    @SuppressWarnings("DuplicatedCode")
    @Override
    protected void addVertices(VertexConsumer buffer, Matrix4f positionMatrix, float r, float g, float b, float uStart, float uEnd, float vStart, float vEnd, BlockPos pos) {
        final float minX = 7f / 16f;
        final float maxX = 9f / 16f;
        final float minZ = 7f / 16f;
        final float maxZ = 9f / 16f;
        final float minY = 1f + 5f / 16f;
        final float maxY = 1f + 10f / 16f;


        // Top face
        buffer.addVertex(positionMatrix, minX, maxY, minZ).setColor(r, g, b, 1f).setUv(0f, 0f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, minX, maxY, maxZ).setColor(r, g, b, 1f).setUv(0f, 1f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, maxX, maxY, maxZ).setColor(r, g, b, 1f).setUv(1f, 1f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, maxX, maxY, minZ).setColor(r, g, b, 1f).setUv(1f, 0f).setUv2(0, 0);

        // Bottom face
        buffer.addVertex(positionMatrix, minX, minY, minZ).setColor(r, g, b, 1f).setUv(0f, 0f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, maxX, minY, minZ).setColor(r, g, b, 1f).setUv(1f, 0f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, maxX, minY, maxZ).setColor(r, g, b, 1f).setUv(1f, 1f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, minX, minY, maxZ).setColor(r, g, b, 1f).setUv(0f, 1f).setUv2(0, 0);

        // West face
        buffer.addVertex(positionMatrix, minX, minY, minZ).setColor(r, g, b, 1f).setUv(0f, 0f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, minX, minY, maxZ).setColor(r, g, b, 1f).setUv(1f, 0f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, minX, maxY, maxZ).setColor(r, g, b, 1f).setUv(1f, 1f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, minX, maxY, minZ).setColor(r, g, b, 1f).setUv(0f, 1f).setUv2(0, 0);

        // East face
        buffer.addVertex(positionMatrix, maxX, minY, minZ).setColor(r, g, b, 1f).setUv(0f, 0f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, maxX, maxY, minZ).setColor(r, g, b, 1f).setUv(0f, 1f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, maxX, maxY, maxZ).setColor(r, g, b, 1f).setUv(1f, 1f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, maxX, minY, maxZ).setColor(r, g, b, 1f).setUv(1f, 0f).setUv2(0, 0);

        // North face
        buffer.addVertex(positionMatrix, minX, minY, minZ).setColor(r, g, b, 1f).setUv(0f, 0f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, minX, maxY, minZ).setColor(r, g, b, 1f).setUv(0f, 1f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, maxX, maxY, minZ).setColor(r, g, b, 1f).setUv(1f, 1f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, maxX, minY, minZ).setColor(r, g, b, 1f).setUv(1f, 0f).setUv2(0, 0);

        // South face
        buffer.addVertex(positionMatrix, minX, minY, maxZ).setColor(r, g, b, 1f).setUv(0f, 0f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, maxX, minY, maxZ).setColor(r, g, b, 1f).setUv(1f, 0f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, maxX, maxY, maxZ).setColor(r, g, b, 1f).setUv(1f, 1f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, minX, maxY, maxZ).setColor(r, g, b, 1f).setUv(0f, 1f).setUv2(0, 0);
    }

    @Override
    protected void onEndBatch() {}
}