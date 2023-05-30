package com.xpkitty.minigame.instance;

import com.xpkitty.minigame.Minigame;
import com.xpkitty.minigame.instance.game.bedwars.BedWarsGame;
import com.xpkitty.minigame.instance.game.bedwars.Tool;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameItemManager {
    public static ItemStack getUnbreakableItem(Material material) {
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
        List<Material> armour = Arrays.asList(Material.LEATHER_HELMET,Material.LEATHER_CHESTPLATE,Material.LEATHER_LEGGINGS,Material.LEATHER_BOOTS,Material.GOLDEN_HELMET,Material.GOLDEN_CHESTPLATE,Material.GOLDEN_LEGGINGS,Material.GOLDEN_BOOTS,Material.CHAINMAIL_HELMET,Material.CHAINMAIL_CHESTPLATE,Material.CHAINMAIL_LEGGINGS,Material.CHAINMAIL_BOOTS,Material.IRON_HELMET,Material.IRON_CHESTPLATE,Material.IRON_LEGGINGS,Material.IRON_BOOTS,Material.DIAMOND_HELMET,Material.DIAMOND_CHESTPLATE,Material.DIAMOND_LEGGINGS,Material.DIAMOND_BOOTS,Material.NETHERITE_HELMET,Material.NETHERITE_CHESTPLATE,Material.NETHERITE_LEGGINGS,Material.NETHERITE_BOOTS,Material.TURTLE_HELMET);
        List<Material> tools = Arrays.asList(Material.WOODEN_AXE,Material.WOODEN_PICKAXE,Material.WOODEN_HOE,Material.WOODEN_SWORD,Material.WOODEN_SHOVEL,Material.STONE_AXE,Material.STONE_PICKAXE,Material.STONE_SHOVEL,Material.STONE_HOE, Material.STONE_SWORD, Material.IRON_AXE,Material.IRON_PICKAXE, Material.IRON_SHOVEL,Material.IRON_HOE,Material.IRON_SWORD,Material.GOLDEN_AXE, Material.GOLDEN_PICKAXE,Material.GOLDEN_SHOVEL, Material.GOLDEN_HOE, Material.GOLDEN_SWORD,Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE,Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE,Material.DIAMOND_SWORD,Material.NETHERITE_AXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_PICKAXE, Material.NETHERITE_HOE,Material.NETHERITE_SWORD, Material.SHEARS,Material.FLINT_AND_STEEL);
        if(tools.contains(material) || armour.contains(material)) {
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }
        stack.setItemMeta(meta);
        return stack;
    }

    // upgrade BedWars tool
    public static void upgradeTool(Player player, Tool tool) {
        if(tool.equals(Tool.PICKAXE)) {

            if(player.getInventory().contains(Material.WOODEN_PICKAXE)) {
                player.getInventory().remove(getUnbreakableItem(Material.WOODEN_PICKAXE));
                player.getInventory().addItem(getUnbreakableItem(Material.IRON_PICKAXE));
            } else if(player.getInventory().contains(Material.IRON_PICKAXE)) {
                player.getInventory().remove(getUnbreakableItem(Material.IRON_PICKAXE));
                player.getInventory().addItem(getUnbreakableItem(Material.GOLDEN_PICKAXE));
            } else if(player.getInventory().contains(Material.GOLDEN_PICKAXE)) {
                player.getInventory().remove(getUnbreakableItem(Material.GOLDEN_PICKAXE));
                player.getInventory().addItem(getUnbreakableItem(Material.DIAMOND_PICKAXE));
            } else {
                player.getInventory().addItem(getUnbreakableItem(Material.WOODEN_PICKAXE));
            }

        } else if(tool.equals(Tool.AXE)) {
            if(player.getInventory().contains(Material.WOODEN_AXE)) {
                player.getInventory().remove(getUnbreakableItem(Material.WOODEN_AXE));
                player.getInventory().addItem(getUnbreakableItem(Material.STONE_AXE));
            } else if(player.getInventory().contains(Material.STONE_AXE)) {
                player.getInventory().remove(getUnbreakableItem(Material.STONE_AXE));
                player.getInventory().addItem(getUnbreakableItem(Material.IRON_AXE));
            } else if(player.getInventory().contains(Material.IRON_AXE)) {
                player.getInventory().remove(getUnbreakableItem(Material.IRON_AXE));
                player.getInventory().addItem(getUnbreakableItem(Material.DIAMOND_AXE));
            } else {
                player.getInventory().addItem(getUnbreakableItem(Material.WOODEN_AXE));
            }
        } else if(tool.equals(Tool.SHEARS)) {
            player.getInventory().addItem(getUnbreakableItem(Material.SHEARS));
        } else if(tool.equals(Tool.KNOCKBACK_STICK)) {
            ItemStack stick = new ItemStack(Material.STICK);
            ItemMeta stickMeta = stick.getItemMeta();
            stickMeta.setDisplayName(ChatColor.WHITE+"Knockback Stick");
            stickMeta.addEnchant(Enchantment.KNOCKBACK, 3, true);
            stick.setItemMeta(stickMeta);
        }
    }

    // get unbreakable died armor item stack
    // handy for team games
    public static ItemStack getUnbreakableDiedArmour(Material material, Player player, Arena arena) {
        ItemStack stack = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.setColor(arena.getPlayerTeam(player).getColour());
        stack.setItemMeta(meta);
        return stack;
    }

    //armour level 0 = leather
    //al 1 = chain-mail
    //al 2 = iron
    //al 3 = diamond
    public static boolean upgradeArmour(Player player, int armorLevel, Minigame minigame) {
        BedWarsGame bedWarsGame = (BedWarsGame) minigame.getArenaManager().getArena(player).getGame();
        int currLevel = bedWarsGame.getPlayerArmorLevel(player);

        if(currLevel<armorLevel) {
            ItemStack leggings;
            ItemStack boots;

            if(armorLevel<=0) {
                return false;
            } else if(armorLevel == 1) {
                boots=getUnbreakableItem(Material.CHAINMAIL_BOOTS);
                leggings=getUnbreakableItem(Material.CHAINMAIL_LEGGINGS);
            } else if(armorLevel == 2) {
                boots=getUnbreakableItem(Material.IRON_BOOTS);
                leggings=getUnbreakableItem(Material.IRON_LEGGINGS);
            } else if(armorLevel == 3) {
                boots=getUnbreakableItem(Material.DIAMOND_BOOTS);
                leggings=getUnbreakableItem(Material.DIAMOND_LEGGINGS);
            } else {
                return false;
            }
                bedWarsGame.setPlayerArmorLevel(player, armorLevel);
                player.getInventory().setBoots(boots);
                player.getInventory().setLeggings(leggings);
                return true;
        }
        return false;
    }

    public static void getUtility(Player player, Utility utility) {
        if(utility.equals(Utility.FIREBALL)) {
            ItemStack item = new ItemStack(Material.FIRE_CHARGE);
            ItemMeta meta = item.getItemMeta();
            meta.setLocalizedName("FIREBALL");
            meta.setDisplayName(ChatColor.WHITE+"Fireball");
            meta.setLore(Collections.singletonList(ChatColor.GRAY + "Right click while holding to shoot"));
            item.setItemMeta(meta);
            player.getInventory().addItem(item);
        }
    }

    public static void getPotion(Player player, PotionEffectType effectType, float durationInSeconds, int amplifier) {

        // make item stack, meta, set name and colour
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        potionMeta.setDisplayName(ChatColor.WHITE + "Potion of " + effectType.getName());
        potionMeta.setColor(Color.fromRGB(224,224,224));

        //convert duration to ticks
        int durationInTicks = (int) (durationInSeconds*20);

        // apply potion effect
        potionMeta.addCustomEffect(new PotionEffect(effectType, durationInTicks,amplifier,true,false, true),true);

        player.getInventory().addItem(potion);
    }
}
