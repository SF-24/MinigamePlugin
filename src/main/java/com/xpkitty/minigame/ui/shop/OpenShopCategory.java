package com.xpkitty.minigame.ui.shop;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.GameType;
import com.xpkitty.minigame.instance.data.PlayerDataSave;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class OpenShopCategory {

    Material kitIconMaterial = Material.CHEST;
    String kitIconName = ChatColor.AQUA + "Kits";
    ArrayList<String> kitIconLore = new ArrayList<>(Arrays.asList(ChatColor.WHITE + "Click to open kit menu"));

    Material moneyAmountIcon = Material.EMERALD;
    String moneyIconName = ChatColor.WHITE + "Current coins: " + ChatColor.GOLD;

    public OpenShopCategory(Player player, ShopCategories category, Minigame minigame) {
        PlayerDataSave playerDataSave = new PlayerDataSave(player, minigame);

        Inventory ui = Bukkit.createInventory(null,9,category.getDisplay());

        ItemStack moneyStack = new ItemStack(moneyAmountIcon);
        ItemMeta moneyStackMeta = moneyStack.getItemMeta();
        moneyStackMeta.setDisplayName(moneyIconName + playerDataSave.getCoins(player, GameType.valueOf(category.name().toUpperCase(Locale.ROOT))));
        moneyStack.setItemMeta(moneyStackMeta);
        ui.addItem(moneyStack);

        for(String string : category.getObjectList()) {

            if(string.equalsIgnoreCase("kit") || string.equalsIgnoreCase("kits")) {

                ItemStack kitStack = new ItemStack(kitIconMaterial);
                ItemMeta kitStackMeta = kitStack.getItemMeta();
                kitStackMeta.setDisplayName(kitIconName);
                kitStackMeta.setLore(kitIconLore);
                kitStackMeta.setLocalizedName("kits");
                kitStack.setItemMeta(kitStackMeta);
                ui.addItem(kitStack);

            } else if(string.equalsIgnoreCase("upgrades")) {
                //TODO: UPGRADES FOR MINIGAMES
            }
        }

        player.openInventory(ui);
    }

}
