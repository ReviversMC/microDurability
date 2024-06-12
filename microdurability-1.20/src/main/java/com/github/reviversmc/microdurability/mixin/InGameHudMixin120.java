package com.github.reviversmc.microdurability.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;

import com.github.reviversmc.microdurability.MicroDurability120;

@Mixin(InGameHud.class)
public class InGameHudMixin120 {
	@Shadow
	private int ticks;

	@Inject(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusBars(Lnet/minecraft/client/gui/DrawContext;)V"
			),
			method = "render"
	)
	private void renderArmorArea(DrawContext context, float delta, CallbackInfo callbackInfo) {
		MicroDurability120.renderer.renderArmorArea(context, ticks, delta);
	}

	@Inject(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;renderCrosshair(Lnet/minecraft/client/gui/DrawContext;)V"
			),
			method = "render"
	)
	private void renderHeldItemExclamationMark(DrawContext context, float delta, CallbackInfo callbackInfo) {
		MicroDurability120.renderer.renderHeldItemLowDurabilityWarning(context, ticks, delta);
	}
}
