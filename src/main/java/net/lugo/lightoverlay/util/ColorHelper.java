package net.lugo.lightoverlay.util;

import net.lugo.lightoverlay.config.ModConfig;

import java.awt.*;

public class ColorHelper {
    public static Color getOverlayColor(int lightLevel) {
        return lightLevel >= ModConfig.lightLevelThreshold ? ModConfig.validColor : ModConfig.invalidColor;
    }

    public static float[] getOverlayColorFloats(int lightLevel) {
        Color color = getOverlayColor(lightLevel);

        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;

        return new float[]{r, g, b};
    }
}
