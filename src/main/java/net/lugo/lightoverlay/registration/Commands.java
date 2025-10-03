package net.lugo.lightoverlay.registration;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.lugo.lightoverlay.command.LightOverlayCommand;

public class Commands {
    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register(LightOverlayCommand::register);
    }
}
