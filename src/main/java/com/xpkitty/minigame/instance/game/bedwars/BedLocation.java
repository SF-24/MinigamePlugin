// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.instance.game.bedwars;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

public class BedLocation extends Location {
    private BlockFace facing;

    public BedLocation(World world, double x, double y, double z, BlockFace facing) {
        super(world, x, y, z);
        this.facing=facing;
    }

    public BlockFace getFacing() {
        return facing;
    }
}