package com.xpkitty.minigame.listener;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
}
