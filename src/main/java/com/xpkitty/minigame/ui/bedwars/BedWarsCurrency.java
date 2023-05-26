package com.xpkitty.minigame.ui.bedwars;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum BedWarsCurrency {

    GOLD(Material.GOLD_INGOT, ChatColor.GOLD.toString(), "Gold","Gold"),
    IRON(Material.IRON_INGOT, ChatColor.WHITE.toString(), "Iron","Iron"),
    DIAMOND(Material.DIAMOND, ChatColor.AQUA.toString(), "Diamond","Diamonds"),
    EMERALD(Material.EMERALD, ChatColor.GREEN.toString(), "Emerald","Emeralds");

    private final Material material;
    private final String colorCode;
    private final String singular;
    private final String plural;

    BedWarsCurrency(Material material, String colorCode, String singular, String plural) {
        this.material=material;
        this.colorCode = colorCode;
        this.singular=singular;
        this.plural=plural;
    }

    public Material getMaterial() {return material;}

    public String getColorCode() {
        return colorCode;
    }

    public String getPlural() {
        return plural;
    }

    public String getSingular() {
        return singular;
    }
}
