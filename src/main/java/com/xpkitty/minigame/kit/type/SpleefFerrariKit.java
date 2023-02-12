package com.xpkitty.minigame.kit.type;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.kit.Kit;
import com.xpkitty.minigame.kit.KitType;
import org.bukkit.ChatColor;
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

public class SpleefFerrariKit extends Kit {
    public SpleefFerrariKit(Minigame minigame, UUID uuid) {
        super(minigame, KitType.KNIGHT, uuid);
    }

    @Override
    public void onStart(Player player) {

        ItemStack snow = new ItemStack(Material.SNOW_BLOCK, 15);
        ItemStack shovel = new ItemStack(Material.STONE_SHOVEL);
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, 30, 0, true, false, true);
        potionMeta.addCustomEffect(effect, true);
        potionMeta.setDisplayName(ChatColor.WHITE + "Potion of Swiftness (30:00)");
        potion.setItemMeta(potionMeta);

        ItemMeta shovelMeta = shovel.getItemMeta();
        shovelMeta.setUnbreakable(true);
        shovelMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        shovelMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        shovelMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("damage",-0.99999f, ADD_NUMBER));
        shovel.setItemMeta(shovelMeta);

        player.getInventory().addItem(shovel);
        player.getInventory().addItem(snow);
        player.getInventory().addItem(potion);
        player.getInventory().addItem(potion);

    }
}
