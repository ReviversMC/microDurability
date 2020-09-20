package dzwdz.microdurability;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;

public class EntryPoint implements ModInitializer {
    public static Config config;

    @Override
    public void onInitialize() {
        HudRenderCallback.EVENT.register(new Renderer());

        config = new Config();
    }

    public static boolean shouldWarn(ItemStack stack) {
        if (stack == null || !stack.isDamageable()) return false;
        if (config.requireMending && EnchantmentHelper.getLevel(Enchantments.MENDING, stack) <= 0) return false;
        int durability = stack.getMaxDamage() - stack.getDamage();
        return durability < config.minDurability
            && durability * 100 / config.minPercent < stack.getMaxDamage();
    }
}
