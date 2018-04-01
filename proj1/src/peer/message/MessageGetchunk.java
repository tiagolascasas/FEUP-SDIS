package peer.message;

import java.nio.charset.StandardCharsets;

public class MessageGetchunk extends Message
{
	private int chunkNo;
	
	public MessageGetchunk(byte[] fileID, int chunkNo)
	{
		super(fileID);
		this.messageType = "GETCHUNK";
		this.chunkNo = chunkNo;
		
		String[] headerFields = {
				this.messageType,
				this.version,
				Integer.toString(senderID),
				new String(this.fileID, StandardCharsets.US_ASCII),
				Integer.toString(this.chunkNo),
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
