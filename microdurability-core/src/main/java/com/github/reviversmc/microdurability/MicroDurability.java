package com.github.reviversmc.microdurability;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.reviversmc.microdurability.mccompat.McVersionCompatInvoker;

public class MicroDurability implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("MicroDurability");
	public static ModConfig config;
	public static Renderer renderer;

	@Override
	public void onInitialize() {
		McVersionCompatInvoker.run();

		AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
	}
}
