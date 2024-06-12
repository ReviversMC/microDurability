package com.github.reviversmc.microdurability;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class Renderer120 extends Renderer1194 {
	@Override
	protected void drawWarningTexture(Identifier texture, Object context, int x, int y, int u, int v, int width, int height) {
		((DrawContext) context).drawTexture(texture, x, y, u, v, width, height);
	}
}
