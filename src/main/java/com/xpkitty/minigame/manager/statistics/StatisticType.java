// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.manager.statistics;

import org.bukkit.ChatColor;

public enum StatisticType {

    WINS("Wins", ChatColor.GREEN.toString()),
    KILLS("Kills", ChatColor.AQUA.toString()),
    FINAL_KILLS("Final kills", ChatColor.RED.toString()),
    BEDS_BROKEN("Beds Broken", ChatColor.YELLOW.toString());

    private final String name;
    private final String colorCode;

    StatisticType(String name, String colorCode) {
        this.name=name;
        this.colorCode = colorCode;
    }

    public String getName() {return name;}

    public String getColor() {
        return colorCode;
    }
}
