package com.github.reviversmc.microdurability;

import java.util.List;

import com.github.reviversmc.microdurability.mccompat.McVersionMixinProvider;

public class MixinProvider120 extends McVersionMixinProvider {
	@Override
	public List<String> getMixins() {
		if (MicroDurability120.IS_COMPATIBLE.get()) {
			return List.of("InGameHudMixin120");
		}

		return null;
	}
}
