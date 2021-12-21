package me.Jon.SurvivalGames.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import me.Jon.SurvivalGames.Game.GameState;
import me.Jon.SurvivalGames.Main;
import me.Jon.SurvivalGames.PlayersSpecs;
import me.Jon.SurvivalGames.SGScoreboards;

/*
 * Class managing the logistics of a player joining the server while the game is ongoing (spectators)
 */
public class SpecEvents implements Listener {
	
	private final SGScoreboards scoreboards = new SGScoreboards();
	
	//spectator join
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) { //don't let player join if in pregame
		Player player = event.getPlayer();
		
		if (Main.game.gameState.equals(GameState.CLEANUP)) {
			player.kickPlayer("You may not join, the server is restarting soon.");
		}
		
		if (Main.game.gameState.equals(GameState.INGAME) || Main.game.gameState.equals(GameState.PREDM) || Main.game.gameState.equals(GameState.DEATHMATCH)) {
			PlayersSpecs.spectators.add(player);
			scoreboards.createScoreboard(player);	
			
			Location loc = Bukkit.getWorld(Main.game.winningMap.toLowerCase()).getSpawnLocation();
			
			player.teleport(loc);
			
			player.setAllowFlight(true);
			
			for (Player p: PlayersSpecs.players) {
				p.hidePlayer(player);
			}
			
			//player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 100));
		}
		

		
			
	}
	
	//spectator can't lose hunger
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		if (PlayersSpecs.spectators.contains((Player) e.getEntity())) {
			e.setCancelled(true);
		}
	}
	
	//spectator can't pick up items
	@EventHandler
	public void onPick(PlayerPickupItemEvent e) {
		if (PlayersSpecs.spectators.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	//spectator can't take damage OR deal damage
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (PlayersSpecs.spectators.contains((Player) e.getEntity())) {
				e.setCancelled(true);
			}
		}
	}
	
	//spectator can't damage players
	@EventHandler
	public void onHitPlayer(EntityDamageByEntityEvent e) {
	
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			if (PlayersSpecs.spectators.contains((Player) e.getDamager())) {
				e.setCancelled(true);
			}
		}
		
	}
	
	//spectator cant interact
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (PlayersSpecs.spectators.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
}
