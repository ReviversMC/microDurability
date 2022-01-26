package dzwdz.microdurability;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class Renderer extends DrawableHelper implements HudRenderCallback {
    private static final Identifier TEX = new Identifier("microdurability", "textures/gui/icons.png");
    private final MinecraftClient mc;

    private float time = 0;

    public Renderer() {
        mc = MinecraftClient.getInstance();
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float delta) {
        int scaledWidth = mc.getWindow().getScaledWidth();
        int scaledHeight = mc.getWindow().getScaledHeight();
        time = (time + delta) % (EntryPoint.config.blinkTime * 40f);

        // render the held item warning
        if (EntryPoint.config.toolWarning) {
            for (ItemStack s : mc.player.getItemsHand()) {
                if (EntryPoint.shouldWarn(s)) {
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, TEX);
                    drawTexture(matrixStack, scaledWidth / 2 - 2, scaledHeight / 2 - 18, 0, 0, 3, 11); //todo: this doesn't align with the crosshair at some resolutions
                    break;
                }
            }
        }

        // render the armor durability
        int x = scaledWidth/2 - 7;
        int y = scaledHeight - 30;
        if (mc.player.experienceLevel > 0) y -= 6;
        if (EntryPoint.config.blinkTime > 0)
            for (ItemStack s : mc.player.getArmorItems())
                if (time < EntryPoint.config.blinkTime * 20f && EntryPoint.shouldWarn(s)) return;

        for (ItemStack s : mc.player.getArmorItems())
            renderBar(s, x, y -= 3);
    }

    public void renderBar(ItemStack stack, int x, int y) {
        if (stack == null || stack.isEmpty()) return;
        if (!EntryPoint.config.undamagedBars && !stack.isItemBarVisible()) return;
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

    private void renderGuiQuad(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(x,         y,          0.0D).color(red, green, blue, alpha).next();
        buffer.vertex(x,         y + height, 0.0D).color(red, green, blue, alpha).next();
        buffer.vertex(x + width, y + height, 0.0D).color(red, green, blue, alpha).next();
        buffer.vertex(x + width, y,          0.0D).color(red, green, blue, alpha).next();
        buffer.end();
        BufferRenderer.draw(buffer);
    }
}
