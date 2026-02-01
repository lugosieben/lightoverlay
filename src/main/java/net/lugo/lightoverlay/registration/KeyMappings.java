package net.lugo.lightoverlay.registration;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.lugo.lightoverlay.LightOverlay;
import net.lugo.lightoverlay.OverlayHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyMappings {

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.parse(LightOverlay.MOD_ID));
    private static final String BASE_KEY = "key." + LightOverlay.MOD_ID;

    public static void registerKeyMappings() {
        registerLightOverlayKeyMapping();
    }

    private static void registerLightOverlayKeyMapping() {
        KeyMapping lightOverlayKey = new KeyMapping(BASE_KEY + ".toggle", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F9, CATEGORY);
        KeyBindingHelper.registerKeyBinding(lightOverlayKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (lightOverlayKey.consumeClick() && lightOverlayKey.isDown()) {
                OverlayHandler.toggle();
            }
        });
    }
}
