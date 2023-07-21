// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.instance.game.bedwars;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum GeneratorType {

    BASE(20, Arrays.asList(Material.IRON_INGOT,Material.IRON_INGOT,Material.IRON_INGOT,Material.IRON_INGOT,Material.IRON_INGOT,Material.GOLD_INGOT), 3),
    BASE_SOLO(60, Arrays.asList(Material.IRON_INGOT,Material.IRON_INGOT,Material.IRON_INGOT,Material.IRON_INGOT,Material.GOLD_INGOT), 3),
    BASE_DOUBLE(50, Arrays.asList(Material.IRON_INGOT,Material.IRON_INGOT,Material.IRON_INGOT,Material.IRON_INGOT,Material.GOLD_INGOT), 3),
    BASE_TEAM(40, Arrays.asList(Material.IRON_INGOT,Material.IRON_INGOT,Material.IRON_INGOT,Material.IRON_INGOT,Material.GOLD_INGOT), 4),
    EMERALD(1200, Collections.singletonList(Material.EMERALD), 1),
    DIAMOND(2400, Collections.singletonList(Material.DIAMOND), 1);

    private final List<Material> lootTable;
    private final int dropCount;
    private final int interval;

    GeneratorType(int interval, List<Material> lootTable, int dropCount) {
        this.interval=interval;
        this.lootTable=lootTable;
        this.dropCount=dropCount;
    }

    public int getDropCount() {return dropCount;}
    public int getInterval() {return interval;}
    public List<Material> getLootTable() {return lootTable;}
}
