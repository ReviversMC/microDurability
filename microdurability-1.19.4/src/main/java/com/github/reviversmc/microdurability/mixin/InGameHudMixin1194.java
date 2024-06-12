package com.github.reviversmc.microdurability.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

import com.github.reviversmc.microdurability.MicroDurability1194;

@Mixin(InGameHud.class)
public class InGameHudMixin1194 {
	@Shadow
	private int ticks;

	@Inject(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusBars(Lnet/minecraft/client/util/math/MatrixStack;)V"
			),
			method = "render"
	)
	private void renderArmorArea(MatrixStack matrices, float delta, CallbackInfo callbackInfo) {
		MicroDurability1194.renderer.renderArmorArea(matrices, ticks, delta);
	}

	@Inject(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;renderCrosshair(Lnet/minecraft/client/util/math/MatrixStack;)V"
			),
			method = "render"
	)
	private void renderHeldItemExclamationMark(MatrixStack matrices, float delta, CallbackInfo callbackInfo) {
		MicroDurability1194.renderer.renderHeldItemLowDurabilityWarning(matrices, ticks, delta);
	}
}
