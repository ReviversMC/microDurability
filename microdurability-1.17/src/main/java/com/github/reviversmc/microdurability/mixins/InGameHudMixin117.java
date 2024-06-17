package com.github.reviversmc.microdurability.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

import com.github.reviversmc.microdurability.MicroDurability;

@Mixin(InGameHud.class)
public class InGameHudMixin117 {
	@Shadow
	private int ticks;

	@Inject(method = "renderHotbar", at = @At("HEAD"))
	private void renderArmorArea(float delta, MatrixStack matrices, CallbackInfo callbackInfo) {
		MicroDurability.renderer.renderArmorArea(matrices, ticks);
	}

	@Inject(method = "renderCrosshair", at = @At("HEAD"))
	private void renderHeldItemExclamationMark(MatrixStack matrices, CallbackInfo callbackInfo) {
		MicroDurability.renderer.renderHeldItemLowDurabilityWarning(matrices, ticks);
	}
}
