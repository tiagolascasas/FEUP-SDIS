package peer;

public class Peer 
{
	private String version;
	private int serverID;
	private String rmi;
	private McastID[] connections;
	
	public Peer(String version, int serverID, String rmi, McastID[] connections) 
	{
		this.version = version;
		this.serverID = serverID;
		this.rmi = rmi;
		this.connections = connections;
	}
	
	public void run()
	{
		while(true)
		{
			System.out.println("running");
		}
	}
}
