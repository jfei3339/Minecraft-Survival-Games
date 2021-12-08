package me.Jon.SurvivalGames;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.entity.Player;

public class PlayersSpecs {
	
	public static HashSet<Player> players = new HashSet<Player>();
	public static HashSet<Player> spectators = new HashSet<Player>();
	public static HashMap<Player, String> nameColors = new HashMap<Player, String>(); 
	public static HashMap<Player, Integer> playersXP = new HashMap<Player, Integer>();
	public static HashMap<Player, Integer> playersKills = new HashMap<Player, Integer>();
	public static HashMap<Player, Integer> playersChests = new HashMap<Player, Integer>();

}
