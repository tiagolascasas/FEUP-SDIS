package server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
	private ConcurrentHashMap<String, Integer> peerPorts;
	
	private ServerManager(){}
	
	public static ServerManager getInstance()
	{
		return instance;
	}
	
	public synchronized void loadState()
	{
		try
		{
			InputStream inputStream = new BufferedInputStream(new FileInputStream(STATEFILE + "_" + id));
			ObjectInput objectStream = new ObjectInputStream(inputStream);
			this.files = (FileStorage)objectStream.readObject();
			this.users = (UserRegistry)objectStream.readObject();
			this.onlineUsers = new OnlineUsers();
			this.backupServers = new ArrayList<>();
			this.peerPorts = new ConcurrentHashMap<String, Integer>();
			objectStream.close();
			System.out.println("Successfully accessed persistent data on file " + STATEFILE + "_" + id);
		}
		catch(IOException | ClassNotFoundException e)
		{
			System.out.println("No persistent data found; starting from a blank state");
			this.users = new UserRegistry();
			this.onlineUsers = new OnlineUsers();
			this.files = new FileStorage();
			this.backupServers = new ArrayList<>();
			this.peerPorts = new ConcurrentHashMap<String, Integer>();
		}
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
	
	public synchronized boolean loginUser(String username, String passwordHash, Socket socket, int peerPort)
	{
		if (onlineUsers.isOnline(username, socket))
			return false;
		
		onlineUsers.setUserOnline(username, socket);
		peerPorts.put(username, peerPort);
		return true;
	}
	
	public synchronized int getPeerPort(String username)
	{
		return peerPorts.get(username);
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
	
	public synchronized String getOwnerOfTrack(String track)
	{
		return this.users.getOwnerOfTrack(track);
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
		if (enableStdoutLogging)
			System.out.println(s);
	}

	public synchronized void setLogging(boolean enableStdoutLogging)
	{
		this.enableStdoutLogging = enableStdoutLogging;
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
	
	public synchronized Socket getSocketOfOnlineUser(String username)
	{
		return this.onlineUsers.getUserSocket(username);
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

	public synchronized void addBackupServer(Socket socket)
	{
		this.backupServers.add(socket);
		/*UPDATE BACKUP SERVER STATE
		 * unfortunately it is not working in time for delivery
		 */
	}
	
	private synchronized byte[] getCurrentState() 
	{
		File file = new File(STATEFILE + "_" + id);
		FileInputStream fis;
		try 
		{
			fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();
			return Utils.encode(new String(data)).getBytes();
		} 
		catch (IOException e ) 
		{
			e.printStackTrace();
		}
		return null;
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
	
	public synchronized void notifyBackups(String message) 
	{
		byte[] msg = Utils.byteArrayAppend(message.getBytes(), new byte[]{'\0'});
		
		ExecutorService es = Executors.newCachedThreadPool();
		
		for (Socket socket : this.backupServers)
		{
			if (socket.isClosed())
			{
				System.out.println("Backup server " + socket.getRemoteSocketAddress() + " is offline");
				this.backupServers.remove(socket);
			}
		}

		for (Socket socket : this.backupServers)
		{
			es.execute(new Runnable() 
			{
				public void run() 
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
			});
		}
		
		es.shutdown();
		try 
		{
			boolean finished;
			do
			{
				finished = es.awaitTermination(1, TimeUnit.MINUTES);
			} while (!finished);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}

	public synchronized void setState(byte[] state) 
	{
		try 
		{
			System.out.println(state.length);
			ByteArrayInputStream stream = new ByteArrayInputStream(state);
			ObjectInput objectStream = new ObjectInputStream(stream);
			this.files = (FileStorage)objectStream.readObject();
			this.users = (UserRegistry)objectStream.readObject();
			System.out.println("State successfully updated");
		} 
		catch (IOException | ClassNotFoundException e) 
		{
			//e.printStackTrace();
			System.out.println("Error processing state");
		}
	}
}
