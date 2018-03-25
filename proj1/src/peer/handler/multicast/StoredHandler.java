package peer.handler.multicast;

import java.nio.charset.StandardCharsets;

import peer.DataManager;
import peer.StoredManager;
import peer.handler.Handler;

public class StoredHandler extends Handler
{
	private String id;
	private int chunkNo;

	public StoredHandler(byte[] message)
	{
		String[] elements = new String(message, StandardCharsets.US_ASCII).split(" ");
		this.id = elements[3];
		this.chunkNo = Integer.parseInt(elements[4]);
		this.handlerType = "StoredHandler";
	}
	
	@Override
	public void run()
	{
		StoredManager manager = DataManager.getInstance().getStoredManager();
		if (manager.increment(this.id, this.chunkNo))
			//log("increased rep count of file " + this.id + ", chunk no " + this.chunkNo + " to " + manager.getReplicationDegree(this.id, this.chunkNo));
			return;
	}

}
