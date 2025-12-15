package net.lugo.lightoverlay.util;

import net.lugo.lightoverlay.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DaylightDetectorBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.SculkSensorBlock;
import java.util.Set;

public class OverlayChecker {
    private static final Minecraft MC = Minecraft.getInstance();
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
    private static boolean isRedstonePowerComponent(Block block) {
        return  block instanceof ButtonBlock ||
                block instanceof PressurePlateBlock ||
                block instanceof LeverBlock ||
                block instanceof RedstoneTorchBlock ||
                block instanceof SculkSensorBlock ||
                block instanceof DaylightDetectorBlock ||
                block instanceof PoweredRailBlock ||
                block instanceof LightningRodBlock;
    }


    public static boolean shouldRenderOverlay(BlockPos pos) {
        //noinspection DataFlowIssue
        boolean isTopSolid = MC.level.loadedAndEntityCanStandOn(pos, MC.player);
        boolean isTopSolidException = topSolidExceptions.contains(MC.level.getBlockState(pos).getBlock());
        if (isTopSolidException) isTopSolid = true;
        BlockPos above = pos.above();
        boolean aboveTopSolid = MC.level.loadedAndEntityCanStandOnFace(above, MC.player, Direction.DOWN);
        if (!isTopSolid || aboveTopSolid) return false;

        if (isRedstonePowerComponent(MC.level.getBlockState(above).getBlock())) return false;

        boolean isForbiddenBlock = forbiddenBlocks.contains(MC.level.getBlockState(pos).getBlock());
        boolean hideBecauseWater = ModConfig.hideWater && MC.level.isWaterAt(above);
        boolean hideBecauseTransparent = ModConfig.hideTransparent && !MC.level.getBlockState(pos).canOcclude();
        if (isTopSolidException) hideBecauseTransparent = false;
        boolean hideBecauseSpecialSpawnCondition = !ModConfig.showSpecialSpawningConditionBlocks &&
                specialSpawnConditionBlocks.contains(MC.level.getBlockState(above).getBlock());
        return !(isForbiddenBlock || hideBecauseWater || hideBecauseTransparent || hideBecauseSpecialSpawnCondition);
    }
}
