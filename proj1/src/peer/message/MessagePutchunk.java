package peer.message;

public class MessagePutchunk extends Message 
{
	private int chunkNo;
	private int replicationDeg;
	private byte[] body;
	
	
	MessagePutchunk(String fileID, int chunkNo, int replicationDeg, byte[] body)
	{
		super(fileID);
		this.messageType = "PUTCHUNK";
		this.chunkNo = chunkNo;
		this.replicationDeg = replicationDeg;
		this.body = body;
	}
	
	@Override
	byte[] getMessageBytes() 
	{
		String[] headerFields = {
			this.messageType,
			this.version,
			Integer.toString(senderID),
			this.fileID,
			Integer.toString(this.chunkNo),
			Integer.toString(this.replicationDeg),
			"\n\n"
		};
		byte[] header = String.join(" ", headerFields).getBytes();
		byte[] message = new byte[header.length + body.length];
		System.arraycopy(header, 0, message, 0, header.length);
		System.arraycopy(body, 0, message, header.length, body.length);
		return message;
	}

}
