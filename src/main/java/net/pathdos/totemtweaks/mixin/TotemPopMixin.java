package net.pathdos.totemtweaks.mixin;

//? if >=26.3 {
import net.minecraft.client.player.ItemActivation;
//?} else if >=1.21.6 {
/*import net.minecraft.client.renderer.ScreenEffectRenderer;
*///?} else {
/*import net.minecraft.client.renderer.GameRenderer;
*///?}
import net.pathdos.totemtweaks.TotemTweaks;
import net.pathdos.totemtweaks.config.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=26.3 {
@Mixin(ItemActivation.class)
//?} else if >=1.21.6 {
/*@Mixin(ScreenEffectRenderer.class)
*///?} else {
/*@Mixin(GameRenderer.class)
*///?}
// Stonecutter keeps the other versions in comments.
@SuppressWarnings({"CommentedOutCode"})
public abstract class TotemPopMixin {

    //? if >=26.3 {
    @Unique
    private static final String ACTIVATE = "activate";

    @Shadow
    private int ticks;

    @Shadow
    private float offX;

    @Shadow
    private float offY;
    //?} else {
    /*@Unique
    private static final String ACTIVATE = "displayItemActivation";

    @Shadow
    private int itemActivationTicks;

    @Shadow
    private float itemActivationOffX;

    @Shadow
    private float itemActivationOffY;
    *///?}

    @Inject(method = ACTIVATE, at = @At("TAIL"))
    private void totemtweaks$applySettings(CallbackInfo ci) {
        Settings settings = TotemTweaks.settings();
        //? if >=26.3 {
        ticks = settings.popAnimation ? settings.animationSpeed : 0;
        if (settings.lockRotationPosition) {
            offX = 0.0F;
            offY = 0.0F;
        }
        //?} else {
        /*itemActivationTicks = settings.popAnimation ? settings.animationSpeed : 0;
        if (settings.lockRotationPosition) {
            itemActivationOffX = 0.0F;
            itemActivationOffY = 0.0F;
        }
        *///?}
    }
}
