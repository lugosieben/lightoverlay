package net.lugo.lightoverlay.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.lugo.lightoverlay.OverlayManager;
import net.lugo.lightoverlay.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;

public class LightOverlayCommand {
    final static MinecraftClient MC = MinecraftClient.getInstance();

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignoredCommandRegistryAccess) {
        dispatcher.register(ClientCommandManager.literal("lightoverlay")
                .then(ClientCommandManager.literal("config")
                        .executes(context -> {
                            MC.send(() -> MC.setScreen(ModConfig.makeScreen(MC.currentScreen)));
                            return 1;
                        })
                )
                .executes(context -> {
                    OverlayManager.toggle();
                    return 1;
                })
        );
    }
}
