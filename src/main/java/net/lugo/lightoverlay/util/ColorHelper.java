package net.lugo.lightoverlay.util;

import net.lugo.lightoverlay.config.ModConfig;

import java.awt.*;

public class ColorHelper {
    public static Color getOverlayColor(int lightLevel, int threshold) {
        return lightLevel >= threshold ? ModConfig.validColor : ModConfig.invalidColor;
    }

    public static Color getOverlayColorWithWarning(int blockLightLevel, int skyLightLevel, int threshold) {
        if (blockLightLevel >= threshold) return ModConfig.validColor;
        return skyLightLevel >= threshold
                ? ModConfig.warningColor
                : ModConfig.invalidColor;
    }
}
