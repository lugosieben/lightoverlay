package net.lugo.lightoverlay;

import net.lugo.lightoverlay.config.ModConfig;
import net.lugo.lightoverlay.renderers.CarpetOverlayRenderer;
import net.lugo.lightoverlay.renderers.CrossOverlayRenderer;
import net.lugo.lightoverlay.renderers.MarkerOverlayRenderer;
import net.lugo.lightoverlay.renderers.NumberOverlayRenderer;
import net.lugo.lightoverlay.util.ColorHelper;
import net.lugo.lightoverlay.util.HudMessage;
import net.lugo.lightoverlay.util.OverlayChecker;
import net.lugo.lightoverlay.util.ReusableBlockData;
import net.lugo.overlaylib.Overlay;
import net.lugo.overlaylib.OverlayRenderer;
import net.lugo.overlaylib.managers.CachedOverlayManager;
import net.lugo.overlaylib.util.OverlayRendererBlockData;
import net.lugo.overlaylib.util.TextureSection;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.FarmlandBlock;

public class OverlayHandler {
    private static final Minecraft MC = Minecraft.getInstance();

    private static boolean isActive = false;
    private static Mode activeMode;
    private static Overlay overlay;

    public enum Mode {
        CROSS(false),
        CARPET(false),
        NUMBER(true),
        MARKER(false);

        public final boolean lightLevelSpecific;
        public OverlayRenderer renderer;
        public Overlay overlay;

        Mode(boolean lightLevelSpecific) {
            this.lightLevelSpecific = lightLevelSpecific;
        }

        public OverlayRenderer createRenderer() {
            return switch (this) {
                case CROSS -> new CrossOverlayRenderer();
                case CARPET -> new CarpetOverlayRenderer();
                case NUMBER -> new NumberOverlayRenderer();
                case MARKER -> new MarkerOverlayRenderer();
            };
        }
    }

    private static final TextureSection.TextureSectionData lightLevelSpecificTextureSectionData = new TextureSection.TextureSectionData(16, 1);

    private static final CachedOverlayManager overlayManager = new CachedOverlayManager((blockPos -> {
        ReusableBlockData data = new ReusableBlockData(blockPos);
        OverlayChecker.CheckerResult checkerResult = OverlayChecker.shouldRenderOverlay(data);
        if (!checkerResult.shouldRender()) return OverlayRendererBlockData.NO_RENDER;
        //noinspection DataFlowIssue
        int lightLevel = MC.level.getBrightness(LightLayer.BLOCK, blockPos.above());
        if (lightLevel >= ModConfig.lightLevelThresholdForDimension(MC.level) && ModConfig.hideGreen) return OverlayRendererBlockData.NO_RENDER;
        float[] colors = ColorHelper.getOverlayColorFloats(lightLevel, MC.level);
        if (data.block() instanceof FarmlandBlock) {
            colors = ColorHelper.getOverlayColorFloats(lightLevel, ModConfig.lightLevelThresholdFarmland);
        }

        TextureSection textureSection = activeMode.lightLevelSpecific ? new TextureSection(lightLevelSpecificTextureSectionData, lightLevel, 0) : TextureSection.SINGULAR;

        return new OverlayRendererBlockData(blockPos, colors[0], colors[1], colors[2], checkerResult.yOffset(), textureSection);
    }));


    public static void init() {
        switchMode(ModConfig.rendererMode);
    }
    public static void toggle() {
        setActive(!isActive);
        if (isActive) {
            HudMessage.show(Component.translatable("text.light-overlay.message.toggle.on"), ChatFormatting.GREEN);
            return;
        }
        HudMessage.show(Component.translatable("text.light-overlay.message.toggle.off"), ChatFormatting.RED);
    }

    public static boolean isActive() {
        return isActive;
    }

    public static void setActive(boolean active) {
        isActive = active;
        if (overlay != null) {
            overlay.setActive(isActive);
        }
    }

    public static void switchMode(Mode mode) {
        activeMode = mode;
        if (overlay != null) {
            overlay.setActive(false);
        }
        if (mode.renderer == null) {
            mode.renderer = mode.createRenderer();
        }
        if (mode.overlay == null) {
            mode.overlay = new Overlay(mode.renderer, ModConfig.chunkScanRange, ModConfig.chunkScanRangeVertical, overlayManager);
            mode.overlay.setRenderFilter(() -> ModConfig.showWhenPaused || !MC.isPaused());
        }
        overlay = mode.overlay;
        overlay.setActive(isActive);
        overlay.register();
    }

    public static void setChunkScanRadius(int radius) {
        overlay.setChunkScanRadius(radius);
    }
    public static void setChunkScanRadiusVertical(int radius) {
        overlay.setChunkScanRadiusVertical(radius);
    }
    public static void setMaxComputationsPerTick(int maxComputationsPerTick) {
        overlayManager.setMaxComputationsPerTick(maxComputationsPerTick);
    }

    public static void reconstructRenderers() {
        LightOverlay.LOGGER.info("Reconstructing renderers");
        if (overlay != null) {
            overlay.setActive(false);
        }
        overlay = null;
        for (Mode mode : Mode.values()) {
            mode.overlay = null;
            mode.renderer = mode.createRenderer();
        }
        overlayManager.clearAll();
        init();
    }

    public static void refresh(BlockPos pos) {
        overlayManager.refresh(pos);
    }
    public static void refresh(SectionPos section) {
        overlayManager.refresh(section);
    }
    public static void clearAll() {
        LightOverlay.LOGGER.info("Clearing all");
        overlayManager.clearAll();
    }
}
