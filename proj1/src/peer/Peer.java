package peer;

import java.net.MulticastSocket;

public class Peer implements Runnable
{
	private String version;
	private int serverID;
	private String rmi;
	private McastID[] connections;
	private MulticastSocket mcSocket, mdbSocket, mbrSocket;
	DispatcherMC mc;
	DispatcherMDB mdb;
	DispatcherMBR mbr;
	
	public Peer(String version, int serverID, String rmi, McastID[] connections) 
	{
		this.version = version;
		this.serverID = serverID;
		this.rmi = rmi;
		this.connections = connections;
		
		try
		{
			System.out.println("Creating Multicast socket in port " + this.connections[0].port + " and group " + this.connections[0].addr);
			this.mcSocket = new MulticastSocket(this.connections[0].port);
			this.mcSocket.joinGroup(connections[0].addr);
			
			System.out.println("Creating Multicast socket in port " + this.connections[1].port + " and group " + this.connections[1].addr);
			this.mdbSocket = new MulticastSocket(this.connections[1].port);
			this.mdbSocket.joinGroup(connections[1].addr);
			
			System.out.println("Creating Multicast socket in port " + this.connections[2].port + " and group " + this.connections[2].addr);
			this.mbrSocket = new MulticastSocket(this.connections[2].port);
			this.mbrSocket.joinGroup(connections[2].addr);
		}
		catch (Exception e)
		{
			System.out.println("Error creating and joining multicast socket(s): " + e.getMessage());
			System.exit(-1);
		}
		
		mc = new DispatcherMC(this.mcSocket, version, serverID);
		mdb = new DispatcherMDB(this.mdbSocket, version, serverID);
		mbr = new DispatcherMBR(this.mbrSocket, version, serverID);
		
	}
	
	@Override
	public void run()
	{
		mc.run();
		mdb.run();
		mbr.run();
	}
}
