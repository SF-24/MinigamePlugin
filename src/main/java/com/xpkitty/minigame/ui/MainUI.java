// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.ui;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.ui.shop.ShopCategories;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MainUI {
    Minigame minigame;
    public MainUI(Player player, Minigame minigame) {
        this.minigame = minigame;

        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Minigames");

        for(ShopCategories type: ShopCategories.values()) {
            ItemStack is = new ItemStack(type.getMaterial());
            ItemMeta isMeta = is.getItemMeta();
            isMeta.setDisplayName(type.getDisplay());
            isMeta.setLore(Arrays.asList(ChatColor.WHITE + type.getDescription()));
            isMeta.setLocalizedName(type.name());
            isMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            isMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            isMeta.addItemFlags(ItemFlag.HIDE_DYE);
            is.setItemMeta(isMeta);

            gui.addItem(is);
        }

        player.openInventory(gui);
    }
}
