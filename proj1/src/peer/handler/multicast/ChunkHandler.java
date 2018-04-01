package peer.handler.multicast;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import peer.Manager;
import peer.MessageRegister;
import peer.RestoreManager;
import peer.Utilities;
import peer.handler.Handler;
import peer.message.Message;

public class ChunkHandler extends Handler
{
	private byte[] data;
	private int senderId;
	private String id;
	private int chunkNo;
	
	public ChunkHandler(byte[] message)
	{
		this.handlerType = "ChunkHandler";
		String[] elements = new String(message, StandardCharsets.US_ASCII).split(" ");
		this.senderId = Integer.parseInt(elements[2]);
		if (this.senderId == Manager.getInstance().getId())
			return;
		this.id = elements[3];
		this.chunkNo = Integer.parseInt(elements[4]);

		ArrayList<Byte> allData = new ArrayList<Byte>();
		int dataStart = -1;
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
		if (this.senderId == Manager.getInstance().getId())
			return;
		
		MessageRegister chunkReg = Manager.getInstance().getChunkRegister();
		chunkReg.setArrived(id, chunkNo);
		
		RestoreManager manager = Manager.getInstance().getRestoredManager();
		if (!manager.storeChunk(id, chunkNo, data))
			return;
		log("reassembled chunk no. " + chunkNo + " of file " + Utilities.minifyId(id));
	}
}
