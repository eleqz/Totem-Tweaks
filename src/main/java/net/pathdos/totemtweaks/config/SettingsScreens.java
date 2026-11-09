package net.pathdos.totemtweaks.config;

import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public final class SettingsScreens {

    //? if >=1.20.2 {
    private static final String YACL = "yet_another_config_lib_v3";
    //?}
    //? if fabric {
    private static final String CLOTH_CONFIG = "cloth-config";
    //?} else {
    /*private static final String CLOTH_CONFIG = "cloth_config";
    *///?}

    private SettingsScreens() {
    }

    public static @Nullable UnaryOperator<Screen> find(Predicate<String> isModLoaded) {
        //? if >=1.20.2 {
        if (isModLoaded.test(YACL)) {
            return YaclSettingsScreen::create;
        }
        //?}
        if (isModLoaded.test(CLOTH_CONFIG)) {
            return ClothSettingsScreen::create;
        }
        return null;
    }
}
