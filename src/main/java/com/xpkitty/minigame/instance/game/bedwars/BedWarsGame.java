package com.xpkitty.minigame.instance.game.bedwars;

import com.xpkitty.minigame.GameState;
import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.instance.GameItemManager;
import com.xpkitty.minigame.instance.Game;
import com.xpkitty.minigame.instance.KillReason;
import com.xpkitty.minigame.instance.team.Team;
import com.xpkitty.minigame.listener.ConnectListener;
import com.xpkitty.minigame.manager.ConfigManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class BedWarsGame extends Game {
    public List<UUID> alivePlayers;
    private ArrayList<Team> bedsAlive;
    private List<UUID> tempDeadPlayers;
    private List<Team> aliveTeams;
    private HashMap<UUID, Integer> armorLevel;

    int worldYStart = 0;

    public BedWarsGame(Minigame minigame, Arena arena, ConnectListener connectListener, HashMap<Team, BedLocation> beds) {
        super(minigame, arena, connectListener);
        alivePlayers=new ArrayList<>();
        bedsAlive=new ArrayList<>();
        aliveTeams=new ArrayList<>();
        tempDeadPlayers = new ArrayList<>();
        armorLevel=new HashMap<>();
    }


    @Override
    public void onStart() {
        for(UUID uuid : arena.getPlayers()) {
            alivePlayers.add(uuid);
            armorLevel.put(uuid,0);

            // put players not in teams, in teams
            if(Bukkit.getPlayer(uuid)!=null){ if(!arena.isPlayerInTeam(Bukkit.getPlayer(uuid))) {
                for(Team team : arena.getTeamsList()) {
                    if(!arena.isTeamFull(team,Bukkit.getPlayer(uuid),false)) {
                        arena.setTeam(Bukkit.getPlayer(uuid),team);
                    }
                }
            }} else {System.out.println("[MinigamePlugin] ERROR! Player if UUID " + uuid + " is null");}


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

        }
        // END OF FOREACH PLAYER

        // spawn team beds and add teams to alive list
        for(Team team : arena.getTeamsList()) {
            aliveTeams.add(team);
            System.out.println("Alive teams" + aliveTeams);

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


        // SETUP BEDS HASHMAP
        bedsAlive.addAll(arena.getTeamsList());

        // send info note on BedWars
        arena.sendMessage("");
        arena.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "BED WARS: GAME HAS STARTED");
        arena .sendMessage("");
        arena.sendMessage(ChatColor.GREEN + "Protect your bed and destroy enemy beds.");
        arena.sendMessage(ChatColor.GREEN + "Upgrade yourself and your team by collecting Iron, Gold, Emerald and Diamond from generators to access powerful upgrades.");
        arena.sendMessage(ChatColor.GREEN + "The last team standing wins. Good luck.");
        arena.sendMessage("");

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

        // setup generators
        for(Generator generator : ConfigManager.getGenerators(arena.getId())) {
            if(generator.getType().equals(GeneratorType.BASE)) {
                int count = arena.getPlayers().size();

                if(count==1) {
                    generator.setType(GeneratorType.BASE_SOLO);
                } else if(count==2) {
                    generator.setType(GeneratorType.BASE_DOUBLE);
                } else {
                    generator.setType(GeneratorType.BASE_TEAM);
                }
            }

            tasks.add(Bukkit.getScheduler().runTaskTimer(minigame, () -> {
                for(int i = 0; i < generator.getType().getDropCount(); i++) {
                    List<Material> loot = generator.getType().getLootTable();
                    Random random = new Random();
                    generator.getWorld().dropItem(new Location(generator.getWorld(),generator.getX(),generator.getY(),generator.getZ()),new ItemStack(loot.get(random.nextInt(loot.size()))));
                }
            }, generator.getType().getInterval(), generator.getType().getInterval()));
        }
    }

    // moved to different class
    /*public ItemStack getUnbreakableItem(Material material) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        if(material.equals(Material.WOODEN_AXE)||material.equals(Material.WOODEN_PICKAXE)||material.equals(Material.STONE_AXE)) {
            meta.addEnchant(Enchantment.DIG_SPEED,1,true);
        }
        if(material.equals(Material.IRON_AXE)||material.equals(Material.IRON_PICKAXE)) {
            meta.addEnchant(Enchantment.DIG_SPEED,2,true);
        }
        if(material.equals(Material.GOLDEN_PICKAXE)) {
            meta.addEnchant(Enchantment.DAMAGE_ALL,2,true);
        }
        if(material.equals(Material.DIAMOND_AXE)||material.equals(Material.DIAMOND_PICKAXE)||material.equals(Material.GOLDEN_PICKAXE)) {
            meta.addEnchant(Enchantment.DIG_SPEED,3,true);
        }
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        stack.setItemMeta(meta);
        return stack;
    }*/



    // function that resets player stuff. on death or on start.
    public void giveEquipment(Player player) {
        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getInventory().addItem(GameItemManager.getUnbreakableItem(Material.WOODEN_SWORD));
        player.getInventory().setHelmet(GameItemManager.getUnbreakableDiedArmour(Material.LEATHER_HELMET,player,arena));
        player.getInventory().setChestplate(GameItemManager.getUnbreakableDiedArmour(Material.LEATHER_CHESTPLATE,player,arena));

        ItemStack leggings= GameItemManager.getUnbreakableDiedArmour(Material.LEATHER_LEGGINGS,player,arena);
        ItemStack boots= GameItemManager.getUnbreakableDiedArmour(Material.LEATHER_BOOTS,player,arena);

        int tier=armorLevel.get(player.getUniqueId());

        if(tier==1) {
            leggings= GameItemManager.getUnbreakableItem(Material.CHAINMAIL_LEGGINGS);
            boots= GameItemManager.getUnbreakableItem(Material.CHAINMAIL_BOOTS);
        } else if(tier==2) {
            leggings= GameItemManager.getUnbreakableItem(Material.IRON_LEGGINGS);
            boots= GameItemManager.getUnbreakableItem(Material.IRON_BOOTS);
        } else if(tier==3) {
            leggings= GameItemManager.getUnbreakableItem(Material.DIAMOND_LEGGINGS);
            boots= GameItemManager.getUnbreakableItem(Material.DIAMOND_BOOTS);
        }

        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);

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
        if(arena.getState().equals(GameState.LIVE)) {
            boolean finalKill = false;
            String playerNameString = arena.getPlayerTeam(player).getColorCode() + player.getDisplayName() + " ";
            String finalKillString = "";
            if (!bedsAlive.contains(arena.getPlayerTeam(player))) {
                finalKill = true;
                finalKillString = ChatColor.AQUA.toString() + ChatColor.BOLD + " FINAL KILL";
            }

            if (killReason.equals(KillReason.VOID_FALL)) {
                arena.sendMessage(playerNameString + ChatColor.GRAY + "fell into the void." + finalKillString);
            } else if (killReason.equals(KillReason.PLAYER_KILL)) {
                arena.sendMessage(playerNameString + ChatColor.GRAY + "was killed by " + arena.getPlayerTeam(Bukkit.getPlayer(killerNameString)).getColorCode() + killerNameString + ChatColor.GRAY + "." + finalKillString);
            } else if (killReason.equals(KillReason.ENTITY_KILL)) {
                arena.sendMessage(playerNameString + ChatColor.GRAY + "was killed by " + killerNameString + ChatColor.GRAY + "." + finalKillString);
            } else if (killReason.equals(KillReason.GENERIC_REASON)) {
                arena.sendMessage(playerNameString + ChatColor.GRAY + "died." + finalKillString);
            } else if (killReason.equals(KillReason.HEIGHT_FALL)) {
                arena.sendMessage(playerNameString + ChatColor.GRAY + "was a noob and took too much fall damage." + finalKillString);
            } else if (killReason.equals(KillReason.REJOIN)) {
                arena.sendMessage(playerNameString + ChatColor.GRAY + "rejoined." + finalKillString);
            }


            if (killReason.equals(KillReason.PLAYER_KILL)) {
                for (ItemStack stack : player.getInventory().getContents()) {
                    List<Material> giveOnDeath = Arrays.asList(Material.IRON_INGOT, Material.GOLD_INGOT, Material.DIAMOND, Material.EMERALD);
                    if (giveOnDeath.contains(stack)) {
                        Bukkit.getPlayer(killerNameString).getInventory().addItem(stack);
                    }
                }
            }

            List<ItemStack> itemsToGive = new ArrayList<>();

            if(player.getInventory().contains(Material.SHEARS)) {itemsToGive.add(GameItemManager.getUnbreakableItem(Material.SHEARS));}
            if(player.getInventory().contains(Material.DIAMOND_PICKAXE)) {itemsToGive.add(GameItemManager.getUnbreakableItem(Material.GOLDEN_PICKAXE));}
            if(player.getInventory().contains(Material.GOLDEN_PICKAXE)) {itemsToGive.add(GameItemManager.getUnbreakableItem(Material.IRON_PICKAXE));}
            if(player.getInventory().contains(Material.IRON_PICKAXE)) {itemsToGive.add(GameItemManager.getUnbreakableItem(Material.WOODEN_PICKAXE));}
            if(player.getInventory().contains(Material.WOODEN_PICKAXE)) {itemsToGive.add(GameItemManager.getUnbreakableItem(Material.WOODEN_PICKAXE));}
            if(player.getInventory().contains(Material.DIAMOND_AXE)) {itemsToGive.add(GameItemManager.getUnbreakableItem(Material.IRON_AXE));}
            if(player.getInventory().contains(Material.STONE_AXE)) {itemsToGive.add(GameItemManager.getUnbreakableItem(Material.WOODEN_AXE));}
            if(player.getInventory().contains(Material.IRON_AXE)) {itemsToGive.add(GameItemManager.getUnbreakableItem(Material.STONE_AXE));}
            if(player.getInventory().contains(Material.WOODEN_AXE)) {itemsToGive.add(GameItemManager.getUnbreakableItem(Material.WOODEN_AXE));}


            player.getInventory().clear();
            player.setFallDistance(0f);

            // make player spectate
            player.setGameMode(GameMode.SPECTATOR);
            player.getInventory().clear();
            player.setHealth(20);
            player.setFoodLevel(20);
            player.teleport(arena.getTeamSpawn(arena.getPlayerTeam(player)));

            if (!finalKill) {
                // if is not final kill, respawn player
                PlayerRespawnTask task = new PlayerRespawnTask(minigame, this, player, 5, itemsToGive);
                task.start();
            } else {
                // If is final kill
                arena.sendMessage(playerNameString + ChatColor.GRAY + "has died and will no longer respawn.");
                player.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "FINAL KILL! " + ChatColor.WHITE + "You will no longer respawn");
                alivePlayers.remove(player.getUniqueId());

                System.out.println("[BEDWARS] " + playerNameString + " final kill");

                // check if team is eliminated
                if (arena.getPlayersOfTeam(arena.getPlayerTeam(player), true).size() <= 0) {
                    Team team = arena.getPlayerTeam(player);
                    aliveTeams.remove(team);
                    arena.sendMessage("");
                    arena.sendMessage(ChatColor.BOLD + "TEAM ELIMINATED > " + team.getColorCode() + team.getName() + " Team " + ChatColor.RED + "has been eliminated");
                    arena.sendMessage("");
                    System.out.println("[BEDWARS] " + aliveTeams);
                }

                // test to see if one team has won
                testForWin();
            }
        } else {
            player.teleport(arena.getTeamSpawn(arena.getPlayerTeam(player)));
        }
    }

    public void testForWin() {
        if(aliveTeams.size()<=1) {
            Team winner = aliveTeams.get(0);
            giveWin(winner);
            System.out.println("[BEDWARS] " + winner.getName() + " wins");
        }
    }

    public void giveWin(Team team) {
        System.out.println("[BEDWARS] game end");

        // send victory message
        for(UUID uuid : arena.getPlayersOfTeam(team,false)) {
            Player player = Bukkit.getPlayer(uuid);

            if(player!=null) {
                player.sendTitle(ChatColor.GOLD.toString() + ChatColor.BOLD + "VICTORY!","");
            }
        }

        // send game end message
        arena.sendMessage("");
        arena.sendMessage(ChatColor.BOLD + "GAME END > " + team.getColorCode() + team.getName() + " Team " + ChatColor.GOLD + "won!");
        arena.sendMessage("");

        // update boolean
        arena.setState(GameState.ENDED);

        Bukkit.getScheduler().runTaskLater(minigame,() -> {
            for(UUID uuid : arena.getPlayers()) {
                Player player = Bukkit.getPlayer(uuid);

                if(player!=null) {
                    arena.sendMessage(ChatColor.WHITE + "You have been teleported to the lobby");
                    arena.reset(false,0);
                }
            }
        },400);
    }

    public boolean destroyBed(Team team, Player player) {
        if(arena.getPlayerTeam(player).equals(team)) { return true; }
        arena.sendMessage("");
        arena.sendMessage(ChatColor.BOLD + "BED DESTRUCTION > " + team.getColorCode()+team.getName() + ChatColor.GRAY + " bed was destroyed by " + arena.getPlayerTeam(player).getColorCode() + player.getDisplayName());
        arena.sendMessage("");
        bedsAlive.remove(team);

        for(UUID uuid : arena.getPlayers()) {
            Player player1 = Bukkit.getPlayer(uuid);
            if(arena.getPlayerTeam(player1).equals(team)) {
                player1.sendTitle(ChatColor.RED + "BED DESTROYED!","You will no longer respawn!");
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
    public void inventoryClickEvent(InventoryClickEvent e) {
        if(arena.getPlayers().contains(e.getWhoClicked().getUniqueId())) {
            if(e.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void itemDropEvent(PlayerDropItemEvent e) {
        if(arena.getPlayers().contains(e.getPlayer().getUniqueId())) {
            Item item = e.getItemDrop();
            if(item.getItemStack().getType().equals(Material.WOODEN_SWORD)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(arena.getPlayers().contains(e.getPlayer().getUniqueId())) {
            ArrayList<Material> bedTypes = new ArrayList<>();
            for (Team team : Team.values()) {
                bedTypes.add(team.getBed());
            }
            if (bedTypes.contains(e.getBlock().getType()) && e.getBlock().hasMetadata("team")) {
                Team team = Team.valueOf(e.getBlock().getMetadata("team").get(0).asString());
                e.setCancelled(destroyBed(team, e.getPlayer()));
            }
            if(!arena.getState().equals(GameState.LIVE)) {
                e.setCancelled(true);
            }
        }

    }


    @EventHandler
    public void onEntityTakeDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            if(alivePlayers.contains(player.getUniqueId()) && player.getHealth()-e.getDamage()<=0) {
                e.setCancelled(true);
                System.out.println(player.getName() + " took damage. cancelling");

                if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                    EntityDamageByEntityEvent ed = (EntityDamageByEntityEvent) e;

                    if(ed.getDamager() instanceof Player) {
                        killPlayer(player, KillReason.PLAYER_KILL, ed.getDamager().getName());
                    } else {
                        killPlayer(player, KillReason.ENTITY_KILL, ed.getDamager().getType().getName());
                    }
                    return;

                }
                if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                    killPlayer(player, KillReason.HEIGHT_FALL, "");
                    return;
                }
                killPlayer(player, KillReason.GENERIC_REASON, "");
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if(alivePlayers.contains(e.getPlayer().getUniqueId())) {
            alivePlayers.remove(e.getPlayer().getUniqueId());
            tempDeadPlayers.add(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(tempDeadPlayers.contains(e.getPlayer().getUniqueId())) {
            Player player = e.getPlayer();
            Team team = arena.getPlayerTeam(player);
            arena.addPlayer(player);

            if(bedsAlive.contains(team)) {
                //if bed is not destroyed
                killPlayer(player, KillReason.REJOIN, "");
            } else {
                //if bed is destroyed
                killPlayer(player, KillReason.REJOIN, "");
            }
        }
    }

    public void setPlayerArmorLevel(Player player, int armorLevel) {
        if(armorLevel>this.armorLevel.get(player.getUniqueId())) {
            this.armorLevel.put(player.getUniqueId(),armorLevel);
            GameItemManager.upgradeArmour(player,armorLevel,minigame);
        }
    }

    public int getPlayerArmorLevel(Player player) {
        return armorLevel.get(player.getUniqueId());
    }

    public Arena getArena() {return arena;}
}
