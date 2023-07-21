// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.ui;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.GameType;
import com.xpkitty.minigame.instance.data.PlayerJsonDataSave;
import com.xpkitty.minigame.manager.statistics.PlayerStatistics;
import com.xpkitty.minigame.manager.statistics.StatisticType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class StatisticsUI {

    public static void openStatisticsUI(Player player, Minigame minigame) {
        Inventory ui = Bukkit.createInventory(null,27, ChatColor.BLACK + "Player Statistics");

        for(int i = 0; i<ui.getSize(); i++) {
            // Create item stack and meta
            ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta glassPaneMeta = glassPane.getItemMeta();

            // name them
            assert glassPaneMeta != null;
            glassPaneMeta.setDisplayName(ChatColor.WHITE + "");

            // apply meta and add to ui
            glassPane.setItemMeta(glassPaneMeta);
            ui.setItem(i,glassPane);
        }

        int games = 0;
        int startVal = 10;
        if(games>7&&games<10) {
            startVal=9;
        }
        if(games>9) {
            startVal=0;
        }

        int i = startVal;
        for(GameType gameType : GameType.values()) {
            if(gameType.getStatistics()!=null) {

                ItemStack item = new ItemStack(gameType.getIcon());
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(ChatColor.AQUA.toString() + ChatColor.BOLD + gameType.getDisplayName());

                ArrayList<String> lore = new ArrayList<>();
                PlayerJsonDataSave playerJsonDataSave = new PlayerJsonDataSave(player,minigame);

                PlayerStatistics playerStatistics = playerJsonDataSave.getPlayerStatisticsForLatestSeason(player);

                for(StatisticType stat : gameType.getStatistics()) {
                    lore.add(ChatColor.WHITE + stat.getName() + ": " + stat.getColor() + playerStatistics.getStatFromGame(gameType,stat));
                }
                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
                ui.setItem(i,item);
                i++;

            }
        }
        player.openInventory(ui);
    }

}
