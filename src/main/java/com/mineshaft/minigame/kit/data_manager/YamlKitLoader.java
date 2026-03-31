package com.mineshaft.minigame.kit.data_manager;

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
            createDemoEvent();
        }

        for(File file : Objects.requireNonNull(folder.listFiles())) {
            initialiseKit(null,file.getName());
        }
    }

    private void initialiseKit(String path, String fileName) {
        String dir = Minigame.getInstance().getDataFolder() + File.separator + "Kits";
        if(path!=null&&!path.isEmpty()) {dir+=File.separator+path;}
        File fileYaml = new File(dir, fileName);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fileYaml);

        HashMap<String, Integer> items = new HashMap<>();
        HashMap<EquipmentSlot, String> equipment = new HashMap<>();

        Map<?,?> itemsRaw = (Map<?, ?>) yamlConfiguration.getMapList("items");
        for(Map.Entry<?,?> entry : itemsRaw.entrySet()) {
            items.put(entry.getKey().toString(), Integer.parseInt(entry.getValue().toString()));
        }

        Map<?,?> equipmentRaw = (Map<?, ?>) yamlConfiguration.getMapList("equipment");
        for(Map.Entry<?,?> entry : itemsRaw.entrySet()) {
            switch (entry.getKey().toString()) {
                case "helmet"-> equipment.put(EquipmentSlot.HEAD,entry.getValue().toString());
                case "chestplate"-> equipment.put(EquipmentSlot.BODY,entry.getValue().toString());
                case "leggings"-> equipment.put(EquipmentSlot.LEGS,entry.getValue().toString());
                case "boots"-> equipment.put(EquipmentSlot.FEET,entry.getValue().toString());
                case "mainhand"-> equipment.put(EquipmentSlot.HAND,entry.getValue().toString());
                case "offhand"-> equipment.put(EquipmentSlot.OFF_HAND,entry.getValue().toString());
            }
        }

        Minigame.getInstance().getKitCache().cacheKit(
                fileName.substring(0, fileName.lastIndexOf(".")),
                new DynamicKit(fileName.substring(0, fileName.lastIndexOf(".")),yamlConfiguration.getInt("cost"),yamlConfiguration.getStringList("games"),yamlConfiguration.getString("icon"),items,equipment)
        );
    }

    private void createDemoEvent() {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
