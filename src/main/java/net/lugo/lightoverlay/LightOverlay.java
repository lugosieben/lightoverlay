package net.lugo.lightoverlay;

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

    private static final CarpetOverlayRenderer CARPET_RENDERER = new CarpetOverlayRenderer();
    private static final CrossOverlayRenderer CROSS_RENDERER = new CrossOverlayRenderer();

	@Override
	public void onInitialize() {
        LOGGER.info("Light Overlay (" + MOD_ID + ") initializing.");

        KeyBindings.registerKeybinds();
        Commands.registerCommands();

        LOGGER.info("Light Overlay (" + MOD_ID + ") initialized.");
	}

    public static OverlayRenderer getCurrentRenderer() { return ModConfig.carpetMode ? CARPET_RENDERER : CROSS_RENDERER; }
}