package server.handlers;

import java.net.Socket;
import java.util.Base64;

import server.Notifier;
import server.ServerManager;
import server.Utils;

public class HandlerDownload extends Handler
{

	private String track;

	public HandlerDownload(Socket socket, String username, String password, String track)
	{
		super("DOWNLOAD", username, password, socket);
		this.track = track;

	}

	@Override
	public void run()
	{
		String message = "";
		log("Starting Download of file from server");

		ServerManager manager = ServerManager.getInstance();
		StringBuilder build = new StringBuilder();
		build.append("RES_DOWNLOAD ");
				
		byte data[] = manager.getTrack(this.track);
		if (data == null) {
			message = Utils.encode("Error: File is not on server");
			build.append(0);
		}
		else if(data.length == 0) {
			message = Utils.encode("Error: Counldn't download file from server");
			build.append(0);
		} else {
			
	        String encodedName = new String(Base64.getEncoder().encode(this.track.getBytes()));
	        String encodedFile = new String(Base64.getEncoder().encode(data));
	        
			message = Utils.encode(encodedName + " " + encodedFile);
			build.append(1);

			String notify = "File " + track + " is now downloaded from server";
			Notifier notif = new Notifier(username, notify);
			notif.start();
		}
		
		build.append(" ").append(message).append('\0');
		String s = build.toString();
		send(s);
		
		log("Exiting");
	}

}
