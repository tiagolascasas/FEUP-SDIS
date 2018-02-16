package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Hashtable;

public class Server 
{
	static int port;
	static DatagramSocket socket;
	static Hashtable plates;
	static boolean running = true;
	
	
	public static void main(String[] args) throws IOException 
	{
		if (args.length != 1)
		{
			System.out.println("Invalid number of arguments");
			return;
		}
		else
			port = Integer.parseInt(args[0]);
		
		plates = new Hashtable<String, String>();
		
		System.out.println("Creating socket on port " + port);
		createSocket();
		listenOnSocket();
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
		System.out.println("-----------------------------");
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
