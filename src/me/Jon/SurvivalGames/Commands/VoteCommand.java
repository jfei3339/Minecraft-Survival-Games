package me.Jon.SurvivalGames.Commands;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.Jon.SurvivalGames.Main;
import me.Jon.SurvivalGames.StringFunctions;
import me.Jon.SurvivalGames.Main.GameState;
import net.md_5.bungee.api.ChatColor;


public class VoteCommand implements Listener, CommandExecutor {
	
	public static String prefix = Main.prefix;
	
	
	
	public static String map1 = "Par 72";
	public static String map2 = "Zone 85 Revamped";
	public static String map3 = "Alaskan Village";
	public static String map4 = "Survival Games 4";
	public static String map5 = "Valleyside University";
	
	
	public static HashSet<String> voters = new HashSet<String>();
	public static HashMap<Integer, String> mapNames = new HashMap<Integer,String>();
	static {
		mapNames.put(1, map1); 
		mapNames.put(2, map2);
		mapNames.put(3, map3);
		mapNames.put(4, map4);
		mapNames.put(5, map5);
	}
	
	public static HashMap<Integer, Integer> votes = new HashMap<Integer, Integer>();
	static {
		votes.put(1, 0);
		votes.put(2, 0);
		votes.put(3, 0);
		votes.put(4, 0);
		votes.put(5, 0);
	}

	
	//commands
	public String cmd1 = "vote";
	public String cmd2 = "v";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			
			//VOTING
			if (cmd.getName().equalsIgnoreCase(cmd1) || cmd.getName().equalsIgnoreCase(cmd2)) {
				
				if (!Main.gameState.equals(GameState.LOBBY)) {
					player.sendMessage(prefix + ChatColor.GREEN + "Voting has ended.");
					return true;
				}
				
				if (voters.contains(player.getUniqueId().toString()) && args.length > 0) {
					player.sendMessage(prefix + ChatColor.GREEN + "You have already voted!");
					return true;
				}
				
				if (args.length == 0) {
					player.sendMessage(prefix + StringFunctions.surround("1", ChatColor.RED + "") + ChatColor.GREEN + mapNames.get(1) + ": " 
							+ StringFunctions.surround(String.valueOf(votes.get(1)), ChatColor.YELLOW + "") + ChatColor.GREEN + "Votes");
					player.sendMessage(prefix + StringFunctions.surround("2", ChatColor.RED + "") + ChatColor.GREEN +mapNames.get(2) + ": " 
							+ StringFunctions.surround(String.valueOf(votes.get(2)), ChatColor.YELLOW + "") + ChatColor.GREEN + "Votes");
					player.sendMessage(prefix + StringFunctions.surround("3", ChatColor.RED + "") + ChatColor.GREEN +mapNames.get(3) + ": " 
							+ StringFunctions.surround(String.valueOf(votes.get(3)), ChatColor.YELLOW + "") + ChatColor.GREEN + "Votes");
					player.sendMessage(prefix + StringFunctions.surround("4", ChatColor.RED + "") + ChatColor.GREEN +mapNames.get(4) + ": " 
							+ StringFunctions.surround(String.valueOf(votes.get(4)), ChatColor.YELLOW + "") + ChatColor.GREEN + "Votes");
					player.sendMessage(prefix + StringFunctions.surround("5", ChatColor.RED + "") + ChatColor.GREEN +mapNames.get(5) + ": " 
							+ StringFunctions.surround(String.valueOf(votes.get(5)), ChatColor.YELLOW + "") + ChatColor.GREEN + "Votes");
					return true;
				}
				
				int numVotes = 1;
				
				if (Main.connectedToPlayerDB == true) {
					
					String rank = Main.playerData.getRank(player.getUniqueId());
					if (rank.equals("DIAMOND")) {
						numVotes = 4;
					} else if (rank.equals("GOLD")) {
						numVotes = 3;
					} else if (rank.equals("SILVER")) {
						numVotes = 2;
					} else if (rank.equals("ADMIN")) {
						numVotes = 4;
					} else if (rank.equals("OWNER")) {
						numVotes = 4;
					}
				}
				
				if (args[0].equalsIgnoreCase("1")) {
					votes.put(1, votes.get(1)+numVotes);
					voters.add(player.getUniqueId().toString());
					player.sendMessage(prefix + ChatColor.GREEN + "You voted for " + map1 + ".");
					return true;
				} else if (args[0].equalsIgnoreCase("2")) {
					votes.put(2, votes.get(2)+numVotes);
					voters.add(player.getUniqueId().toString());
					player.sendMessage(prefix + ChatColor.GREEN + "You voted for " + map2 + ".");
					return true;
				} else if (args[0].equalsIgnoreCase("3")) {
					votes.put(3, votes.get(3)+numVotes);
					voters.add(player.getUniqueId().toString());
					player.sendMessage(prefix + ChatColor.GREEN + "You voted for " + map3 + ".");
					return true;
				} else if (args[0].equalsIgnoreCase("4")) {
					votes.put(4, votes.get(4)+numVotes);
					voters.add(player.getUniqueId().toString());
					player.sendMessage(prefix + ChatColor.GREEN + "You voted for " + map4 + ".");
					return true;
				} else if (args[0].equalsIgnoreCase("5")) {
					votes.put(5, votes.get(5)+numVotes);
					voters.add(player.getUniqueId().toString());
					player.sendMessage(prefix + ChatColor.GREEN + "You voted for " + map5 + ".");
					return true;
				} else {
					System.out.println(args[0]);
					player.sendMessage(prefix + ChatColor.GREEN + "That map doesn't exist!");
					return true;
				}
		
				
			}
			
			//
		}
		
		return true;
	}

}
