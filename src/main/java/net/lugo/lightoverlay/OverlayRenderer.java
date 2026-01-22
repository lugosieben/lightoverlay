package net.lugo.lightoverlay;

import net.lugo.lightoverlay.util.ColorHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.lugo.lightoverlay.config.ModConfig;
import org.joml.Matrix4f;

public abstract class OverlayRenderer {
    private final RenderType renderLayer;

    private final MultiBufferSource.BufferSource vcp = MultiBufferSource.immediate(new ByteBufferBuilder(8192));
    private final PoseStack matrixStack = new PoseStack();

    protected VertexConsumer vertexConsumer;
    private boolean batchStarted = false;

    protected OverlayRenderer(RenderType renderLayer) {
        this.renderLayer = renderLayer;
    }

    public final void startBatch() {
        if (batchStarted) return;

        vertexConsumer = vcp.getBuffer(renderLayer);

        batchStarted = true;
        onStartBatch();
    }

    public final void addBlock(Camera camera, BlockPos pos, int lightLevel) {
        if (!batchStarted) return;

        if (ModConfig.hideGreen && lightLevel >= ModConfig.lightLevelThreshold) return;

        getMatrixStack().pushPose();
        getMatrixStack().mulPose(Axis.XP.rotationDegrees(camera.xRot()));
        getMatrixStack().mulPose(Axis.YP.rotationDegrees(camera.yRot() + 180F));
        Vec3 transformedPos = Vec3.atLowerCornerOf(pos).subtract(camera.position());
        getMatrixStack().translate(transformedPos.x, transformedPos.y, transformedPos.z);

        Matrix4f positionMatrix = getMatrixStack().last().pose();

        float[] colorFloats = ColorHelper.getOverlayColorFloats(lightLevel);
        float rf = colorFloats[0];
        float gf = colorFloats[1];
        float bf = colorFloats[2];

        onAddBlock(positionMatrix, rf, gf, bf, lightLevel, pos);

        getMatrixStack().popPose();
    }

    public final void endBatch() {
        if (!batchStarted) return;
        onEndBatch();
        vcp.endBatch();
        batchStarted = false;
    }

    protected void onStartBatch() {}

    protected abstract void onAddBlock(Matrix4f positionMatrix, float rf, float gf, float bf, int lightLevel, BlockPos pos);

    protected void onEndBatch() {}

    protected PoseStack getMatrixStack() {
        return matrixStack;
    }
}