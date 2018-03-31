package peer.handler.rmi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import peer.Channels;
import peer.Manager;
import peer.RestoreManager;
import peer.Utilities;
import peer.handler.Handler;
import peer.handler.multicast.ChunkHandler;
import peer.message.Message;
import peer.message.MessageGetchunk;
import peer.message.MessageGetchunkEnhanced;

public class RestoreHandler extends Handler
{
	private String fileId;
	private int numberOfChunks;
	private ThreadPoolExecutor threads;
	private String version;

	public RestoreHandler(String fileId, int numberOfChunks)
	{
		this.handlerType = "RestoreHandler";
		this.fileId = fileId;
		this.numberOfChunks = numberOfChunks;
		this.version = Manager.getInstance().getVersion();
		
		if (this.version.equals("1.1"))
			this.threads = new ThreadPoolExecutor(
		            4,
		            50,
		            10000,
		            TimeUnit.MILLISECONDS,
		            new LinkedBlockingQueue<Runnable>()
		            );
	}
	
	@Override
	public void run()
	{
		log("running restore handler for file with id " + Utilities.minifyId(fileId));
	
		RestoreManager manager = Manager.getInstance().getRestoredManager();
		manager.createNewEntry(fileId, numberOfChunks);
		
		for (int i = 0; i < this.numberOfChunks; i++)
		{
			Message message = null;
			
			if (this.version.equals("1.0"))
				message = new MessageGetchunk(fileId.getBytes(), i);
			else if (this.version.equals("1.1"))
				message = new MessageGetchunkEnhanced(fileId.getBytes(), i);
			else return;
			
			send(Channels.MC, message.getMessageBytes());
		}
		
		if (this.version.equals("1.1"))
		{
			ServerSocket serverSocket = Manager.getInstance().getTCPSocket();
			while(!manager.isComplete(fileId))
			{
				try
				{
					Socket socket = serverSocket.accept();
					ArrayList<Byte> message = new ArrayList<Byte>();
					while(true)
					{
						int read = socket.getInputStream().read();
						if (read == -1)
							break;
						else
							message.add((byte)read);
					}
					byte[] byteMessage = new byte[message.size()];
					for (int i = 0; i < message.size(); i++)
						byteMessage[i] = message.get(i);
					
					this.threads.execute(new ChunkHandler(byteMessage));
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
