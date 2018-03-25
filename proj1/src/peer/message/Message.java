package peer.message;

import peer.DataManager;

public abstract class Message 
{	
	public final static char CR  = (char)0x0D;
	public final static char LF  = (char)0x0A; 

	protected byte[] header;
	protected String messageType;
	protected String version;
	protected int senderID;
	protected byte[] fileID;
	
	protected Message(byte[] id)
	{
		this.version = DataManager.getInstance().getVersion();
		this.senderID = DataManager.getInstance().getId();
		this.fileID = id;
	}
	
	abstract byte[] getMessageBytes();
}
