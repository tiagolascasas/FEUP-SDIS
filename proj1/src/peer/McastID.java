package peer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class McastID 
{
	public InetAddress addr;
	public int port;
	
	McastID(String addr, String port)
	{
		try 
		{
			this.addr = InetAddress.getByName(addr);
			this.port = Integer.parseInt(port);
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
			System.out.println("Unable to resolve IP address " + addr + " with port " + port);
			throw new IllegalArgumentException();
		}
	}
}
