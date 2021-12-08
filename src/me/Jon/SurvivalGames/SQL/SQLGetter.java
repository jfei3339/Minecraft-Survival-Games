package me.Jon.SurvivalGames.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Jon.SurvivalGames.Main;
import net.md_5.bungee.api.ChatColor;

public class SQLGetter {
	
	private Main plugin;
	
	public SQLGetter(Main plugin) {
		this.plugin = plugin;
		plugin.SQL.getConnection();
	}
	
	//name, uuid, rank, bans, mutes, unban/mute dates
	public void createInfoTable() {
		PreparedStatement ps;
		
		try {
			ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playerinfo "
					+ "(NAME VARCHAR(100),UUID VARCHAR(100),RANK VARCHAR(100),PRIMARY KEY (NAME))");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void createDataTable() {
		PreparedStatement ps;
		
		try {
			ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playerdata "
					+ "(NAME VARCHAR(100),UUID VARCHAR(100),XP INT(100),LEVEL INT(100),WINS INT(100),KILLS INT(100),LOSSES INT(100),KDR INT(100),CHESTS INT(100),PRIMARY KEY (NAME))");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void infoCreatePlayer(Player player) {
		try {
			UUID uuid = player.getUniqueId();
			
			if (!exists(uuid, "playerinfo")) {
				
				PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO playerinfo (NAME,UUID,RANK) VALUES (?,?,?)");
				ps2.setString(1, player.getName());
				ps2.setString(2, uuid.toString());
				ps2.setString(3, "NONE");
				ps2.executeUpdate();
				Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "New player created in playerinfo!");
				return;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void dataCreatePlayer(Player player) {
		try {
			UUID uuid = player.getUniqueId();
			
			if (!exists(uuid, "playerdata")) {
				
				PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO playerdata (NAME,UUID,XP,LEVEL,WINS,KILLS,LOSSES,KDR,CHESTS) VALUES (?,?,?,?,?,?,?,?,?)");
				ps2.setString(1, player.getName());
				ps2.setString(2, uuid.toString());
				ps2.setInt(3, 0);
				ps2.setInt(4, 1);
				ps2.setInt(5, 0);
				ps2.setInt(6, 0);
				ps2.setInt(7, 0);
				ps2.setInt(8, 0);
				ps2.setInt(9, 0);
				ps2.executeUpdate();
				Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "New player created in playerdata");
				return;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean exists(UUID uuid, String table) {
		try {
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
			ps.setString(1, uuid.toString());
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				return true;
			}
			return false;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void addXP(UUID uuid, int xp) {
		try {
			
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE playerdata SET XP=? WHERE UUID=?");
			ps.setInt(1, (getXP(uuid)+xp));
			ps.setString(2, uuid.toString());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getXP(UUID uuid) {
		try {
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT XP FROM playerdata WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			int points = 0;
			if (rs.next()) {
				points = rs.getInt("XP");
				return points;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getStat(UUID uuid, String stat) {
		try {
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT " + stat + " FROM playerdata WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			int num = 0;
			if (rs.next()) {
				num = rs.getInt(stat);
				return num;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int[] getAllStats(UUID uuid) {
		try {
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT LEVEL, WINS, LOSSES, KILLS, CHESTS FROM playerdata WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			int[] stats = new int[5];
			
			if (rs.next()) {
				stats[0] = rs.getInt("LEVEL");
				stats[1] = rs.getInt("WINS");
				stats[2] = rs.getInt("LOSSES");
				stats[3] = rs.getInt("KILLS");
				stats[4] = rs.getInt("CHESTS");
			}

			
			return stats;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new int[] {1};
	}
	
	public String getName(UUID uuid) {
		try {
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT NAME FROM playerdata WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			String name = "";
			if (rs.next()) {
				name = rs.getString("NAME");
				return name;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public void setStat(UUID uuid, String stat, int value) {
		try {
			
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE playerdata SET " + stat + "=? WHERE UUID=?");
			ps.setInt(1, value);
			ps.setString(2, uuid.toString());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setStat(UUID uuid, String stat, double value) {
		try {
			
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE playerdata SET " + stat + "=? WHERE UUID=?");
			ps.setDouble(1, value);
			ps.setString(2, uuid.toString());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getRank(UUID uuid) {
		try {
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT RANK FROM playerinfo WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			String rank = "";
			if (rs.next()) {
				rank = rs.getString("RANK");
				return rank;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "none";
		
	}
	
	public String dummy() {
		try {
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT RANK FROM playerinfo WHERE UUID=?");
			ps.setString(1, "19f49c90-412a-40a5-9e2d-0c517a43addc");
			ResultSet rs = ps.executeQuery();
			String rank = "";
			if (rs.next()) {
				rank = rs.getString("RANK");
				return rank;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "none";
	}

}
