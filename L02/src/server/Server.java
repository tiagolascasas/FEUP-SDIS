package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Hashtable;

public class Server 
{
	static int port;
	static int mcastPort;
	
	static String mcastAddr;
	
	static DatagramSocket socket;
	static MulticastSocket mulSocket;
	
	static Advertiser thread;
	static DatagramPacket ad;
	
	static Hashtable <String,String> plates;
	static boolean running = true;
	
	public static void main(String[] args) throws IOException 
	{
		if (args.length != 3)
		{
			System.out.println("Invalid number of arguments");
			return;
		}
		else
		{
			
			port = Integer.parseInt(args[0]);
			mcastAddr = args[1];
			mcastPort = Integer.parseInt(args[2]);
		}
		
		plates = new Hashtable<String, String>();
		
		String message = Inet4Address.getLocalHost().getHostAddress() + "%" + port;
		ad = new DatagramPacket(
			message.getBytes(),
			message.getBytes().length,
			InetAddress.getByName(mcastAddr),
			mcastPort
		);
		
		System.out.println("\n\nCreating socket on port " + port);
		createSocket();
		System.out.println("-----------------------------");
		System.out.println("Creating multicast socket on port " + mcastPort);
		createMulticastSocket();
		System.out.println("-----------------------------");
		listenOnSocket();
	}


	private static void createMulticastSocket() throws IOException 
	{
		try {
			mulSocket = new MulticastSocket(mcastPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		thread = new Advertiser(mulSocket, ad);
		thread.start();
		System.out.println("Serving multicast ads on port " + mcastPort);
		
	}


	private static void createSocket() 
	{
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		System.out.println("Serving on port " + port);
	}


	private static void listenOnSocket() throws IOException 
	{
		while(running)
		{
			byte[] buffer = new byte[512];
			DatagramPacket recPacket = new DatagramPacket(buffer, 512);
			socket.receive(recPacket);
			String request = new String(recPacket.getData());
			
			String answer = processRequest(request);
			
			byte[] byteString = answer.getBytes();
			DatagramPacket packet = new DatagramPacket(
										byteString,
										byteString.length,
										recPacket.getAddress(),
										recPacket.getPort());
			socket.send(packet);
		}	
	}

	private static String processRequest(String request) 
	{
		System.out.println("Request: " + request);
		request = request.trim();
		String[] tokens = request.split(" ");
		String answer = "";
		
		if (tokens[0].equals("REGISTER"))
		{
			answer = processRegister(tokens);
		}
		else if (tokens[0].equals("LOOKUP"))
		{
			answer = processLookup(tokens);
		}
		else
			answer = "Unable to parse request";
		
		return answer;
	}


	private static String processLookup(String[] tokens) 
	{
		String plateName = tokens[1];
		String answer = "-1 NOT_FOUND";
		if (plates.get(plateName) != null)
		{
			answer = plates.size() + "\n";
			answer += plateName + " " + plates.get(plateName);
			
		}
		return answer;
	}


	private static String processRegister(String[] tokens) 
	{
		String plateName = tokens[1];
		String ownerName = tokens[2].replace("-", " ");
		
		String answer = "-1";
		if (plates.get(plateName) == null)
		{
			plates.put(plateName, ownerName);
			answer = plates.size() + "\n";
			answer += plateName + " " + ownerName;
			
		}
		return answer;
	}

}
