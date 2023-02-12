package com.xpkitty.minigame.listener;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.instance.PlayerDataSave;
import com.xpkitty.minigame.kit.KitType;
import com.xpkitty.minigame.shop.OpenKitMenu;
import com.xpkitty.minigame.shop.OpenShopCategory;
import com.xpkitty.minigame.shop.ShopCategories;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class UIListener implements Listener {
    Minigame minigame;

    public UIListener(Minigame minigame) {
        this.minigame = minigame;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if(e.getView().getTitle().contains("Kit selection") && e.getInventory() != null && e.getCurrentItem() != null) {
            e.setCancelled(true);

            KitType type = KitType.valueOf(e.getCurrentItem().getItemMeta().getLocalizedName());

            Arena arena = minigame.getArenaManager().getArena(player);
            if(arena != null) {
                KitType activated = arena.getKitType(player);
                if(activated != null && activated == type) {
                    player.sendMessage(ChatColor.RED + "You already have this kit equipped.");
                } else {
                    player.sendMessage(ChatColor.GREEN + "You have equipped the " + type.getDisplay() + ChatColor.GREEN + " kit");
                    arena.setKit(player.getUniqueId(), type);
                }
                player.closeInventory();
            }


        } else if(e.getView().getTitle().contains("Minigame shop") && e.getInventory() != null && e.getCurrentItem() != null) {

            e.setCancelled(true);

            ShopCategories type = ShopCategories.valueOf(e.getCurrentItem().getItemMeta().getLocalizedName());

            new OpenShopCategory(player, type, minigame);
        } else if(e.getView().getTitle().contains("Kit")) {

            e.setCancelled(true);
            String kitName = e.getCurrentItem().getItemMeta().getLocalizedName();

            PlayerDataSave playerDataSave = new PlayerDataSave(player, minigame);

            for(KitType kitType : KitType.values()) {
                if(kitType.name().equalsIgnoreCase(kitName)) {

                    //TODO: KIT BUYING

                    if(!playerDataSave.getKitOwnershipStatus(kitName,player)) {
                        if(playerDataSave.getCoins(player, kitType.getGame()) >= kitType.getPrice()) {

                            YamlConfiguration yamlConfiguration = playerDataSave.getModifyLocation(player);
                            File file = playerDataSave.getFile(player);

                            String kitNameYaml = kitType.name().toLowerCase(Locale.ROOT);
                            yamlConfiguration.set("kits." + kitNameYaml, true);
                            player.sendMessage(ChatColor.GREEN + "Kit purchased: " + kitType.getDisplay());
                            playerDataSave.addPoints(player,"coins",kitType.getGame(),-100);
                            new OpenKitMenu(player,minigame,kitType.getGame());

                            try {
                                yamlConfiguration.save(file);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have enough coins");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You already own this kit!");
                    }
                }
            }



        }

        for(ShopCategories value : ShopCategories.values()) {
            if(e.getView().getTitle().contains(value.getDisplay())) {
                e.setCancelled(true);

                switch (e.getCurrentItem().getItemMeta().getLocalizedName()) {

                    default:
                        return;
                    case "kits":
                        new OpenKitMenu((Player) e.getWhoClicked(), minigame, value.name());
                        break;
                }
            }
        }
    }
}
