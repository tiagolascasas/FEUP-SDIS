package peer.message;

import peer.Manager;

public class MessageOnline extends Message
{
	public MessageOnline()
	{
		super(null);
		String[] headerFields = {
				"ONLINE",
				"1.1",
				Integer.toString(Manager.getInstance().getId()),
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
