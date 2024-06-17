package com.github.reviversmc.microdurability;

import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

public class Renderer121 extends Renderer1205 {
	private RegistryEntry<Enchantment> mending;
	private World world;

	@Override
	protected boolean hasMending(ItemStack stack) {
		return EnchantmentHelper.getLevel(getMending(), stack) > 0;
	}

	private RegistryEntry<Enchantment> getMending() {
		MinecraftClient client = MinecraftClient.getInstance();

		if (client.world != world) {
			world = client.world;
			mending = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.MENDING.getValue()).orElseThrow();
		}

		return mending;
	}
}
