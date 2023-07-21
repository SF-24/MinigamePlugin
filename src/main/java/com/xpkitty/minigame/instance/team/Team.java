// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.instance.team;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

public enum Team {
    RED("Red",ChatColor.RED.toString(), Color.RED, Material.RED_WOOL, Material.RED_STAINED_GLASS, Material.RED_TERRACOTTA, Material.RED_CONCRETE,Material.RED_BED),
    BLUE("Blue",ChatColor.BLUE.toString(), Color.BLUE, Material.BLUE_WOOL, Material.BLUE_STAINED_GLASS, Material.BLUE_TERRACOTTA, Material.BLUE_CONCRETE,Material.BLUE_BED);

    private final String name;
    private final String colorCode;
    private final Material wool,concrete,glass,hardClay;
    private final Material bed;
    private final Color color;

    Team(String name, String colorCode, Color color, Material wool, Material glass, Material hardClay, Material concrete, Material bed) {
        this.name=name;
        this.wool=wool;
        this.color=color;
        this.colorCode=colorCode;
        this.glass=glass;
        this.hardClay=hardClay;
        this.concrete=concrete;
        this.bed=bed;
    }

    public Color getColour() {return color;}
    public String getName() {return name;}
    public String getColorCode() {return colorCode;}
    public Material getWool() {return wool;}
    public Material getGlass() {return glass;}
    public Material getConcrete() {return concrete;}
    public Material getHardClay() {return hardClay;}
    public Material getBed() {return bed;}
}
