package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Client 
{
	private static String mcastAddr;
	private static int mcastPort;
	
	private static InetAddress addr;
	private static String serverAddr;
	private static int serverPort;
	
	private static String operation;
	private static String plateNumber;
	private static String ownerName;
	private static DatagramSocket socket;
	
	public static void main(String[] args) throws IOException 
	{
		if (args.length < 4)
			return;
		else
		{
			mcastAddr = args[0];
			mcastPort = Integer.parseInt(args[1]);
			operation = args[2].toLowerCase();
			if (operation.equals("register"))
			{
				plateNumber = args[3].toUpperCase();
				ownerName = args[4];
			}
			else if (operation.equals("lookup"))
			{
				plateNumber = args[3].toUpperCase();
			}
			else
			{
				System.out.println("Error: unknown option \"" + operation + "\"");
				return;
			}
			
			printInput();
			System.out.println("-----------------------------");
			System.out.println("Trying to find a multicast group...");
			connectToMulticast();
			System.out.println("-----------------------------");
			System.out.println("Connecting to server...");
			connectToServer();
			System.out.println("Connection successfully established");
			System.out.println("-----------------------------");
			System.out.println("Sending request...");
			sendRequest();
			System.out.println("Request successfully sent");
			System.out.println("Waiting for answer...");
			waitForAnswer();
			System.out.println("-----------------------------");
			System.out.println("Disconnecting and exiting...\n\n");
		}
	}

	public static void printInput()
	{
		System.out.println("\n\nMulticast address " + mcastAddr + " with port " + mcastPort);
		if (operation.equalsIgnoreCase("register"))
			System.out.println("Registering plate " + plateNumber + " with owner " + ownerName);
		else
			System.out.println("Looking up plate " + plateNumber);
	}
	
	private static void connectToMulticast() throws IOException 
	{
		MulticastSocket mulSocket = new MulticastSocket(mcastPort);
		mulSocket.joinGroup(InetAddress.getByName(mcastAddr));
		
		byte[] buffer = new byte[256];
		DatagramPacket recPacket = new DatagramPacket(buffer, 256);
		mulSocket.receive(recPacket);
		System.out.println("Successfully joined multicast group, getting server information...");
		String answer = new String(recPacket.getData());
		mulSocket.close();
		
		serverAddr = answer.split("%")[0];
		String n = answer.split("%")[1];
		serverPort = (int)Double.parseDouble(n);
		System.out.println("Server address is " + serverAddr + " with port " + serverPort);
	}
	
	public static void connectToServer() throws UnknownHostException, SocketException
	{
		addr = InetAddress.getByName(serverAddr);
		//addr = InetAddress.getByName("localhost");
		socket = new DatagramSocket(30111, addr);
		socket.setSoTimeout(2000);
	}
	
	public static void sendRequest() throws IOException
	{
		String message;
		if (operation.equals("register"))
			message = "REGISTER " + plateNumber + " " + ownerName;
		else
			message = "LOOKUP " + plateNumber;
		byte[] byteString = message.getBytes();
		
		DatagramPacket packet = new DatagramPacket(
									byteString,
									byteString.length,
									addr,
									serverPort);
		socket.send(packet);
	}
	
	public static void waitForAnswer() throws IOException
	{
		byte[] buffer = new byte[256];
		DatagramPacket recPacket = new DatagramPacket(buffer, 256);
		try {
		socket.receive(recPacket);
		} catch(SocketTimeoutException e) {System.out.println("Timeout"); return;}
		String answer = new String(recPacket.getData());
		
		System.out.println("-----------------------------");/*
		String[] tokens = answer.split(" ");
		String output = "";
		output += operation + " ";
	
		if (Integer.parseInt(tokens[0]) == -1)
			System.out.println("ERROR");
		else
			System.out.println(tokens[0]);*/
		System.out.println(answer);
		
		socket.close();
	}
}
