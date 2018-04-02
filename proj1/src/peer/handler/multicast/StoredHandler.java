package peer.handler.multicast;

import java.nio.charset.StandardCharsets;

import peer.Manager;
import peer.BackupManager;
import peer.ChunkManager;
import peer.handler.Handler;

public class StoredHandler extends Handler
{
	private String id;
	private int chunkNo;
	private int senderId;

	public StoredHandler(byte[] message)
	{
		this.handlerType = "StoredHandler";
		String[] elements = new String(message, StandardCharsets.US_ASCII).split(" ");
		this.senderId = Integer.parseInt(elements[2]);
		this.id = elements[3];
		this.chunkNo = Integer.parseInt(elements[4]);
	}
	
	@Override
	public void run()
	{
		if (this.senderId == Manager.getInstance().getId())
			return;
		
		//if this was the initiator peer, register the stored message
		BackupManager bkManager = Manager.getInstance().getBackupsManager();
		if (bkManager.increment(this.id, this.chunkNo))
			return;
		
		//if this was not the initiator peer, increases the rep. count of the stored
		//chunk, if it has one
		ChunkManager ckManager = Manager.getInstance().getChunkManager();
		ckManager.increaseReplicationCount(id, chunkNo);
	}
}
