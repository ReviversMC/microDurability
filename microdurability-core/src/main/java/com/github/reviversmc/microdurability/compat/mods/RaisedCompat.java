package com.github.reviversmc.microdurability.compat.mods;

import net.fabricmc.loader.api.FabricLoader;

import com.github.reviversmc.microdurability.MicroDurability;

public class RaisedCompat {
	private static String raisedHotbarOffsetObjectShareId;
	private static boolean error = false;

	public static int getHotbarOffset() {
		if (error) {
			return 0;
		} else if (raisedHotbarOffsetObjectShareId == null) {
			init();
			return getHotbarOffset();
		}

		return (int) FabricLoader.getInstance().getObjectShare().get(raisedHotbarOffsetObjectShareId);
	}

	private static void init() {
		String oldId = "raised:distance";
		String newId = "raised:hud";

		if (FabricLoader.getInstance().getObjectShare().get(oldId) instanceof Integer) {
			raisedHotbarOffsetObjectShareId = oldId;
		} else if (FabricLoader.getInstance().getObjectShare().get(newId) instanceof Integer) {
			raisedHotbarOffsetObjectShareId = newId;
		} else {
			MicroDurability.LOGGER.error("Failed to find the \"raised\" mod's hotbar offset value in Fabric Loader's object share. Compatibility will be disabled.");
			error = true;
		}
	}
}
