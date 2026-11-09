package net.pathdos.totemtweaks.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
//? if >=26.3 {
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
//?} else {
/*import net.minecraft.client.renderer.ItemInHandRenderer;
*///?}
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.pathdos.totemtweaks.TotemTweaks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=26.3 {
@Mixin(FirstPersonHandsAndItemsRenderer.class)
//?} else {
/*@Mixin(ItemInHandRenderer.class)
*///?}
// Stonecutter keeps the other versions in comments, and their names differ, so members are matched by type or position.
@SuppressWarnings({"CommentedOutCode", "LocalMayUseName", "ModifyVariableMayUseName", "NameDoesntMatchTargetClass"})
public abstract class HeldTotemMixin {

    //? if >=26.2 {
    @Unique
    private static final String RENDER_ARM = "submitArmWithItem";
    //?} else {
    /*@Unique
    private static final String RENDER_ARM = "renderArmWithItem";
    *///?}

    //? if <26.3 {
    /*@Unique
    private boolean totemtweaks$renderingArm;
    *///?}

    // The fourth float argument is how far the item is lowered by the equip animation, 0 being fully raised.
    @ModifyVariable(method = RENDER_ARM, at = @At("HEAD"), argsOnly = true, ordinal = 3)
    private float totemtweaks$skipEquipAnimation(float lowered, @Local(argsOnly = true) InteractionHand hand) {
        LocalPlayer player = Minecraft.getInstance().player;
        boolean holdsTotem = player != null && player.getItemInHand(hand).is(Items.TOTEM_OF_UNDYING);
        return holdsTotem && TotemTweaks.settings().disableEquipAnimation ? 0.0F : lowered;
    }

    //? if >=26.3 {
    @Inject(method = RENDER_ARM, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"))
    private void totemtweaks$scaleTotem(CallbackInfo ci, @Local(argsOnly = true) ItemStack item, @Local(argsOnly = true) PoseStack poseStack) {
        totemtweaks$scale(item, poseStack);
    }
    //?} else {
    /*// renderItem changes its descriptor three times in this range, so it is entered by name rather than targeted as a call.
    // Before 1.21.2 third person layers call it as well, hence the flag.
    @Inject(method = RENDER_ARM, at = @At("HEAD"))
    private void totemtweaks$enterArm(CallbackInfo ci) {
        totemtweaks$renderingArm = true;
    }

    @Inject(method = RENDER_ARM, at = @At("RETURN"))
    private void totemtweaks$leaveArm(CallbackInfo ci) {
        totemtweaks$renderingArm = false;
    }

    @Inject(method = "renderItem", at = @At("HEAD"))
    private void totemtweaks$scaleTotem(CallbackInfo ci, @Local(argsOnly = true) ItemStack item, @Local(argsOnly = true) PoseStack poseStack) {
        if (totemtweaks$renderingArm) {
            totemtweaks$scale(item, poseStack);
        }
    }
    *///?}

    @Unique
    private static void totemtweaks$scale(ItemStack item, PoseStack poseStack) {
        if (item.is(Items.TOTEM_OF_UNDYING)) {
            float size = TotemTweaks.settings().heldTotemSize();
            poseStack.scale(size, size, size);
        }
    }
}
