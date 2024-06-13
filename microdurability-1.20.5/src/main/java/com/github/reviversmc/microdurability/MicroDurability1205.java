package com.github.reviversmc.microdurability;

import java.util.function.Supplier;

import com.github.reviversmc.microdurability.mccompat.McVersionCompatInitializer;
import com.github.reviversmc.microdurability.mccompat.McVersionHelper;

public class MicroDurability1205 extends McVersionCompatInitializer {
	static final Supplier<Boolean> IS_COMPATIBLE = () -> McVersionHelper.isWithin("1.20.5", McVersionHelper.INFINITY);

	@Override
	public boolean isCompatible() {
		return IS_COMPATIBLE.get();
	}

	@Override
	protected void initialize() {
		MicroDurability.renderer = new Renderer1205();
	}
}
