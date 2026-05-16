package net.lugo.lightoverlay.util;

import net.lugo.lightoverlay.config.ModConfig;
import net.minecraft.client.multiplayer.ClientLevel;

import java.awt.*;

public class ColorHelper {
    public static Color getOverlayColor(int lightLevel, int threshold) {
        return lightLevel >= threshold ? ModConfig.validColor : ModConfig.invalidColor;
    }

    public static float[] getOverlayColorFloats(int lightLevel, int threshold) {
        Color color = getOverlayColor(lightLevel, threshold);
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;

        return new float[]{r, g, b};
    }

    public static float[] getOverlayColorFloats(int lightLevel, ClientLevel level) {
        return getOverlayColorFloats(lightLevel, ModConfig.lightLevelThresholdForDimension(level));
    }
}
