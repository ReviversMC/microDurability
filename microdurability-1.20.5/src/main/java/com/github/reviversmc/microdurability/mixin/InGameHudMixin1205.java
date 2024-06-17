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
public class InGameHudMixin1205 {
	@Shadow
	private int ticks;

	@Inject(method = "renderHotbar", at = @At("RETURN"))
	private void renderArmorArea(DrawContext context, float delta, CallbackInfo callbackInfo) {
		MicroDurability.renderer.renderArmorArea(context, ticks);
	}

	@Inject(method = "renderCrosshair", at = @At("RETURN"))
	private void renderHeldItemExclamationMark(DrawContext context, float delta, CallbackInfo callbackInfo) {
		MicroDurability.renderer.renderHeldItemLowDurabilityWarning(context, ticks);
	}
}
