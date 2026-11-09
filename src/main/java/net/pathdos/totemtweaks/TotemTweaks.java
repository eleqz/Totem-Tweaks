package net.pathdos.totemtweaks;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.gui.screens.Screen;
import net.pathdos.totemtweaks.command.SettingsCommand;
import net.pathdos.totemtweaks.command.SimulatePopCommand;
import net.pathdos.totemtweaks.config.Settings;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

public final class TotemTweaks {

    public static final String MOD_ID = "totemtweaks";

    private static Path settingsPath;
    private static Settings settings = new Settings();

    private TotemTweaks() {
    }

    public static void initialize(Path configDirectory) {
        settingsPath = configDirectory.resolve(MOD_ID + ".json");
        settings = Settings.load(settingsPath);
        // Written back so the file lists every setting, for anyone editing it without the settings screen.
        settings.save(settingsPath);
    }

    public static Settings settings() {
        return settings;
    }

    public static void saveSettings() {
        settings.save(settingsPath);
    }

    public static <S> void registerCommands(CommandDispatcher<S> dispatcher, @Nullable UnaryOperator<Screen> settingsScreen) {
        dispatcher.register(SimulatePopCommand.create());
        if (settingsScreen != null) {
            dispatcher.register(SettingsCommand.create(settingsScreen));
        }
    }
}
