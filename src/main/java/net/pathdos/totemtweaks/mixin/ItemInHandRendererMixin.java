package net.pathdos.totemtweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.pathdos.totemtweaks.config.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/*
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V"))
    private void submitArmWithItem(AbstractClientPlayer player, float tickProgress, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress, PoseStack matrices, SubmitNodeCollector orderedRenderCommandQueue, int light, CallbackInfo info) {
        if (Gui.get().enableTotemSizeChange && item.getItem() == Items.TOTEM_OF_UNDYING) {
            float sizeRange = Gui.get().maxTotemSize - Gui.get().minTotemSize;
            float size = (float) (Math.sin(System.currentTimeMillis() / 1000.0 * Gui.get().totemSizeChangeSpeed) / 2 + 0.5) * sizeRange + Gui.get().minTotemSize;
            matrices.scale(size, size, size);
        } else if (hand == InteractionHand.MAIN_HAND && item.getItem() == Items.TOTEM_OF_UNDYING) {
            matrices.scale(Gui.get().totemSize, Gui.get().totemSize, Gui.get().totemSize);
        } else if (hand == InteractionHand.OFF_HAND && item.getItem() == Items.TOTEM_OF_UNDYING) {
            matrices.scale(Gui.get().totemSize, Gui.get().totemSize, Gui.get().totemSize);
        }
    }

    @ModifyArgs(method = "applyItemArmTransform", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private void modifyItemArmTransform(Args args, PoseStack matrices, HumanoidArm arm, float equipProgress) {
        Minecraft client = Minecraft.getInstance();
        ItemStack itemStack = arm == client.player.getMainArm() ? client.player.getMainHandItem() : client.player.getOffhandItem();
        if (Gui.get().disableEquipAnimation && itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
            args.set(0, (arm == HumanoidArm.RIGHT ? 0.56F : -0.56F));
            args.set(1, -0.52F);
            args.set(2, -0.72F);
        }
    }
}*/
