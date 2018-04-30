package server;

import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;

public class DataManager
{
	private static DataManager instance = null;
	private ServerSocket socket;
	private Connection db;

	private DataManager(){}
	
	public static DataManager getInstance()
	{
		return instance == null ? new DataManager() : instance;
	}

	public void init(ServerSocket socket)
	{
		this.socket = socket;
		this.db = null;
	      
		try 
		{
			Class.forName("org.sqlite.JDBC");
			this.db = DriverManager.getConnection("jdbc:sqlite:database.db");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println("Unable to establish connection to database");
			System.exit(-1); 
		}
	}
	
	public Connection getDatabase()
	{
		return this.db;
	}
}
