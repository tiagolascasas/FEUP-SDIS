package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import client.message.Message;

public class Client {

	private String peerIp; 
	private int peerPort;
	private Message message;
	public static void main(String[] args) {
		Message message;
		try {
			switch(args[1].toUpperCase()) {
			case "BACKUP":
				if(args.length == 4)
				{
					message = new Message(args[1].toUpperCase(), new String[] {args[2], args[3]});
					break;
				}
			case "RESTORE":
			case "DELETE":
			case "RECLAIM":
				if(args.length == 3)
				{
					message = new Message(args[1].toUpperCase(), new String[] {args[2]});
					break;
				}
			case "STATE":
				if(args.length == 2)
				{
					message = new Message(args[1].toUpperCase(), new String[] {});
					break;
				}
			default:
				throw new Exception();
			}
		} catch (Exception e)
		{
			Client.printUsage();
			return;
		}
		Client client = new Client(args[0], message);
		client.sendMessageUDP();
	}

	public static void printUsage() {
		System.out.println("\nUsage: Client <peer_ap> <sub_protocol> [args...]\n");
		System.out.println("<peer-ap>\tpeer access point ip:port\n");
		System.out.println("List of available sub-protocols:\n");
		System.out.println("BACKUP\t\t<file path> <replication degree>");
		System.out.println("RESTORE\t\t<file path>");
		System.out.println("DELETE\t\t<file path>");
		System.out.println("RECLAIM\t\t<maximum amount of disk space, in KBytes>");
		System.out.println("STATE");
		System.out.print("\n");
	}


	public Client(String accessPoint, Message message) {
		String [] peer = accessPoint.split(":");
		//TODO adapt for rmi
		if(peer.length == 2)
		{
			this.peerIp = peer[0];
			this.peerPort = Integer.parseInt(peer[1]);
		}
		else
		{
			this.peerIp = "localhost";
			this.peerPort = Integer.parseInt(peer[0]);
		}
	

		this.message = message;
	}

	private void sendMessageUDP() {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			System.out.println("couldn't use given port...");
			return;
		}
		byte[] buffer = this.message.getMessage().getBytes();
		int responsePort = socket.getLocalPort();
		InetAddress address = null;
		try {
			address = InetAddress.getByName(this.peerIp);
		} catch (UnknownHostException e) {
			System.out.println("invalid hostname...");
			socket.close();
			return;
		}
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, this.peerPort);

		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
			socket.close();
			return;
		}
		socket.close();
		this.awaitResponseUDP(address, responsePort);
	}

	private void awaitResponseUDP(InetAddress address, int responsePort) {
		DatagramSocket socket = null;
		byte[] buffer = new byte[200];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		try {
			socket = new DatagramSocket(responsePort, address);
			System.out.println("Waiting for response...");
			socket.receive(packet);

		} catch (SocketException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			socket.close();
			return;
		} 
		socket.close();

		buffer = packet.getData();
		String data = (new String(buffer)).trim();
		System.out.println("Response recieved: " + data);

		return;
	}

	/*private void sendMessageRMI() {

}*/



}

