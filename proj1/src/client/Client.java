package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.message.Message;

public class Client {

	public static void main(String[] args) {
		String response;
		try {
			Registry registry = LocateRegistry.getRegistry(host, 5555);
			Message stub = (Message) registry.lookup("Message");


			switch(args[1].toUpperCase()) {
			case "BACKUP":
				if(args.length == 4)
				{
					response = stub.sendMessage(args[1].toUpperCase(), new String[] {args[2], args[3]});
					break;
				}
			case "RESTORE":
			case "DELETE":
			case "RECLAIM":
				if(args.length == 3)
				{
					response = stub.sendMessage(args[1].toUpperCase(), new String[] {args[2]});
					break;
				}
			case "STATE":
				if(args.length == 2)
				{
					response = stub.sendMessage(args[1].toUpperCase(), new String[] {});
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

		System.out.println("response: " + response);
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

	public Client() {}


}

