package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server
{
	private static final String SERVERS_FILE = "servers.txt";
	private static final String LEADER_FILE = "leader";
	private ServerSocket socket;
	private boolean running = true;
	private ThreadPoolExecutor threads;
	private Socket leaderSocket;
	
	public Server(int port, int id, int backupPort, boolean enableStdoutLogging)
	{
		ServerManager.getInstance().setId(id);
		ServerManager.getInstance().setPort(port);
		ServerManager.getInstance().setBackupPort(backupPort);
		ServerManager.getInstance().setLogging(enableStdoutLogging);
		
		this.threads = new ThreadPoolExecutor(
	            200,
	            400,
	            10000,
	            TimeUnit.MILLISECONDS,
	            new LinkedBlockingQueue<Runnable>()
				);
		
		try
		{
			this.socket = new ServerSocket(port);
		} 
		catch (IOException e)
		{
			System.out.println("Unable to create socket on port " + port);
			System.exit(-1);
		}
		System.out.println("Serving on port " + port);
	}

	public static void main(String[] args)
	{
		boolean valid = true;
		Integer port = null;
		Integer id = null;
		Integer backupPort = null;
		boolean enable = true;
		
		if (args.length != 3 && args.length != 4)
			valid = false;
		else
		{
			port = Integer.parseInt(args[0]);
			id = Integer.parseInt(args[1]);
			backupPort = Integer.parseInt(args[2]);
			if (args.length == 3 && args[1].equals("-d"))
				enable = false;
		}
		if (!valid)
		{
			System.out.println("\nUsage: java -jar Client.jar <server port> <server id> <backup port> [-d (disable stdout logging)]\n");
			System.exit(-1);
		}
		else
		{
			Server server = new Server(port, id, backupPort, enable);
			server.run();
		}
	}

	public void run()
	{	
		boolean isLeader = setLeader();
		ServerManager.getInstance().setLeaderStatus(isLeader);
		
		StateSaver saver = new StateSaver();
		saver.start();
		
		if (isLeader)
			runServer();
		else
			runBackup();
	}

	private void runServer() 
	{
		System.out.println("This server is the current leader");
		
		BackupServer backup = new BackupServer(ServerManager.getInstance().getBackupPort());
		backup.start();
		
		while (this.running)
		{
			Socket clSocket = null;
			try
			{
				clSocket = this.socket.accept();
				System.out.println("Accepted a connection from client " + clSocket.getRemoteSocketAddress());
			} 
			catch (IOException e)
			{
				System.out.println("Unable to accept a socket connection");
			}
			ClientListener client = new ClientListener(clSocket);
			this.threads.execute(client);
		}
		System.exit(0);
	}

	private void runBackup() 
	{
		String leader = ServerManager.getInstance().getLeader();
		System.out.println("This server is a primary backup");
		System.out.println("Leader is server " + leader);
		
		String leaderIP = leader.split(":")[0];
		int leaderPort = Integer.parseInt(leader.split(":")[1]);
		
		try 
		{
			this.leaderSocket = new Socket(leaderIP, leaderPort);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		LeaderListener listener = new LeaderListener(leaderSocket);
		listener.listen();
		
		System.exit(0);
	}

	private boolean setLeader() 
	{
		boolean isLeader = false;
	    File file = new File(LEADER_FILE);
	    RandomAccessFile in = null;
	    try 
	    {
	        in = new RandomAccessFile(file, "rw");
	        FileLock lock = null;
	        do
	        {
	        	lock = in.getChannel().tryLock();
	        	Thread.sleep(500);
	        }
	        while (lock == null);
	        
            String leader = in.readLine();
            if (leader == null)
            {
            	String thisServer = ServerManager.getInstance().getAddress();
            	in.write(thisServer.getBytes());
            	ServerManager.getInstance().setLeader(thisServer);
            	isLeader = true;
            }
            else
            {
            	ServerManager.getInstance().setLeader(leader);
            	isLeader = false;
            }
            lock.release();
            in.close();
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
		return isLeader;
	}
	
	private ArrayList<String> getServerList()
	{
		ArrayList<String> servers = new ArrayList<>();
		
		FileInputStream fstream;
		try 
		{
			fstream = new FileInputStream(SERVERS_FILE);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			while ((strLine = br.readLine()) != null)   
			{
				servers.add(strLine);
			}
			br.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.exit(-1);
		}
		return servers;
	}
}
