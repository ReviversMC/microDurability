package com.github.reviversmc.microdurability;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "microdurability")
public class ModConfig implements ConfigData {
	@ConfigEntry.Category("armorBars")
	@ConfigEntry.Gui.TransitiveObject
	public ArmorBars armorBars = new ArmorBars();

	public static class ArmorBars {
		public boolean displayArmorBars = true;
		public boolean displayBarsForUndamagedArmor = true;

		@Comment("If an armor piece is undamaged, its bar will have the defined custom color.")
		@ConfigEntry.Gui.Tooltip
		public boolean useCustomBarColorForUndamagedArmor = false;

		@ConfigEntry.ColorPicker(allowAlpha = true)
		public int customBarColorForUndamagedArmor = 0xFFFFFFFF;
	}

	@ConfigEntry.Category("lowDurabilityWarning")
	@ConfigEntry.Gui.TransitiveObject
	public LowDurabilityWarning lowDurabilityWarning = new LowDurabilityWarning();

	public static class LowDurabilityWarning {
		public boolean displayWarningForTools = true;
		public boolean displayWarningForArmor = true;
		public boolean onlyOnMendingItems = true;

		@Comment("An item's durability has to be below both the minimum point value and the minimum percentage for the warning to show!")
		@ConfigEntry.Gui.Tooltip
		@ConfigEntry.BoundedDiscrete(min = 1, max = 250)
		public int minDurabilityPointsBeforeWarning = 100;

		@Comment("An item's durability has to be below both the minimum point value and the minimum percentage for the warning to show!")
		@ConfigEntry.Gui.Tooltip
		@ConfigEntry.BoundedDiscrete(min = 1, max = 99)
		public int minDurabilityPercentageBeforeWarning = 10;

		@Comment("Set to 0 to disable blinking.")
		@ConfigEntry.Gui.Tooltip
		@ConfigEntry.BoundedDiscrete(min = 0, max = 5)
		public float blinkTime = 1f;
	}
}
