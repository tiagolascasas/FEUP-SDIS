package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BackupServer extends Thread 
{
	private ServerSocket socket;
	private boolean running = true;
	private ThreadPoolExecutor threads;

	public BackupServer(int backupPort) 
	{
		try 
		{
			this.socket = new ServerSocket(backupPort);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		this.threads = new ThreadPoolExecutor(
	            200,
	            400,
	            10000,
	            TimeUnit.MILLISECONDS,
	            new LinkedBlockingQueue<Runnable>()
				);
	}
	
	@Override
	public void run()
	{
		while (this.running)
		{
			Socket backupSocket = null;
			try
			{
				backupSocket = this.socket.accept();
				ServerManager.getInstance().addBackupServer(backupSocket);
				System.out.println("Accepted a connection from backup server" + backupSocket.getRemoteSocketAddress());
			} 
			catch (IOException e)
			{
				System.out.println("Unable to accept a socket connection");
			}
		}
	}
}
