package net.lugo.lightoverlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

public abstract class OverlayRenderer {
    private final RenderLayer renderLayer;
    private final Identifier textureId;

    private final VertexConsumerProvider.Immediate vcp = VertexConsumerProvider.immediate(new BufferAllocator(8192));
    private final MatrixStack matrixStack = new MatrixStack();

    protected VertexConsumer vertexConsumer;
    private boolean batchStarted = false;

    protected OverlayRenderer(RenderLayer renderLayer, Identifier textureId) {
        this.renderLayer = renderLayer;
        this.textureId = textureId;
    }

    public final void startBatch() {
        if (batchStarted) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.getTextureManager() == null) {
            return;
        }
        GpuTextureView shaderTexture = mc.getTextureManager().getTexture(textureId).getGlTextureView();

        vertexConsumer = vcp.getBuffer(renderLayer);
        if (shaderTexture != null) {
            RenderSystem.setShaderTexture(0, shaderTexture);
        }

        batchStarted = true;
        onStartBatch();
    }

    public final void addBlock(Camera camera, BlockPos pos, int r, int g, int b) {
        if (!batchStarted) return;

        getMatrixStack().push();
        getMatrixStack().multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        getMatrixStack().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180F));
        Vec3d transformedPos = Vec3d.of(pos).subtract(camera.getPos());
        getMatrixStack().translate(transformedPos.x, transformedPos.y, transformedPos.z);

        Matrix4f positionMatrix = getMatrixStack().peek().getPositionMatrix();

        float rf = r / 255f;
        float gf = g / 255f;
        float bf = b / 255f;

        onAddBlock(positionMatrix, rf, gf, bf, pos);

        getMatrixStack().pop();
    }

    public final void endBatch() {
        if (!batchStarted) return;
        onEndBatch();
        vcp.draw();
        batchStarted = false;
    }

    protected void onStartBatch() {}

    protected abstract void onAddBlock(Matrix4f positionMatrix, float r, float g, float b, BlockPos pos);

    protected void onEndBatch() {}

    protected MatrixStack getMatrixStack() {
        return matrixStack;
    }
}