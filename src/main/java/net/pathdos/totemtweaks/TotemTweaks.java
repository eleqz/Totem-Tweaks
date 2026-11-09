package net.pathdos.totemtweaks;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.terraformersmc.modmenu.ModMenu;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.pathdos.totemtweaks.config.Gui;

public class TotemTweaks implements ModInitializer {

	@Override
	public void onInitialize() {
		AutoConfig.register(Gui.class, GsonConfigSerializer::new);
		registerCommands();
	}

	private void registerCommands() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(ClientCommands.literal("totemTweaks")
					.executes(context -> openConfigScreen(context))
			);

			dispatcher.register(ClientCommands.literal("simulatePop")
					.executes(context -> {
						simulatePop();
						return Command.SINGLE_SUCCESS;
					})
			);
		});
	}

	private int openConfigScreen(CommandContext<?> context) {
		Minecraft client = Minecraft.getInstance();
		client.schedule(() -> {
			client.setScreenAndShow(ModMenu.getConfigScreen("totemtweaks", client.gui.screen()));
		});
		return Command.SINGLE_SUCCESS;
	}

	public static void simulatePop() {
		if (Minecraft.getInstance().player != null) {
			Minecraft.getInstance().player.displayItemActivation(new ItemStack(Items.TOTEM_OF_UNDYING));
		}
	}
}

