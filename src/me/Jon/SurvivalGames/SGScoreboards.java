package me.Jon.SurvivalGames;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import me.Jon.SurvivalGames.Data.ServerInfo;
import me.Jon.SurvivalGames.Main.GameState;
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
	public static void createScoreboard(Player player) {
		
		ScoreboardManager m = Bukkit.getScoreboardManager();
		Scoreboard b = m.getNewScoreboard();
		
		Objective o = b.registerNewObjective("Lobby", "dummy");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		if (Main.gameState.equals(GameState.LOBBY)) {
			if (Main.lobbyTimeLeft == 60) {
				o.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 1:00");
			} else if (Main.lobbyTimeLeft > 9) {
				o.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 0:" + Main.lobbyTimeLeft);
			} else if (Main.lobbyTimeLeft >= 0){
				o.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 0:0" + Main.lobbyTimeLeft);
			} else {
				o.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 0:00");
			}
		} else if (Main.gameState.equals(GameState.PREGAME)) {
			if (Main.preTimeLeft > 9) {
				b.getObjective("Lobby").setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "PreGame" + ChatColor.RED + " 0:" + Main.preTimeLeft);
			} else if (Main.preTimeLeft >= 0){
				b.getObjective("Lobby").setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "PreGame" + ChatColor.RED + " 0:0" + Main.preTimeLeft);
			} else {
				b.getObjective("Lobby").setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "PreGame" + ChatColor.RED + " 0:00");
			}
		}

		
		Score serverIP = o.getScore(Main.serverIP);
		serverIP.setScore(0);
		
		Score blank_ = o.getScore("    ");
		blank_.setScore(1);
		
		Team watching = b.registerNewTeam("watching");
		watching.addEntry(ChatColor.WHITE + "Watching: ");
		watching.setPrefix("");
		watching.setSuffix("" + ChatColor.GREEN + PlayersSpecs.spectators.size());
		
		if (Main.gameState.equals(GameState.PREGAME) || Main.gameState.equals(GameState.INGAME) || Main.gameState.equals(GameState.PREDM) || Main.gameState.equals(GameState.DEATHMATCH) || Main.gameState.equals(GameState.CLEANUP) ) {
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
	public static void updateLobbyScoreboard(Player player) {
		
		Scoreboard b = player.getScoreboard();
		b.getTeam("playing").setSuffix("" + ChatColor.GREEN + PlayersSpecs.players.size());
		if (Main.lobbyTimeLeft == 60) {
			b.getObjective("Lobby").setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 1:00");
		} else if (Main.lobbyTimeLeft > 9) {
			b.getObjective("Lobby").setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 0:" + Main.lobbyTimeLeft);
		} else if (Main.lobbyTimeLeft >= 0){
			b.getObjective("Lobby").setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 0:0" + Main.lobbyTimeLeft);
		} else {
			b.getObjective("Lobby").setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD+ "Lobby" + ChatColor.RED + " 0:00");
		} 
		
	}
	
	/**
	 * Update a scoreboard for a player in PreGame.
	 * 
	 * @param player: the player whose scoreboard to update
	 */
	public static void updatePreScoreboard(Player player) {
		updateScoreboard(player, Main.preTimeLeft, "PreGame ");
	}
	
	/**
	 * Update a scoreboard for a player during INGAME.
	 * 
	 * @param player: the player whose scoreboard to update
	 */
	public static void updateInGameScoreboard(Player player) {
		updateScoreboard(player, Main.inGameTimeLeft, "LiveGame ");
	}
	
	/**
	 * Update a scoreboard for a player in PREDM.
	 * 
	 * @param player: the player whose scoreboard to update
	 */
	public static void updatePreDMScoreboard(Player player) {
		updateScoreboard(player, Main.preDMTimeLeft, "Pre-DM ");
	}
	
	/**
	 * Update a scoreboard for a player in DEATHMATCH
	 * 
	 * @param player: the player whose scoreboard to update
	 */
	public static void updateDMScoreboard(Player player) {
		updateScoreboard(player, Main.DMTimeLeft, "Deathmatch ");
	}
	
	/**
	 * Update a scoreboard for a player in CLEANUP.
	 * 
	 * @param player: the player whose scoreboard to update
	 */
	public static void updateCleanupScoreboard(Player player) {
		updateScoreboard(player, Main.cleanupTimeLeft, "GameEnd ");
	}
	
	/**
	 * Update a scoreboard for a player.
	 * 
	 * @param player: the player to update.
	 * @param timeLeft: how much time is left in the Game State.
	 * @param gameState: the current Game State.
	 */
	public static void updateScoreboard(Player player, int timeLeft, String gameState) {
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
