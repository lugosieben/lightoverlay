package net.lugo.lightoverlay.util;

import net.lugo.lightoverlay.config.ModConfig;
import net.minecraft.client.multiplayer.ClientLevel;

import java.awt.*;

public class ColorHelper {
    public static Color getOverlayColor(int lightLevel, ClientLevel level) {
        return lightLevel >= ModConfig.lightLevelThresholdForDimension(level) ? ModConfig.validColor : ModConfig.invalidColor;
    }

    public static float[] getOverlayColorFloats(int lightLevel, ClientLevel level) {
        Color color = getOverlayColor(lightLevel, level);

        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;

        return new float[]{r, g, b};
    }
}
