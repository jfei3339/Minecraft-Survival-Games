package me.Jon.SurvivalGames.Items;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestTierManager {
	
	//need a list of TIER 2 CHEST LOCATIONS
	
	public static ArrayList<ItemStack> tier1Items = new ArrayList<ItemStack>();
	public static ArrayList<ItemStack> tier2Items = new ArrayList<ItemStack>();
	
	//no duplicates of these
	
	//tier 2 locations
	
	static {
		//Tier 1 weapons
		tier1Items.add(new ItemStack(Material.WOOD_AXE, 1));
		tier1Items.add(new ItemStack(Material.WOOD_SWORD, 1));
		tier1Items.add(new ItemStack(Material.STONE_AXE, 1));
		tier1Items.add(new ItemStack(Material.STONE_SWORD, 1));
		tier1Items.add(new ItemStack(Material.BOW, 1));
		tier1Items.add(new ItemStack(Material.FISHING_ROD, 1));
		tier1Items.add(new ItemStack(Material.ARROW, 2));
		
		//Tier 1 armor
		tier1Items.add(new ItemStack(Material.LEATHER_BOOTS, 1));
		tier1Items.add(new ItemStack(Material.LEATHER_CHESTPLATE, 1));
		tier1Items.add(new ItemStack(Material.LEATHER_HELMET, 1));
		tier1Items.add(new ItemStack(Material.LEATHER_LEGGINGS, 1));
		
		//Tier 1 food
		tier1Items.add(new ItemStack(Material.RAW_BEEF, 1));
		tier1Items.add(new ItemStack(Material.RAW_CHICKEN, 1));
		tier1Items.add(new ItemStack(Material.RAW_FISH, 1));
		tier1Items.add(new ItemStack(Material.PORK, 1));
		tier1Items.add(new ItemStack(Material.CARROT, 1));
		tier1Items.add(new ItemStack(Material.PUMPKIN_PIE, 1));
		tier1Items.add(new ItemStack(Material.APPLE, 1));
		tier1Items.add(new ItemStack(Material.BREAD, 1));
		tier1Items.add(new ItemStack(Material.COOKIE, 4));
		
		//Tier 1 extras
		tier1Items.add(new ItemStack(Material.STICK, 1));
		tier1Items.add(new ItemStack(Material.FLINT, 1));
		tier1Items.add(new ItemStack(Material.FEATHER, 1));
		tier1Items.add(new ItemStack(Material.GOLD_INGOT, 1));
		tier1Items.add(new ItemStack(Material.IRON_INGOT, 1));
		tier1Items.add(new ItemStack(Material.LEATHER, 3));
		tier1Items.add(new ItemStack(Material.BOWL, 2));
		
	}
	
	static {
		//Tier 2 weapons
		tier2Items.add(new ItemStack(Material.STONE_SWORD, 1));
		tier2Items.add(new ItemStack(Material.BOW, 1));
		tier2Items.add(new ItemStack(Material.ARROW, 4));
		tier2Items.add(new ItemStack(Material.FLINT_AND_STEEL, 1));
		
		//Tier 2 armor
		tier2Items.add(new ItemStack(Material.IRON_CHESTPLATE, 1));
		tier2Items.add(new ItemStack(Material.IRON_LEGGINGS, 1));
		tier2Items.add(new ItemStack(Material.IRON_BOOTS, 1));
		tier2Items.add(new ItemStack(Material.IRON_HELMET, 1));
		
		tier2Items.add(new ItemStack(Material.CHAINMAIL_BOOTS, 1));
		tier2Items.add(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
		tier2Items.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1));
		tier2Items.add(new ItemStack(Material.CHAINMAIL_HELMET, 1));
		
		tier2Items.add(new ItemStack(Material.GOLD_BOOTS, 1));
		tier2Items.add(new ItemStack(Material.GOLD_LEGGINGS, 1));
		tier2Items.add(new ItemStack(Material.GOLD_CHESTPLATE, 1));
		tier2Items.add(new ItemStack(Material.GOLD_HELMET, 1));
		
		//Tier 2 food
		
		tier2Items.add(new ItemStack(Material.COOKED_BEEF, 1));
		tier2Items.add(new ItemStack(Material.COOKED_CHICKEN, 1));
		tier2Items.add(new ItemStack(Material.GRILLED_PORK, 1));
		tier2Items.add(new ItemStack(Material.COOKED_FISH, 1));
		tier2Items.add(new ItemStack(Material.GOLDEN_APPLE, 1));
		tier2Items.add(new ItemStack(Material.BAKED_POTATO, 1));
		
		//Tier 2 extras
		tier2Items.add(new ItemStack(Material.IRON_INGOT, 1));
		tier2Items.add(new ItemStack(Material.DIAMOND, 1));
		tier2Items.add(new ItemStack(Material.STICK, 2));	
		tier2Items.add(new ItemStack(Material.BOAT, 2));	
	}
	
	public static void fillTier1Chest(Inventory inv) {
		int tier1Size = tier1Items.size();
		
		HashSet<ItemStack> already1 = new HashSet<ItemStack>();
		HashSet<ItemStack> already2 = new HashSet<ItemStack>();
		
		Random rand = new Random();
		int randy;
		
		for (int i = 0; i < inv.getSize(); i++) {
			randy = rand.nextInt(4);
			
			if (randy == 0) {
				ItemStack item = tier1Items.get(rand.nextInt(tier1Size));
				if (already2.contains(item)) {
					
				} else if (already1.contains(item)) {
					already2.add(item);
					inv.setItem(i, item);
				} else {
					already1.add(item);
					inv.setItem(i, item);
				}
				
			}
		}
		
		//at least 1 item
		inv.setItem(rand.nextInt(27), tier1Items.get(rand.nextInt(tier1Size)));
		
	}
	
	public static void fillTier2Chest(Inventory inv) {
		
		int tier2Size = tier2Items.size();
		
		HashSet<ItemStack> already1 = new HashSet<ItemStack>();
		HashSet<ItemStack> already2 = new HashSet<ItemStack>();
		
		Random rand = new Random();
		int randy;
		
		for (int i = 0; i < inv.getSize(); i++) {
			randy = rand.nextInt(4);
			
			if (randy == 0) {
				ItemStack item = tier2Items.get(rand.nextInt(tier2Size));
				if (already2.contains(item)) {
					
				} else if (already1.contains(item)) {
					
					if (inv.getItem(i) == null) {
						already2.add(item);
						inv.setItem(i, item);
					}


					
					
				} else {

					if (inv.getItem(i) == null) {
						already1.add(item);
						inv.setItem(i, item);
					}
					
				}
				
			}
		}
		
		//at least 1 item
		inv.setItem(rand.nextInt(27), tier2Items.get(rand.nextInt(tier2Size)));
	}
	


}
