//? if fabric {
package net.pathdos.totemtweaks.loader.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.pathdos.totemtweaks.config.SettingsScreens;

import java.util.function.UnaryOperator;

public final class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        UnaryOperator<Screen> settingsScreen = SettingsScreens.find(FabricLoader.getInstance()::isModLoaded);
        if (settingsScreen != null) {
            return settingsScreen::apply;
        }
        return ModMenuApi.super.getModConfigScreenFactory();
    }
}
//?}
