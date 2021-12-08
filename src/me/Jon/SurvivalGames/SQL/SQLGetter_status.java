package me.Jon.SurvivalGames.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import me.Jon.SurvivalGames.Main;

public class SQLGetter_status {
	
	private Main plugin;
	
	public SQLGetter_status(Main plugin) {
		this.plugin = plugin;
		plugin.SQL_status.getConnection();
	}
	
	//name, uuid, rank, bans, mutes, unban/mute dates
	public void createStatusTable() {
		PreparedStatement ps;
		
		try {
			ps = plugin.SQL_status.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS serverstatus "
					+ "(NAME VARCHAR(100),STATUS VARCHAR(100),PLAYERS INT(100),PRIMARY KEY (NAME))");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getStatus(String server) {
		try {
			PreparedStatement ps = plugin.SQL_status.getConnection().prepareStatement("SELECT STATUS FROM serverstatus WHERE NAME=?");
			ps.setString(1, server);
			ResultSet rs = ps.executeQuery();
			String status = "";
			if (rs.next()) {
				status = rs.getString("STATUS");
				return status;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public Integer getNumPlayers(String server) {
		try {
			PreparedStatement ps = plugin.SQL_status.getConnection().prepareStatement("SELECT PLAYERS FROM serverstatus WHERE NAME=?");
			ps.setString(1, server);
			ResultSet rs = ps.executeQuery();
			Integer status = -1;
			if (rs.next()) {
				status = rs.getInt("PLAYERS");
				return status;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public void setStat(String server, String stat, int value) {
		try {
			
			PreparedStatement ps = plugin.SQL_status.getConnection().prepareStatement("UPDATE serverstatus SET " + stat + "=? WHERE NAME=?");
			ps.setInt(1, value);
			ps.setString(2, server);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setStat(String server, String stat, String value) {
		try {
			
			PreparedStatement ps = plugin.SQL_status.getConnection().prepareStatement("UPDATE serverstatus SET " + stat + "=? WHERE NAME=?");
			ps.setString(1, value);
			ps.setString(2, server);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
