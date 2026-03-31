// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3


package com.mineshaft.minigame.command;

import com.mineshaft.minigame.Minigame;
import com.mineshaft.minigame.instance.Arena;
import com.mineshaft.minigame.manager.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {
    private final Minigame minigame;

    public LobbyCommand(Minigame minigame) {
        this.minigame = minigame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            Location loc = ConfigManager.getLobbySpawn();
            player.teleport(loc);

            Arena arena = minigame.getArenaManager().getArena(player);
            if(arena != null) {
                player.sendMessage(ChatColor.RED + "You left the arena");
                arena.removePlayer(player);
            } else {
                player.sendMessage(ChatColor.RED + "You are now in the lobby");
                if(ConfigManager.getGiveLobbyCompassOnJoin()) {
                    Minigame.giveLobbyItems(player);
                }
            }
        }

        return false;
    }
}
