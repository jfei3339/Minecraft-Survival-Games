package me.Jon.SurvivalGames.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.Jon.SurvivalGames.Game.GameState;
import me.Jon.SurvivalGames.Main;
import me.Jon.SurvivalGames.SGScoreboards;
import me.Jon.SurvivalGames.Transition;
import me.Jon.SurvivalGames.Events.ChestOpenEvent;
import net.md_5.bungee.api.ChatColor;

public class AdminCommands implements Listener, CommandExecutor {

	public String cmd1 = "forcenext";
	private final SGScoreboards scoreboards = new SGScoreboards();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase(cmd1)) {
			
			if (sender instanceof Player && Main.connectedToPlayerDB == true) {
				
				String rank = Main.playerData.getRank(((Player) sender).getUniqueId());
				if (rank.equals("ADMIN") || rank.equals("OWNER")) {
					if (Main.game.gameState.equals(GameState.LOBBY)) {
						Main.game.winningMap = Transition.determineWinningMap();
						ChestOpenEvent.getTier2Locations(Main.game.winningMap.toLowerCase());
						
						Bukkit.broadcastMessage(Main.prefix + ChatColor.YELLOW + Main.game.winningMap + ChatColor.GREEN +  " will be played!");
						Main.game.countdownResetFlag = false;
						Transition.teleportPlayers(Main.game.winningMap);
						
						Bukkit.getWorld(Main.game.winningMap).setTime(100);
						
						//distance for lightning strikes in DM
						Main.game.DMDistance = 1.25*Transition.DMLightningDist(Main.game.winningMap);
						
						Main.game.gameState = GameState.PREGAME;
						
						
						//new scoreboard
						for (Player p: Bukkit.getServer().getOnlinePlayers()) {
							p.getScoreboard().getObjective("Lobby").getScore(ChatColor.WHITE + "Watching: ").setScore(2);
							scoreboards.updatePreScoreboard(p);
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


