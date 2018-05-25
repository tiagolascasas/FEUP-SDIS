package server.handlers;

import java.net.Socket;

import server.Notifier;
import server.ServerManager;
import server.Utils;

public class HandlerSearch extends Handler
{
	String searchTerm;
	
	public HandlerSearch(Socket socket, String username, String password, String searchTerm)
	{
		super("SEARCH", username, password, socket);
		this.searchTerm = searchTerm;	
	}

	@Override
	public void run()
	{
		String message = "";
		log("Searching for file on server");

		ServerManager manager = ServerManager.getInstance();
		StringBuilder build = new StringBuilder();
		build.append("RES_SEARCH ");
				
		byte data[] = manager.getTrack(this.searchTerm);
		if (data == null) {
			message = Utils.encode("File is not on server.");
			build.append(0);
		} else {
			
			message = Utils.encode("Encountered file" + this.searchTerm + "is on server.");
			build.append(1);
			
			String notify = "File " + this.searchTerm + " is on from server";
			Notifier notif = new Notifier(username, notify);
			notif.start();
		}
		
		build.append(" ").append(message).append('\0');
		String s = build.toString();
		send(s);
		
		log("Exiting");
	}

}
