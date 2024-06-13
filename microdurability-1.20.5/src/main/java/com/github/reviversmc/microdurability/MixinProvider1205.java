package com.github.reviversmc.microdurability;

import java.util.List;

import com.github.reviversmc.microdurability.mccompat.McVersionMixinProvider;

public class MixinProvider1205 extends McVersionMixinProvider {
	@Override
	public List<String> getMixins() {
		if (MicroDurability1205.IS_COMPATIBLE.get()) {
			return List.of("InGameHudMixin1205");
		}

		return null;
	}
}
