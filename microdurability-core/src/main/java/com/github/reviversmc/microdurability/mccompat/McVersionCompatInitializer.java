package com.github.reviversmc.microdurability.mccompat;

import net.fabricmc.api.ModInitializer;

public abstract class McVersionCompatInitializer implements ModInitializer {
	@Override
	public final void onInitialize() {
		throw new IllegalStateException("This method should not be called");
	}

	public abstract boolean isCompatible();
	protected abstract void initialize();
}
