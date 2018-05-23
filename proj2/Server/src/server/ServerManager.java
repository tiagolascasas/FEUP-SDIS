package server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ServerManager
{
	private static final ServerManager instance = new ServerManager();
	private static final String STATEFILE = "state";
	
	private UserRegistry users;
	private OnlineUsers onlineUsers;
	private FileStorage files;
	private boolean enableStdoutLogging;
	private AsynchronousFileChannel fileChannel;
	
	private ServerManager()
	{
		try
		{
			InputStream inputStream = new BufferedInputStream(new FileInputStream(STATEFILE));
			ObjectInput objectStream = new ObjectInputStream(inputStream);
			this.files = (FileStorage)objectStream.readObject();
			this.users = (UserRegistry)objectStream.readObject();
			this.onlineUsers = new OnlineUsers();
			objectStream.close();
			System.out.println("Successfully accessed persistent data");
		}
		catch(IOException | ClassNotFoundException e)
		{
			System.out.println("No persistent data found; starting from a blank state");
			this.users = new UserRegistry();
			this.onlineUsers = new OnlineUsers();
			this.files = new FileStorage();
		}
	}
	
	public static ServerManager getInstance()
	{
		return instance;
	}
	
	public synchronized boolean registerUser(String username, String passwordHash)
	{
		return users.register(username, passwordHash);
	}
	
	public synchronized boolean userIsRegistered(String username)
	{
		return users.verifyUsername(username);
	}
	
	public synchronized boolean passwordIsCorrect(String username, String passwordHash)
	{
		return users.verifyPassword(username, passwordHash);
	}
	
	public synchronized boolean loginUser(String username, String passwordHash, Socket socket)
	{
		if (onlineUsers.isOnline(username, socket))
			return false;
		
		System.out.println("ONLINE USERS:");
		System.out.print(onlineUsers.toString());
		
		onlineUsers.setUserOnline(username, socket);
		return true;
	}
	
	public synchronized void logoutUser(String username)
	{
		onlineUsers.setUserOffline(username);
	}
	
	public synchronized int saveTrack(String username, String title, byte[] data)
	{
		int res = files.save(title, data);
		if(res == 1)
			users.addTrackToUser(username, title);
		return res;
	}
	
	public synchronized ArrayList<String> searchTrack(String title)
	{
		return files.search(title);
	}
	
	public synchronized byte[] getTrack(String title)
	{
		return files.retrieve(title);
	}

	public synchronized void log(String s)
	{
		byte[] data = s.getBytes();
		try
		{
			ByteBuffer buffer = ByteBuffer.allocate(data.length);
			buffer.put(data);
			buffer.flip();
			this.fileChannel.write(buffer, 0).get();
			buffer.clear();
		} 
		catch (InterruptedException | ExecutionException e)
		{
			e.printStackTrace();
		}

		if (enableStdoutLogging)
			System.out.println(s);
	}

	public synchronized void setLogging(boolean enableStdoutLogging)
	{
		this.enableStdoutLogging = enableStdoutLogging;
		long unixTime = System.currentTimeMillis() / 1000L;
		Path logPath = Paths.get("log_" + unixTime + ".txt");
		try
		{
			this.fileChannel = AsynchronousFileChannel.open(logPath, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public synchronized void saveState()
	{
		String serName = STATEFILE;
		try
		{
			ObjectOutputStream outStr = new ObjectOutputStream(new FileOutputStream(new File(serName)));
			outStr.writeObject(files);
			outStr.writeObject(users);
			outStr.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public synchronized ArrayList<Socket> getOnlineSockets(String excludeUser)
	{
		return onlineUsers.getAllUserSockets(excludeUser);
	}
}
