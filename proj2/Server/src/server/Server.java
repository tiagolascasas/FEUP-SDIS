package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
	private ServerSocket socket;
	private Notifier notifHandler;
	private boolean running = true;
	
	public Server(int port)
	{
		ServerManager.getInstance();
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
		
		if (args.length != 1)
			valid = false;
		else
		{
			port = Integer.parseInt(args[0]);
		}
		if (!valid)
		{
			System.out.println("\nUsage: java -jar Client.jar <server port>\n");
			System.exit(-1);
		}
		else
		{
			Server server = new Server(port);
			server.run();
		}
	}

	public void run()
	{
		this.notifHandler = new Notifier();
		this.notifHandler.run();
		
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
			client.run();
		}
		System.exit(0);
	}
}
