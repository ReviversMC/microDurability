package com.github.reviversmc.microdurability;

import java.util.List;

import com.github.reviversmc.microdurability.mccompat.McVersionMixinProvider;

public class MixinProvider1194 extends McVersionMixinProvider {
	@Override
	public List<String> getMixins() {
		if (MicroDurability1194.IS_COMPATIBLE.get()) {
			return List.of("InGameHudMixin117");
		}

		return null;
	}
}
