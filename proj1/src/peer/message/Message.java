package peer.message;

public abstract class Message 
{
	protected String messageType;
	protected String version;
	protected int senderID;
	protected String fileID;
	
	protected Message(String version, int senderID, String fileID)
	{
		this.version = version;
		this.senderID = senderID;
		this.fileID = fileID;
	}
	
	abstract byte[] getMessageBytes();
}
