package peer;

public class Main 
{
	public static void main(String[] args) 
	{
		if (args.length != 9)
		{
			System.out.println("Invalid number of arguments for P2P Server");
			System.out.println("Usage: <protocol version> <server id> <rmi method name> <MC mcas addr> <MC port> <MDB mcas addr> <MDB port> <MDR mcas addr> <MDR port>");
			return;
		}
		
		McastID[] connections = new McastID[3];
		try 
		{
			connections[0] = new McastID(args[3], args[4]);
			connections[1] = new McastID(args[5], args[6]);
			connections[2] = new McastID(args[7], args[8]);
			
		} catch (IllegalArgumentException e)
		{
			System.exit(-1);
		}
		
		int serverID;
		serverID = (int)Double.parseDouble(args[1]);
		
		Peer peer = new Peer(args[0], serverID, args[2], connections);
		peer.run();
		System.exit(0);
	}
}
