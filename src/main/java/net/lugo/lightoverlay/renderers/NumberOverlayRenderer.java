package net.lugo.lightoverlay.renderers;

import net.lugo.lightoverlay.LightOverlay;
import net.lugo.lightoverlay.config.ModConfig;
import net.lugo.overlaylib.renderers.CamFacingTextureOverlayRenderer;
import net.lugo.overlaylib.util.OverlayRendererBlockData;
import net.lugo.overlaylib.util.OverlayVertexHelper;
import net.minecraft.resources.Identifier;

public class NumberOverlayRenderer extends CamFacingTextureOverlayRenderer {
    private static final Identifier NUMBERS_TEXTURE = Identifier.fromNamespaceAndPath(LightOverlay.MOD_ID, "textures/numbers.png");

    private static final float EPSILON = 1E-3f;

    public NumberOverlayRenderer() {
        super(NUMBERS_TEXTURE, true);
    }

    @Override
    protected void addVertices(float x, float y, float z, OverlayRendererBlockData data) {
        float scale = ModConfig.numberScale;

        float inset = (1f - scale) / 2f;
        OverlayVertexHelper.square(
                buffer,
                OverlayVertexHelper.FixedAxis.Y, y + 1f + EPSILON,
                x + inset, z + inset,
                scale,
                data.color(),
                data.textureSection().uStart(), data.textureSection().vStart(),
                data.textureSection().uEnd(), data.textureSection().vEnd(),
                uvRotation
        );
    }
}
