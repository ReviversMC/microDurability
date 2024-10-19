package com.github.reviversmc.microdurability.compat.mods;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Supplier;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;

import com.github.reviversmc.microdurability.MicroDurability;

public class RaisedCompat {
	private static final boolean isInstalled;
	private static final Version installedVersion;
	private static final Version v_3_0_0;
	private static final Version v_4_0_0;
	private static boolean fatalError = false;
	private static Supplier<Integer> hotbarOffsetSupplier;

	static {
		ModContainer mod = FabricLoader.getInstance().getModContainer("raised").orElse(null);

		isInstalled = mod != null;
		installedVersion = isInstalled ? mod.getMetadata().getVersion() : null;

		try {
			v_3_0_0 = Version.parse("3.0.0");
			v_4_0_0 = Version.parse("4.0.0");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isInstalled() {
		return isInstalled;
	}

	public static boolean isV3OrLater() {
		assert isInstalled;
		return installedVersion.compareTo(v_3_0_0) >= 0;
	}

	public static boolean isV4OrLater() {
		assert isInstalled;
		return installedVersion.compareTo(v_4_0_0) >= 0;
	}

	public static int getHotbarOffset() {
		assert isInstalled;

		if (fatalError) {
			return 0;
		} else if (hotbarOffsetSupplier == null) {
			init();
			return getHotbarOffset();
		}

		return hotbarOffsetSupplier.get();
	}

	private static void init() {
		if (isV4OrLater()) {
			try {
				Class<?> RaisedApi = Class.forName("dev.yurisuika.raised.api.RaisedApi");
				Class<?> Position = Class.forName("dev.yurisuika.raised.util.properties.Position");
				Class<?> Element = Class.forName("dev.yurisuika.raised.util.properties.Element");
				Method RaisedApi_getPosition = RaisedApi.getDeclaredMethod("getPosition", Element);
				Method RaisedApi_getY = RaisedApi.getDeclaredMethod("getY", Element);
				Method Position_getY = Position.getDeclaredMethod("getY");
				Field HOTBAR = Element.getDeclaredField("HOTBAR");

				hotbarOffsetSupplier = () -> {
					try {
						Object hotbar = HOTBAR.get(null);
						Object position = RaisedApi_getPosition.invoke(null, hotbar);

						int positionY = (int) Position_getY.invoke(position);
						int elementY = (int) RaisedApi_getY.invoke(null, hotbar) * positionY;

						return elementY;
					} catch (Throwable e) {
						MicroDurability.LOGGER.error("Failed to get the hotbar offset from the Raised API. Compatibility will be disabled.");
						fatalError = true;
						return 0;
					}
				};
			} catch (Throwable e) {
				MicroDurability.LOGGER.error("Failed to find the Raised API classes. Compatibility will be disabled.");
				fatalError = true;
			}
		} else {
			String newObjectShareId = "raised:hud"; // >=1.2.0
			String oldObjectShareId = "raised:distance"; // <3.0.0 \ {1.2.0, 1.2.1, 1.2.2, 1.2.3}
			String id;

			if (FabricLoader.getInstance().getObjectShare().get(newObjectShareId) instanceof Integer) {
				id = newObjectShareId;
			} else if (FabricLoader.getInstance().getObjectShare().get(oldObjectShareId) instanceof Integer) {
				id = oldObjectShareId;
			} else {
				MicroDurability.LOGGER.error("Failed to find the \"raised\" mod's hotbar offset value in Fabric Loader's object share. Compatibility will be disabled.");
				fatalError = true;
				return;
			}

			hotbarOffsetSupplier = () -> -1 * (int) FabricLoader.getInstance().getObjectShare().get(id);
		}
	}
}
