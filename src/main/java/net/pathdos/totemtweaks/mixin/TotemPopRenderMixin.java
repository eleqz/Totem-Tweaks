package net.pathdos.totemtweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
//? if >=1.21.6 {
import net.minecraft.client.renderer.ScreenEffectRenderer;
//?} else {
/*import net.minecraft.client.renderer.GameRenderer;
*///?}
import net.pathdos.totemtweaks.TotemTweaks;
import net.pathdos.totemtweaks.config.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
//? if >=1.21.6 {
import org.spongepowered.asm.mixin.injection.ModifyArg;
//?}
import org.spongepowered.asm.mixin.injection.ModifyConstant;

//? if >=1.21.6 {
@Mixin(ScreenEffectRenderer.class)
//?} else {
/*@Mixin(GameRenderer.class)
*///?}
// Stonecutter keeps the other versions in comments, and parameter names differ between them.
@SuppressWarnings({"CommentedOutCode", "NameDoesntMatchTargetClass"})
public abstract class TotemPopRenderMixin {

    @Unique
    private static final String RENDER = "renderItemActivationAnimation";

    //? if >=1.21.6 {
    // Vanilla moves the totem from -10 to -1 along the view axis to grow it, -1 being the largest.
    @Unique
    private static final float CLOSEST_DEPTH = -1.0F;
    //?} else {
    /*// Vanilla scales the totem by 50 + 175 * sin(progress), which peaks at 225.
    @Unique
    private static final float LARGEST_SCALE = 225.0F;
    *///?}

    @ModifyConstant(method = RENDER, constant = @Constant(intValue = 40))
    private int totemtweaks$durationTicks(int vanillaTicks) {
        return TotemTweaks.settings().animationSpeed;
    }

    @ModifyConstant(method = RENDER, constant = @Constant(floatValue = 40.0F))
    private float totemtweaks$durationProgress(float vanillaTicks) {
        return TotemTweaks.settings().animationSpeed;
    }

    @ModifyConstant(method = RENDER, constant = {@Constant(floatValue = 900.0F), @Constant(floatValue = 6.0F)})
    private float totemtweaks$rotation(float degrees) {
        return TotemTweaks.settings().disableRotations ? 0.0F : degrees;
    }

    @WrapOperation(method = RENDER, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
    private void totemtweaks$popSize(PoseStack poseStack, float x, float y, float z, Operation<Void> original) {
        Settings settings = TotemTweaks.settings();
        float size = settings.popSize;
        //? if <1.21.6 {
        /*if (settings.staticSize) {
            x = Math.copySign(LARGEST_SCALE, x);
            y = Math.copySign(LARGEST_SCALE, y);
            z = Math.copySign(LARGEST_SCALE, z);
        }
        *///?}
        original.call(poseStack, x * size, y * size, z * size);
    }

    //? if >=1.21.6 {
    @ModifyArg(method = RENDER, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"), index = 2)
    private float totemtweaks$staticSize(float depth) {
        return TotemTweaks.settings().staticSize ? CLOSEST_DEPTH : depth;
    }
    //?}
}
