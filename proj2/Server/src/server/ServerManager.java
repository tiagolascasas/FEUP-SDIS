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
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ServerManager
{
	private static final ServerManager instance = new ServerManager();
	private static final String STATEFILE = "state";
	
	private UserRegistry users;
	private OnlineUsers onlineUsers;
	private FileStorage files;
	private boolean enableStdoutLogging;
	private int id;
	private String leader;
	private boolean leaderStatus;
	private int port;
	private int backupPort;
	private ArrayList<Socket> backupServers;
	private PrintWriter logFile;
	
	private ServerManager()
	{
		try
		{
			InputStream inputStream = new BufferedInputStream(new FileInputStream(STATEFILE));
			ObjectInput objectStream = new ObjectInputStream(inputStream);
			this.files = (FileStorage)objectStream.readObject();
			this.users = (UserRegistry)objectStream.readObject();
			this.onlineUsers = new OnlineUsers();
			this.backupServers = new ArrayList<>();
			objectStream.close();
			System.out.println("Successfully accessed persistent data");
		}
		catch(IOException | ClassNotFoundException e)
		{
			System.out.println("No persistent data found; starting from a blank state");
			this.users = new UserRegistry();
			this.onlineUsers = new OnlineUsers();
			this.files = new FileStorage();
			this.backupServers = new ArrayList<>();
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
		this.logFile.write(s);
		this.logFile.write("\n");

		if (enableStdoutLogging)
			System.out.println(s);
	}

	public synchronized void setLogging(boolean enableStdoutLogging)
	{
		this.enableStdoutLogging = enableStdoutLogging;
		long unixTime = System.currentTimeMillis() / 1000L;
		try
		{
			String fileName = ("log_" + unixTime + "_" + this.id + ".txt");
			this.logFile = new PrintWriter(fileName, "UTF-8");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public synchronized void saveState()
	{
		String serName = STATEFILE + "_" + this.id;
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

	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}

	public String getAddress() 
	{
		String s = "";
		try 
		{
			s = InetAddress.getLocalHost().toString().split("/")[1];
			s += ":" + this.backupPort;
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
		return s;
	}

	public String getLeader() 
	{
		return leader;
	}

	public void setLeader(String leader) 
	{
		this.leader = leader;
	}

	public boolean isLeaderStatus() 
	{
		return leaderStatus;
	}

	public void setLeaderStatus(boolean leaderStatus) 
	{
		this.leaderStatus = leaderStatus;
	}

	public int getPort() 
	{
		return port;
	}

	public void setPort(int port) 
	{
		this.port = port;
	}

	public int getBackupPort() 
	{
		return backupPort;
	}

	public void setBackupPort(int backupPort) 
	{
		this.backupPort = backupPort;
	}

	public void addBackupServer(Socket socket)
	{
		this.backupServers.add(socket);
	}
	
	public void deleteBackupSocket(Socket socket)
	{
		try 
		{
			if (!socket.isClosed())
				socket.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		this.backupServers.remove(socket);
	}
	
	public void notifyBackups(String message) 
	{
		byte[] msg = Utils.byteArrayAppend(message.getBytes(), new byte[]{'\0'});
		for (Socket socket : this.backupServers)
		{
			try 
			{
				socket.getOutputStream().write(msg);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
