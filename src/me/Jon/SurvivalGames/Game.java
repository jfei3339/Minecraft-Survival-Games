package me.Jon.SurvivalGames;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.Jon.SurvivalGames.Data.MapSpawns;
import me.Jon.SurvivalGames.Events.ChestOpenEvent;
import me.Jon.SurvivalGames.Events.SQLevents;
import me.Jon.SurvivalGames.SQL.SQLGetter;
import net.md_5.bungee.api.ChatColor;

/*
 * Class representing the Survival Games gamemode.
 */
public class Game {
	
	public final static int minPlayers = 6;
	
	private static final int lobbyMinTime = 60;
	private int lobbyTimeLeft = lobbyMinTime;
	
	private static final int preMinTime = 15;
	private int preTimeLeft = preMinTime;
	
	private static final int inGameMinTime = 1200;
	private int inGameTimeLeft = inGameMinTime;
	
	private static final int preDMMinTime = 10;
	private int preDMTimeLeft = preDMMinTime;
	
	private static final int DMMinTime = 180;
	private int DMTimeLeft = DMMinTime;
	
	private static final int cleanupMinTime = 10;
	private int cleanupTimeLeft = cleanupMinTime;
	
	private final int refill1Time = 600;
	private final int refill2Time = 120;
	
	//enum representing the current state of the game.
	public static enum GameState {
		LOBBY, 
		PREGAME,
		INGAME,
		PREDM,
		DEATHMATCH,
		CLEANUP,
		RESTARTING
	}
	
	public GameState gameState = GameState.LOBBY;
	private int count = 0;
	
//	private static int taskID;
	public boolean countdownResetFlag = true;
	private boolean DMcountdown = false;
	public String winningMap;
	public double DMDistance;
	private double xCenter;
	//private double yCenter;
	private double zCenter;
	
	private String prefix;
	private SQLGetter playerData;
	
	private SGScoreboards scoreboards = new SGScoreboards();
	
	
	public Game(String prefix, SQLGetter playerData) {
		this.prefix = prefix;
		this.playerData = playerData;
	}
	
	/**
	 * Method that controls the game with a runnable.
	 */
	public void start(Plugin plugin) {
	        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
	            @Override
	            public void run() {
	            	
	            	//dummy to make sure no database timeout
	            	if (count == 0) {
	            		if (Main.connectedToPlayerDB == true) {
	            			playerData.dummy();
	            		}
	            	}

	            	count++;
	            	if (count == 60) {
	            		count = 0;
	            		if (Main.connectedToPlayerDB == true) {
	            			playerData.dummy();
	            		}
	            		
	            		if (gameState.equals(GameState.INGAME)) {
	            			Bukkit.broadcastMessage(prefix + StringFunctions.surround(String.valueOf(PlayersSpecs.players.size()), ChatColor.RED + "") + ChatColor.GREEN + " tribute(s) still remain.");
	            		}
	            	}
	            	
	            	//Updating scoreboard
					for (Player p: Bukkit.getServer().getOnlinePlayers()) {
						if (gameState.equals(GameState.LOBBY)) {
							scoreboards.updateLobbyScoreboard(p);	
						} else if (gameState.equals(GameState.PREGAME))	{
							scoreboards.updatePreScoreboard(p);
						} else if (gameState.equals(GameState.INGAME)) {
							scoreboards.updateInGameScoreboard(p);
						} else if (gameState.equals(GameState.PREDM)) {
							scoreboards.updatePreDMScoreboard(p);
						} else if (gameState.equals(GameState.DEATHMATCH)) {
							scoreboards.updateDMScoreboard(p);
						} else if (gameState.equals(GameState.CLEANUP)) {
							scoreboards.updateCleanupScoreboard(p);
						}
								
					} 		
					
					//LOBBY
					if (gameState.equals(GameState.LOBBY)) {
						if (Bukkit.getOnlinePlayers().size() >= minPlayers && countdownResetFlag == true) {
							
							lobbyTimeLeft -= 1;
							
							if (lobbyTimeLeft == -1) {
								
								
								//Declare map winner, teleport players to map.
								winningMap = Transition.determineWinningMap();
								ChestOpenEvent.getTier2Locations(winningMap);
								
								Bukkit.broadcastMessage(prefix + ChatColor.YELLOW + winningMap + ChatColor.GREEN +  " will be played!");
								countdownResetFlag = false;
								Transition.teleportPlayers(winningMap);
								Bukkit.getWorld(winningMap).setTime(0);
								//distance for lightning strikes in DM
								DMDistance = 1.3*Transition.DMLightningDist(winningMap);
								gameState = GameState.PREGAME;
								
								//new scoreboard
								for (Player p: Bukkit.getServer().getOnlinePlayers()) {
									p.getScoreboard().getObjective("Lobby").getScore(ChatColor.WHITE + "Watching: ").setScore(2);
									scoreboards.updatePreScoreboard(p);
								}
				
							}
							if (lobbyTimeLeft <= 9 && lobbyTimeLeft >= 0) {
								Bukkit.broadcastMessage(prefix + ChatColor.RED + (lobbyTimeLeft+1) + ChatColor.GREEN + " seconds left until voting ends!");
							}
						}
						
						if (Bukkit.getOnlinePlayers().size() < minPlayers) {
							countdownResetFlag = true;
							lobbyTimeLeft = lobbyMinTime;						
						}
					}
					
					//PREGAME
					if (gameState.equals(GameState.PREGAME))	{ 
						
						preTimeLeft -= 1;
						
						if (preTimeLeft <= 9 && preTimeLeft >= 0) {
							Bukkit.broadcastMessage(prefix + ChatColor.RED + (preTimeLeft+1) + ChatColor.GREEN + " seconds left until the game starts!");
						}
						
						if (preTimeLeft == -1) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Let the Games Begin!");
							gameState = GameState.INGAME;
							for (Player p: Bukkit.getOnlinePlayers()) {
								scoreboards.updateInGameScoreboard(p);
							}
						}
						
						//did someone win?
						if (PlayersSpecs.players.size() == 1) {
							Celebration.celebrate();
						} else if (PlayersSpecs.players.size() == 0) {
							gameState = GameState.CLEANUP;
						}
					}
					
					//INGAME	
					if (gameState.equals(GameState.INGAME)) {
						
						inGameTimeLeft -= 1;
						
						if (PlayersSpecs.players.size() <= 3 && DMcountdown == false) {
							DMcountdown = true;
							if (inGameTimeLeft > 60) {
								inGameTimeLeft = 60;
								for (Player p: Bukkit.getOnlinePlayers()) {
									scoreboards.updateInGameScoreboard(p);
								}
							}
							
						}
						
						//chest refill
						if (inGameTimeLeft == refill1Time-1) {
							ChestOpenEvent.refill1 = true;
							Bukkit.broadcastMessage(prefix + ChatColor.YELLOW + "Sponsors have refilled the chests!");	
						} else if (inGameTimeLeft == refill2Time-1) {
							ChestOpenEvent.refill2 = true;
							Bukkit.broadcastMessage(prefix + ChatColor.YELLOW + "Sponsors have refilled the chests!");	
						}
						
						if (inGameTimeLeft == 59) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Players will be teleported to Deathmatch in 60 seconds!");						
						} else if (inGameTimeLeft == 29) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Players will be teleported to Deathmatch in 30 seconds!");
						} else if (inGameTimeLeft <= 9 && inGameTimeLeft >= 0) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Players will be teleported to Deathmatch in " + ChatColor.RED + (inGameTimeLeft+1) + ChatColor.GREEN + " seconds!");
						} else if (inGameTimeLeft == -1) {
							
							Transition.teleportDM(winningMap);
							gameState = GameState.PREDM;
							
							xCenter = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + winningMap + ".pos" + 0 + ".x");
							//yCenter = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + winningMap + ".pos" + 0 + ".y");
							zCenter = MapSpawns.mapSpawnsConfig.getDouble("Maps. " + winningMap + ".pos" + 0 + ".z");
						
							for (Player p: Bukkit.getOnlinePlayers()) {
								scoreboards.updatePreDMScoreboard(p);
							}
						}
						
						//did someone win?
						if (PlayersSpecs.players.size() == 1) {
							Celebration.celebrate();
						} else if (PlayersSpecs.players.size() == 0) {
							gameState = GameState.CLEANUP;
						}
					}
					
					//PREDM
					if (gameState.equals(GameState.PREDM)) {
						preDMTimeLeft -= 1;
						
						if (preDMTimeLeft <= 9 && preDMTimeLeft >= 0) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Deathmatch will begin in " + ChatColor.RED + (preDMTimeLeft+1) + ChatColor.GREEN +  " seconds!");
						} else if (preDMTimeLeft == -1) {
							gameState = GameState.DEATHMATCH;
						}
						
						//did someone win?
						if (PlayersSpecs.players.size() == 1) {
							Celebration.celebrate();
						} else if (PlayersSpecs.players.size() == 0) {
							gameState = GameState.CLEANUP;
						}
					}
					
					//DM
					if (gameState.equals(GameState.DEATHMATCH)) {
						DMTimeLeft -= 1;
						
						//get lightning dist
						for (Player p: PlayersSpecs.players) {
							if (DMTimeLeft % 3 == 0) {
								Location loc = p.getLocation();
								int x = loc.getBlockX();
								int z = loc.getBlockZ();
								if (Math.sqrt((x-xCenter)*(x-xCenter) + (z-zCenter)*(z-zCenter)) > DMDistance) {
									p.getLocation().getWorld().spawnEntity(p.getLocation(), EntityType.LIGHTNING);
									p.sendMessage(prefix + ChatColor.RED + "Please return to spawn!"); 
									
								}
								
							}
							
						}
						
						if (DMTimeLeft == 119) 
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Deathmatch will end in " + ChatColor.RED + 2 + ChatColor.GREEN +  " minutes!");
						else if (DMTimeLeft == 59) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Deathmatch will end in " + ChatColor.RED + (DMTimeLeft+1) + ChatColor.GREEN +  " seconds!");
							DMDistance /= 2;
						} else if (DMTimeLeft == 29) {
							DMDistance /= 2;
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Deathmatch will end in " + ChatColor.RED + (DMTimeLeft+1) + ChatColor.GREEN +  " seconds!");
						} else if (DMTimeLeft <= 9 && DMTimeLeft >= 0) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "Deathmatch will begin in " + ChatColor.RED + (DMTimeLeft+1) + ChatColor.GREEN +  " seconds!");
							DMDistance /= 1.2;
						} else if (DMTimeLeft == -1) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "The game has ended!");
							gameState = GameState.CLEANUP;
						}
						
						//did someone win?
						if (PlayersSpecs.players.size() == 1) Celebration.celebrate();
						else if (PlayersSpecs.players.size() == 0) gameState = GameState.CLEANUP;
						
					}
					
					//CLEANUP
					if (gameState.equals(GameState.CLEANUP)) {
						cleanupTimeLeft -= 1;
						
						if (cleanupTimeLeft == -1) {
							Bukkit.broadcastMessage(prefix + ChatColor.GREEN + "The server is restarting.");
							
							for (Player p: Bukkit.getOnlinePlayers()) {
								p.kickPlayer("Server is restarting");
								//bungee.connect(p, "hub"); //disabled for now
							}
							
						}
						
						if (cleanupTimeLeft == -4) {
							
							//update stats at once at the end of the game
							if (Main.connectedToPlayerDB == true) {
								for (Player p: PlayersSpecs.playersKills.keySet()) {
									UUID uuid = p.getUniqueId();
									
									playerData.setStat(uuid, "XP", playerData.getStat(uuid, "XP") + PlayersSpecs.playersXP.get(p));
									playerData.setStat(uuid, "KILLS", playerData.getStat(uuid, "KILLS") + PlayersSpecs.playersKills.get(p));
									playerData.setStat(uuid, "CHESTS", playerData.getStat(uuid, "CHESTS") + PlayersSpecs.playersChests.get(p));

									Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "\nPLAYER STATS UPDATED!\n");
									
									int newLevel = SQLevents.getProgress(p)[0];
									
									playerData.setStat(uuid, "LEVEL", newLevel);
									
								}
							}
							Bukkit.shutdown();	
						}
					}
	            }
	        }, 0L, 20L);
	    }
	
	/**
	 * Gets the amount of time on the clock remaining in the current game state.
	 * 
	 * @return the amount of time in seconds on the clock remaining in the current game state.
	 */
    public int getTimeLeft() {
    	int timeLeft = 0;
    	switch (gameState) {
	    	case LOBBY: 
	    		timeLeft = lobbyTimeLeft;
	    		break;
	    	case PREGAME: 
	    		timeLeft = preTimeLeft;
	    		break;
	    	case INGAME: 
	    		timeLeft = inGameTimeLeft;
	    		break;
	    	case PREDM: 
	    		timeLeft = preDMTimeLeft;
	    		break;
	    	case DEATHMATCH: 
	    		timeLeft = DMTimeLeft;
	    		break;
	    	case CLEANUP: 
	    		timeLeft = cleanupTimeLeft;
	    		break;
	    	case RESTARTING: 
	    		timeLeft = 0;
	    		break;
    	}

    	return timeLeft;
    }
    
    /**
     * Gets the name of map with most votes in the lobby.
     * 
     * @return the name of the winning map
     */
    public String getWinningMap() {
    	return this.winningMap;
    }
	

}
