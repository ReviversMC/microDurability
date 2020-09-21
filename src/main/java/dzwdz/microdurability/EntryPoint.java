package dzwdz.microdurability;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;

public class EntryPoint implements ModInitializer {
    public static ModConfig config;
    public static Renderer renderer;

    @Override
    public void onInitialize() {
        renderer = new Renderer();

        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public static boolean shouldWarn(ItemStack stack) {
        if (stack == null || !stack.isDamageable()) return false;
        if (config.requireMending && EnchantmentHelper.getLevel(Enchantments.MENDING, stack) <= 0) return false;
        int durability = stack.getMaxDamage() - stack.getDamage();
        return durability < config.minDurability
            && durability * 100f / config.minPercent < stack.getMaxDamage();
    }
}
