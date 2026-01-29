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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.awt.*;


public class ModConfig {
    @SerialEntry
    public static int lightLevelThreshold = 1;

    @SerialEntry
    public static int chunkScanRange = 4;

    @SerialEntry
    public static OverlayHandler.Mode rendererMode = OverlayHandler.Mode.CROSS;

    @SerialEntry
    public static boolean hideGreen = false;
    @SerialEntry
    public static boolean hideTransparent = true;
    @SerialEntry
    public static boolean hideWater = true;
    @SerialEntry
    public static boolean showSpecialSpawningConditionBlocks = false;
    @SerialEntry
    public static boolean showWhenPaused = true;
    @SerialEntry
    public static boolean enableIrisFlickerFix = true;
    @SerialEntry
    public static Integer nearbyCheckDistanceSquared = 16 * 16;

    @SerialEntry
    public static Color validColor = new Color(0, 255, 0, 255);
    @SerialEntry
    public static Color invalidColor = new Color(255, 0, 0, 255);
    @SerialEntry
    public static int maxComputationsPerTick = 8;

    public static Screen makeScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("text.light-overlay.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("text.light-overlay.config.category.main"))
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("text.light-overlay.config.group.general"))
                                .option(Option.<Integer>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.light_level_threshold.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.light_level_threshold.description")))
                                        .binding(
                                                1,
                                                () -> lightLevelThreshold,
                                                newVal -> lightLevelThreshold = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 15)
                                                .step(1))
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.chunk_scan_range.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.chunk_scan_range.description")))
                                        .binding(
                                                4,
                                                () -> chunkScanRange,
                                                newVal -> {
                                                    chunkScanRange = newVal;
                                                    OverlayHandler.setChunkScanRadius(newVal);
                                                })
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 24)
                                                .step(1))
                                        .build())
                                .option(Option.<OverlayHandler.Mode>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.overlay_mode.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.overlay_mode.description")))
                                        .binding(
                                                OverlayHandler.Mode.CROSS,
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
                                .name(Component.translatable("text.light-overlay.config.group.overlay_hiding"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.hide_green.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.hide_green.description")))
                                        .binding(
                                                false,
                                                () -> hideGreen,
                                                newVal -> hideGreen = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.hide_transparent.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.hide_transparent.description")))
                                        .binding(
                                                true,
                                                () -> hideTransparent,
                                                newVal -> hideTransparent = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.hide_water.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.hide_water.description")))
                                        .binding(
                                                true,
                                                () -> hideWater,
                                                newVal -> hideWater = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.show_special_spawning_condition_blocks.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.show_special_spawning_condition_blocks.description")))
                                        .binding(
                                                false,
                                                () -> showSpecialSpawningConditionBlocks,
                                                newVal -> showSpecialSpawningConditionBlocks = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("text.light-overlay.config.group.customize"))
                                .option(Option.<Color>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.valid_color.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.valid_color.description")))
                                        .binding(
                                                new Color(0, 255, 0, 255),
                                                () -> validColor,
                                                newVal -> validColor = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt)
                                                .allowAlpha(true))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.invalid_color.name"))
                                        .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.invalid_color.description")))
                                        .binding(
                                                new Color(255, 0, 0, 255),
                                                () -> invalidColor,
                                                newVal -> invalidColor = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt)
                                                .allowAlpha(true))
                                        .build())
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("text.light-overlay.config.category.misc"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("text.light-overlay.config.option.show_when_paused.name"))
                                .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.show_when_paused.description")))
                                .binding(
                                        true,
                                        () -> showWhenPaused,
                                        newVal -> showWhenPaused = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.light-overlay.config.option.max_computations_per_tick.name"))
                                .description(OptionDescription.of(Component.translatable("text.light-overlay.config.option.max_computations_per_tick.description")))
                                .binding(
                                        8,
                                        () -> maxComputationsPerTick,
                                        newVal -> {
                                            maxComputationsPerTick = newVal;
                                            OverlayHandler.setMaxComputationsPerTick(newVal);
                                        })
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(1, 32)
                                        .step(1))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("text.light-overlay.config.group.experimental"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.enable_iris_flicker_fix.name"))
                                        .binding(
                                                true,
                                                () -> enableIrisFlickerFix,
                                                newVal -> enableIrisFlickerFix = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Component.translatable("text.light-overlay.config.option.nearby_check_distance.name"))
                                        .binding(
                                                16 * 16,
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
