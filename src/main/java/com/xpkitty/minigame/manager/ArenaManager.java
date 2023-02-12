package com.xpkitty.minigame.manager;

import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.listener.ConnectListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class ArenaManager {

    private final List<Arena> arenas = new ArrayList<>();

    ConnectListener connectListener;

    public static List<String> firstN(Map<String, Integer> map, int n) {
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
                n + 1, Map.Entry.comparingByValue()
        );

        int bound = n + 1;
        for (Map.Entry<String, Integer> en : map.entrySet()) {
            pq.offer(en);
            if (pq.size() == bound) {
                pq.poll();
            }
        }

        int i = n;
        String[] array = new String[n];
        while (--i >= 0) {
            array[i] = pq.remove().getKey();
        }
        return Arrays.asList(array);
    }

    public ArenaManager(Minigame minigame, ConnectListener connectListener) {
        this.connectListener = connectListener;
        FileConfiguration config = minigame.getConfig();
        for (String str : config.getConfigurationSection("arenas.").getKeys(false)){
            //World world = Bukkit.createWorld(new WorldCreator(config.getString("arenas." + str + ".world")));

            /*if(config.getBoolean("arenas." + str + ".reset")) {
                //world.setAutoSave(false);
            } else {
                world.setAutoSave(true);
            }*/



            arenas.add(new Arena(minigame, Integer.parseInt(str), new Location(
                    Bukkit.getWorld(config.getString("arenas." + str + ".world")),
                    config.getDouble("arenas." + str + ".x"),
                    config.getDouble("arenas." + str + ".y"),
                    config.getDouble("arenas." + str + ".z"),
                    (float) config.getDouble("arenas." + str + ".yaw"),
                    (float) config.getDouble("arenas." + str + ".pitch")),
                    config.getString("arenas." + str + ".game"),
                    config.getBoolean("arenas." + str + ".reset"),
                    connectListener,
                    new Location(
                            Bukkit.getWorld(config.getString("arenas." + str + ".world")),
                            config.getDouble("arenas." + str + ".respawn.x"),
                            config.getDouble("arenas." + str + ".respawn.y"),
                            config.getDouble("arenas." + str + ".respawn.z"),
                            (float) config.getDouble("arenas." + str + ".respawn.yaw"),
                            (float) config.getDouble("arenas." + str + ".respawn.pitch"))));
            System.out.println();
            System.out.println("Arena " + str + " initialised!");
            System.out.println();

        }
    }

    public List<Arena> getArenas() { return arenas; }

    public Arena getArena(Player player){
        for(Arena arena : arenas){
            if(arena.getPlayers().contains(player.getUniqueId())) {
                return arena;
            }
        }
        return null;
    }

    public Arena getArena(int id){
        for(Arena arena : arenas){
            if(arena.getId() == id ){
                return arena;
            }
        }
        return null;
    }

    public Arena getArena(World world) {
        String worldName = world.getName();
        String arenaWorld = "";
        for (Arena arena : arenas)
        {
            arenaWorld = arena.getWorld().getName();
            if(arenaWorld.equals(worldName)) {

                System.out.println(world.getName() + " is an arena");
                return arena;
            }
        }
        System.out.println(worldName + " has no arenas.");
        return null;
    }

}
