//? if fabric {
package net.pathdos.totemtweaks.loader.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.pathdos.totemtweaks.TotemTweaks;
import net.pathdos.totemtweaks.config.SettingsScreens;

import java.util.function.UnaryOperator;

public final class TotemTweaksFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricLoader loader = FabricLoader.getInstance();
        UnaryOperator<Screen> settingsScreen = SettingsScreens.find(loader::isModLoaded);

        TotemTweaks.initialize(loader.getConfigDir());
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, ignored) ->
                TotemTweaks.registerCommands(dispatcher, settingsScreen));
    }
}
//?}
