package net.lugo.lightoverlay.renderers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lugo.lightoverlay.LightOverlay;
import net.lugo.lightoverlay.OverlayRenderer;
import net.lugo.lightoverlay.util.RenderLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.joml.Matrix4f;

public class CarpetOverlayRenderer extends OverlayRenderer {
    private static final Identifier CARPET_TEXTURE = Identifier.fromNamespaceAndPath(LightOverlay.MOD_ID, "textures/wool.png");
    private static final float CARPET_HEIGHT_BASE = 1f / 16f;
    private static final float CARPET_HEIGHT = CARPET_HEIGHT_BASE + 1E-3f;
    private static final Minecraft MC = Minecraft.getInstance();

    public CarpetOverlayRenderer() {
        super(RenderLayers.LIGHT_OVERLAY_RENDERLAYER.apply(CARPET_TEXTURE));
    }

    @Override
    protected void onAddBlock(Matrix4f positionMatrix, float rf, float gf, float bf, int lightLevel, BlockPos pos) {
        if (MC.level == null) return;

        BlockPos abovePos = pos.above();

        VertexConsumer vc = this.vertexConsumer;
        // Top face
        vc.addVertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 0).setColor(rf, gf, bf, 1f).setUv(0f, 0f).setUv2(0, 0);
        vc.addVertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 1).setColor(rf, gf, bf, 1f).setUv(0f, 1f).setUv2(0, 0);
        vc.addVertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 1).setColor(rf, gf, bf, 1f).setUv(1f, 1f).setUv2(0, 0);
        vc.addVertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 0).setColor(rf, gf, bf, 1f).setUv(1f, 0f).setUv2(0, 0);

        // West face
        if(Block.shouldRenderFace(Blocks.WHITE_CARPET.defaultBlockState(), MC.level.getBlockState(abovePos.west()), Direction.WEST)) {
            vc.addVertex(positionMatrix, 0, 1,                 0).setColor(rf, gf, bf, 1f).setUv(0f, 0f).setUv2(0, 0);
            vc.addVertex(positionMatrix, 0, 1,                 1).setColor(rf, gf, bf, 1f).setUv(1f, 0f).setUv2(0, 0);
            vc.addVertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 1).setColor(rf, gf, bf, 1f).setUv(1f, CARPET_HEIGHT_BASE).setUv2(0, 0);
            vc.addVertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 0).setColor(rf, gf, bf, 1f).setUv(0f, CARPET_HEIGHT_BASE).setUv2(0, 0);
        }

        // East face
        if(Block.shouldRenderFace(Blocks.WHITE_CARPET.defaultBlockState(), MC.level.getBlockState(abovePos.east()), Direction.EAST)) {
            vc.addVertex(positionMatrix, 1, 1,                 0).setColor(rf, gf, bf, 1f).setUv(0f, 0f).setUv2(0, 0);
            vc.addVertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 0).setColor(rf, gf, bf, 1f).setUv(0f, CARPET_HEIGHT_BASE).setUv2(0, 0);
            vc.addVertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 1).setColor(rf, gf, bf, 1f).setUv(1f, CARPET_HEIGHT_BASE).setUv2(0, 0);
            vc.addVertex(positionMatrix, 1, 1,                 1).setColor(rf, gf, bf, 1f).setUv(1f, 0f).setUv2(0, 0);
        }

        // North face
        if(Block.shouldRenderFace(Blocks.WHITE_CARPET.defaultBlockState(), MC.level.getBlockState(abovePos.north()), Direction.NORTH)) {
            vc.addVertex(positionMatrix, 0, 1,                 0).setColor(rf, gf, bf, 1f).setUv(0f, 0f).setUv2(0, 0);
            vc.addVertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 0).setColor(rf, gf, bf, 1f).setUv(0f, CARPET_HEIGHT_BASE).setUv2(0, 0);
            vc.addVertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 0).setColor(rf, gf, bf, 1f).setUv(1f, CARPET_HEIGHT_BASE).setUv2(0, 0);
            vc.addVertex(positionMatrix, 1, 1,                 0).setColor(rf, gf, bf, 1f).setUv(1f, 0f).setUv2(0, 0);
        }

        // South face
        if(Block.shouldRenderFace(Blocks.WHITE_CARPET.defaultBlockState(), MC.level.getBlockState(abovePos.south()), Direction.SOUTH)) {
            vc.addVertex(positionMatrix, 0, 1,                 1).setColor(rf, gf, bf, 1f).setUv(0f, 0f).setUv2(0, 0);
            vc.addVertex(positionMatrix, 1, 1,                 1).setColor(rf, gf, bf, 1f).setUv(1f, 0f).setUv2(0, 0);
            vc.addVertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 1).setColor(rf, gf, bf, 1f).setUv(1f, CARPET_HEIGHT).setUv2(0, 0);
            vc.addVertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 1).setColor(rf, gf, bf, 1f).setUv(0f, CARPET_HEIGHT).setUv2(0, 0);
        }
    }
}