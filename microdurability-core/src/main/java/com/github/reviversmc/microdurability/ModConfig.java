package com.github.reviversmc.microdurability;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "microdurability")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.PrefixText
    boolean requireMending = true;

    int minDurability = 100;
    float minPercent = 10;

    float blinkTime = 1f;
    boolean toolWarning = true;

    boolean undamagedBars = true;

    boolean armorWarning = true;

}
