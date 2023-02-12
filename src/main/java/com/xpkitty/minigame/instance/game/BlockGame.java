package com.xpkitty.minigame.instance.game;

import com.xpkitty.minigame.GameState;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.instance.Game;
import com.xpkitty.minigame.listener.ConnectListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.UUID;

public class BlockGame extends Game {

    private final HashMap<UUID, Integer> points;

    public BlockGame(Minigame minigame, Arena arena, ConnectListener connectListener) {
        super(minigame, arena, connectListener);
        points = new HashMap<>();
    }

    public void addPoint(Player player) {
        int playerPoints = points.get(player.getUniqueId()) + 1;
        if(playerPoints == 20) {
            arena.sendMessage(ChatColor.GOLD + player.getName() + " HAS WON! Thanks for playing.");
            arena.reset(false, 0);
            return;
        }

        player.sendMessage(ChatColor.GREEN + "+1 POINT!");
        points.replace(player.getUniqueId(), playerPoints);

    }

    @Override
    public void onStart() {
        arena.sendMessage(ChatColor.GREEN + "GAME HAS STARTED! Your objective is to break 20 blocks.");

        for(UUID uuid : arena.getPlayers()) {
            points.put(uuid, 0);
            Bukkit.getPlayer(uuid).closeInventory();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){

        if(arena.getPlayers().contains(e.getPlayer().getUniqueId()) && arena.getState() == GameState.LIVE) {
            addPoint(e.getPlayer());
        }

    }
}
