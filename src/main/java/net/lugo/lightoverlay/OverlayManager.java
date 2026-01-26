package net.lugo.lightoverlay;

import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.lugo.lightoverlay.config.ModConfig;
import net.lugo.lightoverlay.renderers.CarpetOverlayRenderer;
import net.lugo.lightoverlay.renderers.CrossOverlayRenderer;
import net.lugo.lightoverlay.renderers.MarkerOverlayRenderer;
import net.lugo.lightoverlay.renderers.NumberOverlayRenderer;
import net.lugo.lightoverlay.util.DistanceUtil;
import net.lugo.lightoverlay.util.HudMessage;
import net.lugo.lightoverlay.util.OverlayCache;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class OverlayManager {
    private static boolean activated = false;
    private static final Minecraft MC = Minecraft.getInstance();

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
        MutableComponent message = Component.translatable("text.light-overlay.message.toggle.on");
        if (!activated) message = Component.translatable("text.light-overlay.message.toggle.off");
        HudMessage.show(message, ChatFormatting.DARK_AQUA);
    }

    public static void render(WorldRenderContext context) {
        if (MC.player == null || MC.level == null || (!ModConfig.showWhenPaused && MC.isPaused()) || !activated) return;

        renderWithCache(context);
    }

    @SuppressWarnings("DataFlowIssue")
    private static void renderWithCache(WorldRenderContext context) {
        OverlayRenderer activeRenderer = ModConfig.rendererType.getRenderer();
        activeRenderer.startBatch(context);

        int nearbySectionCount = 0;
        int playerChunkX = (int) Math.floor(MC.player.getX() / 16.0);
        int playerChunkZ = (int) Math.floor(MC.player.getZ() / 16.0);

        List<SectionPos> sectionsToRender = new ArrayList<>();
        BlockPos playerPos = MC.player.blockPosition();

        int effectiveChunkScanRange = Math.min(ModConfig.chunkScanRange, MC.options.getEffectiveRenderDistance() + 1);

        for (int dx = -effectiveChunkScanRange; dx <= effectiveChunkScanRange; dx++) {
            for (int dz = -effectiveChunkScanRange; dz <= effectiveChunkScanRange; dz++) {
                if (dx * dx + dz * dz > effectiveChunkScanRange * effectiveChunkScanRange) continue;
                int chunkX = playerChunkX + dx;
                int chunkZ = playerChunkZ + dz;
                for (int sectionY = MC.level.getMinSectionY(); sectionY <= MC.level.getMaxSectionY(); sectionY++) {
                    sectionsToRender.add(SectionPos.of(chunkX, sectionY, chunkZ));
                    if (DistanceUtil.getDistanceSquared(SectionPos.of(chunkX, sectionY, chunkZ), playerPos) < ModConfig.nearbyCheckDistanceSquared) {
                        nearbySectionCount++;
                    }
                }
            }
        }

        sectionsToRender.sort((a, b) -> {
            double distA = DistanceUtil.getDistanceSquared(a, playerPos);
            double distB = DistanceUtil.getDistanceSquared(b, playerPos);
            return Double.compare(distA, distB);
        });

        for (SectionPos sectionPos : sectionsToRender) {
            OverlayCache.queueForCompute(sectionPos);
        }

        OverlayCache.processQueue();

        for (SectionPos sectionPos : sectionsToRender) {
            OverlayCache.CacheSectionPosEntry entry = OverlayCache.get(sectionPos);
            if (entry == null || entry.blocks == null) continue;

            for (OverlayCache.CacheBlockPosEntry blockEntry : entry.blocks) {
                BlockPos blockPos = blockEntry.pos();
                int lightLevel = blockEntry.lightLevel();
                boolean isNearby = nearbySectionCount > 0;
                activeRenderer.addBlock(blockPos, lightLevel, isNearby);
            }
            nearbySectionCount--;
        }

        activeRenderer.endBatch();
    }

    public static void draw() {
        OverlayRenderer activeRenderer = ModConfig.rendererType.getRenderer();
        activeRenderer.uploadThenDraw();
    }
}