
package net.pathdos.totemtweaks.mixin;

import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.item.ItemStack;
import net.pathdos.totemtweaks.config.Gui;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderState.ItemActivationRenderState.class)
public abstract class ItemActivationRenderStateMixin implements ItemActivationRenderStateAccessor {

    @Shadow @Final @Mutable
    public int ticks;

    @Override
    public int totemTweaks$getTicks() {
        return this.ticks;
    }

    @Shadow @Final @Mutable
    public float offX;

    @Shadow @Final @Mutable
    public float offY;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void modifyItemActivation(
            ItemStack item,
            int ticks,
            float offX,
            float offY,
            CallbackInfo ci
    ) {
        Gui config = Gui.get();

        if (!config.TotemPopAnimation) {
            this.ticks = 0;
        } else {
            this.ticks = config.animationSpeed;
        }

        if (config.lockRotationPosition) {
            this.offX = 0.0F;
            this.offY = 0.0F;
        }
    }


}
