// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3


package com.xpkitty.minigame.instance;

import com.xpkitty.minigame.GameState;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.listener.ConnectListener;
import com.xpkitty.minigame.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Game implements Listener {

    // GAME CLASS, GAME SPECIFIC
    protected ConfigManager config;
    protected Arena arena;
    protected ConnectListener connectListener;
    protected Minigame minigame;
    protected List<BukkitTask> tasks;

    public Game(Minigame minigame, Arena arena, ConnectListener connectListener) {
        this.arena = arena;
        this.minigame = minigame;
        this.connectListener = connectListener;
        tasks = new ArrayList<>();

        Bukkit.getPluginManager().registerEvents(this, minigame);

        for(UUID uuid : arena.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);

            // close player inventory
            player.closeInventory();

            // fill health and hunger bars
            player.setHealth(20);
            player.setFoodLevel(20);

            // clear player potion effects
            for(PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }
        }
    }

    public void start() {
        arena.setState(GameState.LIVE);
        onStart();
    }

    public abstract void onStart();

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    public void cancelTasks() {
        for(BukkitTask task : tasks) {
            task.cancel();
        }
    }

    public abstract boolean isTeamGame();
}
