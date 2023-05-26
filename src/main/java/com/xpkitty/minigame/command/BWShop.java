package com.xpkitty.minigame.command;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.game.bedwars.BedWarsGame;
import com.xpkitty.minigame.ui.bedwars.Shop;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BWShop implements CommandExecutor {
    Minigame minigame;

    public BWShop(Minigame minigame) {
        this.minigame=minigame;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(args[0].equalsIgnoreCase("bw")) {
                if(minigame.getArenaManager().getArena(player)!=null) {
                    if(minigame.getArenaManager().getArena(player).getGame()!=null) {
                        if (minigame.getArenaManager().getArena(player).getGame() instanceof BedWarsGame) {
                            new Shop(player);
                        }
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "USE: /shop bw");
                player.sendMessage(ChatColor.RED + "debug only: WARNING! EACH USE IS LOGGED AND REPORTED");
            }

        } else {
            System.out.println("ERROR. sender not player");
        }

        return false;
    }
}
