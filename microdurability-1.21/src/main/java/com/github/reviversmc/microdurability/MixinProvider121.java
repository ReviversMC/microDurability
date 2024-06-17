package com.github.reviversmc.microdurability;

import java.util.List;

import com.github.reviversmc.microdurability.mccompat.McVersionMixinProvider;

public class MixinProvider121 extends McVersionMixinProvider {
	@Override
	public List<String> getMixins() {
		if (MicroDurability121.IS_COMPATIBLE.get()) {
			return List.of("InGameHudMixin121");
		}

		return null;
	}
}
