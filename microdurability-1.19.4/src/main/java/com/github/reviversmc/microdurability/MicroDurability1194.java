package com.github.reviversmc.microdurability;

import net.fabricmc.api.ModInitializer;

public class MicroDurability1194 implements ModInitializer {
	public static RendererBase renderer;

	@Override
	public void onInitialize() {
		renderer = new Renderer1194();
	}
}
