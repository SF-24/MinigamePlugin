package com.xpkitty.minigame.manager;

import com.xpkitty.minigame.instance.game.CornerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class Region{

    World world;
    Location lowCorner;
    Location highCorner;

    public Region(Location corner1, Location corner2) {
        lowCorner=LocationManager.getSingleCornerFromTwo(corner1,corner2, CornerType.LOWER);
        highCorner=LocationManager.getSingleCornerFromTwo(corner1,corner2, CornerType.HIGHER);
        world=corner1.getWorld();
    }

    public Location getLowCorner() {return lowCorner;}
    public Location getHighCorner() {return getHighCorner();}

    public boolean isPlayerInRegion(Player player) {
        return isLocationInRegion(player.getLocation());
    }

    public boolean isLocationInRegion(Location location) {
        if(MathsManager.isNumberInNumberRegion(lowCorner.getZ(),highCorner.getZ(),location.getZ()) && MathsManager.isNumberInNumberRegion(lowCorner.getY(),highCorner.getY(),location.getY()) && MathsManager.isNumberInNumberRegion(lowCorner.getX(),highCorner.getX(), location.getX())) {
            return Objects.equals(location.getWorld(), world);
        }
        return false;
    }

    public ArrayList<Location> getBlocksInRegionAtSetY(int y) {
        ArrayList<Location> loc = new ArrayList<>();

        for(int x = (int) lowCorner.getX(); x<highCorner.getX(); x++) {
            for(int z = (int) lowCorner.getZ(); z<highCorner.getZ(); z++) {
                if(!isLocationInRegion(new Location(world,x,y,z))) return null;
                loc.add(new Location(world,x,y,z));
            }
        }
        return loc;
    }

    public void fillRegion(Material material) {
        for(Location location : getBlocksInRegion()) {
            world.getBlockAt(location).setType(material);
        }
    }

    public void fillRegionAtSetY(Material material, int y) {
        if(getBlocksInRegionAtSetY(y)==null) {
            System.out.println("[MinigamePlugin] Error! Y value " + y + " is not in region");
            return;
        }

        for(Location location : getBlocksInRegionAtSetY(y)) {
            world.getBlockAt(location).setType(material);
        }
    }

    // get list of blocks in region
    public ArrayList<Location> getBlocksInRegion() {
        ArrayList<Location> loc = new ArrayList<>();

        for(int x = (int) lowCorner.getX(); x<highCorner.getX(); x++) {
            for(int y = (int) lowCorner.getY(); y<highCorner.getY(); y++) {
                for(int z = (int) lowCorner.getZ(); z<highCorner.getZ(); z++) {
                    loc.add(new Location(world,x,y,z));
                }
            }
        }
        return loc;
    }
}
