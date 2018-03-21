package peer;

import java.net.MulticastSocket;
import java.util.concurrent.CountDownLatch;

public class Peer extends Thread
{
	private String rmiMethodName;
	private McastID[] connections;
	private MulticastSocket mcSocket, mdbSocket, mbrSocket;
	static boolean running = true;
	
	Thread mc;
	Thread mdb;
	Thread mbr;
	Thread client;
	
	public Peer(String version, int serverID, String rmi, McastID[] connections) 
	{
		DataManager.getInstance().init(version, serverID, connections);
		this.rmiMethodName = rmi;
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
			
			DataManager.getInstance().setSockets(mcSocket, mbrSocket, mdbSocket);
		}
		catch (Exception e)
		{
			System.out.println("Error creating and joining multicast socket(s): " + e.getMessage());
			System.exit(-1);
		}
		
		mc = new DispatcherMC(this.mcSocket);
		mdb = new DispatcherMDB(this.mdbSocket);
		mbr = new DispatcherMBR(this.mbrSocket);
		client = new DispatcherRMI(this.rmiMethodName);
	}
	
	@Override
	public void run()
	{
		mc.start();
		mdb.start();
		mbr.start();
		client.start();
		
		CountDownLatch latch = new CountDownLatch(4);
		try
		{
			latch.await();
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		return;
	}
}
