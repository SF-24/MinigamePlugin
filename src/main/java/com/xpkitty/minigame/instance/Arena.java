package com.xpkitty.minigame.instance;

import com.xpkitty.minigame.GameState;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.game.BlockGame;
import com.xpkitty.minigame.instance.game.KnockoutGame;
import com.xpkitty.minigame.instance.game.PVPGame;
import com.xpkitty.minigame.instance.game.ShovelSpleef;
import com.xpkitty.minigame.instance.game.bedwars.BedLocation;
import com.xpkitty.minigame.instance.game.bedwars.BedWarsGame;
import com.xpkitty.minigame.instance.team.Team;
import com.xpkitty.minigame.kit.Kit;
import com.xpkitty.minigame.kit.KitType;
import com.xpkitty.minigame.kit.type.*;
import com.xpkitty.minigame.listener.ConnectListener;
import com.xpkitty.minigame.manager.ConfigManager;
import org.bukkit.*;
import org.bukkit.block.data.type.TNT;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Arena {

    private final Minigame minigame;

    private final int id;
    private final HashMap<String,Location> spawns;
    private HashMap<String, BedLocation> beds;
    private final Location respawn;
    private final String gameName;
    public boolean ifReset;

    private HashMap<Team, Location> teamSpawns;
    private HashMap<Team, BedLocation> teamBeds;

    private GameState state;
    private final List<UUID> players;
    private final HashMap<UUID, Kit> kits;
    private HashMap<UUID, Team> teams;
    private Countdown countdown;
    private Game game;
    private boolean canJoin;
    private final ConnectListener connectListener;

    public Arena(Minigame minigame, int id, HashMap<String,Location> spawns, HashMap<String, BedLocation> beds, String game, boolean ifReset, ConnectListener connectListener, Location respawn){
        teamBeds = new HashMap<>();
        teamSpawns = new HashMap<>();

        this.connectListener = connectListener;
        this.minigame = minigame;

        this.id = id;
        this.spawns = spawns;
        this.beds=beds;
        this.respawn = respawn;
        this.gameName = game;
        this.ifReset = ifReset;

        this.state = GameState.RECRUITING;
        this.players = new ArrayList<>();
        this.kits = new HashMap<>();
        this.teams = new HashMap<>();
        this.countdown = new Countdown(minigame, this);
        /*switch (gameName) {
            case "BLOCK":
                this.game = new BlockGame(minigame, this);
                break;
            case "PVP":
                this.game = new PVPGame(minigame, this);
                break;
            default:
                System.out.println("MINIGAME " + game + " DOES NOT EXIST: ERROR!");
                break;
        }*/

        setupNewGame();
        this.canJoin = true;
    }

    /* GAME */

    private void setupNewGame() {
        switch (gameName) {
            case "BLOCK":
                this.game = new BlockGame(minigame, this, connectListener);
                break;
            case "PVP":
                this.game = new PVPGame(minigame, this, connectListener);
                break;
            case "SHOVELSPLEEF":
                this.game = new ShovelSpleef(minigame, this, connectListener);
                break;
            case "KNOCKOUT":
                this.game = new KnockoutGame(minigame, this, connectListener);
                break;
            case "BEDWARS":
                for(String name : beds.keySet()) {
                    teamBeds.put(Team.valueOf(name.toUpperCase(Locale.ROOT)), beds.get(name));
                }

                this.game = new BedWarsGame(minigame, this, connectListener,teamBeds);
            default:
                System.out.println("MINIGAME " + game + " DOES NOT EXIST: ERROR!");
                break;
        }
        if(isTeamGame()) {
            for(String name : spawns.keySet()) {
                teamSpawns.put(Team.valueOf(name.toUpperCase(Locale.ROOT)), spawns.get(name));
            }
        }
    }

    public void start() { game.start(); }

    public void reset(Boolean wait, int WaitTime) {
        if(state == GameState.LIVE || state == GameState.ENDED) {

            // get lobby spawn
            Location loc = ConfigManager.getLobbySpawn();

            // cancel all bukkit tasks
            game.cancelTasks();

            // heal players and clear effects
            for(UUID uuid : players) {

                Bukkit.getPlayer(uuid).setHealth(20);
                Bukkit.getPlayer(uuid).setFoodLevel(20);
                for(PotionEffect effect: Bukkit.getPlayer(uuid).getActivePotionEffects()) { Bukkit.getPlayer(uuid).removePotionEffect(effect.getType()); }
            }

            // wait if set
            if(wait) {
                try {
                    Thread.sleep(WaitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            // reset player stuff
            for(UUID uuid : players) {
                Player player = Bukkit.getPlayer(uuid);
                player.teleport(loc);
                removeKit(player.getUniqueId());
                removeTeam(player.getUniqueId());
            }
            players.clear();
            kits.clear();

            /*if(ifReset) {
                this.canJoin = false;
                String worldName = spawn.getWorld().getName();
                Bukkit.unloadWorld(spawn.getWorld(), false);
                World world = Bukkit.createWorld(new WorldCreator(worldName));
                world.setAutoSave(false);
            }*/

            // if game type is BedWars, reset arena
            if(getGameType().equalsIgnoreCase("BEDWARS")) {
                ArrayList<Location> corners = minigame.getConfigManager().getCorners(id);
                Location corner1 = corners.get(0);
                Location corner2 = corners.get(1);

                List<Material> blocksToRemove = new ArrayList<>();
                blocksToRemove.add(Material.OAK_PLANKS);
                blocksToRemove.add(Material.LADDER);
                blocksToRemove.add(Material.END_STONE);
                blocksToRemove.add(Material.WATER);
                blocksToRemove.add(Material.OBSIDIAN);
                blocksToRemove.add(Material.SPONGE);
                blocksToRemove.add(Material.TNT);
                for (Team team : getTeamsList()) {
                    blocksToRemove.add(team.getWool());
                    blocksToRemove.add(team.getHardClay());
                    blocksToRemove.add(team.getGlass());
                }

                int startY = 0;
                int endY = 0;
                if (corner1.getY() < corner2.getY()) {
                    startY = (int) corner1.getY();
                    endY = (int) corner2.getY();
                } else {
                    endY = (int) corner1.getY();
                    startY = (int) corner2.getY();
                }

                int startX = 0;
                int endX = 0;
                if (corner1.getY() < corner2.getX()) {
                    startX = (int) corner1.getX();
                    endX = (int) corner2.getX();
                } else {
                    endX = (int) corner1.getX();
                    startX = (int) corner2.getX();
                }

                int startZ = 0;
                int endZ = 0;
                if (corner1.getY() < corner2.getZ()) {
                    startZ = (int) corner1.getZ();
                    endZ = (int) corner2.getZ();
                } else {
                    endZ = (int) corner1.getZ();
                    startZ = (int) corner2.getZ();
                }

                for (int y = startY; y < endY; y++) {
                    for (int x = startX; x < endX; x++) {
                        for (int z = startZ; z < endZ; z++) {
                            Location blockLoc = new Location(getWorld(), x, y, z);
                            if (blocksToRemove.contains(getWorld().getBlockAt(blockLoc).getType())) {
                                getWorld().getBlockAt(blockLoc).setType(Material.AIR);
                            }
                        }
                    }
                }
            }

        }

        sendTitle("","");
        state = GameState.RECRUITING;
        countdown.cancel();
        countdown = new Countdown(minigame, this);
        game.unregister();
        setupNewGame();
    }

    /* TOOLS */

    public void sendMessage(String message){
        for (UUID uuid: players) {
            Bukkit.getPlayer(uuid).sendMessage(message);
        }
    }

    public void sendTitle(String title, String subtitle){
        for (UUID uuid: players) {
            Bukkit.getPlayer(uuid).sendTitle(title, subtitle);
        }
    }

    public void giveItem(ItemStack item) {
        for(UUID uuid: players) {
            Bukkit.getPlayer(uuid).getInventory().addItem(item);
        }
    }

    public void clearInventory() {
        for(UUID uuid: players) {
            Bukkit.getPlayer(uuid).getInventory().clear();
        }
    }

    /* PLAYERS */

    public void addPlayer(Player player) {

        //get random spawn from list
        Location location = getRandomLocation();

        if(location!=null) {
            players.add(player.getUniqueId());
            player.teleport(location);
            if (getGameType().equals("PVP")) {
                player.sendMessage(ChatColor.GOLD + "Choose your kit with /arena kit");
                setKit(player.getUniqueId(), KitType.PVP_DEFAULT);
            } else if (getGameType().equals("SHOVELSPLEEF")) {
                player.sendMessage(ChatColor.GOLD + "Choose your kit with /arena kit");
                setKit(player.getUniqueId(), KitType.SPLEEF_DEFAULT);
            }

            if (state.equals(GameState.RECRUITING) && players.size() >= ConfigManager.getRequiredPlayers()) {
                countdown.start();
            }
        } else {
            player.sendMessage(ChatColor.RED+ "spawn == null");
        }
    }

    public void removePlayer(Player player) {
        Team team = getPlayerTeam(player);
        players.remove(player.getUniqueId());

        if(teams.containsKey(player.getUniqueId())) {
            isTeamFull(team, null, true);
        }

        player.teleport(ConfigManager.getLobbySpawn());
        player.sendTitle("","");

        removeTeam(player.getUniqueId());
        removeKit(player.getUniqueId());

        for(PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setHealth(20);
        player.setFoodLevel(20);
        player.closeInventory();
        player.getInventory().clear();
        player.setVisualFire(false);
        player.setFireTicks(0);
        player.setFallDistance(0);
        System.out.println(player.getName() + " left arena " + id);

        if(state == GameState.COUNTDOWN && players.size() < ConfigManager.getRequiredPlayers()) {
            sendMessage(ChatColor.RED + "There are not enough players. Countdown stopped.");
            reset(false, 0);
            return;
        }

        if (state == GameState.LIVE && players.size() < ConfigManager.getRequiredPlayers()) {
            sendMessage(ChatColor.RED + "The game has ended as too many players have left.");
            reset(false, 0);
        }
    }

    /* INFO */

    public int getId() { return id; }
    public World getWorld() { return getRandomLocation().getWorld(); }

    public GameState getState() { return state; }
    public List<UUID> getPlayers() { return players; }

    public Game getGame(){ return game;}
    public boolean canJoin() { return canJoin; }
    public void toggleCanJoin() { this.canJoin = !this.canJoin; System.out.println("can join: " + canJoin); }

    public void setState(GameState state) { this.state = state; }
    public HashMap<UUID, Kit> getKits() { return kits; }
    public HashMap<UUID, Team> getTeams() {return teams;}

    public String getGameType() { return gameName; }
    public boolean getReset() { return ifReset; }

    public Location getRandomLocation() {

        ArrayList<Location> loc = new ArrayList<>(spawns.values());

        Random rand = new Random();
        return loc.get(rand.nextInt(loc.size()));
    }

    public HashMap<String, Location> getSpawns() { return spawns; }

    public Location getRespawn() { return respawn; }

    public void removeKit(UUID uuid) {
        if(kits.containsKey(uuid)) {
            kits.get(uuid).remove();
            kits.remove(uuid);
        }
    }

    public void removeTeam(UUID uuid) {
        if(teams.containsKey(uuid)) {
            teams.remove(uuid);
        }
    }

    public void setTeam(Player player, Team team) {
        UUID uuid = player.getUniqueId();
        removeTeam(uuid);
        teams.put(uuid,team);
    }

    public void setKit(UUID uuid, KitType type) {
        removeKit(uuid);
        if(type.equals(KitType.PVP_DEFAULT)) {
            kits.put(uuid, new DefaultPvpKit(minigame, uuid));
        } else if(type.equals(KitType.KNIGHT)) {
            kits.put(uuid, new KnightKit(minigame, uuid));
        } else if(type.equals(KitType.ARMORER)) {
            kits.put(uuid, new ArmourerKit(minigame, uuid));
        } else if(type.equals(KitType.SCOUT)) {
            kits.put(uuid, new ScoutKit(minigame, uuid));
        } else if(type.equals(KitType.SPLEEF_DEFAULT)) {
            kits.put(uuid, new DefaultSpleefKit(minigame, uuid));
        } else if(type.equals(KitType.ARCHER)) {
            kits.put(uuid, new ArcherKit(minigame, uuid));
        } else if(type.equals(KitType.SPLEEF_NOOB)) {
            kits.put(uuid, new SpleefNoobKit(minigame, uuid));
        } else if(type.equals(KitType.SPLEEF_FERRARI)) {
            kits.put(uuid, new SpleefFerrariKit(minigame, uuid));
        }
    }

    public KitType getKitType(Player player) {
        return kits.containsKey(player.getUniqueId()) ? kits.get(player.getUniqueId()).getType() : null;
    }

    public ArrayList<Team> getTeamsList() {
        if(isTeamGame()) {
            ArrayList<Team> teamList = new ArrayList<>();
            for(Team team : Team.values()) {
                teamList.add(team);
            }
            return teamList;
        }
        return null;
    }


    public int getTeamSize(Team team) {
        if(isTeamGame()) {
            if(getTeamsList().contains(team)) {
                int size = 0;
                for(Team teams : teams.values()) {
                    if(teams.equals(team)) {
                        size++;
                    }
                }
                System.out.println("team: " + team.getName() + " has " + size + " players");
                return size;
            }
            return -2;
        }
        return -1;
    }

    public boolean isPlayerInTeam(Player player) {
        return teams.containsKey(player.getUniqueId());
    }

    public Team getPlayerTeam(Player player) {
        if(teams.containsKey(player.getUniqueId())) {
            return teams.get(player.getUniqueId());
        }

        return null;
    }

    public boolean isTeamFull(Team team, Player player, boolean kickToMaintainCount) {
        if(team==null) {
            System.out.println("ERROR!!!!!! " + team + " == null");
            return true;
        }

        int playerCount = players.size();
        int teamCount = getTeamsList().size();
        int checkedTeamCount = getTeamSize(team);

        int teamDiv = playerCount / teamCount;
        int remainderDiv = playerCount % teamCount;

        if(player!=null) {
            if (!team.equals(getPlayerTeam(player)) && isPlayerInTeam(player)) {
                //remainderDiv++;
            }
        }

        System.out.println("TEAM "+ team.getName() +" COUNT: " + teamCount + "/" + teamDiv + ", " + remainderDiv);

        if(checkedTeamCount<teamDiv) {
            return false;
        } else if(checkedTeamCount>=teamDiv+teamCount) {
            return true;
        }
        // check for players over cap but included in remainder
        for(Team element : getTeamsList()) {
            int teamPlayerCount = getTeamSize(element);
            if(teamPlayerCount>teamDiv) {
                int value = teamPlayerCount-teamDiv;

                if(value>0) {
                    remainderDiv-=value;
                    if(remainderDiv<0) {
                        if (kickToMaintainCount) {
                        }
                    }
                }
                System.out.println("team name: " + element.getName());
                System.out.println("Player count: " + teamPlayerCount + "/" + teamDiv + " %% " + remainderDiv);
            }
        }
        if(remainderDiv<=0) {
            return true;
        }
        return false;
    }

    public Location getTeamSpawn(Team team) {
        if(getTeamsList().contains(team) && isTeamGame()) {
            return teamSpawns.get(team);
        }
        return null;
    }

    public HashMap<Team, BedLocation> getBeds() {
            if (getGameType().equalsIgnoreCase("BedWars")) {
                return teamBeds;
            }
            return null;
        }

    public boolean isTeamGame() {
        GameType gameType = GameType.valueOf(getGameType().toUpperCase(Locale.ROOT));

        if (gameType != null) {
            return gameType.isTeamGame();
        }
        return false;
    }

    public int getMaxPlayers() {
        return ConfigManager.getMaximumPlayers();
    }
}