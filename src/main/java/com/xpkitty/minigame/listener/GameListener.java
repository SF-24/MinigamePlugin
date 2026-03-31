// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.listener;

import com.mineshaft.mineshaftapi.util.ui.UIUtil;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.ui.GameSelectorUI;
import com.xpkitty.minigame.ui.MainUI;
import com.xpkitty.minigame.ui.StatisticsUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class GameListener implements Listener {

    private final Minigame minigame;

    public GameListener(Minigame minigame){
        this.minigame = minigame;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e){

        if(minigame.getArenaManager() != null && minigame.getArenaManager().getArena(e.getWorld()) != null) {
            System.out.println(e.getWorld() + " has been loaded");
            Arena arena = minigame.getArenaManager().getArena(e.getWorld());
            if (arena != null && arena.getReset()) {
                arena.toggleCanJoin();
            }
        }

    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if(e.getItemDrop().getItemStack().getItemMeta()!=null) {
            String locName = UIUtil.getOnclick(e.getItemDrop().getItemStack());
            if(locName.contains("lobby_")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onUIClick(InventoryClickEvent e) {

        if(e.getCurrentItem()!=null && e.getCurrentItem().getItemMeta()!=null) {
            String locName = UIUtil.getOnclick(e.getCurrentItem());
            e.setCancelled(interactTest((Player) e.getWhoClicked(),locName));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            e.getPlayer().getInventory().getItemInMainHand();
            if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null) {
                String locName = UIUtil.getOnclick(e.getPlayer().getInventory().getItemInMainHand());
                e.setCancelled(interactTest(e.getPlayer(), locName));
            }
        }
    }

    public boolean interactTest(Player player, String locName) {

        if(locName.contains("lobby_")) {
            String useName = locName.substring(6);

            if(useName.equalsIgnoreCase("game_selector")) {
                GameSelectorUI.openMainGameSelectorUI(player,minigame);
            } else if(useName.equalsIgnoreCase("shop")) {
                new MainUI(player,minigame);
            } else if(useName.equalsIgnoreCase("profile")) {
                StatisticsUI.openStatisticsUI(player,minigame);
            } else {
                player.sendMessage(ChatColor.RED + "ERROR! unidentified on click name: " + locName + " | useName = " + useName);
            }

            return true;
        }
        return false;
    }
}
