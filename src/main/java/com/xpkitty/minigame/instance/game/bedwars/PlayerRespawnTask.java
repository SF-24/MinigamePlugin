package com.xpkitty.minigame.instance.game.bedwars;

import com.xpkitty.minigame.Minigame;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PlayerRespawnTask extends BukkitRunnable {
    private final Minigame minigame;
    private final BedWarsGame bedWarsGame;
    private final List<ItemStack> itemsToGive;
    private int time;
    private final Player player;

    public PlayerRespawnTask(Minigame minigame, BedWarsGame bedWarsGame, Player player, int time, List<ItemStack> itemsToGive) {
        this.time = time+1;
        this.player=player;
        this.bedWarsGame=bedWarsGame;
        this.minigame = minigame;
        this.itemsToGive=itemsToGive;
    }

    public void start() {
        runTaskTimer(minigame, 0, 20);
    }

    @Override
    public void run() {
        time-=1;
        if(time==0) {
            // send respawn message
            player.sendTitle(ChatColor.GREEN + "RESPAWNED","", 0, 20, 0);

            // give equipment and reset health, food, potion effects, etc.
            bedWarsGame.giveEquipment(player);

            // give survival mode
            player.setGameMode(GameMode.SURVIVAL);

            // tp to base
            player.teleport(bedWarsGame.getArena().getTeamSpawn(bedWarsGame.getArena().getPlayerTeam(player)));


            for(ItemStack item : itemsToGive) {
                player.getInventory().addItem(item);
            }

        } else if(time>0) {

            // send message
            String secondString = "seconds";
            if(time == 1) {
                secondString="second";
            }

            player.sendTitle(ChatColor.RED + "YOU DIED",ChatColor.YELLOW + "You will respawn in " + ChatColor.RED + time + " " + ChatColor.YELLOW + secondString, 0, 100 ,0 );
            player.sendMessage(ChatColor.YELLOW + "You will respawn in " + ChatColor.RED + time + " " + ChatColor.YELLOW + secondString);
        } else {
            player.sendTitle("","");
            cancel();
        }
    }
}
