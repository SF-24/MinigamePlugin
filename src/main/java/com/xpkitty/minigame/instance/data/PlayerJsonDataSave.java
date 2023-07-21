// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.instance.data;

import com.google.gson.Gson;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.GameType;
import com.xpkitty.minigame.kit.KitType;
import com.xpkitty.minigame.manager.ConfigManager;
import com.xpkitty.minigame.manager.statistics.PlayerStatistics;
import com.xpkitty.minigame.manager.statistics.StatisticType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class PlayerJsonDataSave {

    private final Minigame minigame;

    public PlayerJsonDataSave(Player player, Minigame minigame) {
        this.minigame = minigame;

        try {
            initiateFile(player.getUniqueId() + ".json", minigame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initiateFile(String name, Minigame minigame) throws Exception {

        String path = "plugins" + File.separator + "Minigame" + File.separator + "PlayerData";
        System.out.println("PATH: " + path);

        File file = new File(path, name);

        if(!file.exists()) {
            makeNewFile(file);
        }
    }

    // gets player data json file
    public static File getFile(Minigame minigame, Player player) {
        UUID id = player.getUniqueId();

        String path = minigame.getDataFolder() + File.separator + "Data" + File.separator + "PlayerData";
        File file = new File(path,id + ".json");

        if(!file.exists()) {
            makeNewFile(file);
        }
        return file;
    }

    public static void makeNewFile(File file) {
        try {
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        PlayerDataClass playerDataClass = makeEmptyData();
        writeData(playerDataClass, file);
    }


    // write data to a file
    public static void writeData(PlayerDataClass settingsData, File file) {
        Writer writer = null;
        Gson gson = new Gson();

        try {
            writer = new FileWriter(file, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(writer==null) {
            System.err.println("[Minigame] ERROR! Attempted writing to file \"" + file.getName() + "\" Error! Writer == null");
            return;
        }

        //IF WRITER IS NOT NULL
        gson.toJson(settingsData, writer);
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("[Minigame] Error in lines 58 and 59 of class SettingsReader in RpgPlugin. Please report this to an administrator or developer of RpgPlugin by XpKitty. Could not flush or close writer.");
        }

        System.out.println("[Minigame] written json data of \"" + file.getName() + "\" to file");
    }

    // make empty data file
    public static PlayerDataClass makeEmptyData() {

        // add kits
        HashMap<KitType, Boolean> map = new HashMap<>();
        for(KitType element : KitType.values()) {
            boolean val = false;
            if(element.getPrice()<=0) {
                val = true;
            }

            map.put(element, val);
        }

        // add all coin types with count 0
        HashMap<CoinType, Integer> coinMap = new HashMap<>();
        for(CoinType element : CoinType.values()) {
            coinMap.put(element, 0);
        }

        HashMap<Integer, PlayerStatistics> stats = new HashMap<>();
        stats.put(ConfigManager.getCurrentSeason(), new PlayerStatistics());

        return new PlayerDataClass(coinMap, map, stats);
    }

    //loads player json data file
    public PlayerDataClass loadData(Minigame minigame, Player player) {
        File file = getFile(minigame, player);
        Gson gson = new Gson();
        Reader reader = null;

        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(reader==null) {
            System.err.println("[Minigame] Error reading file \"" + file.getName() + "\"");
            return null;
        }

        PlayerDataClass pdc = gson.fromJson(reader, PlayerDataClass.class);
        if(pdc==null) {
            System.out.println("ERROR! PlayerDataClass is null");
        }

        assert pdc != null;
        pdc.updateStatisticHashMap();

        return pdc;
    }


    // get player statistics for certain season
    public PlayerStatistics getPlayerStatisticsForSeason(Player player, int season) {
        PlayerDataClass data = loadData(minigame, player);
        if(data.getStatisticsForSeason(season) != null) {
            return data.getStatisticsForSeason(season);
        }

        return null;
    }

    public PlayerStatistics getPlayerStatisticsForLatestSeason(Player player) {
        return getPlayerStatisticsForSeason(player, ConfigManager.getCurrentSeason());
    }

    public void addStatisticForLatestSeason(GameType gameType,Player player, StatisticType statisticType) {
        PlayerDataClass data = loadData(minigame, player);
        PlayerStatistics statistics = data.getStatisticsForSeason(ConfigManager.getCurrentSeason());
        statistics.addToStatForGame(gameType,statisticType,1);
        data.updateLatestPlayerStatistics(statistics);
        saveFile(data,player);
    }

    public void saveFile(PlayerDataClass data, UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        assert player != null;
        writeData(data, getFile(minigame,player));
    }

    public void saveFile(PlayerDataClass data, Player player) {
        writeData(data, getFile(minigame,player));
    }

    public void giveKit(Player player, KitType kitType) {
        PlayerDataClass data = loadData(minigame, player);
        data.giveKit(kitType);
        saveFile(data,player);
    }


    public int getCoins(Player player, String game) {
        for(CoinType coinType : CoinType.values()) {
            if (game.equalsIgnoreCase(coinType.name())) {
                return loadData(minigame, player).getCoinCount(coinType);
            }
        }
        System.out.println("[Minigame] ERROR! Unidentified coin type. Running from PlayerJsonDataSave.java line 154. TYPE=[" + game + "]");
        return 0;
    }

    public void addCoins(Player player, String game, int amount) {
        for(CoinType coinType : CoinType.values()) {
            if (game.equalsIgnoreCase(coinType.name())) {
                PlayerDataClass data = loadData(minigame, player);
                data.addCoins(coinType, amount);
                saveFile(data,player);
            }
        }
    }


    public boolean getKitOwnershipStatus(KitType kit, Player player) {

        String kitName = kit.name();
        for(KitType kitType : KitType.values()) {
            if(kitType.name().equalsIgnoreCase(kitName)) {
                return loadData(minigame, player).getKitStatus(kitType);
            }
        }

        System.out.println("[Minigame] Error getting kit status of kit: [" + kitName + "]");
        return false;
    }

}
