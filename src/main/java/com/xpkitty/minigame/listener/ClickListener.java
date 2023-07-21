// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class ClickListener implements Listener {

    @EventHandler
    void onClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(item!=null) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    String locName = meta.getLocalizedName();
                    if(locName.equalsIgnoreCase("FIREBALL")) {
                        if(item.getAmount()>1) {
                            item.setAmount(item.getAmount()-1);
                        } else {
                            item = new ItemStack(Material.AIR);
                        }
                        player.getInventory().setItemInMainHand(item);

                        //setup basic variables
                        final Location start = player.getLocation().add(0, 1, 0);
                        // Optionally, set y to zero at player.getLocation().getDirection().setY(0)
                        final Vector facing = player.getEyeLocation().add(player.getLocation().getDirection().multiply(10)).toVector();
                        // multiply by a constant, if you want a higher velocity
                        final Vector initialV = facing.subtract(start.toVector()).normalize();

                        //setup location and spawn fireball
                        Fireball fireball = player.getWorld().spawn(start, Fireball.class);
                        fireball.setDirection(start.getDirection());
                        fireball.setShooter(player);
                        fireball.setVelocity(initialV);
                        fireball.setYield(1.5f);


                    }
                }
            }
        }
    }
}
