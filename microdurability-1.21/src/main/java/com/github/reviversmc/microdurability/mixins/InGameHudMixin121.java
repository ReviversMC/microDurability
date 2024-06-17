package com.github.reviversmc.microdurability.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;

import com.github.reviversmc.microdurability.MicroDurability;

@Mixin(InGameHud.class)
public class InGameHudMixin121 {
	@Shadow
	private int ticks;

	@Inject(method = "renderHotbar", at = @At("RETURN"))
	private void renderArmorArea(DrawContext context, RenderTickCounter tickCounter, CallbackInfo callbackInfo) {
		MicroDurability.renderer.renderArmorArea(context, ticks);
	}

	@Inject(method = "renderCrosshair", at = @At("RETURN"))
	private void renderHeldItemExclamationMark(DrawContext context, RenderTickCounter tickCounter, CallbackInfo callbackInfo) {
		MicroDurability.renderer.renderHeldItemLowDurabilityWarning(context, ticks);
	}
}
