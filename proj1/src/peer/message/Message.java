package peer.message;

import peer.DataManager;

public abstract class Message 
{
	protected String messageType;
	protected String version;
	protected int senderID;
	protected String fileID;
	
	protected Message(String fileID)
	{
		this.version = DataManager.getInstance().getVersion();
		this.senderID = DataManager.getInstance().getId();
		this.fileID = fileID;
	}
	
	abstract byte[] getMessageBytes();
}
