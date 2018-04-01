package peer.message;

import java.nio.charset.StandardCharsets;

import peer.Utilities;

public class MessagePutchunk extends Message 
{
	private int chunkNo;
	private int replicationDeg;
	private byte[] body;
	
	public MessagePutchunk(byte[] id, int chunkNo, int replicationDeg)
	{
		super(id);
		this.messageType = "PUTCHUNK";
		this.chunkNo = chunkNo;
		this.replicationDeg = replicationDeg;
		
		String[] headerFields = {
				this.messageType,
				this.version,
				Integer.toString(senderID),
				new String(this.fileID, StandardCharsets.US_ASCII),
				Integer.toString(this.chunkNo),
				Integer.toString(this.replicationDeg),
				"" + CR + LF + CR + LF
			};
		this.header = String.join(" ", headerFields).getBytes();
	}
	
	public void setBody(byte[] body)
	{
		this.body = body;
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
