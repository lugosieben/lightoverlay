package net.lugo.lightoverlay.util;

import net.lugo.lightoverlay.config.ModConfig;
import net.minecraft.client.multiplayer.ClientLevel;

import java.awt.*;

public class ColorHelper {
    public static Color getOverlayColor(int lightLevel, int threshold) {
        return lightLevel >= threshold ? ModConfig.validColor : ModConfig.invalidColor;
    }

    public static Color getOverlayColor(int lightLevel, ClientLevel level) {
        return getOverlayColor(lightLevel, ModConfig.lightLevelThresholdForDimension(level));
    }
}
