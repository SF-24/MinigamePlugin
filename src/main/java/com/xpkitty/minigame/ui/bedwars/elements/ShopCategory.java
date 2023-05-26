package com.xpkitty.minigame.ui.bedwars.elements;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum ShopCategory {
    QUICK_BUY(Material.NETHER_STAR,ChatColor.AQUA+"Quick Buy"),
    BLOCKS(Material.END_STONE, ChatColor.WHITE+"Blocks"),
    MELEE(Material.IRON_SWORD, ChatColor.WHITE+"Melee"),
    ARMOUR(Material.IRON_CHESTPLATE, ChatColor.WHITE+"Armour"),
    TOOLS(Material.DIAMOND_PICKAXE, ChatColor.WHITE+"Tools"),
    RANGED(Material.BOW, ChatColor.WHITE+"Ranged"),
    POTIONS(Material.POTION,ChatColor.WHITE+"Potions"),
    UTILITY(Material.FIRE_CHARGE, ChatColor.WHITE+"Utility");

    private final Material icon;
    private final String name;

    ShopCategory(Material icon, String name) {
        this.icon=icon;
        this.name=name;
    }

    public Material getIcon() {return icon;}
    public String getName() {return name;}
}
