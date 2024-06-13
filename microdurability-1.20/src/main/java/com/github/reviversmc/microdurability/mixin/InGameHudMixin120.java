package com.github.reviversmc.microdurability.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;

import com.github.reviversmc.microdurability.MicroDurability;

@Mixin(InGameHud.class)
public class InGameHudMixin120 {
	@Shadow
	private int ticks;

	@Inject(method = "renderHotbar", at = @At("HEAD"))
	private void renderArmorArea(float delta, DrawContext context, CallbackInfo callbackInfo) {
		MicroDurability.renderer.renderArmorArea(context, ticks);
	}

	@Inject(method = "renderCrosshair", at = @At("HEAD"))
	private void renderHeldItemExclamationMark(DrawContext context, CallbackInfo callbackInfo) {
		MicroDurability.renderer.renderHeldItemLowDurabilityWarning(context, ticks);
	}
}
