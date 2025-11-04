package net.lugo.lightoverlay.util;

import net.lugo.lightoverlay.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OverlayCache {
    private static final MinecraftClient MC = MinecraftClient.getInstance();
    private static final ConcurrentHashMap<ChunkSectionPos, CacheSectionPosEntry> cache = new ConcurrentHashMap<>();
    private static int MAX_CACHE_SIZE = 1000;
    private static final int MAX_COMPUTATIONS_PER_TICK = 4;
    private static RegistryKey<World> lastDimension = null;

    private static final Queue<ChunkSectionPos> computeQueue = new ConcurrentLinkedQueue<>();
    private static final Set<ChunkSectionPos> queuedSections = ConcurrentHashMap.newKeySet();

    public static class CacheSectionPosEntry {
        public final ChunkSectionPos pos;
        public final CacheBlockPosEntry[] blocks;
        public long lastAccessTime;

        public CacheSectionPosEntry(ChunkSectionPos pos, CacheBlockPosEntry[] blocks) {
            this.pos = pos;
            this.blocks = blocks;
            this.lastAccessTime = System.currentTimeMillis();
        }
    }

    public record CacheBlockPosEntry(BlockPos pos, int lightLevel) { }

    private static void checkDimensionChange() {
        if (MC.world != null) {
            RegistryKey<World> currentDimension = MC.world.getRegistryKey();
            if (lastDimension != currentDimension) {
                clearAll();
                lastDimension = currentDimension;
            }
        }
    }

    private static void updateMaxCacheSize() {
        if (MC.world == null) return;

        int chunkScanRange = ModConfig.chunkScanRange;
        int horizontalChunks = 0;
        for (int dx = -chunkScanRange; dx <= chunkScanRange; dx++) {
            for (int dz = -chunkScanRange; dz <= chunkScanRange; dz++) {
                if (dx * dx + dz * dz <= chunkScanRange * chunkScanRange) {
                    horizontalChunks++;
                }
            }
        }
        int verticalSections = MC.world.getTopSectionCoord() - MC.world.getBottomSectionCoord() + 1;
        int totalSections = horizontalChunks * verticalSections;

        int requiredCacheSize = totalSections * 2;

        MAX_CACHE_SIZE = Math.max(MAX_CACHE_SIZE, Math.max(requiredCacheSize, 1000));
    }

    public static void queueForCompute(ChunkSectionPos sectionPos) {
        if (cache.containsKey(sectionPos)) {
            return;
        }

        if (!queuedSections.contains(sectionPos)) {
            queuedSections.add(sectionPos);
            computeQueue.offer(sectionPos);
        }
    }

    public static void processQueue() {
        if (MC.world == null || MC.player == null) return;

        checkDimensionChange();
        updateMaxCacheSize();

        if (computeQueue.size() > MAX_COMPUTATIONS_PER_TICK * 2) {
            reprioritizeQueue();
        }

        int processed = 0;
        while (processed < MAX_COMPUTATIONS_PER_TICK && !computeQueue.isEmpty()) {
            ChunkSectionPos sectionPos = computeQueue.poll();
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

        BlockPos playerPos = MC.player.getBlockPos();
        List<ChunkSectionPos> sections = new ArrayList<>();

        ChunkSectionPos pos;
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

    public static void clear(ChunkSectionPos sectionPos) {
        cache.remove(sectionPos);
        queuedSections.remove(sectionPos);
    }

    public static void clearFromBlockPos(BlockPos blockPos) {
        ChunkSectionPos sectionPos = ChunkSectionPos.from(blockPos);
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

    public static void compute(ChunkSectionPos sectionPos) {
        if (MC.world == null) return;

        checkDimensionChange();

        List<CacheBlockPosEntry> renderableBlocks = new ArrayList<>();

        int minX = ChunkSectionPos.getBlockCoord(sectionPos.getX());
        int minY = ChunkSectionPos.getBlockCoord(sectionPos.getY());
        int minZ = ChunkSectionPos.getBlockCoord(sectionPos.getZ());

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    BlockPos blockPos = new BlockPos(minX + x, minY + y, minZ + z);
                    boolean shouldRender = OverlayChecker.shouldRenderOverlay(blockPos);

                    if (shouldRender) {
                        int lightLevel = MC.world.getLightLevel(LightType.BLOCK, blockPos.up());
                        renderableBlocks.add(new CacheBlockPosEntry(blockPos, lightLevel));
                    }
                }
            }
        }

        CacheBlockPosEntry[] blocksArray = renderableBlocks.toArray(new CacheBlockPosEntry[0]);
        cache.put(sectionPos, new CacheSectionPosEntry(sectionPos, blocksArray));

        removeOldEntries();
    }

    public static CacheSectionPosEntry get(ChunkSectionPos sectionPos) {
        checkDimensionChange();
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
