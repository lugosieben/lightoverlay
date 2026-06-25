package net.lugo.lightoverlay.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.lugo.lightoverlay.LightOverlay;
import net.lugo.lightoverlay.OverlayHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.level.Level;

import java.awt.*;


public class ModConfig {
    private static final class Defaults {
        static final int LIGHT_LEVEL_THRESHOLD = 1;
        static final int LIGHT_LEVEL_THRESHOLD_NETHER = 12;
        static final int LIGHT_LEVEL_THRESHOLD_END = 1;
        static final int LIGHT_LEVEL_THRESHOLD_FARMLAND = 9;
        static final int CHUNK_SCAN_RANGE = 4;
        static final int CHUNK_SCAN_RANGE_VERTICAL = 25;
        static final OverlayHandler.Mode RENDERER_MODE = OverlayHandler.Mode.CROSS;
        static final boolean HIDE_GREEN = false;
        static final boolean HIDE_TRANSPARENT = true;
        static final boolean HIDE_WATER = true;
        static final boolean SHOW_SPECIAL_SPAWNING_CONDITION_BLOCKS = false;
        static final boolean SHOW_ON_FARMLAND = false;
        static final boolean SHOW_WHEN_PAUSED = true;
        static final boolean ENABLE_IRIS_FLICKER_FIX = true;
        static final int NEARBY_CHECK_DISTANCE_SQUARED = 16 * 16;
        static final Color VALID_COLOR = new Color(0, 255, 0, 255);
        static final Color INVALID_COLOR = new Color(255, 0, 0, 255);
        static final int MAX_COMPUTATIONS_PER_TICK = 32;
    }

    @SerialEntry
    public static int lightLevelThreshold = Defaults.LIGHT_LEVEL_THRESHOLD;

    @SerialEntry
    public static int lightLevelThresholdNether = Defaults.LIGHT_LEVEL_THRESHOLD_NETHER;

    @SerialEntry
    public static int lightLevelThresholdEnd = Defaults.LIGHT_LEVEL_THRESHOLD_END;

    @SerialEntry
    public static int lightLevelThresholdFarmland = Defaults.LIGHT_LEVEL_THRESHOLD_FARMLAND;
    public static int lightLevelThresholdForDimension(ClientLevel level) {
        if (Level.NETHER.equals(level.dimension())) {
            return lightLevelThresholdNether;
        }
        if (Level.END.equals(level.dimension())) {
            return lightLevelThresholdEnd;
        }
        return lightLevelThreshold;
    }

    @SerialEntry
    public static int chunkScanRange = Defaults.CHUNK_SCAN_RANGE;

    @SerialEntry
    public static int chunkScanRangeVertical = Defaults.CHUNK_SCAN_RANGE_VERTICAL;
    public static boolean isChunkScanRangeVerticalInfinite(int value) {
        return value > 24;
    }

    @SerialEntry
    public static OverlayHandler.Mode rendererMode = Defaults.RENDERER_MODE;

    @SerialEntry
    public static boolean hideGreen = Defaults.HIDE_GREEN;

    @SerialEntry
    public static boolean hideTransparent = Defaults.HIDE_TRANSPARENT;

    @SerialEntry
    public static boolean hideWater = Defaults.HIDE_WATER;

    @SerialEntry
    public static boolean showSpecialSpawningConditionBlocks = Defaults.SHOW_SPECIAL_SPAWNING_CONDITION_BLOCKS;

    @SerialEntry
    public static boolean showOnFarmland = Defaults.SHOW_ON_FARMLAND;

    @SerialEntry
    public static boolean showWhenPaused = Defaults.SHOW_WHEN_PAUSED;

    @SerialEntry
    public static Integer nearbyCheckDistanceSquared = Defaults.NEARBY_CHECK_DISTANCE_SQUARED;

    @SerialEntry
    public static Color validColor = Defaults.VALID_COLOR;

    @SerialEntry
    public static Color invalidColor = Defaults.INVALID_COLOR;

    @SerialEntry
    public static int maxComputationsPerTick = Defaults.MAX_COMPUTATIONS_PER_TICK;

    @SerialEntry
    public static boolean enableIrisFlickerFix = Defaults.ENABLE_IRIS_FLICKER_FIX;

    public static Screen makeScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("text.light-overlay.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("text.light-overlay.config.category.main"))
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("text.light-overlay.config.group.general"))
                                .option(Option.<Integer>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.chunk_scan_range.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.chunk_scan_range.description")))
                                        .binding(
                                                Defaults.CHUNK_SCAN_RANGE,
                                                () -> chunkScanRange,
                                                newVal -> {
                                                    chunkScanRange = newVal;
                                                    OverlayHandler.setChunkScanRadius(newVal);
                                                })
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 24)
                                                .step(1))
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.scan_range_vertical.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.scan_range_vertical.description")))
                                        .binding(
                                                Defaults.CHUNK_SCAN_RANGE_VERTICAL,
                                                () -> chunkScanRangeVertical,
                                                newVal -> {
                                                    chunkScanRangeVertical = newVal;
                                                    OverlayHandler.setChunkScanRadiusVertical(newVal);
                                                })
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 25)
                                                .step(1)
                                                .formatValue(v -> isChunkScanRangeVerticalInfinite(v) ? Component.translatable("text.light-overlay.config.option.scan_range_vertical.infinite") : Component.literal(v.toString())))
                                        .build())
                                .option(Option.<OverlayHandler.Mode>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.overlay_mode.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.overlay_mode.description")))
                                        .binding(
                                                Defaults.RENDERER_MODE,
                                                () -> rendererMode,
                                                newVal -> {
                                                    rendererMode = newVal;
                                                    OverlayHandler.switchMode(newVal);
                                                })
                                        .controller(opt -> EnumControllerBuilder.create(opt)
                                                .enumClass(OverlayHandler.Mode.class)
                                                .formatValue(v -> Component.translatable("text.light-overlay.config.option.overlay_mode." + v.name().toLowerCase())))
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("text.light-overlay.config.group.light_level_thresholds"))
                                .option(Option.<Integer>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.light_level_threshold.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.light_level_threshold.description")))
                                        .binding(
                                                Defaults.LIGHT_LEVEL_THRESHOLD,
                                                () -> lightLevelThreshold,
                                                newVal -> {
                                                    lightLevelThreshold = newVal;
                                                    OverlayHandler.clearAll();
                                                })
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 15)
                                                .step(1))
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.light_level_threshold_nether.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.light_level_threshold_nether.description")))
                                        .binding(
                                                Defaults.LIGHT_LEVEL_THRESHOLD_NETHER,
                                                () -> lightLevelThresholdNether,
                                                newVal -> {
                                                    lightLevelThresholdNether = newVal;
                                                    OverlayHandler.clearAll();
                                                })
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 15)
                                                .step(1))
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.light_level_threshold_end.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.light_level_threshold_end.description")))
                                        .binding(
                                                Defaults.LIGHT_LEVEL_THRESHOLD_END,
                                                () -> lightLevelThresholdEnd,
                                                newVal -> {
                                                    lightLevelThresholdEnd = newVal;
                                                    OverlayHandler.clearAll();
                                                })
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 15)
                                                .step(1))
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.light_level_threshold_farmland.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.light_level_threshold_farmland.description")))
                                        .binding(
                                                Defaults.LIGHT_LEVEL_THRESHOLD_FARMLAND,
                                                () -> lightLevelThresholdFarmland,
                                                newVal -> {
                                                    lightLevelThresholdFarmland = newVal;
                                                    OverlayHandler.clearAll();
                                                })
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 15)
                                                .step(1))
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("text.light-overlay.config.group.overlay_hiding"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.hide_green.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.hide_green.description")))
                                        .binding(
                                                Defaults.HIDE_GREEN,
                                                () -> hideGreen,
                                                newVal -> {
                                                    hideGreen = newVal;
                                                    OverlayHandler.clearAll();
                                                })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.hide_transparent.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.hide_transparent.description")))
                                        .binding(
                                                Defaults.HIDE_TRANSPARENT,
                                                () -> hideTransparent,
                                                newVal -> {
                                                    hideTransparent = newVal;
                                                    OverlayHandler.clearAll();
                                                })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.hide_water.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.hide_water.description")))
                                        .binding(
                                                Defaults.HIDE_WATER,
                                                () -> hideWater,
                                                newVal -> {
                                                    hideWater = newVal;
                                                    OverlayHandler.clearAll();
                                                })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.show_special_spawning_condition_blocks.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.show_special_spawning_condition_blocks.description")))
                                        .binding(
                                                Defaults.SHOW_SPECIAL_SPAWNING_CONDITION_BLOCKS,
                                                () -> showSpecialSpawningConditionBlocks,
                                                newVal -> {
                                                    showSpecialSpawningConditionBlocks = newVal;
                                                    OverlayHandler.clearAll();
                                                })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.show_on_farmland.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.show_on_farmland.description")))
                                        .binding(
                                                Defaults.SHOW_ON_FARMLAND,
                                                () -> showOnFarmland,
                                                newVal -> {
                                                    showOnFarmland = newVal;
                                                    OverlayHandler.clearAll();
                                                })
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("text.light-overlay.config.group.customize"))
                                .option(Option.<Color>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.valid_color.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.valid_color.description")))
                                        .binding(
                                                Defaults.VALID_COLOR,
                                                () -> validColor,
                                                newVal -> {
                                                    validColor = newVal;
                                                    OverlayHandler.clearAll();
                                                })
                                        .controller(opt -> ColorControllerBuilder.create(opt)
                                                .allowAlpha(false))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.invalid_color.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.invalid_color.description")))
                                        .binding(
                                                Defaults.INVALID_COLOR,
                                                () -> invalidColor,
                                                newVal -> {
                                                    invalidColor = newVal;
                                                    OverlayHandler.clearAll();
                                                })
                                        .controller(opt -> ColorControllerBuilder.create(opt)
                                                .allowAlpha(false))
                                        .build())
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("text.light-overlay.config.category.misc"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("text.light-overlay.config.option.show_when_paused.name"))
                                .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.show_when_paused.description")))
                                .binding(
                                        Defaults.SHOW_WHEN_PAUSED,
                                        () -> showWhenPaused,
                                        newVal -> showWhenPaused = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                                .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.light-overlay.config.option.max_computations_per_tick.name"))
                                .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.max_computations_per_tick.description")))
                                .binding(
                                        Defaults.MAX_COMPUTATIONS_PER_TICK,
                                        () -> maxComputationsPerTick,
                                        newVal -> {
                                            maxComputationsPerTick = newVal;
                                            OverlayHandler.setMaxComputationsPerTick(newVal);
                                        })
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(1, 128)
                                        .step(1))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("text.light-overlay.config.group.experimental"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.enable_iris_flicker_fix.name"))
                                        .binding(
                                                Defaults.ENABLE_IRIS_FLICKER_FIX,
                                                () -> enableIrisFlickerFix,
                                                newVal -> enableIrisFlickerFix = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.nearby_check_distance.name"))
                                        .binding(
                                                Defaults.NEARBY_CHECK_DISTANCE_SQUARED,
                                                () -> nearbyCheckDistanceSquared,
                                                newVal -> nearbyCheckDistanceSquared = newVal)
                                        .controller(IntegerFieldControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("text.light-overlay.config.category.report"))
                        .option(ButtonOption.createBuilder()
                                .name(Component.translatable("text.light-overlay.config.button.report"))
                                .description(OptionDescription.of(Component.translatable("text.light-overlay.config.button.report.description")))
                                .text(Component.literal(""))
                                .action((yaclScreen, buttonOption) -> {
                                    var modContainerOpt = FabricLoader.getInstance().getModContainer(LightOverlay.MOD_ID);
                                    modContainerOpt.ifPresent(modContainer -> {
                                        var issuesUrlOpt = modContainer.getMetadata().getContact().get("issues");
                                        issuesUrlOpt.ifPresent(url -> Util.getPlatform().openUri(url));
                                    });
                                })
                                .build())
                        .build())
                .save(HANDLER::save)
                .build()
                .generateScreen(parent);
    }

    public static final ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(Identifier.fromNamespaceAndPath(LightOverlay.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("light-overlay.json5"))
                    .setJson5(true)
                    .build())
            .build();
}
