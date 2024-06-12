package com.github.reviversmc.microdurability.mccompat;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;

public final class McVersionHelper {
	public static final String INFINITY = "*";
	public static final Version MC_VERSION = FabricLoader.getInstance()
			.getModContainer("minecraft")
			.orElseThrow()
			.getMetadata()
			.getVersion();

	public static boolean isWithin(String inclusiveLowerBounds, String inclusiveUpperBounds) {
		Version lowestSupportedVersion;
		Version highestSupportedVersion;

		try {
			lowestSupportedVersion = Version.parse(inclusiveLowerBounds);
			highestSupportedVersion = Version.parse(inclusiveUpperBounds);
		} catch (VersionParsingException e) {
			throw new RuntimeException("Failed to parse version bounds", e);
		}

		if (MC_VERSION.compareTo(lowestSupportedVersion) < 0) {
			return false;
		}

		if (inclusiveUpperBounds.equals(INFINITY) || MC_VERSION.compareTo(highestSupportedVersion) <= 0) {
			return true;
		}

		return false;
	}
}
