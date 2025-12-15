package net.lugo.lightoverlay.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;

public class DistanceUtil {
    public static double getDistanceSquared(SectionPos sectionPos, BlockPos blockPos) {
        int centerX = SectionPos.sectionToBlockCoord(sectionPos.getX()) + 8;
        int centerY = SectionPos.sectionToBlockCoord(sectionPos.getY()) + 8;
        int centerZ = SectionPos.sectionToBlockCoord(sectionPos.getZ()) + 8;

        double dx = centerX - blockPos.getX();
        double dy = centerY - blockPos.getY();
        double dz = centerZ - blockPos.getZ();

        return dx * dx + dy * dy + dz * dz;
    }
}
