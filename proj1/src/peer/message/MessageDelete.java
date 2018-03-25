package peer.message;

import java.nio.charset.StandardCharsets;

public class MessageDelete extends Message
{
	public MessageDelete(byte[] fileID)
	{
		super(fileID);
		this.messageType = "DELETE";
		
		String[] headerFields = {
				this.messageType,
				this.version,
				Integer.toString(senderID),
				new String(this.fileID, StandardCharsets.US_ASCII),
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
