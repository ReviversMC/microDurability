package com.github.reviversmc.microdurability;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.reviversmc.microdurability.compat.minecraft.McVersionCompatInvoker;
import com.github.reviversmc.microdurability.compat.mods.ClothConfigCompat;

public class MicroDurability implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("MicroDurability");
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
