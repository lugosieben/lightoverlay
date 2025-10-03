package net.lugo.lightoverlay;

import net.lugo.lightoverlay.config.ModConfig;
import net.lugo.lightoverlay.util.HudMessage;
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
    private static final ModConfig config = LightOverlay.getConfig();
    static MinecraftClient MC = MinecraftClient.getInstance();

    public static void toggle() {
        activated = !activated;
        MutableText message = Text.translatable("text.light-overlay.message.toggle.on");
        if (!activated) message = Text.translatable("text.light-overlay.message.toggle.off");
        HudMessage.show(message, Formatting.DARK_AQUA);
    }

    public static void renderEnd() {
        if (MC.player == null || MC.world == null || MC.isPaused() || !activated) return;
        Vec3d playerPos = new Vec3d(MC.player.getX(), MC.player.getY(), MC.player.getZ());

        OverlayRenderer.startBatch();

        for (int x = -config.scanRadius; x <= config.scanRadius; x++) {
            for (int y = -config.scanRadius; y <= 5; y++) {
                for (int z = -config.scanRadius; z <= config.scanRadius; z++) {
                    Vec3d relativePos = new Vec3d(x,y,z);
                    Vec3d pos = playerPos.add(relativePos);
                    BlockPos blockPos = new BlockPos((int)pos.x, (int)pos.y, (int)pos.z);
                    if (MC.world.isTopSolid(blockPos, MC.player) && !MC.world.isTopSolid(blockPos.up(), MC.player)) {
                        Color color = Color.RED;
                        int blockLightLevel = MC.world.getLightLevel(LightType.BLOCK, blockPos.up());
                        if (blockLightLevel >= config.lightLevelThreshold) color = Color.GREEN;
                        OverlayRenderer.addBlock(MC.gameRenderer.getCamera(), Vec3d.of(blockPos), color.getRed(), color.getGreen(), color.getBlue(), 0.01F);
                    }
                }
            }
        }

        OverlayRenderer.endBatch();
    }
}