package com.xpkitty.minigame.kit;

import com.xpkitty.minigame.instance.GameType;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;

public enum KitType {

    // A LIST OF KITS FOR PVP MINIGAME
    PVP_DEFAULT(ChatColor.WHITE + "Default",
            Material.IRON_SWORD,
            "The default PVP kit", 0, GameType.PVP),
    KNIGHT(ChatColor.GOLD + "Knight",
            Material.GOLDEN_CHESTPLATE,
            "A kit with average armour and weapons.", 0, GameType.PVP),
    SCOUT(ChatColor.GREEN + "Scout",
            Material.SPLASH_POTION,
            "The best kit for fast moving", 100, GameType.PVP),
    ARMORER(ChatColor.WHITE + "Armourer",
            Material.IRON_CHESTPLATE,
            "The best kit for not taking damage", 100, GameType.PVP),
    ARCHER(ChatColor.WHITE + "Archer" ,
            Material.BOW,
           "The best ranged kit", 250, GameType.PVP),
    SPLEEF_DEFAULT(ChatColor.WHITE + "Default",
            Material.DIAMOND_SHOVEL,
            "The default spleef kit",0,GameType.SHOVELSPLEEF),
    SPLEEF_NOOB(ChatColor.YELLOW +"Noob",
            Material.WOODEN_SHOVEL,
            "The kit for players who want a challenge", 50, GameType.SHOVELSPLEEF),
    SPLEEF_FERRARI(ChatColor.RED + "Ferrari",
            Material.ENDER_PEARL,
            "The best kit for fast movement", 50, GameType.SHOVELSPLEEF);

    private final String display;
    private final String description;
    private final GameType game;
    private final Material material;
    private final int price;

    KitType(String display, Material material, String description, int price, GameType game) {
        this.display = display;
        this.material = material;
        this.description = description;
        this.price = price;
        this.game = game;
    }

    public Material getMaterial() { return material; }
    public String getDisplay() { return display; }
    public String getDescription() { return description; }
    public GameType getGame() { return game; }
    public Integer getPrice() { return price; }

}
