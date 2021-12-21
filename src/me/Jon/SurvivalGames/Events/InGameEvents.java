package me.Jon.SurvivalGames.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import me.Jon.SurvivalGames.Main;
import me.Jon.SurvivalGames.Main.GameState;
import me.Jon.SurvivalGames.PlayersSpecs;
import me.Jon.SurvivalGames.StringFunctions;
import me.Jon.SurvivalGames.ServerActions.LobbyActions;

public class InGameEvents implements Listener {

	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		
		if (Main.gameState.equals(GameState.INGAME) || Main.gameState.equals(GameState.PREDM) || Main.gameState.equals(GameState.DEATHMATCH)) {
			Player player = event.getPlayer();
			if (PlayersSpecs.players.contains(player)) {
				
				World w = player.getWorld();
				Location loc = player.getLocation();
				
				
				
				for (ItemStack i: player.getInventory().getContents()) {			
					if (i != null && !(i.getType().equals(Material.AIR))) {
						System.out.println(i);
						w.dropItemNaturally(loc, i);
					}
				}
				
				for (ItemStack i: player.getInventory().getArmorContents()) {
					if (i != null && !(i.getType().equals(Material.AIR))) {
						System.out.println(i);
						w.dropItemNaturally(loc, i);
					}
				}
				
				
				PlayersSpecs.players.remove(player);
				//death message
				Bukkit.broadcastMessage(PlayersSpecs.nameColors.get(player)+ player.getDisplayName() + ChatColor.GOLD + " has died.");
				Bukkit.broadcastMessage(PlayersSpecs.nameColors.get(player) + player.getDisplayName() + ChatColor.GOLD + " has left.");
			}
			
			if (PlayersSpecs.spectators.contains(event.getPlayer())) {
				PlayersSpecs.spectators.remove(event.getPlayer());
			}
		}
		
		if (Main.gameState.equals(GameState.CLEANUP)) {
			Player player = event.getPlayer();
			if (PlayersSpecs.players.contains(player)) {
				PlayersSpecs.players.remove(player);
			}
		}
		
	}
	
	//only leaves, vines, shrooms, fire can be broken. 
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		
		//specs can't destroy blocks regardless
		if (PlayersSpecs.spectators.contains(event.getPlayer())) {
			event.setCancelled(true);
		}
		
		if ((Main.gameState.equals(GameState.INGAME) || Main.gameState.equals(GameState.PREDM) || Main.gameState.equals(GameState.DEATHMATCH) || Main.gameState.equals(GameState.CLEANUP)) && event.getPlayer().getGameMode().equals(GameMode.SURVIVAL) ){
		
			
			Material type = event.getBlock().getType();
			
			//break items except for leaves, vine, fire, etc.
			if (type == Material.LEAVES) {
			} else if (type == Material.VINE) {
				
			} else if (type == Material.FIRE) {
				
			} else if (type == Material.BROWN_MUSHROOM) {
				
			} else if (type == Material.RED_MUSHROOM) {
				
			} else if (type == Material.GRASS) {
				
			} else if (type == Material.LONG_GRASS) {
				
			} else {
				event.setCancelled(true);
			}

			
			
		}
	}
	
	//only fire can be placed
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		
		if ((Main.gameState.equals(GameState.INGAME) || Main.gameState.equals(GameState.PREDM) || Main.gameState.equals(GameState.DEATHMATCH) || Main.gameState.equals(GameState.CLEANUP)) && event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)){
			if (event.getBlock().getType() == Material.FIRE) {
				
			} else {
				event.setCancelled(true);
			}
			
		}
	}
	
	//flint and steel drains by 16
	@EventHandler
	public void onFNS(PlayerItemDamageEvent e) {
		if (e.getItem().getType().equals(Material.FLINT_AND_STEEL)) {
			
			short curr = e.getItem().getDurability();
			short mod = 16;
			Integer newDur = curr+mod;
			
			e.getItem().setDurability(newDur.shortValue());
		}
	}
	
	//can't craft buckets
    @EventHandler
    public void craftItem(PrepareItemCraftEvent e) {
        Material itemType = e.getRecipe().getResult().getType();
        if(itemType==Material.BUCKET) {
            e.getInventory().setResult(new ItemStack(Material.AIR));

        }
    }
    
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();
		e.setDeathMessage(null);
		
		World w = player.getWorld();
		Location loc = player.getLocation();
		
		
		
		for (ItemStack i: player.getInventory().getContents()) {			
			if (i != null && !(i.getType().equals(Material.AIR))) {
				System.out.println(i);
				w.dropItemNaturally(loc, i);
			}
		}
		
		for (ItemStack i: player.getInventory().getArmorContents()) {
			if (i != null && !(i.getType().equals(Material.AIR))) {
				System.out.println(i);
				w.dropItemNaturally(loc, i);
			}
		}
		
		LobbyActions.clearPlayer(player);
		
		//player.spigot().respawn();
		
		for (Player p: PlayersSpecs.players) {
			p.hidePlayer(player);
		}

		
		if (Main.gameState.equals(GameState.INGAME) || Main.gameState.equals(GameState.PREDM) || Main.gameState.equals(GameState.DEATHMATCH)) {
			Bukkit.broadcastMessage(PlayersSpecs.nameColors.get(player) + player.getDisplayName() + ChatColor.GOLD + " has died.");
			
			//GET KILLER AND DO STUFF
			//SQL events has other stuff
			if (player.getKiller() instanceof Player) {
				Player killer = player.getKiller();
				killer.sendMessage(Main.prefix + ChatColor.AQUA + "You killed " + PlayersSpecs.nameColors.get(player) + player.getDisplayName());
				
			
			}
			
			w.strikeLightningEffect(loc);
			player.setAllowFlight(true);
			
			//remove from alive players
			PlayersSpecs.players.remove(player);
			PlayersSpecs.spectators.add(player);
			if (PlayersSpecs.players.size() > 1) {
				Bukkit.broadcastMessage(Main.prefix + ChatColor.GREEN + "Only " + StringFunctions.surround(String.valueOf(PlayersSpecs.players.size()), ChatColor.RED + "") + ChatColor.GREEN + " tributes remain!");
			}
			
		}
		

		
	}
	
	//prevent explosions
    @EventHandler
    public void onBlockExplode(EntityExplodeEvent e) {
        e.blockList().clear();
        e.setCancelled(true);
    }
	
	
	
	

}
