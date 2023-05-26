package com.xpkitty.minigame.ui.bedwars.elements;

import com.xpkitty.minigame.ui.bedwars.BedWarsCurrency;
import org.bukkit.Material;

public enum Item {

    WOOL(Material.WHITE_WOOL,16,ShopCategory.BLOCKS,19,19,"Wool",4,BedWarsCurrency.IRON),
    TERRACOTTA(Material.TERRACOTTA,16,ShopCategory.BLOCKS,-1,20,"Hardened Clay",12,BedWarsCurrency.IRON),
    GLASS(Material.GLASS,4,ShopCategory.BLOCKS,39,21,"Blast Proof Glass",12,BedWarsCurrency.IRON),
    END_STONE(Material.END_STONE,12,ShopCategory.BLOCKS,40,22,"End Stone",24,BedWarsCurrency.IRON),
    LADDER(Material.LADDER,8,ShopCategory.BLOCKS,-1,23,"Ladder",4,BedWarsCurrency.IRON),
    PLANKS(Material.OAK_PLANKS,16,ShopCategory.BLOCKS,28,24,"Planks",4,BedWarsCurrency.GOLD),
    OBSIDIAN(Material.OBSIDIAN,4,ShopCategory.BLOCKS,-1,25,"Obsidian",4,BedWarsCurrency.EMERALD),

    STONE_SWORD(Material.STONE_SWORD,1,ShopCategory.MELEE,20,19,"Stone Sword",10,BedWarsCurrency.IRON),
    IRON_SWORD(Material.IRON_SWORD,1,ShopCategory.MELEE,29,20,"Iron Sword", 7, BedWarsCurrency.GOLD),
    DIAMOND_SWORD(Material.DIAMOND_SWORD,1,ShopCategory.MELEE,-1,21,"Diamond Sword",4,BedWarsCurrency.EMERALD),
    KNOCKBACK_STICK(Material.STICK,1,ShopCategory.MELEE,-1,22,"Knockback Stick",5,BedWarsCurrency.GOLD),

    CHAIN_ARMOR(Material.CHAINMAIL_BOOTS,1,ShopCategory.ARMOUR,21,19,"Permanent Chainmail Armour", 40, BedWarsCurrency.IRON),
    IRON_ARMOR(Material.IRON_BOOTS,1,ShopCategory.ARMOUR,30,20,"Permanent Iron Armour", 12, BedWarsCurrency.GOLD),
    DIAMOND_ARMOR(Material.DIAMOND_BOOTS,1,ShopCategory.ARMOUR,-1,21,"Permanent Diamond Armour", 6, BedWarsCurrency.EMERALD),

    SHEARS(Material.SHEARS,1,ShopCategory.TOOLS,31,19,"Permanent Shears",20,BedWarsCurrency.IRON),
    PICKAXE(Material.WOODEN_PICKAXE,1,ShopCategory.TOOLS,22,20,"Pickaxe",3,BedWarsCurrency.GOLD),
    AXE(Material.WOODEN_AXE,1, ShopCategory.TOOLS, 32, 21, "Axe", 3, BedWarsCurrency.GOLD),

    ARROW(Material.ARROW,6,ShopCategory.RANGED,32,19,"Arrow",2,BedWarsCurrency.GOLD),
    BOW(Material.BOW,1,ShopCategory.RANGED,23,20,"Bow",12,BedWarsCurrency.GOLD),

    INVIS(Material.POTION,1,ShopCategory.POTIONS,33,19,"Invisibility Potion",2,BedWarsCurrency.EMERALD),

    GAPPLE(Material.GOLDEN_APPLE,1,ShopCategory.UTILITY,37,19,"Golden Apple", 3, BedWarsCurrency.GOLD),
    FIREBALL(Material.FIRE_CHARGE,1,ShopCategory.UTILITY,-1,20,"Fireball",40,BedWarsCurrency.IRON),
    TNT(Material.TNT,1,ShopCategory.UTILITY,25,21,"TNT",8,BedWarsCurrency.GOLD),
    ENDER_PEARL(Material.ENDER_PEARL,1,ShopCategory.UTILITY,-1,22,"Ender Pearl",4,BedWarsCurrency.EMERALD),
    WATER_BUCKER(Material.WATER_BUCKET,1,ShopCategory.UTILITY,34,23,"Water Bucket",6,BedWarsCurrency.GOLD),
    SPONGE(Material.SPONGE,4,ShopCategory.UTILITY,-1,24,"Sponge",3,BedWarsCurrency.GOLD),
    EMERALD(Material.EMERALD,1,ShopCategory.UTILITY,-1,25,"Emerald",4,BedWarsCurrency.DIAMOND);


    private final ShopCategory category;
    private final int mainSlot;
    private final int slot;
    private final String name;
    private final BedWarsCurrency currency;
    private final int price;
    private final int count;
    private final Material material;

    Item(Material material, int count, ShopCategory category, int mainSlot, int slot, String name, int price, BedWarsCurrency currency){
        this.category=category;
        this.mainSlot=mainSlot;
        this.slot=slot;
        this.name=name;
        this.price=price;
        this.currency=currency;
        this.material=material;
        this.count=count;
    }

    public int getCount() {
        return count;
    }

    public Material getMaterial() {
        return material;
    }

    public ShopCategory getCategory() {
        return category;
    }

    public int getMainSlot() {
        return mainSlot;
    }

    public int getSlot() {
        return slot;
    }

    public BedWarsCurrency getCurrency() {
        return currency;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}
