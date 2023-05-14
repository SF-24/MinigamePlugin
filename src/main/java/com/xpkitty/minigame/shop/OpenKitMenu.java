package com.xpkitty.minigame.shop;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.data.PlayerDataSave;
import com.xpkitty.minigame.kit.KitType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class OpenKitMenu {

    String priceLine = ChatColor.WHITE + "Price: " + ChatColor.GOLD;

    public OpenKitMenu(Player player, Minigame minigame, String game) {

        int kitCount = 0;

        for(KitType kitType : KitType.values()) {
            if(kitType.name().equalsIgnoreCase(game.toUpperCase(Locale.ROOT))) {

                kitCount++;

            }
        }
        int size = 9;

        if(kitCount<=9) {
            size = 9;
        } else if(kitCount<=18) {
            size = 18;
        } else if(kitCount<=27) {
            size = 27;
        } else if(kitCount<=36) {
            size = 36;
        } else if(kitCount<=45) {
            size = 45;
        } else if(kitCount<=54) {
            size = 54;
        } else {
            size=54;
        }

        PlayerDataSave playerDataSave = new PlayerDataSave(player, minigame);

        int coins = playerDataSave.getCoins(player, game);

        Inventory ui = Bukkit.createInventory(null, size, ChatColor.BLACK + "Kits, " + ChatColor.DARK_RED + "You have " + coins + " coins");

        for(KitType kitType : KitType.values()) {
            if(kitType.getGame().equalsIgnoreCase(game.toUpperCase(Locale.ROOT))) {

                ItemStack kit = new ItemStack(kitType.getMaterial());
                ItemMeta kitMeta = kit.getItemMeta();
                kitMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                kitMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                kitMeta.addItemFlags(ItemFlag.HIDE_DYE);
                kitMeta.setDisplayName(kitType.getDisplay());

                boolean ownsKit = playerDataSave.getKitOwnershipStatus(kitType.name(), player);

                String ownedLine = "";

                if(ownsKit) {
                    ownedLine = ChatColor.GREEN + "OWNED";
                } else {
                    if(coins>=kitType.getPrice()) {
                        ownedLine = ChatColor.GOLD + "Click to buy";
                    } else {
                        ownedLine = ChatColor.RED + "Cannot afford kit";
                    }
                }

                int price = kitType.getPrice();
                kitMeta.setLore(new ArrayList<String>(Arrays.asList(ChatColor.GRAY + kitType.getDescription(),"",priceLine + price, ownedLine)));
                kitMeta.setLocalizedName(kitType.name());
                kit.setItemMeta(kitMeta);
                ui.addItem(kit);

            }
        }

        player.openInventory(ui);
    }
}
