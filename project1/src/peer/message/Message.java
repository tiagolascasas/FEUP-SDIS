package peer.message;

import peer.Manager;

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
		this.version = Manager.getInstance().getVersion();
		this.senderID = Manager.getInstance().getId();
		this.fileID = id;
	}
	
	abstract byte[] getMessageBytes();
}
