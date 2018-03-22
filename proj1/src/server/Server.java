package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import server.message.Message;

public class Server implements Message {

	public Server() {}

	public String sendMessage(String operation, String[] args) {
		//TODO Adapt with peer code
		String message = operation;
		for(int i = 0; i<args.length; i++)
		{
			message += "-" + args[i];
		}

		return message;
	}

	public static void main(String args[]) {
		//TODO adapt for peer
		try {
			Server obj = new Server();
			Message stub = (Message) UnicastRemoteObject.exportObject(obj, 0);

			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry(5555);
			registry.bind("Message", stub);

			System.err.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}
}