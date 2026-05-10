package net.lugo.lightoverlay.util;

import net.lugo.lightoverlay.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.*;

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


    public static CheckerResult shouldRenderOverlay(ReusableBlockData data) {
        ClientLevel level = MC.level;
        Player player = MC.player;
        if (level == null || player == null) {
            return NO_RENDER_RESULT;
        }
        boolean isTopSolid = level.loadedAndEntityCanStandOn(data.blockPos(), player);
        boolean isTopSolidException = topSolidExceptions.contains(data.block());
        if (isTopSolidException) isTopSolid = true;
        boolean aboveTopSolid = level.loadedAndEntityCanStandOnFace(data.above().blockPos(), player, Direction.DOWN);
        if (ModConfig.showOnFarmland && data.block() instanceof FarmlandBlock) {
            return new CheckerResult(true, -0.0625f);
        }
        if (!isTopSolid || aboveTopSolid) return NO_RENDER_RESULT;

        if (isRedstonePowerComponent(data.above().block())) return NO_RENDER_RESULT;

        boolean isForbiddenBlock = forbiddenBlocks.contains(data.block());
        boolean hideBecauseWater = ModConfig.hideWater && level.isWaterAt(data.above().blockPos());
        boolean hideBecauseTransparent = ModConfig.hideTransparent && !data.blockState().canOcclude();
        if (isTopSolidException) hideBecauseTransparent = false;
        boolean hideBecauseSpecialSpawnCondition = !ModConfig.showSpecialSpawningConditionBlocks &&
                specialSpawnConditionBlocks.contains(data.above().block());
        boolean shouldRender = !(isForbiddenBlock || hideBecauseWater || hideBecauseTransparent || hideBecauseSpecialSpawnCondition);
        float yOffset = 0f;
        if (shouldRender && data.above().block() instanceof SnowLayerBlock && data.above().blockState().getValue(SnowLayerBlock.LAYERS) == 1) {
            yOffset = 0.125f;
        }
        return new CheckerResult(shouldRender, yOffset);
    }
}
