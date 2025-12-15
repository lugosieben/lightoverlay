package net.lugo.lightoverlay;

import net.lugo.lightoverlay.util.ColorHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RotationAxis;
import net.lugo.lightoverlay.config.ModConfig;
import org.joml.Matrix4f;

public abstract class OverlayRenderer {
    private final RenderLayer renderLayer;

    private final VertexConsumerProvider.Immediate vcp = VertexConsumerProvider.immediate(new BufferAllocator(8192));
    private final MatrixStack matrixStack = new MatrixStack();

    protected VertexConsumer vertexConsumer;
    private boolean batchStarted = false;

    protected OverlayRenderer(RenderLayer renderLayer) {
        this.renderLayer = renderLayer;
    }

    public final void startBatch() {
        if (batchStarted) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.getTextureManager() == null) {
            return;
        }

        vertexConsumer = vcp.getBuffer(renderLayer);

        batchStarted = true;
        onStartBatch();
    }

    public final void addBlock(Camera camera, BlockPos pos, int lightLevel) {
        if (!batchStarted) return;

        if (ModConfig.hideGreen && lightLevel >= ModConfig.lightLevelThreshold) return;

        getMatrixStack().push();
        getMatrixStack().multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        getMatrixStack().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180F));
        Vec3d transformedPos = Vec3d.of(pos).subtract(camera.getCameraPos());
        getMatrixStack().translate(transformedPos.x, transformedPos.y, transformedPos.z);

        Matrix4f positionMatrix = getMatrixStack().peek().getPositionMatrix();

        float[] colorFloats = ColorHelper.getOverlayColorFloats(lightLevel);
        float rf = colorFloats[0];
        float gf = colorFloats[1];
        float bf = colorFloats[2];

        onAddBlock(positionMatrix, rf, gf, bf, lightLevel, pos);

        getMatrixStack().pop();
    }

    public final void endBatch() {
        if (!batchStarted) return;
        onEndBatch();
        vcp.draw();
        batchStarted = false;
    }

    protected void onStartBatch() {}

    protected abstract void onAddBlock(Matrix4f positionMatrix, float rf, float gf, float bf, int lightLevel, BlockPos pos);

    protected void onEndBatch() {}

    protected MatrixStack getMatrixStack() {
        return matrixStack;
    }
}