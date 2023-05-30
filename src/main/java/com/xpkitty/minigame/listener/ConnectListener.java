package com.xpkitty.minigame.listener;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.instance.GameType;
import com.xpkitty.minigame.instance.data.PlayerDataSave;
import com.xpkitty.minigame.manager.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ConnectListener implements Listener {



    private final HashMap<UUID, PlayerDataSave> dataSaveList = new HashMap<>();
    public Minigame minigame;

    public ConnectListener(Minigame minigame){
        this.minigame = minigame;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();

        ArrayList<String> disabledWorlds = new ArrayList<>();

        disabledWorlds.add("CityLife");
        disabledWorlds.add("Flat");
        disabledWorlds.add("Bobby");
        disabledWorlds.add("Utumno");

        if(!disabledWorlds.contains(player.getWorld().getName())) {
            player.sendMessage(ChatColor.GREEN + ConfigManager.getMessage());
            player.teleport(ConfigManager.getLobbySpawn());
            Minigame.giveLobbyItems(player);
        }
        PlayerDataSave instance = new PlayerDataSave(player, minigame);

        if(!dataSaveList.containsKey(player.getUniqueId())) {
            dataSaveList.put(player.getUniqueId(), instance);
            System.out.println("adding key to HashMap");
        }
        instance.addPoints(player, GameType.PVP, 0);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();

        Arena arena = minigame.getArenaManager().getArena(player);
        if(arena != null) {
            arena.removePlayer(player);
        }
        dataSaveList.remove(player.getUniqueId());

    }

    public HashMap<UUID, PlayerDataSave> getDataSaveList() { return dataSaveList; }
    public PlayerDataSave getPlayerData(Player player) {
        if(dataSaveList.containsKey(player.getUniqueId())) {
            return(dataSaveList.get(player.getUniqueId()));
        }

        return null;
    }

    private void initiateFile() {}
}
