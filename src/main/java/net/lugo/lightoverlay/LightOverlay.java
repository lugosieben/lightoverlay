package net.lugo.lightoverlay;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import net.lugo.lightoverlay.config.ModConfig;
import net.lugo.lightoverlay.registration.Commands;
import net.lugo.lightoverlay.registration.KeyBindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightOverlay implements ModInitializer {
	public static final String MOD_ID = "light-overlay";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static ModConfig CONFIG;

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
}