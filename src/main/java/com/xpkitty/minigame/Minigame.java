package com.xpkitty.minigame;

import com.xpkitty.minigame.command.ArenaCommand;
import com.xpkitty.minigame.command.BWShop;
import com.xpkitty.minigame.command.LobbyCommand;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.listener.*;
import com.xpkitty.minigame.manager.ArenaManager;
import com.xpkitty.minigame.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Minigame extends JavaPlugin{

    private ArenaManager arenaManager;
    private final ConfigManager configManager = new ConfigManager();


    @Override
    public void onEnable(){
        ConfigManager.setupConfig(this);

        ConnectListener listener = new ConnectListener(this);
        arenaManager = new ArenaManager(this, listener);

        Bukkit.getPluginManager().registerEvents(new GameListener(this), this);
        Bukkit.getPluginManager().registerEvents(new UIListener(this), this);
        Bukkit.getPluginManager().registerEvents(listener, this);
        Bukkit.getPluginManager().registerEvents(new ExplodeListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new PregameListener(this), this);

        getCommand("arena").setExecutor(new ArenaCommand(this));
        getCommand("lobby").setExecutor(new LobbyCommand(this));
        getCommand("shop").setExecutor(new BWShop(this));
    }

    @Override
    public void onDisable() {
        for(Arena arena : arenaManager.getArenas()) {
            arena.sendTitle(ChatColor.RED + "GAME HAS ENDED", ChatColor.GOLD + "PLUGIN DISABLED");
        }
    }

    public static void sendCoinsMessage(Player player, int amount) {
        player.sendMessage(ChatColor.GOLD + "+" + amount + " coins");
    }

    public ConfigManager getConfigManager() {return configManager; }
    public ArenaManager getArenaManager() { return arenaManager; }

}
