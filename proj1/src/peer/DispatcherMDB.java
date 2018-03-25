package peer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import peer.handler.multicast.PutchunkHandler;

public class DispatcherMDB extends Dispatcher
{
	public DispatcherMDB() 
	{
		this.socket = DataManager.getInstance().getSocket(CHANNELS.MDB);
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
			case "PUTCHUNK":
				threads.execute(new PutchunkHandler(message));
				break;
			default:
				break;
		}
	}
}
