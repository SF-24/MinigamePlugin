package com.xpkitty.minigame.instance;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.kit.KitType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class PlayerDataSave {

    private final Minigame minigame;
    private final Player player;

    public HashMap<UUID, File> fileMap = new HashMap<>();
    public HashMap<UUID, YamlConfiguration> modifyFileMap = new HashMap<>();

    public PlayerDataSave(Player player, Minigame minigame) {
        this.player = player;
        this.minigame = minigame;

        try {
            initiateFile(player.getUniqueId() + ".yml", minigame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initiateFile(String name, Minigame minigame) throws Exception {

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
    }

    public int getCoins(Player player, String game) {
        YamlConfiguration modifyFile = getModifyLocation(player);
        game = game.toUpperCase(Locale.ROOT);

        if(modifyFile.contains("coins."+game)) {
            return modifyFile.getInt("coins." + game.toUpperCase(Locale.ROOT));
        }
        return 0;
    }

    public void addPoints(Player player, String dir, String type, int amount) {
        File file = getFile(player);
        YamlConfiguration modifyFile = getModifyLocation(player);

        if(file != null && modifyFile != null) {
            String directory = dir + "." + type;
            if(!modifyFile.contains(dir)) {
                modifyFile.createSection(dir);
            }
            if(!modifyFile.contains(directory)) {
                modifyFile.createSection(directory);
                modifyFile.set(directory, 0);
            }
            int points = modifyFile.getInt(directory);
            modifyFile.set(directory, points + amount);
            try {
                modifyFile.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public YamlConfiguration getModifyLocation(Player player) {
        if(modifyFileMap.containsKey(player.getUniqueId())) {
            YamlConfiguration toReturn = modifyFileMap.get(player.getUniqueId());
            return toReturn;
        }
        return null;
    }

    public File getFile(Player player) {
        if(fileMap.containsKey(player.getUniqueId())) {
            File toReturn = fileMap.get(player.getUniqueId());
            return toReturn;
        }
        return null;
    }

    public boolean getKitOwnershipStatus(String kitName, Player player) {


        YamlConfiguration modifyFile = getModifyLocation(player);
        kitName = kitName.toLowerCase(Locale.ROOT);


        if(!modifyFile.contains("kits." + kitName)) {
            return false;
        }
        return modifyFile.getBoolean("kits." + kitName);
    }
}
