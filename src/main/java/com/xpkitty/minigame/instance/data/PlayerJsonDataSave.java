package com.xpkitty.minigame.instance.data;

import com.google.gson.Gson;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.kit.KitType;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
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

        return new PlayerDataClass(coinMap, map);
    }

    //loads player json settings file
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

        return pdc;
    }




    public void giveKit(Player player, KitType kitType) {
        PlayerDataClass data = loadData(minigame, player);
        data.giveKit(kitType);
        writeData(data, getFile(minigame,player));
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
                writeData(data, getFile(minigame,player));
            }
        }
    }


    public boolean getKitOwnershipStatus(String kitName, Player player) {

        kitName = kitName.toLowerCase(Locale.ROOT);
        for(KitType kitType : KitType.values()) {
            if(kitType.name().equalsIgnoreCase(kitName)) {
                return loadData(minigame, player).getKitStatus(kitType);
            }
        }

        System.out.println("[Minigame] Error getting kit status of kit: [" + kitName + "]");
        return false;
    }

}
