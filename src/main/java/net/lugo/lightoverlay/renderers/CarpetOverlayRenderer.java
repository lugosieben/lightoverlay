package net.lugo.lightoverlay.renderers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lugo.lightoverlay.LightOverlay;
import net.lugo.overlaylib.OverlayRenderer;
import net.lugo.overlaylib.util.RenderPipelines;
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
        super(RenderPipelines.POSITION_TEX_COLOR_FOG, CARPET_TEXTURE, false);
    }

    @Override
    protected void onStartBatch() {}

    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void addVertices(VertexConsumer buffer, Matrix4f positionMatrix, float r, float g, float b, float uStart, float uEnd, float vStart, float vEnd, BlockPos pos) {
        BlockPos abovePos = pos.above();

        buffer.addVertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 0).setColor(r, g, b, 1f).setUv(0f, 0f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 1).setColor(r, g, b, 1f).setUv(0f, 1f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 1).setColor(r, g, b, 1f).setUv(1f, 1f).setUv2(0, 0);
        buffer.addVertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 0).setColor(r, g, b, 1f).setUv(1f, 0f).setUv2(0, 0);

        // West face
        if(Block.shouldRenderFace(Blocks.WHITE_CARPET.defaultBlockState(), MC.level.getBlockState(abovePos.west()), Direction.WEST)) {
            buffer.addVertex(positionMatrix, 0, 1,                 0).setColor(r, g, b, 1f).setUv(0f, 0f).setUv2(0, 0);
            buffer.addVertex(positionMatrix, 0, 1,                 1).setColor(r, g, b, 1f).setUv(1f, 0f).setUv2(0, 0);
            buffer.addVertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 1).setColor(r, g, b, 1f).setUv(1f, CARPET_HEIGHT_BASE).setUv2(0, 0);
            buffer.addVertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 0).setColor(r, g, b, 1f).setUv(0f, CARPET_HEIGHT_BASE).setUv2(0, 0);
        }

        // East face
        if(Block.shouldRenderFace(Blocks.WHITE_CARPET.defaultBlockState(), MC.level.getBlockState(abovePos.east()), Direction.EAST)) {
            buffer.addVertex(positionMatrix, 1, 1,                 0).setColor(r, g, b, 1f).setUv(0f, 0f).setUv2(0, 0);
            buffer.addVertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 0).setColor(r, g, b, 1f).setUv(0f, CARPET_HEIGHT_BASE).setUv2(0, 0);
            buffer.addVertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 1).setColor(r, g, b, 1f).setUv(1f, CARPET_HEIGHT_BASE).setUv2(0, 0);
            buffer.addVertex(positionMatrix, 1, 1,                 1).setColor(r, g, b, 1f).setUv(1f, 0f).setUv2(0, 0);
        }

        // North face
        if(Block.shouldRenderFace(Blocks.WHITE_CARPET.defaultBlockState(), MC.level.getBlockState(abovePos.north()), Direction.NORTH)) {
            buffer.addVertex(positionMatrix, 0, 1,                 0).setColor(r, g, b, 1f).setUv(0f, 0f).setUv2(0, 0);
            buffer.addVertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 0).setColor(r, g, b, 1f).setUv(0f, CARPET_HEIGHT_BASE).setUv2(0, 0);
            buffer.addVertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 0).setColor(r, g, b, 1f).setUv(1f, CARPET_HEIGHT_BASE).setUv2(0, 0);
            buffer.addVertex(positionMatrix, 1, 1,                 0).setColor(r, g, b, 1f).setUv(1f, 0f).setUv2(0, 0);
        }

        // South face
        if(Block.shouldRenderFace(Blocks.WHITE_CARPET.defaultBlockState(), MC.level.getBlockState(abovePos.south()), Direction.SOUTH)) {
            buffer.addVertex(positionMatrix, 0, 1,                 1).setColor(r, g, b, 1f).setUv(0f, 0f).setUv2(0, 0);
            buffer.addVertex(positionMatrix, 1, 1,                 1).setColor(r, g, b, 1f).setUv(1f, 0f).setUv2(0, 0);
            buffer.addVertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 1).setColor(r, g, b, 1f).setUv(1f, CARPET_HEIGHT).setUv2(0, 0);
            buffer.addVertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 1).setColor(r, g, b, 1f).setUv(0f, CARPET_HEIGHT).setUv2(0, 0);
        }
    }

    @Override
    protected void onEndBatch() {}
}