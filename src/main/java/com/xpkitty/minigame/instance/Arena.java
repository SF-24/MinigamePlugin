package com.xpkitty.minigame.instance;

import com.xpkitty.minigame.GameState;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.game.BlockGame;
import com.xpkitty.minigame.instance.game.KnockoutGame;
import com.xpkitty.minigame.instance.game.PVPGame;
import com.xpkitty.minigame.instance.game.ShovelSpleef;
import com.xpkitty.minigame.kit.Kit;
import com.xpkitty.minigame.kit.KitType;
import com.xpkitty.minigame.kit.type.*;
import com.xpkitty.minigame.listener.ConnectListener;
import com.xpkitty.minigame.manager.ConfigManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.List;

public class Arena {

    private final Minigame minigame;

    private final int id;
    private final Location spawn;
    private final Location respawn;
    private final String gameName;
    public boolean ifReset;

    private GameState state;
    private final List<UUID> players;
    private final HashMap<UUID, Kit> kits;
    private Countdown countdown;
    private Game game;
    private boolean canJoin;
    private final ConnectListener connectListener;

    public Arena(Minigame minigame, int id, Location spawn, String game, boolean ifReset, ConnectListener connectListener, Location respawn){

        this.connectListener = connectListener;
        this.minigame = minigame;

        this.id = id;
        this.spawn = spawn;
        this.respawn = respawn;
        this.gameName = game;
        this.ifReset = ifReset;

        this.state = GameState.RECRUITING;
        this.players = new ArrayList<>();
        this.kits = new HashMap<>();
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
            default:
                System.out.println("MINIGAME " + game + " DOES NOT EXIST: ERROR!");
                break;
        }
    }

    public void start() { game.start(); }

    public void reset(Boolean wait, int WaitTime) {
        if(state == GameState.LIVE || state == GameState.ENDED) {

            Location loc = ConfigManager.getLobbySpawn();

            for(UUID uuid : players) {

                Bukkit.getPlayer(uuid).setHealth(20);
                Bukkit.getPlayer(uuid).setFoodLevel(20);
                for(PotionEffect effect: Bukkit.getPlayer(uuid).getActivePotionEffects()) { Bukkit.getPlayer(uuid).removePotionEffect(effect.getType()); }
            }

            if(wait) {
                try {
                    Thread.sleep(WaitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for(UUID uuid : players) {
                Player player = Bukkit.getPlayer(uuid);
                player.teleport(loc);
                removeKit(player.getUniqueId());
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
        players.add(player.getUniqueId());
        player.teleport(spawn);
        if(getGameType().equals("PVP")) {
            player.sendMessage(ChatColor.GOLD + "Choose your kit with /arena kit");
            setKit(player.getUniqueId(), KitType.PVP_DEFAULT);
        } else if(getGameType().equals("SHOVELSPLEEF")) {
            player.sendMessage(ChatColor.GOLD + "Choose your kit with /arena kit");
            setKit(player.getUniqueId(), KitType.SPLEEF_DEFAULT);
        }

        if(state.equals(GameState.RECRUITING) && players.size() >= ConfigManager.getRequiredPlayers()) {
            countdown.start();
        }
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        player.teleport(ConfigManager.getLobbySpawn());
        player.sendTitle("","");

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
    public World getWorld() { return spawn.getWorld(); }

    public GameState getState() { return state; }
    public List<UUID> getPlayers() { return players; }

    public Game getGame(){ return game;}
    public boolean canJoin() { return canJoin; }
    public void toggleCanJoin() { this.canJoin = !this.canJoin; System.out.println("can join: " + canJoin); }

    public void setState(GameState state) { this.state = state; }
    public HashMap<UUID, Kit> getKits() { return kits; }

    public String getGameType() { return gameName; }
    public boolean getReset() { return ifReset; }

    public Location getLocation() {return spawn;}
    public Location getRespawn() { return respawn; }

    public void removeKit(UUID uuid) {
        if(kits.containsKey(uuid)) {
            kits.get(uuid).remove();
            kits.remove(uuid);
        }
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
}