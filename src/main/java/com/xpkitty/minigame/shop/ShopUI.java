package com.xpkitty.minigame.shop;

import com.xpkitty.minigame.Minigame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ShopUI {
    Minigame minigame;
    public ShopUI(Player player, Minigame minigame) {
        this.minigame = minigame;

        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Minigame shop");

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
