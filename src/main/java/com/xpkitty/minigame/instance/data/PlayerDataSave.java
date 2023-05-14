package com.xpkitty.minigame.instance.data;

import com.xpkitty.minigame.Minigame;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Locale;

public class PlayerDataSave {

    public PlayerJsonDataSave playerJsonDataSave;

    public PlayerDataSave(Player player, Minigame minigame) {
        playerJsonDataSave = new PlayerJsonDataSave(player,minigame);
    }

    /*private void initiateFile(String name, Minigame minigame) throws Exception {

        String path = "plugins" + File.separator + "Minigame" + File.separator + "PlayerData";
        System.out.println("PATH: " + path);

        File file = new File(path, name);

        if(!file.exists()) {
            File dir = file.getParentFile();
            dir.mkdirs();
            file.createNewFile();
        }

        YamlConfiguration modifyFile = YamlConfiguration.loadConfiguration(file);

        if(modifyFile.contains("coins.SPLEEF")) {
            if(modifyFile.getInt("coins.SPLEEF") > 0) {
                if(!modifyFile.contains("coins.SHOVELSPLEEF")) {
                    modifyFile.set("coins.SHOVELSPLEEF", modifyFile.getInt("coins.SPLEEF"));
                    modifyFile.set("coins.SPLEEF",0);
                } else {
                    Integer coinsShovelSpleef = modifyFile.getInt("coins.SPLEEF") + modifyFile.getInt("coins.SHOVELSPLEEF");
                    modifyFile.set("coins.SPLEEF",0);
                    modifyFile.set("coins.SHOVELSPLEEF",coinsShovelSpleef);
                }
            }
            modifyFile.save(file);
        }

        if(!modifyFile.contains("player-name")) {
            modifyFile.createSection("player-name");
            modifyFile.set("player-name", player.getDisplayName());
        }

        if(!modifyFile.contains("kits")) {
            modifyFile.createSection("kits");
        }
        for (KitType kit : KitType.values()) {
            String kitName = kit.name().toLowerCase(Locale.ROOT);

            if(!modifyFile.contains("kits."+kitName)) {
                modifyFile.createSection("kits."+kitName);
                if(kit.getPrice()>0) {
                    modifyFile.set("kits."+kitName,false);
                } else {
                    modifyFile.set("kits."+kitName,true);
                }

            } else {
                if(kit.getPrice()<=0 && !modifyFile.getBoolean("kits." + kitName)) {
                    modifyFile.set("kits."+kitName,true);
                }
            }
        }

        fileMap.put(player.getUniqueId(), file);
        modifyFileMap.put(player.getUniqueId(), modifyFile);
        modifyFile.save(file);
    }*/

    public int getCoins(Player player, String game) {
        return playerJsonDataSave.getCoins(player,game);
    }

    public void addPoints(Player player, String type, int amount) {
        playerJsonDataSave.addCoins(player,type,amount);
    }


    public boolean getKitOwnershipStatus(String kitName, Player player) {
        return playerJsonDataSave.getKitOwnershipStatus(kitName,player);
    }
}
