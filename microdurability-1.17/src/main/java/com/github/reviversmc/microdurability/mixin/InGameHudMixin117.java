package com.github.reviversmc.microdurability.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

import com.github.reviversmc.microdurability.MicroDurability117;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin117 {
    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusBars(Lnet/minecraft/client/util/math/MatrixStack;)V"
            ),
            method = "render"
    )
    public void renderMicroDurability(MatrixStack matrices, float delta, CallbackInfo callbackInfo) {
        MicroDurability117.renderer.onHudRender(matrices, delta);
    }
}
