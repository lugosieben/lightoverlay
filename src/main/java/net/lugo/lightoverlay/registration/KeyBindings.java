package net.lugo.lightoverlay.registration;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.lugo.lightoverlay.LightOverlay;
import net.lugo.lightoverlay.OverlayManager;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    private static final String CATEGORY = "key.categories." + LightOverlay.MOD_ID;
    private static final String BASE_KEY = "key." + LightOverlay.MOD_ID;

    public static void registerKeybinds() {
        registerLightOverlayKey();
    }

    private static void registerLightOverlayKey() {
        KeyBinding lightOverlayKey = new KeyBinding(BASE_KEY + ".toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F9, CATEGORY);
        KeyBindingHelper.registerKeyBinding(lightOverlayKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (lightOverlayKey.wasPressed()) {
                OverlayManager.toggle();
            }
        });
    }
}
