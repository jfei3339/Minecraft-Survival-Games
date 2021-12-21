package me.Jon.SurvivalGames.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Jon.SurvivalGames.Game.GameState;
import me.Jon.SurvivalGames.Main;
import me.Jon.SurvivalGames.PlayersSpecs;
import me.Jon.SurvivalGames.SGScoreboards;

/*
 * Class managing the logistics of a player joining the server while the game is about to start (pregame)
 */
public class PreEvents implements Listener {
	
	private SGScoreboards scoreboards = new SGScoreboards();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) { //don't let player join if in pregame
		if (Main.game.gameState.equals(GameState.PREGAME)) {
			Player player = event.getPlayer();
			PlayersSpecs.spectators.add(player);
			scoreboards.createScoreboard(player);			
			
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (Main.game.gameState.equals(GameState.PREGAME)) {
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
		if (Main.game.gameState.equals(GameState.PREGAME) || Main.game.gameState.equals(GameState.PREDM) || Main.game.gameState.equals(GameState.CLEANUP) ) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if ((Main.game.gameState.equals(GameState.PREGAME) || Main.game.gameState.equals(GameState.PREDM)) &&  PlayersSpecs.players.contains(e.getPlayer())) {
			Player player = e.getPlayer();
			Location from = e.getFrom();
			
			if (from.getZ() != e.getTo().getZ() && from.getX() != e.getTo().getX()) {
				player.teleport(from);
			}
		}

		
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (Main.game.gameState.equals(GameState.PREGAME)) {
			event.setCancelled(true);
		}
	}

}
