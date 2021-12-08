package me.Jon.SurvivalGames.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Jon.SurvivalGames.Main;
import me.Jon.SurvivalGames.PlayersSpecs;
import me.Jon.SurvivalGames.SGScoreboards;

public class PreEvents implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) { //don't let player join if in pregame
		
		
		if (Main.gameState.equals("PREGAME")) {
			Player player = event.getPlayer();
			PlayersSpecs.spectators.add(player);
			SGScoreboards.createScoreboard(player);			
		}
		
			
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (Main.gameState.equals("PREGAME")) {
			if (PlayersSpecs.players.contains(event.getPlayer())) {
				PlayersSpecs.players.remove(event.getPlayer());
			}
			
			if (PlayersSpecs.spectators.contains(event.getPlayer())) {
				PlayersSpecs.spectators.remove(event.getPlayer());
			}
		}

		
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (Main.gameState.equals("PREGAME") || Main.gameState.equals("PREDM") || Main.gameState.equals("CLEANUP") ) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if ((Main.gameState.equals("PREGAME") || Main.gameState.equals("PREDM")) &&  PlayersSpecs.players.contains(e.getPlayer())) {
			Player player = e.getPlayer();
			Location from = e.getFrom();
			
			if (from.getZ() != e.getTo().getZ() && from.getX() != e.getTo().getX()) {
				player.teleport(from);
			}
		}

		
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (Main.gameState.equals("PREGAME")) {
			event.setCancelled(true);
		}
	}

}
