package net.lugo.lightoverlay.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.lugo.lightoverlay.OverlayHandler;
import net.lugo.lightoverlay.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;

public class LightOverlayCommand {
    final static Minecraft MC = Minecraft.getInstance();

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext ignoredCommandRegistryAccess) {
        dispatcher.register(ClientCommandManager.literal("lightoverlay")
                .then(ClientCommandManager.literal("config")
                        .executes(context -> {
                            MC.schedule(() -> MC.setScreen(ModConfig.makeScreen(MC.screen)));
                            return 1;
                        })
                )
                .executes(context -> {
                    OverlayHandler.toggle();
                    return 1;
                })
        );
    }
}
