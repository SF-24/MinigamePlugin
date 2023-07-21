// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.instance.team;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.instance.GameType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class TeamUI {
    Minigame minigame;
    public TeamUI(Player player, Minigame minigame, GameType game) {
        this.minigame = minigame;

        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Team selection");
        Arena arena = minigame.getArenaManager().getArena(player);
        


        if(arena!=null) {
            if(arena.isTeamGame()) {
                for (Team team : Team.values()) {
                    String fullStatus=ChatColor.RED.toString() + ChatColor.BOLD + "FULL";
                    String selectStatus=ChatColor.GREEN.toString() + ChatColor.BOLD + "SELECTED";
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(ChatColor.WHITE + "Player count: " + arena.getTeamSize(team));

                    if (arena.isTeamFull(team, player,false)) {
                        lore.add(fullStatus);
                    }
                    if(arena.isPlayerInTeam(player) && arena.getPlayerTeam(player).equals(team)) {
                        lore.add(selectStatus);
                    }
                    ItemStack is = new ItemStack(team.getWool());
                    ItemMeta isMeta = is.getItemMeta();
                    isMeta.setDisplayName(team.getColorCode() + ChatColor.BOLD + team.getName());
                    isMeta.setLore(lore);
                    isMeta.setLocalizedName(team.name());
                    isMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    isMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    isMeta.addItemFlags(ItemFlag.HIDE_DYE);
                    is.setItemMeta(isMeta);

                    gui.addItem(is);
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "You are not in an arena.");
        }

        player.openInventory(gui);
    }

}
