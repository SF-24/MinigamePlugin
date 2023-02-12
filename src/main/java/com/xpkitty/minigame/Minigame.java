package com.xpkitty.minigame;

import com.xpkitty.minigame.command.ArenaCommand;
import com.xpkitty.minigame.command.LobbyCommand;
import com.xpkitty.minigame.listener.ConnectListener;
import com.xpkitty.minigame.listener.GameListener;
import com.xpkitty.minigame.listener.UIListener;
import com.xpkitty.minigame.manager.ArenaManager;
import com.xpkitty.minigame.manager.ConfigManager;
import com.xpkitty.rpgplugin.Rpg;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Minigame extends JavaPlugin{

    private ArenaManager arenaManager;
    private final ConfigManager configManager = new ConfigManager();

    Rpg api;

    @Override
    public void onEnable(){
        api = (Rpg) Bukkit.getPluginManager().getPlugin("RpgPlugin");


        ConfigManager.setupConfig(this);



        ConnectListener listener = new ConnectListener(this);
        arenaManager = new ArenaManager(this, listener);

        Bukkit.getPluginManager().registerEvents(new GameListener(this), this);
        Bukkit.getPluginManager().registerEvents(new UIListener(this), this);
        Bukkit.getPluginManager().registerEvents(listener, this);

        getCommand("arena").setExecutor(new ArenaCommand(this));
        getCommand("lobby").setExecutor(new LobbyCommand(this));
    }

    public Rpg getApi() { return api; }

    public ConfigManager getConfigManager() {return configManager; }
    public ArenaManager getArenaManager() { return arenaManager; }

}
