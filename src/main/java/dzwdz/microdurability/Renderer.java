package dzwdz.microdurability;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class Renderer extends DrawableHelper implements HudRenderCallback {
    private static final Identifier TEX = new Identifier("microdurability", "textures/gui/icons.png");
    private final MinecraftClient mc;

    private float time = 0;

    public Renderer() {
        mc = MinecraftClient.getInstance();
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float delta) {
        if (mc.player == null) return;

        int scaledWidth = mc.getWindow().getScaledWidth();
        int scaledHeight = mc.getWindow().getScaledHeight();
        time = (time + delta) % (EntryPoint.config.blinkTime * 40f);

        // render the held item warning
        for (ItemStack s : mc.player.getItemsHand()) {
            if (EntryPoint.shouldWarn(s)) {
                mc.getTextureManager().bindTexture(TEX);
                drawTexture(matrixStack, scaledWidth / 2 - 2, scaledHeight / 2 - 18, 0, 0, 3, 11); //todo: this doesn't align with the crosshair at some resolutions
                break;
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
        if (stack != null && stack.isDamaged()) {
            RenderSystem.disableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.disableAlphaTest();
            RenderSystem.disableBlend();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            float f = (float)stack.getDamage();
            float g = (float)stack.getMaxDamage();
            float h = Math.max(0.0F, (g - f) / g);
            int i = Math.round(13.0F - f * 13.0F / g);
            int j = MathHelper.hsvToRgb(h / 3.0F, 1.0F, 1.0F);
            this.renderGuiQuad(bufferBuilder, x, y, 13, 2, 0, 0, 0, 255);
            this.renderGuiQuad(bufferBuilder, x, y, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);
            RenderSystem.enableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableTexture();
            RenderSystem.enableDepthTest();
        }
    }

    private void renderGuiQuad(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        buffer.begin(7, VertexFormats.POSITION_COLOR);
        buffer.vertex((double)(x + 0), (double)(y + 0), 0.0D).color(red, green, blue, alpha).next();
        buffer.vertex((double)(x + 0), (double)(y + height), 0.0D).color(red, green, blue, alpha).next();
        buffer.vertex((double)(x + width), (double)(y + height), 0.0D).color(red, green, blue, alpha).next();
        buffer.vertex((double)(x + width), (double)(y + 0), 0.0D).color(red, green, blue, alpha).next();
        Tessellator.getInstance().draw();
    }
}
