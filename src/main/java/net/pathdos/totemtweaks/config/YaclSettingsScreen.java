//? if >=1.20.2 {
package net.pathdos.totemtweaks.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.pathdos.totemtweaks.TotemTweaks;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class YaclSettingsScreen {

    private static final Settings DEFAULTS = new Settings();

    private YaclSettingsScreen() {
    }

    public static Screen create(@Nullable Screen parent) {
        Settings settings = TotemTweaks.settings();

        return YetAnotherConfigLib.createBuilder()
                .title(text("title"))
                .category(ConfigCategory.createBuilder()
                        .name(text("category.general"))
                        .option(size("totem_size", DEFAULTS.totemSize, () -> settings.totemSize, value -> settings.totemSize = value))
                        .option(size("pop_size", DEFAULTS.popSize, () -> settings.popSize, value -> settings.popSize = value))
                        .option(toggle("disable_equip_animation", DEFAULTS.disableEquipAnimation,
                                () -> settings.disableEquipAnimation, value -> settings.disableEquipAnimation = value))
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(text("category.totem_pop"))
                        .option(toggle("pop_animation", DEFAULTS.popAnimation, () -> settings.popAnimation, value -> settings.popAnimation = value))
                        .option(Option.<Integer>createBuilder()
                                .name(text("animation_speed"))
                                .description(OptionDescription.of(text("animation_speed.description")))
                                .binding(DEFAULTS.animationSpeed, () -> settings.animationSpeed, value -> settings.animationSpeed = value)
                                .controller(option -> IntegerSliderControllerBuilder.create(option).range(1, 100).step(1))
                                .build())
                        .option(toggle("lock_rotation_position", DEFAULTS.lockRotationPosition,
                                () -> settings.lockRotationPosition, value -> settings.lockRotationPosition = value))
                        .option(toggle("disable_rotations", DEFAULTS.disableRotations,
                                () -> settings.disableRotations, value -> settings.disableRotations = value))
                        .option(toggle("static_size", DEFAULTS.staticSize, () -> settings.staticSize, value -> settings.staticSize = value))
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(text("category.size_animation"))
                        .option(toggle("enable_totem_size_change", DEFAULTS.enableTotemSizeChange,
                                () -> settings.enableTotemSizeChange, value -> settings.enableTotemSizeChange = value))
                        .option(slider("totem_size_change_speed", DEFAULTS.totemSizeChangeSpeed, 0.1F, 5.0F, 0.1F,
                                () -> settings.totemSizeChangeSpeed, value -> settings.totemSizeChangeSpeed = value))
                        .option(size("min_totem_size", DEFAULTS.minTotemSize, () -> settings.minTotemSize, value -> settings.minTotemSize = value))
                        .option(size("max_totem_size", DEFAULTS.maxTotemSize, () -> settings.maxTotemSize, value -> settings.maxTotemSize = value))
                        .build())
                .save(TotemTweaks::saveSettings)
                .build()
                .generateScreen(parent);
    }

    private static Option<Boolean> toggle(String key, boolean defaultValue, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        return Option.<Boolean>createBuilder()
                .name(text(key))
                .description(OptionDescription.of(text(key + ".description")))
                .binding(defaultValue, getter, setter)
                .controller(TickBoxControllerBuilder::create)
                .build();
    }

    private static Option<Float> size(String key, float defaultValue, Supplier<Float> getter, Consumer<Float> setter) {
        return slider(key, defaultValue, 0.05F, 2.0F, 0.05F, getter, setter);
    }

    private static Option<Float> slider(String key, float defaultValue, float min, float max, float step,
                                        Supplier<Float> getter, Consumer<Float> setter) {
        return Option.<Float>createBuilder()
                .name(text(key))
                .description(OptionDescription.of(text(key + ".description")))
                .binding(defaultValue, getter, setter)
                .controller(option -> FloatSliderControllerBuilder.create(option).range(min, max).step(step))
                .build();
    }

    private static Component text(String key) {
        return Component.translatable(TotemTweaks.MOD_ID + ".settings." + key);
    }
}
//?}
