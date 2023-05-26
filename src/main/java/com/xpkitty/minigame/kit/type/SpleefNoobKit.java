package com.xpkitty.minigame.kit.type;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.kit.Kit;
import com.xpkitty.minigame.kit.KitType;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

import static org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER;

public class SpleefNoobKit extends Kit {

    public SpleefNoobKit(Minigame minigame, UUID uuid) {
        super(minigame, KitType.SPLEEF_NOOB, uuid);
    }

    @Override
    public void onStart(Player player) {

        ItemStack snow = new ItemStack(Material.DIRT, 20);
        ItemStack shovel = new ItemStack(Material.WOODEN_SHOVEL);
        ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        potionMeta.setDisplayName("Noob Grenade (Dangerous)");
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, 10, 0, true, true, true);
        potionMeta.addCustomEffect(potionEffect, false);
        potion.setItemMeta(potionMeta);

        ItemStack potion2 = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta2 = (PotionMeta) potion2.getItemMeta();
        potionMeta2.setDisplayName("Noob Grenade (Dangerous)");
        PotionEffect potionEffect2 = new PotionEffect(PotionEffectType.SLOW_DIGGING, 10, 0, true, true, true);
        potionMeta2.addCustomEffect(potionEffect2, false);
        potion2.setItemMeta(potionMeta2);

        ItemStack potion3 = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta3 = (PotionMeta) potion3.getItemMeta();
        potionMeta3.setDisplayName("Noob Grenade (Dangerous)");
        PotionEffect potionEffect3 = new PotionEffect(PotionEffectType.SLOW, 10, 0, true, true, true);
        potionMeta3.addCustomEffect(potionEffect3, false);
        potion3.setItemMeta(potionMeta2);

        ItemMeta shovelMeta = shovel.getItemMeta();
        shovelMeta.setUnbreakable(true);
        shovelMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        shovelMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        shovelMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("damage",-0.99999f, ADD_NUMBER));
        shovel.setItemMeta(shovelMeta);

        player.getInventory().addItem(shovel);
        player.getInventory().addItem(snow);
        player.getInventory().addItem(potion);
        player.getInventory().addItem(potion3);
        player.getInventory().addItem(potion2);

    }
}
