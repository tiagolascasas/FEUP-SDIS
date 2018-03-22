package peer.handler.multicast;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import peer.CHANNELS;
import peer.DataManager;
import peer.handler.Handler;
import peer.message.MessageStored;

public class PutchunkHandler extends Handler
{
	private static final int MAX_WAIT = 400;
	private byte[] data;
	private String id;
	private int repDeg;
	private int chunkNo;
	
	public PutchunkHandler(byte[] message)
	{
		String[] elements = new String(message, StandardCharsets.US_ASCII).split(" ");
		this.id = elements[3];
		this.chunkNo = Integer.parseInt(elements[4]);
		this.repDeg = Integer.parseInt(elements[5]);
		this.data = elements[6].getBytes();
	}
	
	@Override
	public void run()
	{
		if (!DataManager.getInstance().canStore(id))
			return;
		
		DataManager.getInstance().store(id, chunkNo, repDeg, data);
		
		MessageStored reply = new MessageStored(id.getBytes(), chunkNo);
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
		
		send(CHANNELS.MC, reply.getMessageBytes());
	}
}
