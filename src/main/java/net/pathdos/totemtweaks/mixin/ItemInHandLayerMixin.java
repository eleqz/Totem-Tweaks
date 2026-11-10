package net.pathdos.totemtweaks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.pathdos.totemtweaks.config.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin {

    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"))
    private void submitArmWithItem(ArmedEntityRenderState state, ItemStackRenderState item, ItemStack itemStack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (Gui.get().enableTotemSizeChange && itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
            float sizeRange = Gui.get().maxTotemSize - Gui.get().minTotemSize;
            float size = (float) (Math.sin(System.currentTimeMillis() / 1000.0 * Gui.get().totemSizeChangeSpeed) / 2 + 0.5) * sizeRange + Gui.get().minTotemSize;
            poseStack.scale(size, size, size);
        } else if (itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
            poseStack.scale(Gui.get().totemSize, Gui.get().totemSize, Gui.get().totemSize);
        }
    }
}