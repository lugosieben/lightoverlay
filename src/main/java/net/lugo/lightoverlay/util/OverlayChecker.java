package net.lugo.lightoverlay.util;

import net.lugo.lightoverlay.config.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Set;

public class OverlayChecker {
    private static final MinecraftClient MC = MinecraftClient.getInstance();
    private static final Set<Block> forbiddenBlocks = Set.of(
            Blocks.BEDROCK,
            Blocks.COMMAND_BLOCK,
            Blocks.CHAIN_COMMAND_BLOCK,
            Blocks.REPEATING_COMMAND_BLOCK
    );
    private static final Set<Block> topSolidExceptions = Set.of(
            Blocks.SLIME_BLOCK,
            Blocks.MUD,
            Blocks.SOUL_SAND
    );
    private static final Set<Block> specialSpawnConditionBlocks = Set.of(
            Blocks.RAIL,                // Only aquatic mobs
            Blocks.ACTIVATOR_RAIL,
            Blocks.DETECTOR_RAIL,
            Blocks.POWERED_RAIL,
            Blocks.WITHER_ROSE,         // Only wither skeletons
            Blocks.SWEET_BERRY_BUSH    // Only foxes
    );


    public static boolean shouldRenderOverlay(BlockPos pos) {
        //noinspection DataFlowIssue
        boolean isTopSolid = MC.world.isTopSolid(pos, MC.player);
        boolean isTopSolidException = topSolidExceptions.contains(MC.world.getBlockState(pos).getBlock());
        if (isTopSolidException) isTopSolid = true;
        BlockPos above = pos.up();
        boolean aboveTopSolid = MC.world.isDirectionSolid(above, MC.player, Direction.DOWN);
        if (!isTopSolid || aboveTopSolid) return false;

        boolean isForbiddenBlock = forbiddenBlocks.contains(MC.world.getBlockState(pos).getBlock());
        boolean hideBecauseWater = ModConfig.hideWater && MC.world.isWater(above);
        boolean hideBecauseTransparent = ModConfig.hideTransparent && !MC.world.getBlockState(pos).isOpaque();
        if (isTopSolidException) hideBecauseTransparent = false;
        boolean hideBecauseSpecialSpawnCondition = !ModConfig.showSpecialSpawningConditionBlocks &&
                specialSpawnConditionBlocks.contains(MC.world.getBlockState(above).getBlock());
        return !(isForbiddenBlock || hideBecauseWater || hideBecauseTransparent || hideBecauseSpecialSpawnCondition);
    }
}
