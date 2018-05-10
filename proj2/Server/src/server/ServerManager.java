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
	private static final String STATEFILE = ".state";
	
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
	
	public boolean registerUser(String username, String passwordHash)
	{
		return users.register(username, passwordHash);
	}
	
	public boolean userIsRegistered(String username)
	{
		return users.verifyUsername(username);
	}
	
	public boolean passwordIsCorrect(String username, String passwordHash)
	{
		return users.verifyPassword(username, passwordHash);
	}
	
	public boolean loginUser(String username, String passwordHash, Socket socket)
	{
		if (onlineUsers.isOnline(username))
			return false;
		System.out.print(onlineUsers.toString());
		
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

	public void log(String s)
	{
		byte[] data = s.getBytes();
		try
		{
			this.fileChannel.write(ByteBuffer.wrap(data), 0).get();
		} 
		catch (InterruptedException | ExecutionException e)
		{
			e.printStackTrace();
		}

		if (enableStdoutLogging)
			System.out.println(s);
	}

	public void setLogging(boolean enableStdoutLogging)
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
	
	public void saveState()
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
}
