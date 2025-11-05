package net.lugo.lightoverlay.renderers;

import net.lugo.lightoverlay.LightOverlay;
import net.lugo.lightoverlay.OverlayRenderer;
import net.lugo.lightoverlay.util.RenderLayers;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.joml.Matrix4f;

public class CarpetOverlayRenderer extends OverlayRenderer {
    private static final Identifier CARPET_TEXTURE = Identifier.of(LightOverlay.MOD_ID, "textures/wool.png");
    private static final float CARPET_HEIGHT_BASE = 1f / 16f;
    private static final float CARPET_HEIGHT = CARPET_HEIGHT_BASE + 1E-3f;
    private static final MinecraftClient MC = MinecraftClient.getInstance();

    public CarpetOverlayRenderer() {
        super(RenderLayers.LIGHT_OVERLAY_RENDERLAYER, CARPET_TEXTURE);
    }

    @Override
    protected void onAddBlock(Matrix4f positionMatrix, float rf, float gf, float bf, int lightLevel, BlockPos pos) {
        if (MC.world == null) return;

        BlockPos abovePos = pos.up();

        VertexConsumer vc = this.vertexConsumer;
        // Top face
        vc.vertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 0).color(rf, gf, bf, 1f).texture(0f, 0f).light(0, 0);
        vc.vertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 1).color(rf, gf, bf, 1f).texture(0f, 1f).light(0, 0);
        vc.vertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 1).color(rf, gf, bf, 1f).texture(1f, 1f).light(0, 0);
        vc.vertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 0).color(rf, gf, bf, 1f).texture(1f, 0f).light(0, 0);

        // West face
        if(Block.shouldDrawSide(Blocks.WHITE_CARPET.getDefaultState(), MC.world.getBlockState(abovePos.west()), Direction.WEST)) {
            vc.vertex(positionMatrix, 0, 1,                 0).color(rf, gf, bf, 1f).texture(0f, 0f).light(0, 0);
            vc.vertex(positionMatrix, 0, 1,                 1).color(rf, gf, bf, 1f).texture(1f, 0f).light(0, 0);
            vc.vertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 1).color(rf, gf, bf, 1f).texture(1f, CARPET_HEIGHT_BASE).light(0, 0);
            vc.vertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 0).color(rf, gf, bf, 1f).texture(0f, CARPET_HEIGHT_BASE).light(0, 0);
        }

        // East face
        if(Block.shouldDrawSide(Blocks.WHITE_CARPET.getDefaultState(), MC.world.getBlockState(abovePos.east()), Direction.EAST)) {
            vc.vertex(positionMatrix, 1, 1,                 0).color(rf, gf, bf, 1f).texture(0f, 0f).light(0, 0);
            vc.vertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 0).color(rf, gf, bf, 1f).texture(0f, CARPET_HEIGHT_BASE).light(0, 0);
            vc.vertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 1).color(rf, gf, bf, 1f).texture(1f, CARPET_HEIGHT_BASE).light(0, 0);
            vc.vertex(positionMatrix, 1, 1,                 1).color(rf, gf, bf, 1f).texture(1f, 0f).light(0, 0);
        }

        // North face
        if(Block.shouldDrawSide(Blocks.WHITE_CARPET.getDefaultState(), MC.world.getBlockState(abovePos.north()), Direction.NORTH)) {
            vc.vertex(positionMatrix, 0, 1,                 0).color(rf, gf, bf, 1f).texture(0f, 0f).light(0, 0);
            vc.vertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 0).color(rf, gf, bf, 1f).texture(0f, CARPET_HEIGHT_BASE).light(0, 0);
            vc.vertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 0).color(rf, gf, bf, 1f).texture(1f, CARPET_HEIGHT_BASE).light(0, 0);
            vc.vertex(positionMatrix, 1, 1,                 0).color(rf, gf, bf, 1f).texture(1f, 0f).light(0, 0);
        }

        // South face
        if(Block.shouldDrawSide(Blocks.WHITE_CARPET.getDefaultState(), MC.world.getBlockState(abovePos.south()), Direction.SOUTH)) {
            vc.vertex(positionMatrix, 0, 1,                 1).color(rf, gf, bf, 1f).texture(0f, 0f).light(0, 0);
            vc.vertex(positionMatrix, 1, 1,                 1).color(rf, gf, bf, 1f).texture(1f, 0f).light(0, 0);
            vc.vertex(positionMatrix, 1, 1 + CARPET_HEIGHT, 1).color(rf, gf, bf, 1f).texture(1f, CARPET_HEIGHT).light(0, 0);
            vc.vertex(positionMatrix, 0, 1 + CARPET_HEIGHT, 1).color(rf, gf, bf, 1f).texture(0f, CARPET_HEIGHT).light(0, 0);
        }
    }
}