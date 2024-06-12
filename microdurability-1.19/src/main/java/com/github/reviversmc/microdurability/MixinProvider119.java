package com.github.reviversmc.microdurability;

import java.util.List;

import com.github.reviversmc.microdurability.mccompat.McVersionMixinProvider;

public class MixinProvider119 extends McVersionMixinProvider {
	@Override
	public List<String> getMixins() {
		if (MicroDurability119.IS_COMPATIBLE.get()) {
			return List.of("InGameHudMixin117");
		}

		return null;
	}
}
