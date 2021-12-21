package me.Jon.SurvivalGames.Events;

import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Jon.SurvivalGames.Game.GameState;
import me.Jon.SurvivalGames.Main;
import me.Jon.SurvivalGames.PlayersSpecs;
import me.Jon.SurvivalGames.Data.MapTier2;
import me.Jon.SurvivalGames.Items.ChestTierManager;


/*
 * Class that manages what happens when a player opens a chest.
 */
public class ChestOpenEvent implements Listener {
	
	public static HashSet<String> openedChests = new HashSet<String>();
	public static HashSet<String> tier2 = new HashSet<String>();
	public static boolean refill1 = false;
	public static boolean refill2 = false;
	
	
	/**
	 * Gets the location of the tier 2 chests (chests with better items) on a certain map.
	 * 
	 * @param map: the name of the map.
	 */
	public static void getTier2Locations(String map) {
		
		int chestNum = 1;
		while (MapTier2.mapTier2Config.contains("Maps. " + map + "." + chestNum)) {
			int x = MapTier2.mapTier2Config.getInt("Maps. " + map + "." + chestNum + ".x" );
			int y = MapTier2.mapTier2Config.getInt("Maps. " + map + "." + chestNum + ".y" );
			int z = MapTier2.mapTier2Config.getInt("Maps. " + map + "." + chestNum + ".z" );
			
			tier2.add(String.valueOf(x) + " " + String.valueOf(y) + " " + String.valueOf(z));
			chestNum++;
		}
		
	}
	
	/**
	 * Handles the event of a player opening a chest.
	 * 
	 * @param e: an event where a player interacts with something, possibly opening a chest.
	 */
	@EventHandler
	public void onChestOpen(PlayerInteractEvent e) {
		
		if (!Main.game.gameState.equals(GameState.LOBBY)){
			
			if (e.hasBlock()) {
				Block b = e.getClickedBlock();
				Location b_Loc = b.getLocation();
				String coords = String.valueOf(b_Loc.getBlockX()) + " " + String.valueOf(b_Loc.getBlockY()) + " " + String.valueOf(b_Loc.getBlockZ());
				
				if (b.getType().equals(Material.CHEST)) {

					PlayersSpecs.playersChests.put(e.getPlayer(), PlayersSpecs.playersChests.get(e.getPlayer()) + 1);
					
					
					if (openedChests.contains(coords) ) {
						//refills?
						
						if (refill1 == true) {
							refill1 = false;
							BlockState state = b.getState();
							if (state instanceof Chest) {
								
								Chest c = (Chest) state;
								//chest tiering here!
								if (tier2.contains(coords)) {
									ChestTierManager.fillTier2Chest(c.getInventory());
								} else {
									ChestTierManager.fillTier1Chest(c.getInventory());
								}															}
								
							}
						
					
					} else {
						
						BlockState state = b.getState();
						if (state instanceof Chest) {
							
							Chest c = (Chest) state;
							c.getInventory().clear();
							//chest tiering here!
							if (tier2.contains(coords)) {
								
								ChestTierManager.fillTier2Chest(c.getInventory());
							} else {
								ChestTierManager.fillTier1Chest(c.getInventory());
							}
							
							openedChests.add(coords);
							
							if (c.getInventory().getSize() == 54) {
								//Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "big chest not seen location is " + coords);
								
								String coords1 = String.valueOf(b_Loc.getBlockX()+1) + " " + String.valueOf(b_Loc.getBlockY()) + " " + String.valueOf(b_Loc.getBlockZ());
								String coords2 = String.valueOf(b_Loc.getBlockX()-1) + " " + String.valueOf(b_Loc.getBlockY()) + " " + String.valueOf(b_Loc.getBlockZ());

								String coords5 = String.valueOf(b_Loc.getBlockX()) + " " + String.valueOf(b_Loc.getBlockY()) + " " + String.valueOf(b_Loc.getBlockZ()+1);
								String coords6 = String.valueOf(b_Loc.getBlockX()) + " " + String.valueOf(b_Loc.getBlockY()) + " " + String.valueOf(b_Loc.getBlockZ()-1);
								
								openedChests.add(coords1);
								openedChests.add(coords2);
								openedChests.add(coords5);
								openedChests.add(coords6);
								
								
							}						
						}
						
						
						
					}
					
					//chest open stats
					/*
					if (Main.connected == true) {
						Player p = e.getPlayer();
						UUID uuid = p.getUniqueId();
						Main.data.setStat(uuid, "CHESTS", Main.data.getStat(uuid, "CHESTS") + 1);
					} 
					
					*/
				}
			}
		} 	
	
	}
}
