package net.lugo.lightoverlay;

import net.fabricmc.api.ModInitializer;

import net.lugo.lightoverlay.config.ModConfig;
import net.lugo.lightoverlay.registration.Commands;
import net.lugo.lightoverlay.registration.KeyMappings;
import net.lugo.lightoverlay.registration.RenderingEvents;
import net.lugo.lightoverlay.util.IrisUtil;
import net.lugo.lightoverlay.util.RenderPipelines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightOverlay implements ModInitializer {
	public static final String MOD_ID = "light-overlay";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        LOGGER.info("Light Overlay (" + MOD_ID + ") initializing.");

        KeyMappings.registerKeyMappings();
        Commands.registerCommands();

        RenderingEvents.register();

        if (IrisUtil.irisDetected()) {
            LOGGER.info("Iris detected.");
            LOGGER.info("Registering pipelines with Iris.");
            RenderPipelines.registerWithIris();
        }

        ModConfig.HANDLER.load();

        LOGGER.info("Light Overlay (" + MOD_ID + ") initialized.");
	}
}