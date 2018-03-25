package peer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import peer.handler.multicast.DeleteHandler;
import peer.handler.multicast.GetchunkHandler;
import peer.handler.multicast.RemovedHandler;
import peer.handler.multicast.StoredHandler;

public class DispatcherMC extends Dispatcher
{
	public DispatcherMC()
	{
		this.socket = DataManager.getInstance().getSocket(CHANNELS.MC);
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
			case "STORED":
				threads.execute(new StoredHandler(message));
				break;
			case "GETCHUNK":
				threads.execute(new GetchunkHandler(message));
				break;
			case "DELETE":
				threads.execute(new DeleteHandler(message));
				break;
			case "REMOVED":
				threads.execute(new RemovedHandler(message));
				break;
			default:
				break;
		}
	}
}
