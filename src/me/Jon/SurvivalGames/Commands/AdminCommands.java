package me.Jon.SurvivalGames.Commands;

import java.io.Console;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.Jon.SurvivalGames.Main;
import me.Jon.SurvivalGames.SGScoreboards;
import me.Jon.SurvivalGames.Transition;
import me.Jon.SurvivalGames.Events.ChestOpenEvent;
import net.md_5.bungee.api.ChatColor;

public class AdminCommands implements Listener, CommandExecutor {

	public String cmd1 = "forcenext";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		
		if (cmd.getName().equalsIgnoreCase(cmd1)) {
			
			if (sender instanceof Player && Main.connected == true) {
				
				String rank = Main.data.getRank(((Player) sender).getUniqueId());
				if (rank.equals("ADMIN") || rank.equals("OWNER")) {
					if (Main.gameState.equals("LOBBY")) {
						Main.winningMap = Transition.determineWinner();
						ChestOpenEvent.getTier2Locations(Main.winningMap.toLowerCase());
						
						Bukkit.broadcastMessage(Main.prefix + ChatColor.YELLOW + Main.winningMap + ChatColor.GREEN +  " will be played!");
						Main.flag = false;
						Transition.teleportPlayers(Main.winningMap);
						
						Bukkit.getWorld(Main.winningMap).setTime(100);
						
						//distance for lightning strikes in DM
						Main.DMDistance = 1.25*Transition.DMLightningDist(Main.winningMap);
						
						Main.gameState = "PREGAME";
						
						
						//new scoreboard
						for (Player p: Bukkit.getServer().getOnlinePlayers()) {
							p.getScoreboard().getObjective("Lobby").getScore(ChatColor.WHITE + "Watching: ").setScore(2);
							SGScoreboards.updatePreScoreboard(p);
						}
					}
				} else {
					sender.sendMessage("Command not available.");
				}
				
			} else {
				sender.sendMessage("Command not available.");
			}
			

			
		}
		return true;
	}
	
}

