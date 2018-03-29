package peer.handler.multicast;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import peer.Channels;
import peer.Chunk;
import peer.ChunkManager;
import peer.Manager;
import peer.handler.Handler;
import peer.message.MessageChunk;

public class RemovedHandler extends Handler
{
	private int chunkNo;
	private String id;
	private int senderId;

	public RemovedHandler(byte[] message)
	{
		this.handlerType = "RemovedHandler";
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
		
		ChunkManager manager = Manager.getInstance().getChunkManager();
		if (manager.decreaseReplicationCount(id, chunkNo))
		{
			Chunk chunk = manager.findChunk(id, chunkNo);
			
			log("decreased rep count of chunk " + chunkNo + " of file " + id + ", starting backup subprotocol for that chunk");
		}
		else
			log("decreased rep count of chunk " + chunkNo + " of file " + id);
	}
}
