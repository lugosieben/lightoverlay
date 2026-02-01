package net.lugo.lightoverlay.renderers;

import net.lugo.lightoverlay.LightOverlay;
import net.lugo.overlaylib.renderers.SimpleTextureOverlayRenderer;
import net.minecraft.resources.Identifier;

public class NumberOverlayRenderer extends SimpleTextureOverlayRenderer {
    private static final Identifier NUMBERS_TEXTURE = Identifier.fromNamespaceAndPath(LightOverlay.MOD_ID, "textures/numbers.png");
    public NumberOverlayRenderer() {
        super(NUMBERS_TEXTURE, true);
    }
}