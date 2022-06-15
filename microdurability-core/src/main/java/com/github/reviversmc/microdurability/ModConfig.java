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
    }


    @ConfigEntry.Category("lowDurabilityWarning")
    @ConfigEntry.Gui.TransitiveObject
    public LowDurabilityWarning lowDurabilityWarning = new LowDurabilityWarning();

    public static class LowDurabilityWarning {
        public boolean displayWarningForTools = true;
        public boolean displayWarningForArmor = true;
        public boolean onlyOnMendingItems = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 250)
        public int minDurabilityPointsBeforeWarning = 100;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 1, max = 99)
        public int minDurabilityPercentageBeforeWarning = 10;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 5)
        public float blinkTime = 1f;
    }

}
