package net.lugo.lightoverlay.renderers;

import net.lugo.lightoverlay.LightOverlay;
import net.lugo.lightoverlay.OverlayRenderer;
import net.lugo.lightoverlay.util.RenderLayers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;

public class MarkerOverlayRenderer extends OverlayRenderer {
    private static final Identifier CROSS_TEXTURE = Identifier.of(LightOverlay.MOD_ID, "textures/marker.png");
    private static final MinecraftClient MC = MinecraftClient.getInstance();

    public MarkerOverlayRenderer() {
        super(RenderLayers.LIGHT_OVERLAY_RENDERLAYER, CROSS_TEXTURE);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    protected void onAddBlock(Matrix4f positionMatrix, float rf, float gf, float bf, int lightLevel, BlockPos pos) {
        if (MC.world == null) return;

        VertexConsumer vc = this.vertexConsumer;

        final float minX = 7f / 16f;
        final float maxX = 9f / 16f;
        final float minZ = 7f / 16f;
        final float maxZ = 9f / 16f;
        final float minY = 1f + 5f / 16f;
        final float maxY = 1f + 10f / 16f;


        // Top face
        vc.vertex(positionMatrix, minX, maxY, minZ).color(rf, gf, bf, 1f).texture(0f, 0f).light(0, 0);
        vc.vertex(positionMatrix, minX, maxY, maxZ).color(rf, gf, bf, 1f).texture(0f, 1f).light(0, 0);
        vc.vertex(positionMatrix, maxX, maxY, maxZ).color(rf, gf, bf, 1f).texture(1f, 1f).light(0, 0);
        vc.vertex(positionMatrix, maxX, maxY, minZ).color(rf, gf, bf, 1f).texture(1f, 0f).light(0, 0);

        // Bottom face
        vc.vertex(positionMatrix, minX, minY, minZ).color(rf, gf, bf, 1f).texture(0f, 0f).light(0, 0);
        vc.vertex(positionMatrix, maxX, minY, minZ).color(rf, gf, bf, 1f).texture(1f, 0f).light(0, 0);
        vc.vertex(positionMatrix, maxX, minY, maxZ).color(rf, gf, bf, 1f).texture(1f, 1f).light(0, 0);
        vc.vertex(positionMatrix, minX, minY, maxZ).color(rf, gf, bf, 1f).texture(0f, 1f).light(0, 0);

        // West face
        vc.vertex(positionMatrix, minX, minY, minZ).color(rf, gf, bf, 1f).texture(0f, 0f).light(0, 0);
        vc.vertex(positionMatrix, minX, minY, maxZ).color(rf, gf, bf, 1f).texture(1f, 0f).light(0, 0);
        vc.vertex(positionMatrix, minX, maxY, maxZ).color(rf, gf, bf, 1f).texture(1f, 1f).light(0, 0);
        vc.vertex(positionMatrix, minX, maxY, minZ).color(rf, gf, bf, 1f).texture(0f, 1f).light(0, 0);

        // East face
        vc.vertex(positionMatrix, maxX, minY, minZ).color(rf, gf, bf, 1f).texture(0f, 0f).light(0, 0);
        vc.vertex(positionMatrix, maxX, maxY, minZ).color(rf, gf, bf, 1f).texture(0f, 1f).light(0, 0);
        vc.vertex(positionMatrix, maxX, maxY, maxZ).color(rf, gf, bf, 1f).texture(1f, 1f).light(0, 0);
        vc.vertex(positionMatrix, maxX, minY, maxZ).color(rf, gf, bf, 1f).texture(1f, 0f).light(0, 0);

        // North face
        vc.vertex(positionMatrix, minX, minY, minZ).color(rf, gf, bf, 1f).texture(0f, 0f).light(0, 0);
        vc.vertex(positionMatrix, minX, maxY, minZ).color(rf, gf, bf, 1f).texture(0f, 1f).light(0, 0);
        vc.vertex(positionMatrix, maxX, maxY, minZ).color(rf, gf, bf, 1f).texture(1f, 1f).light(0, 0);
        vc.vertex(positionMatrix, maxX, minY, minZ).color(rf, gf, bf, 1f).texture(1f, 0f).light(0, 0);

        // South face
        vc.vertex(positionMatrix, minX, minY, maxZ).color(rf, gf, bf, 1f).texture(0f, 0f).light(0, 0);
        vc.vertex(positionMatrix, maxX, minY, maxZ).color(rf, gf, bf, 1f).texture(1f, 0f).light(0, 0);
        vc.vertex(positionMatrix, maxX, maxY, maxZ).color(rf, gf, bf, 1f).texture(1f, 1f).light(0, 0);
        vc.vertex(positionMatrix, minX, maxY, maxZ).color(rf, gf, bf, 1f).texture(0f, 1f).light(0, 0);
    }
}