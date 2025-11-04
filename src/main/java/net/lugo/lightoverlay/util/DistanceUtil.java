package net.lugo.lightoverlay.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;

public class DistanceUtil {
    public static double getDistanceSquared(ChunkSectionPos sectionPos, BlockPos blockPos) {
        int centerX = ChunkSectionPos.getBlockCoord(sectionPos.getX()) + 8;
        int centerY = ChunkSectionPos.getBlockCoord(sectionPos.getY()) + 8;
        int centerZ = ChunkSectionPos.getBlockCoord(sectionPos.getZ()) + 8;

        double dx = centerX - blockPos.getX();
        double dy = centerY - blockPos.getY();
        double dz = centerZ - blockPos.getZ();

        return dx * dx + dy * dy + dz * dz;
    }
}
