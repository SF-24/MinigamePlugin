package com.xpkitty.minigame.instance;

import com.xpkitty.minigame.GameState;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.listener.ConnectListener;
import com.xpkitty.minigame.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public abstract class Game implements Listener {

    // GAME CLASS, GAME SPECIFIC
    protected ConfigManager config;
    protected Arena arena;
    protected ConnectListener connectListener;

    public Game(Minigame minigame, Arena arena, ConnectListener connectListener) {
        this.arena = arena;
        this.connectListener = connectListener;

        Bukkit.getPluginManager().registerEvents(this, minigame);
    }

    public void start() {
        arena.setState(GameState.LIVE);
        onStart();

    }

    public abstract void onStart();

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

}
