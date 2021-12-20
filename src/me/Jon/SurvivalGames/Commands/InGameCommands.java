package me.Jon.SurvivalGames.Commands;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


import me.Jon.SurvivalGames.Main;
import me.Jon.SurvivalGames.PlayersSpecs;
import me.Jon.SurvivalGames.PluginMessage;
import me.Jon.SurvivalGames.StringFunctions;
import me.Jon.SurvivalGames.Events.SQLevents;
import net.md_5.bungee.api.ChatColor;

public class InGameCommands implements Listener, CommandExecutor {

	public String cmd1 = "spec";
	public String cmd2 = "spectate";
	public String cmd3 = "force";
	public String cmd4 = "list";
	public String cmd5 = "l";
	public String cmd6 = "stats";
	public String cmd7 = "hub";
	public String cmd8 = "progress";
	public String cmd9 = "level";
	
	private PluginMessage bungee = new PluginMessage();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			
			
			//spectate comand
			if (cmd.getName().equalsIgnoreCase(cmd1) || cmd.getName().equalsIgnoreCase(cmd2)) {
				
				if (PlayersSpecs.spectators.contains(player)) {
					
					if (args.length == 0) {
						player.sendMessage(Main.prefix + ChatColor.RED + "Usage: /spec (playername).");
					} else {
						
						if (PlayersSpecs.players.contains( Bukkit.getPlayer(args[0]) )) {
							
							player.teleport(Bukkit.getPlayer(args[0]));
							
						} else {
							player.sendMessage(Main.prefix + ChatColor.RED + "Player not found.");
						}
						
					}
					
				} else {
					player.sendMessage(Main.prefix + ChatColor.RED + "You are not a spectator.");
				}
				
			}
			
			//list players/specs command
			if (cmd.getName().equalsIgnoreCase(cmd4) || cmd.getName().equalsIgnoreCase(cmd5)) {
				String playing = ChatColor.BOLD + "Playing: ";
				for (Player p:PlayersSpecs.players) {
					playing += ChatColor.DARK_GREEN + p.getDisplayName() + ChatColor.GRAY + ", ";
				}
				//get rid of comma
				if (PlayersSpecs.players.size() > 0) {
					playing = playing.substring(0, playing.length() - 2);
				}
				
				
				String watching = ChatColor.BOLD + "Watching: ";
				for (Player p:PlayersSpecs.spectators) {
					watching += ChatColor.DARK_GREEN + p.getDisplayName() + ChatColor.GRAY + ", ";
				}
				//get rid of comma
				if (PlayersSpecs.spectators.size() > 0) {
					watching = watching.substring(0, watching.length() - 2);
				}
				
				
				player.sendMessage(playing);
				player.sendMessage(watching);
				
			}
			
			//stats command
			if (cmd.getName().equalsIgnoreCase(cmd6)) {
				
				if (Main.connected == true) {
					
					if (args.length == 0) {
						//get sender's stats
						UUID uuid = player.getUniqueId();
						
						int[] stats = Main.data.getAllStats(uuid);
						
						double K = (double) stats[3];
						double D = (double) stats[2];
						if (D == 0) {
							D = 1.0;
						}
						double kdr = K/D;
						
						player.sendMessage(ChatColor.BOLD + "Your Stats");
						player.sendMessage(ChatColor.DARK_GREEN + "Level: " + StringFunctions.surround(String.valueOf(stats[0]) + "★", ChatColor.YELLOW + "") + ChatColor.YELLOW);
						player.sendMessage(ChatColor.DARK_GREEN + "Wins: " + StringFunctions.surround(String.valueOf(stats[1]), ChatColor.YELLOW + ""));
						player.sendMessage(ChatColor.DARK_GREEN + "Kills: " + StringFunctions.surround(String.valueOf(stats[3]), ChatColor.YELLOW + ""));
						player.sendMessage(ChatColor.DARK_GREEN + "Losses/Deaths" + StringFunctions.surround(String.valueOf(stats[2]), ChatColor.YELLOW + ""));
						player.sendMessage(ChatColor.DARK_GREEN + "KDR: " + StringFunctions.surround(String.valueOf(kdr), ChatColor.YELLOW + ""));
						player.sendMessage(ChatColor.DARK_GREEN + "Chests Opened: " + StringFunctions.surround(String.valueOf(stats[4]), ChatColor.YELLOW + ""));
						
					} else {
						UUID uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
						
						if (Main.data.exists(uuid, "playerdata")) {
							
							String name = Main.data.getName(uuid);
							//String rank = Main.data.getRank(uuid);

							//level wins losses kills chests
							int[] stats = Main.data.getAllStats(uuid);
							
							double K = (double) stats[3];
							double D = (double) stats[2];
							if (D == 0) {
								D = 1.0;
							}
							double kdr = K/D;
							
							player.sendMessage(ChatColor.BOLD + name + "'s Stats");
							//player.sendMessage(ChatColor.DARK_GREEN + "Rank: " + ChatColor.WHITE + rank);
							player.sendMessage(ChatColor.DARK_GREEN + "Level: " + StringFunctions.surround(String.valueOf(stats[0]) + "★", ChatColor.YELLOW + "") + ChatColor.YELLOW);
							player.sendMessage(ChatColor.DARK_GREEN + "Wins: " + StringFunctions.surround(String.valueOf(stats[1]), ChatColor.YELLOW + ""));
							player.sendMessage(ChatColor.DARK_GREEN + "Kills: " + StringFunctions.surround(String.valueOf(stats[3]), ChatColor.YELLOW + ""));
							player.sendMessage(ChatColor.DARK_GREEN + "Losses/Deaths" + StringFunctions.surround(String.valueOf(stats[2]), ChatColor.YELLOW + ""));
							player.sendMessage(ChatColor.DARK_GREEN + "KDR: " + StringFunctions.surround(String.valueOf(kdr), ChatColor.YELLOW + ""));
							player.sendMessage(ChatColor.DARK_GREEN + "Chests Opened: " + StringFunctions.surround(String.valueOf(stats[4]), ChatColor.YELLOW + ""));
							
							
						} else {
							player.sendMessage(Main.prefix + ChatColor.RED + "Player not found.");
						}

					}
					
				} else {
					player.sendMessage(Main.prefix + ChatColor.RED + "Stats are not available right now.");
				}
				
			}
			
			if (cmd.getName().equalsIgnoreCase(cmd7)) {
				
				player.sendMessage(ChatColor.GREEN + "Connecting you to hub...");
				bungee.connect(player, "hub");
			}
			
			if (cmd.getName().equalsIgnoreCase(cmd8) || cmd.getName().equalsIgnoreCase(cmd9)) {
				UUID uuid = player.getUniqueId();
				
				int xp = Main.data.getStat(uuid, "XP");
				int[] progress = SQLevents.getProgress(player);
				player.sendMessage(ChatColor.DARK_GREEN + "Your Leveling Progress");
				player.sendMessage(ChatColor.WHITE + "Total XP earned: " + ChatColor.GOLD + xp);
				player.sendMessage(ChatColor.WHITE + "Current Level: " + ChatColor.GOLD + progress[0]);
				player.sendMessage(ChatColor.WHITE + "Experience to Next Level: " + ChatColor.GOLD + (progress[1]-xp));
			}
			
			
			
		}
		
		return true;
	}
	

}
