// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3


package com.xpkitty.minigame.command;

import com.xpkitty.minigame.GameState;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.manager.ConfigManager;
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
                Minigame.giveLobbyItems(player);
            }
        }

        return false;
    }
}
