// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3


package com.xpkitty.minigame.kit.type;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.kit.Kit;
import com.xpkitty.minigame.kit.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class KnightKit extends Kit {
    public KnightKit(Minigame minigame, UUID uuid) {
        super(minigame, KitType.KNIGHT, uuid);
    }

    @Override
    public void onStart(Player player) {
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();
        assert swordMeta != null;
        swordMeta.setUnbreakable(true);
        sword.setItemMeta(swordMeta);

        player.getInventory().addItem(sword);
        player.getInventory().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.GOLDEN_BOOTS));
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 10));
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
    }
}
