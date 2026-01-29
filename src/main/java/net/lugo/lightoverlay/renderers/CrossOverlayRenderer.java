package net.lugo.lightoverlay.renderers;


import net.lugo.lightoverlay.LightOverlay;
import net.lugo.lightoverlay.config.ModConfig;
import net.lugo.overlaylib.renderers.SimpleTextureOverlayRenderer;
import net.minecraft.resources.Identifier;

public class CrossOverlayRenderer extends SimpleTextureOverlayRenderer {
    private static final Identifier CROSS_TEXTURE = Identifier.fromNamespaceAndPath(LightOverlay.MOD_ID, "textures/cross.png");

    public CrossOverlayRenderer() {
        super(CROSS_TEXTURE, ModConfig.enableIrisFlickerFix);
    }
}