package net.lugo.lightoverlay;

import net.fabricmc.api.ModInitializer;

import net.lugo.lightoverlay.config.ModConfig;
import net.lugo.lightoverlay.registration.Commands;
import net.lugo.lightoverlay.registration.KeyBindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightOverlay implements ModInitializer {
	public static final String MOD_ID = "light-overlay";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        LOGGER.info("Light OverlayRendererType (" + MOD_ID + ") initializing.");

        KeyBindings.registerKeybinds();
        Commands.registerCommands();

        ModConfig.HANDLER.load();

        LOGGER.info("Light OverlayRendererType (" + MOD_ID + ") initialized.");
	}
}