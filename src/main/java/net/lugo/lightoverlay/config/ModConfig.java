package net.lugo.lightoverlay.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.lugo.lightoverlay.LightOverlay;

@SuppressWarnings("CanBeFinal")
@Config(name = LightOverlay.MOD_ID)
public class ModConfig  implements ConfigData {
    @ConfigEntry.BoundedDiscrete(min = 1, max = 15)
    @ConfigEntry.Gui.Tooltip()
    public int lightLevelThreshold = 1;

    @ConfigEntry.BoundedDiscrete(min = 5, max = 100)
    public int scanRadius = 20;

    public boolean carpetMode = false;

    public boolean hideGreenCrosses = false;
    public boolean hideTransparentBlockCrosses = true;
    public boolean hideWaterCrosses = true;
    @ConfigEntry.Gui.Tooltip
    public boolean showSpecialSpawningConditionBlocks = false;
}
