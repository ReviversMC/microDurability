package com.github.reviversmc.microdurability;

import java.util.function.Supplier;

import com.github.reviversmc.microdurability.mccompat.McVersionCompatInitializer;
import com.github.reviversmc.microdurability.mccompat.McVersionHelper;

public class MicroDurability117 extends McVersionCompatInitializer {
	static final Supplier<Boolean> IS_COMPATIBLE = () -> McVersionHelper.isWithin("1.17", "1.18.2");

	@Override
	public boolean isCompatible() {
		return IS_COMPATIBLE.get();
	}

	@Override
	public void initialize() {
		Renderer117 renderer = new Renderer117();
		renderer.init();
		MicroDurability.renderer = renderer;
	}
}
