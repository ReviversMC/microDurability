package com.github.reviversmc.microdurability;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;

public class MicroDurability implements ModInitializer {
	public static Renderer renderer;
    public static ModConfig config;

    @Override
    public void onInitialize() {
		renderer = new Renderer();

        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public static boolean shouldWarn(ItemStack stack) {
        if (stack == null || !stack.isDamageable()) {
            return false;
        }
        if (config.lowDurabilityWarning.onlyOnMendingItems
                && EnchantmentHelper.getLevel(Enchantments.MENDING, stack) <= 0) {
            return false;
        }
        int durability = stack.getMaxDamage() - stack.getDamage();

        boolean damageAbsoluteValueEnough = durability < config.lowDurabilityWarning.minDurabilityPointsBeforeWarning;
        boolean damagePercentageEnough =
                (durability * 100f / stack.getMaxDamage()) < config.lowDurabilityWarning.minDurabilityPercentageBeforeWarning;

        return damageAbsoluteValueEnough && damagePercentageEnough;
    }
}
