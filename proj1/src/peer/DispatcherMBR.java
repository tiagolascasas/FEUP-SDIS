package peer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import peer.handler.multicast.ChunkHandler;
import peer.handler.multicast.PutchunkHandler;

public class DispatcherMBR extends Dispatcher
{
	public DispatcherMBR() 
	{
		this.socket = DataManager.getInstance().getSocket(CHANNELS.MBR);
		this.threads = new ThreadPoolExecutor(
	            4,
	            400,
	            10000,
	            TimeUnit.MILLISECONDS,
	            new LinkedBlockingQueue<Runnable>()
	            );
	}

	@Override
	void processMessage(byte[] message)
	{
		String type = new String(message, StandardCharsets.US_ASCII).split(" ")[0];
		
		switch(type.toUpperCase())
		{
			case "CHUNK":
				threads.execute(new ChunkHandler(message));
				break;
			default:
				break;
		}
	}

}
