//? if neoforge {
/*package net.pathdos.totemtweaks.loader.neoforge;

import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
//? if >=1.20.5 {
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//?} else {
/^import net.neoforged.neoforge.client.ConfigScreenHandler;
^///?}
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.pathdos.totemtweaks.TotemTweaks;
import net.pathdos.totemtweaks.config.SettingsScreens;

import java.util.function.UnaryOperator;

@Mod(TotemTweaks.MOD_ID)
public final class TotemTweaksNeoForge {

    public TotemTweaksNeoForge(ModContainer container, Dist dist) {
        if (!dist.isClient()) {
            return;
        }

        ModList mods = ModList.get();
        UnaryOperator<Screen> settingsScreen = SettingsScreens.find(mods::isLoaded);

        TotemTweaks.initialize(FMLPaths.CONFIGDIR.get());
        NeoForge.EVENT_BUS.addListener((RegisterClientCommandsEvent event) ->
                TotemTweaks.registerCommands(event.getDispatcher(), settingsScreen));

        if (settingsScreen != null) {
            registerSettingsScreen(container, settingsScreen);
        }
    }

    private static void registerSettingsScreen(ModContainer container, UnaryOperator<Screen> settingsScreen) {
        //? if >=1.20.5 {
        container.registerExtensionPoint(IConfigScreenFactory.class, (owner, parent) -> settingsScreen.apply(parent));
        //?} else {
        /^container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> settingsScreen.apply(parent)));
        ^///?}
    }
}
*///?}
