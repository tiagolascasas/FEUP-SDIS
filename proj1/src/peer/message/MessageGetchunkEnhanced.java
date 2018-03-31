package peer.message;

import java.nio.charset.StandardCharsets;

import peer.Channels;
import peer.Manager;

public class MessageGetchunkEnhanced extends Message
{
	private int chunkNo;
	
	public MessageGetchunkEnhanced(byte[] fileID, int chunkNo)
	{
		super(fileID);
		this.messageType = "GETCHUNK";
		this.chunkNo = chunkNo;
		this.version = "1.1";
		
		String[] headerFields = {
				this.messageType,
				this.version,
				Integer.toString(senderID),
				new String(this.fileID, StandardCharsets.US_ASCII),
				Integer.toString(this.chunkNo),
				"" + CR + LF,
				Integer.toString(Manager.getInstance().getPort(Channels.TCP)),
				Manager.getInstance().getLocalIP(),
				"" + CR + LF + CR + LF
			};
		this.header = String.join(" ", headerFields).getBytes();
	}

	@Override
	public byte[] getMessageBytes()
	{
		return this.header;
	}
}