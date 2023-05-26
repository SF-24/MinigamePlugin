package com.xpkitty.minigame.instance.data;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.GameType;
import com.xpkitty.minigame.kit.KitType;
import com.xpkitty.minigame.ui.shop.ShopCategories;
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

    public int getCoins(Player player, GameType game) {
        return playerJsonDataSave.getCoins(player,game.name().toLowerCase(Locale.ROOT));
    }

    public void addPoints(Player player, GameType type, int amount) {
        playerJsonDataSave.addCoins(player,type.name().toUpperCase(Locale.ROOT),amount);
    }


    public boolean getKitOwnershipStatus(KitType kitType, Player player) {
        return playerJsonDataSave.getKitOwnershipStatus(kitType,player);
    }

    public PlayerJsonDataSave getPlayerJsonDataSave() {return playerJsonDataSave;}
}
