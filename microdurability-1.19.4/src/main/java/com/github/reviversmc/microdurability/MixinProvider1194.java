package com.github.reviversmc.microdurability;

import java.util.List;

import com.github.reviversmc.microdurability.compat.minecraft.McVersionMixinProvider;

public class MixinProvider1194 extends McVersionMixinProvider {
	@Override
	public List<String> getMixins() {
		if (MicroDurability1194.IS_COMPATIBLE.get()) {
			return List.of("InGameHudMixin116");
		}

		return null;
	}
}
