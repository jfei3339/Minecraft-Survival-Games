package me.Jon.SurvivalGames;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import me.Jon.SurvivalGames.Game.GameState;
import me.Jon.SurvivalGames.Data.ServerInfo;
import net.md_5.bungee.api.ChatColor;

/*
 * Class that manages scoreboards of players.
 */
public class SGScoreboards {

	/**
	 * Creates a scoreboard for a player.
	 * 
	 * @param player the player to create a scoreboard for
	 */
	public void createScoreboard(Player player) {
		
		ScoreboardManager m = Bukkit.getScoreboardManager();
		Scoreboard b = m.getNewScoreboard();
		
		Objective o = b.registerNewObjective("Lobby", "dummy");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		if (Main.game.gameState.equals(GameState.LOBBY)) {
			
			if (Main.game.getTimeLeft() == 60) o.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 1:00");
			else if (Main.game.getTimeLeft() > 9) o.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 0:" + Main.game.getTimeLeft());
			else if (Main.game.getTimeLeft() >= 0) o.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 0:0" + Main.game.getTimeLeft());
			else o.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 0:00");
			
		} else if (Main.game.gameState.equals(GameState.PREGAME)) {
			
			if (Main.game.getTimeLeft() > 9) b.getObjective("Lobby").setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "PreGame" + ChatColor.RED + " 0:" + Main.game.getTimeLeft());
			else if (Main.game.getTimeLeft() >= 0) b.getObjective("Lobby").setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "PreGame" + ChatColor.RED + " 0:0" + Main.game.getTimeLeft());
			else b.getObjective("Lobby").setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "PreGame" + ChatColor.RED + " 0:00");
			
		}

		
		Score serverIP = o.getScore(Main.serverIP);
		serverIP.setScore(0);
		
		Score blank_ = o.getScore("    ");
		blank_.setScore(1);
		
		Team watching = b.registerNewTeam("watching");
		watching.addEntry(ChatColor.WHITE + "Watching: ");
		watching.setPrefix("");
		watching.setSuffix("" + ChatColor.GREEN + PlayersSpecs.spectators.size());
		
		if (Main.game.gameState.equals(GameState.PREGAME) || Main.game.gameState.equals(GameState.INGAME) || Main.game.gameState.equals(GameState.PREDM) || Main.game.gameState.equals(GameState.DEATHMATCH) || Main.game.gameState.equals(GameState.CLEANUP) ) {
			o.getScore(ChatColor.WHITE + "Watching: ").setScore(2);
		}
		//watching.setScore(2);
		
		Team playing = b.registerNewTeam("playing");
		playing.addEntry(ChatColor.WHITE + "Playing: ");
		playing.setPrefix("");
		playing.setSuffix("" + ChatColor.GREEN + PlayersSpecs.players.size());
		o.getScore(ChatColor.WHITE + "Playing: ").setScore(3);
		
		Score Players = o.getScore(ChatColor.GRAY + "» " + ChatColor.WHITE + "Players");
		Players.setScore(4);
			 
		Team blank = b.registerNewTeam("blank");
		blank.addEntry(" ");
		blank.setPrefix(" ");
		blank.setSuffix(" ");
		o.getScore(" ").setScore(5);	
		
		Score serverName = o.getScore(ChatColor.GOLD + "US " + ChatColor.YELLOW + ServerInfo.ServerConfig.getString("serverName").substring(2));
		serverName.setScore(6);
		
		Score server = o.getScore(ChatColor.GRAY + "» " + ChatColor.WHITE + "Server");
		server.setScore(7);
		
		Team blank1 = b.registerNewTeam("blank1");
		blank1.addEntry("  ");
		blank1.setPrefix("  ");
		blank1.setSuffix("  ");
		o.getScore("  ").setScore(8);
		
		Score name = o.getScore(PlayersSpecs.nameColors.get(player) + player.getDisplayName());
		name.setScore(9);
		
		Score You = o.getScore(ChatColor.GRAY + "» " + ChatColor.WHITE + "You");
		You.setScore(10);
		
		
		
		player.setScoreboard(b);
		
	}	
	
	/**
	 * Update a scoreboard for a player when in the lobby.
	 * 
	 * @param player: the player whose scoreboard to update
	 */
	public void updateLobbyScoreboard(Player player) {
		
		Scoreboard b = player.getScoreboard();
		b.getTeam("playing").setSuffix("" + ChatColor.GREEN + PlayersSpecs.players.size());
		int timeLeft = Main.game.getTimeLeft();
		
		if (timeLeft == 60) {
			b.getObjective("Lobby").setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 1:00");
		} else if (timeLeft > 9) {
			b.getObjective("Lobby").setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 0:" + timeLeft);
		} else if (timeLeft >= 0){
			b.getObjective("Lobby").setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 0:0" + timeLeft);
		} else {
			b.getObjective("Lobby").setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 0:00");
		} 
		
	}
	
	/**
	 * Update a scoreboard for a player in PreGame.
	 * 
	 * @param player: the player whose scoreboard to update
	 */
	public void updatePreScoreboard(Player player) {
		updateScoreboard(player, Main.game.getTimeLeft(), "PreGame ");
	}
	
	/**
	 * Update a scoreboard for a player during INGAME.
	 * 
	 * @param player: the player whose scoreboard to update
	 */
	public void updateInGameScoreboard(Player player) {
		updateScoreboard(player, Main.game.getTimeLeft(), "LiveGame ");
	}
	
	/**
	 * Update a scoreboard for a player in PREDM.
	 * 
	 * @param player: the player whose scoreboard to update
	 */
	public void updatePreDMScoreboard(Player player) {
		updateScoreboard(player, Main.game.getTimeLeft(), "Pre-DM ");
	}
	
	/**
	 * Update a scoreboard for a player in DEATHMATCH
	 * 
	 * @param player: the player whose scoreboard to update
	 */
	public void updateDMScoreboard(Player player) {
		updateScoreboard(player, Main.game.getTimeLeft(), "Deathmatch ");
	}
	
	/**
	 * Update a scoreboard for a player in CLEANUP.
	 * 
	 * @param player: the player whose scoreboard to update
	 */
	public void updateCleanupScoreboard(Player player) {
		updateScoreboard(player, Main.game.getTimeLeft(), "GameEnd ");
	}
	
	/**
	 * Update a scoreboard for a player.
	 * 
	 * @param player: the player to update.
	 * @param timeLeft: how much time is left in the Game State.
	 * @param gameState: the current Game State.
	 */
	public void updateScoreboard(Player player, int timeLeft, String gameState) {
		Scoreboard b = player.getScoreboard();
		b.getTeam("playing").setSuffix("" + ChatColor.GREEN + PlayersSpecs.players.size());
		b.getTeam("watching").setSuffix("" + ChatColor.GREEN + PlayersSpecs.spectators.size());
		
		int minutes = timeLeft / 60;
		int seconds = timeLeft % 60;
		
		String mins;
		String secs;
		
		if (minutes > 9) {
			mins = String.valueOf(minutes);
		} else if (minutes >= 0){
			mins = "0" + String.valueOf(minutes);
		} else {
			mins = "00";
		}
		
		if (seconds > 9) {
			secs = String.valueOf(seconds);
		} else if (seconds >= 0){
			secs = "0" + String.valueOf(seconds);
		} else {
			secs = "00";
		}
		
		b.getObjective("Lobby").setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ gameState + ChatColor.RED + mins + ":" + secs );
		
	}
	
	
	
	

}
