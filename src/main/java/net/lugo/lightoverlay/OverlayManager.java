package net.lugo.lightoverlay;

import net.lugo.lightoverlay.config.ModConfig;
import net.lugo.lightoverlay.util.HudMessage;
import net.lugo.lightoverlay.util.OverlayChecker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;

import java.awt.*;

public class OverlayManager {
    private static boolean activated = false;
    private static final MinecraftClient MC = MinecraftClient.getInstance();

    public static void toggle() {
        activated = !activated;
        MutableText message = Text.translatable("text.light-overlay.message.toggle.on");
        if (!activated) message = Text.translatable("text.light-overlay.message.toggle.off");
        HudMessage.show(message, Formatting.DARK_AQUA);
    }

    public static void renderEnd() {
        if (MC.player == null || MC.world == null || MC.isPaused() || !activated) return;
        OverlayRenderer activeRenderer = LightOverlay.getCurrentRenderer();
        Vec3d playerPos = new Vec3d(MC.player.getX(), MC.player.getY(), MC.player.getZ());

        activeRenderer.startBatch();

        for (int x = -ModConfig.scanRadius; x <= ModConfig.scanRadius; x++) {
            for (int y = -ModConfig.scanRadius; y <= 5; y++) {
                for (int z = -ModConfig.scanRadius; z <= ModConfig.scanRadius; z++) {
                    Vec3d relativePos = new Vec3d(x,y,z);
                    Vec3d pos = playerPos.add(relativePos);
                    BlockPos blockPos = BlockPos.ofFloored(pos.x, pos.y, pos.z);
                    if (OverlayChecker.shouldRenderOverlay(blockPos)) {
                        Color color = Color.RED;
                        int blockLightLevel = MC.world.getLightLevel(LightType.BLOCK, blockPos.up());
                        if (blockLightLevel >= ModConfig.lightLevelThreshold) {
                            if (ModConfig.hideGreen) continue;
                            color = Color.GREEN;
                        }
                        activeRenderer.addBlock(MC.gameRenderer.getCamera(), blockPos, color.getRed(), color.getGreen(), color.getBlue());
                    }
                }
            }
        }

        activeRenderer.endBatch();
    }
}