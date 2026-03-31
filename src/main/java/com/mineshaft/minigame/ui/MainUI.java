// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.mineshaft.minigame.ui;

import com.mineshaft.mineshaftapi.util.ui.UIUtil;
import com.mineshaft.minigame.Minigame;
import com.mineshaft.minigame.ui.shop.ShopCategories;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class MainUI {
    Minigame minigame;

    public MainUI(Player player, Minigame minigame) {
        this.minigame = minigame;

        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Minigames");

        for(ShopCategories type: ShopCategories.values()) {
            ItemStack is = new ItemStack(type.getMaterial());
            ItemMeta isMeta = is.getItemMeta();
            assert isMeta != null;
            isMeta.setDisplayName(type.getDisplay());
            isMeta.setLore(Collections.singletonList(ChatColor.WHITE + type.getDescription()));
            isMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            isMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            isMeta.addItemFlags(ItemFlag.HIDE_DYE);
            is.setItemMeta(isMeta);

            UIUtil.setOnclick(is,type.name());

            gui.addItem(is);
        }
        player.openInventory(gui);
    }
}
