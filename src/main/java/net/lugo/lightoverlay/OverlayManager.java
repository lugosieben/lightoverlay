package net.lugo.lightoverlay;

import net.lugo.lightoverlay.config.ModConfig;
import net.lugo.lightoverlay.renderers.CarpetOverlayRenderer;
import net.lugo.lightoverlay.renderers.CrossOverlayRenderer;
import net.lugo.lightoverlay.renderers.MarkerOverlayRenderer;
import net.lugo.lightoverlay.renderers.NumberOverlayRenderer;
import net.lugo.lightoverlay.util.HudMessage;
import net.lugo.lightoverlay.util.OverlayChecker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class OverlayManager {
    private static boolean activated = false;
    private static final MinecraftClient MC = MinecraftClient.getInstance();

    public enum OverlayRendererType {
        CARPET(new CarpetOverlayRenderer()),
        CROSS(new CrossOverlayRenderer()),
        NUMBER(new NumberOverlayRenderer()),
        MARKER(new MarkerOverlayRenderer());

        private final OverlayRenderer renderer;

        OverlayRendererType(OverlayRenderer renderer) {
            this.renderer = renderer;
        }

        public OverlayRenderer getRenderer() {
            return renderer;
        }
    }

    public static void toggle() {
        activated = !activated;
        MutableText message = Text.translatable("text.light-overlay.message.toggle.on");
        if (!activated) message = Text.translatable("text.light-overlay.message.toggle.off");
        HudMessage.show(message, Formatting.DARK_AQUA);
    }

    public static void renderEnd() {
        if (MC.player == null || MC.world == null || (!ModConfig.showWhenPaused && MC.isPaused()) || !activated) return;
        Vec3d playerPos = new Vec3d(MC.player.getX(), MC.player.getY(), MC.player.getZ());
        OverlayRenderer activeRenderer = ModConfig.rendererType.getRenderer();
        activeRenderer.startBatch();

        for (int x = -ModConfig.scanRadius; x <= ModConfig.scanRadius; x++) {
            for (int z = -ModConfig.scanRadius; z <= ModConfig.scanRadius; z++) {
                if (ModConfig.cylinderMode && x * x + z * z > ModConfig.scanRadius * ModConfig.scanRadius) continue;
                for (int y = -ModConfig.scanRadiusY; y <= ModConfig.scanRadiusY; y++) {
                    Vec3d relativePos = new Vec3d(x,y,z);
                    Vec3d pos = playerPos.add(relativePos);
                    BlockPos blockPos = BlockPos.ofFloored(pos.x, pos.y, pos.z);
                    if (OverlayChecker.shouldRenderOverlay(blockPos)) {
                        activeRenderer.addBlock(MC.gameRenderer.getCamera(), blockPos);
                    }
                }
            }
        }

        activeRenderer.endBatch();
    }
}