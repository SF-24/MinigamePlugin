package com.xpkitty.minigame.instance.game.bedwars;

import org.bukkit.Location;
import org.bukkit.World;

public class Generator extends Location {
    private GeneratorType type;

    public Generator(World world, double x, double y, double z, GeneratorType type) {
        super(world, x, y, z);
        this.type=type;
    }

    public GeneratorType getType() {
        return type;
    }
    public void setType(GeneratorType generatorType) {
        type=generatorType;
    }
}
