// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.listener;

import com.xpkitty.minigame.GameState;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class PregameListener implements Listener {

    private final Minigame minigame;

    public PregameListener(Minigame minigame) {
       this.minigame = minigame;
    }

    @EventHandler
    void playerTakeDamageEvent(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            Arena arena = minigame.getArenaManager().getArena(player);
            if (arena != null) {
                if (!arena.getState().equals(GameState.LIVE)) {
                    e.setCancelled(true);
                    if (player.getHealth() < 0) {
                        player.setHealth(20);
                        player.setFallDistance(0);
                        player.setVelocity(new Vector(0, 0, 0));
                        player.teleport(arena.getRandomSpawnLocation());
                        player.setFoodLevel(20);
                    }
                }
            }
        }
    }
    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent event) {
        EntityDamageEvent.DamageCause cause = event.getCause();
        Entity player = event.getEntity();

        if(player instanceof Player) {
            if(minigame.getArenaManager().getArena((Player) player)!=null) {
                if (!minigame.getArenaManager().getArena((Player) player).getState().equals(GameState.LIVE) && minigame.getArenaManager().getArena((Player) player) != null && cause.equals(EntityDamageEvent.DamageCause.LAVA)) {
                    player.setFireTicks(0);
                    player.setFallDistance(0);
                    player.setVelocity(new Vector(0, 0, 0));
                    player.teleport(minigame.getArenaManager().getArena((Player) player).getRandomSpawnLocation());
                    ((Player) player).setHealth(20);
                    ((Player) player).setFoodLevel(20);
                    if(player.getLocation().getY()>0) {
                        player.teleport(minigame.getArenaManager().getArena((Player) player).getRandomSpawnLocation());
                    }
                }
            }
        }
    }

    @EventHandler
    void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Arena arena = minigame.getArenaManager().getArena(player);
        if(arena!=null) {
            if(!arena.getState().equals(GameState.LIVE)) {
                e.setCancelled(true);
            }
        }
    }
}
