package com.xpkitty.minigame.manager;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.game.bedwars.Generator;
import com.xpkitty.minigame.instance.game.bedwars.GeneratorType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Locale;

public class ConfigManager {

    private static FileConfiguration config;

    public static void setupConfig(Minigame minigame){
        ConfigManager.config = minigame.getConfig();
        minigame.saveDefaultConfig();
    }

    public static int getRequiredPlayers() { return config.getInt("required-players"); }

    public static int getMaximumPlayers() { return config.getInt("maximum-players"); }

    public static int getCountdownSeconds() { return config.getInt("countdown-seconds"); }

    public static String getMessage() { return config.getString("welcome-message"); }

    public static Location getLobbySpawn() {
        return new Location(
                Bukkit.getWorld(config.getString("lobby-spawn.world")),
                config.getDouble("lobby-spawn.x"),
                config.getDouble("lobby-spawn.y"),
                config.getDouble("lobby-spawn.z"),
                (float) config.getDouble("lobby-spawn.yaw"),
                (float) config.getDouble("lobby-spawn.pitch"));
    }

    public static ArrayList<Location> getCorners(int arenaId) {
        ArrayList<Location> corners = new ArrayList<>();
        for(int i=0; i<2; i++) {
            String v=arenaId+".corners."+i;

            Location loc = new Location(
                    Bukkit.getWorld(config.getString("arenas." + arenaId + ".world")),
                    config.getDouble("arenas." + v + ".x"),
                    config.getDouble("arenas." + v + ".y"),
                    config.getDouble("arenas." + v + ".z"));
            corners.add(loc);

            System.out.println("map corners v= " + v);
            System.out.println(loc);
        }
        return corners;
    }

    public static ArrayList<Generator> getGenerators(int arenaId) {

        ArrayList<Generator> generators = new ArrayList<>();

        for(String element : config.getConfigurationSection("arenas."+arenaId+".generators.").getKeys(false)) {
            String v = arenaId+".generators."+element;
            System.out.println("generator v " + v);

            Generator generator = new Generator(
                    Bukkit.getWorld(config.getString("arenas." + arenaId + ".world")),
                    config.getDouble("arenas." + v + ".x"),
                    config.getDouble("arenas." + v + ".y"),
                    config.getDouble("arenas." + v + ".z"),
                    GeneratorType.valueOf(config.getString("arenas." + v + ".type").toUpperCase(Locale.ROOT)));

            generators.add(generator);
        }
        return generators;
    }
}
