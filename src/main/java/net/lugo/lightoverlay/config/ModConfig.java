package net.lugo.lightoverlay.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.lugo.lightoverlay.LightOverlay;
import net.lugo.lightoverlay.OverlayManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.awt.*;


public class ModConfig {
    @SerialEntry
    public static int lightLevelThreshold = 1;

    @SerialEntry
    public static int scanRadius = 20;
    @SerialEntry
    public static int scanRadiusY = 5;

    @SerialEntry
    public static OverlayManager.OverlayRendererType rendererType = OverlayManager.OverlayRendererType.CROSS;

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
    public static boolean cylinderMode = false;

    @SerialEntry
    public static Color validColor = new Color(0, 255, 0, 255);
    @SerialEntry
    public static Color invalidColor = new Color(255, 0, 0, 255);

    public static Screen makeScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("text.light-overlay.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("text.light-overlay.config.category.main"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("text.light-overlay.config.group.general"))
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("text.light-overlay.config.option.light_level_threshold.name"))
                                        .description(OptionDescription.of(Text.translatable("text.light-overlay.config.option.light_level_threshold.description")))
                                        .binding(
                                                1,
                                                () -> lightLevelThreshold,
                                                newVal -> lightLevelThreshold = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 15)
                                                .step(1))
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("text.light-overlay.config.option.scan_radius.name"))
                                        .description(OptionDescription.of(Text.translatable("text.light-overlay.config.option.scan_radius.description")))
                                        .binding(
                                                20,
                                                () -> scanRadius,
                                                newVal -> scanRadius = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 50)
                                                .step(1))
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("text.light-overlay.config.option.scan_radius_y.name"))
                                        .description(OptionDescription.of(Text.translatable("text.light-overlay.config.option.scan_radius_y.description")))
                                        .binding(
                                                5,
                                                () -> scanRadiusY,
                                                newVal -> scanRadiusY = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 50)
                                                .step(1))
                                        .build())
                                .option(Option.<OverlayManager.OverlayRendererType>createBuilder()
                                        .name(Text.translatable("text.light-overlay.config.option.overlay_mode.name"))
                                        .description(OptionDescription.of(Text.translatable("text.light-overlay.config.option.overlay_mode.description")))
                                        .binding(
                                                OverlayManager.OverlayRendererType.CROSS,
                                                () -> rendererType,
                                                newVal -> rendererType = newVal)
                                        .controller(opt -> EnumControllerBuilder.create(opt)
                                                .enumClass(OverlayManager.OverlayRendererType.class)
                                                .formatValue(v -> Text.translatable("text.light-overlay.config.option.overlay_mode." + v.name().toLowerCase())))
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("text.light-overlay.config.group.overlay_hiding"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("text.light-overlay.config.option.hide_green.name"))
                                        .description(OptionDescription.of(Text.translatable("text.light-overlay.config.option.hide_green.description")))
                                        .binding(
                                                false,
                                                () -> hideGreen,
                                                newVal -> hideGreen = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("text.light-overlay.config.option.hide_transparent.name"))
                                        .description(OptionDescription.of(Text.translatable("text.light-overlay.config.option.hide_transparent.description")))
                                        .binding(
                                                true,
                                                () -> hideTransparent,
                                                newVal -> hideTransparent = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("text.light-overlay.config.option.hide_water.name"))
                                        .description(OptionDescription.of(Text.translatable("text.light-overlay.config.option.hide_water.description")))
                                        .binding(
                                                true,
                                                () -> hideWater,
                                                newVal -> hideWater = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("text.light-overlay.config.option.show_special_spawning_condition_blocks.name"))
                                        .description(OptionDescription.of(Text.translatable("text.light-overlay.config.option.show_special_spawning_condition_blocks.description")))
                                        .binding(
                                                false,
                                                () -> showSpecialSpawningConditionBlocks,
                                                newVal -> showSpecialSpawningConditionBlocks = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("text.light-overlay.config.group.customize"))
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("text.light-overlay.config.option.valid_color.name"))
                                        .description(OptionDescription.of(Text.translatable("text.light-overlay.config.option.valid_color.description")))
                                        .binding(
                                                new Color(0, 255, 0, 255),
                                                () -> validColor,
                                                newVal -> validColor = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt)
                                                .allowAlpha(true))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("text.light-overlay.config.option.invalid_color.name"))
                                        .description(OptionDescription.of(Text.translatable("text.light-overlay.config.option.invalid_color.description")))
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
                        .name(Text.translatable("text.light-overlay.config.category.misc"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("text.light-overlay.config.option.show_when_paused.name"))
                                .description(OptionDescription.of(Text.translatable("text.light-overlay.config.option.show_when_paused.description")))
                                .binding(
                                        true,
                                        () -> showWhenPaused,
                                        newVal -> showWhenPaused = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("text.light-overlay.config.option.cylinder_mode.name"))
                                .description(OptionDescription.of(Text.translatable("text.light-overlay.config.option.cylinder_mode.description")))
                                .binding(
                                        false,
                                        () -> cylinderMode,
                                        newVal -> cylinderMode = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("text.light-overlay.config.category.report"))
                        .option(ButtonOption.createBuilder()
                                .name(Text.translatable("text.light-overlay.config.button.report"))
                                .description(OptionDescription.of(Text.translatable("text.light-overlay.config.button.report.description")))
                                .text(Text.literal(""))
                                .action((yaclScreen, buttonOption) -> {
                                    var modContainerOpt = FabricLoader.getInstance().getModContainer(LightOverlay.MOD_ID);
                                    modContainerOpt.ifPresent(modContainer -> {
                                        var issuesUrlOpt = modContainer.getMetadata().getContact().get("issues");
                                        issuesUrlOpt.ifPresent(url -> Util.getOperatingSystem().open(url));
                                    });
                                })
                                .build())
                        .build())
                .save(HANDLER::save)
                .build()
                .generateScreen(parent);
    }

    public static final ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(Identifier.of(LightOverlay.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("light-overlay.json5"))
                    .setJson5(true)
                    .build())
            .build();
}
