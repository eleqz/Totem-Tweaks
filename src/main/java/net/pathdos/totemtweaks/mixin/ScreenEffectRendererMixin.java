package net.pathdos.totemtweaks.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.pathdos.totemtweaks.config.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.state.level.PlayerRenderState;


@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

/*    @Shadow
    private int itemActivationTicks;

    @Shadow
    private float itemActivationOffX;

    @Shadow
    private float itemActivationOffY;

    // Works?
    @Inject (method = "displayItemActivation", at = @At("TAIL"))
    public void InjectdisplayItemActivation(ItemStack stack, RandomSource random, CallbackInfo ci) {
        if (!Gui.get().TotemPopAnimation) {
            this.itemActivationTicks = 0;
        } else {
            this.itemActivationTicks = Gui.get().animationSpeed;
        }
        if (Gui.get().lockRotationPosition) {
            this.itemActivationOffX = 0;
            this.itemActivationOffY = 0;
        }
    }*/

    @ModifyVariable(method = "renderItemActivationAnimation", at = @At("STORE"), name = "tick")
    private int modifyTickRenderFloatingItem(int tick, @Local(argsOnly = true) PlayerRenderState state) {

        return Gui.get().animationSpeed - tick;
    }

    @ModifyVariable(method = "renderItemActivationAnimation", at = @At("STORE"), name = "scale")
    private float modifyFloatRenderfloatingItem(float f) {
        return f * 40 / Gui.get().animationSpeed;
    }

    @ModifyArgs(method = "renderItemActivationAnimation", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private void modifyTranslateArgs(Args args) {
        float zpos = -1F;
        if (Gui.get().staticSize) {
            args.set(2, zpos);
        }
    }

    @ModifyArgs(method = "renderItemActivationAnimation", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
    private void modifyScaleArgs(Args args) {
        float scale = 0.8F;

        scale *= Gui.get().popSize;

        args.set(0, scale);
        args.set(1, scale);
        args.set(2, scale);
    }

    @WrapWithCondition(method = "renderItemActivationAnimation", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;rotateDegrees(Lcom/mojang/math/Axis;F)V", ordinal = 0))
    private boolean wrapRotationY(PoseStack instance, Axis axis, float angle) {
        return !Gui.get().disableRotations;
    }

    @WrapWithCondition(method = "renderItemActivationAnimation", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;rotateDegrees(Lcom/mojang/math/Axis;F)V", ordinal = 1))
    private boolean wrapRotationX(PoseStack instance, Axis axis, float angle) {
        return !Gui.get().disableRotations;
    }

    @WrapWithCondition(method = "renderItemActivationAnimation", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;rotateDegrees(Lcom/mojang/math/Axis;F)V", ordinal = 2))
    private boolean wrapRotationZ(PoseStack instance, Axis axis, float angle) {
        return !Gui.get().disableRotations;
    }
}