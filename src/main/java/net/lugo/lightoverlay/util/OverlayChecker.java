package net.lugo.lightoverlay.util;

import net.lugo.lightoverlay.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class OverlayChecker {
    public record CheckerResult(boolean shouldRender, float yOffset) {}
    private static final CheckerResult NO_RENDER_RESULT = new CheckerResult(false, 0f);

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


    @SuppressWarnings("DataFlowIssue")
    public static CheckerResult shouldRenderOverlay(BlockPos pos) {
        BlockState blockState = MC.level.getBlockState(pos);
        Block block = blockState.getBlock();
        BlockPos above = pos.above();
        BlockState aboveBlockState = MC.level.getBlockState(above);
        Block aboveBlock = aboveBlockState.getBlock();
        boolean isTopSolid = MC.level.loadedAndEntityCanStandOn(pos, MC.player);
        boolean isTopSolidException = topSolidExceptions.contains(block);
        if (isTopSolidException) isTopSolid = true;
        boolean aboveTopSolid = MC.level.loadedAndEntityCanStandOnFace(above, MC.player, Direction.DOWN);
        if (!isTopSolid || aboveTopSolid) return NO_RENDER_RESULT;

        if (isRedstonePowerComponent(aboveBlock)) return NO_RENDER_RESULT;

        boolean isForbiddenBlock = forbiddenBlocks.contains(block);
        boolean hideBecauseWater = ModConfig.hideWater && MC.level.isWaterAt(above);
        boolean hideBecauseTransparent = ModConfig.hideTransparent && !blockState.canOcclude();
        if (isTopSolidException) hideBecauseTransparent = false;
        boolean hideBecauseSpecialSpawnCondition = !ModConfig.showSpecialSpawningConditionBlocks &&
                specialSpawnConditionBlocks.contains(aboveBlock);
        boolean shouldRender = !(isForbiddenBlock || hideBecauseWater || hideBecauseTransparent || hideBecauseSpecialSpawnCondition);
        float yOffset = 0f;
        if (shouldRender && aboveBlock instanceof SnowLayerBlock && aboveBlockState.getValue(SnowLayerBlock.LAYERS) == 1) {
            yOffset = 0.125f;
        }
        return new CheckerResult(shouldRender, yOffset);
    }
}
