package peer.handler.multicast;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import peer.Channels;
import peer.ChunkManager;
import peer.Manager;
import peer.handler.Handler;
import peer.message.MessageChunk;

public class GetchunkHandler extends Handler
{
	private static final int MAX_WAIT = 400;
	private int chunkNo;
	private String id;
	private int senderId;

	public GetchunkHandler(byte[] message)
	{
		String[] elements = new String(message, StandardCharsets.US_ASCII).split(" ");
		this.senderId = Integer.parseInt(elements[2]);
		this.id = elements[3];
		this.chunkNo = Integer.parseInt(elements[4]);
		this.handlerType = "GetchunkHandler";
	}

	@Override
	public void run()
	{
		if (this.senderId == Manager.getInstance().getId())
			return;
		
		ChunkManager manager = Manager.getInstance().getChunkManager();
		byte[] data = manager.retrieveChunk(id, chunkNo);
		if (data == null)
			return;
		MessageChunk message = new MessageChunk(id.getBytes(), chunkNo, data);
		
		Random r = new Random();
		int time = r.nextInt(MAX_WAIT);
		try
		{
			Thread.sleep(time);
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		log("returned chunk no. " + chunkNo + " of file " + id);
		send(Channels.MDR, message.getMessageBytes());
	}
}
