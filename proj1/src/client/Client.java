package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Base64;

import peer.message.MessageRMI;
import peer.Utilities;

public class Client {

	public static void main(String[] args) {

		String [] messageArgs;
		byte[][] file = new byte[2][];
		try {
			String[] accessPoint = args[0].split(":");
			String host = null,remoteObjectName;
			if(accessPoint.length == 1)
			{
				remoteObjectName = accessPoint[0];
			}
			else
			{
				host = accessPoint[0];
				remoteObjectName = accessPoint[1];
			}
			Registry registry = LocateRegistry.getRegistry(host);
			MessageRMI stub = (MessageRMI) registry.lookup(remoteObjectName);


			switch(args[1].toUpperCase()) {
			case "BACKUP":
				if(args.length == 4)
				{
					messageArgs = new String[] {args[3]};
					file[0] = Base64.getEncoder().encode(Utilities.fileToBinary(args[2]));
					file[1] = Base64.getEncoder().encode(Utilities.calculateMetadataIdentifier(args[2]));
					break;
				}
				throw new Exception();
			case "RESTORE":
			case "DELETE":
				if(args.length == 3)
				{
					messageArgs = new String[] {};
					file[0] = Base64.getEncoder().encode(Utilities.fileToBinary(args[2]));
					file[1] = Base64.getEncoder().encode(Utilities.calculateMetadataIdentifier(args[2]));
					break;
				}
				throw new Exception();
			case "RECLAIM":
				if(args.length == 3)
				{
					messageArgs = new String[] {args[2]};
					break;
				}
				throw new Exception();
			case "STATE":
				if(args.length == 2)
				{
					messageArgs = new String[] {};
					break;
				}
				throw new Exception();
			default:
				throw new Exception();
			}

			byte[] response = stub.sendMessage(args[1].toUpperCase(), messageArgs, file);

			if(response != null)
			{
				Utilities.binaryToFile(response, args[2]);
			}

			System.out.println("Message transmited successfully");

		} catch (Exception e)
		{
			Client.printUsage();
			return;
		}

	}

	public static void printUsage() {
		System.out.println("\nUsage: Client <peer_ap> <sub_protocol> [args...]\n");
		System.out.println("<peer-ap>\tpeer access point <hostname>:<name of remote object>\n");
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

