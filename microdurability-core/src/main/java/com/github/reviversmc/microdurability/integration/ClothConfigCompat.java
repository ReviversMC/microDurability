package com.github.reviversmc.microdurability.integration;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

import net.minecraft.client.gui.screen.Screen;

import com.github.reviversmc.microdurability.ModConfig;

public class ClothConfigCompat {
	public static ModConfig loadConfig() {
		return AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new).getConfig();
	}

	public static Screen getScreen(Screen parent) {
		return AutoConfig.getConfigScreen(ModConfig.class, parent).get();
	}
}
