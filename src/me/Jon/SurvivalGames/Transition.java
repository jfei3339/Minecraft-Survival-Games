package me.Jon.SurvivalGames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.Jon.SurvivalGames.Commands.VoteCommand;
import me.Jon.SurvivalGames.Data.MapSpawns;
import me.Jon.SurvivalGames.Events.ChestOpenEvent;
import me.Jon.SurvivalGames.ServerActions.LobbyActions;

public class Transition {
	
	public static String determineWinner() {
		
		String winner = VoteCommand.map1;
		int votes = VoteCommand.votes.get(1);
		
		for (int i = 1; i<=5; i++) {
			if (VoteCommand.votes.get(i) > votes) {
				winner = VoteCommand.mapNames.get(i);
				votes = VoteCommand.votes.get(i);
			}
		}
		
		ChestOpenEvent.getTier2Locations(winner);
		
		return winner;
		
	}
	
	public static void teleportPlayers(String map) {
		
		int pos = 1;
		for (Player p: PlayersSpecs.players) {
			
			p.setAllowFlight(false);
			LobbyActions.clearPlayer(p);
			
			String posNum = String.valueOf(pos);
			
			String map1 = map; //map.toLowerCase();
			
			
			double x = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + posNum + ".x");
			double y = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + posNum + ".y");
			double z = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + posNum + ".z");
			float yaw = (float) MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + posNum + ".yaw");
			float pitch = (float) MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + posNum + ".pitch");
			
			Location loc = new Location(Bukkit.getWorld(map1), x, y, z, yaw, pitch); 
			
			p.teleport(loc);
			
			pos++;
			
		}
		
	}
	
	public static void teleportDM(String map) {
		
		int pos = 1;
		for (Player p: PlayersSpecs.players) {
			
			String posNum = String.valueOf(pos);
			
			String map1 = map; //map.toLowerCase();
			
			double x = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + posNum + ".x");
			double y = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + posNum + ".y");
			double z = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + posNum + ".z");
			float yaw = (float) MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + posNum + ".yaw");
			float pitch = (float) MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + posNum + ".pitch");
			
			Location loc = new Location(Bukkit.getWorld(map1), x, y, z, yaw, pitch); 
			
			p.teleport(loc);
			
			pos += 7;
			
		}
		//teleports specs as well
		for (Player p: PlayersSpecs.spectators) {
			double x = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map + ".pos" + 0 + ".x");
			double y = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map + ".pos" + 0 + ".y");
			double z = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map + ".pos" + 0 + ".z");
			float yaw = (float) MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map + ".pos" + 0 + ".yaw");
			float pitch = (float) MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map + ".pos" + 0 + ".pitch");
			Location loc = new Location(Bukkit.getWorld(map), x, y, z, yaw, pitch); 
			p.teleport(loc);
		}
		
	}
	
	public static double DMLightningDist(String map) {
		String map1 = map; //map.toLowerCase();
		
		//spawn
		double x = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + 0 + ".x");
		//double y = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + 0 + ".y");
		double z = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + 0 + ".z");
		
		
		//arbitrary pos 1
		double x1 = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + 1 + ".x");
		//double y1 = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + 1 + ".y");
		double z1 = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + map1 + ".pos" + 1 + ".z");

		return Math.sqrt((x-x1)*(x-x1) + (z-z1)*(z-z1)); 
		
	}
	
	

}
