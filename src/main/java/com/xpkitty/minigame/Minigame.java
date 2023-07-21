// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

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

    public static void giveLobbyItems(Player player) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();
        assert compassMeta != null;
        compassMeta.setDisplayName(ChatColor.WHITE + "Game Selector");
        compassMeta.setLocalizedName("lobby_game_selector");
        compassMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Right click when in hand to open game selection"));
        compass.setItemMeta(compassMeta);

        ItemStack shop = new ItemStack(Material.EMERALD);
        ItemMeta shopMeta = shop.getItemMeta();
        assert shopMeta != null;
        shopMeta.setDisplayName(ChatColor.WHITE + "Shop");
        shopMeta.setLocalizedName("lobby_shop");
        shopMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Right click when in hand to open shop"));
        shop.setItemMeta(shopMeta);

        ItemStack profile = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta profileMeta = (SkullMeta) profile.getItemMeta();
        assert profileMeta != null;
        profileMeta.setOwningPlayer(player);
        profileMeta.setDisplayName(ChatColor.WHITE + "Profile");
        profileMeta.setLocalizedName("lobby_profile");
        profileMeta.setLore(Collections.singletonList(ChatColor.GRAY + "Right click when in hand to open profile"));
        profile.setItemMeta(profileMeta);

        player.getInventory().clear();
        player.getInventory().setItem(0,compass);
        player.getInventory().setItem(4,shop);
        player.getInventory().setItem(8,profile);
    }

    public ConfigManager getConfigManager() {return configManager; }
    public ArenaManager getArenaManager() { return arenaManager; }

}
