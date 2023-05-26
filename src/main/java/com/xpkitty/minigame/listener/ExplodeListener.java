package com.xpkitty.minigame.listener;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.Arena;
import com.xpkitty.minigame.instance.team.Team;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExplodeListener implements Listener {
    Minigame minigame;

    public ExplodeListener(Minigame minigame) {
        this.minigame=minigame;
    }

    @EventHandler
    void ExplodeEvent(EntityExplodeEvent e) {
        for(Arena arena : minigame.getArenaManager().getArenas()) {
            if(arena.getGameType().equalsIgnoreCase("bedwars")&& e.getLocation().getWorld().getName().equals(arena.getWorld().getName())) {
                List<Block> blockList = e.blockList();
                List<Material> explodeBlacklist = Arrays.asList(Material.POLISHED_ANDESITE, Material.SMOOTH_STONE_SLAB,Material.SANDSTONE,Material.SANDSTONE_STAIRS,Material.BLACK_WOOL,Material.GRAY_WOOL,Material.SPRUCE_PLANKS,Material.DIAMOND_BLOCK,Material.EMERALD_BLOCK,Material.COBBLESTONE);
                ArrayList<Material> explodeBlacklistTeamColor = new ArrayList<>();
                for(Team team: arena.getTeamsList()) {
                    explodeBlacklistTeamColor.add(team.getGlass());
                    explodeBlacklistTeamColor.add(team.getConcrete());
                    explodeBlacklistTeamColor.add(team.getBed());
                }


                e.setCancelled(true);
                if(e.getEntity().getType().equals(EntityType.PRIMED_TNT)) {
                    arena.getWorld().createExplosion(e.getLocation(),4,false,false);
                    e.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, e.getLocation(), 25, 1.25, 1.25 ,1.25);
                } else if(e.getEntity().getType().equals(EntityType.FIREBALL)) {
                    arena.getWorld().createExplosion(e.getLocation(),2,true,false);
                    e.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, e.getLocation(), 15, 0.8, 0.8 ,0.8);
                }
                e.getLocation().getWorld().playSound(e.getLocation(), Sound.ENTITY_GENERIC_EXPLODE,1.0f, 1.0f);


                for(Block block : blockList) {
                    final BlockState state = block.getState();

                    /*if(explodeBlacklist.contains(state.getType()) || explodeBlacklistTeamColor.contains(state.getType())) {
                        Bukkit.getServer().getScheduler().runTaskLater(minigame, () -> {
                            state.update(true, false);

                            if(state.getType().toString().contains("bed")) {

                            }

                        }, 1/10);
                    }*/
                    if(!explodeBlacklist.contains(state.getType()) && !explodeBlacklistTeamColor.contains(state.getType())) {
                        block.setType(Material.AIR);                    }
                }
            }
        }
    }
}
