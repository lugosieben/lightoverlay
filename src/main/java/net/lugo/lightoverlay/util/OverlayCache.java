package net.lugo.lightoverlay.util;

import net.lugo.lightoverlay.LightOverlay;
import net.lugo.lightoverlay.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LightLayer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OverlayCache {
    private static final Minecraft MC = Minecraft.getInstance();
    private static final ConcurrentHashMap<SectionPos, CacheSectionPosEntry> cache = new ConcurrentHashMap<>();
    private static int MAX_CACHE_SIZE = 1000;

    private static final Queue<SectionPos> computeQueue = new ConcurrentLinkedQueue<>();
    private static final Set<SectionPos> queuedSections = ConcurrentHashMap.newKeySet();

    public static class CacheSectionPosEntry {
        public final SectionPos pos;
        public final CacheBlockPosEntry[] blocks;
        public long lastAccessTime;

        public CacheSectionPosEntry(SectionPos pos, CacheBlockPosEntry[] blocks) {
            this.pos = pos;
            this.blocks = blocks;
            this.lastAccessTime = System.currentTimeMillis();
        }
    }

    public record CacheBlockPosEntry(BlockPos pos, int lightLevel) { }

    private static void updateMaxCacheSize() {
        if (MC.level == null) return;

        int chunkScanRange = ModConfig.chunkScanRange;
        int horizontalChunks = 0;
        for (int dx = -chunkScanRange; dx <= chunkScanRange; dx++) {
            for (int dz = -chunkScanRange; dz <= chunkScanRange; dz++) {
                if (dx * dx + dz * dz <= chunkScanRange * chunkScanRange) {
                    horizontalChunks++;
                }
            }
        }
        int verticalSections = MC.level.getMaxSectionY() - MC.level.getMinSectionY() + 1;
        int totalSections = horizontalChunks * verticalSections;

        int requiredSections = totalSections * 2;
        if (requiredSections > MAX_CACHE_SIZE) {
            LightOverlay.LOGGER.info("Resizing MAX_CACHE_SIZE, total sections * 2 ({}) is higher than current limit {}. New size is {}", requiredSections, MAX_CACHE_SIZE, requiredSections);
            MAX_CACHE_SIZE = requiredSections;
        }
    }

    public static void queueForCompute(SectionPos sectionPos) {
        if (cache.containsKey(sectionPos)) {
            return;
        }

        if (!queuedSections.contains(sectionPos)) {
            queuedSections.add(sectionPos);
            computeQueue.offer(sectionPos);
        }
    }

    public static void processQueue() {
        if (MC.level == null || MC.player == null) return;

        updateMaxCacheSize();

        if (computeQueue.size() > ModConfig.maxComputationsPerTick * 2) {
            reprioritizeQueue();
        }

        int processed = 0;
        while (processed < ModConfig.maxComputationsPerTick && !computeQueue.isEmpty()) {
            SectionPos sectionPos = computeQueue.poll();
            if (sectionPos != null) {
                queuedSections.remove(sectionPos);

                if (!cache.containsKey(sectionPos)) {
                    compute(sectionPos);
                }
                processed++;
            }
        }
    }

    private static void reprioritizeQueue() {
        if (MC.player == null) return;

        BlockPos playerPos = MC.player.blockPosition();
        List<SectionPos> sections = new ArrayList<>();

        SectionPos pos;
        while ((pos = computeQueue.poll()) != null) {
            sections.add(pos);
        }

        sections.sort((a, b) -> {
            double distA = DistanceUtil.getDistanceSquared(a, playerPos);
            double distB = DistanceUtil.getDistanceSquared(b, playerPos);
            return Double.compare(distA, distB);
        });

        computeQueue.addAll(sections);
    }

    public static void clear(SectionPos sectionPos) {
        cache.remove(sectionPos);
        queuedSections.remove(sectionPos);
    }

    public static void clearFromBlockPos(BlockPos blockPos) {
        SectionPos sectionPos = SectionPos.of(blockPos);
        if (sectionPos.minBlockY() == blockPos.getY()) clear(sectionPos.offset(0, -1, 0));
        clear(sectionPos);
    }

    private static void removeOldEntries() {
        if (cache.size() > MAX_CACHE_SIZE) {
            int toRemove = cache.size() / 5; // Remove oldest 20%
            cache.values().stream()
                .sorted(java.util.Comparator.comparingLong(a -> a.lastAccessTime))
                .limit(toRemove)
                .forEach(entry -> cache.remove(entry.pos));
        }
    }

    public static void compute(SectionPos sectionPos) {
        if (MC.level == null) return;

        List<CacheBlockPosEntry> renderableBlocks = new ArrayList<>();

        int minX = SectionPos.sectionToBlockCoord(sectionPos.getX());
        int minY = SectionPos.sectionToBlockCoord(sectionPos.getY());
        int minZ = SectionPos.sectionToBlockCoord(sectionPos.getZ());

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    BlockPos blockPos = new BlockPos(minX + x, minY + y, minZ + z);
                    boolean shouldRender = OverlayChecker.shouldRenderOverlay(blockPos);

                    if (shouldRender) {
                        int lightLevel = MC.level.getBrightness(LightLayer.BLOCK, blockPos.above());
                        renderableBlocks.add(new CacheBlockPosEntry(blockPos, lightLevel));
                    }
                }
            }
        }

        CacheBlockPosEntry[] blocksArray = renderableBlocks.toArray(new CacheBlockPosEntry[0]);
        cache.put(sectionPos, new CacheSectionPosEntry(sectionPos, blocksArray));

        removeOldEntries();
    }

    public static CacheSectionPosEntry get(SectionPos sectionPos) {
        CacheSectionPosEntry entry = cache.get(sectionPos);
        if (entry != null) {
            entry.lastAccessTime = System.currentTimeMillis();
        }
        return entry;
    }

    public static void clearAll() {
        cache.clear();
        computeQueue.clear();
        queuedSections.clear();
    }
}
