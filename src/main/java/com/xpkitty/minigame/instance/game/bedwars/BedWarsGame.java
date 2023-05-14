package com.xpkitty.minigame.instance.game.bedwars;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.instance.Game;
import com.xpkitty.minigame.instance.KillReason;
import com.xpkitty.minigame.instance.team.Team;
import com.xpkitty.minigame.listener.ConnectListener;
import com.xpkitty.minigame.manager.ConfigManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class BedWarsGame extends Game {
    private HashMap<Team, BedLocation> beds;
    private List<UUID> alivePlayers;
    private HashMap<Team, Boolean> bedsAlive;

    int worldYStart = 0;

    public BedWarsGame(Minigame minigame, Arena arena, ConnectListener connectListener, HashMap<Team, BedLocation> beds) {
        super(minigame, arena, connectListener);
        this.beds=beds;
        alivePlayers=new ArrayList<>();
        bedsAlive=new HashMap<>();
    }


    @Override
    public void onStart() {
        for(UUID uuid : arena.getPlayers()) {
            alivePlayers.add(uuid);

            // put players not in teams, in teams
            if(!arena.isPlayerInTeam(Bukkit.getPlayer(uuid))) {
                for(Team team : arena.getTeamsList()) {
                    if(!arena.isTeamFull(team,Bukkit.getPlayer(uuid),false)) {
                        arena.setTeam(Bukkit.getPlayer(uuid),team);
                    }
                }
            }

            // spawn team beds
            for(Team team : arena.getTeamsList()) {
                BedLocation location = arena.getBeds().get(team);
                Block block = location.getBlock();
                for(Bed.Part part : Bed.Part.values()) {
                    block.setBlockData(Bukkit.createBlockData(team.getBed(), (data) ->  {
                        ((Bed) data).setPart(part);
                        ((Bed) data).setFacing(location.getFacing());
                    }));
                    block.setMetadata("team", new FixedMetadataValue(minigame, team.name()));
                    block = block.getRelative(location.getFacing().getOppositeFace());
                }
            }

            // player variable
            Player player = Bukkit.getPlayer(uuid);

            // send empty title
            player.sendTitle("","");

            // tp player to their base and set gamemode to survival mode
            player.teleport(arena.getTeamSpawn(arena.getPlayerTeam(player)));
            player.setGameMode(GameMode.SURVIVAL);

            // give equipment and clear ender chest
            giveEquipment(player);
            player.getEnderChest().clear();

            // send info note on BedWars
            arena.sendMessage("");
            arena.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "BED WARS: GAME HAS STARTED");
            arena .sendMessage("");
            arena.sendMessage(ChatColor.GREEN + "Protect your bed and destroy enemy beds.");
            arena.sendMessage(ChatColor.GREEN + "Upgrade yourself and your team by collecting Iron, Gold, Emerald and Diamond from generators to access powerful upgrades.");
            arena.sendMessage(ChatColor.GREEN + "The last team standing wins. Good luck.");
            arena.sendMessage("");
        }

        // SETUP BEDS HASHMAP
        for(Team team:arena.getTeamsList()) {
            bedsAlive.put(team,true);
        }

        // setup runnable tasks

        // respawn player when below certain height
        worldYStart=getWorldYStart();
        tasks.add(Bukkit.getScheduler().runTaskTimer(minigame, () -> {
            for(UUID uuid : alivePlayers) {
                if(Bukkit.getPlayer(uuid).getLocation().getY() <= worldYStart-10) {
                    killPlayer(Bukkit.getPlayer(uuid),KillReason.VOID_FALL,"");
                }
            }
        }, 4, 4));


    }

    public ItemStack getUnbreakableItem(Material material) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        stack.setItemMeta(meta);
        return stack;
    }

    // get unbreakable died armor item stack
    public ItemStack getUnbreakableDiedArmour(Material material, Player player) {
        ItemStack stack = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.setColor(arena.getPlayerTeam(player).getColour());
        stack.setItemMeta(meta);
        return stack;
    }

    // function that resets player stuff. on death or on start.
    public void giveEquipment(Player player) {
        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getInventory().addItem(getUnbreakableItem(Material.WOODEN_SWORD));
        player.getInventory().setHelmet(getUnbreakableDiedArmour(Material.LEATHER_HELMET,player));
        player.getInventory().setChestplate(getUnbreakableDiedArmour(Material.LEATHER_CHESTPLATE,player));
        player.getInventory().setLeggings(getUnbreakableDiedArmour(Material.LEATHER_LEGGINGS,player));
        player.getInventory().setBoots(getUnbreakableDiedArmour(Material.LEATHER_BOOTS,player));

        for(PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }

    int getWorldYStart() {
        ArrayList<Location> corners = ConfigManager.getCorners(arena.getId());

            System.out.println(corners);

        Location corner2 = corners.get(1);
        Location corner1 = corners.get(0);
        if (corner1.getY() < corner2.getY()) {
            return (int) corner1.getY();
        } else {
            return (int) corner2.getY();
        }
    }

    void killPlayer(Player player, KillReason killReason, String killerNameString) {
        boolean finalKill = false;
        String playerNameString = arena.getPlayerTeam(player).getColorCode() + player.getDisplayName() + " ";
        String finalKillString = "";
        if(!bedsAlive.containsKey(arena.getPlayerTeam(player))) {
            finalKill = true;
            finalKillString = ChatColor.GOLD.toString() + ChatColor.BOLD + " FINAL KILL";
        }

        if(killReason.equals(KillReason.VOID_FALL)) {
            arena.sendMessage(playerNameString + ChatColor.WHITE + "fell into the void." + finalKillString);
        } else if(killReason.equals(KillReason.PLAYER_KILL)) {
            arena.sendMessage(playerNameString + ChatColor.WHITE + "was sent to Davey Jones locker by " + arena.getPlayerTeam(Bukkit.getPlayer(killerNameString)).getColorCode() + killerNameString + finalKillString);
        } else if(killReason.equals(KillReason.ENTITY_KILL)) {
            arena.sendMessage(playerNameString + ChatColor.WHITE + "fell in battle to " + killerNameString + finalKillString);
        } else if(killReason.equals(KillReason.GENERIC_REASON)) {
            arena.sendMessage(playerNameString + ChatColor.WHITE + "died." + finalKillString);
        }

        if(killReason.equals(KillReason.PLAYER_KILL)) {
            for(ItemStack stack : player.getInventory().getContents()) {
                List<Material> giveOnDeath = Arrays.asList(Material.IRON_INGOT, Material.GOLD_INGOT,Material.DIAMOND,Material.EMERALD);
                boolean droppedLoot = false;
                if(giveOnDeath.contains(stack)) {
                    Bukkit.getPlayer(killerNameString).getInventory().addItem(stack);
                    droppedLoot=true;
                }
                if(droppedLoot) {
                    Bukkit.getPlayer(killerNameString).sendMessage(ChatColor.GREEN + "You killed " + playerNameString + ChatColor.WHITE + " and got some loot.");
                }
            }
        }

        player.getInventory().clear();
        player.setFallDistance(0f);

        if(!finalKill) {
            // if is not final kill, respawn player
            giveEquipment(player);
            player.sendMessage(ChatColor.RED + "RESPAWN");
            player.teleport(arena.getTeamSpawn(arena.getPlayerTeam(player)));
        } else {
            player.setGameMode(GameMode.SPECTATOR);
            player.getInventory().clear();
            player.setHealth(20);
            player.setFoodLevel(20);
            player.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "FINAL KILL! " + ChatColor.WHITE + "You have been eliminated");
        }
    }

    public boolean destroyBed(Team team, Player player) {
        if(arena.getPlayerTeam(player).equals(team)) { return true; }
        arena.sendMessage(team.getColorCode()+team.getName() + ChatColor.WHITE + " bed has been destroyed by " + arena.getPlayerTeam(player).getColorCode() + player.getDisplayName());

        bedsAlive.remove(team);

        for(UUID uuid : arena.getPlayers()) {
            Player player1 = Bukkit.getPlayer(uuid);
            if(arena.getPlayerTeam(player1).equals(team)) {
                player1.sendTitle(ChatColor.RED + "BED DESTROYED","");
                player1.playSound(player1.getLocation(),Sound.ENTITY_ENDER_DRAGON_GROWL,1.0f,1.0f);
            } else {
                player1.playSound(player1.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f);
            }
        }

        return false;
    }

    @Override
    public boolean isTeamGame() {
        return true;
    }












    // Listener

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        ArrayList<Material> bedTypes = new ArrayList<>();
        for(Team team : Team.values()) {
            bedTypes.add(team.getBed());
        }

        if(bedTypes.contains(e.getBlock().getType()) && e.getBlock().hasMetadata("team")) {
            Team team = Team.valueOf(e.getBlock().getMetadata("team").get(0).asString());
            e.setCancelled(destroyBed(team,e.getPlayer()));
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            if(alivePlayers.contains(player) && player.getHealth()<=0) {
                if(e.getDamager()instanceof Player) {
                    killPlayer(player, KillReason.PLAYER_KILL, e.getDamager().getName());
                } else {
                    killPlayer(player, KillReason.ENTITY_KILL, e.getDamager().getType().getName());
                }
            }
        }
    }
}
