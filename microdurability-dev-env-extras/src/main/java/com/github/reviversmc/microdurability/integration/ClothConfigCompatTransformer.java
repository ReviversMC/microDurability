package com.github.reviversmc.microdurability.integration;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;

import com.github.reviversmc.microdurability.ModConfig;

/**
 * Removes the {@code ConfigData} interface from the {@link ModConfig} class in case Cloth Config isn't loaded,
 * to prevent a ClassNotFoundException.
 */
public class ClothConfigCompatTransformer implements Runnable {
	@Override
	public void run() {
		if (FabricLoader.getInstance().isModLoaded("cloth-config2")) return;

		ClassTinkerers.addTransformation("com.github.reviversmc.microdurability.ModConfig", classNode -> {
			classNode.interfaces.remove("me/shedaniel/autoconfig/ConfigData");
		});
	}
}
