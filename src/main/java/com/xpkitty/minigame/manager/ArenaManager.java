package com.xpkitty.minigame.manager;

import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.GameType;
import com.xpkitty.minigame.instance.game.bedwars.BedLocation;
import com.xpkitty.minigame.listener.ConnectListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
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


    //initialize arenas
    public ArenaManager(Minigame minigame, ConnectListener connectListener) {
        this.connectListener = connectListener;
        FileConfiguration config = minigame.getConfig();
        for (String str : config.getConfigurationSection("arenas.").getKeys(false)){
            System.out.println("arenas." + str);

            if (Bukkit.getWorld(config.getString("arenas." + str + ".world")) != null) {
                HashMap<String, Location> spawns = new HashMap<>();
                HashMap<String, BedLocation> beds = new HashMap<>();

                if(config.contains("arenas."+str+".beds")) {
                    for(String element : config.getConfigurationSection("arenas."+str+".beds.").getKeys(false)) {
                        String v = str+".beds."+element;
                        System.out.println("spawn v " + v);

                        BedLocation bed = new BedLocation(
                                Bukkit.getWorld(config.getString("arenas." + str + ".world")),
                                config.getDouble("arenas." + v + ".x"),
                                config.getDouble("arenas." + v + ".y"),
                                config.getDouble("arenas." + v + ".z"),
                                BlockFace.valueOf(config.getString("arenas." + v + ".facing").toUpperCase(Locale.ROOT)));
                        beds.put(element.toLowerCase(Locale.ROOT),bed);
                    }
                }

                for(String element : config.getConfigurationSection("arenas."+str+".spawns.").getKeys(false)) {
                    String v = str+".spawns."+element;
                    System.out.println("spawn v " + v);

                    Location spawn = new Location(
                            Bukkit.getWorld(config.getString("arenas." + str + ".world")),
                            config.getDouble("arenas." + v + ".x"),
                            config.getDouble("arenas." + v + ".y"),
                            config.getDouble("arenas." + v + ".z"),
                            (float) config.getDouble("arenas." + v + ".yaw"),
                            (float) config.getDouble("arenas." + v + ".pitch"));
                    spawns.put(element.toLowerCase(Locale.ROOT),spawn);
                }


                Location respawn = new Location(
                        Bukkit.getWorld(config.getString("arenas." + str + ".world")),
                        config.getDouble("arenas." + str + ".respawn.x"),
                        config.getDouble("arenas." + str + ".respawn.y"),
                        config.getDouble("arenas." + str + ".respawn.z"),
                        (float) config.getDouble("arenas." + str + ".respawn.yaw"),
                        (float) config.getDouble("arenas." + str + ".respawn.pitch"));

                String gameName = config.getString("arenas." + str + ".game");
                GameType gameType = GameType.valueOf(gameName.toUpperCase(Locale.ROOT));

                arenas.add(new Arena(minigame, Integer.parseInt(str), spawns, beds,
                        gameType,
                        config.getBoolean("arenas." + str + ".reset"),
                        connectListener,
                        respawn));

                System.out.println();
                System.out.println("Arena " + str + " initialised!");
                System.out.println("Spawn loc. " + spawns);
                System.out.println("Respawn loc. " + respawn);
                System.out.println();

            } else {
                System.out.println("");
                System.out.println("");
                System.out.println("ERROR! SPAWN WORLD OF " + config.getString("arenas." + str + ".world") + " I NULL!");
                System.out.println("");
                System.out.println("");
            }
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

    public void addPlayerToArena(Player player, int arena) {
        if(getArena(player)!=null) {
            getArena(player).removePlayer(player);
        }
        getArena(arena).addPlayer(player);
        player.sendMessage(ChatColor.GREEN + "Joining arena " + arena);
    }

}
