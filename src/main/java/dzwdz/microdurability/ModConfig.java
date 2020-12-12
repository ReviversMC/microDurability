package dzwdz.microdurability;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "microdurability")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.PrefixText
    boolean requireMending = true;

    int minDurability = 100;
    float minPercent = 10;

    float blinkTime = 1f;
    boolean toolWarning = true;

    boolean undamagedBars = true;
}
