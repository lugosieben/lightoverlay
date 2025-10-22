package net.lugo.lightoverlay;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import net.lugo.lightoverlay.config.ModConfig;
import net.lugo.lightoverlay.registration.Commands;
import net.lugo.lightoverlay.registration.KeyBindings;
import net.lugo.lightoverlay.renderers.CarpetOverlayRenderer;
import net.lugo.lightoverlay.renderers.CrossOverlayRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightOverlay implements ModInitializer {
	public static final String MOD_ID = "light-overlay";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static ModConfig CONFIG;

    private static final CarpetOverlayRenderer CARPET_RENDERER = new CarpetOverlayRenderer();
    private static final CrossOverlayRenderer CROSS_RENDERER = new CrossOverlayRenderer();

	@Override
	public void onInitialize() {
        LOGGER.info("Light Overlay (" + MOD_ID + ") initializing.");

        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        KeyBindings.registerKeybinds();
        Commands.registerCommands();

        LOGGER.info("Light Overlay (" + MOD_ID + ") initialized.");
	}

    public static ModConfig getConfig() { return CONFIG; }
    public static OverlayRenderer getCurrentRenderer() { return CONFIG.carpetMode ? CARPET_RENDERER : CROSS_RENDERER; }
}