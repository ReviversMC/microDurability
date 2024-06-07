package com.github.reviversmc.microdurability.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.reviversmc.microdurability.MicroDurability;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Inject(
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusBars(Lnet/minecraft/client/gui/DrawContext;)V"
			),
			method = "renderMainHud"
	)
	public void renderMicroDurability(DrawContext context, float delta, CallbackInfo callbackInfo) {
		MicroDurability.renderer.onHudRender(context, delta);
	}
}
