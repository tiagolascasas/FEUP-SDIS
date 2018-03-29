package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client 
{
	private static String hostname;
	private static int port;
	private static String operation;
	private static String plateNumber;
	private static String ownerName;
	private static DatagramSocket socket;
	private static InetAddress addr;
	
	public static void main(String[] args) throws IOException 
	{
		if (args.length < 4)
			return;
		else
		{
			hostname = args[0];
			port = Integer.parseInt(args[1]);
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
			System.out.println("Connecting to server...");
			connectToServer();
			System.out.println("Connection successfully established");
			System.out.println("Sending request...");
			sendRequest();
			System.out.println("Request successfully sent");
			System.out.println("Waiting for answer...");
			waitForAnswer();
			System.out.println("Disconnecting and exiting...");
		}
	}
	
	public static void printInput()
	{
		System.out.println("Host " + hostname + " at port " + port);
		if (operation.equalsIgnoreCase("register"))
			System.out.println("Registering plate " + plateNumber + " with owner " + ownerName);
		else
			System.out.println("Looking up plate " + plateNumber);
	}
	
	public static void connectToServer() throws UnknownHostException, SocketException
	{
		addr = InetAddress.getByName(hostname);
		socket = new DatagramSocket(30403, addr);
		socket.getSoTimeout();
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
									port);
		socket.send(packet);
	}
	
	public static void waitForAnswer() throws IOException
	{
		byte[] buffer = new byte[256];
		DatagramPacket recPacket = new DatagramPacket(buffer, 256);
		socket.receive(recPacket);
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
			
		System.out.println("-----------------------------");
		
		socket.close();
	}
}
