package peer.handler.multicast;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import peer.BackupManager;
import peer.Channels;
import peer.Chunk;
import peer.ChunkManager;
import peer.Manager;
import peer.MessageRegister;
import peer.RestoreManager;
import peer.Utilities;
import peer.handler.Handler;
import peer.message.MessageChunk;
import peer.message.MessagePutchunk;

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
		if (this.senderId == Manager.getInstance().getId())
			return;
		this.id = elements[3];
		this.chunkNo = Integer.parseInt(elements[4]);
	}

	@Override
	public void run()
	{
		if (this.senderId == Manager.getInstance().getId())
			return;
		
		ChunkManager manager = Manager.getInstance().getChunkManager();
		BackupManager bkManager = Manager.getInstance().getBackupsManager();
		
		MessageRegister putchunkReg = Manager.getInstance().getPutchunkRegister();
		
		if (bkManager.hasInitiated(id))
			return;
		
		if (manager.decreaseReplicationCount(id, chunkNo))
		{
			putchunkReg.registerNewChunk(id, chunkNo);
			
			Chunk chunk = manager.findChunk(id, chunkNo);
			MessagePutchunk message = new MessagePutchunk(id.getBytes(), chunkNo, chunk.getDesiredRepDegree());
			message.setBody(chunk.retrieveChunkData());
			
			Random r = new Random();
			int timeToWait = r.nextInt(400);
			try
			{
				Thread.sleep(timeToWait);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			if (putchunkReg.isArrived(id, chunkNo))
				return;
			putchunkReg.unregisterChunk(id, chunkNo);
			
			send(Channels.MDB, message.getMessageBytes());
			log("decreased rep count of chunk " + chunkNo + " of file " + Utilities.minifyId(id) + ", started backup subprotocol for that chunk");
		}
		else
			log("decreased rep count of chunk " + chunkNo + " of file " + Utilities.minifyId(id));
	}
}
