package com.github.reviversmc.microdurability.compat.mods;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;

import com.github.reviversmc.microdurability.MicroDurability;

public class DoubleHotbarCompat {
	private static final boolean isInstalled;
	private static final Version installedVersion;
	private static final Version v_1_1_0;
	private static final Version v_1_2_0;
	private static final Version v_1_3_1;
	private static final Map<String, Boolean> configFieldErrored = new LinkedHashMap<>();
	private static boolean fatalError = false;
	private static Supplier<Integer> shiftSupplier;

	static {
		ModContainer mod = FabricLoader.getInstance().getModContainer("double_hotbar").orElse(null);

		isInstalled = mod != null;
		installedVersion = isInstalled ? mod.getMetadata().getVersion() : null;

		try {
			v_1_1_0 = Version.parse("1.1.0");
			v_1_2_0 = Version.parse("1.2.0");
			v_1_3_1 = Version.parse("1.3.0");
		} catch (VersionParsingException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isInstalled() {
		return isInstalled;
	}

	public static int getHotbarHeight() {
		assert isInstalled;

		if (fatalError) {
			return 0;
		} else if (shiftSupplier == null) {
			init();
			return getHotbarHeight();
		}

		return shiftSupplier.get();
	}

	private static void init() {
		try {
			Class<?> config = Class.forName("com.sidezbros.double_hotbar.DHModConfig");
			Object configInstance = config.getDeclaredField("INSTANCE").get(null);

			Field secondBar = null;
			Field shift = null;
			Field disabled = null;

			if (installedVersion.compareTo(v_1_1_0) >= 0) {
				try {
					secondBar = config.getDeclaredField("displayDoubleHotbar");
					secondBar.setAccessible(true);
				} catch (Throwable e) {
					configFieldError("displayDoubleHotbar");
				}
			}

			if (installedVersion.compareTo(v_1_2_0) >= 0) {
				try {
					shift = config.getDeclaredField("shift");
					shift.setAccessible(true);
				} catch (Throwable e) {
					configFieldError("shift");
				}
			}

			if (installedVersion.compareTo(v_1_3_1) >= 0) {
				try {
					disabled = config.getDeclaredField("disableMod");
					disabled.setAccessible(true);
				} catch (Throwable e) {
					configFieldError("disableMod");
				}
			}

			final Field finalSecondBar = secondBar;
			final Field finalShift = shift;
			final Field finalDisabled = disabled;

			shiftSupplier = () -> {
				int height = 0;

				try {
					if (finalSecondBar != null && !finalSecondBar.getBoolean(configInstance)) {
						return height;
					}
				} catch (Throwable e) {
					configFieldError("displayDoubleHotbar");
				}

				try {
					if (finalDisabled != null && finalDisabled.getBoolean(configInstance)) {
						return height;
					}
				} catch (Throwable e) {
					configFieldError("disableMod");
				}

				try {
					height += finalShift == null ? 21 : finalShift.getInt(configInstance);
				} catch (Throwable e) {
					configFieldError("shift");
				}

				return height;
			};
		} catch (Throwable e) {
			error("Failed to read the \"double_hotbar\" mod's config. Compatibility will be disabled.");
			fatalError = true;
		}
	}

	private static void configFieldError(String fieldName) {
		if (configFieldErrored.getOrDefault(fieldName, false)) {
			return;
		}

		error("Failed to read the \"double_hotbar\" mod's \"" + fieldName + "\" config value. This could lead to the armor durability bars being positioned at an incorrect height.");
		configFieldErrored.put(fieldName, true);
	}

	private static void error(String message) {
		MicroDurability.LOGGER.error(message);
	}
}
