package server;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ServerManager
{
	private static ServerManager instance = null;
	private static final String STATEFILE = ".state";
	
	private UserRegistry users;
	private OnlineUsers onlineUsers;
	private FileStorage files;
	
	private ServerManager()
	{
		try
		{
			InputStream inputStream = new BufferedInputStream(new FileInputStream(STATEFILE));
			ObjectInput objectStream = new ObjectInputStream(inputStream);
			this.users = (UserRegistry)objectStream.readObject();
			this.onlineUsers = (OnlineUsers)objectStream.readObject();
			this.files = (FileStorage)objectStream.readObject();
			objectStream.close();
		}
		catch(IOException | ClassNotFoundException e)
		{
			System.out.println("No serialized state found; starting from a blank state");
			this.users = new UserRegistry();
			this.onlineUsers = new OnlineUsers();
			this.files = new FileStorage();
		}
	}
	
	public static ServerManager getInstance()
	{
		return instance == null ? new ServerManager() : instance;
	}
	
	public boolean userExists(String username)
	{
		return users.exists(username);
	}
	
	public void registerUser(String username, String passwordHash)
	{
		users.register(username, passwordHash);
	}
	
	public boolean loginUser(String username, String passwordHash, Socket socket)
	{
		if (!users.verifyUser(username, passwordHash))
			return false;
		onlineUsers.setUserOnline(username, socket);
		return true;
	}
	
	public void logoutUser(String username)
	{
		onlineUsers.setUserOffline(username);
	}
	
	public void saveTrack(String username, String title, byte[] data)
	{
		users.addTrackToUser(username, title);
		files.save(title, data);
	}
	
	public ArrayList<String> searchTrack(String title)
	{
		return files.search(title);
	}
	
	public byte[] getTrack(String title)
	{
		return files.retrieve(title);
	}
}
