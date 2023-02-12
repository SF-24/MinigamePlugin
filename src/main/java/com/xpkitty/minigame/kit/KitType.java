package com.xpkitty.minigame.kit;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;

public enum KitType {

    // A LIST OF KITS FOR PVP MINIGAME
    PVP_DEFAULT(ChatColor.WHITE + "Default",
            Material.IRON_SWORD,
            "The default PVP kit", 0, "PVP"),
    KNIGHT(ChatColor.GOLD + "Knight",
            Material.GOLDEN_CHESTPLATE,
            "A kit with average armour and weapons.", 0, "PVP"),
    SCOUT(ChatColor.GREEN + "Scout",
            Material.SPLASH_POTION,
            "The best kit for fast moving", 100, "PVP"),
    ARMORER(ChatColor.WHITE + "Armourer",
            Material.IRON_CHESTPLATE,
            "The best kit for not taking damage", 100, "PVP"),
    ARCHER(ChatColor.WHITE + "Archer" ,
            Material.BOW,
           "The best ranged kit", 250, "PVP"),
    SPLEEF_DEFAULT(ChatColor.WHITE + "Default",
            Material.DIAMOND_SHOVEL,
            "The default spleef kit",0,"SHOVELSPLEEF"),
    SPLEEF_NOOB(ChatColor.YELLOW +"Noob",
            Material.WOODEN_SHOVEL,
            "The kit for players who want a challenge", 50, "SHOVELSPLEEF"),
    SPLEEF_FERRARI(ChatColor.RED + "Ferrari",
            Material.ENDER_PEARL,
            "The best kit for fast movement", 50, "SHOVELSPLEEF");

    private final String display;
    private final String description;
    private final String game;
    private final Material material;
    private final int price;

    KitType(String display, Material material, String description, int price, String game) {
        this.display = display;
        this.material = material;
        this.description = description;
        this.price = price;
        this.game = game;
    }

    public Material getMaterial() { return material; }
    public String getDisplay() { return display; }
    public String getDescription() { return description; }
    public String getGame() { return game; }
    public Integer getPrice() { return price; }

}
