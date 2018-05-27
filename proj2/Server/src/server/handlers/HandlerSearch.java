package server.handlers;

import java.net.Socket;
import java.util.ArrayList;

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
		
		String searchTerm = Utils.decode(this.searchTerm);
		ArrayList<String> searchResults = manager.searchTrack(searchTerm);
		if (searchResults.isEmpty()) {
			message = Utils.encode("File is not on server.");
			build.append(0);
		} else {
			String msg = "Encountered file " + searchTerm + " on server as:\n";
			for(String file : searchResults) {
				msg += file + '\n';
			}
			message = Utils.encode(msg.substring(0, msg.length()-1));
			build.append(1);
			
			String notify = "File " + searchTerm + " on from server";
			Notifier notif = new Notifier(username, notify);
			notif.start();
		}
		
		build.append(" ").append(message).append('\0');
		String s = build.toString();
		send(s);
		
		log("Exiting");
	}

}
