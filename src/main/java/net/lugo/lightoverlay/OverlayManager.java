package net.lugo.lightoverlay;

import net.lugo.lightoverlay.config.ModConfig;
import net.lugo.lightoverlay.renderers.CarpetOverlayRenderer;
import net.lugo.lightoverlay.renderers.CrossOverlayRenderer;
import net.lugo.lightoverlay.renderers.MarkerOverlayRenderer;
import net.lugo.lightoverlay.renderers.NumberOverlayRenderer;
import net.lugo.lightoverlay.util.DistanceUtil;
import net.lugo.lightoverlay.util.HudMessage;
import net.lugo.lightoverlay.util.OverlayCache;
import net.lugo.lightoverlay.util.OverlayChecker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.LightType;

import java.util.ArrayList;
import java.util.List;

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

        if (ModConfig.enableCache) {
            renderWithCache();
        } else {
            renderWithoutCache();
        }
    }

    @SuppressWarnings("DataFlowIssue")
    private static void renderWithCache() {
        OverlayRenderer activeRenderer = ModConfig.rendererType.getRenderer();
        activeRenderer.startBatch();

        int playerChunkX = (int) Math.floor(MC.player.getX() / 16.0);
        int playerChunkZ = (int) Math.floor(MC.player.getZ() / 16.0);

        List<ChunkSectionPos> sectionsToRender = new ArrayList<>();
        BlockPos playerPos = MC.player.getBlockPos();

        int effectiveChunkScanRange = Math.min(ModConfig.chunkScanRange, MC.options.getClampedViewDistance());

        for (int dx = -effectiveChunkScanRange; dx <= effectiveChunkScanRange; dx++) {
            for (int dz = -effectiveChunkScanRange; dz <= effectiveChunkScanRange; dz++) {
                if (dx * dx + dz * dz > effectiveChunkScanRange * effectiveChunkScanRange) continue;
                int chunkX = playerChunkX + dx;
                int chunkZ = playerChunkZ + dz;
                for (int sectionY = MC.world.getBottomSectionCoord(); sectionY <= MC.world.getTopSectionCoord(); sectionY++) {
                    sectionsToRender.add(ChunkSectionPos.from(chunkX, sectionY, chunkZ));
                }
            }
        }

        sectionsToRender.sort((a, b) -> {
            double distA = DistanceUtil.getDistanceSquared(a, playerPos);
            double distB = DistanceUtil.getDistanceSquared(b, playerPos);
            return Double.compare(distA, distB);
        });

        for (ChunkSectionPos sectionPos : sectionsToRender) {
            OverlayCache.queueForCompute(sectionPos);
        }

        OverlayCache.processQueue();

        for (ChunkSectionPos sectionPos : sectionsToRender) {
            OverlayCache.CacheSectionPosEntry entry = OverlayCache.get(sectionPos);
            if (entry == null || entry.blocks == null) continue;

            for (OverlayCache.CacheBlockPosEntry blockEntry : entry.blocks) {
                BlockPos blockPos = blockEntry.pos();
                int lightLevel = blockEntry.lightLevel();
                activeRenderer.addBlock(MC.gameRenderer.getCamera(), blockPos, lightLevel);
            }
        }

        activeRenderer.endBatch();
    }


    @SuppressWarnings("DataFlowIssue")
    @Deprecated
    private static void renderWithoutCache() {
        OverlayRenderer activeRenderer = ModConfig.rendererType.getRenderer();
        activeRenderer.startBatch();

        int playerChunkX = (int) Math.floor(MC.player.getX() / 16.0);
        int playerChunkZ = (int) Math.floor(MC.player.getZ() / 16.0);

        int effectiveChunkScanRange = Math.min(ModConfig.chunkScanRange, MC.options.getClampedViewDistance());

        for (int dx = -effectiveChunkScanRange; dx <= effectiveChunkScanRange; dx++) {
            for (int dz = -effectiveChunkScanRange; dz <= effectiveChunkScanRange; dz++) {
                if (dx * dx + dz * dz > ModConfig.chunkScanRange * ModConfig.chunkScanRange) continue;
                int chunkX = playerChunkX + dx;
                int chunkZ = playerChunkZ + dz;
                for (int sectionY = MC.world.getBottomSectionCoord(); sectionY <= MC.world.getTopSectionCoord(); sectionY++) {
                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 16; z++) {
                                int blockX = chunkX * 16 + x;
                                int blockY = sectionY * 16 + y;
                                int blockZ = chunkZ * 16 + z;
                                BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);
                                if (OverlayChecker.shouldRenderOverlay(blockPos)) {
                                    int lightLevel = MC.world.getLightLevel(LightType.BLOCK, blockPos.up());
                                    activeRenderer.addBlock(MC.gameRenderer.getCamera(), blockPos, lightLevel);
                                }
                            }
                        }
                    }
                }
            }
        }

        activeRenderer.endBatch();
    }
}