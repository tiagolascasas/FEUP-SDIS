package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server
{
	private ServerSocket socket;
	private Notifier notifHandler;
	private boolean running = true;
	private ThreadPoolExecutor threads;
	
	public Server(int port, boolean enableStdoutLogging)
	{
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
		boolean enable = true;
		
		if (args.length != 1 && args.length != 2)
			valid = false;
		else
		{
			port = Integer.parseInt(args[0]);
			if (args.length == 2)
				enable = Integer.parseInt(args[1]) != 0;
		}
		if (!valid)
		{
			System.out.println("\nUsage: java -jar Client.jar <server port> [enable stdout logging 0..1]\n");
			System.exit(-1);
		}
		else
		{
			Server server = new Server(port, enable);
			server.run();
		}
	}

	public void run()
	{	
		StateSaver saver = new StateSaver();
		saver.start();
		
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
}
