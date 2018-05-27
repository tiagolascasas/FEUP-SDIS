package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class BackupServer extends Thread
{
	private SSLServerSocket socket;
	private boolean running = true;
	public BackupServer(int backupPort)
	{

		System.setProperty("javax.net.ssl.trustStore", "truststore");
		System.setProperty("javax.net.ssl.keyStore", "server.keys"); //TODO ssl create certificate
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");

		try
		{
			SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			this.socket = (SSLServerSocket) ssf.createServerSocket(backupPort);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		new ThreadPoolExecutor(
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
				System.out.println("Accepted a connection from backup server " + backupSocket.getRemoteSocketAddress());
			}
			catch (IOException e)
			{
				System.out.println("Unable to accept a socket connection from a backup server");
			}
		}
	}
}
