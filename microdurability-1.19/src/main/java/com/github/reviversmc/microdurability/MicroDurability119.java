package com.github.reviversmc.microdurability;

import net.fabricmc.api.ModInitializer;

public class MicroDurability119 implements ModInitializer {
	public static RendererBase renderer;

	@Override
	public void onInitialize() {
		renderer = new Renderer119();
	}
}
