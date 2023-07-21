// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.ui.game_select;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum GameCategories {

    ALL(13, ChatColor.AQUA + "All Games", Material.DIAMOND, "All available games"),
    PRACTICE(11,"Practice", Material.IRON_SWORD, "Practice your PvP here"),
    MINIGAMES(15,"Minigames", Material.REDSTONE, "BedWars, Spleef and more");

    private final String description;
    private final Material icon;
    private final String name;
    private final int gameSelectorSlot;

    GameCategories(int gameSelectorSlot, String name, Material icon, String description) {
        this.name=name;
        this.icon=icon;
        this.description=description;
        this.gameSelectorSlot=gameSelectorSlot;
    }

    public int getGameSelectorSlot() {return gameSelectorSlot;}
    public String getName() {return name;}
    public Material getIcon() {return icon;}
    public String getDescription() {return description;}
}
