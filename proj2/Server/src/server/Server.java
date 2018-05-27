package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Server
{
	private static final String SERVERS_FILE = "servers.txt";
	private SSLServerSocket socket;
	private boolean running = true;
	private ThreadPoolExecutor threads;
	private SSLSocket leaderSocket;
	private int port;
	private int id;
	private int backupPort;

	public Server(int port, int id, int backupPort, boolean enableStdoutLogging)
	{
		System.setProperty("javax.net.ssl.keyStore", "server.keys"); //TODO ssl create certificate
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		System.setProperty("javax.net.ssl.trustStore", "truststore");

		this.port = port;
		this.id = id;
		this.backupPort = backupPort;

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

		initServingSocket();

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
		ServerManager.getInstance().getLeader();
		System.out.println("This server is a primary backup");

		LeaderListener listener = new LeaderListener(leaderSocket);
		listener.listen();

		System.exit(0);
	}

	private boolean setLeader()
	{
		System.out.println("Choosing a leader...");

		ArrayList<String> servers = getServerList();
		ArrayList<Integer> ports = new ArrayList<>();
		ArrayList<String> ips = new ArrayList<>();

		for (int i = 0; i < servers.size(); i++)
		{
			String[] elements = servers.get(i).split(":");
			ips.add(elements[0]);
			ports.add(Integer.parseInt(elements[1]));
		}

		Random r = new Random();
		int sleepTime = r.nextInt(1000) + this.id * 1000;
		try
		{
			Thread.sleep(sleepTime);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();

		for (int i = 0; i < ips.size(); i++)
		{
			try
			{
				this.leaderSocket = (SSLSocket) ssf.createSocket(ips.get(i), ports.get(i));
				System.out.println("Server " + ips.get(i) + ":" + ports.get(i) + " is the leader");
				return false;
			}
			catch (ConnectException e)
			{
				this.leaderSocket = null;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return true;
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

	private void initServingSocket()
	{

		try
		{//TODO ssl server socket
			SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			this.socket = (SSLServerSocket) ssf.createServerSocket(port);
			this.socket.setNeedClientAuth(false);
		}
		catch (IOException e)
		{
			System.out.println("Unable to create socket on port " + port);
			System.exit(-1);
		}
		System.out.println("Serving on port " + port);
	}
}
