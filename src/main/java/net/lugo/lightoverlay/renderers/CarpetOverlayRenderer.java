package net.lugo.lightoverlay.renderers;

import net.lugo.lightoverlay.LightOverlay;
import net.lugo.overlaylib.OverlayRenderer;
import net.lugo.overlaylib.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class CarpetOverlayRenderer extends OverlayRenderer {
    private static final Identifier CARPET_TEXTURE = Identifier.fromNamespaceAndPath(LightOverlay.MOD_ID, "textures/wool.png");
    private static final float EPSILON = 1E-3f;
    private static final float CARPET_HEIGHT_BASE = 1f / 16f;
    private static final float CARPET_HEIGHT = CARPET_HEIGHT_BASE + EPSILON;
    private static final Minecraft MC = Minecraft.getInstance();

    public CarpetOverlayRenderer() {
        super(RenderPipelines.POSITION_TEX_COLOR_FOG_TRIANGLES, CARPET_TEXTURE, false);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void addVertices(float worldX, float worldY, float worldZ, OverlayRendererBlockData data) {
        TextureSection tex = data.textureSection();
        BlockPos abovePos = data.pos().above();
        float r = data.r();
        float g = data.g();
        float b = data.b();

        OverlayVertexHelper.squareFromTriags(
                buffer,
                OverlayVertexHelper.FixedAxis.Y, worldY + 1 + CARPET_HEIGHT,
                worldX, worldZ,
                1f,
                r, g, b,
                tex.uStart(), tex.vStart(),
                tex.uEnd(), tex.vEnd(),
                UVRotation.NONE
        );

        // West face
        if(Block.shouldRenderFace(Blocks.WHITE_CARPET.defaultBlockState(), MC.level.getBlockState(abovePos.west()), Direction.WEST)) {
            OverlayVertexHelper.rectFromTriags(
                    buffer,
                    OverlayVertexHelper.FixedAxis.X, worldX,
                    worldY + 1, worldZ,
                    worldY + 1 + CARPET_HEIGHT, worldZ + 1,
                    r, g, b,
                    tex.uStart(), tex.vStart(),
                    tex.uEnd(), tex.vEnd(),
                    UVRotation.NONE
            );
        }

        // East face
        if(Block.shouldRenderFace(Blocks.WHITE_CARPET.defaultBlockState(), MC.level.getBlockState(abovePos.east()), Direction.EAST)) {
            OverlayVertexHelper.rectFromTriags(
                    buffer,
                    OverlayVertexHelper.FixedAxis.X, worldX + 1,
                    worldY + 1, worldZ + 1,
                    worldY + 1 + CARPET_HEIGHT, worldZ,
                    r, g, b,
                    tex.uStart(), tex.vStart(),
                    tex.uEnd(), tex.vEnd(),
                    UVRotation.NONE
            );
        }

        // North face
        if(Block.shouldRenderFace(Blocks.WHITE_CARPET.defaultBlockState(), MC.level.getBlockState(abovePos.north()), Direction.NORTH)) {
            OverlayVertexHelper.rectFromTriags(
                    buffer,
                    OverlayVertexHelper.FixedAxis.Z, worldZ,
                    worldX, worldY + 1,
                    worldX + 1, worldY + 1 + CARPET_HEIGHT,
                    r, g, b,
                    tex.uStart(), tex.vStart(),
                    tex.uEnd(), tex.vEnd(),
                    UVRotation.NONE
            );
        }

        // South face
        if(Block.shouldRenderFace(Blocks.WHITE_CARPET.defaultBlockState(), MC.level.getBlockState(abovePos.south()), Direction.SOUTH)) {
            OverlayVertexHelper.rectFromTriags(
                    buffer,
                    OverlayVertexHelper.FixedAxis.Z, worldZ + 1,
                    worldX, worldY + 1 + CARPET_HEIGHT,
                    worldX + 1, worldY + 1,
                    r, g, b,
                    tex.uStart(), tex.vStart(),
                    tex.uEnd(), tex.vEnd(),
                    UVRotation.NONE
            );
        }
    }
}