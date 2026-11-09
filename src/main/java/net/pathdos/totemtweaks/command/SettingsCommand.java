package net.pathdos.totemtweaks.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.UnaryOperator;

public final class SettingsCommand {

    private SettingsCommand() {
    }

    public static <S> LiteralArgumentBuilder<S> create(UnaryOperator<Screen> settingsScreen) {
        return LiteralArgumentBuilder.<S>literal("totemTweaks").executes(ignored -> run(settingsScreen));
    }

    private static int run(UnaryOperator<Screen> settingsScreen) {
        Minecraft minecraft = Minecraft.getInstance();
        // Chat closes its screen after running the command, which would close the settings again right away.
        //? if >=26.2 {
        minecraft.schedule(() -> minecraft.gui.setScreen(settingsScreen.apply(null)));
        //?} else if >=1.21.2 {
        /*minecraft.schedule(() -> minecraft.setScreen(settingsScreen.apply(null)));
        *///?} else {
        /*minecraft.tell(() -> minecraft.setScreen(settingsScreen.apply(null)));
        *///?}
        return Command.SINGLE_SUCCESS;
    }
}
