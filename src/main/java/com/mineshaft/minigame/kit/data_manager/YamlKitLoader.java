package com.mineshaft.minigame.kit.data_manager;

import com.mineshaft.mineshaftapi.util.Logger;
import com.mineshaft.minigame.Minigame;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class YamlKitLoader {

    public YamlKitLoader() {
        initialiseFiles();
    }

    public void initialiseFiles() {
        File folder = new File(Minigame.getInstance().getDataFolder() + File.separator + "Kits");
        if(!folder.exists()){
            folder.mkdirs();
        }

        if(folder.listFiles()==null || folder.listFiles().length==0) {
            createDemoKit();
        }

        for(File file : Objects.requireNonNull(folder.listFiles())) {
            initialiseKit(file);
        }
    }

    private void initialiseKit(File fileYaml) {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        HashMap<String, Integer> items = new HashMap<>();
        HashMap<EquipmentSlot, String> equipment = new HashMap<>();

        for(String element : yamlConfiguration.getConfigurationSection("items").getKeys(false)) {
            items.put(element, yamlConfiguration.getInt("items." + element));
        }

        for(String element : yamlConfiguration.getConfigurationSection("equipment").getKeys(false)) {
            switch (element) {
                case "helmet"-> equipment.put(EquipmentSlot.HEAD,yamlConfiguration.getString("items." + element));
                case "chestplate"-> equipment.put(EquipmentSlot.BODY,yamlConfiguration.getString("items." + element));
                case "leggings"-> equipment.put(EquipmentSlot.LEGS,yamlConfiguration.getString("items." + element));
                case "boots"-> equipment.put(EquipmentSlot.FEET,yamlConfiguration.getString("items." + element));
                case "mainhand"-> equipment.put(EquipmentSlot.HAND,yamlConfiguration.getString("items." + element));
                case "offhand"-> equipment.put(EquipmentSlot.OFF_HAND,yamlConfiguration.getString("items." + element));
            }
        }

        String fileName = fileYaml.getName();
        Minigame.getInstance().getKitCache().cacheKit(
                fileName.substring(0, fileName.lastIndexOf(".")),
                new DynamicKit(fileName.substring(0, fileName.lastIndexOf(".")),yamlConfiguration.getInt("cost"),yamlConfiguration.getStringList("games"),yamlConfiguration.getString("icon"),items,equipment)
        );
        Logger.logInfo("Kit loaded " + fileName);
    }

    private void createDemoKit() {
        String dir = Minigame.getInstance().getDataFolder() + File.separator + "Kits";
        File fileYaml = new File(dir, "demo-kit.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        yamlConfiguration.set("icon","minecraft:iron_sword");

        yamlConfiguration.set("equipment", Map.of(
                "helmet","minecraft:leather_helmet",
                "chestplate","minecraft:leather_chestplate",
                "leggings","minecraft:leather_leggings",
                "boots","minecraft:leather_boots",
                "offhand","minecraft:shield"
        ));

        yamlConfiguration.set("items", Map.of(
                "minecraft:iron_sword",1,
                "minecraft:golden_apple",2,
                "minecraft:cooked_chicken",10
        ));

        yamlConfiguration.set("games", List.of("PVP"));

        yamlConfiguration.set("cost",0);
        try {
            yamlConfiguration.save(fileYaml);
            Logger.logInfo("Created demo kit");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
