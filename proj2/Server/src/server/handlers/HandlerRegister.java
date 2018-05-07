package server.handlers;

import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import server.ServerManager;
import server.utils.Utils;

public class HandlerRegister extends Handler 
{
	public HandlerRegister(Socket socket, String username, String password) 
	{
		super("REGISTER", username, password, socket);
	}

	@Override
	public void run() 
	{
		String hash = Utils.hashPassword(password);
		/*
		Connection db = ServerManager.getInstance().getDatabase();
		try
		{
			PreparedStatement stmt = db.prepareStatement("insert into Users values (?, ?)");
			stmt.setString(1, username);
			stmt.setString(2, hash);
			stmt.executeUpdate();
		} 
		catch (SQLException e) 
		{
			//user exists
		}*/
	}
}
