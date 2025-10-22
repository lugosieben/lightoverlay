package net.lugo.lightoverlay.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.lugo.lightoverlay.LightOverlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;


public class ModConfig {
    public static int lightLevelThreshold = 1;

    public static int scanRadius = 20;

    public static boolean carpetMode = false;

    public static boolean hideGreen = false;
    public static boolean hideTransparent = true;
    public static boolean hideWater = true;
    public static boolean showSpecialSpawningConditionBlocks = false;

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
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("text.light-overlay.config.option.carpet_mode.name"))
                                        .description(OptionDescription.of(Text.translatable("text.light-overlay.config.option.carpet_mode.description")))
                                        .binding(
                                                false,
                                                () -> carpetMode,
                                                newVal -> carpetMode = newVal)
                                        .controller(TickBoxControllerBuilder::create)
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
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("text.light-overlay.config.category.report"))
                        .option(ButtonOption.createBuilder()
                                .name(Text.translatable("text.light-overlay.config.button.report"))
                                .description(OptionDescription.of(Text.translatable("text.light-overlay.config.button.report.description")))
                                .action(((yaclScreen, buttonOption) -> {
                                    var modContainerOpt = FabricLoader.getInstance().getModContainer(LightOverlay.MOD_ID);
                                    modContainerOpt.ifPresent(modContainer -> {
                                        var issuesUrlOpt = modContainer.getMetadata().getContact().get("issues");
                                        issuesUrlOpt.ifPresent(url -> Util.getOperatingSystem().open(url));
                                    });
                                }))
                                .build())
                        .build())
                .build()
                .generateScreen(parent);
    }
}
