package net.pathdos.totemtweaks.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class SimulatePopCommand {

    private SimulatePopCommand() {
    }

    public static <S> LiteralArgumentBuilder<S> create() {
        return LiteralArgumentBuilder.<S>literal("simulatePop").executes(ignored -> run());
    }

    private static int run() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return 0;
        }
        ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);
        //? if >=26.3 {
        minecraft.player.displayItemActivation(totem);
        //?} else {
        /*minecraft.gameRenderer.displayItemActivation(totem);
        *///?}
        return Command.SINGLE_SUCCESS;
    }
}
