package client.message;

public class Message {
	private String message;
	public Message(String operation, String[] args) {
		this.message = operation;
		for(int i = 0; i<args.length; i++)
		{
			this.message += "-" + args[i];
		}
	}
	public String getMessage() {
		return message;
	}
}
