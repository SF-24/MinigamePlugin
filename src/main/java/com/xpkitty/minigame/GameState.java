package com.xpkitty.minigame;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum GameState {

    RECRUITING(ChatColor.GREEN + "Recruiting", Material.LIME_WOOL),
    COUNTDOWN(ChatColor.YELLOW + "Countdown", Material.YELLOW_WOOL),
    LIVE(ChatColor.RED + "Started", Material.RED_WOOL),
    ENDED(ChatColor.GREEN + "Ended, recruiting soon", Material.PINK_WOOL);

    private final Material icon;
    private final String displayName;

    GameState(String displayName, Material icon) {
        this.displayName = displayName;
        this.icon=icon;
    }

    public Material getIcon() {return icon;}
    public String getDisplayName() {return displayName;}
}
