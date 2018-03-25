package peer.handler.multicast;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

import peer.CHANNELS;
import peer.DataManager;
import peer.handler.Handler;
import peer.message.Message;
import peer.message.MessageStored;

public class PutchunkHandler extends Handler
{
	private static final int MAX_WAIT = 400;
	private byte[] data;
	private int senderId;
	private String id;
	private int repDeg;
	private int chunkNo;
	
	public PutchunkHandler(byte[] message)
	{
		this.handlerType = "PutchunkHandler";
		String[] elements = new String(message, StandardCharsets.US_ASCII).split(" ");
		this.senderId = Integer.parseInt(elements[2]);
		this.id = elements[3];
		this.chunkNo = Integer.parseInt(elements[4]);
		this.repDeg = Integer.parseInt(elements[5]);

		ArrayList<Byte> allData = new ArrayList<Byte>();
		int dataStart = 0;
		for (int i = 0; i < message.length; i++)
		{
			if (message[i] == Message.CR &&
				message[i+1] == Message.LF &&
				message[i+2] == Message.CR &&
				message[i+3] == Message.LF)
				dataStart = i + 4;
		}
		for (int i = dataStart; i < message.length; i++)
			allData.add(message[i]);
		this.data = new byte[allData.size()];
		for (int i = 0; i < allData.size(); i++)
			this.data[i] = allData.get(i);
	}
	
	@Override
	public void run()
	{	
		if (this.senderId == DataManager.getInstance().getId())
			return;
		
		if (!DataManager.getInstance().store(id, chunkNo, repDeg, data))
			return;
		log("stored chunk no. " + chunkNo + " of file " + id);
		
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
