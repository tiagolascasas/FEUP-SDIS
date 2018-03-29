package peer.message;

import java.nio.charset.StandardCharsets;

public class MessageChunk extends Message
{

	private int chunkNo;
	private byte[] body;
	
	public MessageChunk(byte[] fileID, int chunkNo, byte[] data)
	{
		super(fileID);
		this.messageType = "CHUNK";
		this.chunkNo = chunkNo;
		this.body = data;
		
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
		byte[] message = new byte[header.length + body.length];
		System.arraycopy(header, 0, message, 0, header.length);
		System.arraycopy(body, 0, message, header.length, body.length);
		return message;
	}
}
