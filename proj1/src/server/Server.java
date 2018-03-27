package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import server.message.Message;

public class Server implements Message {
//TODO IMPORTANT adapt in DispatcherRMI
	public Server() {}

	public String sendMessage(String operation, String[] args) {
		String result = "";
		switch(operation)
		{
		case "BACKUP":
			//TODO call backup method
			break;
		case "RESTORE":
			//TODO call restore method
			break;
		case "DELETE":
			//TODO call delete method
			break;
		case "RECLAIM":
			//TODO call reclaim method
			break;
		case "STATE":
			//TODO call state method
			break;			
		}

		return result; //returns to client
	}

	// To delete when no longer needed
	public static void main(String args[]) {
		Server obj = new Server();
		obj.run();
	}

	public void run() {
		try {
			Message stub = (Message) UnicastRemoteObject.exportObject(this, 0);

			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("Message", stub);

			System.err.println("Server ready");
		}catch (Exception e) {
			System.err.println("An error occured, couldn't start server...");
		}
	}
}