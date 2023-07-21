// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.instance;

import com.xpkitty.minigame.manager.statistics.StatisticType;
import com.xpkitty.minigame.ui.game_select.GameCategories;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.xpkitty.minigame.manager.statistics.StatisticType.KILLS;
import static com.xpkitty.minigame.manager.statistics.StatisticType.WINS;

public enum GameType {
    BLOCK_GAME("Deprecated: Block Game", Material.OAK_PLANKS, false, false, null, null),
    PVP("PvP", Material.IRON_SWORD, false, true, GameCategories.PRACTICE, Arrays.asList(WINS, KILLS)),
    SHOVELSPLEEF("Shovel Spleef", Material.DIAMOND_SHOVEL,false, true, GameCategories.MINIGAMES, Collections.singletonList(WINS)),
    KNOCKOUT("Knockout", Material.STICK,false, false, GameCategories.PRACTICE, Collections.singletonList(WINS)),
    BEDWARS("BedWars",Material.RED_BED,true, false, GameCategories.MINIGAMES, Arrays.asList(WINS,KILLS,StatisticType.FINAL_KILLS,StatisticType.BEDS_BROKEN));

    private final boolean isTeamGame;
    private final String displayName;
    private final GameCategories category;
    private final boolean hasKits;
    private final Material icon;
    private final List<StatisticType> statistics;

    GameType(String displayName, Material icon, boolean isTeamGame, boolean hasKits, GameCategories category, List<StatisticType> statistics) {
        this.isTeamGame=isTeamGame;
        this.displayName = displayName;
        this.hasKits=hasKits;
        this.category=category;
        this.icon=icon;
        this.statistics=statistics;
    }

    public List<StatisticType> getStatistics() {return statistics;}
    public Material getIcon() {return icon;}
    public GameCategories getCategory() {return category;}
    public boolean hasKits() {return hasKits;}
    public String getDisplayName() {return displayName;}
    public boolean isTeamGame() {return isTeamGame;}
}
