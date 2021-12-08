package me.Jon.SurvivalGames.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL_status {
	
	private String host = "178.63.127.184";
	private String port = "3306";
	private String database = "s5438_serverstatus";
	private String username = "u5438_E1t0WGAv0O";
	private String password = "aAEVYK7O@gmD.hbmVX45j!54";
	
	private Connection connection;
	
	public boolean isConnected() {
		return (connection == null ? false : true);
	}
	
	public void connect() throws ClassNotFoundException, SQLException {
		if (!isConnected()) {
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);
		}
	}
	
	public void disconnect() {
		if (isConnected()) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public Connection getConnection() {
		return connection;
	}

}
