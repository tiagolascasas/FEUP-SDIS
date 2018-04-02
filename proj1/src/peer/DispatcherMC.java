package peer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import peer.handler.multicast.DeleteHandler;
import peer.handler.multicast.GetchunkHandler;
import peer.handler.multicast.OnlineHandler;
import peer.handler.multicast.RemovedHandler;
import peer.handler.multicast.StoredHandler;

public class DispatcherMC extends Dispatcher
{
	public DispatcherMC()
	{
		this.socket = Manager.getInstance().getSocket(Channels.MC);
		this.threads = new ThreadPoolExecutor(
		            200,
		            400,
		            10000,
		            TimeUnit.MILLISECONDS,
		            new LinkedBlockingQueue<Runnable>()
		            );
	}
	
	@Override
	void processMessage(byte[] message)
	{
		String type = new String(message, StandardCharsets.US_ASCII).split(" ", 2)[0];
		
		switch(type)
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
			case "ONLINE":
				threads.execute(new OnlineHandler(message));
				break;
			default:
				break;
		}
	}
}
