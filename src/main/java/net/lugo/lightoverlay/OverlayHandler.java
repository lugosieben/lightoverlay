package net.lugo.lightoverlay;

import net.lugo.lightoverlay.config.ModConfig;
import net.lugo.lightoverlay.renderers.CarpetOverlayRenderer;
import net.lugo.lightoverlay.renderers.CrossOverlayRenderer;
import net.lugo.lightoverlay.renderers.MarkerOverlayRenderer;
import net.lugo.lightoverlay.renderers.NumberOverlayRenderer;
import net.lugo.lightoverlay.util.ColorHelper;
import net.lugo.lightoverlay.util.HudMessage;
import net.lugo.lightoverlay.util.OverlayChecker;
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

public class OverlayHandler {
    private static final Minecraft MC = Minecraft.getInstance();

    private static boolean isActive = false;
    private static Mode activeMode;
    private static Overlay overlay;

    public enum Mode {
        CROSS(new CrossOverlayRenderer(),false),
        CARPET(new CarpetOverlayRenderer(),false),
        NUMBER(new NumberOverlayRenderer(),true),
        MARKER(new MarkerOverlayRenderer(),false);

        public final OverlayRenderer renderer;
        public final boolean lightLevelSpecific;
        public Overlay overlay;

        Mode(OverlayRenderer renderer, boolean lightLevelSpecific) {
            this.renderer = renderer;
            this.lightLevelSpecific = lightLevelSpecific;
        }
    }

    private static final TextureSection.TextureSectionData lightLevelSpecificTextureSectionData = new TextureSection.TextureSectionData(16, 1);

    private static final CachedOverlayManager overlayManager = new CachedOverlayManager((blockPos -> {
        OverlayChecker.CheckerResult checkerResult = OverlayChecker.shouldRenderOverlay(blockPos);
        if (!checkerResult.shouldRender()) return OverlayRendererBlockData.NO_RENDER;
        //noinspection DataFlowIssue
        int lightLevel = MC.level.getBrightness(LightLayer.BLOCK, blockPos.above());
        if (lightLevel >= ModConfig.lightLevelThreshold && ModConfig.hideGreen) return OverlayRendererBlockData.NO_RENDER;
        float[] colors = ColorHelper.getOverlayColorFloats(lightLevel);

        TextureSection textureSection = activeMode.lightLevelSpecific ? new TextureSection(lightLevelSpecificTextureSectionData, lightLevel, 0) : TextureSection.SINGULAR;

        return new OverlayRendererBlockData(blockPos, colors[0], colors[1], colors[2], checkerResult.yOffset(), textureSection);
    }));


    public static void init() {
        switchMode(ModConfig.rendererMode);
    }
    public static void toggle() {
        isActive = !isActive;
        overlay.setActive(isActive);

        if (isActive) {
            HudMessage.show(Component.translatable("text.light-overlay.message.toggle.on"), ChatFormatting.GREEN);
            return;
        }
        HudMessage.show(Component.translatable("text.light-overlay.message.toggle.off"), ChatFormatting.RED);
    }

    public static void switchMode(Mode mode) {
        activeMode = mode;
        if (overlay != null) {
            overlay.setActive(false);
        }
        if (mode.overlay == null) {
            mode.overlay = new Overlay(mode.renderer, ModConfig.chunkScanRange, overlayManager);
        }
        overlay = mode.overlay;
        overlay.setActive(isActive);
        overlay.register();
    }

    public static void setChunkScanRadius(int radius) {
        overlay.setChunkScanRadius(radius);
    }
    public static void setMaxComputationsPerTick(int maxComputationsPerTick) {
        overlayManager.setMaxComputationsPerTick(maxComputationsPerTick);
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
