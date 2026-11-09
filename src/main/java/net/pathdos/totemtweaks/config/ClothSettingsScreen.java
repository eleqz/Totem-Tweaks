package net.pathdos.totemtweaks.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.FloatListEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.pathdos.totemtweaks.TotemTweaks;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class ClothSettingsScreen {

    private static final Settings DEFAULTS = new Settings();

    private ClothSettingsScreen() {
    }

    public static Screen create(@Nullable Screen parent) {
        Settings settings = TotemTweaks.settings();
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(text("title"))
                .setSavingRunnable(TotemTweaks::saveSettings);
        ConfigEntryBuilder entries = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(text("category.general"));
        general.addEntry(size(entries, "totem_size", settings.totemSize, DEFAULTS.totemSize, value -> settings.totemSize = value));
        general.addEntry(size(entries, "pop_size", settings.popSize, DEFAULTS.popSize, value -> settings.popSize = value));
        general.addEntry(toggle(entries, "disable_equip_animation", settings.disableEquipAnimation, DEFAULTS.disableEquipAnimation,
                value -> settings.disableEquipAnimation = value));

        ConfigCategory totemPop = builder.getOrCreateCategory(text("category.totem_pop"));
        totemPop.addEntry(toggle(entries, "pop_animation", settings.popAnimation, DEFAULTS.popAnimation, value -> settings.popAnimation = value));
        totemPop.addEntry(entries.startIntSlider(text("animation_speed"), settings.animationSpeed, 1, 100)
                .setDefaultValue(DEFAULTS.animationSpeed)
                .setTooltip(text("animation_speed.description"))
                .setSaveConsumer(value -> settings.animationSpeed = value)
                .build());
        totemPop.addEntry(toggle(entries, "lock_rotation_position", settings.lockRotationPosition, DEFAULTS.lockRotationPosition,
                value -> settings.lockRotationPosition = value));
        totemPop.addEntry(toggle(entries, "disable_rotations", settings.disableRotations, DEFAULTS.disableRotations,
                value -> settings.disableRotations = value));
        totemPop.addEntry(toggle(entries, "static_size", settings.staticSize, DEFAULTS.staticSize, value -> settings.staticSize = value));

        ConfigCategory sizeAnimation = builder.getOrCreateCategory(text("category.size_animation"));
        sizeAnimation.addEntry(toggle(entries, "enable_totem_size_change", settings.enableTotemSizeChange, DEFAULTS.enableTotemSizeChange,
                value -> settings.enableTotemSizeChange = value));
        sizeAnimation.addEntry(number(entries, "totem_size_change_speed", settings.totemSizeChangeSpeed, DEFAULTS.totemSizeChangeSpeed, 0.1F, 5.0F,
                value -> settings.totemSizeChangeSpeed = value));
        sizeAnimation.addEntry(size(entries, "min_totem_size", settings.minTotemSize, DEFAULTS.minTotemSize, value -> settings.minTotemSize = value));
        sizeAnimation.addEntry(size(entries, "max_totem_size", settings.maxTotemSize, DEFAULTS.maxTotemSize, value -> settings.maxTotemSize = value));

        return builder.build();
    }

    private static BooleanListEntry toggle(ConfigEntryBuilder entries, String key, boolean value, boolean defaultValue, Consumer<Boolean> setter) {
        return entries.startBooleanToggle(text(key), value)
                .setDefaultValue(defaultValue)
                .setTooltip(text(key + ".description"))
                .setSaveConsumer(setter)
                .build();
    }

    private static FloatListEntry size(ConfigEntryBuilder entries, String key, float value, float defaultValue, Consumer<Float> setter) {
        return number(entries, key, value, defaultValue, 0.05F, 2.0F, setter);
    }

    private static FloatListEntry number(ConfigEntryBuilder entries, String key, float value, float defaultValue,
                                         float min, float max, Consumer<Float> setter) {
        return entries.startFloatField(text(key), value)
                .setDefaultValue(defaultValue)
                .setMin(min)
                .setMax(max)
                .setTooltip(text(key + ".description"))
                .setSaveConsumer(setter)
                .build();
    }

    private static Component text(String key) {
        return Component.translatable(TotemTweaks.MOD_ID + ".settings." + key);
    }
}
