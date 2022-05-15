package com.github.reviversmc.microdurability;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public abstract class RendererBase extends DrawableHelper implements HudRenderCallback {
    private static final Identifier TEX = new Identifier("microdurability", "textures/gui/icons.png");
    private final MinecraftClient mc;

    private float time = 0;

    public RendererBase() {
        mc = MinecraftClient.getInstance();
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float delta) {
        int scaledWidth = mc.getWindow().getScaledWidth();
        int scaledHeight = mc.getWindow().getScaledHeight();
        time = (time + delta) % (MicroDurability.config.blinkTime * 40f);

        // render the held item warning
        if (MicroDurability.config.toolWarning) {
            for (ItemStack s : mc.player.getItemsHand()) {
                if (MicroDurability.shouldWarn(s)) {
                    if (MicroDurability.config.blinkTime > 0
                            && time < MicroDurability.config.blinkTime * 10f) {
                        break;
                    }
                    RenderSystem.setShaderTexture(0, TEX);
                    drawTexture(matrixStack, scaledWidth / 2 - 2, scaledHeight / 2 - 18, 0, 0, 3, 11); //todo: this doesn't align with the crosshair at some resolutions
                    RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
                    break;
                }
            }
        }

        // render the armor durability
        int x = scaledWidth/2 - 7;
        int y = scaledHeight - 30;
        if (mc.player.experienceLevel > 0) y -= 6;
        if (MicroDurability.config.blinkTime > 0)
            for (ItemStack s : mc.player.getArmorItems())
                if (time < MicroDurability.config.blinkTime * 20f && MicroDurability.shouldWarn(s)) return;

        for (ItemStack s : mc.player.getArmorItems())
            renderBar(s, x, y -= 3);
    }

    public void renderBar(ItemStack stack, int x, int y) {
        if (stack == null || stack.isEmpty()) return;
        if (!MicroDurability.config.undamagedBars && !stack.isItemBarVisible()) return;
        if (!stack.isDamageable()) return;

        RenderSystem.disableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.disableBlend();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        int width = stack.getItemBarStep();
        int color = stack.getItemBarColor();
        this.renderGuiQuad(bufferBuilder, x, y, 13, 2, 0, 0, 0, 255);
        this.renderGuiQuad(bufferBuilder, x, y, width, 1, color >> 16 & 255, color >> 8 & 255, color & 255, 255);
        RenderSystem.enableBlend();
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
    }

    protected abstract void renderGuiQuad(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha);
}
