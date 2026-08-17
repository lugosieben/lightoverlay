package net.lugo.lightoverlay.renderers;

import net.lugo.lightoverlay.LightOverlay;
import net.lugo.overlaylib.renderers.CachedMeshOverlayRenderer;
import net.lugo.overlaylib.util.OverlayRendererBlockData;
import net.lugo.overlaylib.util.OverlayVertexHelper;
import net.lugo.overlaylib.util.RenderPipelines;
import net.lugo.overlaylib.util.UVRotation;
import net.minecraft.resources.Identifier;

import java.awt.Color;

public class MarkerOverlayRenderer extends CachedMeshOverlayRenderer {
    private static final Identifier MARKER_TEXTURE = Identifier.fromNamespaceAndPath(LightOverlay.MOD_ID, "textures/marker.png");

    public MarkerOverlayRenderer() {
        super(RenderPipelines.POSITION_TEX_COLOR_FOG_TRIANGLES, MARKER_TEXTURE, true);
    }

    @Override
    protected void addVertices(float worldX, float worldY, float worldZ, OverlayRendererBlockData data) {
        final float minX = 7f / 16f;
        final float maxX = 9f / 16f;
        final float minZ = 7f / 16f;
        final float maxZ = 9f / 16f;
        final float minY = 1f + 5f / 16f;
        final float maxY = 1f + 10f / 16f;
        final Color color = data.color();

        final float x0 = worldX + minX;
        final float x1 = worldX + maxX;
        final float y0 = worldY + minY;
        final float y1 = worldY + maxY;
        final float z0 = worldZ + minZ;
        final float z1 = worldZ + maxZ;

        OverlayVertexHelper.rect(buffer, OverlayVertexHelper.FixedAxis.Y, y1, x0, z0, x1, z1, color, 0f, 0f, 1f, 1f, UVRotation.NONE); // Top
        OverlayVertexHelper.rect(buffer, OverlayVertexHelper.FixedAxis.Y, y0, x0, z1, x1, z0, color, 0f, 0f, 1f, 1f, UVRotation.NONE); // Bottom
        OverlayVertexHelper.rect(buffer, OverlayVertexHelper.FixedAxis.X, x0, y0, z0, y1, z1, color, 0f, 0f, 1f, 1f, UVRotation.NONE); // West
        OverlayVertexHelper.rect(buffer, OverlayVertexHelper.FixedAxis.X, x1, y0, z1, y1, z0, color, 0f, 0f, 1f, 1f, UVRotation.NONE); // East
        OverlayVertexHelper.rect(buffer, OverlayVertexHelper.FixedAxis.Z, z0, x0, y0, x1, y1, color, 0f, 0f, 1f, 1f, UVRotation.NONE); // North
        OverlayVertexHelper.rect(buffer, OverlayVertexHelper.FixedAxis.Z, z1, x0, y1, x1, y0, color, 0f, 0f, 1f, 1f, UVRotation.NONE); // South
    }
}