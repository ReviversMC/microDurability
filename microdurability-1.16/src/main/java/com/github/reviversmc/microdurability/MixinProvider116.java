package com.github.reviversmc.microdurability;

import java.util.Collections;
import java.util.List;

import com.github.reviversmc.microdurability.compat.minecraft.McVersionMixinProvider;

public class MixinProvider116 extends McVersionMixinProvider {
	@Override
	public List<String> getMixins() {
		if (MicroDurability116.IS_COMPATIBLE.get()) {
			return Collections.singletonList("InGameHudMixin116");
		}

		return null;
	}
}
