package server.handlers;

import java.net.Socket;
import java.util.Base64;

import server.Notifier;
import server.ServerManager;
import server.Utils;

public class HandlerUpload extends Handler
{
	private String title;
	private String body;

	public HandlerUpload(Socket socket, String username, String password, String title, String body)
	{
		super("UPLOAD", username, password, socket);
		this.title = Utils.decode(title);
		this.body = body;
	}

	@Override
	public void run()
	{
		log("Starting Upload of file");
		
		ServerManager manager = ServerManager.getInstance();
		StringBuilder build = new StringBuilder();
		build.append("RES_UPLOAD ");

		byte[] data = Base64.getDecoder().decode(this.body);
		int res = manager.saveTrack(username, title, data);
				
		String message = "";
		if (res == -1)
		{
			message = Utils.encode("Error: file was already uploaded, possibly by another user");
			build.append(0);
		}
		else if (res == 0) 
		{
			message = Utils.encode("Error: Couldn't upload file to server");
			build.append(0);
		} 
		else
		{
			message = Utils.encode("Successfully uploaded file to server");
			build.append(1);
			
			String notify = "File " + title + " is now saved on server";
			Notifier notif = new Notifier(username, notify);
			notif.start();
		}
		
		build.append(" ").append(message).append('\0');
		String s = build.toString();
		send(s);
		
		log("Exiting");
	}

}
