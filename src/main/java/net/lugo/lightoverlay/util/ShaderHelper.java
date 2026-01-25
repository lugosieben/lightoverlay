package net.lugo.lightoverlay.util;

import net.lugo.lightoverlay.LightOverlay;
import net.minecraft.resources.Identifier;

public class ShaderHelper {
    public static final Identifier LIGHTOVERLAY_VERTEX_SHADER = Identifier.fromNamespaceAndPath(LightOverlay.MOD_ID, "position_tex_color_fog");
    public static final Identifier LIGHTOVERLAY_FRAGMENT_SHADER = Identifier.fromNamespaceAndPath(LightOverlay.MOD_ID, "position_tex_color_fog");
}
