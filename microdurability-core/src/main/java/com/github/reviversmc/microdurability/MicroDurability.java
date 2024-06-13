package com.github.reviversmc.microdurability;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.reviversmc.microdurability.integration.ClothConfigCompat;
import com.github.reviversmc.microdurability.mccompat.McVersionCompatInvoker;

public class MicroDurability implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("MicroDurability");
	public static ModConfig config;
	public static Renderer renderer;

	@Override
	public void onInitialize() {
		McVersionCompatInvoker.run();

		if (FabricLoader.getInstance().isModLoaded("cloth-config2")) {
			config = ClothConfigCompat.loadConfig();
		} else {
			config = new ModConfig();
		}
	}
}
