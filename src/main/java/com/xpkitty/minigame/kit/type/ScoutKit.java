// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.kit.type;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.kit.Kit;
import com.xpkitty.minigame.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class ScoutKit extends Kit {
    public ScoutKit(Minigame minigame, UUID uuid) {
        super(minigame, KitType.SCOUT, uuid);
    }


    @Override
    public void onStart(Player player) {
        ItemStack sword = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.setUnbreakable(true);
        sword.setItemMeta(swordMeta);

        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, 600, 1, true, false, true);
        assert potionMeta != null;
        potionMeta.addCustomEffect(effect, true);
        potionMeta.setColor(Color.AQUA);
        potionMeta.setDisplayName(ChatColor.WHITE + "Potion of Swiftness");
        potion.setItemMeta(potionMeta);

        player.getInventory().addItem(sword);
        player.getInventory().addItem(new ItemStack(Material.DIAMOND_HOE));
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 10));
        player.getInventory().addItem(potion);
        player.getInventory().addItem(potion);
    }
}
