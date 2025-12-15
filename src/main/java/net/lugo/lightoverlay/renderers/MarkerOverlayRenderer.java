package net.lugo.lightoverlay.renderers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lugo.lightoverlay.LightOverlay;
import net.lugo.lightoverlay.OverlayRenderer;
import net.lugo.lightoverlay.util.RenderLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

public class MarkerOverlayRenderer extends OverlayRenderer {
    private static final Identifier CROSS_TEXTURE = Identifier.fromNamespaceAndPath(LightOverlay.MOD_ID, "textures/marker.png");
    private static final Minecraft MC = Minecraft.getInstance();

    public MarkerOverlayRenderer() {
        super(RenderLayers.LIGHT_OVERLAY_RENDERLAYER.apply(CROSS_TEXTURE));
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    protected void onAddBlock(Matrix4f positionMatrix, float rf, float gf, float bf, int lightLevel, BlockPos pos) {
        if (MC.level == null) return;

        VertexConsumer vc = this.vertexConsumer;

        final float minX = 7f / 16f;
        final float maxX = 9f / 16f;
        final float minZ = 7f / 16f;
        final float maxZ = 9f / 16f;
        final float minY = 1f + 5f / 16f;
        final float maxY = 1f + 10f / 16f;


        // Top face
        vc.addVertex(positionMatrix, minX, maxY, minZ).setColor(rf, gf, bf, 1f).setUv(0f, 0f).setUv2(0, 0);
        vc.addVertex(positionMatrix, minX, maxY, maxZ).setColor(rf, gf, bf, 1f).setUv(0f, 1f).setUv2(0, 0);
        vc.addVertex(positionMatrix, maxX, maxY, maxZ).setColor(rf, gf, bf, 1f).setUv(1f, 1f).setUv2(0, 0);
        vc.addVertex(positionMatrix, maxX, maxY, minZ).setColor(rf, gf, bf, 1f).setUv(1f, 0f).setUv2(0, 0);

        // Bottom face
        vc.addVertex(positionMatrix, minX, minY, minZ).setColor(rf, gf, bf, 1f).setUv(0f, 0f).setUv2(0, 0);
        vc.addVertex(positionMatrix, maxX, minY, minZ).setColor(rf, gf, bf, 1f).setUv(1f, 0f).setUv2(0, 0);
        vc.addVertex(positionMatrix, maxX, minY, maxZ).setColor(rf, gf, bf, 1f).setUv(1f, 1f).setUv2(0, 0);
        vc.addVertex(positionMatrix, minX, minY, maxZ).setColor(rf, gf, bf, 1f).setUv(0f, 1f).setUv2(0, 0);

        // West face
        vc.addVertex(positionMatrix, minX, minY, minZ).setColor(rf, gf, bf, 1f).setUv(0f, 0f).setUv2(0, 0);
        vc.addVertex(positionMatrix, minX, minY, maxZ).setColor(rf, gf, bf, 1f).setUv(1f, 0f).setUv2(0, 0);
        vc.addVertex(positionMatrix, minX, maxY, maxZ).setColor(rf, gf, bf, 1f).setUv(1f, 1f).setUv2(0, 0);
        vc.addVertex(positionMatrix, minX, maxY, minZ).setColor(rf, gf, bf, 1f).setUv(0f, 1f).setUv2(0, 0);

        // East face
        vc.addVertex(positionMatrix, maxX, minY, minZ).setColor(rf, gf, bf, 1f).setUv(0f, 0f).setUv2(0, 0);
        vc.addVertex(positionMatrix, maxX, maxY, minZ).setColor(rf, gf, bf, 1f).setUv(0f, 1f).setUv2(0, 0);
        vc.addVertex(positionMatrix, maxX, maxY, maxZ).setColor(rf, gf, bf, 1f).setUv(1f, 1f).setUv2(0, 0);
        vc.addVertex(positionMatrix, maxX, minY, maxZ).setColor(rf, gf, bf, 1f).setUv(1f, 0f).setUv2(0, 0);

        // North face
        vc.addVertex(positionMatrix, minX, minY, minZ).setColor(rf, gf, bf, 1f).setUv(0f, 0f).setUv2(0, 0);
        vc.addVertex(positionMatrix, minX, maxY, minZ).setColor(rf, gf, bf, 1f).setUv(0f, 1f).setUv2(0, 0);
        vc.addVertex(positionMatrix, maxX, maxY, minZ).setColor(rf, gf, bf, 1f).setUv(1f, 1f).setUv2(0, 0);
        vc.addVertex(positionMatrix, maxX, minY, minZ).setColor(rf, gf, bf, 1f).setUv(1f, 0f).setUv2(0, 0);

        // South face
        vc.addVertex(positionMatrix, minX, minY, maxZ).setColor(rf, gf, bf, 1f).setUv(0f, 0f).setUv2(0, 0);
        vc.addVertex(positionMatrix, maxX, minY, maxZ).setColor(rf, gf, bf, 1f).setUv(1f, 0f).setUv2(0, 0);
        vc.addVertex(positionMatrix, maxX, maxY, maxZ).setColor(rf, gf, bf, 1f).setUv(1f, 1f).setUv2(0, 0);
        vc.addVertex(positionMatrix, minX, maxY, maxZ).setColor(rf, gf, bf, 1f).setUv(0f, 1f).setUv2(0, 0);
    }
}