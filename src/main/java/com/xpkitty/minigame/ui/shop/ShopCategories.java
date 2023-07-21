// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.ui.shop;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public enum ShopCategories {

    PVP(ChatColor.WHITE + "Duels",Material.IRON_SWORD,ChatColor.GRAY + "Free For All PVP", new ArrayList<>(Collections.singletonList("kits"))),
    SHOVELSPLEEF(ChatColor.WHITE + "Spleef",Material.DIAMOND_SHOVEL,ChatColor.GRAY + "Your favourite minecraft minigame", new ArrayList<>(Collections.singletonList("kits"))),
    KNOCKOUT(ChatColor.WHITE + "Knockout",Material.STICK,ChatColor.GRAY + "Just knock.",new ArrayList<>()),
    BEDWARS(ChatColor.WHITE + "BedWars",Material.RED_BED,ChatColor.GRAY + "Bed Destroyed!",new ArrayList<>());

    private final String display;
    private final String description;
    private final Material material;
    private final ArrayList<String> objectList;

    ShopCategories(String display, Material material, String description, ArrayList<String> objectList) {
        this.display = display;
        this.material = material;
        this.description = description;
        this.objectList = objectList;
    }

    public Material getMaterial() { return material; }
    public String getDisplay() { return display; }
    public String getDescription() { return description; }
    public ArrayList<String> getObjectList() { return objectList; }
    public boolean objectListContains(String object) {
        return objectList.contains(object);
    }
}
